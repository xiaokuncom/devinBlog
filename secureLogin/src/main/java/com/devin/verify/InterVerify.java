package com.devin.verify;

import com.devin.constants.VerifyType;
import com.devin.entity.User;

/**
 * @author Java Devin
 * @createTime 2023/10/22 17:09
 * @desc
 */
public interface InterVerify {

    /**
     * 鉴权
     * @param user
     * @return
     * @throws Exception
     */
    boolean verify(User user) throws Exception;

    /**
     * 是否为该类型
     * @param type
     * @return
     */
    boolean isVerifyType(VerifyType type);
}
