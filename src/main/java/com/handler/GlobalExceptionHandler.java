package com.handler;

import com.exception.BaseException;
import com.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

// 全局异常处理
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
        public Result exceptionHandler(BaseException baseException)
    {

        return Result.error(baseException.getMessage());
    }
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException exception)
            {
        if (exception.getMessage().contains("Duplicate entry"))
        {
            String[] split = exception.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return Result.error(msg);
        }
        return Result.error("未知错误");
    }
}
