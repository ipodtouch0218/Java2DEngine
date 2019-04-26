package me.ipodtouch0218.java2dengine.util;

public class Vector2D implements Cloneable {

	private double dx = 0, dy = 0;
	
	
	//---CONSTRUCTORS---//
	public Vector2D() {}
	public Vector2D(double x, double y) {
		dx = x;
		dy = y;
	}
	public Vector2D(double magnitude, float angle) {
		dx = 1; dy = 1;
		normalize();
		setMagnitude(magnitude);
		setAngle(angle);
	}
	
	//---BASIC GETTERS AND SETTERS---//
	public double getX() { return dx; }
	public double getY() { return dy; }
	
	public Vector2D setX(double x) { 
		dx = x;
		return this;
	}
	public Vector2D setY(double y) {
		dy = y;
		return this;
	}
	
	//---SIMPLE MATH---//
	
	public Vector2D add(Vector2D v) {
		dx+=v.dx;
		dy+=v.dy;
		
		return this;
	}
	
	public Vector2D add(double x, double y) {
		dx+=x;
		dy+=y;
		
		return this;
	}
	
	public Vector2D subtract(Vector2D v) {
		dx-=v.dx;
		dy-=v.dy;
		
		return this;
	}
	public Vector2D subtract(double x, double y) {
		dx-=x;
		dy-=y;
		
		return this;
	}
	
	public Vector2D multiply(double m) {
		dx*=m;
		dy*=m;
		
		return this;
	}
	
	public Vector2D multiply(Vector2D vec) {
		dx*=vec.dx;
		dy*=vec.dy;
		
		return this;
	}
	
	public Vector2D divide(double d) {
		dx/=d;
		dy/=d;
		
		return this;
	}
	
	public Vector2D divide(Vector2D v) {
		dx/=v.dx;
		dy/=v.dy;
		
		return this;
	}
	
	//---SELF PROPERTIES---//
		//getters//
	
	public float getAngleDegrees() {
		float angle = (float) Math.toDegrees(getAngleRadians())%360;
			
		return angle;
	}	
	
	public float getAngleRadians() {
		return (float) Math.atan2(dx,-dy);
	}
	
	public double getMagnitude() {
		return Math.sqrt(((dx*dx) + (dy*dy)));
	}
	
	//setters//
	
	public Vector2D setAngle(float angle) {
		double mag = getMagnitude();
		dx = mag*Math.cos(Math.toRadians(angle));
		dy = mag*Math.sin(Math.toRadians(angle));
		
		return this;
	}
	
	public Vector2D setMagnitude(double magnitude) {
		normalize();
		multiply(magnitude);
		
		return this;
	}
	
	//---COMPARATIVE PROPERTIES---//
	
	public float getAngleBetween(Vector2D target) {
		double changeX = target.getX() - dx;
		double changeY = target.getY() - dy;
		
		return (float) Math.toDegrees(Math.atan2(-changeY, changeX))%360;
	}
	
	public double getDistanceBetween(Vector2D endPoint) {
		double changeX = endPoint.dx-dx;
		double changeY = endPoint.dy-dy;
		return Math.sqrt((changeX*changeX) + (changeY*changeY));
	}
	
	//---FUNCTIONS---//
	
	public Vector2D normalize() {
		double mag = getMagnitude();
		dx /= mag;
		dy /= mag;
		
		return this;
	}
	
	//---OTHERS---//
	public Vector2D copy() {
		try {
			return (Vector2D) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "Vector2D[dx="+dx+", dy="+dy+", mag="+getMagnitude()+", angle="+getAngleDegrees()+"]";
	}
}
