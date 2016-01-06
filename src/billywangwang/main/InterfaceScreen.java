package billywangwang.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import billywangwang.main.game.GameScreen;
import billywangwang.main.game.undo.UndoEvent;

@SuppressWarnings("serial")
public class InterfaceScreen extends JPanel {

	private JList<String> optionsList;
	private JButton saveButton;
	private JButton loadButton;
	
	public InterfaceScreen(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}		
		setLayout(null);
		setSize(Main.WIDTH - (Main.WIDTH - 350), Main.HEIGHT);
		int panelWidth = getWidth();
		//int panelHeight = getHeight();
		
		//Options
		Border defaultBorder = BorderFactory.createLineBorder(new Color(0, 75, 0), 3, true);
		
		String[] options = {"Change Size", "Add Event"};
		optionsList = new JList<String>(options);
		DefaultListCellRenderer renderer = (DefaultListCellRenderer)optionsList.getCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		optionsList.setBorder(defaultBorder);
		optionsList.setBounds(10, 11, panelWidth - 20, 100);
		optionsList.addListSelectionListener(new ListSelectionListener(){;
			public void valueChanged(ListSelectionEvent e){
				if(optionsList.getSelectedIndex() == 0){
					String w1 = JOptionPane.showInputDialog("Enter the width - ");
					if(w1 != null){
						String h1 = JOptionPane.showInputDialog("Enter the height - ");
						if(h1 != null){
							int width = Integer.parseInt(w1);
							int height = Integer.parseInt(h1);
							
							int w = GameScreen.getLevel().width;
							int h = GameScreen.getLevel().height;
							GameScreen.getLevel().width = width * 32;
							GameScreen.getLevel().height = height * 32;
							GameScreen.undo.add(new UndoEvent(){
								public void undo(){
									GameScreen.getLevel().width = w;
									GameScreen.getLevel().height = h;
								}
							});
						}
					}
				}
				optionsList.clearSelection();
				Main.getGameScreen().requestFocus();
			}
		});
		
		saveButton = new JButton("Save");
		saveButton.setBounds(10, 115, panelWidth - 20, 30);
		saveButton.setBorder(defaultBorder);
		saveButton.setBackground(Color.WHITE);
		saveButton.setContentAreaFilled(false);
		saveButton.setOpaque(true);
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GameScreen.getLevel().save();
			}
		});
		
		loadButton = new JButton("Load");
		loadButton.setBounds(10, 149, panelWidth - 20, 30);
		loadButton.setBorder(defaultBorder);
		loadButton.setBackground(Color.WHITE);
		loadButton.setContentAreaFilled(false);
		loadButton.setOpaque(true);
		loadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GameScreen.getLevel().load();
			}
		});
		
		add(optionsList);
		add(saveButton);
		add(loadButton);
	}
	
}
