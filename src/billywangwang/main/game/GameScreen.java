package billywangwang.main.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import billywangwang.main.Main;
import billywangwang.main.game.input.KeyInput;
import billywangwang.main.game.input.MouseInput;
import billywangwang.main.game.undo.UndoQueue;

@SuppressWarnings("serial")
public class GameScreen extends Canvas implements Runnable {
	
	public static final int WIDTH = Main.WIDTH - 350, HEIGHT = Main.HEIGHT;
	
	public static BufferedImage testImage;
	
	public static UndoQueue undo;
	
	private boolean running = false;
	private Thread thread = new Thread(this, "GameView Thread");
	
	private static MouseInput mouseInput;
	private static KeyInput keyInput;
	
	private double scale = 1.0;
	private double scaleMin = 0.13;
	private double scaleMax = 5;
	private static double tx = 0.0, ty = 0.0;
	
	private static Level level;
	
	private boolean middlePressed = false;
	private int lmx = 0;
	private int lmy = 0;
	
	public GameScreen(){
		Dimension size = new Dimension(Main.WIDTH - 350, Main.HEIGHT);
		setPreferredSize(size);
		
		addMouseMotionListener(mouseInput = new MouseInput());
		addMouseListener(mouseInput);
		addMouseWheelListener(mouseInput);
		addKeyListener(keyInput = new KeyInput());
		
		level = new Level();
		
		try {
			testImage = ImageIO.read(GameScreen.class.getResourceAsStream("/test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tx = 0 + (WIDTH / 8);
		ty = 0 + (HEIGHT / 2);
		
		undo = new UndoQueue();
	}
	
	public void start(){
		running = true;
		thread.start();
	}
	
	public void stop(){
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		long last = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		double delta = 0.0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		int ticks = 0;
		requestFocus();
		while(running){
			long now = System.nanoTime();
			delta += (now - last) / ns;
			last = now;
			while(delta >= 1){
				tick();
				ticks++;
				delta--;
			}
			
			if(frames < ticks){
				render();
				frames++;
			}
			
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(System.currentTimeMillis() - timer > 1000){
				System.out.println(frames + " fps, " + ticks + " ticks");
				ticks = 0;
				frames = 0;
				timer += 1000;
			}
		}
	}
	
	private void tick(){		
//		int val = (targetVal - val) * 0.1;
		if(keyInput.isKeyDown(KeyEvent.VK_Q)){
			scale -= 0.1;
		}
		
		if(keyInput.isKeyDown(KeyEvent.VK_E)){
			scale += 0.1;
		}
		
		if(mouseInput.getMouseWheel() == 1){
			scale -= 0.1;
		}
		
		if(mouseInput.getMouseWheel() == -1){
			scale += 0.1;
		}
		
		if(mouseInput.isButtonDown(MouseEvent.BUTTON2) && !middlePressed){
			lmx = mouseInput.getX();
			lmy = mouseInput.getY();
			middlePressed = true;
		}
		
		if(middlePressed){
			if(!mouseInput.isButtonDown(MouseEvent.BUTTON2))
				middlePressed = false;
			
			tx -= (lmx - mouseInput.getX()) * 0.05;
			ty -= (lmy - mouseInput.getY()) * 0.05;
		}
		
		if(keyInput.isKeyDown(KeyEvent.VK_F)){
			tx = 0;
			ty = 0;
		}
		
		scale = clamp(scale, scaleMin, scaleMax);
		
		level.tick(scale, tx, ty);
		mouseInput.tick();
	}
	
	private double clamp(double val, double min, double max){
		if(val < min)
			return min;
		
		if(val > max)
			return max;
		
		return val;
	}
	
	private void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;			
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		((Graphics2D)g).scale(scale, scale);
		((Graphics2D)g).translate(tx, ty);
		level.render(g);
		g.dispose();
		bs.show();
	}
	
	public static void moveUp(double val){
		ty += val;
	}
	
	public static void moveLeft(double val){
		tx += val;
	}

	public static void moveDown(double val){
		ty -= val;
	}
	
	public static void moveRight(double val){
		tx -= val;
	}
	
	public static MouseInput getMouseInput(){
		return mouseInput;
	}
	
	public static KeyInput getKeyInput(){
		return keyInput;
	}
	
	public static Level getLevel(){
		return level;
	}
}