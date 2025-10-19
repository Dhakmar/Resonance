package io.github.test_gdx.entities;

/**
 * Enemy — класс врага
 *
 * ПРИМЕЧАНИЕ: В концепции "Resonance" враги НЕ являются угрозой.
 * Их роль скорее декоративная или часть атмосферы.
 *
 * Сейчас этот класс не используется (убрали врагов из игры).
 * Оставляю как заготовку на случай, если понадобятся позже.
 *
 * Возможное использование в будущем:
 * - Враги как часть атмосферы (не убивают, просто движутся)
 * - Опасные препятствия (но не враги в классическом смысле)
 * - NPC, которые реагируют на присутствие персонажа
 */
public class Enemy {

    // ============== ПОЗИЦИЯ И РАЗМЕР ==============

    /** X-координата врага */
    private float x;

    /** Y-координата врага */
    private float y;

    /** Ширина врага */
    private float width;

    /** Высота врага */
    private float height;

    // ============== ДВИЖЕНИЕ ==============

    /** Скорость движения врага (по оси X)
     *
     * Враги обычно движутся туда-сюда (влево-вправо).
     * Положительное значение = вправо
     * Отрицательное значение = влево
     */
    private float velocityX;

    /** Граница слева (враг не выходит за эту позицию) */
    private float minX;

    /** Граница справа (враг не выходит за эту позицию) */
    private float maxX;

    // ============== ТИП ВРАГА ==============

    /** Тип врага (может быть разные поведения)
     *
     * Примеры:
     * - WALKER — просто ходит туда-сюда
     * - FLYER — летает (может быть выше)
     * - BOUNCER — прыгает
     */
    private EnemyType type;

    /**
     * EnemyType — перечисление типов врагов
     */
    public enum EnemyType {
        WALKER,    // ходит туда-сюда
        FLYER,     // летает
        BOUNCER    // прыгает
    }

    // ============== КОНСТРУКТОР ==============

    /**
     * Конструктор Enemy
     *
     * @param x начальная позиция X
     * @param y начальная позиция Y
     * @param width ширина врага
     * @param height высота врага
     */
    public Enemy(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Движение
        this.velocityX = 150f;  // движется вправо
        this.minX = 0f;         // левая граница экрана
        this.maxX = 800f;       // правая граница экрана

        // Тип
        this.type = EnemyType.WALKER;
    }

    /**
     * Конструктор с типом врага
     *
     * @param x позиция X
     * @param y позиция Y
     * @param width ширина
     * @param height высота
     * @param type тип врага
     */
    public Enemy(float x, float y, float width, float height, EnemyType type) {
        this(x, y, width, height);
        this.type = type;
    }

    // ============== ОБНОВЛЕНИЕ ==============

    /**
     * update(deltaTime) — обновить позицию врага
     *
     * Враг движется туда-сюда между minX и maxX.
     *
     * @param deltaTime время между кадрами
     */
    public void update(float deltaTime) {
        // Применяем скорость к позиции
        x += velocityX * deltaTime;

        // Проверяем границы и отскакиваем
        if (x < minX || x + width > maxX) {
            velocityX *= -1;  // меняем направление

            // Зажимаем позицию, чтобы не провалилась за границу
            if (x < minX) {
                x = minX;
            }
            if (x + width > maxX) {
                x = maxX - width;
            }
        }
    }

    /**
     * updateByPlayerX(playerX) — враг "следит" за персонажем
     *
     * Враг может двигаться в направлении персонажа
     * (может быть использовано для более интеллектуального поведения).
     *
     * @param playerX позиция персонажа по оси X
     */
    public void updateByPlayerX(float playerX) {
        // Если игрок справа от врага → враг движется вправо
        if (playerX > x + width / 2) {
            velocityX = Math.abs(velocityX);  // движется вправо
        }
        // Если игрок слева от врага → враг движется влево
        else if (playerX < x + width / 2) {
            velocityX = -Math.abs(velocityX);  // движется влево
        }

        // Обновляем позицию
        update(0.016f);  // примерно один кадр
    }

    // ============== ГЕТТЕРЫ ==============

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public float getVelocityX() { return velocityX; }

    public EnemyType getType() { return type; }

    // ============== СЕТТЕРЫ ==============

    public void setVelocityX(float vx) { this.velocityX = vx; }

    /**
     * setBounds(minX, maxX) — установить границы движения
     *
     * @param minX левая граница
     * @param maxX правая граница
     */
    public void setBounds(float minX, float maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }
}
