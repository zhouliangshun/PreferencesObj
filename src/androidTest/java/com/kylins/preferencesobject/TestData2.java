package com.kylins.preferencesobject;

import com.kylins.obj.pref.annotation.SharedPreferencesName;

import java.util.HashSet;

/**
 * Created by j-zhouliangshun on 2016/6/24.
 */
@SharedPreferencesName("com.kylins.preferencesobject.TestData")
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
