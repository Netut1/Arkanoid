package com.example.arkanoid;

public class LevelManager {
    private final GameState state;
    private final BrickManager brickManager;
    private final BallManager ballManager;
    private final PaddleManager paddleManager;
    private final LevelFactory levelFactory;
    private GameEventListener listener;

    public LevelManager(GameState state, BrickManager brickManager,
                        BallManager ballManager, PaddleManager paddleManager,
                        LevelFactory levelFactory) {
        this.state = state;
        this.brickManager = brickManager;
        this.ballManager = ballManager;
        this.paddleManager = paddleManager;
        this.levelFactory = levelFactory;
    }

    public void setListener(GameEventListener listener) {
        this.listener = listener;
    }

    public void loadCurrentLevel() {
        Level level = levelFactory.getLevel(state.getCurrentLevel());
        brickManager.loadLevel(level);
    }

    public void checkLevelComplete() {
        if (brickManager.isLevelComplete()) {
            if (listener != null) listener.onLevelComplete();
        }
    }

    public void proceedToNextLevel() {
        state.nextLevel();
        state.setLives(GameConstants.INITIAL_LIVES);
        loadCurrentLevel();
        ballManager.reset(paddleManager.getPaddle());
        state.clearEffects();
        paddleManager.update();
    }
}