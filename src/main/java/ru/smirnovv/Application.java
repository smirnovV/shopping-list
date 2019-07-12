package ru.smirnovv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Сервис управления списком покупок с возможностью планирования.
 */
@SuppressWarnings("hideUtilityClassConstructor")
@SpringBootApplication
public class Application {
    /**
     * Точка входа.
     *
     * @param args программные аргументы.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Класс, обрабатывающий глобальные исключения.
     */
    @ControllerAdvice
    public static class GlobalExceptionHandler {
        /**
         * Обрабатывает исключения и возвращает ответ с информацией об ошибке.
         *
         * @param request   запрос, где произошла ошибка.
         * @param exception выброшенная ошибка.
         * @return ответ с информациоей об ошибке.
         * @see ErrorType
         */
        @ExceptionHandler(Exception.class)
        public final ResponseEntity<ErrorType> internalServerException(
                final HttpServletRequest request, final Exception exception) {
            return new ResponseEntity<>(
                    new ErrorType(request.getRequestURI(), INTERNAL_SERVER_ERROR.value(), exception.getMessage()),
                    INTERNAL_SERVER_ERROR);
        }

        /**
         * Обрабатывает {@link MissingServletRequestParameterException} и возвращает ответ с информацией об ошибке.
         *
         * @param request   запрос, где произошла ошибка.
         * @param exception выброшенная ошибка.
         * @return ответ с информациоей об ошибке.
         * @see ErrorType
         */
        @ExceptionHandler(MissingServletRequestParameterException.class)
        public final ResponseEntity<ErrorType> missingRequestParameterException(
                final HttpServletRequest request, final MissingServletRequestParameterException exception) {
            return new ResponseEntity<>(
                    new ErrorType(request.getRequestURI(), BAD_REQUEST.value(), exception.getMessage()),
                    BAD_REQUEST);
        }
    }
}
