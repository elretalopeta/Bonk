package com.flx.bonk;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapSet {
	private Bitmap[] bitmaps;
	
	private int[][] sheetInfo = {
			{ 0, 66, 24, 32, 0 },	//  0: Bonk walking right 1
			{ 32, 66, 24, 32, 0 },	//  1: Bonk walking right 2
			{ 63, 66, 24, 32, 0 },	//  2: Bonk walking right 3
			{ 93, 66, 24, 32, 0 },	//  3: Bonk walking right 4
			{ 405, 66, 28, 32, 0 }, 	//  4: Bonk jumping right
			{ 163, 60, 24, 32, 0 }, 	//  5: Bonk falling right
			{ 0, 66, 24, 32, 1 },	//  6: Bonk walking left 1
			{ 32, 66, 24, 32, 1 },	//  7: Bonk walking left 2
			{ 63, 66, 24, 32, 1 },	//  8: Bonk walking left 3
			{ 93, 66, 24, 32, 1 },	//  9: Bonk walking left 4
			{ 405, 66, 28, 32, 1 }, 	// 10: Bonk jumping left
			{ 163, 60, 24, 32, 1 },	// 11: Bonk falling left
			{ 133, 16, 28, 32, 0 },	// 12: Bonk standing 1
			{ 165, 16, 28, 32, 0 },	// 13: Bonk standing 2
			{ 196, 16, 28, 32, 0 },	// 14: Bonk standing 3
			{ 0, 256, 28, 32, 0 },	// 15: Bonk jumping/falling front
			{ 80, 265, 32, 30, 0 },	// 16: Enemy up 1
			{ 120, 265, 32, 30, 0 },	// 17: Enemy up 2
			{ 162, 265, 32, 30, 0 },	// 18: Enemy down 1
			{ 199, 265, 32, 30, 0 },	// 19: Enemy down 2
			{ 237, 265, 32, 30, 0 },	// 20: Enemy front

			{ 316, 300, 16, 16, 0 },	// 21: Cloud left
			{ 332, 300, 16, 16, 0 },	// 22: Cloud right
			{ 348, 300, 16, 16, 0 },	// 23: Empty sky
			{ 364, 300, 16, 16, 0 },	// 24: Sky semi
			{ 380, 300, 16, 16, 0 },	// 25: Sky dark
			{ 396, 300, 16, 16, 0 },	// 26: Plant left
			{ 412, 300, 16, 16, 0 },	// 27: Plant center
			{ 428, 300, 16, 16, 0 },	// 28: Plant right
			{ 302, 318, 12, 12, 0 },	// 29: Coin 1
			{ 318, 318, 12, 12, 0 },	// 30: Coin 2
			{ 334, 318, 12, 12, 0 },	// 31: Coin 3
			{ 350, 318, 12, 12, 0 },	// 32: Coin 4
			{ 366, 318, 12, 12, 0 },	// 33: Coin 5
			{ 380, 316, 16, 16, 0 },	// 34: Plant single
			{ 396, 316, 16, 16, 0 },	// 35: Platform left
			{ 412, 316, 16, 16, 0 },	// 36: Platform center
			{ 428, 316, 16, 16, 0 },	// 37: Platform right
			{ 300, 332, 16, 16, 0 },	// 38: Box
			{ 316, 332, 16, 16, 0 },	// 39: Plant A
			{ 332, 332, 16, 16, 0 },	// 40: Column
			{ 348, 332, 16, 16, 0 },	// 41: Plant B
			{ 364, 332, 16, 16, 0 },	// 42: Ground single
			{ 380, 332, 16, 16, 0 },	// 43: Plant C
			{ 396, 332, 16, 16, 0 },	// 44: Ground left
			{ 412, 332, 16, 16, 0 },	// 45: Ground center
			{ 428, 332, 16, 16, 0 },	// 46: Ground right	
			
			{ 0, 212, 32, 32, 0 },	// 47: Bonk dying 1
			{ 37, 212, 28, 32, 0 },	// 48: Bonk dying 2
			{ 71, 212, 28, 32, 0 },	// 49: Bonk dying 3
			{ 107, 212, 28, 32, 0 },	// 50: Bonk dying 4
			{ 140, 212, 28, 32, 0 },	// 51: Bonk dying 5

			//x, y,anchura, altura, posicion
			{ 86, 263, 28, 35, 0 },  //52: bomba capa 1
			{ 125, 263, 30, 35, 0 }, //53: bomba capa 2
			{ 164, 263, 32, 35, 0 }, //54: bomba capa 3
			{ 206, 263, 28, 38, 0 }, //55: bomba capa 4
			{ 247, 263, 26, 68, 0 }, //56: bomba capa 5
			{ 286, 263, 28, 38, 0 }, //57: bomba capa 6
			{ 324, 263, 32, 35, 0 }, //58: bomba capa 7
			{ 365, 263, 30, 35, 0 }  //59: bomba capa 8
	};
	
	public Bitmap getBitmap(int i) { return bitmaps[i]; }
	
	public BitmapSet(Resources res) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;

		Bitmap bitmapsBMP = BitmapFactory.decodeResource(res, R.raw.bonk, opts);
		Matrix rot1 = new Matrix();
		Matrix rot2 = new Matrix();
		rot2.setScale(-1, 1);
		bitmaps = new Bitmap[sheetInfo.length];
		for (int i = 0; i < sheetInfo.length; i++) {
			int x = sheetInfo[i][0];
			int y = sheetInfo[i][1];
			int w = sheetInfo[i][2];
			int h = sheetInfo[i][3];
			boolean mustRotate = (sheetInfo[i][4] == 1);
			bitmaps[i] = Bitmap.createBitmap(bitmapsBMP, x, y, w, h, 
					mustRotate?rot2:rot1, true);
		}
		bitmapsBMP.recycle();
	}
}
