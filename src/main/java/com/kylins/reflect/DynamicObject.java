package com.kylins.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**动态对象,利用反射调用对象的方法
 * Created by j-zhouliangshun on 2016/6/15.
 */
public class DynamicObject {

    private String className;
    private Object instance;
    private Class instanceClass;

    /**
     * 构造一个反射的对象
     * @param className 对象名字
     * @param args 参数，参数个数必须小于10
     */
    public DynamicObject(String className, Object ...args){

        this.className = className;
        Class[] argsClass = new Class[args.length];
        for(int i = 0;i<args.length;i++){
            argsClass[i] = args[i].getClass();
        }
        try {
            instanceClass  = Class.forName(className);
            Constructor constructor =  instanceClass.getConstructor(argsClass);
            instance = constructor.newInstance(args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从对象创建一个动态调用对象
     * @param object
     */
    public DynamicObject(Object object){
        if(object!=null){
            instanceClass  = object.getClass();
            className = instanceClass.getName();
            instance = object;
        }
    }

    /**
     * 判断是否有某个
     * @param className
     * @return
     */
    public static boolean hasClass(String className){
        Class cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
           // e.printStackTrace();
        }
        return cls!=null;
    }

    public static <T>  T newInstance(String className){
        try {
            return (T)Class.forName(className).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射调用方法
     * @param name 方法名字
     * @param args 参数，参数个数小于10
     * @param <T> 返回的类型
     * @return
     */
    public<T> T call(String name,Object ...args){

        if(instanceClass==null||instance==null)
            return null;

        Class[] argsClass = new Class[args.length];
        for(int i = 0;i<args.length;i++){
            argsClass[i] = args[i].getClass();
        }
        try {
            Method[] allMethods = instanceClass.getDeclaredMethods();
            for (Method m : allMethods) {
                String mname = m.getName();
                if (!mname.equals(name)) {
                    continue;
                }
                Class[] methodParaClasses = m.getParameterTypes();
                if(methodParaClasses.length!=argsClass.length)
                    continue;
                for (int i = 0; i < methodParaClasses.length; i++) {
                    Class<?> parameterClass = argsClass[i];
                    Class<?> methodParaClass = methodParaClasses[i];
                    boolean isAssignable = methodParaClass.isAssignableFrom(parameterClass);
                    if (!isAssignable) {
                        continue;
                    }
                }
                Object returnObj = m.invoke(instance, args);
                if(returnObj!=null){
                    return (T)returnObj;
                }
                return null;
            }
            String str = name+" [";
            for(int i = 0;i<argsClass.length;i++){
                Class arg  = argsClass[i];
                str+=arg.toString();
                if(i!=argsClass.length-1){
                    str+=",";
                }
            }
            str+="]";
            throw new NoSuchMethodException(str);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个公共属性，已动态对象返回
     * @param name 属性名称
     * @return
     */
    public DynamicObject field(String name){

        if(instanceClass==null||instance==null)
            return null;

        try {
            Field field = instanceClass.getField(name);
           return new DynamicObject(field.get(instance));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个公共属性，已实际对象返回
     * @param name 属性名称
     * @return
     */
    public<T> T getFieldValue(String name){
        if(instanceClass==null||instance==null)
            return null;
        try {
            Field field = instanceClass.getField(name);
            return (T)field.get(instance);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public String getClassName() {
        return className;
    }

    public<T> T getInstance() {
        return (T)instance;
    }
}
