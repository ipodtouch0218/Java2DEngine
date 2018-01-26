package me.ipodtouch0218.java2dengine.display.sprite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private HashMap<Integer[], GameSprite> cached = new HashMap<>();
	
	private BufferedImage sheet;
	private int lengthX;
	private int lengthY;
	
	public SpriteSheet(String file, int lengthX, int lengthY) {
		try {
			sheet = ImageIO.read(this.getClass().getResourceAsStream("/res/sprites/" + file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.lengthX = lengthX;
		this.lengthY = lengthY;
	}

	//---Getters---//
	public GameSprite getSprite(int x, int y) {
		Integer[] coords = new Integer[]{x,y};
		if (cached.containsKey(coords)) {
			return cached.get(coords).clone();
		}
		
		GameSprite sprite = new GameSprite(sheet.getSubimage(lengthX*x, lengthY*y, lengthX, lengthY));
		
		cached.put(coords, sprite);
		return sprite;
	}

	public int getLengthX() { return lengthX; }
	public int getLengthY() { return lengthY; }
	public BufferedImage getSheet() { return sheet; }
}
