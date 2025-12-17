package com.okvedTest;

import com.okvedTest.Okved.OkvedEntry;

/**
 * Класс, представляющий результат поиска кода ОКВЭД.
 *
 * <p>Содержит найденную запись ОКВЭД и длину совпадения с телефонным номером.
 */
public class BestMatchResult {

    /**
     * Найденная запись ОКВЭД.
     */
    private final OkvedEntry entry;

    /**
     * Длина совпадения (количество совпавших символов с конца).
     */
    private final int matchLength;

    /**
     * Создаём новый результат поиска.
     *
     * @param entry найденная запись ОКВЭД
     * @param matchLength длина совпадения
     * @throws IllegalArgumentException если entry null или matchLength отрицательный
     */
    public BestMatchResult(OkvedEntry entry, int matchLength) {
        if (entry == null) {
            throw new IllegalArgumentException("Запись ОКВЭД не может быть null.");
        }
        if (matchLength < 0) {
            throw new IllegalArgumentException("Длина совпадения не может быть отрицательной.");
        }

        this.entry = entry;
        this.matchLength = matchLength;
    }

    /**
     * Возвращаем найденную запись ОКВЭД.
     *
     * @return запись ОКВЭД
     */
    public OkvedEntry getEntry() {
        return entry;
    }

    /**
     * Возвращаем длину совпадения.
     *
     * @return количество совпавших символов с конца
     */
    public int getMatchLength() {
        return matchLength;
    }

    /**
     * Проверяем, является ли результат резервным (совпадение = 0).
     *
     * @return true, если использована резервная стратегия
     */
    public boolean isFallback() {
        return matchLength == 0;
    }
}
