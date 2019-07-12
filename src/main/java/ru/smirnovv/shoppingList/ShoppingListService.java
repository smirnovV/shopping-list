package ru.smirnovv.shoppingList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.springframework.util.Assert.notNull;

/**
 * Сервис, управляющий списком покупок.
 */
@SuppressWarnings({"designForExtension", "magicNumber"})
@Service
public class ShoppingListService {
    /**
     * Репозиторий, управляющий списком покупок.
     */
    private ShoppingListRepository shoppingListRepository;

    /**
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param shoppingListRepository репозиторий, управляющий списком покупок.
     */
    @Autowired
    public ShoppingListService(final ShoppingListRepository shoppingListRepository) {
        notNull(shoppingListRepository, "Argument 'shoppingListRepository' can not be null");
        this.shoppingListRepository = shoppingListRepository;
    }

    /**
     * Проверка актуальности записей покупок.
     * Если количество дней с последней покупки превышает период,
     * то товар становится актуальным для покупки.
     */
    @Transactional
    public void checkActual() {
        List<Purchase> list = shoppingListRepository.findAllByActualFalse();

        Date today = new Date();

        for (Purchase purchase : list) {
            if (purchase.getPeriod() != 0
                    && purchase.getDate() != null
                    && (today.getTime() - purchase.getDate().getTime()) / (3600 * 24 * 1000) > purchase.getPeriod()) {
                purchase.setActual(true);
                shoppingListRepository.save(purchase);
            }
        }
    }

    /**
     * Возвращает список покупок.
     *
     * @param pageable информация о нумераций страниц.
     * @return список покупок.
     */
    @Transactional
    public Page<Purchase> shoppingList(final Pageable pageable) {
        checkActual();

        return shoppingListRepository.findAll(pageable);
    }

    /**
     * Возвращает список актуальных покупок.
     *
     * @param pageable информация о нумераций страниц.
     * @return список актуальных покупок.
     */
    @Transactional
    public Page<Purchase> actualShoppingList(final Pageable pageable) {
        checkActual();

        return shoppingListRepository.findAllByActualIsTrue(pageable);
    }

    /**
     * Добавляет товар в список покупок.
     *
     * @param title название товара.
     * @return добавленная запись о покупке.
     * @throws InvalidParameterException выбрасывается если название покупки больше 50 символов или пустое.
     */
    @Transactional
    public Purchase add(final String title) throws InvalidParameterException {
        if (!(0 < title.length() && title.length() < 50)) {
            throw new InvalidParameterException(
                    "Invalid title! The title must be no longer than 50 characters and not empty");
        }

        return shoppingListRepository.save(new Purchase(title));
    }

    /**
     * Возвращает запись о покупке по id, если представлена.
     *
     * @param id id записи о покупке.
     * @return найденная запись о покупке.
     * @throws PurchaseNotFoundException выбрасывается, если запись о покупке с данным id не найдена.
     */
    @Transactional
    public Purchase getPurchaseById(final long id) throws PurchaseNotFoundException {
        Purchase purchase = shoppingListRepository.findById(id).orElseThrow(
                () -> new PurchaseNotFoundException("Purchase " + id + " not found."));

        Date today = new Date();

        if (!purchase.isActual()
                && purchase.getPeriod() != 0
                && purchase.getDate() != null
                && (today.getTime() - purchase.getDate().getTime()) / (3600 * 24 * 1000) > purchase.getPeriod()) {
            purchase.setActual(true);
            shoppingListRepository.save(purchase);
        }

        return purchase;
    }

    /**
     * Изменяет актуальность покупки.
     *
     * @param id id записи о покупке.
     * @return обновленная запись о покупке.
     * @throws PurchaseNotFoundException выбрасывается, если запись о покупке с данным id не найдена.
     */
    @Transactional
    public Purchase changeRelevance(final long id) throws PurchaseNotFoundException {
        Purchase purchase = shoppingListRepository.findById(id).orElseThrow(
                () -> new PurchaseNotFoundException("Purchase " + id + " not found."));

        if (purchase.isActual()) {
            purchase.setActual(false);
            purchase.setDate(new Date());
        } else {
            purchase.setActual(true);
        }

        return shoppingListRepository.save(purchase);
    }

    /**
     * Изменяет период покупки.
     *
     * @param id     id записи о покупке.
     * @param period период покупки.
     * @return обновленная запись о покупке.
     * @throws InvalidParameterException выбрасывается если период покупки отрицательный.
     */
    @Transactional
    public Purchase changePeriod(final long id, final long period) throws InvalidParameterException {
        Purchase purchase = shoppingListRepository.findById(id).orElseThrow(
                () -> new PurchaseNotFoundException("Purchase " + id + " not found."));

        if (period < 0) {
            throw new InvalidParameterException("Invalid period! The period must be not negative.");
        }
        purchase.setPeriod(period);

        return shoppingListRepository.save(purchase);
    }

    /**
     * Удаляет запись о покупке по id.
     *
     * @param id id записи о покупке.
     */
    @Transactional
    public void remove(final long id) {
        shoppingListRepository.deleteById(id);
    }
}
