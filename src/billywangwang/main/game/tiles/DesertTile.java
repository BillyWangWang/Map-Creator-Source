package billywangwang.main.game.tiles;

import java.awt.Graphics;

import billywangwang.main.game.GameScreen;
import billywangwang.main.tile.TileConstants;

public class DesertTile extends Tile {

	public DesertTile(int xx, int yy) {
		super(TileConstants.ID_DESERT, true, xx, yy);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.drawImage(GameScreen.resources.desert, x, y, null);
	}

}
