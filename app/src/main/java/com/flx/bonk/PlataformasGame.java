package com.flx.bonk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;

public class PlataformasGame extends Game {

	// 50ms : 1000/50 = 20fps
	@Override public int getDesiredDeltaTime() { return 50; }

	private final static int DESIRED_HEIGHT = Scene.SCENE_HEIGHT * 16;
	private final static int DESIRED_WIDTH = (int)(DESIRED_HEIGHT * 1.6);
	private final static int MAX_WIDTH = DESIRED_WIDTH + 40;

	private final static boolean DEBUG_COLLISION = false;

	private Paint paint, paintScore;

	private int t, l, w, h;
	private float sc;
	private int score;
	private Scene scene;
	private BitmapSet bitmapSet;
	private Audio audio;
	private Bonk bonk;
	private Coin[] coins;
	private Enemy[] enemies;

	public PlataformasGame(Activity activity) {
		super(activity);
	}
	
	@Override public void start() {
		// adjust scale based on width:height ratio and decide scale
		w = DESIRED_WIDTH;
		h = (int)((float)w * height / width);
		t = l = 0;
		sc = 1;
		if (h > DESIRED_HEIGHT) {
			t = (h - DESIRED_HEIGHT) / 2;
			h  = DESIRED_HEIGHT;
		}
		else {
			sc = (float)DESIRED_HEIGHT / h;
			h = DESIRED_HEIGHT;
			w = (int)(w * sc);
			if (w > MAX_WIDTH) {
				l = (int)((w - MAX_WIDTH) / 2);
				w = MAX_WIDTH;
			}
		}
		sc = (float)(height) / (h + 2 * t);

		// load bitmaps
		bitmapSet = new BitmapSet(activity.getResources());
		
		// Bonk
		bonk = new Bonk(0, 208);
		
		// Coins
		coins = new Coin[6];
		coins[0] = new Coin(80, 200);
		coins[1] = new Coin(320, 160);
		coins[2] = new Coin(128, 75);
		coins[3] = new Coin(200, 80);
		coins[4] = new Coin(220, 100);
		coins[5] = new Coin(250, 124);
		
		// enemies
		enemies = new Enemy[20];

		//empieza/altura/finaliza/altura
		enemies[0] = new Enemy(10 * 16, 14 * 16, 10 * 16, 0 * 16, false);
		enemies[1] = new Enemy(20 * 16, 9 * 16, 20 * 16, 0 * 16, false);
		enemies[2] = new Enemy(30 * 16, 14 * 16, 30 * 16, 0 * 16, false);
		enemies[3] = new Enemy(40 * 16, 10 * 16, 40 * 16, 0 * 16, false);
		enemies[4] = new Enemy(50 * 16, 5 * 16, 50 * 16, 0 * 16, false);
		enemies[5] = new Enemy(60 * 16, 14 * 16, 60 * 16, 0 * 16, false);
		enemies[6] = new Enemy(70 * 16, 5 * 16, 70 * 16, 0 * 16, false);
		enemies[7] = new Enemy(80 * 16, 8 * 16, 80 * 16, 0 * 16, false);
		enemies[8] = new Enemy(90 * 16, 10 * 16, 90 * 16, 0 * 16, false);
		enemies[9] = new Enemy(100 * 16, 6 * 16, 100 * 16, 0 * 16, false);
		enemies[10] = new Enemy(70 * 16, 7 * 16, 70 * 16, 0 * 16, false);
		enemies[11] = new Enemy(80 * 16, 5 * 16, 80 * 16, 0 * 16, false);
		enemies[12] = new Enemy(90 * 16, 13 * 16, 90 * 16, 0 * 16, false);
		enemies[13] = new Enemy(100 * 16, 10 * 16, 100 * 16, 0 * 16, false);
		enemies[14] = new Enemy(10 * 16, 3 * 16, 10 * 16, 0 * 16, false);
		enemies[15] = new Enemy(20 * 16, 6 * 16, 10 * 16, 0 * 16, false);
		enemies[16] = new Enemy(30 * 16, 5 * 16, 20 * 16, 0 * 16, false);
		enemies[17] = new Enemy(40 * 16, 8 * 16, 40 * 16, 0 * 16, false);
		enemies[18] = new Enemy(50 * 16, 10 * 16, 50 * 16, 0 * 16, false);
		enemies[19] = new Enemy(60 * 16, 6 * 16, 60 * 16, 0 * 16, false);


		// load scene
		scene = new Scene();
		scene.load(activity);
		
		// load music
		audio = new Audio();
		audio.load(activity);
		
		newGame();
	}

	@Override public void finish() {
		audio.stopMusic();
		audio.unload();
	}

	public void newGame() {
		score = 0;
		clearTime();
		audio.startMusic();
		resumeGame();
	}

	private int action;
	@Override public void attend(MotionEvent event) {
		action = 0;
		int idx = event.getActionIndex();
		int act = event.getActionMasked();
		int n = event.getPointerCount();
		for (int i = 0; i < n; i++) {
			if (idx == i) {
				if ((act == MotionEvent.ACTION_UP) || 
						(act == MotionEvent.ACTION_POINTER_UP) || 
						(act == MotionEvent.ACTION_CANCEL)) continue;
			}
			int x = (int)(event.getX(i)) * 100 / width;
			if (x < 12) action |= 1;		// LEFT
			else	 if (x < 25) action |= 2;	// RIGHT
			else if (x < 33) {}
			else if (x < 66) {	// PAUSE
				if ((act == MotionEvent.ACTION_DOWN) || 
						(act == MotionEvent.ACTION_POINTER_DOWN)) {
					paused = !paused;
					audio.pause();
					mustRender = true;
				}
			}
			else if (x > 87) action |= 4;	// JUMP
		}
		if (action == 3) action = 0;
		if (action == 7) action = 4;
		event.recycle();
	}

