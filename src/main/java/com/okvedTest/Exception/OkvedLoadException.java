package com.okvedTest.Exception;

/**
 * Исключение, возникающее при ошибке загрузки или парсинга справочника ОКВЭД.
 *
 * <p>Это исключение выбрасывается классом OkvedLoader в случаях:
 * <ul>
 *   <li>Ошибка сетевого подключения</li>
 *   <li>HTTP ошибка (не 200 OK)</li>
 *   <li>Невалидный JSON</li>
 *   <li>Пустой справочник</li>
 * </ul>
 */
public class OkvedLoadException extends Exception {

    public OkvedLoadException(String message) {
        super(message);
    }

    public OkvedLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
