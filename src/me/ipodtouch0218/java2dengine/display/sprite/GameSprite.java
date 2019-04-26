package me.ipodtouch0218.java2dengine.display.sprite;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.ipodtouch0218.java2dengine.display.GameRenderer;

public class GameSprite implements Cloneable {

	//TODO: everything
	
	private VolatileImage originalImage;
	
	private String fileName;
	private VolatileImage image;
	
	private double setScaleX = 1, setScaleY = 1;
	private double setRotation = 0;
	private boolean transparency = false;
	
	public GameSprite(String imageName, boolean transparency) {
		this.transparency = transparency;
		try {
			originalImage = toVolatile(ImageIO.read(this.getClass().getResourceAsStream("/res/sprites/" + imageName)), transparency);
			fileName = imageName.split("/")[imageName.split("/").length-1];
			image = originalImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GameSprite(BufferedImage image) {
		this(image, image.getTransparency() == BufferedImage.TRANSLUCENT);
	}
	public GameSprite(BufferedImage image, boolean transparent) {
		transparency = transparent;
		
		this.originalImage = toVolatile(image, transparency);
		this.image = toVolatile(image, transparency);
	}
	public GameSprite(VolatileImage image) {
		this.originalImage = image;
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
		image = toVolatile(op.filter(originalImage.getSnapshot(), null), transparency);
	}
 	public void flipHorizontally() {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = toVolatile(op.filter(image.getSnapshot(), null), transparency);
	}
	public void flipVertically() {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = toVolatile(op.filter(image.getSnapshot(), null), transparency);
	}
	
	//---Setters---//
	public void setScale(double x, double y) {
		this.setScaleX = x;
		this.setScaleY = y;
	}
	
	//---Getters---//
	public VolatileImage getImage() {
		return image; 
	}
	public double getScaleX() { return setScaleX; }
	public double getScaleY() { return setScaleY; }
	public double getRotation() { return setRotation; }
	public String getFileName() { return fileName; }
	
	
	//---what---//
	public static VolatileImage toVolatile(BufferedImage image, boolean transparency) {
		VolatileImage newImg = GameRenderer.createVolatile(image.getWidth(), image.getHeight(), transparency);
		newImg.createGraphics().drawImage(image, 0, 0, null);
		return newImg;
	}
	public static BufferedImage createCompatibleImage(int width, int height, int type) {
		return GameRenderer.gc.createCompatibleImage(width, height, type);
	}
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
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
