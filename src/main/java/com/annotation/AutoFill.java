package com.annotation;

import com.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义注解 用于标识方法进行公共字段填充处理
@Target(ElementType.METHOD)//该注解只能在方法上使用固定写法
@Retention(RetentionPolicy.RUNTIME)//只能在运行时期
public @interface AutoFill {
    //数据库操作类型:update insert
    OperationType value();

}
