package com.kylins.obj.pref;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;


/**
 * Created by zhouliangshun on 2016/6/22.
 */
class SharePreObject<T> implements InvocationHandler {

    private Class<T> mClass;
    private T object;
    private SharedPreferences mSharedPreferences;

    /**
     * Construct method
     *
     * @param objClass Data target class
     * @param context  Android application context
     */
    SharePreObject(Context context, Class<T> objClass) {
        this(objClass, context.getSharedPreferences(objClass.getName(), Application.MODE_PRIVATE));
    }

    /**
     * @param objClass          Data target class
     * @param sharedPreferences Android SharedPreferences instance
     */
    SharePreObject(Class<T> objClass, SharedPreferences sharedPreferences) {
        mClass = objClass;
        try {
            mSharedPreferences = sharedPreferences;
            Object obj = Proxy.newProxyInstance(objClass.getClassLoader(), new Class[]{objClass}, this);

            if (obj != null) {
                object = (T) obj;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void destroy() {
        object = null;
        mSharedPreferences = null;
    }

    T get() {
        return object;
    }

    /**
     * process get value
     *
     * @param method
     * @param args
     * @return
     */
    private Object getFieldValue(Method method, Object[] args) {
        //get file name
        String fieldName = method.getName().substring(3);
        //get return value type
        Class type = method.getReturnType();
        if (!TextUtils.isEmpty(fieldName)) {
            //check the file is in share preference
            try {
                switch (type.getSimpleName()) {
                    case "String":
                        return mSharedPreferences.getString(fieldName, null);
                    case "Integer":
                    case "int":
                        return mSharedPreferences.getInt(fieldName, 0);
                    case "Boolean":
                    case "boolean":
                        return mSharedPreferences.getBoolean(fieldName, false);
                    case "Long":
                    case "long":
                        return mSharedPreferences.getLong(fieldName, 0);
                    case "Float":
                    case "float":
                        return mSharedPreferences.getFloat(fieldName, 0.0f);
                }

                if (Set.class.isAssignableFrom(type)) {//check is set
                    try {
                        Set<String> newSet = (Set<String>) type.newInstance();
                        Set<String> oldSet = mSharedPreferences.getStringSet(fieldName, null);
                        if (oldSet != null) {
                            newSet.addAll(oldSet);
                        }
                        return newSet;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        throw new RuntimeException("Not support this type for SharePre Bean:" + type.getName());
    }

    /**
     * process set value
     *
     * @param method
     * @param args
     */
    private void setFieldValue(Method method, Object[] args) {
        //get file name
        String fieldName = method.getName().substring(3);

        // new value is null,remove from store
        if (args.length == 0 || args[0] == null) {
            if (mSharedPreferences.contains(fieldName)) {
                mSharedPreferences.edit().remove(fieldName).apply();
            }
            return;
        }

        Class type = args[0].getClass();
        if (!TextUtils.isEmpty(fieldName)) {
            //check the name is exits
            try {
                //保存对象
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                switch (type.getSimpleName()) {
                    case "String": {
                        editor.putString(fieldName, (String) args[0]);
                        editor.apply();
                    }
                    return;
                    case "Integer":
                    case "int": {
                        editor.putInt(fieldName, (Integer) args[0]);
                        editor.apply();
                    }
                    return;
                    case "Boolean":
                    case "boolean": {
                        editor.putBoolean(fieldName, (Boolean) args[0]);
                        editor.apply();
                    }
                    return;
                    case "Long":
                    case "long": {
                        editor.putLong(fieldName, (Long) args[0]);
                        editor.apply();
                    }
                    return;
                    case "Float":
                    case "float": {
                        editor.putFloat(fieldName, (Float) args[0]);
                        editor.apply();
                    }
                    return;
                }

                if (Set.class.isAssignableFrom(type)) {//check is set
                    try {
                        editor.putStringSet(fieldName, (Set<String>) args[0]);
                        editor.apply();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        throw new RuntimeException("Not support this type for SharePre Bean:" + type.getName());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // If the method is a method from Object then defer to normal invocation.
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        String methodName = method.getName();
        if ((args == null || args.length == 0) && methodName.startsWith("get")) {
            return getFieldValue(method, args);
        }

        if (args != null && args.length == 1 && (methodName.startsWith("set"))) {
            setFieldValue(method, args);
        }

        return null;
    }
}