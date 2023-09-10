package com.devin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Java Devin
 * @date 2023/9/10 20:11
 * @desc 用户表
 */
@Data
public class User {

    /**
     * id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 登录名
     */
    private String loginName;


    /**
     * 密码
     */
    private String pwd;

    /**
     * 姓名
     */
    private String name;


    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String gender;

}
