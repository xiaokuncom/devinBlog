package com.devin.verify;

import com.devin.constants.VerifyType;
import com.devin.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liangkun
 * @createTime 2023/10/22 17:02
 * @desc  鉴权支持类
 */
@Component
@Slf4j
public class VerifySupport {

    @Resource
    private List<AbstractVerify> verifys;

    /**
     * 鉴权
     * @param user  用户信息
     * @param verifyType  验证方式
     * @return
     */
    public boolean verify(User user, int verifyType) throws Exception {
        Set<AbstractVerify> verifies = verifys.stream().filter(verify -> verify.isVerifyType(VerifyType.valueOf(verifyType))).collect(Collectors.toSet());
        boolean result = false;

        if (CollectionUtils.isEmpty(verifies)) {
            log.error("未配置或未实现鉴权类型, type:{}", verifyType);
            throw new Exception("未配置或未实现鉴权类型！！");
        }

        for (AbstractVerify verify : verifies) {
            result =  verify.verify(user);
        }
        return result;
    }

}
