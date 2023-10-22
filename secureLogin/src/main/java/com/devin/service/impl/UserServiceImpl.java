package com.devin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devin.config.LoginConfig;
import com.devin.entity.User;
import com.devin.mapper.UserMapper;
import com.devin.service.UserService;
import com.devin.util.RsaUtil;
import com.devin.verify.VerifySupport;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Java Devin
 * @date 2023/9/10 20:44
 * @desc
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private LoginConfig loginConfig;

    @Resource
    private VerifySupport verifySupport;

    /**
     * 添加用户
     * @param user
     */
    @Override
    public void add(User user) throws Exception {
        String loginName = user.getLoginName();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name", loginName);
        List<User> users = userMapper.selectList(wrapper);
        if (users != null && users.size() > 0) {
            throw new RuntimeException("登录名已存在！！！");
        }

        String pwd = user.getPwd();
        if (StringUtils.isBlank(pwd)) {
            throw new RuntimeException("密码不能为空！！！");
        }

        // 将密码加密
        user.setPwd(RsaUtil.RSAEncode(loginConfig.getPublicKey(), pwd));
        this.save(user);
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public String login(User user) throws Exception {
        return verifySupport.verify(user, Integer.parseInt(loginConfig.getVerifyType())) ? "登录成功" : "用户名或密码错误！！";
    }

}
