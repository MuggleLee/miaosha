package com.hao.miaosha.exception;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
public interface CommonError {
    int getErrCode();

    String getErrMsg();

    void setErrMsg(String errMsg);
}
