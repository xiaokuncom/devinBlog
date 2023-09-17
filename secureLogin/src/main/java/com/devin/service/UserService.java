package com.devin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devin.entity.User;

/**
 * @author Java Devin
 * @date 2023/9/10 20:43
 * @desc
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录
     * @param user
     * @return
     */
    String login(User user) throws Exception;

    /**
     * 注册用户
     * @param user
     */
    void add(User user) throws Exception;
}
