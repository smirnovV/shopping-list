package ru.smirnovv.shoppingList;

/**
 * Исключение, указывающее что необходимая запись о покупке не найдена.
 */
public class PurchaseNotFoundException extends RuntimeException {
    /**
     * Создает экземпляр с информациоей об исключении.
     *
     * @param message сообщение об исключении.
     */
    public PurchaseNotFoundException(final String message) {
        super(message);
    }
}
