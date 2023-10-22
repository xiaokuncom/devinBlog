package com.devin.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author liangkun
 * @createTime 2023/10/22 17:04
 * @desc  鉴权类型
 */
@AllArgsConstructor
public enum VerifyType {
    DB(0, "数据库"),
    LDAP(1, "AD域");

    private final int code;
    private final String desc;

    @JsonValue
    public int code() {
        return code;
    }

    public VerifyType value(int value) {
        return valueOf(value);
    }

    @JsonCreator
    public static VerifyType valueOf(int value) {
        if (DB.code() == value) {
            return DB;
        } else if (LDAP.code() == value) {
            return LDAP;
        }
        return null;
    }

}
