package com.flx.bonk;

import android.graphics.Rect;

public class Coin {
	int x, y;
	int sp;
	private int[] sprites = { 29, 30, 31, 32, 33 };

	public Coin(int x, int y) {
		this.x = x;
		this.y = y;
		this.sp = (int)(Math.random() * sprites.length);
	}
	
	private int tick;
	public int nextSprite() {
		tick++; 
		if (tick % 3 == 0) {
			tick = 0;
			sp = (sp + 1) % sprites.length;
		}
		return sprites[sp];
	}

	private Rect bounds = new Rect();
	public Rect getBounds() {
		bounds.left = x;
		bounds.right = x + 12;
		bounds.top = y - 12;
		bounds.bottom = y;
		return bounds;
	}
}

