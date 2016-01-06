package billywangwang.main.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import billywangwang.main.game.tiles.GrassTile;
import billywangwang.main.game.tiles.PlayerSpawnTile;
import billywangwang.main.game.tiles.Tile;
import billywangwang.main.game.undo.UndoEvent;

public class Brush {
	
	private double scale = 1.0;
	private double tx = 0.0, ty = 0.0;
	
	private int brushIndex = 0;
	
	private boolean leftButtonPressed = false;
	private boolean rightButtonPressed = false;
	
	public void tick(double scale, double tx, double ty){
		this.scale = scale;
		this.tx = tx;
		this.ty = ty;
		
		int mx = (int)(GameScreen.getMouseInput().getX() / scale);
		int my = (int)(GameScreen.getMouseInput().getY() / scale);
		mx -= tx;
		my -= ty;
		int fmx = (mx / 32);
		int fmy = (my / 32);
		fmx *= 32;
		fmy *= 32;
		
		if(GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON1) && !leftButtonPressed){
			switch(brushIndex){
			case Tile.ID_GRASS:
				GrassTile grassTile = new GrassTile(fmx, fmy);
				GameScreen.getLevel().getTiles().add(grassTile);
				GameScreen.undo.add(new UndoEvent(){
					public void undo(){
						GameScreen.getLevel().removeTile(grassTile.getX(), grassTile.getY());
					}
				});
				break;
				
			case Tile.ID_PLAYER_SPAWN:
				PlayerSpawnTile playerSpawn = new PlayerSpawnTile(fmx, fmy);
				int x = GameScreen.getLevel().getPlayerSpawn().getX();
				int y = GameScreen.getLevel().getPlayerSpawn().getY();
				GameScreen.getLevel().setPlayerSpawn(playerSpawn);
				GameScreen.undo.add(new UndoEvent(){
					public void undo(){
						GameScreen.getLevel().setPlayerSpawn(new PlayerSpawnTile(x, y));
					}
				});
			}
			leftButtonPressed = true;
		}
		
		if(GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON3) && !rightButtonPressed){
			Tile t = GameScreen.getLevel().removeTile(fmx, fmy);
			GameScreen.undo.add(new UndoEvent(){
				public void undo(){
					GameScreen.getLevel().getTiles().add(t);
				}
			});
			rightButtonPressed = true;
		}
		
		if(leftButtonPressed){
			if(!GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON1))
				leftButtonPressed = false;
		}
		
		if(rightButtonPressed){
			if(!GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON3)){
				rightButtonPressed = false;
			}
		}		
	}
	
	public void render(Graphics g){
		int mx = (int)(GameScreen.getMouseInput().getX() / scale);
		int my = (int)(GameScreen.getMouseInput().getY() / scale);
		mx -= tx;
		my -= ty;
		int fmx = (mx / 32);
		int fmy = (my / 32);
		fmx *= 32;
		fmy *= 32;
		g.setColor(Color.WHITE);
		g.drawRect(fmx, fmy, 32, 32);
	}
	
	public void setBrushType(int id){
		brushIndex = id;
	}

}
