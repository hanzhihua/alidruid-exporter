package com.alidruid.prometheus;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.JdbcConnectionStat;
import com.alibaba.druid.stat.JdbcSqlStat;
import com.alibaba.druid.stat.JdbcStatManager;
import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.GaugeMetricFamily;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;

@Slf4j
public class DruidSqlCollector  extends AbstractDruidCollector {

    private static final List<String> LABEL_NAMES = Collections.singletonList("sql");

    public DruidSqlCollector(DruidDataSource druidDataSource) {
        super(druidDataSource);
    }


    @Override
    public List<Collector.MetricFamilySamples> collect() {

        Map<String, JdbcSqlStat> jdbcSqlStatMap =  druidDataSource.getSqlStatMap();
        if(jdbcSqlStatMap == null){

        }
        List<Collector.MetricFamilySamples> samples = new ArrayList<>();
        for(final JdbcSqlStat jdbcSqlStat:jdbcSqlStatMap.values()){
            samples.add(
                    createGauge("druid_sql_error_count", "errorCount",jdbcSqlStat,
                            jdbcSqlStat.getErrorCount())
                    );
            samples.add(
                    createGauge("druid_sql_concurrent_max", "ConcurrentMax",jdbcSqlStat,
                            jdbcSqlStat.getConcurrentMax())
            );
            samples.add(
                    createGauge("druid_sql_running_count", "RunningCount",jdbcSqlStat,
                            jdbcSqlStat.getRunningCount())
            );
            samples.add(
                    createGauge("druid_sql_fetch_rowcount", "FetchRowCount",jdbcSqlStat,
                            jdbcSqlStat.getFetchRowCount())
            );
            samples.add(
                    createGauge("druid_sql_update_count", "UpdateCount",jdbcSqlStat,
                            jdbcSqlStat.getUpdateCount())
            );

            long[] histogramValues = jdbcSqlStat.getHistogramValues();
            if(histogramValues.length == 8){
                samples.add(
                        createGauge("druid_sql_cost_morethan100ms", "cost > 100ms",jdbcSqlStat,
                                histogramValues[3]));
                samples.add(
                        createGauge("druid_sql_cost_morethan1s", "cost > 1s",jdbcSqlStat,
                                histogramValues[4]));
                samples.add(
                        createGauge("druid_sql_cost_morethan10s", "cost > 10s",jdbcSqlStat,
                                histogramValues[5]));
                samples.add(
                        createGauge("druid_sql_cost_morethan100s", "cost > 100s",jdbcSqlStat,
                                histogramValues[6]));
                samples.add(
                        createGauge("druid_sql_cost_morethan1000ms", "cost > 100s",jdbcSqlStat,
                                histogramValues[7]));
            }else{
                log.warn("histogramValues length{} is invalid!",histogramValues.length);
            }
        }
        return samples;
    }


    @Override
    public List<String> getLabelNames() {
        return LABEL_NAMES;
    }
}
