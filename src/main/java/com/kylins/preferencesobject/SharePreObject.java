package com.kylins.preferencesobject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import leo.android.cglib.proxy.Enhancer;
import leo.android.cglib.proxy.MethodInterceptor;
import leo.android.cglib.proxy.MethodProxy;

/**
 * Created by j-zhouliangshun on 2016/6/22.
 */
class SharePreObject<T> implements MethodInterceptor {

    private Class<T> mClass;
    private T objcet;
    private Context mContext;

    public SharePreObject(Class<T> objClass, Context context) {
        mClass = objClass;
        mContext = context.getApplicationContext();

        try {
                Enhancer enhancer = new Enhancer(mContext);
            enhancer.setSuperclass(objClass);
            enhancer.setInterceptor(this);
            objcet  = (T)enhancer.create();
            initObject();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void initObject() throws IllegalAccessException {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(mClass.getName(), Application.MODE_PRIVATE);
        for(Field field:mClass.getDeclaredFields()){
            Class type = field.getType();
            String name = field.getName();
            if(sharedPreferences.contains(name)){
                switch (type.getSimpleName()){

                    case "StringSet": {
                        Set<String> value = sharedPreferences.getStringSet(name, null);
                        if (value != null) {
                            StringSet strSet = new StringSet();
                            strSet.addAll(value);
                            field.setAccessible(true);
                            field.set(objcet, strSet);
                        }
                    }
                    break;
                    case "String":
                    {
                        String value = sharedPreferences.getString(name,null);
                        if(value!=null){
                            field.setAccessible(true);
                            field.set(objcet,value);
                        }
                    }
                    break;
                    case "Integer":
                    case "int":{
                        int value = sharedPreferences.getInt(name,0);
                        if(value!=Integer.MAX_VALUE)
                            field.setAccessible(true);
                            field.set(objcet, value);
                    }
                    break;
                    case "Boolean":
                    case "boolean":{
                        boolean value = sharedPreferences.getBoolean(name,false);
                        field.setAccessible(true);
                        field.set(objcet,value);
                    }
                    break;
                    case "Long":
                    case "long":{
                        long value = sharedPreferences.getLong(name,0);
                        field.setAccessible(true);
                        field.set(objcet, value);
                    }
                    break;
                    case "Float":
                    case "float":{
                        float value = sharedPreferences.getFloat(name,0.0f);
                        field.setAccessible(true);
                        field.set(objcet,value);
                    }
                    break;
                }

            }

        }
    }

    protected T getObjcet()
    {
        return objcet;
    }

    @Override
    public Object intercept(Object o, Object[] args, MethodProxy methodProxy) throws Exception {
        Object object = methodProxy.invokeSuper(o,args);
        String methodName = methodProxy.getMethodName();
        if(args!=null&&args.length==1&&methodName.startsWith("set")){
            //获取变量名
            String fieldName = methodName.substring(3);
            if(!TextUtils.isEmpty(fieldName)){
                //检查名字是否存在
               try
               {
                   Field field = mClass.getDeclaredField(StringUtils.lowerFristChar(fieldName));
//                   Field field  = mClass.getField(fieldName);
                   //保存对象
                   SharedPreferences sharedPreferences = mContext.getSharedPreferences(mClass.getName(), Application.MODE_PRIVATE);
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   Class type = field.getType();
                   String name = field.getName();
                   switch (type.getSimpleName()){
                       case "StringSet": {
                           editor.putStringSet(name,(StringSet)args[0]);
                           editor.apply();
                       }
                       break;
                       case "String":
                       {
                           editor.putString(name,(String) args[0]);
                           editor.apply();
                       }
                       break;
                       case "Integer":
                       case "int":{
                           editor.putInt(name,(Integer) args[0]);
                           editor.apply();
                       }
                       break;
                       case "Boolean":
                       case "boolean":{
                           editor.putBoolean(name,(Boolean) args[0]);
                           editor.apply();
                       }
                       break;
                       case "Long":
                       case "long":{
                           editor.putLong(name,(Long) args[0]);
                           editor.apply();
                       }
                       break;
                       case "Float":
                       case "float":{
                           editor.putFloat(name,(Float) args[0]);
                           editor.apply();
                       }
                       break;
                   }
               }catch (Exception ex){
                   ex.printStackTrace();
               }
            }
        }

        return  object;
    }
}
