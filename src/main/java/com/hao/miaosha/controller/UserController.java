package com.hao.miaosha.controller;

import com.hao.miaosha.units.MD5Unit;
import com.hao.miaosha.vo.UserVo;
import com.hao.miaosha.exception.EmError;
import com.hao.miaosha.exception.MyException;
import com.hao.miaosha.response.CommonResonse;
import com.hao.miaosha.service.UserService;
import com.hao.miaosha.bo.UserBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author MuggleLee
 * @date 2020/4/25
 */
@RestController("User")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return
     * @throws MyException
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public CommonResonse getByUser(@RequestParam(name = "userId") int userId) throws MyException {
        UserBO userBO = userService.getByUserId(userId);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userBO, userVo);
        return CommonResonse.create(userVo);
    }

    /**
     * 获取验证码
     * @param telphone 手机号码
     * @return
     */
    @RequestMapping(value = "/getOtp",method = RequestMethod.POST)
    @ResponseBody
    public CommonResonse getOtp(@RequestParam("telphone")String telphone){
        // 生成验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String code = String.valueOf(randomInt);
        // 与用户手机号码关联
        request.getSession().setAttribute(telphone,code);

        //发送短信(之后再做)
        System.out.println("手机号码：" + telphone + " 验证码：" + code);
        return CommonResonse.create(null);
    }

    /**
     * 用户注册
     * @param name 用户名
     * @param gender 用户性别
     * @param age 用户年龄
     * @param telphone 用户手机
     * @param password 密码
     * @param otpCode 验证码
     * @return
     * @throws MyException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST,consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonResonse register(@RequestParam("name") String name,
                                  @RequestParam("gender") byte gender,
                                  @RequestParam("age") int age,
                                  @RequestParam("telphone") String telphone,
                                  @RequestParam("password") String password,
                                  @RequestParam("otpCode") String otpCode) throws MyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println(request.getSession().getAttribute(telphone));
        if(!request.getSession().getAttribute(telphone).equals(otpCode)){
            throw new MyException(EmError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        // 组装业务逻辑的User类
        UserBO userBO = UserBO.builder()
                .name(name)
                .age(age)
                .gender(gender)
                .encrptPassword(MD5Unit.EncodeByMd5(password))
                .registerMode("byPhone")
                .telphone(telphone).build();
        userService.register(userBO);
        // 注册成功
        return CommonResonse.create(null);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST,consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonResonse login(@RequestParam("telphone") String telphone,
                                  @RequestParam("password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, MyException {
        UserBO userBO = userService.login(telphone, password);
        // 将token存进redis
        String uuidToken = UUID.randomUUID().toString();
        uuidToken = uuidToken.replace("-", "");
        redisTemplate.opsForValue().set(uuidToken,userBO);
        redisTemplate.expire(uuidToken,1, TimeUnit.HOURS);
        // 登录成功
        return CommonResonse.create(uuidToken);
    }
}
