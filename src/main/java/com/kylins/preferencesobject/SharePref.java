package com.kylins.preferencesobject;

/**
 * 保存类里的属性到SharedPreferences，只支持String,StringSet,int,long,boolean,float
 */

import android.content.Context;

import java.util.HashMap;

/**
 * Created by j-zhouliangshun on 2016/6/24.
 */
public class SharePref {

    private static HashMap<Class,Object> instances = new HashMap<>();

    protected Context context;

    /**
     * 获取需要持久化的对象的单列模式
     * @param beanClass 类的class
     * @param context Android程序的上下文
     * @param <T> 持久化对象类型
     * @return 返回单例对象
     */
    public static <T>  T getInstance(Class<T> beanClass,Context context) {
        if(instances.containsKey(beanClass)){
            return (T)instances.get(beanClass);
        }

        SharePreObject<T> object = new SharePreObject(beanClass,context);
        if(object.getObjcet()!=null)
            instances.put(beanClass,object.getObjcet());

        return object.getObjcet();
    }

}
