package com.kylins.obj.copy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by zhouliangshun on 2016/8/4.
 */
@Target(ElementType.FIELD)
public @interface Name {
    String name();
}
