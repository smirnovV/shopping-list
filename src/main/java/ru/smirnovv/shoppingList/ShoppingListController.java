package ru.smirnovv.shoppingList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.smirnovv.ErrorType;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.util.Assert.notNull;

/**
 * Rest-контроллер, управляющий списком покупок.
 */
@RestController
@RequestMapping("/shoppinglist")
public class ShoppingListController {
    /**
     * Сервис, управляющий списком покупок.
     */
    private ShoppingListService shoppingListService;

    /**
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param shoppingListService сервис, управляющий списком покупок.
     */
    @Autowired
    public ShoppingListController(final ShoppingListService shoppingListService) {
        notNull(shoppingListService, "Argument 'shoppingListService' can not be null");
        this.shoppingListService = shoppingListService;
    }

    /**
     * Возвращает список покупок.
     *
     * @param pageable информация о нумераций страниц.
     * @return список покупок.
     */
    @GetMapping
    public final Page<Purchase> shoppingList(@PageableDefault(sort = "id") final Pageable pageable) {
        return shoppingListService.shoppingList(pageable);
    }

    /**
     * Возвращает список актуальных покупок.
     *
     * @param pageable информация о нумераций страниц.
     * @return список актуальных покупок.
     */
    @GetMapping("/actual")
    public final Page<Purchase> actualShoppingList(@PageableDefault(sort = "id") final Pageable pageable) {
        return shoppingListService.actualShoppingList(pageable);
    }

    /**
     * Добавляет товар в список покупок.
     *
     * @param title название товара.
     * @return добавленная запись о покупке.
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public final Purchase add(@RequestParam final String title) {
        return shoppingListService.add(title);
    }

    /**
     * Возвращает запись о покупке по id, если представлена.
     *
     * @param id id записи о покупке.
     * @return найденная запись о покупке.
     */
    @GetMapping("/{id}")
    public final Purchase getPurchaseById(@PathVariable final long id) {
        return shoppingListService.getPurchaseById(id);
    }

    /**
     * Изменяет актуальность покупки.
     *
     * @param id id записи о покупке.
     * @return обновленная запись о покупке.
     */
    @PostMapping("/{id}")
    public final Purchase changeRelevance(@PathVariable final long id) {
        return shoppingListService.changeRelevance(id);
    }

    /**
     * Изменяет период покупки.
     *
     * @param id     id записи о покупке.
     * @param period период покупки.
     * @return обновленная запись о покупке.
     */
    @PutMapping("/{id}")
    public final Purchase changePeriod(@PathVariable final long id, @RequestParam final long period) {
        return shoppingListService.changePeriod(id, period);
    }

    /**
     * Удаляет запись о покупке по id.
     *
     * @param id id записи о покупке.
     */
    @DeleteMapping("/{id}")
    public final void remove(@PathVariable final long id) {
        shoppingListService.remove(id);
    }

    /**
     * Обрабатывает {@link InvalidParameterException} и возвращает ответ с информацией об ошибке.
     *
     * @param request   запрос, где произошла ошибка.
     * @param exception выброшенная ошибка.
     * @return ответ с информациоей об ошибке.
     * @see ErrorType
     */
    @ExceptionHandler(InvalidParameterException.class)
    public final ResponseEntity<ErrorType> handleInvalidParameterException(
            final HttpServletRequest request, final InvalidParameterException exception) {
        return new ResponseEntity<>(
                new ErrorType(request.getRequestURI(), BAD_REQUEST.value(), exception.getMessage()),
                BAD_REQUEST);
    }

    /**
     * Обрабатывает {@link PurchaseNotFoundException} и возвращает ответ с информацией об ошибке.
     *
     * @param request   запрос, где произошла ошибка.
     * @param exception выброшенная ошибка.
     * @return ответ с информациоей об ошибке.
     * @see ErrorType
     */
    @ExceptionHandler(PurchaseNotFoundException.class)
    public final ResponseEntity<ErrorType> handlePurchaseNotFoundException(
            final HttpServletRequest request, final PurchaseNotFoundException exception) {
        return new ResponseEntity<>(
                new ErrorType(request.getRequestURI(), NOT_FOUND.value(), exception.getMessage()),
                NOT_FOUND);
    }
}
