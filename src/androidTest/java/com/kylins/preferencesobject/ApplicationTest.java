package com.kylins.preferencesobject;

import android.test.ApplicationTestCase;

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

   public void testRight(){
        Data data = SharePref.getInstance(Data.class,getApplication());
        assertEquals(data.isF(),true);

        data.setF(true);
    }
}