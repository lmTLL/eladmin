package me.zhengjie.aop.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解类
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Target(ElementType.METHOD)//注解的作用目标方法
@Retention(RetentionPolicy.RUNTIME)// 注解会在class字节码文件中存在，在运行时可以通过反射获取到
public @interface Log {
    String value() default "";
}
