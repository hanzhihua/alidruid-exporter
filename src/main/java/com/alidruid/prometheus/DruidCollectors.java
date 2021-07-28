package com.alidruid.prometheus;

import com.alibaba.druid.pool.DruidDataSource;
import io.prometheus.client.CollectorRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@AllArgsConstructor
public class DruidCollectors {

    private final CollectorRegistry registry;
    private final DruidDataSource druidDataSource;

    private AtomicBoolean started = new AtomicBoolean(false);



    public void init(){
        if(!started.getAndSet(true)) {
            DruidDSCollector druidCollector = new DruidDSCollector(druidDataSource);
            druidCollector.register(registry);

            DruidSqlCollector druidSqlCollector = new DruidSqlCollector(druidDataSource);
            druidCollector.register(registry);
        }else{
            log.warn("DruidCollectors already init");
        }
    }


}
