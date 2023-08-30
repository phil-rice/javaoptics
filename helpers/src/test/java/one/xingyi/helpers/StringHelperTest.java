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

}