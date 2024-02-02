package com.devin.syslogdemo.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.net.SyslogAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.devin.syslogdemo.appender.SysLogAppenderTCP;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.impl.StaticLoggerBinder;

import java.nio.charset.StandardCharsets;

/**
 * @author JavaDevin
 * @createTime 2024/1/21 16:41
 * @desc  syslog工具类
 */
@Slf4j
public class SyslogUtil {

    /**
     * syslog相关配置常量
     */
    public static final String DEFAULT_SYSLOG_NAME = "SYSLOG";
    public static final String DEFAULT_SYSLOG_FACILITY = "USER";

    /**
     * syslog发送协议：UDP
     */
    public static final int SYSLOG_SEND_PROTOCOL_UDP = 0;

    /**
     * syslog发送协议：TCP
     */
    public static final int SYSLOG_SEND_PROTOCOL_TCP = 1;

    /**
     * 发送syslog统一入口
     *
     * @param message
     */
    public static void sendSyslog(String message) {
        log.info(message);
    }

    /**
     * 设置syslog
     * @param host
     * @param port
     * @param protocol
     * @param pattern
     * @throws Exception
     */
    public static void setSyslog(String host, int port, int protocol, String pattern) throws Exception {
        // 获取一个LoggerContext对象
        LoggerContext lc = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
        // 使用获取到的LoggerContext获取名为 SyslogUtil 的 Logger 对象
        Logger logger = lc.getLogger(SyslogUtil.class);

        // 关闭之前的Syslog日志处理器
        closeSyslog(logger);
        // 获取一个新的Syslog日志处理器
        SyslogAppender syslogAppender = SyslogUtil.getAppender(host, port, protocol);

        // 配置参数
        //  以下配置除了pattern以外，其余配置一个都不能少，少一个就报错，就发不出去，一删一个不吱声！！！
        //  大牛请无视这句话！！！
        if (StringUtils.isNotBlank(pattern)) {
            syslogAppender.setSuffixPattern(pattern);
        }
        //  设置日志记录器的上下文
        syslogAppender.setContext(lc);
        syslogAppender.setCharset(StandardCharsets.UTF_8);
        //  设置日志 facility
        syslogAppender.setFacility(DEFAULT_SYSLOG_FACILITY);
        //  日志名称
        syslogAppender.setName(DEFAULT_SYSLOG_NAME);
        //  添加一个日志级别过滤器，用于控制日志的输出级别
        syslogAppender.addFilter(new ThresholdFilter());

        // 启动Syslog日志处理器
        syslogAppender.start();
        // 将配置好的Syslog日志处理器添加到日志记录器中
        logger.addAppender(syslogAppender);
    }

    /**
     * 清除logger上下文中我们自定义的SysLogAppender信息
     * @param logger
     */
    private static void closeSyslog(Logger logger) {
        Appender<ILoggingEvent> appender = logger.getAppender(DEFAULT_SYSLOG_NAME);
        if (appender != null) {
            appender.stop();
        }
        logger.detachAppender(DEFAULT_SYSLOG_NAME);
    }

    /**
     * 获取TCP/UDP协议的Appender
     *
     * @param host
     * @param port
     * @param protocol
     * @return
     * @throws Exception
     */
    private static SyslogAppender getAppender(String host, int port, int protocol) throws Exception {
        SyslogAppender syslogAppender;
        switch (protocol) {
            case SYSLOG_SEND_PROTOCOL_UDP:
                syslogAppender = new SyslogAppender();
                syslogAppender.setSyslogHost(host);
                syslogAppender.setPort(port);
                break;
            case SYSLOG_SEND_PROTOCOL_TCP:
                syslogAppender = new SysLogAppenderTCP(host, port, 1, 2, 10);
                break;
            default:
                throw new Exception("未知的协议类型！！");
        }

        return syslogAppender;
    }


}
