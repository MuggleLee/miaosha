package com.hao.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.miaosha.exception.EmError;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.mapper.UserMapper;
import com.hao.miaosha.mapper.UserPasswordMapper;
import com.hao.miaosha.model.UserDO;
import com.hao.miaosha.model.UserPasswordDO;
import com.hao.miaosha.service.UserService;
import com.hao.miaosha.service.model.UserModel;
import com.hao.miaosha.units.ConvertBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPasswordMapper userPasswordMapper;

    @Override
    public UserModel getByUserId(int userId) throws MyException {
        UserDO userDO = userMapper.selectOne(new QueryWrapper<UserDO>().eq("id", userId));
        if(StringUtils.isEmpty(userDO)){
            throw new MyException(EmError.USER_NOT_EXIST);
        }
        UserModel userModel = new UserModel();
        UserPasswordDO userPasswordDO = userPasswordMapper.selectOne(new QueryWrapper<UserPasswordDO>().eq("user_id", userId));
        userModel = (UserModel) ConvertBean.convertFromDataObject(userDO,userModel);
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }

}
