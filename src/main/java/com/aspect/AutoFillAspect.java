package com.aspect;


import com.annotation.AutoFill;
import com.constant.AutoFillConstant;
import com.context.BaseContext;
import com.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

//自定义切面实现公共字段自动填充
@Aspect//该函数是一个切面注解，用于定义切面。切面是AOP（面向切面编程）中的一个概念，它是指关注点的模块化，例如日志、事务管理等。
// 通过@Aspect注解，可以将切面逻辑定义在一个类中，并通过@Before、@After等注解指定切面逻辑执行的时机。
@Component
@Slf4j
public class AutoFillAspect {

    //切入点
    @Pointcut("execution(* com.mapper.*.*(..)) && @annotation(com.annotation.AutoFill)")//匹配所有的类和方法并且加入了AutoFill注解
    //匹配所有的方法所有的参数
    public void autoFillPointCut(){

    }
    //前置通知,在通知中进行公共字段赋值
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的填充...");
        //获取当前被拦截的方法的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取到当前被拦截的方法的签名
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class); //获取到该方法上AutoFill注解的实例对象
         OperationType operationType = autoFill.value();//通过autoFill.value()获取到AutoFill注解中value属性的值，即数据库操作类型。
        Object[] args = joinPoint.getArgs(); //获取到当前被拦截方法的所有参数
        if(args==null||args.length==0)
        {
            return;
        }
        Object entity = args[0];
        //准备赋值的数据
        LocalDateTime now=LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //根据当前不同的操作类型,为相应的属性通过反射来赋值
        if (operationType==operationType.INSERT)
        {
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射来为对象属性赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //为4个公共字段赋值
        }else if(operationType==OperationType.UPDATE)
        {
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

             //2个公共字段
        }

    }
}
