package io.github.test_gdx.entities;

/**
 * Platform — класс платформы
 * <p>
 * Платформа — это статичный объект, на котором стоит персонаж.
 * <p>
 * Отвечает за:
 * - Позицию (x, y)
 * - Размер (width, height)
 * - Флаг "стартовая" (не удаляется из игры)
 * - Информацию для светления (позже)
 * <p>
 * НЕ отвечает за:
 * - Рисование (это делает GameScreen)
 * - Коллизии (это делает CollisionDetector)
 * - Движение (платформы статичны)
 */
public class Platform {

    // ============== ПОЗИЦИЯ И РАЗМЕР ==============

    /**
     * X-координата платформы (левый край)
     */
    private float x;

    /**
     * Y-координата платформы (нижний край)
     */
    private float y;

    /**
     * Ширина платформы
     */
    private float width;

    /**
     * Высота платформы
     */
    private float height;

    // ============== СТАТУС ==============

    /**
     * Флаг: это стартовая платформа?
     * <p>
     * Стартовая платформа:
     * - Находится в нижней части экрана
     * - НЕ удаляется, когда уходит за экран
     * - Служит границей, ниже которой не упасть
     * <p>
     * Обычная платформа:
     * - Удаляется, когда выходит за нижнюю границу видимости
     * - Переиспользуется (очищается из памяти)
     */
    private boolean isStarting;

    // ============== КОНСТРУКТОР ==============

    /**
     * Конструктор Platform
     *
     * @param x          позиция X (левый край)
     * @param y          позиция Y (нижний край)
     * @param width      ширина платформы
     * @param height     высота платформы
     * @param isStarting флаг: стартовая платформа?
     */
    public Platform(float x, float y, float width, float height, boolean isStarting) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isStarting = isStarting;
    }

    // ============== ГЕТТЕРЫ ==============

    /**
     * getX() — получить позицию X
     *
     * @return X-координата левого края платформы
     */
    public float getX() {
        return x;
    }

    /**
     * getY() — получить позицию Y
     *
     * @return Y-координата нижнего края платформы
     */
    public float getY() {
        return y;
    }

    /**
     * getWidth() — получить ширину
     *
     * @return ширина платформы
     */
    public float getWidth() {
        return width;
    }

    /**
     * getHeight() — получить высоту
     *
     * @return высота платформы
     */
    public float getHeight() {
        return height;
    }

    /**
     * isStarting() — стартовая ли это платформа?
     * <p>
     * Используется в GameScreen.updatePlatforms() для проверки:
     * "Можно ли удалить эту платформу из памяти?"
     *
     * @return true если это стартовая платформа (не удаляется)
     */
    public boolean isStarting() {
        return isStarting;
    }

    // ============== ПОЗИЦИЯ ВЕРХНЕГО КРАЯ ==============

    /**
     * getTopY() — получить Y верхнего края платформы
     * <p>
     * Полезно для проверок типа:
     * "Персонаж выше платформы или нет?"
     *
     * @return Y-координата верхнего края (y + height)
     */
    public float getTopY() {
        return y + height;
    }

    /**
     * getCenterX() — получить центральную X платформы
     * <p>
     * Может быть полезно для спецэффектов (света, частиц)
     *
     * @return X-координата центра платформы
     */
    public float getCenterX() {
        return x + width / 2;
    }

    /**
     * getCenterY() — получить центральную Y платформы
     *
     * @return Y-координата центра платформы
     */
    public float getCenterY() {
        return y + height / 2;
    }

    // ============== ПРОВЕРКИ ==============

    /**
     * contains(px, py) — находится ли точка внутри платформы?
     * <p>
     * Используется для детальной проверки коллизий.
     * Сейчас не используется (используем AABB в CollisionDetector),
     * но может быть полезно позже.
     *
     * @param px X-координата точки
     * @param py Y-координата точки
     * @return true если точка внутри платформы
     */
    public boolean contains(float px, float py) {
        return px >= x && px <= x + width &&
            py >= y && py <= y + height;
    }

    /**
     * distanceTo(px, py) — расстояние от платформы до точки
     * <p>
     * Может быть использовано для светления
     * (чем ближе персонаж, тем ярче платформа)
     *
     * @param px X-координата точки (обычно персонаж)
     * @param py Y-координата точки
     * @return расстояние от центра платформы до точки
     */
    public float distanceTo(float px, float py) {
        float centerX = getCenterX();
        float centerY = getCenterY();

        float dx = px - centerX;
        float dy = py - centerY;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    // ============== СЕТТЕРЫ (РЕДКО ИСПОЛЬЗУЮТСЯ) ==============

    /**
     * setY(newY) — изменить Y позицию
     * <p>
     * Сейчас не используется (платформы статичны).
     * Может быть полезно если добавим движущиеся платформы.
     */
    public void setY(float newY) {
        this.y = newY;
    }
}
