package com.devin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devin.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Java Devin
 * @date 2023/9/10 20:43
 * @desc
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
