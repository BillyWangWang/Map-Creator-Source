package billywangwang.main.game.tiles;

import java.awt.Graphics;

import billywangwang.main.game.GameScreen;
import billywangwang.main.tile.TileConstants;

public class WaterTile extends Tile {

	public WaterTile(int xx, int yy) {
		super(TileConstants.ID_WATER, true, xx, yy);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.drawImage(GameScreen.resources.water, x, y, null);
	}

}
