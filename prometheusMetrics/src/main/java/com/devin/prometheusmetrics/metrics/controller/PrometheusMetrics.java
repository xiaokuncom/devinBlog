package com.devin.prometheusmetrics.metrics.controller;

import com.devin.prometheusmetrics.metrics.service.MetricsService;
import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Java Devin
 * @date 2023/5/2 14:59
 */
@RestController
public class PrometheusMetrics extends MetricsServlet {

    @Autowired
    private MetricsService metricsService;

    @GetMapping("/metrics")
    public void redisMetrics(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 调用方法 注册prometheus指标对象 -> 计算数据 -> 将数据存储进指标对象
        metricsService.addMetric();

        // 上面的操作我们已经将指标以及数据注册进prometheus中了
        // 接下来我们不需要做其他操作 放行请求即可
        this.doGet(request,response);
    }

}
