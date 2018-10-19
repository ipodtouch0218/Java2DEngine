package me.ipodtouch0218.java2dengine.util;

import java.lang.reflect.Array;

public class RandomUtils {

	public static <A,V> boolean arrayContains(A[] array, V value) {
		if (array.getClass() != value.getClass()) { return false; }
		for (A entry : array) {
			if (entry.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> T[] addArrays(T[] a, T[] b) {
	    int aLen = a.length;
	    int bLen = b.length;

	    @SuppressWarnings("unchecked")
	    T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
	    System.arraycopy(a, 0, c, 0, aLen);
	    System.arraycopy(b, 0, c, aLen, bLen);

	    return c;
	}
}
