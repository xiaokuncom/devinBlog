package com.devin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devin.entiy.Staff;
import com.devin.mapper.StaffMapper;
import com.devin.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Java Devin
 * @createTime 2023/7/7 11:35
 * @desc
 */
@Service("staffService")
@Slf4j
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {
}
