package com.okvedTest;

/**
 * Класс для вывода результатов работы приложения.
 *
 * <p>Выводим в консоль:
 * <ul>
 *   <li>Нормализованный телефонный номер</li>
 *   <li>Найденный код ОКВЭД</li>
 *   <li>Название вида деятельности</li>
 *   <li>Длину совпадения</li>
 *   <li>Предупреждение, если использована резервная стратегия</li>
 * </ul>
 */
public class ResultPrinter {

    /**
     * Выводим результат поиска в консоль.
     *
     * <p>Пример вывода:
     * <pre>
     * ===== РЕЗУЛЬТАТ =====
     * Нормализованный номер: +79123456789
     * Найденный ОКВЭД: 56.789
     * Название: Деятельность ресторанов и услуги по доставке продуктов питания
     * Длина совпадения: 5 символов
     * </pre>
     *
     * @param normalizedPhone нормализованный телефонный номер
     * @param result результат поиска ОКВЭД
     */
    public void print(String normalizedPhone, BestMatchResult result) {
        System.out.println();
        System.out.println("===== РЕЗУЛЬТАТ =====");
        System.out.println("Нормализованный номер: " + normalizedPhone);
        System.out.println("Найденный ОКВЭД: " + result.getEntry().getCode());
        System.out.println("Название: " + result.getEntry().getName());
        System.out.println("Длина совпадения: " + result.getMatchLength() + " символов");

        if (result.isFallback()) {
            System.out.println();
            System.out.println("Совпадений не найдено. Применена резервная стратегия.");
        }

        System.out.println("=====================");
    }
}