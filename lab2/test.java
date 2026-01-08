package org;

import org.example.StringProcess;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StringProcessTest {

    @Test
    void testTokenizeString() {
        StringProcess processor = new StringProcess("hello,world,java", ",");
        String result = processor.process();

        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
        assertTrue(result.contains("java"));
    }

    @Test
    void testFindNumbers() {
        StringProcess processor = new StringProcess("12.5 3.14 99.99 text", " ");
        processor.process();
        ArrayList<Double> numbers = processor.getNumbers();
        assertEquals(3, numbers.size());
        assertTrue(numbers.contains(12.5));
        assertTrue(numbers.contains(3.14));
        assertTrue(numbers.contains(99.99));
        assertFalse(numbers.contains("text"));
    }

    @Test
    void testFindTime() {
        StringProcess processor = new StringProcess("15-30 08-45 23-15 hello", " ");
        String result = processor.process();
        assertTrue(result.contains("15-30"));
        assertTrue(result.contains("08-45"));
        assertTrue(result.contains("23-15"));
    }

    @Test
    public void testTime() {
    StringProcess processor = new StringProcess("25-70 15-60", " ");
    processor.process();
    ArrayList<String> timeTokens = processor.getTimeTokens();
    assertFalse(timeTokens.contains("25-70"));
    assertFalse(timeTokens.contains("15-60"));
    assertTrue(timeTokens.isEmpty());
}

    @Test
    void testRemoveMinTok() {
        StringProcess processor = new StringProcess("test- java- hello world-", " ");
        String result = processor.process();
        assertTrue(result.contains("java-"));
    }

    @Test
    void testEmptyString() {
        StringProcess processor = new StringProcess("", ",");
        String result = processor.process();
        assertTrue(result.contains("Input string: "));
        assertTrue(result.contains("Separators: ,"));
    }

    @Test
    void testNoNumbers() {
        StringProcess processor = new StringProcess("hello world java", " ");
        String result = processor.process();
        assertTrue(result.contains("Double numbers: "));
    }

    @Test
    void testSeparators() {
        StringProcess processor = new StringProcess("a,b.c d;e", ",.; ");
        String result = processor.process();
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
        assertTrue(result.contains("d"));
        assertTrue(result.contains("e"));
    }
}