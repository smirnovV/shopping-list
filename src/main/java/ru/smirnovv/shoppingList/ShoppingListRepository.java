package ru.smirnovv.shoppingList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий, управляющий списком покупок.
 */
public interface ShoppingListRepository extends JpaRepository<Purchase, Long> {
    /**
     * Возвращает список актуальных покупок.
     *
     * @param pageable информация о нумераций страниц.
     * @return список актуальных покупок.
     */
    Page<Purchase> findAllByActualIsTrue(Pageable pageable);

    /**
     * Возвращает список неактуальных покупок.
     *
     * @return список неактуальных покупок.
     */
    List<Purchase> findAllByActualFalse();
}
