package com.alidruid.prometheus;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.JdbcConnectionStat;
import com.alibaba.druid.stat.JdbcSqlStat;
import com.alibaba.druid.stat.JdbcStatManager;
import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.GaugeMetricFamily;

import java.util.*;
import java.util.function.Function;

public class DruidSqlCollector  extends Collector {

    private static final List<String> LABEL_NAMES = Collections.singletonList("sql");

    private DruidDataSource druidDataSource;

    public DruidSqlCollector(DruidDataSource druidDataSource){
        this.druidDataSource = druidDataSource;
    }

    @Override
    public List<Collector.MetricFamilySamples> collect() {

        Map<String, JdbcSqlStat> jdbcSqlStatMap =  druidDataSource.getSqlStatMap();
        if(jdbcSqlStatMap == null){

        }
        List<Collector.MetricFamilySamples> samples = new ArrayList<>();
        for(final JdbcSqlStat jdbcSqlStat:jdbcSqlStatMap.values()){
            samples.add(
                    createGauge("errorCount", "errorCount",jdbcSqlStat,
                            stat -> (double) stat.getErrorCount())
                    );
            samples.add(
                    createGauge("ConcurrentMax", "ConcurrentMax",jdbcSqlStat,
                            stat -> (double) stat.getConcurrentMax())
            );
            samples.add(
                    createGauge("RunningCount", "RunningCount",jdbcSqlStat,
                            stat -> (double) stat.getRunningCount())
            );
            samples.add(
                    createGauge("FetchRowCount", "FetchRowCount",jdbcSqlStat,
                            stat -> (double) stat.getFetchRowCount())
            );
            samples.add(
                    createGauge("UpdateCount", "UpdateCount",jdbcSqlStat,
                            stat -> (double) stat.getUpdateCount())
            );

            long[] histogramValues = jdbcSqlStat.getHistogramValues();
            samples.add(
                    createCounter("hi", "<100ms",jdbcSqlStat,
                            stat -> (double) stat.getUpdateCount())
            );
        }
        return samples;
    }


    private CounterMetricFamily createCounter(String metric, String help, JdbcSqlStat jdbcSqlStat,
                                            Function<JdbcSqlStat, Double> metricValueFunction) {
        CounterMetricFamily metricFamily = new CounterMetricFamily(metric, help, LABEL_NAMES);
        metricFamily.addMetric(Arrays.asList(jdbcSqlStat.getSql()),metricValueFunction.apply(jdbcSqlStat));
        return metricFamily;
    }

    private GaugeMetricFamily createGauge(String metric, String help, JdbcSqlStat jdbcSqlStat,
                                            Function<JdbcSqlStat, Double> metricValueFunction) {
        GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, LABEL_NAMES);
        metricFamily.addMetric(Arrays.asList(jdbcSqlStat.getSql()),metricValueFunction.apply(jdbcSqlStat));
        return metricFamily;
    }
}
