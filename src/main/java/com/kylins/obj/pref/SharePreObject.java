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
class SharePreObject<T> {

    private Class<T> mClass;
    private T object;
    private  SharedPreferences sharedPreferences;

    SharePreObject(Class<T> objClass, Context context) {
        mClass = objClass;
        try {
            sharedPreferences = context.getSharedPreferences(mClass.getName(), Application.MODE_PRIVATE);
            Object obj = Proxy.newProxyInstance(objClass.getClassLoader(), new Class[]{objClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    String methodName = method.getName();
                    if((args == null || args.length == 0)&&methodName.startsWith("get")){
                        Object obj = getFieldValue(method,args);
                        if(obj!=null){
                            return obj;
                        }
                    }

                    if (args != null && args.length == 1 && (methodName.startsWith("set"))) {
                        setFieldValue(method,args);
                        return null;
                    }

                    return method.invoke(proxy,args);
                }
            });

            if(obj != null){
                object = (T) obj;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void destroy(){
        object = null;
    }

    T get() {
        return object;
    }

    /**
     * process get bean
     * @param method
     * @param args
     * @return
     */
    private Object getFieldValue(Method method, Object[] args){
        //获取变量名
        String fieldName = method.getName().substring(3);
        if (!TextUtils.isEmpty(fieldName)) {
            //检查名字是否存在
            try {
                //保存对象
                Class type = method.getReturnType();
                switch (type.getSimpleName()) {
                    case "String":
                        return sharedPreferences.getString(fieldName,null);
                    case "Integer":
                    case "int":
                        return sharedPreferences.getInt(fieldName,0);
                    case "Boolean":
                    case "boolean":
                        return sharedPreferences.getBoolean(fieldName,false);
                    case "Long":
                    case "long":
                        return sharedPreferences.getLong(fieldName,0);
                    case "Float":
                    case "float":
                        return sharedPreferences.getFloat(fieldName,0.0f);
                }

                if (type.isAssignableFrom(Set.class)) {//check is set
                    try {
                        type.getMethod("add", String.class);//check is Set<String>
                        Set<String> newSet = (Set<String>) type.newInstance();
                        Set<String> oldSet = sharedPreferences.getStringSet(fieldName, null);
                        if(oldSet!=null){
                            newSet.addAll(oldSet);
                        }
                        return newSet;
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    /**
     *
     * @param method
     * @param args
     */
    private void  setFieldValue(Method method, Object[] args){
        //获取变量名
        String fieldName = method.getName().substring(3);
        if (!TextUtils.isEmpty(fieldName)) {
            //检查名字是否存在
            try {
                //保存对象
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Class type = args[0].getClass();
                switch (type.getSimpleName()) {
                    case "String": {
                        editor.putString(fieldName, (String) args[0]);
                        editor.apply();
                    }
                    break;
                    case "Integer":
                    case "int": {
                        editor.putInt(fieldName, (Integer) args[0]);
                        editor.apply();
                    }
                    break;
                    case "Boolean":
                    case "boolean": {
                        editor.putBoolean(fieldName, (Boolean) args[0]);
                        editor.apply();
                    }
                    break;
                    case "Long":
                    case "long": {
                        editor.putLong(fieldName, (Long) args[0]);
                        editor.apply();
                    }
                    break;
                    case "Float":
                    case "float": {
                        editor.putFloat(fieldName, (Float) args[0]);
                        editor.apply();
                    }
                    break;
                }

                if (type.isAssignableFrom(Set.class)) {//check is set
                    try {
                        type.getMethod("add", String.class);//check is Set<String>
                        editor.putStringSet(fieldName, (Set<String>) args[0]);
                        editor.apply();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();
        if((args == null || args.length == 0)&&methodName.startsWith("get")){
            Object obj = getFieldValue(method,args);
            if(obj!=null){
                return obj;
            }
        }

        if (args != null && args.length == 1 && (methodName.startsWith("set"))) {
            setFieldValue(method,args);
            return null;
        }

       return method.invoke(proxy,args);
    }
}

//    @Override
//        method for cglib out date
//    public Object intercept(Object o, Object[] args, MethodProxy methodProxy) throws Exception {
//        Object object = methodProxy.invokeSuper(o, args);
//        String methodName = methodProxy.getMethodName();
//        if (args != null && args.length == 1 && methodName.startsWith("set")) {
//            //获取变量名
//            String fieldName = methodName.substring(3);
//            if (!TextUtils.isEmpty(fieldName)) {
//                //检查名字是否存在
//                try {
//                    Field field = mClass.getDeclaredField(StringUtils.lowerFristChar(fieldName));
////                   Field field  = mClass.getField(fieldName);
//                    //保存对象
//                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(mClass.getName(), Application.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    Class type = field.getType();
//                    String name = field.getName();
//                    switch (type.getSimpleName()) {
//                        case "StringSet": {
//                            editor.putStringSet(name, (StringSet) args[0]);
//                            editor.apply();
//                        }
//                        break;
//                        case "String": {
//                            editor.putString(name, (String) args[0]);
//                            editor.apply();
//                        }
//                        break;
//                        case "Integer":
//                        case "int": {
//                            editor.putInt(name, (Integer) args[0]);
//                            editor.apply();
//                        }
//                        break;
//                        case "Boolean":
//                        case "boolean": {
//                            editor.putBoolean(name, (Boolean) args[0]);
//                            editor.apply();
//                        }
//                        break;
//                        case "Long":
//                        case "long": {
//                            editor.putLong(name, (Long) args[0]);
//                            editor.apply();
//                        }
//                        break;
//                        case "Float":
//                        case "float": {
//                            editor.putFloat(name, (Float) args[0]);
//                            editor.apply();
//                        }
//                        break;
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//
//        return object;
//    }
