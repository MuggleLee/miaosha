package com.hao.miaosha.validator;

import com.hao.miaosha.exception.EmError;
import com.hao.miaosha.exception.MyException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * @author Muggle Lee
 * @Date: 2020/4/26 13:57
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //实现校验方法并返回校验结果
    public ValidatorResult validator(Object bean) throws MyException {
        ValidatorResult validatorResult = new ValidatorResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        // 大于0证明有错误
        if (constraintViolationSet.size() > 0) {
            validatorResult.setHasError(true);
            constraintViolationSet.forEach(constraintViolation->{
                String errorMessage = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                validatorResult.getErrorMap().put(propertyName,errorMessage);
            });
        }
        // 如果参数有异常，返回错误信息
        if(validatorResult.isHasError()){
            throw new MyException(EmError.PARAMETER_VALIDATION_ERROR,validatorResult.getErrorMsg());
        }
        return validatorResult;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
