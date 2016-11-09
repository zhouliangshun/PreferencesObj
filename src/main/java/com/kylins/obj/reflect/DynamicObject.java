package com.kylins.obj.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Dynamic objects, using the reflection method invocation object
 * Created by zhouliangshun on 2016/6/15.
 */
public class DynamicObject {

    private String className;
    private Object instance;
    private Class instanceClass;

    /**
     * To construct a reflection of objects
     * @param className class name
     * @param args args
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
     * Construct a DynamicObject from a instance object
     * @param object instance
     */
    public DynamicObject(Object object){
        if(object!=null){
            instanceClass  = object.getClass();
            className = instanceClass.getName();
            instance = object;
        }
    }

    /**
     * Assert the class is exits
     * @param className class of you will assert
     * @return true is exits false is not
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

    /**
     * Create a instance from a class, The class must have a zero args construct Method
     * @param className class of the new instance
     * @param <T> the object class type
     * @return new instance
     */
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
     * call some method
     * @param name method name
     * @param args args
     * @param <T> return
     * @return return the method return
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
     * Get public file the field packaging a DynamicObject
     * @param name field name
     * @return file of DynamicObject
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
     * et public file the field,raw filed type
     * @param name field name
     * @param <T> the raw type
     * @return field name
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
