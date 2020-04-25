package com.hao.miaosha.exception;

import com.hao.miaosha.response.CommonResonse;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResonse doError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception ex) {
        ex.printStackTrace();
        Map<String,Object> responseData = new HashMap<>();
        if( ex instanceof MyException){
            MyException businessException = (MyException)ex;
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
        }else if(ex instanceof ServletRequestBindingException){
            responseData.put("errCode",EmError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg","url绑定路由问题");
        }else if(ex instanceof NoHandlerFoundException){
            responseData.put("errCode",EmError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg","没有找到对应的访问路径");
        }else{
            responseData.put("errCode", EmError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg",EmError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonResonse.create(responseData,"fail");
    }
}