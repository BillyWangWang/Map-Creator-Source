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
import billywangwang.main.graphics.BrushConstants;
import billywangwang.main.tile.TileConstants;

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
		
		String[] options = {"Change Size", "Change Tile", "Change Tool"};
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
				else if(optionsList.getSelectedIndex() == 1){
					Object[] tiles = {"Grass", "Player Spawn", "Water", "Desert", "Stone"};
					String s = (String)JOptionPane.showInputDialog(null, "Tiles", "Input", JOptionPane.PLAIN_MESSAGE, null, tiles, "Tile");
					if(s != null && !s.equals("Tile")){
						switch(s){
						case "Grass":
							GameScreen.getLevel().getBrush().setBrushType(TileConstants.ID_GRASS);
							break;
						case "Player Spawn":
							GameScreen.getLevel().getBrush().setBrushType(TileConstants.ID_PLAYER_SPAWN);
							break;
						case "Water":
							GameScreen.getLevel().getBrush().setBrushType(TileConstants.ID_WATER);
							break;
						case "Desert":
							GameScreen.getLevel().getBrush().setBrushType(TileConstants.ID_DESERT);
							break;
						case "Stone":
							GameScreen.getLevel().getBrush().setBrushType(TileConstants.ID_STONE);
							break;
						}
					}
				}
				else if(optionsList.getSelectedIndex() == 2){
					Object[] tools = {"Pencil", "Rectangle"};
					String tool = (String)JOptionPane.showInputDialog(null, "Tools", "Input", JOptionPane.PLAIN_MESSAGE, null, tools, "Tool");
					if(tool != null){
					switch(tool){
						case "Pencil":
							GameScreen.getLevel().getBrush().setToolType(BrushConstants.BRUSH_PENCIL);
							break;
						case "Rectangle":
							GameScreen.getLevel().getBrush().setToolType(BrushConstants.BRUSH_FLOOD);
							break;
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
