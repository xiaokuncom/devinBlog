package com.devin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author liangkun
 * @createTime 2023/10/22 17:27
 * @desc  登录功能的配置类
 */
@Data
@Configuration
public class LoginConfig {

    /**
     * 公钥
     */
    @Value("${devin.blog.publicKey}")
    public String publicKey;
    /**
     * 私钥
     */
    @Value("${devin.blog.privateKey}")
    public String privateKey;
    /**
     * 登录方式
     */
    @Value("${devin.blog.verifyType}")
    public String verifyType;

}
