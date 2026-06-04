package com.gfg.library69.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//CLASS, SOURCE(discarded by complier)
@Target(ElementType.METHOD)
public @interface MethodInfo {

    String author();

    String description();
}
