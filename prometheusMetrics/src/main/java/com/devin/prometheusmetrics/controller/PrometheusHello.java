package com.devin.prometheusmetrics.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Java Devin
 * @date 2023/5/2 14:13
 */
@RestController
public class PrometheusHello {

    @RequestMapping("hello")
    public String hello(){
//        try {
//            TimeUnit.SECONDS.sleep(2);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return "hello prometheus";
    }


}
