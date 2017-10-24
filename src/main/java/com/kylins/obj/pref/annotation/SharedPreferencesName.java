package com.kylins.obj.pref.annotation;

import android.content.SharedPreferences;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhouliangshun on 2017/10/24.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited()
public @interface SharedPreferencesName {

    String value();
}
