package com.kylins.preferencesobject;

import java.util.HashSet;

/**
 * Created by j-zhouliangshun on 2016/6/24.
 */
public interface TestData {

    int getA();

    void setA(int a);

    HashSet<Integer> getB();
    void setB(HashSet<Integer> b);

    HashSet<String> getC();

    void setC(HashSet<String> c);

    float getD();

    void setD(float d);

    long getE();

    void setE(long e);

    boolean getF();

    void setF(boolean f);
}