	private int sceneOffset = 0;
	@Override public void update(long delta) {
		if (paused) return;
		
		// ENEMIES
		for (int i = 0; i < enemies.length; i++) {
			Enemy enemy = enemies[i];
			enemy.update(delta);
		}
		
		// BONK
		bonk.update(delta, action);

		// FALLED OUT
		if (bonk.y > 255) {
			bonk.y = 255;
			if (bonk.st != 9) {
				bonk.setState(9);
				audio.die();
			}
			bonk.vy = 0;
		}

		// GROUND
		int r = bonk.y >> 4;
		int c = bonk.x >> 4;
		if (!scene.isGround(r, c) && !scene.isGround(r, c+1)) {
			bonk.vy++; if (bonk.vy > 5) bonk.vy = 5;
		}
		else {
			bonk.vy = 0;
			bonk.y = r << 4;
			if ((bonk.st == 4) || (bonk.st == 2)) bonk.st = 3;
			if ((bonk.st == 5) || (bonk.st == 1)) bonk.st = 0;
			if ((bonk.st == 6) || (bonk.st == 8)) bonk.st = 7;
		}
		
		// GETTING COINS
		for (int i = 0; i < coins.length; i++) {
			Coin coin = coins[i];
			if (bonk.getBounds().intersect(coin.getBounds())) {
				coin.y = 500;
				score += 10;
				audio.coin();
			}
		}

		// TOUCHING ENEMIES
		for (int i = 0; i < enemies.length; i++) {
			Enemy enemy = enemies[i];
			if (bonk.getBounds().intersect(enemy.getBounds())) {
				if (bonk.st != 9) {
					bonk.setState(9);
					audio.die();
				}
			}
		}

		mustRender = true;
	}

	@Override public void render(Canvas canvas) {
		if (scene == null) return;
		if (paint == null) {
			paint = new Paint();
			paint.setStyle(Style.STROKE);
			paintScore = new Paint();
			paintScore.setColor(Color.WHITE);
			paintScore.setShadowLayer(2, 2, 2, Color.LTGRAY);
			paintScore.setTextSize(20);
		}
		
		// compute offset of screen and apply with scale
		canvas.drawColor(Color.BLACK);
		canvas.scale(sc, sc);
		if (bonk.x - sceneOffset < w / 4) {
			sceneOffset = bonk.x - w / 4;
			if (sceneOffset < 0) sceneOffset = 0;
		}
		if (bonk.x - sceneOffset > 3 * w / 4) {
			sceneOffset = bonk.x - 3 * w / 4;
			if (sceneOffset > 500*16 - w) sceneOffset = 500*16 - w;
		}
		canvas.translate(l, t);
		
		// paint scene
		paint.setColor(Color.WHITE);
		canvas.clipRect(0, 0, w, h);
		for (int r = 0; r < h / 16; r++) {
			int y = r * 16;
			int back = 23;
			if (r == 13) back = 24;
			else if (r > 13) back = 25;
			for (int c = sceneOffset / 16; c < (w + sceneOffset) / 16 + 1; c++) {
				if (c == 500) break;
				int x = c * 16 - sceneOffset;
				int i = scene.getBitmap(r, c);
				canvas.drawBitmap(bitmapSet.getBitmap(back), x, y, paint);
				if (i != -1) {
					canvas.drawBitmap(bitmapSet.getBitmap(i), x, y, paint);
				}
			}
		}

		// BONK
		int sprite = bonk.getSprite();
		Bitmap bbmp = bitmapSet.getBitmap(sprite);
		int x = bonk.x - sceneOffset;
		if (bbmp.getWidth() == 28) x -= 2;
		canvas.drawBitmap(bbmp, x, bonk.y - 32, paint);
		if (DEBUG_COLLISION) {
			Rect r = bonk.getBounds();
			r.offset(-sceneOffset, 0);
			canvas.drawRect(r, paint);
		}
		
		// COINS
		for (int i = 0; i < coins.length; i++) {
			Coin c = coins[i];
			Bitmap cbmp = bitmapSet.getBitmap(c.nextSprite());
			canvas.drawBitmap(cbmp, c.x - sceneOffset, c.y - 12, paint);
			if (DEBUG_COLLISION) {
				Rect r = c.getBounds();
				r.offset(-sceneOffset, 0);
				canvas.drawRect(r, paint);
			}
		}

		// ENEMIES
		for (int i = 0; i < enemies.length; i++) {
			Enemy e = enemies[i];
			Bitmap ebmp = bitmapSet.getBitmap(e.nextSprite());
			canvas.drawBitmap(ebmp, e.x - sceneOffset, e.y - 22, paint);
			if (DEBUG_COLLISION) {
				Rect r = e.getBounds();
				r.offset(-sceneOffset, 0);
				canvas.drawRect(r, paint);
			}
		}

		// SCORES
		String msg = "Score: " + score;
		canvas.drawText(msg, 2, 20, paintScore);
		
		// if paused, draw a pause symbol on screen
		if (paused) {
			paint.setColor(0x80808080);
			paint.setStyle(Style.FILL_AND_STROKE);
			canvas.drawCircle(w/2, h/2, 40, paint);
			paint.setColor(0x80FFFFFF);
			canvas.drawRect(w/2-15, h/2-20, w/2-5, h/2+20, paint);
			canvas.drawRect(w/2+5, h/2-20, w/2+15, h/2+20, paint);
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.WHITE);
		}
	}
}