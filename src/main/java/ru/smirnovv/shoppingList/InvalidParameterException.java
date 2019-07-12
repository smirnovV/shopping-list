package ru.smirnovv.shoppingList;

/**
 * Исключение, указывающее что переданный параметр не удовлетворяет условию.
 * Название покупки не может быть больше 50 символов и не может быть пустым.
 * Период покупки не может быть отрицательным числом.
 */
public class InvalidParameterException extends RuntimeException {
    /**
     * Создает экземпляр с информациоей об исключении.
     *
     * @param message сообщение об исключении.
     */
    public InvalidParameterException(final String message) {
        super(message);
    }
}
