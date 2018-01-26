package me.ipodtouch0218.java2dengine.display.sprite;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameSprite implements Cloneable {

	private BufferedImage originalImage;
	
	private String fileName;
	private BufferedImage image;
	private double xScale = 1, yScale = 1;
	private float transparency = 1f;
	private float rotation = 0;
	
	public GameSprite(String imageName) {
		try {
			originalImage = ImageIO.read(this.getClass().getResourceAsStream("/res/sprites/" + imageName));
			
			fileName = imageName.split("/")[imageName.split("/").length-1];
			
			BufferedImage newImg = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			newImg.getGraphics().drawImage(originalImage, 0, 0, null);
			image = newImg;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GameSprite(BufferedImage image) {
		this.image = image;
		
	}
	
	//---Setters---//
	public void setScale(double x, double y) {
		this.xScale = x;
		this.yScale = y;
	}
	public void setTransparency(float percentage) {
		this.transparency = percentage;
		
		BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, percentage));
		g.drawImage(originalImage, 0, 0, null);
	
		image = newImg;
	}
	public void setRotation(float yaw) {
		rotation = yaw;
	}
	
	//---Getters---//
	public Image getScaledImage() {
		return image.getScaledInstance((int) (image.getWidth()*xScale), (int) (image.getHeight()*yScale), Image.SCALE_FAST);
	}
	public BufferedImage getImage() { 
		Graphics2D g = ((Graphics2D) image.getGraphics());
		g.rotate(Math.toRadians(rotation), image.getWidth()/2, image.getHeight()/2);
		
		return image; 
	}
	public double getScaleX() { return xScale; }
	public double getScaleY() { return yScale; }
	public float getTransparency() { return transparency; }
	public float getRotation() { return rotation; }
	public String getFileName() { return fileName; }
	
	//---Others---//
	public GameSprite clone() {
		try {
			return (GameSprite) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
