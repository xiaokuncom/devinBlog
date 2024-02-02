package com.devin.syslogdemo.controller;

import com.devin.syslogdemo.utils.SyslogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Java Devin
 * @createTime 2024/1/21 17:11
 * @desc  syslog 接口层
 */
@RestController
@RequestMapping("/syslog")
@Slf4j
public class SyslogDemoController {


    /**
     * 发送业务消息
     * @param message
     */
    @GetMapping("/sendMessage")
    public void sendMessage(String message) {
        SyslogUtil.sendSyslog(message);
    }

    /**
     * 发送log日志
     */
    @GetMapping("/sendLog")
    public void sendLog() throws InterruptedException {
        log.info("测试log-info");
        TimeUnit.SECONDS.sleep(1);
        log.error("测试log-1");
        TimeUnit.SECONDS.sleep(1);
        log.error("测试log-2");
        TimeUnit.SECONDS.sleep(1);
        log.error("测试log-3");
        TimeUnit.SECONDS.sleep(1);
        log.error("测试log-4");
        TimeUnit.SECONDS.sleep(1);
        log.error("测试log-bye");
    }

    /**
     * 设置syslog
     * @param host
     * @param port
     * @param protocol
     * @throws Exception
     */
    @GetMapping("/setSyslog")
    public void setSyslog(@RequestParam("host") String host, @RequestParam("port") int port, @RequestParam("protocol") int protocol) throws Exception {
        SyslogUtil.setSyslog(host, port, protocol, null);
    }
}
