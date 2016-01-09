package billywangwang.main.game.tiles;

import java.awt.Graphics;

import billywangwang.main.game.GameScreen;
import billywangwang.main.tile.TileConstants;

public class StoneTile extends Tile {

	public StoneTile(int xx, int yy){
		super(TileConstants.ID_STONE, true, xx, yy);
	}
	
	public void tick(){
		
	}
	
	public void render(Graphics g){
		g.drawImage(GameScreen.resources.stone, x, y, null);
	}
	
}
