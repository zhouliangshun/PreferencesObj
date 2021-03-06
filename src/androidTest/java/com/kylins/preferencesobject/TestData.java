package com.kylins.preferencesobject;

import java.util.HashSet;

/**
 * Created by j-zhouliangshun on 2016/6/24.
 */
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
