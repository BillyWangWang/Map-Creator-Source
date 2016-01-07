package billywangwang.main.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import billywangwang.main.game.tiles.GrassTile;
import billywangwang.main.game.tiles.PlayerSpawnTile;
import billywangwang.main.game.tiles.Tile;
import billywangwang.main.game.tiles.WaterTile;
import billywangwang.main.game.undo.UndoEvent;
import billywangwang.main.tile.TileConstants;

public class Level {

	public int width, height;

	private double tx = 0.0, ty = 0.0;
	private double scale = 1.0;
	
	private double moveFactor = 1.0;
	private double moveSpeed = 10.0;
	
	private boolean undoing = false;
	private int undoInterval = 150;
	private long undoTimer = System.currentTimeMillis();
	private boolean undoPressed = false;

	private static LinkedList<Tile> tiles = new LinkedList<Tile>();
	
	private PlayerSpawnTile playerSpawn;

	private Brush brush;

	public Level() {
		brush = new Brush();

		width = 32 * 32;
		height = 32 * 32;
		
		playerSpawn = new PlayerSpawnTile(0, 0);
	}

	public void tick(double scale, double tx, double ty) {
		this.scale = scale;
		this.tx = tx;
		this.ty = ty;

		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);
			if(t != null)
				tiles.get(i).tick();
		}

		brush.tick(scale, tx, ty);
		playerSpawn.tick();
		
		if(GameScreen.getKeyInput().isKeyDown(KeyEvent.VK_W))
			GameScreen.moveUp(moveSpeed * moveFactor);
		if(GameScreen.getKeyInput().isKeyDown(KeyEvent.VK_A))
			GameScreen.moveLeft(moveSpeed * moveFactor);
		if(GameScreen.getKeyInput().isKeyDown(KeyEvent.VK_S))
			GameScreen.moveDown(moveSpeed * moveFactor);
		if(GameScreen.getKeyInput().isKeyDown(KeyEvent.VK_D))
			GameScreen.moveRight(moveSpeed * moveFactor);
		
		moveFactor = scale;
		
		if(GameScreen.getKeyInput().isKeyDown(KeyEvent.VK_CONTROL)){			
			if(GameScreen.getKeyInput().isKeyDown(KeyEvent.VK_Z)){				
				if(!undoing && System.currentTimeMillis() - undoTimer > undoInterval * 2){
					undoTimer += undoInterval;
					undoing = true;
				}
				
				if(undoing && System.currentTimeMillis() - undoTimer > undoInterval){
					GameScreen.undo.undo();
					undoTimer += undoInterval;
				}
				
				if(!undoPressed){
					GameScreen.undo.undo();
					undoPressed = true;
				}
			}
		}
		
		if(undoPressed){
			if(!GameScreen.getKeyInput().isKeyDown(KeyEvent.VK_Z)){
				undoing = false;
				undoPressed = false;
			}
		}
		
		if(!undoPressed && System.currentTimeMillis() - undoTimer > undoInterval){
			undoTimer += undoInterval;
		}
		
		moveFactor = 1 / scale;
	}

	public void render(Graphics g) {
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);
			if(t != null){
				t.render(g);
			}
		}
		
		playerSpawn.render(g);
		
		g.setColor(Color.ORANGE);
		g.drawRect(0, 0, width, height);
		brush.render(g);
	}
	
	public Tile createTile(int id, int x, int y){
		switch(id){
		case TileConstants.ID_GRASS:
			return new GrassTile(x, y);
		case TileConstants.ID_PLAYER_SPAWN:
			return new PlayerSpawnTile(x, y);
		case TileConstants.ID_WATER:
			return new WaterTile(x, y);
		}
		
		return null;
	}

	public void save() {
		String fileName = JOptionPane.showInputDialog("Enter a name - ");

		if (fileName != null) {
			StringBuilder string = new StringBuilder();

			for (int i = 0; i < tiles.size(); i++) {
				Tile t = tiles.get(i);

				string.append("/");
				string.append(t.getId());
				string.append("/");
				string.append(t.getX());
				string.append("/");
				string.append(t.getY());
				string.append("/");
				string.append("\n");
			}

			try {
				Path currentRelativePath = Paths.get("");
				
				new File(currentRelativePath.toAbsolutePath().toString() + "\\levels\\" + fileName).mkdirs();
				
				File tileFile = new File(currentRelativePath.toAbsolutePath().toString() + "\\levels\\" + fileName + "\\" + fileName + ".tile");
				File levelFile = new File(currentRelativePath.toAbsolutePath().toString() + "\\levels\\" + fileName + ".level");

				if (!tileFile.exists()) {
					tileFile.createNewFile();
				}
				
				if(!levelFile.exists())
					levelFile.createNewFile();

				FileOutputStream tileOutput = new FileOutputStream(tileFile);

				tileOutput.write(string.toString().getBytes());

				tileOutput.close();
				
				File spawnFile = new File(currentRelativePath.toAbsolutePath().toString() + "\\levels\\" + fileName + "\\" + fileName + ".spawn");
				
				if(!spawnFile.exists()){
					spawnFile.createNewFile();
				}
				
				FileOutputStream spawnOutput = new FileOutputStream(spawnFile);
				
				spawnOutput.write(("/" + playerSpawn.getX() + "/" + playerSpawn.getY() + "/").getBytes());
				
				spawnOutput.close();
				
				FileOutputStream levelOutput = new FileOutputStream(levelFile);
				
				levelOutput.write(("/t" + fileName + ".tile").getBytes());
				levelOutput.write("\n".getBytes());
				levelOutput.write("end".getBytes());
				
				levelOutput.close();

				JOptionPane.showMessageDialog(null, "Saved!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void load() {
		JFileChooser fileChooser = new JFileChooser();
		Path currentRelativePath = Paths.get("");
		new File(currentRelativePath.toAbsolutePath().toString() + "\\levels").mkdirs();
		fileChooser.setCurrentDirectory(new File(currentRelativePath.toAbsolutePath().toString() + "\\levels"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("(GAMENAME) level files", "level"));

		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			LinkedList<Tile> oldTiles = tiles;
			for (int i = 0; i < tiles.size(); i++) {
				tiles.remove(i);
			}

			try {
				String folderName = fileChooser.getSelectedFile().getName().split(".level")[0];
				File tileFile = new File(currentRelativePath.toAbsolutePath().toString() + "\\levels\\" + folderName + "\\" + folderName + ".tile");

				Scanner tileScanner = new Scanner(tileFile);
				
				while(tileScanner.hasNext()){
					String line = tileScanner.nextLine();
					
					String[] args = line.split("/");
					
					GameScreen.getLevel().getTiles().add(GameScreen.getLevel().createTile(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])));
				}
				
				tileScanner.close();
				
				File spawnFile = new File(currentRelativePath.toAbsolutePath().toString() + "\\levels\\" + folderName + "\\" + folderName + ".spawn");
				
				Scanner spawnScanner = new Scanner(spawnFile);
				
				while(spawnScanner.hasNext()){
					String line = spawnScanner.nextLine();
					
					String[] args = line.split("/");
					
					playerSpawn = new PlayerSpawnTile(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				}
				
				spawnScanner.close();
				
				GameScreen.undo.add(new UndoEvent(){
					public void undo(){
						tiles = oldTiles;
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public Tile removeTile(int tx, int ty) {
		for (int i = 0; i < tiles.size(); i++) {
			Tile t = tiles.get(i);
			if(t != null){
				if (t.getX() == tx && t.getY() == ty) {
					tiles.remove(i);
					return t;
				}
			}
		}

		return null;
	}

	public LinkedList<Tile> getTiles() {
		return tiles;
	}
	
	public double getScale(){
		return scale;
	}
	
	public double getTranslateX(){
		return tx;
	}
	
	public double getTranslateY(){
		return ty;
	}
	
	public Brush getBrush(){
		return brush;
	}
	
	public void setPlayerSpawn(PlayerSpawnTile t){
		playerSpawn = t;
	}
	
	public PlayerSpawnTile getPlayerSpawn(){
		return playerSpawn;
	}
}