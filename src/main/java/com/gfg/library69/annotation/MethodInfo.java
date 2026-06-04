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
/* Data type for members
primitive,String, Class reference(Class<?> type();),Enum,
another annotation (NestedAnnotation nested();)
 array of primitives   int[] values();
*/
/* ElementType
The constants
ANNOTATION_TYPE,CONSTRUCTOR,FIELD,LOCAL_VARIABLE,METHOD,PACKAGE,MODULE,PARAMETER,TYPE,
and TYPE_PARAMETER
*/
