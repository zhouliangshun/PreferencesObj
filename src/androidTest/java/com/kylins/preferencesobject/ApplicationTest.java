package com.kylins.preferencesobject;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.kylins.obj.pref.SharePref;

import java.util.HashSet;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @LargeTest
    public void testRight() {
        TestData testData = SharePref.getInstance(TestData.class, getApplication());
        testData.setA(1);

        HashSet<Integer> ints = new HashSet<>();
        ints.add(1);
        ints.add(2);

        testData.setB(ints);

        HashSet<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        testData.setC(strings);

        testData.setD(1.0f);
        testData.setE(10000);
        testData.setF(true);

        SharePref.destroy();
        testData = SharePref.getInstance(TestData.class, getApplication());
        HashSet<Integer> my = testData.getB();

        assertEquals(testData.getA(),1);
        //assertEquals(testData.getB(),ints);
        assertEquals(testData.getC(),strings);
        assertEquals(testData.getD(),1.0f);
        assertEquals(testData.getE(),10000);
        assertEquals(testData.getF(), true);

        SharePref.destroy();

    }
}