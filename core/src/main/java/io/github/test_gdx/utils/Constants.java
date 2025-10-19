package io.github.test_gdx.utils;

/**
 * Constants — класс со всеми константами игры
 * <p>
 * Зачем это нужно?
 * - Все "магические числа" в одном месте
 * - Легко менять параметры (баланс, размеры, скорости)
 * - Если число используется в нескольких местах, меняешь один раз
 * - Код более читаем (GRAVITY вместо -500)
 * <p>
 * Все переменные static final, то есть:
 * - static — принадлежат классу, не объекту
 * - final — нельзя менять (константа)
 * - public — доступна всем
 * <p>
 * Использование:
 * Constants.GRAVITY
 * Constants.WORLD_WIDTH
 * Constants.PLAYER_SIZE
 */
public class Constants {

    // ============== ЭКРАН И МИР ==============

    /**
     * Ширина игрового мира (в пикселях)
     */
    public static final float WORLD_WIDTH = 800f;

    /**
     * Высота игрового мира (в пикселях)
     */
    public static final float WORLD_HEIGHT = 480f;

    // ============== ПЕРСОНАЖ ==============

    /**
     * Ширина персонажа
     */
    public static final float PLAYER_SIZE = 30f;

    /**
     * Высота персонажа
     */
    public static final float PLAYER_HEIGHT = 30f;

    /**
     * Гравитация (ускорение падения, направлено вниз)
     * <p>
     * Отрицательное значение, потому что в системе координат libGDX:
     * Y вверх = положительное
     * Y вниз = отрицательное
     * <p>
     * Примерно -500 пикселей/сек²
     */
    public static final float GRAVITY = -500f;

    /**
     * Сила прыжка (начальная скорость вверх при прыжке)
     * <p>
     * Положительное значение, персонаж летит вверх
     * Чем больше — тем выше прыжок
     */
    public static final float JUMP_FORCE = 400f;

    /**
     * Сила прыжка в воздухе (double jump)
     * <p>
     * Может быть слабее, чем обычный прыжок
     * Для этого примера равен обычному
     */
    public static final float AIR_JUMP_FORCE = 350f;

    /**
     * Максимальная скорость падения
     * <p>
     * Если персонаж падает слишком долго, скорость не растёт бесконечно
     * Зажимается на этом значении (чтобы физика была стабильной)
     */
    public static final float MAX_FALL_SPEED = -600f;

    /**
     * Скорость горизонтального движения
     */
    public static final float MOVE_SPEED = 300f;

    /**
     * Минимальная Y-позиция (граница, ниже которой не упасть)
     * <p>
     * Обычно это Y стартовой платформы или чуть ниже
     */
    public static final float MINIMUM_Y = 50f;

    // ============== ПЛАТФОРМЫ ==============

    /**
     * Ширина платформы
     */
    public static final float PLATFORM_WIDTH = 80f;

    /**
     * Высота платформы
     */
    public static final float PLATFORM_HEIGHT = 15f;

    /**
     * Y-позиция стартовой платформы (в нижней части экрана)
     * <p>
     * Персонаж начинает игру на этой платформе
     */
    public static final float PLATFORM_START_Y = 100f;

    /**
     * Минимальное вертикальное расстояние между платформами
     * <p>
     * Гарантирует, что платформы не расположены слишком близко
     */
    public static final float PLATFORM_MIN_GAP = 50f;

    /**
     * Максимальное вертикальное расстояние между платформами
     * <p>
     * Гарантирует, что платформы не расположены слишком далеко
     * (иначе не сможешь прыгнуть на следующую)
     * <p>
     * Должно быть ≤ максимальной высоте прыжка
     * Примерно: (JUMP_FORCE)² / (2 * |GRAVITY|) ≈ 160 пиксели
     * Берём 120 для безопасности
     */
    public static final float PLATFORM_MAX_GAP = 120f;

    /**
     * Минимальная X-позиция для генерации платформы
     * <p>
     * Платформы не генерируются прямо у края экрана
     */
    public static final float PLATFORM_MIN_X = 10f;

