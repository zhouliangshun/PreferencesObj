package com.kylins.obj.pref;

import android.os.Build;

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

        TestData testData = SharePref.getInstance(TestData.class, RuntimeEnvironment.application);
        testData.setA(1);
        testData.setB(1.0);

        HashSet<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        testData.setC(strings);

        testData.setD(1.0f);
        testData.setE(10000);
        testData.setF(true);

        assertEquals(testData.getA(), 1);
        assertEquals(testData.getB(), 1.0);
        assertEquals(testData.getC(), strings);
        assertEquals(testData.getD(), 1.0f);
        assertEquals(testData.getE(), 10000);
        assertEquals(testData.isF(), true);

        SharePref.destroy();

    }

    public interface TestData {

        int getA();

        void setA(int a);

        double getB();

        void setB(double b);

        HashSet<String> getC();

        void setC(HashSet<String> c);

        float getD();

        void setD(float d);

        long getE();

        void setE(long e);

        boolean isF();

        void setF(boolean f);
    }

}