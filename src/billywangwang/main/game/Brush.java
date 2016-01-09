package billywangwang.main.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import billywangwang.main.game.tiles.PlayerSpawnTile;
import billywangwang.main.game.tiles.Tile;
import billywangwang.main.game.undo.UndoEvent;
import billywangwang.main.graphics.BrushConstants;
import billywangwang.main.tile.TileConstants;

public class Brush {

	private double scale = 1.0;
	private double tx = 0.0, ty = 0.0;

	private int brushIndex = 0;
	private int toolIndex = 0;

	private boolean leftButtonPressed = false;
	private boolean rightButtonPressed = false;
	
	private int floodStartX, floodStartY, floodEndX, floodEndY;
	
	public void tick(double scale, double tx, double ty) {
		this.scale = scale;
		this.tx = tx;
		this.ty = ty;

		int mx = (int) (GameScreen.getMouseInput().getX() / scale);
		int my = (int) (GameScreen.getMouseInput().getY() / scale);
		mx -= tx;
		my -= ty;
		int fmx = (mx / 32);
		int fmy = (my / 32);
		fmx *= 32;
		fmy *= 32;
		
		switch(toolIndex){
		case BrushConstants.BRUSH_PENCIL:
			if (GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON1) && !leftButtonPressed) {
				if (brushIndex != TileConstants.ID_PLAYER_SPAWN) {
					Tile t = GameScreen.getLevel().createTile(brushIndex, fmx, fmy);
					GameScreen.getLevel().getTiles().add(t);
					GameScreen.undo.add(new UndoEvent() {
						public void undo() {
							GameScreen.getLevel().getTiles().remove(t);
						}
					});
				} else {
					int x = GameScreen.getLevel().getPlayerSpawn().getX();
					int y = GameScreen.getLevel().getPlayerSpawn().getY();
					GameScreen.getLevel().setPlayerSpawn(new PlayerSpawnTile(fmx, fmy));
					GameScreen.undo.add(new UndoEvent() {
						public void undo() {
							GameScreen.getLevel().setPlayerSpawn(new PlayerSpawnTile(x, y));
						}
					});
				}
				leftButtonPressed = true;
			}
			
			if(GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON3) && !rightButtonPressed){
				Tile t = GameScreen.getLevel().removeTile(fmx, fmy);
				GameScreen.undo.add(new UndoEvent() {
					public void undo() {
						GameScreen.getLevel().getTiles().add(t);
					}
				});
				rightButtonPressed = true;
			}
			break;
		case BrushConstants.BRUSH_FLOOD:
			if (GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON1) && !leftButtonPressed) {
				floodStartX = fmx;
				floodStartY = fmy;
				floodEndX = fmx;
				floodEndY = fmy;
				leftButtonPressed = true;
			}
			
			if(GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON3) && !rightButtonPressed){
				floodStartX = fmx;
				floodStartY = fmy;
				floodEndX = fmx;
				floodEndY = fmy;
				rightButtonPressed = true;				
			}
			break;
		}

		if (leftButtonPressed) {
			if (!GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON1)){
				if(toolIndex == BrushConstants.BRUSH_FLOOD){
					floodFill();
				}
				leftButtonPressed = false;
			}
			if(toolIndex == BrushConstants.BRUSH_FLOOD){
				floodEndX = fmx + 32;
				floodEndY = fmy + 32;
			}
		}

		if (rightButtonPressed) {
			if (!GameScreen.getMouseInput().isButtonDown(MouseEvent.BUTTON3)) {
				if(toolIndex == BrushConstants.BRUSH_FLOOD){
					floodDelete();
				}
				rightButtonPressed = false;
			}
			if(toolIndex == BrushConstants.BRUSH_FLOOD){
				floodEndX = fmx + 32;
				floodEndY = fmy + 32;
			}
		}
	}

	public void render(Graphics g) {
		int mx = (int) (GameScreen.getMouseInput().getX() / scale);
		int my = (int) (GameScreen.getMouseInput().getY() / scale);
		mx -= tx;
		my -= ty;
		int fmx = (mx / 32);
		int fmy = (my / 32);
		fmx *= 32;
		fmy *= 32;
		if(toolIndex == BrushConstants.BRUSH_PENCIL){
			g.setColor(Color.WHITE);
			g.drawRect(fmx, fmy, 32, 32);
		}
		else if(toolIndex == BrushConstants.BRUSH_FLOOD){
			g.setColor(Color.YELLOW);
			g.drawRect(fmx, fmy, 32, 32);
			
			if(floodEndX < 0) floodEndX *= -1;
			if(floodEndY < 0) floodEndY *= -1;
			
			if(floodStartX != -1){
				g.setColor(Color.WHITE);
				g.drawRect(floodStartX, floodStartY, floodEndX - floodStartX, floodEndY - floodStartY);
			}
		}
	}
	
	private void floodFill(){
		if(brushIndex != TileConstants.ID_PLAYER_SPAWN){
			LinkedList<Tile> tiles = new LinkedList<Tile>();
			for(int x = 0; x < floodEndX - floodStartX; x += 32){
				for(int y = 0; y < floodEndY - floodStartY; y += 32){
					Tile t = GameScreen.getLevel().createTile(brushIndex, floodStartX + x, floodStartY + y);
					tiles.add(t);
					GameScreen.getLevel().getTiles().add(t);
				}
			}
			GameScreen.undo.add(new UndoEvent(){
				public void undo(){
					for(int i = 0; i < tiles.size(); i++){
						GameScreen.getLevel().getTiles().remove(tiles.get(i));
					}
				}
			});
			floodStartX = -1;
			floodStartY = -1;
			floodEndX = -1;
			floodEndY = -1;
		}
		else{
			floodStartX = -1;
			floodStartY = -1;
			floodEndX = -1;
			floodEndY = -1;
		}
	}
	
	public void floodDelete(){
		if(brushIndex != TileConstants.ID_PLAYER_SPAWN){
			LinkedList<Tile> tiles = new LinkedList<Tile>();
			for(int x = 0; x < floodEndX - floodStartX; x += 32){
				for(int y = 0; y < floodEndY - floodStartY; y += 32){
					for(int i = 0; i < GameScreen.getLevel().getTiles().size(); i++){
						Tile t = GameScreen.getLevel().getTiles().get(i);
						
						int xx = floodStartX + x;
						int yy = floodStartY + y;
						
						if(t.getX() == xx && t.getY() == yy){
							tiles.add(t);
							GameScreen.getLevel().getTiles().remove(t);
						}
					}
				}
			}
			
			GameScreen.undo.add(new UndoEvent(){
				public void undo(){
					for(int i = 0; i < tiles.size(); i++){
						GameScreen.getLevel().getTiles().add(tiles.get(i));
					}
				}
			});
			floodStartX = -1;
			floodStartY = -1;
			floodEndX = -1;
			floodEndY = -1;
		}
		else{
			floodStartX = -1;
			floodStartY = -1;
			floodEndX = -1;
			floodEndY = -1;
		}
	}

	public void setBrushType(int id) {
		brushIndex = id;
	}
	
	public void setToolType(int id){
		toolIndex = id;
	}

}
