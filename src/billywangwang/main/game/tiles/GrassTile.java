package billywangwang.main.game.tiles;

import java.awt.Graphics;

import billywangwang.main.game.GameScreen;
import billywangwang.main.tile.TileConstants;

public class GrassTile extends Tile {

	public GrassTile(int xx, int yy) {
		super(TileConstants.ID_GRASS, true, xx, yy);
	}

	public void tick() {
		if(x < 0 || y < 0 || x > GameScreen.getLevel().width || y > GameScreen.getLevel().height)
			GameScreen.getLevel().getTiles().remove(this);
	}

	public void render(Graphics g) {
		g.drawImage(GameScreen.resources.grass, (int)x, (int)y, null);
	}
}