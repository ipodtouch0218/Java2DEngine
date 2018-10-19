package me.ipodtouch0218.java2dengine.display.ui.events;

import me.ipodtouch0218.java2dengine.object.hitbox.UIHitbox;

public abstract class UIEventHandler {
	
	private UIHitbox hitbox;
	public UIEventHandler(UIHitbox hitbox) {
		this.hitbox = hitbox;
	}
	
	//---events---//
	public void mouseClickEvent(float mouseX, float mouseY, int button) {}
	public void mouseHoverEvent(float mouseX, float mouseY) {}
	public void mouseUnHoverEvent(float mouseX, float mouseY) {}
	
	//-----//
	//getters
	public UIHitbox getHitbox() {
		return hitbox; 
	}
	
	//setters
	public void setHitbox(UIHitbox newHitbox) {
		this.hitbox = newHitbox;
	}
}
