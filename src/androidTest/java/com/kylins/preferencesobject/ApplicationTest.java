package com.kylins.preferencesobject;

import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.kylins.obj.pref.SharePref;

import java.util.HashSet;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<TestApplication> {
    public ApplicationTest() {
        super(TestApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
    }

    @LargeTest
    public void testRight() {
        TestData testData = SharePref.getInstance(TestData.class, getApplication());
        testData.setA(1);
        testData.setB(1.0);

        HashSet<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        testData.setC(strings);

        testData.setD(1.0f);
        testData.setE(10000);
        testData.setF(true);

        assertEquals(testData.getA(),1);
        assertEquals(testData.getB(),1.0);
        assertEquals(testData.getC(),strings);
        assertEquals(testData.getD(),1.0f);
        assertEquals(testData.getE(),10000);
        assertEquals(testData.isF(), true);

        SharePref.destory();

    }
}