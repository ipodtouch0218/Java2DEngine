package me.ipodtouch0218.java2dengine.display.sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private HashMap<Integer[], GameSprite> cached = new HashMap<>();
	
	private String file;
	private BufferedImage sheet;
	private int lengthX;
	private int lengthY;
	
	public SpriteSheet(String file, int lengthX, int lengthY) {
		this.file = file;
		load();
		this.lengthX = lengthX;
		this.lengthY = lengthY;
	}

	//---Getters---//
	public GameSprite getSprite(int x, int y) {
		if (sheet == null) { return null; }
		for (Entry<Integer[],GameSprite> cachedSprites : cached.entrySet()) {
			Integer[] coords = cachedSprites.getKey();
			if (coords[0] == x && coords[1] == y) {
				return cachedSprites.getValue();
			}
		}
		
		GameSprite sprite = new GameSprite(sheet.getSubimage(lengthX*x, lengthY*y, lengthX, lengthY));
		
		
		cached.put(new Integer[]{x,y}, sprite);
		return sprite;
	}
	public GameSprite getSpriteRange(int startX, int startY, int endX, int endY) {
		BufferedImage collectedImage = GameSprite.createCompatibleImage((endX-startX+1)*lengthX, (endY-startY+1)*lengthY, BufferedImage.TYPE_INT_ARGB);
		Graphics2D collectedGraphics = collectedImage.createGraphics();
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				collectedGraphics.drawImage(getSprite(x, y).getImage(), (x-startX)*lengthX, (y-startY)*lengthY, null);
			}
		}
		collectedGraphics.dispose();
		return new GameSprite(collectedImage);
	}
	
	public void close() {
		sheet = null;
	}
	public void load() {
		try {
			sheet = ImageIO.read(this.getClass().getResourceAsStream("/res/sprites/" + file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getLengthX() { return lengthX; }
	public int getLengthY() { return lengthY; }
	public BufferedImage getSheet() { return sheet; }
}
