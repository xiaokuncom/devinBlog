package com.devin.syslogdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author liangkun
 * @createTime 2023/11/16 11:30
 * @desc syslog接收数据测试
 */
@Slf4j
public class SyslogTest {

    @Test
    public void tcp() throws IOException {
        try (ServerSocket ss = new ServerSocket(10086)) {
            close:while (true) {
                Socket s = ss.accept();
                InputStream is = s.getInputStream();
                while (true) {
                    byte[] by = new byte[1024];
                    int len = is.read(by);
                    if (len < 0) {
                        break;
                    }
                    if (len > 0) {
                        String data = new String(by, 0, len);
                        System.out.print("tcp2服务端:" + data);
                        if (data.contains("bye")) {
                            break close;
                        }
                    }
                }
            }
        }
    }

    @Test
    public void tcp1() throws IOException {
        try (ServerSocket ss = new ServerSocket(10087)) {
            close:while (true) {
                Socket s = ss.accept();
                InputStream is = s.getInputStream();
                while (true) {
                    byte[] by = new byte[1024];
                    int len = is.read(by);
                    if (len < 0) {
                        break;
                    }
                    if (len > 0) {
                        String data = new String(by, 0, len);
                        System.out.println("tcp1服务端:" + data);
                        if (data.contains("bye")) {
                            break close;
                        }
                    }
                }
            }
        }
    }

    @Test
    public void udp1() throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(10085);
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[512], 512);
            try {
                datagramSocket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.print("udp1服务端:" + msg);
                if (msg.contains("bye")) {
                    break;
                }
                log.info("{}/{}:{}", packet.getAddress(), packet.getPort(), msg);
            } catch (Exception e) {
                log.error("listener error", e);
            }
        }
        datagramSocket.close();
    }

    @Test
    public void udp2() throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(10084);
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[512], 512);
            try {
                datagramSocket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println("udp2服务端:" + msg);
                if (msg.contains("bye")) {
                    break;
                }
                log.info("{}/{}:{}", packet.getAddress(), packet.getPort(), msg);
            } catch (Exception e) {
                log.error("listener error", e);
            }
        }
        datagramSocket.close();
    }


}
