package com.flx.bonk;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Audio {
	private SoundPool soundPool;
	private MediaPlayer mediaPlayer;
	private int coin, die, pause;

	public void load(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mediaPlayer = MediaPlayer.create(activity, R.raw.music);
		mediaPlayer.setLooping(true);
		mediaPlayer.setVolume(0.25f, 0.25f);
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		coin = soundPool.load(activity, R.raw.coin, 1);
		die = soundPool.load(activity, R.raw.die, 1);
		pause = soundPool.load(activity, R.raw.pause, 1);
	}
	public void startMusic() {
		mediaPlayer.start();
	}
	public void stopMusic() {
		mediaPlayer.pause();
	}

	public void unload() {
		soundPool.release();
		mediaPlayer.release();
	}

	public void coin() { soundPool.play(coin, 1, 1, 1, 0, 1); }
	public void die() { soundPool.play(die, 1, 1, 1, 0, 1); }
	public void pause() { soundPool.play(pause, 1, 1, 1, 0, 1); }
}
