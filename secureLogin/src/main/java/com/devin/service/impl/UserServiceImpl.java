package com.devin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devin.entity.User;
import com.devin.mapper.UserMapper;
import com.devin.service.UserService;
import com.devin.util.RsaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

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

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAK9aqKR8Wx7hpw/Uv2+VL8A6no00+3U42KNgbRQjPmcsB5kKYTjafGGTXyPdIl7qkCQY655S4M4+ENzbXKZcbp0CAwEAAQ==";
    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAr1qopHxbHuGnD9S/b5UvwDqejTT7dTjYo2BtFCM+ZywHmQphONp8YZNfI90iXuqQJBjrnlLgzj4Q3NtcplxunQIDAQABAkEAnjuOpi7ZArrYx75QqN3UYwAChqViq1qKkpK2m09aRjm/lhJvdFHg21sqn6/OQIPIq1GVoJGIRG0XKQ9nRve4+QIhAPkNw36zEl0TQIhMQzz3YysnMl5VeTGHnBLGGlyxkF6zAiEAtD6ulspPeNAPMlMXzABUKWImx4Y8t3derjq92NJepW8CIQC1uMg7S1gL179PdC3fbzbJxujmkgFDFYrY2/liRyFrJwIgMu9jtwkE3EcvhDEnEGcnYWpzOv8cOVxqTSIcdCE+zdECIQCaq2usptt2808lEC6lV81wFIl9kjXain79zROKRDwcUw==";

    @Resource
    UserMapper userMapper;

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
        user.setPwd(RsaUtil.RSADecode(PUBLIC_KEY, pwd));
        this.save(user);
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public String login(User user) throws Exception {
        return verify(user) ? "登录成功" : "密码错误，登录失败！！";
    }

    /**
     * 校验密码
     * @param loginUser
     * @return
     */
    private boolean verify(User loginUser) throws Exception {
        String loginName = loginUser.getLoginName();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getLoginName, loginName);
        List<User> userList = this.list(wrapper);
        if (userList == null || userList.size() == 0) {
            throw new RuntimeException(String.format("用户名不存在, userName:%s", loginName));
        }
        // 能取出来就是唯一的用户 存的时候已经校验过了
        User userDb = userList.get(0);

        String loginUserPwd = loginUser.getPwd();
        String userDbPwd = userDb.getPwd();

        // 解密
        String decodeLoginUserPwd = RsaUtil.RSADecode(PRIVATE_KEY, loginUserPwd);
        String decodeUserDbPwd = RsaUtil.RSADecode(PRIVATE_KEY, userDbPwd);
        return decodeUserDbPwd.equals(decodeLoginUserPwd);
    }
}
