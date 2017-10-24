package com.kylins.obj.pref;

import android.app.Application;

import com.kylins.obj.pref.annotation.SharedPreferencesName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by developer on 04/11/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SharePrefTest {
    @Test
    public void getInstance() throws Exception {

        TestData testData = SharePref.getInstance(RuntimeEnvironment.application, TestData.class);
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
        testData = SharePref.getInstance(RuntimeEnvironment.application, TestData.class);
        Set<Integer> my = testData.getIntSet();

        assertEquals(1, testData.getInt());
        assertEquals("123", testData.getString());
        assertEquals(strings, testData.getStringSet());
        assertEquals(ints, testData.getIntSet());
        assertEquals(1.0f, testData.getFloat(), 0.0f);
        assertEquals(10000, testData.getLong());
        assertEquals(true, testData.getBoolean());

        SharePref.destroy();

    }

    @Test
    public void testChangeSharePrefName() {
        TestData testData = SharePref.getInstance(RuntimeEnvironment.application, TestData.class);
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
        TestData2 testData2 = SharePref.getInstance(RuntimeEnvironment.application, TestData2.class);

        assertEquals(1, testData2.getInt());
        assertEquals("123", testData2.getString());
        assertEquals(strings, testData2.getStringSet());
        assertEquals(ints, testData2.getIntSet());
        assertEquals(1.0f, testData2.getFloat(), 0.0f);
        assertEquals(10000, testData2.getLong());
        assertEquals(true, testData2.getBoolean());

        SharePref.destroy();
    }

    @Test
    public void testChangeSharePrefInstance() {
        TestData testData = SharePref.getInstance(RuntimeEnvironment.application, TestData.class);
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
        TestData3 testData3 = SharePref.getInstance(RuntimeEnvironment.application, TestData3.class,
                RuntimeEnvironment.application.getSharedPreferences("com.kylins.preferencesobject.SharePrefTest$TestData", Application.MODE_PRIVATE));

        assertEquals(1, testData3.getInt());
        assertEquals("123", testData3.getString());
        assertEquals(strings, testData3.getStringSet());
        assertEquals(ints, testData3.getIntSet());
        assertEquals(1.0f, testData3.getFloat(), 0.0f);
        assertEquals(10000, testData3.getLong());
        assertEquals(true, testData3.getBoolean());

        SharePref.destroy();
    }


    public interface TestData {

        int getInt();

        void setInt(int a);

        HashSet<Integer> getIntSet();

        void setIntSet(HashSet<Integer> value);

        HashSet<String> getStringSet();

        void setStringSet(HashSet<String> value);

        String getString();

        void setString(String value);

        float getFloat();

        void setFloat(float value);

        long getLong();

        void setLong(long value);

        boolean getBoolean();

        void setBoolean(boolean value);
    }

    @SharedPreferencesName("com.kylins.preferencesobject.SharePrefTest$TestData")
    public interface TestData2 {

        int getInt();

        void setInt(int a);

        HashSet<Integer> getIntSet();

        void setIntSet(HashSet<Integer> value);

        HashSet<String> getStringSet();

        void setStringSet(HashSet<String> value);

        String getString();

        void setString(String value);

        float getFloat();

        void setFloat(float value);

        long getLong();

        void setLong(long value);

        boolean getBoolean();

        void setBoolean(boolean value);
    }

    public interface TestData3 {

        int getInt();

        void setInt(int a);

        HashSet<Integer> getIntSet();

        void setIntSet(HashSet<Integer> value);

        HashSet<String> getStringSet();

        void setStringSet(HashSet<String> value);

        String getString();

        void setString(String value);

        float getFloat();

        void setFloat(float value);

        long getLong();

        void setLong(long value);

        boolean getBoolean();

        void setBoolean(boolean value);
    }

}