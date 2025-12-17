package com.okvedTest.Okved;

import com.okvedTest.BestMatchResult;

import java.util.Comparator;
import java.util.Objects;

/**
 * Класс для поиска кода ОКВЭД с максимальным совпадением по окончанию телефонного номера.
 *
 * <p>Алгоритм:
 * <ol>
 *   <li>Сравниваем цифры номера и кода ОКВЭД с конца</li>
 *      *   <li>Выбираем ОКВЭД с максимальной длиной совпадения</li>
 *      *   <li>При равной длине совпадения приоритет отдаётся более детализированному коду
 *      *       (с большей длиной цифровой части)</li>
 *      *   <li>Если совпадений нет — применяем резервнкю стратегию</li>
 * </ol>
 */
public class OkvedMatcher {

    private static final String PHONE_PREFIX = "+7";

    /**
     * Находим код ОКВЭД с максимальным совпадением по окончанию номера.
     *
     * @param normalizedPhone нормализованный номер в формате {@code +79XXXXXXXXX}
     *      * @param okvedData данные справочника ОКВЭД
     *      * @return результат с найденным ОКВЭД и длиной совпадения
     *      * @throws IllegalArgumentException если входные параметры некорректны
     */
    public BestMatchResult findBestMatch(String normalizedPhone, OkvedData okvedData) {
        Objects.requireNonNull(normalizedPhone, "Телефонный номер не может быть null.");
        Objects.requireNonNull(okvedData, "Данные ОКВЭД не могут быть null.");

        if (!normalizedPhone.startsWith(PHONE_PREFIX)) {
            throw new IllegalArgumentException("Неверный формат нормализованного номера: " + normalizedPhone);
        }

        String phoneDigits = normalizedPhone.substring(PHONE_PREFIX.length());

        OkvedEntry bestMatch = null;
        int maxMatchLength = -1;

        for (OkvedEntry entry : okvedData.getEntries()) {
            String okvedDigits = entry.getDigitsOnly();
            int matchLength = getMatchLength(phoneDigits, okvedDigits);

            boolean isBetter =
                    matchLength > maxMatchLength ||
                            (matchLength == maxMatchLength &&
                                    bestMatch != null &&
                                    okvedDigits.length() > bestMatch.getDigitsOnly().length());

            if (isBetter) {
                bestMatch = entry;
                maxMatchLength = matchLength;
            }
        }

        if (bestMatch == null || maxMatchLength <= 0) {
            bestMatch = selectFallback(okvedData);
            maxMatchLength = 0;
        }

        return new BestMatchResult(bestMatch, maxMatchLength);
    }

    /**
     * Резервная стратегия выбора ОКВЭД.
     *
     * <p>В случае отсутствия совпадений выбираем
     * наиболее общий код (с минимальной длиной цифровой части).
     *
     * @param okvedData данные справочника ОКВЭД
     * @return выбранная запись ОКВЭД
     */
    private OkvedEntry selectFallback(OkvedData okvedData) {
        return okvedData.getEntries().stream()
                .min(Comparator.comparingInt(e -> e.getDigitsOnly().length()))
                .orElseThrow(() ->
                        new IllegalStateException("Справочник ОКВЭД не содержит записей."));
    }

    /**
     * Вычисляем длину совпадения двух строк с конца.
     *
     * @param phoneDigits цифры телефонного номера
     * @param okvedDigits цифры кода ОКВЭД
     * @return количество совпавших символов с конца
     */
    private int getMatchLength(String phoneDigits, String okvedDigits) {
        int matchLength = 0;

        for (int i = 1; i <= Math.min(phoneDigits.length(), okvedDigits.length()); i++) {
            if (phoneDigits.charAt(phoneDigits.length() - i)
                    == okvedDigits.charAt(okvedDigits.length() - i)) {
                matchLength++;
            } else {
                break;
            }
        }

        return matchLength;
    }
}