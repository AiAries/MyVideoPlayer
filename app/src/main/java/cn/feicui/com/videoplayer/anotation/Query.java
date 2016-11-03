package cn.feicui.com.videoplayer.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2016/11/2 0002.
 */
@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Query {
    String value() default "";
}
