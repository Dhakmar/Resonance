package io.github.test_gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.test_gdx.InputHandler;
import io.github.test_gdx.entities.Player;
import io.github.test_gdx.entities.Platform;
import io.github.test_gdx.utils.Constants;
import io.github.test_gdx.utils.CollisionDetector;

/**
 * GameScreen — главный координатор игры
 * <p>
 * Отвечает за:
 * - Управление объектами (Player, Platform, Item)
 * - Обновление логики каждый кадр
 * - Рисование на экран
 * - Управление состоянием (PLAYING, GAME_OVER)
 */
public class GameScreen {

    // ============== КОМПОНЕНТЫ ==============

    private InputHandler inputHandler;
    private Player player;
    private Array<Platform> platforms;

    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private BitmapFont font;

    private GameState gameState;
    private float timeSinceGameOver;
    private int score;

    // ============== КОНСТРУКТОР ==============

    public GameScreen() {
        initializeGraphics();
        initializeGame();
    }

    // ============== ИНИЦИАЛИЗАЦИЯ ==============

    private void initializeGraphics() {
        camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);

        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
    }

    private void initializeGame() {
        inputHandler = new InputHandler();

        // Создаём персонажа в центре, чуть выше стартовой платформы
        player = new Player(
            Constants.WORLD_WIDTH / 2 - Constants.PLAYER_SIZE / 2,
            Constants.PLATFORM_START_Y + Constants.PLAYER_SIZE
        );

        platforms = new Array<>();

        // Создаём стартовую платформу
        createStartingPlatforms();

        gameState = GameState.PLAYING;
        timeSinceGameOver = 0;
        score = 0;
    }

    private void createStartingPlatforms() {
        // Стартовая платформа (фиксированная)
        Platform startPlatform = new Platform(
            Constants.WORLD_WIDTH / 2 - Constants.PLATFORM_WIDTH / 2,
            Constants.PLATFORM_START_Y,
            Constants.PLATFORM_WIDTH,
            Constants.PLATFORM_HEIGHT,
            true  // флаг: стартовая
        );
        platforms.add(startPlatform);

        // Остальные платформы выше
        for (int i = 0; i < 15; i++) {
            addPlatform();
        }
    }

    private void addPlatform() {
        float x = MathUtils.random(Constants.PLATFORM_MIN_X, Constants.PLATFORM_MAX_X);

        Platform lastPlatform = platforms.peek();
        float verticalGap = MathUtils.random(Constants.PLATFORM_MIN_GAP, Constants.PLATFORM_MAX_GAP);
        float y = lastPlatform.getY() + verticalGap;

        Platform newPlatform = new Platform(x, y, Constants.PLATFORM_WIDTH, Constants.PLATFORM_HEIGHT, false);
        platforms.add(newPlatform);
    }

    // ============== ОСНОВНОЙ ЦИКЛ ==============

    public void update(float deltaTime) {
        if (gameState == GameState.PLAYING) {
            updateInput();
            updatePhysics(deltaTime);
            checkCollisions();
            updatePlatforms();
            checkGameOver();
            updateCamera();
        } else if (gameState == GameState.GAME_OVER) {
            handleGameOver();
        }
    }

    private void updateInput() {
        // Прыжок
        if (inputHandler.shouldJump()) {
            player.jump();
        }

        // Горизонтальное движение
        if (inputHandler.isMovingLeft()) {
            player.moveLeft();
        } else if (inputHandler.isMovingRight()) {
            player.moveRight();
        } else {
            player.stopHorizontalMovement();
        }
    }

    private void updatePhysics(float deltaTime) {
        // Гравитация
        player.applyGravity(deltaTime);

        // Обновление позиции
        player.update(deltaTime);
    }

    private void checkCollisions() {
        // Проверка столкновений с платформами
        for (Platform platform : platforms) {
            boolean colliding = CollisionDetector.checkAABB(
                player.getX(), player.getY(), player.getWidth(), player.getHeight(),
                platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight()
            );

            if (colliding && player.getVelocityY() < 0) {
                // Персонаж падал и коснулся платформы → прыжок
                player.jump();
                player.resetAirJumps();
            }
        }

        // Проверка нижней границы
        if (player.getY() < Constants.MINIMUM_Y) {
            player.setY(Constants.MINIMUM_Y);
            player.setVelocityY(0);
        }
    }

    private void updatePlatforms() {
        // Удаление старых платформ
        for (int i = platforms.size - 1; i >= 0; i--) {
            Platform platform = platforms.get(i);

            // Удаляем только если это не стартовая платформа
            if (platform.getY() < player.getY() - Constants.WORLD_HEIGHT && !platform.isStarting()) {
                platforms.removeIndex(i);
            }
        }

        // Генерирование новых платформ
        Platform lastPlatform = platforms.peek();
        if (lastPlatform.getY() < player.getY() + Constants.WORLD_HEIGHT) {
            addPlatform();
        }
    }

    private void checkGameOver() {
        // Падение слишком далеко вниз
        if (player.getY() < camera.position.y - Constants.WORLD_HEIGHT / 2 - 100) {
            gameState = GameState.GAME_OVER;
            timeSinceGameOver = 0;
        }
    }

    private void updateCamera() {
        float targetY = player.getY() + Constants.CAMERA_OFFSET;
        camera.position.y = targetY;
        camera.update();
    }

    private void handleGameOver() {
        timeSinceGameOver += Gdx.graphics.getDeltaTime();

        // Возможность перезагрузки через 1 секунду
        if (timeSinceGameOver > 1.0f && inputHandler.shouldJump()) {
            restart();
        }
    }

    private void restart() {
        platforms.clear();
        initializeGame();
    }

    // ============== РИСОВАНИЕ ==============

    public void render() {
        // Очистка экрана
        ScreenUtils.clear(
            Constants.BG_COLOR_R,
            Constants.BG_COLOR_G,
            Constants.BG_COLOR_B,
            Constants.BG_COLOR_A
        );

        // Рисование игровых объектов (с камерой)
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        drawPlatforms();
        drawPlayer();

        shapeRenderer.end();

        // Рисование UI (без камеры)
        drawUI();
    }

    private void drawPlatforms() {
        for (Platform platform : platforms) {
            // Цвет зависит от расстояния до персонажа
            float distanceToPlayer = Math.abs(platform.getY() - player.getY());

            if (distanceToPlayer < Constants.PLATFORM_LIGHT_RANGE) {
                // Близко — ярче
                shapeRenderer.setColor(
                    Constants.PLATFORM_ACTIVE_R,
                    Constants.PLATFORM_ACTIVE_G,
                    Constants.PLATFORM_ACTIVE_B,
                    Constants.PLATFORM_ACTIVE_A
                );
            } else {
                // Далеко — тусклее
                shapeRenderer.setColor(
                    Constants.PLATFORM_INACTIVE_R,
                    Constants.PLATFORM_INACTIVE_G,
                    Constants.PLATFORM_INACTIVE_B,
                    Constants.PLATFORM_INACTIVE_A
                );
            }

            shapeRenderer.rect(
                platform.getX(),
                platform.getY(),
                platform.getWidth(),
                platform.getHeight()
            );
        }
    }

    private void drawPlayer() {
        shapeRenderer.setColor(
            Constants.PLAYER_COLOR_R,
            Constants.PLAYER_COLOR_G,
            Constants.PLAYER_COLOR_B,
            Constants.PLAYER_COLOR_A
        );
        shapeRenderer.rect(
            player.getX(),
            player.getY(),
            player.getWidth(),
            player.getHeight()
        );
    }

    private void drawUI() {
        spriteBatch.begin();
        spriteBatch.setColor(1, 1, 1, 1);

        // Счёт (высота)
        score = (int) (player.getY() / 10);
        font.draw(spriteBatch, "Height: " + score, 10, Gdx.graphics.getHeight() - 10);

        // Game Over экран
        if (gameState == GameState.GAME_OVER) {
            float screenCenterX = Gdx.graphics.getWidth() / 2f;
            float screenCenterY = Gdx.graphics.getHeight() / 2f;

            font.draw(spriteBatch, "GAME OVER", screenCenterX - 70, screenCenterY + 30);
            font.draw(spriteBatch, "Height: " + score, screenCenterX - 80, screenCenterY);
            font.draw(spriteBatch, "Press SPACE to restart", screenCenterX - 150, screenCenterY - 30);
        }

        spriteBatch.end();
    }

    // ============== ОЧИСТКА ==============

    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
        font.dispose();
    }
}
