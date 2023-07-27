package com.foogui.common.utils;


import com.foogui.common.enums.ErrorCode;
import com.foogui.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;


/**
 * Valid手动校验工具
 *
 * @author Foogui
 * @date 2023/07/27
 */
@Component
@Slf4j
public class ValidationUtils {

    private final static Validator VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static void valid(Object object, Class<?>... groups) {
        
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object, groups);

        if (constraintViolations!=null && !constraintViolations.isEmpty()) {
            Optional<String> errorMessage = constraintViolations.stream().map(ConstraintViolation::getMessage).findFirst();
            if (errorMessage.isPresent()) {
                log.error("ValidationUtil.exception: {}", errorMessage.get());
                throw new BizException(ErrorCode.PARAM_ERROR.getCode(), errorMessage.get());
            }
        }
    }

}
