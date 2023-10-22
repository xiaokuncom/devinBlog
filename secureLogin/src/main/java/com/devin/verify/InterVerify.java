package com.devin.verify;

import com.devin.constants.VerifyType;
import com.devin.entity.User;

/**
 * @author liangkun
 * @createTime 2023/10/22 17:09
 * @desc
 */
public interface InterVerify {

    boolean verify(User user) throws Exception;

    boolean isVerifyType(VerifyType type);
}
