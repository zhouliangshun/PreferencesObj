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

        TestData testData = SharePref.getInstance(getContext(), TestData.class);
        testData.setInt(1);

        HashSet<Integer> ints = new HashSet<>();
        ints.add(1);
        ints.add(2);

        testData.setIntSet(ints);

        testData.setString("123");

        HashSet<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        testData.setStringSet(strings);

        testData.setFloat(1.0f);
        testData.setLong(10000);
        testData.setBoolean(true);

        SharePref.destroy();
        testData = SharePref.getInstance(getContext(), TestData.class);

        assertEquals(1, testData.getInt());
        assertEquals("123", testData.getString());
        assertEquals(strings, testData.getStringSet());
        assertEquals(ints, testData.getIntSet());
        assertEquals(1.0f, testData.getFloat());
        assertEquals(10000, testData.getLong());
        assertEquals(true, testData.getBoolean());

        SharePref.destroy();

    }

    @LargeTest
    public void testSetNullValue() {

        TestData testData = SharePref.getInstance(getContext(), TestData.class);

        HashSet<Integer> ints = new HashSet<>();
        ints.add(1);
        ints.add(2);
        testData.setIntSet(ints);

        testData.setString("123");

        HashSet<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        testData.setStringSet(strings);

        //set null value
        testData.setIntSet(null);
        testData.setString(null);
        testData.setStringSet(null);

        SharePref.destroy();

        testData = SharePref.getInstance(getContext(), TestData.class);

        assertEquals(null, testData.getIntSet());
        assertEquals(null, testData.getStringSet());
        assertEquals(null, testData.getString());

        SharePref.destroy();
    }

    @LargeTest
    public void testChangeSharePrefName() {
        TestData testData = SharePref.getInstance(getContext(), TestData.class);
        testData.setInt(1);

        HashSet<Integer> ints = new HashSet<>();
        ints.add(1);
        ints.add(2);

        testData.setIntSet(ints);

        testData.setString("123");

        HashSet<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        testData.setStringSet(strings);

        testData.setFloat(1.0f);
        testData.setLong(10000);
        testData.setBoolean(true);

        SharePref.destroy();
        TestData2 testData2 = SharePref.getInstance(getContext(), TestData2.class);

        assertEquals(1, testData2.getInt());
        assertEquals("123", testData2.getString());
        assertEquals(strings, testData2.getStringSet());
        assertEquals(ints, testData2.getIntSet());
        assertEquals(1.0f, testData2.getFloat());
        assertEquals(10000, testData2.getLong());
        assertEquals(true, testData2.getBoolean());

        SharePref.destroy();
    }

    @LargeTest
    public void testChangeSharePrefInstance() {
        TestData testData = SharePref.getInstance(getContext(), TestData.class);
        testData.setInt(1);

        HashSet<Integer> ints = new HashSet<>();
        ints.add(1);
        ints.add(2);

        testData.setIntSet(ints);

        testData.setString("123");

        HashSet<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        testData.setStringSet(strings);

        testData.setFloat(1.0f);
        testData.setLong(10000);
        testData.setBoolean(true);

        SharePref.destroy();
        TestData3 testData3 = SharePref.getInstance(getContext(), TestData3.class,
                getContext().getSharedPreferences("com.kylins.preferencesobject.TestData", Application.MODE_PRIVATE));

        assertEquals(1, testData3.getInt());
        assertEquals("123", testData3.getString());
        assertEquals(strings, testData3.getStringSet());
        assertEquals(ints, testData3.getIntSet());
        assertEquals(1.0f, testData3.getFloat());
        assertEquals(10000, testData3.getLong());
        assertEquals(true, testData3.getBoolean());

        SharePref.destroy();
    }
}