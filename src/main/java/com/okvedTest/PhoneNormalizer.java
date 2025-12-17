package com.okvedTest;

import com.okvedTest.Exception.PhoneNormalizationException;

import java.util.regex.Pattern;

/**
 * Класс для нормализации российских мобильных номеров.
 *
 * <p>Принимаем номер в любом формате и приводим его к стандартному виду +79XXXXXXXXX.
 * Поддерживаемые форматы ввода:
 * <ul>
 *   <li>+79123456789</li>
 *   <li>89123456789</li>
 *   <li>79123456789</li>
 *   <li>+7 (912) 345-67-89</li>
 *   <li>8 912 345 67 89</li>
 *   <li>и другие варианты с пробелами, скобками, дефисами</li>
 * </ul>
 */
public class PhoneNormalizer {

    /**
     * Паттерн для извлечения только цифр из строки.
     */
    private static final Pattern DIGITS_ONLY = Pattern.compile("[^0-9]");

    /**
     * Нормализуем российский мобильный номер телефона.
     *
     * <p>Алгоритм:
     * <ol>
     *   <li>Удаляет все символы кроме цифр</li>
     *   <li>Проверяет длину (должно быть 10 или 11 цифр)</li>
     *   <li>Заменяет 8 на 7 в начале (если есть)</li>
     *   <li>Проверяет, что номер начинается с 7 и второй цифрой идёт 9</li>
     *   <li>Формирует итоговый формат +79XXXXXXXXX</li>
     * </ol>
     *
     * @param input исходная строка с номером телефона
     * @return нормализованный номер в формате +79XXXXXXXXX
     * @throws PhoneNormalizationException если номер невозможно нормализовать
     */
    public String normalize(String input) throws PhoneNormalizationException {
        if (input == null || input.isEmpty()) {
            throw new PhoneNormalizationException("Номер не может быть пустым.");
        }

        // Удаляем все кроме цифр
        String digitsOnly = DIGITS_ONLY.matcher(input).replaceAll("");

        // Проверяем длину
        if (digitsOnly.length() < 10 || digitsOnly.length() > 11) {
            throw new PhoneNormalizationException(
                    "Неверная длина номера: " + digitsOnly.length() + " цифр. Ожидается 10 или 11 цифр"
            );
        }

        // Нормализуем к 11 цифрам
        String normalized;
        if (digitsOnly.length() == 10) {
            // Если 10 цифр, добавляем 7 в начало
            normalized = "7" + digitsOnly;
        } else {
            // Если 11 цифр и начинается с 8, заменяем на 7
            if (digitsOnly.startsWith("8")) {
                normalized = "7" + digitsOnly.substring(1);
            } else {
                normalized = digitsOnly;
            }
        }

        // Проверяем, что начинается с 7
        if (!normalized.startsWith("7")) {
            throw new PhoneNormalizationException(
                    "Номер должен начинаться с 7 или 8. Получено: " + normalized.charAt(0)
            );
        }

        // Проверяем, что второй символ — 9 (код мобильного оператора)
        if (normalized.charAt(1) != '9') {
            throw new PhoneNormalizationException(
                    "Это не мобильный номер. Второй символ должен быть 9, получено: " + normalized.charAt(1)
            );
        }

        // Формируем результат с плюсом
        return "+" + normalized;
    }
}