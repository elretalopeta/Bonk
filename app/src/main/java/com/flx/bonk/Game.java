package com.flx.bonk;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;

public abstract class Game {
    
    protected Activity activity;

    protected boolean paused, mustRender;
    protected int width, height;
    protected long time, lastTime, startPeriod, startTime, playTempTime;
    
    public Game(Activity activity) {
        this.activity = activity;
    }
    
    public String getString(int id) {
        return activity.getResources().getString(id);
    }
    
    public void clearTime() { playTempTime = 0; startTime = time; }
    public long getTotalTime() { return time - startTime; }
    public long getPlayTime() { return playTempTime + (paused ? 0 : (time - startPeriod)); }

    public void pauseGame() { 
        paused = true;
        playTempTime += (time - startPeriod);
    }
    public void resumeGame() { 
        paused = false;
        startPeriod = time;
    }

    public abstract int getDesiredDeltaTime();
    public abstract void start();
    public abstract void finish();
    public abstract void attend(MotionEvent event);
    public abstract void update(long delta);
    public abstract void render(Canvas canvas);
}
