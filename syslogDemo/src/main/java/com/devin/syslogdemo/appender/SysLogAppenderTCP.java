package com.devin.syslogdemo.appender;

import ch.qos.logback.classic.net.SyslogAppender;
import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @author Java Devin
 * @createTime 2023/12/18 13:33
 * @desc  syslog tcp协议 appender
 */
@Slf4j
public class SysLogAppenderTCP extends SyslogAppender {
    
    private static final int INT_ZERO = 0;
    /**
     * host
     */
    private String host;
    /**
     * 端口
     */
    private int port;
    /**
     * socket超时时间
     */
    private int timeout = 10;
    /**
     * socket连接失败后的重试次数
     */
    private int retryNum = 2;
    /**
     * socket连接失败后重试的时间间隔
     */
    private int retryTime = 100;
    private Socket socket;

    SysLogAppenderTCP(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public SysLogAppenderTCP() {
    }

    public SysLogAppenderTCP(String host, int port, int timeout, int retryNum, int retryTime, Socket socket) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.retryNum = retryNum;
        this.retryTime = retryTime;
        this.socket = socket;
    }

    public SysLogAppenderTCP(String host, int port, int timeout, int retryNum, int retryTime) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.retryNum = retryNum;
        this.retryTime = retryTime;
    }


    @Override
    public SyslogOutputStream createOutputStream() throws SocketException, UnknownHostException {
        //  这里留给代码调用时设置host、port
        if (StringUtils.isEmpty(host) && port == 0) {
            host = getSyslogHost();
            port = getPort();
        }

        return new SyslogOutputStream(host, port) {
            private int count = INT_ZERO;

            @Override
            public void write(byte[] byteArray, int offset, int len) throws IOException {
                try {
                    if (socket == null) {
                        socket = new Socket(host, port);
                        socket.setSoTimeout(timeout * 1000);
                    }
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(byteArray, offset, len);
                    count = INT_ZERO;
                } catch (IOException e) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(retryTime);
                    } catch (Exception exc) {
                        log.error("线程sleep错误", exc);
                        Thread.currentThread().interrupt();
                    }
                    //  每次连接失败后将socket对象置空并且计数 达到预设的阈值时打印报错日志
                    socket = null;
                    count++;
                    if (count >= retryNum) {
                        log.error("syslog连接失败, host:{}, port:{}", host, port);
                        // 这里抛得异常会被logback吞掉，但是没关系，先在控制台打印错误信息一样的，反正得有报错^_^
                        throw new RuntimeException("syslog连接失败！！");
                    }
                    this.write(byteArray, offset, len);
                }
            }
        };
    }

    @Override
    public void stop() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                log.error("停止socket连接失败");
                throw new RuntimeException(e);
            }
        }
        super.stop();
    }
}