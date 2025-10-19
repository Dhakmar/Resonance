package io.github.test_gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * InputHandler — обработчик всех типов ввода
 * <p>
 * Задача: унифицировать ввод с клавиатуры, мыши и сенсорного экрана
 * <p>
 * Все три типа ввода должны работать одновременно и вызывать одни и те же действия.
 * Игрок может:
 * - Нажать пробел
 * - Кликнуть мышью
 * - Коснуться экрана
 * И во всех случаях персонаж должен прыгнуть.
 */
public class InputHandler {

    // ============== СОСТОЯНИЕ КАСАНИЯ ==============

    /**
     * wasTouch — было ли касание в ПРОШЛОМ кадре
     * <p>
     * Используется для обнаружения НОВОГО касания (переход из false в true)
     * Без этого мы не сможем различить "только что коснулся" и "держу касание"
     */
    private boolean wasTouch;

    /**
     * wasMouse — было ли нажатие мыши в ПРОШЛОМ кадре
     * <p>
     * Аналогично касанию, но для мыши
     * (в libGDX есть isButtonJustPressed, но лучше иметь единообразный подход)
     */
    private boolean wasMouse;

    // ============== КОНСТРУКТОР ==============

    /**
     * Конструктор — инициализация состояния ввода
     */
    public InputHandler() {
        wasTouch = false;
        wasMouse = false;
    }

    // ============== ОСНОВНЫЕ МЕТОДЫ ВВОДА ==============

    /**
     * shouldJump() — нужно ли прыгать?
     * <p>
     * Проверяет все три типа ввода и возвращает true, если какой-то активирован.
     * <p>
     * Используется так:
     * if (inputHandler.shouldJump()) {
     * player.jump();
     * }
     *
     * @return true если игрок только что нажал клавишу прыжка (любым способом)
     */
    public boolean shouldJump() {
        // Проверка 1: клавиатура (пробел)
        if (isKeyJustPressed(Input.Keys.SPACE)) {
            return true;
        }

        // Проверка 2: мышь (левый клик)
        if (isMouseJustClicked()) {
            return true;
        }

        // Проверка 3: сенсор (касание экрана)
        if (isTouchJustPressed()) {
            return true;
        }

        return false;
    }

    /**
     * isMovingLeft() — движение влево
     *
     * @return true если нажата клавиша влево или движение мыши влево (позже)
     */
    public boolean isMovingLeft() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
            Gdx.input.isKeyPressed(Input.Keys.A);
    }

    /**
     * isMovingRight() — движение вправо
     *
     * @return true если нажата клавиша вправо или движение мыши вправо (позже)
     */
    public boolean isMovingRight() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
            Gdx.input.isKeyPressed(Input.Keys.D);
    }

    // ============== ЧАСТНЫЕ МЕТОДЫ (ДЕТАЛИ РЕАЛИЗАЦИИ) ==============

    /**
     * isKeyJustPressed(int keycode) — была ли клавиша ТОЛЬКО ЧТО нажата?
     * <p>
     * В libGDX есть встроенный метод, мы его оборачиваем для простоты.
     *
     * @param keycode код клавиши (Input.Keys.SPACE, Input.Keys.LEFT и т.д.)
     * @return true если клавиша нажата именно в этом кадре (не в прошлом)
     */
    private boolean isKeyJustPressed(int keycode) {
        return Gdx.input.isKeyJustPressed(keycode);
    }

    /**
     * isMouseJustClicked() — был ли клик ТОЛЬКО ЧТО?
     * <p>
     * Логика: если мышь нажата ТЕ ПЕРЬ, но НЕ была нажата в прошлом кадре —
     * это значит, что клик произошёл прямо сейчас.
     *
     * @return true если клик произошёл в этом кадре
     */
    private boolean isMouseJustClicked() {
        // Текущее состояние мыши
        boolean currentMousePressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        // Был ли только что клик?
        // Логика: если сейчас нажата И раньше не была нажата → это новый клик
        boolean justClicked = currentMousePressed && !wasMouse;

        // Обновляем состояние для следующего кадра
        wasMouse = currentMousePressed;

        return justClicked;
    }

    /**
     * isTouchJustPressed() — было ли касание ТОЛЬКО ЧТО?
     * <p>
     * На сенсорных устройствах нет встроенного "just pressed", поэтому
     * мы отслеживаем переход из "нет касания" в "есть касание".
     * <p>
     * Логика аналогична мыши:
     * - Если касание есть ТЕПЕРЬ, но НЕ было в прошлом кадре → новое касание
     *
     * @return true если касание произошло в этом кадре
     */
    private boolean isTouchJustPressed() {
        boolean currentTouch = Gdx.input.isTouched();  // ← правильно
        boolean justTouched = currentTouch && !wasTouch;
        wasTouch = currentTouch;
        return justTouched;
    }

    // ============== ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ (ПОЗЖЕ) ==============

    /**
     * getMoveX() — получить смещение по оси X (для аналогового стика)
     * <p>
     * Позже можно использовать для сенсорного управления
     * (если игрок тянет палец влево-вправо)
     *
     * @return значение от -1 (влево) до 1 (вправо)
     */
    public float getMoveX() {
        // Пока просто проверяем клавиши
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            return -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            return 1;
        }
        return 0;
    }

    /**
     * getTouchX() — получить координату X касания
     * <p>
     * Позже можно использовать для определения, где игрок коснулся экрана
     *
     * @return X координата первого касания (или -1, если нет касаний)
     */
    public float getTouchX() {
        if (Gdx.input.isTouched()) {
            return Gdx.input.getX(0);  // 0 = первое касание
        }
        return Gdx.input.getX(0);
    }

    /**
     * getTouchY() — получить координату Y касания
     *
     * @return Y координата первого касания (или -1, если нет касаний)
     */
    public float getTouchY() {
        if (Gdx.input.isTouched()) {
            return Gdx.input.getY(0);
        }
        return Gdx.input.getY(0);
    }
}
