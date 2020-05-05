package com.hao.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao.miaosha.exception.EmError;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.mapper.UserMapper;
import com.hao.miaosha.mapper.UserPasswordMapper;
import com.hao.miaosha.po.UserPO;
import com.hao.miaosha.po.UserPasswordPO;
import com.hao.miaosha.service.UserService;
import com.hao.miaosha.bo.UserBO;
import com.hao.miaosha.units.ConvertBean;
import com.hao.miaosha.units.MD5Unit;
import com.hao.miaosha.validator.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserBO getByUserId(int userId) throws MyException {
        UserPO userDO = userMapper.selectOne(new QueryWrapper<UserPO>().eq("id", userId));
        if(StringUtils.isEmpty(userDO)){
            throw new MyException(EmError.USER_NOT_EXIST);
        }
        UserBO userBO = new UserBO();
        UserPasswordPO userPasswordPO = userPasswordMapper.selectOne(new QueryWrapper<UserPasswordPO>().eq("user_id", userId));
        userBO = (UserBO) ConvertBean.convertFromDataObject(userDO, userBO);
        userBO.setEncrptPassword(userPasswordPO.getEncrptPassword());
        return userBO;
    }

    /**
     * 如果参数都符合要求就会将用户信息存进user_info表，用户密码存进user_password表
     * @param userBO
     * @throws MyException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UserBO userBO) throws MyException {
        if(StringUtils.isEmpty(userBO)){
            throw new MyException(EmError.PARAMETER_VALIDATION_ERROR);
        }

        // 校验参数是否符合要求，不符合的话会抛异常给前端
        validator.validator(userBO);

        // 判断手机号码是否已重复注册
        UserPO userPO = userMapper.selectOne(new QueryWrapper<UserPO>().eq("telphone", userBO.getTelphone()));
        if(!StringUtils.isEmpty(userPO)){
            throw new MyException(EmError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }

        // 存进user_info表
        UserPO userDO = UserPO.builder().
                age(userBO.getAge()).
                gender(userBO.getGender()).
                name(userBO.getName()).
                telphone(userBO.getTelphone()).
                registerMode(userBO.getRegisterMode()).
                thirdPartyId(userBO.getThirdPartyId())
                .addTime(new Date())
                .updateTime(new Date())
                .build();
        userMapper.insert(userDO);

        // 存进user_password表
        UserPasswordPO userPasswordPO = UserPasswordPO.builder().
                userId(userDO.getId()).
                encrptPassword(userBO.getEncrptPassword()).build();
        userPasswordMapper.insert(userPasswordPO);
    }


    @Override
    public UserBO login(String telphone,String password) throws MyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new MyException(EmError.PARAMETER_VALIDATION_ERROR);
        }
        UserPO userPO = userMapper.selectOne(new QueryWrapper<UserPO>().eq("telphone",telphone));
        if(StringUtils.isEmpty(userPO)){
            throw new MyException(EmError.USER_NOT_EXIST);
        }
        UserPasswordPO userPasswordPO = userPasswordMapper.selectOne(new QueryWrapper<UserPasswordPO>().eq("user_id", userPO.getId()));
        if(!MD5Unit.EncodeByMd5(password).equals(userPasswordPO.getEncrptPassword())){
            throw new MyException(EmError.USER_LOGIN_FAIL);
        }else{
            UserBO userBO = UserBO.builder()
                    .id(userPO.getId())
                    .age(userPO.getAge())
                    .gender(userPO.getGender())
                    .telphone(userPO.getTelphone())
                    .registerMode(userPO.getRegisterMode())
                    .thirdPartyId(userPO.getThirdPartyId())
                    .name(userPO.getName())
                    .encrptPassword(userPasswordPO.getEncrptPassword())
                    .build();
            return userBO;
        }
    }

    /**
     * 通过Redis缓存获取用户信息
     * @param userId
     * @return
     */
    @Override
    public UserBO getUserByIdInCache(Integer userId) throws MyException {
        String key = "user_validate_" + userId;
        UserBO userBO = (UserBO) redisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(userBO)){
            userBO = this.getByUserId(userId);
            redisTemplate.opsForValue().set(key,userBO);
            // 设置过期时间为1小时
            redisTemplate.expire(key,1,TimeUnit.HOURS);
        }
        return userBO;
    }

}
