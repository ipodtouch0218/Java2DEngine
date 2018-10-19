package me.ipodtouch0218.java2dengine.display.sprite;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.ipodtouch0218.java2dengine.display.GameRenderer;

public class GameSprite implements Cloneable {

	//TODO: everything
	
	private VolatileImage originalImage;
	
	private String fileName;
	private VolatileImage image;
	
	private double setScaleX = 1, setScaleY = 1;
	private float setTransparency = 1;
	private double setRotation = 0;
	
	
	public GameSprite(String imageName) {
		try {
			originalImage = toVolatile(ImageIO.read(this.getClass().getResourceAsStream("/res/sprites/" + imageName)));
			fileName = imageName.split("/")[imageName.split("/").length-1];

			image = originalImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GameSprite(BufferedImage image) {
		this.originalImage = toVolatile(image);
		this.image = toVolatile(image);
	}
	public GameSprite(VolatileImage image) {
		this.originalImage = toVolatile(image.getSnapshot());
		this.image = toVolatile(image.getSnapshot());
	}

	//---Methods---//
	public void rotate(double amount) {
	    setRotation(setRotation+amount);
	}
	public void setRotation(double amount) {
		setRotation = amount;
		
		double toRotate = Math.toRadians(setRotation);
		AffineTransform tx = AffineTransform.getRotateInstance(toRotate, (image.getWidth() / 2), (image.getHeight() / 2));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);         
		image = toVolatile(op.filter(originalImage.getSnapshot(), null));
	}
 	public void flipHorizontally() {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = toVolatile(op.filter(image.getSnapshot(), null));
	}
	public void flipVertically() {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = toVolatile(op.filter(image.getSnapshot(), null));
	}
	
	//---Setters---//
	public void setScale(double x, double y) {
		this.setScaleX = x;
		this.setScaleY = y;
	}
	public void setTransparency(float percentage) {
		this.setTransparency = percentage;
		
		VolatileImage newImg = GameRenderer.createVolatile(image.getWidth(), image.getHeight(), true);
		
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, percentage));
		newImg.getGraphics().drawImage(image, 0, 0, null);
	
		image = newImg;
	}
	
	//---Getters---//
	public VolatileImage getImage() {
		/*
		boolean newImage = false; //if the image was replaced
		if (setRotation != actualRotation || setTransparency != actualTransparency ||
				setScaleX != actualScaleX || setScaleY != actualScaleY) {
			newImage = true;
		}
		
		if (newImage) {
			image = deepCopyImage(originalImage);
			
			double toRotate = Math.toRadians(setRotation-actualRotation);
			AffineTransform tx = AffineTransform.getRotateInstance(toRotate, (image.getWidth() / 2), (image.getHeight() / 2));
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);         
			op.filter(originalImage, image);
			
			
			
			//TODO: transparency?
			
			actualRotation = setRotation;
			actualTransparency = setTransparency;
			actualScaleX = setScaleX;
			actualScaleY = setScaleY;
		}
		*/
		return image; 
	}
	public double getScaleX() { return setScaleX; }
	public double getScaleY() { return setScaleY; }
	public float getTransparency() { return setTransparency; }
	public double getRotation() { return setRotation; }
	public String getFileName() { return fileName; }
	
	//---what---//
	public static VolatileImage toVolatile(BufferedImage image) {
		VolatileImage newImg = GameRenderer.createVolatile(image.getWidth(), image.getHeight(), image.getType() == BufferedImage.TYPE_INT_ARGB);
		newImg.createGraphics().drawImage(image, 0, 0, null);
		return newImg;
	}
	public static BufferedImage createCompatibleImage(int width, int height, int type) {
		return GameRenderer.gc.createCompatibleImage(width, height, type);
	}
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
