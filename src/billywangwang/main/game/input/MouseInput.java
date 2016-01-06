package billywangwang.main.game.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseInput implements MouseMotionListener, MouseListener, MouseWheelListener{

	private boolean[] buttons = new boolean[20];
	
	private int x, y;
	private int wheel = 0;
	
	private long timer = System.currentTimeMillis();
	
	public void tick(){
		if(System.currentTimeMillis() - timer > 1){
			wheel = 0;
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		wheel = e.getWheelRotation();
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
	}

	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getMouseWheel(){
		return wheel;
	}
	
	public boolean isButtonDown(int button){
		return buttons[button];
	}
}