package io.github.test_gdx.utils;

/**
 * CollisionDetector — утилита для проверки коллизий
 * <p>
 * Содержит статические методы для обнаружения столкновений между объектами.
 * <p>
 * Зачем отдельный класс?
 * - Логика коллизий в одном месте
 * - Легко тестировать
 * - Легко менять алгоритм (например, с AABB на Circle collision)
 * - Переиспользовать в других местах
 * <p>
 * Все методы static (классовые), не нужно создавать объект:
 * CollisionDetector.checkAABB(...)
 * CollisionDetector.circleCollision(...)
 */
public class CollisionDetector {

    /**
     * checkAABB(x1, y1, w1, h1, x2, y2, w2, h2) — проверка пересечения двух прямоугольников
     * <p>
     * AABB = Axis-Aligned Bounding Box
     * Это самый быстрый и простой способ проверить столкновение двух прямоугольников.
     * <p>
     * Логика:
     * Два прямоугольника НЕ пересекаются, если верно хотя бы одно из:
     * 1. Левый край 1 > правый край 2
     * 2. Правый край 1 < левый край 2
     * 3. Нижний край 1 > верхний край 2
     * 4. Верхний край 1 < нижний край 2
     * <p>
     * Если ВСЕ эти условия ЛОЖНЫ, то прямоугольники пересекаются.
     * <p>
     * Использование:
     * boolean hit = CollisionDetector.checkAABB(
     * player.getX(), player.getY(), player.getWidth(), player.getHeight(),
     * platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight()
     * );
     *
     * @param x1 X левого края первого объекта
     * @param y1 Y нижнего края первого объекта
     * @param w1 ширина первого объекта
     * @param h1 высота первого объекта
     * @param x2 X левого края второго объекта
     * @param y2 Y нижнего края второго объекта
     * @param w2 ширина второго объекта
     * @param h2 высота второго объекта
     * @return true если прямоугольники пересекаются
     */
    public static boolean checkAABB(
        float x1, float y1, float w1, float h1,
        float x2, float y2, float w2, float h2
    ) {
        // Проверяем все четыре условия "непересечения"
        // Если какое-то из них верно → прямоугольники НЕ пересекаются

        // Условие 1: левый край 1 > правый край 2
        if (x1 > x2 + w2) {
            return false;  // Объект 1 справа от объекта 2
        }

        // Условие 2: правый край 1 < левый край 2
        if (x1 + w1 < x2) {
            return false;  // Объект 1 слева от объекта 2
        }

        // Условие 3: нижний край 1 > верхний край 2
        if (y1 > y2 + h2) {
            return false;  // Объект 1 выше объекта 2
        }

        // Условие 4: верхний край 1 < нижний край 2
        if (y1 + h1 < y2) {
            return false;  // Объект 1 ниже объекта 2
        }

        // Если ни одно условие не верно → пересечение
        return true;
    }

    /**
     * checkPointInRect(px, py, x, y, w, h) — находится ли точка внутри прямоугольника?
     * <p>
     * Используется редко, но может быть полезно для проверки касания по координатам.
     * <p>
     * Логика:
     * Точка внутри, если:
     * px >= x && px <= x+w && py >= y && py <= y+h
     *
     * @param px X-координата точки
     * @param py Y-координата точки
     * @param x  X левого края прямоугольника
     * @param y  Y нижнего края прямоугольника
     * @param w  ширина прямоугольника
     * @param h  высота прямоугольника
     * @return true если точка внутри прямоугольника
     */
    public static boolean checkPointInRect(float px, float py, float x, float y, float w, float h) {
        return px >= x && px <= x + w &&
            py >= y && py <= y + h;
    }

    /**
     * circleCollision(x1, y1, r1, x2, y2, r2) — столкновение двух кругов
     * <p>
     * Используется для округлых объектов (враги, итемы).
     * <p>
     * Логика:
     * Два круга пересекаются, если расстояние между центрами < сумма радиусов.
     * distance = sqrt((x2-x1)² + (y2-y1)²)
     * if distance < r1 + r2 → столкновение
     *
     * @param x1 X центра первого круга
     * @param y1 Y центра первого круга
     * @param r1 радиус первого круга
     * @param x2 X центра второго круга
     * @param y2 Y центра второго круга
     * @param r2 радиус второго круга
     * @return true если круги пересекаются
     */
    public static boolean circleCollision(float x1, float y1, float r1, float x2, float y2, float r2) {
        // Вычисляем разницу координат
        float dx = x2 - x1;
        float dy = y2 - y1;

        // Вычисляем расстояние между центрами
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Проверяем: расстояние < сумма радиусов?
        return distance < r1 + r2;
    }

    /**
     * circleCollisionOptimized(x1, y1, r1, x2, y2, r2) — оптимизированная версия
     * <p>
     * Сильно дороговизны операция sqrt. Можем избежать, сравнив квадраты:
     * distance² < (r1+r2)²
     * <p>
     * Это быстрее, чем вычислять корень.
     *
     * @param x1 X центра первого круга
     * @param y1 Y центра первого круга
     * @param r1 радиус первого круга
     * @param x2 X центра второго круга
     * @param y2 Y центра второго круга
     * @param r2 радиус второго круга
     * @return true если круги пересекаются
     */
    public static boolean circleCollisionOptimized(float x1, float y1, float r1, float x2, float y2, float r2) {
        float dx = x2 - x1;
        float dy = y2 - y1;

        float sumRadii = r1 + r2;

        // Сравниваем квадраты (избегаем sqrt)
        return dx * dx + dy * dy < sumRadii * sumRadii;
    }

    /**
     * distanceBetween(x1, y1, x2, y2) — расстояние между двумя точками
     * <p>
     * Использует формулу Евклида:
     * distance = sqrt((x2-x1)² + (y2-y1)²)
     *
     * @param x1 X первой точки
     * @param y1 Y первой точки
     * @param x2 X второй точки
     * @param y2 Y второй точки
     * @return расстояние между точками
     */
    public static float distanceBetween(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * distanceBetweenSquared(x1, y1, x2, y2) — квадрат расстояния
     * <p>
     * Быстрее, чем distanceBetween, потому что избегаем sqrt.
     * Используется когда нужно сравнивать расстояния, а не точные значения.
     *
     * @param x1 X первой точки
     * @param y1 Y первой точки
     * @param x2 X второй точки
     * @param y2 Y второй точки
     * @return квадрат расстояния
     */
    public static float distanceBetweenSquared(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx * dx + dy * dy;
    }
}
