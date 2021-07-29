package com.alidruid.prometheus;

import com.alibaba.druid.pool.DruidDataSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DruidDSCollector extends AbstractDruidCollector {

    private static final List<String> LABEL_NAMES = Collections.singletonList("pool");

    public DruidDSCollector(DruidDataSource druidDataSource) {
        super(druidDataSource);
    }

    public List<MetricFamilySamples> doCollect() {

        if(druidDataSource == null || druidDataSource.isClosed() ){
            return AbstractDruidCollector.EMPTY_LIST;
        }

        return Arrays.asList(
                createGauge("druid_ds_active_count", "Active count",druidDataSource,
                        druidDataSource.getActiveCount()),
                createGauge("druid_ds_active_peak", "Active peak",druidDataSource,
                        druidDataSource.getActivePeak()),
                createGauge("druid_ds_error_count", "Error count",druidDataSource,
                        druidDataSource.getErrorCount()),
                createGauge("druid_ds_execute_count", "Execute count",druidDataSource,
                        druidDataSource.getExecuteCount()),
                createGauge("druid_ds_max_active", "Max active",druidDataSource,
                        druidDataSource.getMaxActive()),
                createGauge("druid_ds_min_idle", "Min idle",druidDataSource,
                        druidDataSource.getMinIdle()),
                createGauge("druid_ds_max_wait", "Max wait",druidDataSource,
                        druidDataSource.getMaxWait()),
                createGauge("druid_ds_max_wait_thread_count", "Max wait thread count",druidDataSource,
                        druidDataSource.getMaxWaitThreadCount()),
                createGauge("druid_ds_pooling_count", "Pooling count",druidDataSource,
                        druidDataSource.getPoolingCount()),
                createGauge("druid_ds_pooling_peak", "Pooling peak",druidDataSource,
                        druidDataSource.getPoolingPeak()),
                createGauge("druid_ds_rollback_count", "Rollback count",druidDataSource,
                        druidDataSource.getRollbackCount()),
                createGauge("druid_ds_wait_thread_count", "Wait thread count",druidDataSource,
                        druidDataSource.getWaitThreadCount())
        );
    }

    @Override
    public List<String> getLabelNames() {
        return LABEL_NAMES;
    }
}
