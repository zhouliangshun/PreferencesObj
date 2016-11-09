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
    private SharedPreferences sharedPreferences;

    SharePreObject(Class<T> objClass, Context context) {
        mClass = objClass;
        try {
            sharedPreferences = context.getSharedPreferences(mClass.getName(), Application.MODE_PRIVATE);
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
        sharedPreferences = null;
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
                        return sharedPreferences.getString(fieldName, null);
                    case "Integer":
                    case "int":
                        return sharedPreferences.getInt(fieldName, 0);
                    case "Boolean":
                    case "boolean":
                        return sharedPreferences.getBoolean(fieldName, false);
                    case "Long":
                    case "long":
                        return sharedPreferences.getLong(fieldName, 0);
                    case "Float":
                    case "float":
                        return sharedPreferences.getFloat(fieldName, 0.0f);
                }

                if (Set.class.isAssignableFrom(type)) {//check is set
                    try {
                        Set<String> newSet = (Set<String>) type.newInstance();
                        Set<String> oldSet = sharedPreferences.getStringSet(fieldName, null);
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
        Class type = args[0].getClass();
        if (!TextUtils.isEmpty(fieldName)) {
            //check the name is exits
            try {
                //保存对象
                SharedPreferences.Editor editor = sharedPreferences.edit();
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