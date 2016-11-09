package com.kylins.obj.copy;

import android.text.TextUtils;

import com.kylins.obj.copy.annotation.DeepCopy;
import com.kylins.obj.copy.annotation.Ignore;
import com.kylins.obj.copy.annotation.Name;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Object Copy
 * Created by zhouliangshun on 2016/8/4.
 */
public class ObjectCopy {

    /**
     * ObjectCopy,Only copy values
     *
     * @param targetClass target class
     * @param source source the values from the instance
     * @param <T> convert to the type
     * @return Target class instance
     */
    public static <T> T copy(Class<T> targetClass, Object source) {
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
        if (target == null || source == null)
            return null;
        try {
            Class<T> targetClass = (Class<T>) target.getClass();
            Field[] fields = targetClass.getDeclaredFields();
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
                    setFieldValue(field, target, getFieldValue(sourceName, source));
                } else {
                    if (!field.getType().isArray()) {
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            setWithCollection(field, target, getFieldValue(sourceName, source));
                        } else {
                            setFieldValue(field, target, copy(field.getType(), getFieldValue(sourceName, source)));
                        }
                    } else {
                        setWithArray(field, target, getFieldValue(sourceName, source));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return target;
    }


    public static Object getFieldValue(String field, Object object) {
        try {
            Field sourceField = object.getClass().getDeclaredField(field);
            return getFieldValue(sourceField, object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
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

    public static void setFieldValue(Field field, Object object, Object value) {
        try {
            if (value != null) {
                field.setAccessible(true);
                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Field getField(String field, Class cls) {
        if (cls != null) {
            try {
                return cls.getDeclaredField(field);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                getField(field, cls.getSuperclass());
            }
        }
        return null;
    }

    public static <T> T newInstance(Class<T> cls) {
        Constructor constructors = null;
        try {
            constructors = cls.getConstructor();
            return (T) constructors.newInstance();
        } catch (NoSuchMethodException e) {
            //  e.printStackTrace();
            try {
                return UnsafeAllocator.create().newInstance(cls);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
