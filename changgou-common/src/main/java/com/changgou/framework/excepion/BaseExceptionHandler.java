package com.changgou.framework.excepion;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/19
 */
@ControllerAdvice //此注解表示,全局抓捕异常,只要作用在@ResquestMapping上,所有的异常都会被捕获
public class BaseExceptionHandler extends Exception {
    /**
     * @Description: 处理异常
     * @Param:
     * @return:
     * @Author: Mr.Wang
     * @Date: 2020/2/19
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e, HttpServletResponse response) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
