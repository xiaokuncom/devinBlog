package com.devin.controller;

import com.devin.entity.User;
import com.devin.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Java Devin
 * @date 2023/9/10 20:38
 * @desc 登录接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/add")
    public void add(@Validated @RequestBody User user) {
        userService.add(user);
    }

    @GetMapping("/login")
    public String login( User user) {
        return userService.login(user);
    }

}
