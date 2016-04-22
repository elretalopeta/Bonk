package com.flx.bonk;

import java.util.concurrent.LinkedBlockingDeque;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private Handler handler;
    private Game game;
    private LinkedBlockingDeque<MotionEvent> eventQueue;
    private boolean running;

    public GameView(Context context, Game game) {
        super(context);
        eventQueue = new LinkedBlockingDeque<MotionEvent>();
        this.game = game;
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @SuppressLint("Recycle")
    public void enqueueEvent(MotionEvent event) { 
        try { eventQueue.putFirst(MotionEvent.obtain(event)); } 
        catch (InterruptedException e) { } 
    }
    
    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        game.width = width;
        game.height = height;
        handler = new Handler();
        if (!running) {
            game.startTime = game.startPeriod = game.time = System.currentTimeMillis();
            game.start();
            running = true;
            game.mustRender = true;
            handler.post(runnable);
        }
    }
    
    private Runnable runnable = new Runnable() {
        @Override public void run() {
            int desired = game.getDesiredDeltaTime();
            game.time = System.currentTimeMillis();
            long delta = game.time - game.lastTime;
            game.lastTime = game.time;
            
            if (running) {
                handler.removeCallbacks(this);
                handler.postDelayed(this, desired);

                // attend user interaction
                while (!eventQueue.isEmpty()) {
                    MotionEvent event = eventQueue.removeLast();
                    game.attend(event);
                }

                game.update(delta);
                
                if (game.mustRender) {
                    Canvas canvas = getHolder().lockCanvas();
                    if (canvas != null) {
                        game.render(canvas);
                        game.mustRender = false;
                        getHolder().unlockCanvasAndPost(canvas);
                    }
                }
            }
            else {
                game.finish();
            }
        }
    };
    
    @Override public void surfaceCreated(SurfaceHolder holder) { }
    @Override public void surfaceDestroyed(SurfaceHolder holder) { running = false; }
    
    @Override public boolean onTouchEvent(MotionEvent event) {
        enqueueEvent(event);
        return true;
    }
}

