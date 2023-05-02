package com.devin.prometheusmetrics.metrics.service;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.springframework.stereotype.Component;

/**
 * @author Java Devin
 * @date 2023/5/2 15:01
 */
@Component
public class MetricsService {

    /**
     * 注册一个Gauge的数据的指标 Gauge是prometheus的指标类型之一
     * 注意这里为什么要使用static以及final修饰
     * 是因为现在创建的指标对象就相当于一个容器，数据装进这个容器中交给Prometheus，如果每次存储数据时都new一个指标对象，那么之前的数据就没了
     */
    private static final Gauge gauge = Gauge
            // name：指标名称  help：指标的描述
            .build("redis_keys_num","redis keys num")
            // 使用默认的注册器
            .register(CollectorRegistry.defaultRegistry);

    public void addMetric(){
        // 这里就是给注册的指标存储数据的地方 我们这里只是示例 写一个假数据 大家按需存储即可
        // 如监控redis中key的数量的话 就可以先使用jedis查询出key的数量 然后再将查询到的数据交给指标对象即可
        gauge.set(100);
    }


}
