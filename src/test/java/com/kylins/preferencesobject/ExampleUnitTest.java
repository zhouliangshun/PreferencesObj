package com.kylins.preferencesobject;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        HashSet<String> sh = new HashSet<>();
        System.out.printf(sh.getClass().getSimpleName());
        assertEquals(4, 2 + 2);
    }
}