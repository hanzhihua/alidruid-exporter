package com.alidruid.prometheus;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.JdbcSqlStat;
import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.GaugeMetricFamily;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public abstract class AbstractDruidCollector extends Collector {

    protected DruidDataSource druidDataSource;

    public abstract List<String> getLabelNames();


//    protected <T> CounterMetricFamily createCounter(String metric, String help, T jdbcSqlStat,
//                                              Function<T, Double> metricValueFunction) {
//        CounterMetricFamily metricFamily = new CounterMetricFamily(metric, help, getLabelNames());
//        metricFamily.addMetric(Arrays.asList("jdbcSqlStat.getSql()"),metricValueFunction.apply(jdbcSqlStat));
//        return metricFamily;
//    }

    protected <T> GaugeMetricFamily createGauge(String metric, String help, JdbcSqlStat jdbcSqlStat,
                                                double value) {
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, getLabelNames());
        metricFamily.addMetric(Arrays.asList("jdbcSqlStat.getSql()"),value);
        return metricFamily;
    }

    protected <T> GaugeMetricFamily createGauge(String metric, String help, DruidDataSource druidDataSource,
                                                double value) {
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, getLabelNames());
        metricFamily.addMetric(Arrays.asList(druidDataSource.getName()),value);
        return metricFamily;
    }
}
