package com.devin.entiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Java Devin
 * @createTime 2023/7/7 11:13
 * @desc 员工信息表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "staff")
public class Staff implements Serializable {

    /**
     * 编号
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 资料
     */
    private byte[] data;
}
