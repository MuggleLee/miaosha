package com.hao.miaosha.service;

import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.bo.UserBO;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
public interface UserService {
    UserBO getByUserId(int userId) throws MyException;

    void register(UserBO userBO) throws MyException;

    UserBO login(String telphone,String password) throws MyException, UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 通过Redis缓存获取用户信息
     * @param userId
     * @return
     */
    UserBO getUserByIdInCache(Integer userId) throws MyException;
}
