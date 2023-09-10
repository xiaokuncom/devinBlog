package com.devin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devin.entity.User;
import com.devin.mapper.UserMapper;
import com.devin.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import java.util.List;

/**
 * @author Java Devin
 * @date 2023/9/10 20:44
 * @desc
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public void add(User user) {
        String loginName = user.getLoginName();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name", loginName);
        List<User> users = userMapper.selectList(wrapper);
        if (users != null && users.size() > 0) {
            throw new RuntimeException("登录名已存在！！！");
        }

        String pwd = user.getPwd();
        if (StringUtils.isEmpty(pwd)) {
            throw new RuntimeException("密码不能为空！！！");
        }

        this.save(user);
    }

    @Override
    public String login(User user) {
        String loginName = user.getLoginName();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getLoginName, loginName);
        List<User> userList = this.list(wrapper);
        if (userList == null || userList.size() == 0) {
            throw new RuntimeException(String.format("用户名不存在, userName:%s", loginName));
        }

        // 能取出来就是唯一的用户 存的时候已经校验过了
        String userPwd = user.getPwd();
        User userDb = userList.get(0);
        String pwdDb = userDb.getPwd();
        return userPwd.equals(pwdDb) ? "登录成功" : "密码错误，登录失败！！";
    }
}
