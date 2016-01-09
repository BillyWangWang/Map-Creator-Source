package billywangwang.main.game.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;

import billywangwang.main.game.GameScreen;
import billywangwang.main.tile.TileConstants;

public abstract class Tile {
	
	protected int id;
	protected int x, y;
	
	public Tile(int id, boolean init, int xx, int yy){
		this.id = id;
		x = xx;
		y = yy;
		
		if(init){
			for(int i = 0; i < GameScreen.getLevel().getTiles().size(); i++){
				Tile t = GameScreen.getLevel().getTiles().get(i);
				
				if(t != null){			
					if(t.getX() == x && t.getY() == y){
						GameScreen.getLevel().getTiles().remove(this);
					}
				}
			}
		}
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	public Rectangle getBounds(){
		return new Rectangle(x, y, TileConstants.WIDTH, TileConstants.HEIGHT);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}