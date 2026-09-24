package com.netut.arkanoid.engine;

import com.netut.arkanoid.engine.interfaces.GameEventListener;
import com.netut.arkanoid.game.entity.ball.Ball;
import com.netut.arkanoid.game.entity.brick.Brick;
import com.netut.arkanoid.game.entity.paddle.Paddle;
import com.netut.arkanoid.game.level.LevelFactory;
import com.netut.arkanoid.game.power_up_panel.power_up.PowerUp;
import com.netut.arkanoid.game.power_up_panel.power_up.PowerUpType;
import com.netut.arkanoid.game.GameState;
import com.netut.arkanoid.manager.game.entity.BallManager;
import com.netut.arkanoid.manager.game.entity.BrickManager;
import com.netut.arkanoid.manager.game.entity.PaddleManager;
import com.netut.arkanoid.manager.game.entity.PowerUpManager;
import com.netut.arkanoid.manager.game.level.LevelManager;

import java.util.List;

public class GameEngine {
    private final int screenHeight;
    private GameState state;
    private BallManager ballManager;
    private PaddleManager paddleManager;
    private BrickManager brickManager;
    private PowerUpManager powerUpManager;
    private LevelManager levelManager;
    private GameEventListener listener;
    private GameLoop gameLoop;
    private boolean gameOver = false;

    public GameEngine(int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        init(screenWidth, screenHeight);
    }

    private void init(int screenWidth, int screenHeight) {
        state = new GameState();
        paddleManager = new PaddleManager(screenWidth, GameConstants.PADDLE_HEIGHT,
                GameConstants.PADDLE_WIDTH, state);
        paddleManager.reset(screenHeight - GameConstants.PADDLE_Y_OFFSET);

        powerUpManager = new PowerUpManager(screenHeight, state, null, paddleManager);
        brickManager = new BrickManager(state, powerUpManager);
        ballManager = new BallManager(screenWidth, screenHeight, state,
                paddleManager, brickManager, GameConstants.BALL_SPEED);
        powerUpManager.setBallManager(ballManager);

        LevelFactory levelFactory = new LevelFactory(screenWidth);
        levelManager = new LevelManager(state, brickManager, ballManager,
                paddleManager, levelFactory);
    }

    public void setListener(GameEventListener listener) {
        this.listener = listener;
        levelManager.setListener(listener);
        powerUpManager.setListener(listener);
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public void resetGame() {
        gameOver = false;
        state.reset();
        paddleManager.reset(screenHeight - GameConstants.PADDLE_Y_OFFSET);
        ballManager.reset(paddleManager.getPaddle());
        levelManager.loadCurrentLevel();
    }

    public void update() {
        if (gameOver) return;

        state.updateTimers();
        paddleManager.update();
        ballManager.update(paddleManager.getPaddle());
        powerUpManager.update(paddleManager.getPaddle());
        levelManager.checkLevelComplete();

        if (state.getLives() <= 0 && !gameOver) {
            gameOver = true;
            if (listener != null) {
                listener.onGameOver(state.getScore(), state.getCurrentLevel());
            }
        }
    }

    public void activatePowerUp(PowerUpType type) {
        powerUpManager.activate(type);
    }

    public void proceedToNextLevel() {
        levelManager.proceedToNextLevel();
    }

    public void movePaddle(float touchX) {
        paddleManager.moveTo(touchX);
    }

    public void launchBalls() {
        ballManager.launchAll();
    }

    public void restoreState(float paddleX, boolean launched, long resetTime) {
        paddleManager.setPaddleX(paddleX);
        ballManager.setBallLaunched(launched);
        ballManager.setBallResetTime(resetTime);
    }

    public void loadCurrentLevel() {
        levelManager.loadCurrentLevel();
    }

    public void resetPositions() {
        paddleManager.reset(screenHeight - GameConstants.PADDLE_Y_OFFSET);
        ballManager.reset(paddleManager.getPaddle());
    }

    // Геттеры
    public long getBallResetTime() { return ballManager.getBallResetTime(); }
    public GameState getState() { return state; }
    public List<Ball> getBalls() { return ballManager.getBalls(); }
    public Paddle getPaddle() { return paddleManager.getPaddle(); }
    public List<Brick> getBricks() { return brickManager.getBricks(); }
    public List<PowerUp> getPowerUps() { return powerUpManager.getPowerUps(); }
    public boolean isBallLaunched() { return ballManager.isBallLaunched(); }
}