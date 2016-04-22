package com.flx.bonk;

import android.graphics.Rect;

public class Bonk {
	public int x = 0, y = 0, vy = 0, st = 0, sp = 0;

	private int[][] NEWSTATES = {
			{ 0, 7, 3, 0, 1, 8, 2, 1 },	// 0 = STANDING STILL
			{ 1, 8, 2, 1, 1, 8, 2, 1 },	// 1 = JUMP UP
			{ 1, 8, 2, 2, 2, 8, 2, 2 },	// 2 = JUMP RIGHT
			{ 0, 7, 3, 0, 1, 8, 2, 1 },	// 3 = WALK RIGHT
			{ 5, 6, 4, 4, 4, 6, 4, 4 },	// 4 = FALL RIGHT
			{ 5, 6, 4, 5, 5, 6, 4, 5 },	// 5 = FALL DOWN
			{ 5, 6, 4, 6, 6, 6, 4, 4 },	// 6 = FALL LEFT
			{ 0, 7, 3, 0, 1, 8, 2, 1 },	// 7 = WALK LEFT
			{ 1, 8, 2, 8, 8, 8, 2, 8 },	// 8 = JUMP LEFT
			{ 9, 9, 9, 9, 9, 9, 9, 9 },	// 9 = KILLED!
	};

	private int[][] BONK_SPRITES = {
			{ 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14, 13, 13, 13, 13 },
			{ 15 },
			{ 4 },
			{ 0, 1, 2, 3 },
			{ 5 },
			{ 15 },
			{ 11 },
			{ 6, 7, 8, 9 },
			{ 10 },
			{ 47, 48, 49, 50, 51, 50, 49, 48 }
	};
	
	public Bonk(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void update(long delta, int action) {
		int newst = NEWSTATES[st][action];
		if ((newst == 6) || (newst == 7) || (newst == 8)) {
			x -= 4;
			if (x < 0) x = 0;
		}
		if ((newst == 2) || (newst == 3) || (newst == 4)) {
			x += 4;
			if (x > 500*16-24) x = 500*16-24;
		}
		if (((st == 0) || (st == 3) || (st == 7)) && 
				((newst == 1) || (newst == 8) || (newst == 2))) {
			vy = -10;
		}

		if (vy > 0) {
			if ((newst == 8) || (newst == 7)) newst = 6;
			if ((newst == 1) || (newst == 0)) newst = 5;
			if ((newst == 2) || (newst == 3)) newst = 4;
		}

		y += vy;
		
		if (st != newst) {
			setState(newst);
		}
		else {
			int max = BONK_SPRITES[st].length;
			sp++; if (sp == max) sp = 0;
		}
	}
	
	public void setState(int newst) {
		if (st != newst) {
			st = newst;
			sp = 0;			
		}
	}
	
	public int getSprite() {
		int sprite = BONK_SPRITES[st][sp];
		return sprite;
	}
	
	private Rect bounds = new Rect();
	public Rect getBounds() {
		bounds.left = x + 4;
		bounds.right = x + 20;
		bounds.top = y - 32;
		bounds.bottom = y;
		return bounds;
	}

}
