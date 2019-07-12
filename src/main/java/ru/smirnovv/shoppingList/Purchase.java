package ru.smirnovv.shoppingList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Сущность, представляющая зарегестрированные покупки.
 */
@SuppressWarnings("magicNumber")
@Entity
public class Purchase {
    /**
     * Id покупки.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Название покупки.
     */
    @NotEmpty
    @Size(min = 1, max = 50)
    private String title;

    /**
     * Актуальность покупки.
     */
    private boolean actual;

    /**
     * Дата последней покупки.
     */
    private Date date;

    /**
     * Период покупки в днях. (0 - покупка одноразовая)
     */
    @PositiveOrZero
    private Long period;

    /**
     * Создает экземпляр с внедренными зависимостями.
     * Новая покупка является активной и одноразовой.
     *
     * @param title название покупки.
     */
    public Purchase(@NotEmpty @Size(min = 1, max = 50) final String title) {
        this.title = title;
        actual = true;
        period = 0L;
    }

    /**
     * Конструктор по умолчанию.
     */
    public Purchase() {
    }

    /**
     * Возвращает id покупки.
     *
     * @return id покупки.
     */
    public final Long getId() {
        return id;
    }

    /**
     * Возвращает название покупки.
     *
     * @return название покупки.
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Обновляет название покупки.
     *
     * @param title название покупки.
     */
    public final void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Возвращает актуальность покупки.
     *
     * @return актуальность покупки.
     */
    public final boolean isActual() {
        return actual;
    }

    /**
     * Обновляет актуальность покупки.
     *
     * @param actual актуальность покупки.
     */
    public final void setActual(final boolean actual) {
        this.actual = actual;
    }

    /**
     * Возвращает дату последней покупки.
     *
     * @return дата последней покупки.
     */
    public final Date getDate() {
        if (date == null) {
            return null;
        }

        return new Date(date.getTime());
    }

    /**
     * Обновляет дату последней покупки.
     *
     * @param date дата последней покупки.
     */
    public final void setDate(final Date date) {
        this.date = new Date(date.getTime());
    }

    /**
     * возвращает период покупок.
     *
     * @return период покупок.
     */
    public final Long getPeriod() {
        return period;
    }

    /**
     * Обновляет период покупок.
     *
     * @param period период покупок.
     */
    public final void setPeriod(final Long period) {
        this.period = period;
    }
}
