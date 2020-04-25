package com.hao.miaosha.controller;

import com.hao.miaosha.controller.viewObject.UserVo;
import com.hao.miaosha.exception.EmError;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.response.CommonResonse;
import com.hao.miaosha.service.impl.UserServiceImpl;
import com.hao.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
@RestController("User")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResonse getByUser(@RequestParam(name = "userId") int userId) throws MyException {
        UserModel userModel = userService.getByUserId(userId);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel, userVo);
        return CommonResonse.create(userVo);
    }
}
