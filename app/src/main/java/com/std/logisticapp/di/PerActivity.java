package com.std.logisticapp.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Maik on 2016/4/25.
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}
