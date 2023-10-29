package com.devin.verify.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.devin.config.LoginConfig;
import com.devin.constants.VerifyType;
import com.devin.entity.User;
import com.devin.mapper.UserMapper;
import com.devin.util.RsaUtil;
import com.devin.verify.AbstractVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Java Devin
 * @createTime 2023/10/22 17:10
 * @desc  数据库鉴权
 */
@Component
@Slf4j
public class DbVerify extends AbstractVerify {

    @Resource
    private UserMapper userMapper;

    @Resource
    private LoginConfig loginConfig;

    @Override
    public boolean verify(User user) throws Exception {
        String loginName = user.getLoginName();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getLoginName, loginName);
        List<User> userList = userMapper.selectList(wrapper);
        if (userList == null || userList.size() == 0) {
            log.error("用户名或密码错误");
            throw new Exception("用户名或密码错误！！");
        }
        // 能取出来就是唯一的用户 存的时候已经校验过了
        User userDb = userList.get(0);

        String loginUserPwd = user.getPwd();
        String userDbPwd = userDb.getPwd();

        // 解密
        String decodeLoginUserPwd = RsaUtil.RSADecode(loginConfig.getPrivateKey(), loginUserPwd);
        String decodeUserDbPwd = RsaUtil.RSADecode(loginConfig.getPrivateKey(), userDbPwd);
        if (!decodeUserDbPwd.equals(decodeLoginUserPwd)) {
            log.error("用户名或密码错误");
            throw new Exception("用户名或密码错误！！");
        }
        return true;
    }

    @Override
    public boolean isVerifyType(VerifyType type) {
        return VerifyType.DB.equals(type);
    }
}
