package com.flx.bonk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;

public class Scene {
	public final static int SCENE_HEIGHT = 16;
	private String[] scene;
	
	public void load(Activity activity) {
		// load scene
		scene = new String[SCENE_HEIGHT];
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					activity.getResources().openRawResource(R.raw.scene)));
			for (int i = 0; i < SCENE_HEIGHT; i++) {
				String linea = in.readLine();
				scene[i] = linea;
			}
			in.close();
		}
		catch (IOException e) { 
			scene = null;
		}
	}

	public boolean isGround(int r, int c) {
		char s = scene[r].charAt(c);
		if (s == '-') return true;
		if (s == '<') return true;
		if (s == '>') return true;
		return false;
	}
	
	public int getBitmap(int r, int c) {
		char e = scene[r].charAt(c);
		int i = -1;
		switch (e) {
		case '<': i = 35; break;
		case '-': i = 36; break;
		case '>': i = 37; break;
		case '[': i = 44; break;
		case '#': i = 45; break;
		case ']': i = 46; break;
		case '|': i = 40; break;
		case '{': i = 21; break;
		case '}': i = 22; break;
		}
		return i;
	}
}
