package billywangwang.main.game.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	public BufferedImage spriteSheet;
	
	public SpriteSheet(String path){
		try{
			spriteSheet = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public BufferedImage getImage(int col, int row, int w, int h){
		return spriteSheet.getSubimage(col * w, row * h, w, h);
	}
	
	public BufferedImage getSpriteSheetImage(){
		return spriteSheet;
	}

}
