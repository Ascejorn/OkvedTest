import com.okvedTest.Exception.PhoneNormalizationException;
import com.okvedTest.PhoneNormalizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для класса PhoneNormalizer.
 *
 * <p>Проверяем корректность нормализации телефонных номеров
 * в различных форматах и валидацию ошибочных входных данных.
 */
class PhoneNormalizerTest {

    private PhoneNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new PhoneNormalizer();
    }

    /**
     * Тест нормализации номера в формате +79XXXXXXXXX.
     */
    @Test
    void testNormalizeInternationalFormat() throws PhoneNormalizationException {
        String result = normalizer.normalize("+79123456789");
        assertEquals("+79123456789", result);
    }

    /**
     * Тест нормализации номера, начинающегося с 8.
     */
    @Test
    void testNormalizeStartingWith8() throws PhoneNormalizationException {
        String result = normalizer.normalize("89123456789");
        assertEquals("+79123456789", result);
    }

    /**
     * Тест нормализации номера без префикса.
     */
    @Test
    void testNormalizeWithout7() throws PhoneNormalizationException {
        String result = normalizer.normalize("79123456789");
        assertEquals("+79123456789", result);
    }

    /**
     * Тест нормализации номера с пробелами.
     */
    @Test
    void testNormalizeWithSpaces() throws PhoneNormalizationException {
        String result = normalizer.normalize("+7 912 345 67 89");
        assertEquals("+79123456789", result);
    }

    /**
     * Тест нормализации номера со скобками и дефисами.
     */
    @Test
    void testNormalizeWithParenthesesAndDashes() throws PhoneNormalizationException {
        String result = normalizer.normalize("8 (912) 345-67-89");
        assertEquals("+79123456789", result);
    }

    /**
     * Тест нормализации номера с 10 цифрами (без кода страны).
     */
    @Test
    void testNormalizeTenDigits() throws PhoneNormalizationException {
        String result = normalizer.normalize("9123456789");
        assertEquals("+79123456789", result);
    }

    /**
     * Тест с другим номером оператора (999).
     */
    @Test
    void testNormalizeOperatorCode999() throws PhoneNormalizationException {
        String result = normalizer.normalize("+79991234567");
        assertEquals("+79991234567", result);
    }

    /**
     * Тест с пустой строкой.
     */
    @Test
    void testNormalizeEmptyString() {
        PhoneNormalizationException exception = assertThrows(
                PhoneNormalizationException.class,
                () -> normalizer.normalize("")
        );
        assertTrue(exception.getMessage().contains("не может быть пустым"));
    }

    /**
     * Тест с null.
     */
    @Test
    void testNormalizeNull() {
        PhoneNormalizationException exception = assertThrows(
                PhoneNormalizationException.class,
                () -> normalizer.normalize(null)
        );
        assertTrue(exception.getMessage().contains("не может быть пустым"));
    }

    /**
     * Тест с неверной длиной (слишком короткий).
     */
    @Test
    void testNormalizeTooShort() {
        PhoneNormalizationException exception = assertThrows(
                PhoneNormalizationException.class,
                () -> normalizer.normalize("+7912345")
        );
        assertTrue(exception.getMessage().contains("Неверная длина"));
    }

    /**
     * Тест с неверной длиной (слишком длинный).
     */
    @Test
    void testNormalizeTooLong() {
        PhoneNormalizationException exception = assertThrows(
                PhoneNormalizationException.class,
                () -> normalizer.normalize("+791234567890000")
        );
        assertTrue(exception.getMessage().contains("Неверная длина"));
    }

    /**
     * Тест с неверным кодом страны (не 7 или 8).
     */
    @Test
    void testNormalizeWrongCountryCode() {
        PhoneNormalizationException exception = assertThrows(
                PhoneNormalizationException.class,
                () -> normalizer.normalize("+19123456789")
        );
        assertTrue(exception.getMessage().contains("должен начинаться с 7 или 8"));
    }

    /**
     * Тест с неверным кодом оператора (не мобильный).
     */
    @Test
    void testNormalizeNotMobileNumber() {
        PhoneNormalizationException exception = assertThrows(
                PhoneNormalizationException.class,
                () -> normalizer.normalize("+74951234567")
        );
        assertTrue(exception.getMessage().contains("не мобильный"));
    }

    /**
     * Тест с буквами в номере.
     */
    @Test
    void testNormalizeWithLetters() throws PhoneNormalizationException {
        // Буквы должны быть удалены, остаются только цифры
        String result = normalizer.normalize("+7 (912) ABC-345-67-89");
        assertEquals("+79123456789", result);
    }
}
