package com.alidruid.prometheus;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.JdbcSqlStat;
import io.prometheus.client.Collector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class DruidSqlCollector  extends AbstractDruidCollector {

    private static final List<String> LABEL_NAMES = Collections.singletonList("sql");

    public DruidSqlCollector(DruidDataSource druidDataSource) {
        super(druidDataSource);
    }


    @Override
    public List<Collector.MetricFamilySamples> doCollect() {

        Map<String, JdbcSqlStat> jdbcSqlStatMap =  druidDataSource.getSqlStatMap();
        if(jdbcSqlStatMap == null){

        }
        List<Collector.MetricFamilySamples> samples = new ArrayList<>();
        for(final JdbcSqlStat jdbcSqlStat:jdbcSqlStatMap.values()){
            samples.add(
                    createGauge("druid_sql_error_count", "druid sql errorCount",jdbcSqlStat,
                            jdbcSqlStat.getErrorCount())
                    );
            samples.add(
                    createGauge("druid_sql_concurrent_max", "druid sql ConcurrentMax",jdbcSqlStat,
                            jdbcSqlStat.getConcurrentMax())
            );
            samples.add(
                    createGauge("druid_sql_running_count", "druid sql RunningCount",jdbcSqlStat,
                            jdbcSqlStat.getRunningCount())
            );
            samples.add(
                    createGauge("druid_sql_fetch_rowcount", "druid sql FetchRowCount",jdbcSqlStat,
                            jdbcSqlStat.getFetchRowCount())
            );
            samples.add(
                    createGauge("druid_sql_update_count", "druid sql UpdateCount",jdbcSqlStat,
                            jdbcSqlStat.getUpdateCount())
            );

            long[] histogramValues = jdbcSqlStat.getHistogramValues();
            if(histogramValues.length == 8){
                samples.add(
                        createGauge("druid_sql_cost_morethan100ms", "druid sql cost > 100ms",jdbcSqlStat,
                                histogramValues[3]));
                samples.add(
                        createGauge("druid_sql_cost_morethan1s", "druid sql cost > 1s",jdbcSqlStat,
                                histogramValues[4]));
                samples.add(
                        createGauge("druid_sql_cost_morethan10s", "druid sql cost > 10s",jdbcSqlStat,
                                histogramValues[5]));
                samples.add(
                        createGauge("druid_sql_cost_morethan100s", "druid sql cost > 100s",jdbcSqlStat,
                                histogramValues[6]));
                samples.add(
                        createGauge("druid_sql_cost_morethan1000ms", "druid sql cost > 1000s",jdbcSqlStat,
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
