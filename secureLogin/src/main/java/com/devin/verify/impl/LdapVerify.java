package com.devin.verify.impl;

import com.devin.config.LoginConfig;
import com.devin.constants.VerifyType;
import com.devin.entity.User;
import com.devin.util.RsaUtil;
import com.devin.verify.AbstractVerify;
import com.google.protobuf.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Java Devin
 * @createTime 2023/10/22 17:10
 * @desc  Ldap鉴权
 */
@Component
@Slf4j
public class LdapVerify extends AbstractVerify {

    @Resource
    private LdapTemplate ldapTemplate;

    @Resource
    private LoginConfig loginConfig;

    /**
     * ldap 字段前缀
     */
    public static final String LDAP_CN = "cn";

    @Override
    public boolean verify(User user) throws Exception {
        // 将入参的密码进行解密
        String decodePwd = RsaUtil.RSADecode(loginConfig.getPrivateKey(), user.getPwd());

        // 查询该用户是否存在
        EqualsFilter equalsFilter = new EqualsFilter(LDAP_CN, user.getLoginName());
        List<String> search = ldapTemplate.search(LdapQueryBuilder.query().filter(equalsFilter), (AttributesMapper<String>) r -> r.get("CN").get().toString());
        if (CollectionUtils.isEmpty(search)) {
            log.error("用户不存在，请检查ldap！！");
            throw new Exception("用户名或密码错误！！");
        }

        // 校验密码是否一致(注意Ldap中是手动设置的明文密码)
        return ldapTemplate.authenticate("", equalsFilter.toString(), decodePwd);
    }

    @Override
    public boolean isVerifyType(VerifyType type) {
        return VerifyType.LDAP.equals(type);
    }
}
