package io.github.test_gdx.managers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import io.github.test_gdx.entities.Platform;
import io.github.test_gdx.utils.Constants;

/**
 * PlatformManager — менеджер платформ
 * <p>
 * Отвечает за:
 * - Генерацию новых платформ
 * - Удаление старых платформ
 * - Гарантию, что платформы расположены правильно (в пределах дальности прыжка)
 * - Возможность добавления специальных платформ (позже)
 * <p>
 * ПРИМЕЧАНИЕ: Сейчас этот класс слабо используется.
 * В текущей версии логика генерации платформ в GameScreen.
 * Позже можно перенести сюда для чистоты кода.
 */
public class PlatformManager {

    /**
     * Ссылка на массив платформ (в GameScreen)
     * <p>
     * Позже можно сделать, чтобы PlatformManager сам хранил массив.
     * Но сейчас это хранится в GameScreen для простоты.
     */
    private Array<Platform> platforms;

    /**
     * Счётчик созданных платформ (для отладки/статистики)
     */
    private int platformCount;

    /**
     * Конструктор PlatformManager
     */
    public PlatformManager() {
        this.platforms = new Array<>();
        this.platformCount = 0;
    }

    /**
     * createPlatform(x, y, isStarting) — создать одну платформу
     *
     * @param x          позиция X
     * @param y          позиция Y
     * @param isStarting флаг: стартовая платформа?
     * @return новый объект Platform
     */
    public Platform createPlatform(float x, float y, boolean isStarting) {
        Platform platform = new Platform(
            x, y,
            Constants.PLATFORM_WIDTH,
            Constants.PLATFORM_HEIGHT,
            isStarting
        );

        platformCount++;
        return platform;
    }

    /**
     * generateStartingPlatforms() — генерировать начальные платформы
     * <p>
     * Используется при инициализации игры.
     *
     * @return массив начальных платформ
     */
    public Array<Platform> generateStartingPlatforms() {
        Array<Platform> startingPlatforms = new Array<>();

        // Стартовая платформа (в центре, внизу)
        Platform startPlatform = createPlatform(
            Constants.WORLD_WIDTH / 2 - Constants.PLATFORM_WIDTH / 2,
            Constants.PLATFORM_START_Y,
            true  // стартовая
        );
        startingPlatforms.add(startPlatform);

        // Остальные платформы выше
        for (int i = 0; i < 15; i++) {
            Platform newPlatform = generateRandomPlatform(startingPlatforms.peek());
            startingPlatforms.add(newPlatform);
        }

        return startingPlatforms;
    }

    /**
     * generateRandomPlatform(lastPlatform) — генерировать одну случайную платформу
     * <p>
     * Гарантирует, что новая платформа расположена в пределах,
     * куда можно прыгнуть (дальность прыжка < PLATFORM_MAX_GAP)
     *
     * @param lastPlatform последняя платформа (использует её Y для расчёта)
     * @return новая платформа выше последней
     */
    private Platform generateRandomPlatform(Platform lastPlatform) {
        // X: случайная, но в пределах экрана
        float x = MathUtils.random(Constants.PLATFORM_MIN_X, Constants.PLATFORM_MAX_X);

        // Y: выше последней платформы на гарантированное расстояние
        float verticalGap = MathUtils.random(
            Constants.PLATFORM_MIN_GAP,
            Constants.PLATFORM_MAX_GAP
        );
        float y = lastPlatform.getY() + verticalGap;

        // Создаём обычную платформу (не стартовая)
        return createPlatform(x, y, false);
    }

    /**
     * removePlatform(index) — удалить платформу по индексу
     *
     * @param index индекс платформы в массиве
     */
    public void removePlatform(int index) {
        platforms.removeIndex(index);
    }

    /**
     * getPlatforms() — получить массив платформ
     *
     * @return массив всех платформ
     */
    public Array<Platform> getPlatforms() {
        return platforms;
    }

    /**
     * getPlatformCount() — получить количество созданных платформ (статистика)
     *
     * @return количество платформ, созданных с момента инициализации
     */
    public int getPlatformCount() {
        return platformCount;
    }

    /**
     * reset() — сбросить менеджер
     * <p>
     * Используется при перезагрузке игры.
     */
    public void reset() {
        platforms.clear();
        platformCount = 0;
    }
}
