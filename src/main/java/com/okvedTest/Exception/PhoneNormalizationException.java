package com.okvedTest.Exception;

/**
 * Исключение, возникающее при невозможности нормализовать телефонный номер.
 *
 * <p>Это исключение выбрасывается классом PhoneNormalizer в случаях:
 * <ul>
 *   <li>Пустой или null номер</li>
 *   <li>Неверная длина номера</li>
 *   <li>Неверный формат (не российский номер)</li>
 *   <li>Не мобильный номер (не начинается с 9 после кода страны)</li>
 * </ul>
 */
public class PhoneNormalizationException extends Exception {

    public PhoneNormalizationException(String message) {
        super(message);
    }

    public PhoneNormalizationException(String message, Throwable cause) {
        super(message, cause);
    }
}