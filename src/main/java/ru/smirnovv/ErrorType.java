package ru.smirnovv;

/**
 * Класс, содержащий информацию об ошибке.
 */
public class ErrorType {
    /**
     * Запрос, где произошла ошибка.
     */
    private final String url;

    /**
     * HTTP статус.
     */
    private final int status;

    /**
     * Детальное сообщение.
     */
    private final String message;

    /**
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param url     запрос, где произошла ошибка.
     * @param status  HTTP статус.
     * @param message детальное сообщение.
     */
    public ErrorType(final String url, final int status, final String message) {
        this.url = url;
        this.status = status;
        this.message = message;
    }

    /**
     * Возвращает запрос, где произошла ошибка.
     *
     * @return запрос, где произошла ошибка.
     */
    public final String getUrl() {
        return url;
    }

    /**
     * Возвращает HTTP статус.
     *
     * @return HTTP статус.
     */
    public final int getStatus() {
        return status;
    }

    /**
     * Возвращает детальное сообщение.
     *
     * @return детальное сообщение.
     */
    public final String getMessage() {
        return message;
    }
}
