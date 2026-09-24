package com.netut.arkanoid.engine;

public class GameLoop implements Runnable {
    private volatile boolean playing = false;
    private volatile boolean paused = false;
    private final GameEngine engine;
    private final Runnable drawRunnable;
    private Thread thread;

    public GameLoop(GameEngine engine, Runnable drawRunnable) {
        this.engine = engine;
        this.drawRunnable = drawRunnable;
        engine.setGameLoop(this);
    }

    public void start() {
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        playing = false;
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() { paused = true; }
    public void resume() { paused = false; }

    @Override
    public void run() {
        while (playing && !Thread.interrupted()) {
            if (!paused) {
                engine.update();
            }
            drawRunnable.run();
            controlFPS();
        }
    }

    private void controlFPS() {
        long frameTime = 1000 / GameConstants.TARGET_FPS;
        long startTime = System.currentTimeMillis();
        try {
            long elapsed = System.currentTimeMillis() - startTime;
            long sleepTime = frameTime - elapsed;
            if (sleepTime > 0) Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}