package com.kylins.obj.pref;

/**
 * Save object to  SharedPreferences，only support type: String,Set<String>,int,long,boolean,float
 * Usage:
 * 1) Create a interface and write get and set for your metadata(the set<Name> and get<Name> must be same
 * 2) Use SharePref.getInstance get instance. and if you wang to save, just call object set method.
 * 3) You should call SharePref.destroy to release all singleton instance.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.kylins.obj.pref.annotation.SharedPreferencesName;

import java.util.HashMap;

/**
 * Created by zhouliangshun on 2016/6/24.
 */
public class SharePref {

    private static HashMap<String, SharePreObject> instances = new HashMap<>();

    /**
     * Get instance of yor class wil persistence to share preferences
     *
     * @param beanClass Class of persistence object
     * @param context   Android context
     * @param <T>       Generality type T of persistence object
     * @return Singleton instance of persistence object
     * @deprecated Please use new method <see>getInstance(Context context, Class<T> beanClass)</see>
     */
    public static <T> T getInstance(Class<T> beanClass, Context context) {
        if (instances.containsKey(beanClass)) {
            T obj = (T) instances.get(beanClass).get();
            if (obj != null) {
                return obj;
            }
            instances.remove(beanClass);
        }

        SharePreObject<T> object = new SharePreObject<T>(context, beanClass);
        if (object.get() == null) {
            throw new NullPointerException("Can't build class:" + beanClass.getName());
        }

        instances.put(beanClass.getName(), object);
        return object.get();
    }

    /**
     * Get instance of yor class wil persistence to share preferences
     *
     * @param beanClass Class of persistence object
     * @param context   Android context
     * @param <T>       Generality type T of persistence object
     * @return Singleton instance of persistence object
     */
    public static <T> T getInstance(Context context, Class<T> beanClass) {
        if (instances.containsKey(beanClass)) {
            T obj = (T) instances.get(beanClass).get();
            if (obj != null) {
                return obj;
            }
            instances.remove(beanClass);
        }

        SharedPreferencesName sharedPreferencesName = beanClass.getAnnotation(SharedPreferencesName.class);
        SharePreObject<T> object = null;
        if (sharedPreferencesName != null) {
            object = new SharePreObject<T>(context, beanClass, context.getSharedPreferences(sharedPreferencesName.value(), Application.MODE_PRIVATE));
        } else {
            object = new SharePreObject<T>(context, beanClass);
        }

        if (object == null || object.get() == null) {
            throw new NullPointerException("Can't build class:" + beanClass.getName());
        }

        instances.put(beanClass.getName(), object);
        return object.get();
    }

    /**
     * @param context           Android context
     * @param beanClass         Class of persistence object
     * @param sharedPreferences SharedPreferences instance, if don't default SharedPreferences
     * @param <T>               Generality type T of persistence object
     * @return Singleton instance of persistence object
     */
    public static <T> T getInstance(Context context, Class<T> beanClass, SharedPreferences sharedPreferences) {
        if (instances.containsKey(beanClass)) {
            T obj = (T) instances.get(beanClass).get();
            if (obj != null) {
                return obj;
            }
            instances.remove(beanClass);
        }

        SharePreObject<T> object = new SharePreObject<T>(context, beanClass, sharedPreferences);
        if (object.get() == null) {
            throw new NullPointerException("Can't build class:" + beanClass.getName());
        }

        instances.put(beanClass.getName(), object);
        return object.get();
    }


    /**
     * Destroy all singleton instance
     */
    public static void destroy() {
        for (HashMap.Entry<String, SharePreObject> entry : instances.entrySet()) {
            entry.getValue().destroy();
        }
        instances.clear();
    }

}
