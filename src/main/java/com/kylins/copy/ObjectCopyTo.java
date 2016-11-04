package com.kylins.copy;

import android.text.TextUtils;

import com.kylins.copy.annotation.DeepCopy;
import com.kylins.copy.annotation.Ignore;
import com.kylins.copy.annotation.Name;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

import static com.kylins.copy.ObjectCopy.getField;
import static com.kylins.copy.ObjectCopy.newInstance;

/**
 * Created by zhouliangshun on 2016/8/4.
 */
public class ObjectCopyTo {

    public static <T> T copy(Class<T> targetClass, Object source) {

        if (targetClass == null || source == null)
            return null;
        return copy(newInstance(targetClass), source);
    }

    public static <T> T copy(Class<T> targetClass, Object source, Object... args) {

        if (targetClass == null || source == null)
            return null;
        try {
            Class[] argCls = new Class[args.length];
            for (int i = 0; i < argCls.length; i++) {
                argCls[i] = args[i].getClass();
            }
            return (T) copy(targetClass.getDeclaredConstructor(argCls).newInstance(), source);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T copy(T target, Object source) {
        if (target == null || source == null) {
            return null;
        }
        try {
            Class<T> targetClass = (Class<T>) target.getClass();
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                String sourceName = name;
                if (field.getAnnotation(Ignore.class) != null)
                    continue;
                Name changeName = field.getAnnotation(Name.class);
                if (changeName != null) {
                    if (!TextUtils.isEmpty(changeName.name())) {
                        sourceName = changeName.name();
                    }
                }

                DeepCopy deepCopy = field.getAnnotation(DeepCopy.class);
                if (deepCopy == null) {
                    setFieldValue(sourceName, target, getFieldValue(field, source));
                } else {
                    if (!field.getType().isArray()) {
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            Field newField = getField(sourceName, targetClass);
                            setWithCollection(newField, target, getFieldValue(field, source));
                        } else {
                            Field newField = getField(sourceName, targetClass);
                            setFieldValue(sourceName, target, copy(newField.getType(), getFieldValue(field, source)));
                        }
                    } else {
                        Field newField = getField(sourceName, targetClass);
                        setWithArray(newField, target, getFieldValue(field, source));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return target;
    }

    public static void setWithCollection(Field field, Object object, Object value) {
        try {
            if (value != null) {
                Collection collection = (Collection) value;
                ParameterizedType integerListType = (ParameterizedType) field.getGenericType();
                Class<?> type = (Class<?>) integerListType.getActualTypeArguments()[0];
                Collection newCollection = newInstance(ArrayList.class);
                for (Object t : collection) {
                    newCollection.add(copy(type, t));
                }
                field.setAccessible(true);
                field.set(object, newCollection);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setWithArray(Field field, Object object, Object value) {
        try {
            if (value != null) {
                int leng = Array.getLength(value);
                Object newArray = Array.newInstance(field.getType().getComponentType(), leng);
                for (int i = 0; i < leng; i++) {
                    Object t = Array.get(value, i);
                    Array.set(newArray, i, copy(field.getType().getComponentType(), t));
                }
                field.setAccessible(true);
                field.set(object, newArray);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setFieldValue(String field, Object object, Object value) {
        try {
            if (value != null) {
                Field objField = getField(field, object.getClass());
                objField.setAccessible(true);
                objField.set(object, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getFieldValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
