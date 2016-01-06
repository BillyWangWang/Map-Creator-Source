package billywangwang.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import billywangwang.main.game.GameScreen;

public class Main {
	
	public static final int WIDTH = 1500, HEIGHT = 900;

	private static GameScreen gameView;
	private static InterfaceScreen interfaceView;
	
	public static void main(String[] args){
		gameView = new GameScreen();
		interfaceView = new InterfaceScreen();
		
		JFrame frame = new JFrame();
		
		frame.setResizable(false);
		frame.setContentPane(interfaceView);
		frame.setLayout(new BorderLayout());
		frame.add(gameView, BorderLayout.EAST);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		gameView.start();
	}
	
	public static GameScreen getGameScreen(){
		return gameView;
	}
	
	public static InterfaceScreen getInterfaceScreen(){
		return interfaceView;
	}
}