package com.hao.miaosha.exception;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
public class MyException extends Exception implements CommonError{

    private CommonError commonError;

    public MyException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    public MyException(CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        this.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public void setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
    }
}
