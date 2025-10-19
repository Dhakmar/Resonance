// ============== Main.java ==============

package io.github.test_gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import io.github.test_gdx.game.GameScreen;

/**
 * Main — точка входа приложения
 * <p>
 * Это главный класс, который наследует ApplicationAdapter от libGDX.
 * Он управляет жизненным циклом приложения.
 * <p>
 * Методы:
 * - create() — вызывается один раз при запуске
 * - render() — вызывается каждый кадр (60 раз в секунду)
 * - dispose() — вызывается один раз при закрытии
 */
public class Main extends ApplicationAdapter {

    /**
     * GameScreen — главный экран игры
     * <p>
     * Здесь находится вся логика игры (update, render, collision, etc)
     * Main просто делегирует ему все вызовы.
     */
    private GameScreen gameScreen;

    /**
     * create() — инициализация при запуске приложения
     * <p>
     * Вызывается один раз, в самом начале.
     * Здесь создаём GameScreen и остальные начальные объекты.
     */
    @Override
    public void create() {
        // Создаём главный экран игры
        gameScreen = new GameScreen();
    }

    /**
     * render() — рендеринг каждого кадра
     * <p>
     * Вызывается 60 раз в секунду (или сколько монитор поддерживает).
     * <p>
     * Логика:
     * 1. Обновить логику игры (update)
     * 2. Нарисовать на экран (render)
     */
    @Override
    public void render() {
        // deltaTime — время между этим кадром и предыдущим (примерно 0.016 сек)
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Обновляем логику игры
        gameScreen.update(deltaTime);

        // Рисуем всё на экран
        gameScreen.render();
    }

    /**
     * dispose() — очистка при закрытии приложения
     * <p>
     * Вызывается один раз, когда приложение закрывается.
     * Освобождаем ресурсы (текстуры, шрифты, шейдеры и т.д.)
     */
    @Override
    public void dispose() {
        gameScreen.dispose();
    }
}
