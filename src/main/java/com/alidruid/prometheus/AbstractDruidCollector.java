package com.alidruid.prometheus;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.JdbcSqlStat;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Slf4j
public abstract class AbstractDruidCollector extends Collector {

    protected DruidDataSource druidDataSource;

    public abstract List<String> getLabelNames();

    public abstract List<MetricFamilySamples> doCollect();

    protected static final List<MetricFamilySamples> EMPTY_LIST = new ArrayList<>();

    protected <T> GaugeMetricFamily createGauge(String metric, String help, JdbcSqlStat jdbcSqlStat,
                                                double value) {
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, getLabelNames());
        metricFamily.addMetric(Arrays.asList(jdbcSqlStat.getSql()),value);
        return metricFamily;
    }

    protected <T> GaugeMetricFamily createGauge(String metric, String help, DruidDataSource druidDataSource,
                                                double value) {
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, getLabelNames());
        metricFamily.addMetric(Arrays.asList(druidDataSource.getName()),value);
        return metricFamily;
    }

    public final List<MetricFamilySamples> collect() {

        if (druidDataSource == null || druidDataSource.isClosed()) {
            log.warn("druidDataSource:{} is invalid",druidDataSource);
            return AbstractDruidCollector.EMPTY_LIST;
        }
        return doCollect();
    }
}
