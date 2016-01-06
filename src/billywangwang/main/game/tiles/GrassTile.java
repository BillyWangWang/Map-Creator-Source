package billywangwang.main.game.tiles;

import java.awt.Graphics;

import billywangwang.main.game.GameScreen;

public class GrassTile extends Tile {

	public GrassTile(int xx, int yy) {
		super(Tile.ID_GRASS, xx, yy);
	}

	public void tick() {
		if(x < 0 || y < 0 || x > GameScreen.getLevel().width || y > GameScreen.getLevel().height)
			GameScreen.getLevel().getTiles().remove(this);
	}

	public void render(Graphics g) {
		g.drawImage(GameScreen.testImage, (int)x, (int)y, null);
	}
}