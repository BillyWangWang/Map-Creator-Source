package billywangwang.main.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import billywangwang.main.game.tiles.PlayerSpawnTile;
import billywangwang.main.game.tiles.Tile;
import billywangwang.main.game.undo.UndoEvent;
import billywangwang.main.tile.TileConstants;

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
			if(brushIndex != TileConstants.ID_PLAYER_SPAWN){
				Tile t = GameScreen.getLevel().createTile(brushIndex, fmx, fmy);
				GameScreen.getLevel().getTiles().add(t);
				GameScreen.undo.add(new UndoEvent(){
					public void undo(){
						GameScreen.getLevel().getTiles().remove(t);
					}
				});
			}
			else{
				int x = GameScreen.getLevel().getPlayerSpawn().getX();
				int y = GameScreen.getLevel().getPlayerSpawn().getY();
				GameScreen.getLevel().setPlayerSpawn(new PlayerSpawnTile(fmx, fmy));
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
