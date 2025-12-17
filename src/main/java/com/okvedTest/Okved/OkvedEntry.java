package com.okvedTest.Okved;

/**
 * Класс, представляющий одну запись справочника ОКВЭД.
 *
 * <p>Содержит код ОКВЭД и его полное название.
 */
public class OkvedEntry {

    /**
     * Код ОКВЭД (например, "01.11.11").
     */
    private final String code;

    /**
     * Полное название вида деятельности.
     */
    private final String name;

    /**
     * Код ОКВЭД без точек (только цифры), используем для поиска совпадений.
     */
    private final String digitsOnly;

    /**
     * Создаём новую запись ОКВЭД.
     *
     * @param code код ОКВЭД
     * @param name название вида деятельности
     * @throws IllegalArgumentException если код или название null/пусты
     */
    public OkvedEntry(String code, String name) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Код ОКВЭД не может быть пустым.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Название ОКВЭД не может быть пустым.");
        }

        this.code = code;
        this.name = name;
        this.digitsOnly = code.replaceAll("[^0-9]", "");
    }

    /**
     * Возвращаем код ОКВЭД с точками.
     *
     * @return код ОКВЭД
     */
    public String getCode() {
        return code;
    }

    /**
     * Возвращаем название вида деятельности.
     *
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращаем код ОКВЭД без точек (только цифры).
     *
     * <p>Используем для поиска совпадений с телефонным номером.
     *
     * @return код без точек
     */
    public String getDigitsOnly() {
        return digitsOnly;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
