package me.ipodtouch0218.java2dengine.display.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class UICanvas {

	private ArrayList<UIElement> elements = new ArrayList<UIElement>();
	
	public void render(Graphics2D g) {
		for (UIElement element : elements) {
			if (element.isRendering()) {
				element.render(g);
			}
		}
	}
	
	//-----//
	public void addElement(UIElement element) {
		if (!elements.contains(element)) {
			elements.add(element);
		}
	}
	public void removeElement(UIElement element) {
		elements.remove(element);
	}
	
	
}
