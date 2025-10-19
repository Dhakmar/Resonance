package io.github.test_gdx.entities;

import io.github.test_gdx.utils.Constants;

/**
 * Player — класс персонажа (игрока)
 * <p>
 * Отвечает за:
 * - Позицию (x, y)
 * - Скорость (velocityX, velocityY)
 * - Физику (гравитация, движение)
 * - Прыжки (обычные и воздушные)
 * - Энергию (позже)
 * <p>
 * НЕ отвечает за:
 * - Рисование (это делает GameScreen)
 * - Коллизии (это делает CollisionDetector)
 * - Ввод (это делает InputHandler)
 */
public class Player {

    // ============== ПОЗИЦИЯ И РАЗМЕР ==============

    /**
     * X-координата персонажа (левый край)
     */
    private float x;

    /**
     * Y-координата персонажа (нижний край)
     */
    private float y;

    /**
     * Ширина персонажа (в пикселях)
     */
    private float width;

    /**
     * Высота персонажа (в пикселях)
     */
    private float height;

    // ============== СКОРОСТЬ ==============

    /**
     * Скорость по оси X (горизонтальная)
     * <p>
     * Положительное = движение вправо
     * Отрицательное = движение влево
     */
    private float velocityX;

    /**
     * Скорость по оси Y (вертикальная)
     * <p>
     * Положительное = движение вверх
     * Отрицательное = движение вниз (падение)
     * <p>
     * Каждый кадр: velocityY += gravity * deltaTime
     */
    private float velocityY;

    // ============== ПРЫЖКИ ==============

    /**
     * Количество оставшихся прыжков в воздухе
     * <p>
     * При касании платформы: jumpsRemaining = 1 (один воздушный прыжок)
     * При прыжке в воздухе: jumpsRemaining -= 1
     * <p>
     * Когда = 0, больше не можешь прыгать, пока не коснёшься платформы
     */
    private int jumpsRemaining;

    /**
     * Максимальное количество воздушных прыжков
     * <p>
     * Сейчас = 1 (можешь прыгнуть один раз в воздухе)
     * Позже может быть больше (double jump, triple jump и т.д.)
     */
    private static final int MAX_AIR_JUMPS = 1;

    // ============== ЭНЕРГИЯ (ПОЗЖЕ) ==============

    /**
     * Текущая энергия персонажа (0-100)
     * <p>
     * Максимум: Constants.MAX_ENERGY (100)
     * Минимум: 0
     * <p>
     * Уменьшается: каждый кадр (энергия затухает со временем)
     * Уменьшается: при каждом прыжке
     * Увеличивается: при касании итема
     */
    private float energy;

    // ============== КОНСТРУКТОР ==============

    /**
     * Конструктор Player
     *
     * @param startX начальная позиция X (обычно центр экрана)
     * @param startY начальная позиция Y (обычно на стартовой платформе)
     */
    public Player(float startX, float startY) {
        // Позиция
        this.x = startX;
        this.y = startY;
        this.width = Constants.PLAYER_SIZE;
        this.height = Constants.PLAYER_HEIGHT;

        // Скорость (начинаем с нулевой скорости)
        this.velocityX = 0;
        this.velocityY = 0;

        // Прыжки (начинаем с нулевых воздушных прыжков, потому что не в воздухе)
        this.jumpsRemaining = 0;

        // Энергия (начинаем с полной энергии)
        this.energy = Constants.START_ENERGY;
    }

    // ============== ОБНОВЛЕНИЕ ПОЗИЦИИ ==============

    /**
     * update(deltaTime) — обновление позиции персонажа
     * <p>
     * Применяет скорость к позиции:
     * новая_позиция = старая_позиция + скорость * время
     * <p>
     * Также обрабатывает "обёртывание" (если вышел за край экрана слева, появляется справа)
     *
     * @param deltaTime время между кадрами (примерно 0.016 сек)
     */
    public void update(float deltaTime) {
        // Применяем скорость к позиции
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;

        // Обёртывание по горизонтали (экран-то циклический)
        if (x < 0) {
            x = Constants.WORLD_WIDTH;  // Вышел слева → появляется справа
        }
        if (x + width > Constants.WORLD_WIDTH) {
            x = -width;  // Вышел справа → появляется слева
        }
    }

    /**
     * applyGravity(deltaTime) — применить гравитацию
     * <p>
     * Каждый кадр: velocityY += gravity * deltaTime
     * Персонаж ускоряется вниз.
     * <p>
     * Также зажимаем максимальную скорость падения (MAX_FALL_SPEED),
     * чтобы физика была стабильной.
     *
     * @param deltaTime время между кадрами
     */
    public void applyGravity(float deltaTime) {
        // Увеличиваем (в отрицательную сторону) скорость падения
        velocityY += Constants.GRAVITY * deltaTime;

        // Зажимаем максимальную скорость падения
        if (velocityY < Constants.MAX_FALL_SPEED) {
            velocityY = Constants.MAX_FALL_SPEED;
        }
    }

