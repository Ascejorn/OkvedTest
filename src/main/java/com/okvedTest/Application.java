package com.okvedTest;

import com.okvedTest.Exception.OkvedLoadException;
import com.okvedTest.Exception.PhoneNormalizationException;
import com.okvedTest.Okved.OkvedData;
import com.okvedTest.Okved.OkvedLoader;
import com.okvedTest.Okved.OkvedMatcher;

import java.util.Scanner;

public class Application {

    /**
     * URL для загрузки актуального справочника ОКВЭД.
     */
    private static final String OKVED_URL =
            "https://raw.githubusercontent.com/bergstar/testcase/master/okved.json";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("=== Поиск ОКВЭД ===");
            System.out.println("Введите российский мобильный номер:");

            String input = scanner.nextLine();

            run(input);

        } catch (PhoneNormalizationException e) {
            System.err.println("Ошибка нормализации номера: " + e.getMessage());
        } catch (OkvedLoadException e) {
            System.err.println("Ошибка загрузки ОКВЭД: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка выполнения приложения.");
            e.printStackTrace();
        }
    }

    /**
     * Основной сценарий выполнения приложения.
     *
     * @param input исходная строка с телефонным номером
     * @throws PhoneNormalizationException если номер невозможно нормализовать
     * @throws OkvedLoadException если не удалось загрузить справочник ОКВЭД
     */
    private static void run(String input)
            throws PhoneNormalizationException, OkvedLoadException {

        // Шаг 1: Нормализация номера
        PhoneNormalizer normalizer = new PhoneNormalizer();
        String normalizedPhone = normalizer.normalize(input);

        // Шаг 2: Загрузка ОКВЭД
        OkvedLoader loader = new OkvedLoader();
        OkvedData okvedData = loader.loadFromUrl(OKVED_URL);

        // Шаг 3: Поиск ОКВЭД по окончанию номера
        OkvedMatcher matcher = new OkvedMatcher();
        BestMatchResult result = matcher.findBestMatch(normalizedPhone, okvedData);

        // Шаг 4: Вывод результата
        ResultPrinter printer = new ResultPrinter();
        printer.print(normalizedPhone, result);
    }
}