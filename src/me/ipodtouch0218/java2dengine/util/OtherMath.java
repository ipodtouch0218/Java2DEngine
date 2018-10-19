package me.ipodtouch0218.java2dengine.util;

public class OtherMath {

	public static float smootherstep(float x) {
		return x*x*x*(x*(x*6.0f - 15.0f) + 10.0f); 
	}
	
}