    /**
     * Максимальная X-позиция для генерации платформы
     * <p>
     * WORLD_WIDTH - PLATFORM_WIDTH - отступ
     */
    public static final float PLATFORM_MAX_X = WORLD_WIDTH - PLATFORM_WIDTH - 10f;

    // ============== КАМЕРА ==============

    /**
     * Смещение камеры вверх от позиции персонажа
     * <p>
     * Камера находится выше персонажа, чтобы видеть приближающиеся платформы
     * Единица: пиксели
     */
    public static final float CAMERA_OFFSET = 100f;

    // ============== ВИЗУАЛЬНЫЕ ЭФФЕКТЫ ==============

    // Цвета персонажа (RGBA: Red, Green, Blue, Alpha)
    public static final float PLAYER_COLOR_R = 0.2f;
    public static final float PLAYER_COLOR_G = 0.8f;
    public static final float PLAYER_COLOR_B = 1f;
    public static final float PLAYER_COLOR_A = 1f;

    // Цвета платформ (близкие)
    public static final float PLATFORM_ACTIVE_R = 0.3f;
    public static final float PLATFORM_ACTIVE_G = 1.0f;
    public static final float PLATFORM_ACTIVE_B = 0.3f;
    public static final float PLATFORM_ACTIVE_A = 1f;

    // Цвета платформ (далёкие)
    public static final float PLATFORM_INACTIVE_R = 0.2f;
    public static final float PLATFORM_INACTIVE_G = 0.6f;
    public static final float PLATFORM_INACTIVE_B = 0.2f;
    public static final float PLATFORM_INACTIVE_A = 1f;

    // Цвет фона
    public static final float BG_COLOR_R = 0.15f;
    public static final float BG_COLOR_G = 0.15f;
    public static final float BG_COLOR_B = 0.2f;
    public static final float BG_COLOR_A = 1f;

    // Дистанция активации света платформы
    public static final float PLATFORM_LIGHT_RANGE = 200f;

    // ============== ЭНЕРГИЯ (ПОЗЖЕ) ==============

    /**
     * Максимальная энергия персонажа
     */
    public static final float MAX_ENERGY = 100f;

    /**
     * Начальная энергия при старте игры
     */
    public static final float START_ENERGY = 100f;

    /**
     * Скорость потери энергии (за секунду)
     * <p>
     * Энергия уменьшается даже без действий
     * Позже: energy -= ENERGY_DECAY_RATE * deltaTime
     */
    public static final float ENERGY_DECAY_RATE = 5f;  // 5% за секунду

    /**
     * Стоимость обычного прыжка (сколько энергии потратить)
     */
    public static final float JUMP_ENERGY_COST = 10f;

    /**
     * Стоимость воздушного прыжка
     */
    public static final float AIR_JUMP_ENERGY_COST = 15f;

    /**
     * Восстановление энергии при касании итема
     */
    public static final float ITEM_ENERGY_RESTORE = 50f;

    // ============== ИТЕМЫ (ПОЗЖЕ) ==============

    /**
     * Размер итема
     */
    public static final float ITEM_SIZE = 20f;

    /**
     * Вероятность появления итема на платформе (0.0 - 1.0)
     * <p>
     * 0.1 = 10% платформ имеют итем
     * 0.05 = 5% (редче)
     */
    public static final float ITEM_SPAWN_CHANCE = 0.05f;

    // ============== ВРАГИ (ПОЗЖЕ ИЛИ УДАЛИТЬ) ==============

    // Враги убраны из концепции, но оставляю на случай, если понадобятся позже

    // ============== ПРОЧЕЕ ==============

    /**
     * Целевой FPS (кадры в секунду)
     * <p>
     * Информационная константа, обычно не меняется
     * libGDX автоматически стремится к 60 FPS
     */
    public static final float TARGET_FPS = 60f;

    /**
     * Максимальное deltaTime за один кадр
     * <p>
     * Если кадр рендерился очень долго (лаг), deltaTime может быть большим
     * Зажимаем его, чтобы физика не сломалась
     */
    public static final float MAX_DELTA_TIME = 0.1f;  // 100 миллисекунд
}
