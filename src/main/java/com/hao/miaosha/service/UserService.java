package com.hao.miaosha.service;

import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.model.UserDO;
import com.hao.miaosha.service.model.UserModel;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
public interface UserService {
    UserModel getByUserId(int userId) throws MyException;
}
