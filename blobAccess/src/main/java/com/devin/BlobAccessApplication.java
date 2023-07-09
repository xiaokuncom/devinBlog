package com.devin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Java Devin
 * @createTime 2023/7/7 11:51
 * @desc 大文件存取示例
 */
@SpringBootApplication
@MapperScan({"com.devin.**.mapper"})
public class BlobAccessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlobAccessApplication.class, args);
    }

}
