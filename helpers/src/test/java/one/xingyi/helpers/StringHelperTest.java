package one.xingyi.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static one.xingyi.helpers.StringHelper.*;

class StringHelperTest {

    @Test
    void testQuoteIfNeeded() {
        assertEquals("1", quoteIfNeeded(1));
        assertEquals("1", quoteIfNeeded(1L));
        assertEquals("\"1\"", quoteIfNeeded("1"));
    }

    @Test
    void testToAv() {
        assertEquals("'a':1,'b':2", toSingleQuotes(toAttributeValue(",", "a", 1, "b", 2)));
    }

    @Test
    void testToJsonObject() {
        assertEquals("{'a':1,'b':'two'}", toSingleQuotes(toJsonObject("a", 1, "b", "two")));
    }

    @Test
    void testBrackets() {
        assertEquals("{a}", brackets.apply("a"));
    }

    @Test
    void testDoubleQuote() {
        assertEquals("\"a\"", doubleQuote.apply("a"));
    }
    @Test
    public void testSanitizeForObjectName() {
        assertEquals("example_com", StringHelper.sanitizeForObjectName("example.com"));
        assertEquals("example_com_123", StringHelper.sanitizeForObjectName("example.com/123"));
        assertEquals("https_example_com", StringHelper.sanitizeForObjectName("https://example.com"));
        assertEquals("special_characters_____", StringHelper.sanitizeForObjectName("special_characters!@#$%"));
        assertEquals("mixed_case_Example_COM", StringHelper.sanitizeForObjectName("mixed-case_Example.COM"));
        assertEquals("only_numbers_1234567890", StringHelper.sanitizeForObjectName("only-numbers_1234567890"));
    }

}