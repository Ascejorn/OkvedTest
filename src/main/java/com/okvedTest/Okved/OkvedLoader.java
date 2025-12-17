package com.okvedTest.Okved;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.okvedTest.Exception.OkvedLoadException;

/**
 * Класс для загрузки справочника ОКВЭД из внешнего источника.
 *
 * <p>Загружаем JSON-файл по HTTPS и парсим его в структурированный объект.
 * Используем библиотеку Gson для работы с JSON.
 *
 * @see <a href="https://github.com/google/gson">Gson на GitHub</a>
 */
public class OkvedLoader {

    /**
     * Таймаут запроса.
     */
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final Gson gson;

    private final HttpClient httpClient;

    /**
     * Создаём новый загрузчик ОКВЭД.
     */
    public OkvedLoader() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
        this.gson = new Gson();
    }

    /**
     * Загружаем справочник ОКВЭД по указанному URL.
     *
     * @param urlString URL для загрузки JSON-файла
     * @return объект с данными ОКВЭД
     * @throws OkvedLoadException если произошла ошибка загрузки или парсинга
     */
    public OkvedData loadFromUrl(String urlString) throws OkvedLoadException {
        try {
            String json = downloadJson(urlString);
            return parseJson(json);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OkvedLoadException(
                    "Загрузка ОКВЭД была прервана.", e
            );
        } catch (IOException e) {
            throw new OkvedLoadException(
                    "Ошибка загрузки ОКВЭД из " + urlString, e
            );
        }
    }

    /**
     * Скачиваем JSON-файл по указанному URL.
     *
     * @param urlString URL для загрузки
     * @return содержимое файла в виде строки
     * @throws IOException если произошла ошибка сети
     */
    private String downloadJson(String urlString) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .timeout(TIMEOUT)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new IOException("HTTP ошибка: " + statusCode + ", Тело: " + response.body());
        }

        return response.body();
    }

    /**
     * Парсим JSON-строку в структурированные данные ОКВЭД.
     *
     * <p>Рекурсивно обходим иерархическую структуру и извлекаем все коды.
     *
     * @param json JSON-строка для парсинга
     * @return объект с данными ОКВЭД
     * @throws OkvedLoadException если JSON невалиден
     */
    private OkvedData parseJson(String json) throws OkvedLoadException {
        try {
            JsonElement rootElement = gson.fromJson(json, JsonElement.class);
            if (!rootElement.isJsonArray()) {
                throw new OkvedLoadException("Некорректный формат ОКВЭД: ожидался JSON-массив.");
            }
            JsonArray root = rootElement.getAsJsonArray();
            List<OkvedEntry> entries = new ArrayList<>();

            for (JsonElement section : root) {
                parseSection(section.getAsJsonObject(), entries);
            }

            if (entries.isEmpty()) {
                throw new OkvedLoadException("ОКВЭД файл не содержит записей.");
            }

            return new OkvedData(entries);
        } catch (Exception e) {
            throw new OkvedLoadException("Ошибка парсинга JSON.", e);
        }
    }

    /**
     * Рекурсивно парсим секцию ОКВЭД и добавляем записи в список.
     *
     * @param obj     JSON-объект секции
     * @param entries список для добавления записей
     */
    private void parseSection(JsonObject obj, List<OkvedEntry> entries) {
        if (obj.has("code") && obj.has("name")) {
            String code = obj.get("code").getAsString();
            String name = obj.get("name").getAsString();

            // Пропускаем разделы (содержат только буквы и пробелы)
            boolean hasDigit = code.chars().anyMatch(Character::isDigit);
            if (hasDigit) {
                entries.add(new OkvedEntry(code, name));
            }
        }

        // Рекурсивно обрабатываем вложенные элементы
        if (obj.has("items") && obj.get("items").isJsonArray()) {
            JsonArray items = obj.getAsJsonArray("items");
            for (JsonElement item : items) {
                if (item.isJsonObject()) {
                    parseSection(item.getAsJsonObject(), entries);
                }
            }
        }
    }
}
