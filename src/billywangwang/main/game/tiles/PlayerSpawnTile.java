package billywangwang.main.game.tiles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import billywangwang.main.tile.TileConstants;

public class PlayerSpawnTile extends Tile {

	public PlayerSpawnTile(int xx, int yy) {
		super(TileConstants.ID_PLAYER_SPAWN, false, xx, yy);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(new Color(255, 0, 0, 128));
		g.fillRect(x, y, TileConstants.WIDTH, TileConstants.HEIGHT);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		g.setColor(Color.WHITE);
		g.drawString("P", x + (TileConstants.WIDTH / 2), y + (TileConstants.HEIGHT / 2));
	}
}