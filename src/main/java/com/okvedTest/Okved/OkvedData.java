package com.okvedTest.Okved;

import java.util.Collections;
import java.util.List;

/**
 * Класс-контейнер для хранения данных справочника ОКВЭД.
 *
 * <p>Содержит список всех кодов ОКВЭД, загруженных из источника.
 * Обеспечивает неизменяемость данных после создания.
 */
public class OkvedData {

    /**
     * Неизменяемый список записей ОКВЭД.
     */
    private final List<OkvedEntry> entries;

    /**
     * Создаём новый объект с данными ОКВЭД.
     *
     * @param entries список записей ОКВЭД (будет обёрнут в неизменяемый список)
     * @throws IllegalArgumentException если список null или пуст
     */
    public OkvedData(List<OkvedEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            throw new IllegalArgumentException("Список ОКВЭД не может быть пустым.");
        }
        this.entries = Collections.unmodifiableList(entries);
    }

    /**
     * Возвращаем неизменяемый список всех записей ОКВЭД.
     *
     * @return список записей ОКВЭД
     */
    public List<OkvedEntry> getEntries() {
        return entries;
    }

    /**
     * Возвращаем количество записей в справочнике.
     *
     * @return количество записей
     */
    public int size() {
        return entries.size();
    }
}