    // ============== ПРЫЖКИ ==============

    /**
     * jump() — сделать прыжок
     * <p>
     * Логика:
     * 1. Если находимся на платформе (jumpsRemaining > 0): обычный прыжок
     * 2. Если в воздухе и есть оставшиеся прыжки: воздушный прыжок (слабее)
     * 3. Иначе: ничего не происходит
     * <p>
     * После прыжка: velocityY устанавливается на JUMP_FORCE (движемся вверх)
     */
    public void jump() {
        // Если находимся на платформе (только что коснулись)
        // jumpsRemaining будет переустановлено на MAX_AIR_JUMPS в checkCollisions
        // Поэтому если jumpsRemaining > MAX_AIR_JUMPS, значит это первый прыжок

        if (jumpsRemaining > MAX_AIR_JUMPS) {
            // Обычный прыжок (с платформы)
            velocityY = Constants.JUMP_FORCE;
            jumpsRemaining = MAX_AIR_JUMPS;  // Теперь можешь прыгнуть один раз в воздухе

            // Позже добавим: энергия -= Constants.JUMP_ENERGY_COST;
        } else if (jumpsRemaining > 0) {
            // Воздушный прыжок
            velocityY = Constants.AIR_JUMP_FORCE;
            jumpsRemaining -= 1;

            // Позже добавим: энергия -= Constants.AIR_JUMP_ENERGY_COST;
        }
        // Иначе: ничего (не можешь прыгать)
    }

    /**
     * resetAirJumps() — сбросить счётчик воздушных прыжков
     * <p>
     * Вызывается при касании платформы (в GameScreen.checkCollisions)
     * Позволяет прыгнуть один раз в воздухе на следующий раз
     */
    public void resetAirJumps() {
        jumpsRemaining = MAX_AIR_JUMPS;
    }

    // ============== ДВИЖЕНИЕ ==============

    /**
     * moveLeft() — движение влево
     * <p>
     * Устанавливает скорость влево (отрицательное значение)
     */
    public void moveLeft() {
        velocityX = -Constants.MOVE_SPEED;
    }

    /**
     * moveRight() — движение вправо
     * <p>
     * Устанавливает скорость вправо (положительное значение)
     */
    public void moveRight() {
        velocityX = Constants.MOVE_SPEED;
    }

    /**
     * stopHorizontalMovement() — остановить горизонтальное движение
     * <p>
     * Используется, когда игрок отпускает клавиши движения
     * (Позже можно вызывать в InputHandler)
     */
    public void stopHorizontalMovement() {
        velocityX = 0;
    }

    // ============== ЭНЕРГИЯ (ПОЗЖЕ) ==============

    /**
     * decayEnergy(deltaTime) — потеря энергии со временем
     * <p>
     * Вызывается каждый кадр в GameScreen.update()
     * energy -= energyDecayRate * deltaTime
     *
     * @param deltaTime время между кадрами
     */
    public void decayEnergy(float deltaTime) {
        energy -= Constants.ENERGY_DECAY_RATE * deltaTime;
        if (energy < 0) {
            energy = 0;
        }
    }

    /**
     * restoreEnergy(amount) — восстановить энергию
     * <p>
     * Вызывается при касании итема
     *
     * @param amount количество энергии для восстановления
     */
    public void restoreEnergy(float amount) {
        energy += amount;
        if (energy > Constants.MAX_ENERGY) {
            energy = Constants.MAX_ENERGY;
        }
    }

    /**
     * isAlive() — жив ли персонаж?
     * <p>
     * Позже используется для проверки Game Over
     *
     * @return false если энергия <= 0
     */
    public boolean isAlive() {
        return energy > 0;
    }

    // ============== ГЕТТЕРЫ (ПОЛУЧЕНИЕ ДАННЫХ) ==============

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public float getEnergy() {
        return energy;
    }

    public int getJumpsRemaining() {
        return jumpsRemaining;
    }

    // ============== СЕТТЕРЫ (УСТАНОВЛЕНИЕ ДАННЫХ) ==============

    /**
     * setX(newX) — установить позицию X
     * <p>
     * Используется редко, в основном при граничных условиях
     */
    public void setX(float newX) {
        this.x = newX;
    }

    /**
     * setY(newY) — установить позицию Y
     * <p>
     * Используется при проверке границ (нельзя упасть ниже)
     */
    public void setY(float newY) {
        this.y = newY;
    }

    /**
     * setVelocityY(newVY) — установить скорость Y
     * <p>
     * Используется при граничных условиях
     */
    public void setVelocityY(float newVY) {
        this.velocityY = newVY;
    }
}
