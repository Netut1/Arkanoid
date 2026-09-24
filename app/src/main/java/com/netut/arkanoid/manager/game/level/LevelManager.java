package com.netut.arkanoid.manager.game.level;

import com.netut.arkanoid.engine.GameConstants;
import com.netut.arkanoid.engine.interfaces.GameEventListener;
import com.netut.arkanoid.game.level.Level;
import com.netut.arkanoid.game.level.LevelFactory;
import com.netut.arkanoid.game.GameState;
import com.netut.arkanoid.manager.game.entity.BallManager;
import com.netut.arkanoid.manager.game.entity.BrickManager;
import com.netut.arkanoid.manager.game.entity.PaddleManager;

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