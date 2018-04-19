package com.ctrip.xpipe.redis.console.alert.policy.timing;

import com.ctrip.xpipe.redis.console.alert.ALERT_TYPE;
import com.ctrip.xpipe.redis.console.alert.AlertEntity;
import com.ctrip.xpipe.redis.console.alert.policy.AlertPolicy;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.LongSupplier;

/**
 * @author chen.zhu
 * <p>
 * Apr 19, 2018
 */
public class RecoveryTimeSlotControl implements TimeSlotControl {

    private RecoveryTimeAlgorithm calculator = new NaiveRecoveryTimeAlgorithm();

    private Map<ALERT_TYPE, LongSupplier> checkIntervals = Maps.newHashMap();

    @Override
    public long durationMilli(AlertEntity alert) {
        ALERT_TYPE type = alert.getAlertType();
        if(!type.reportRecovery()) {
            return -1;
        }

        long checkInterval = checkIntervals.get(type).getAsLong();
        return calculator.calculate(checkInterval);
    }

    @Override
    public void mark(ALERT_TYPE alertType, LongSupplier checkInterval) {
        checkIntervals.put(alertType, checkInterval);
    }

    @Override
    public boolean supports(Class<? extends AlertPolicy> clazz) {
        return clazz.isAssignableFrom(RecoveryTimeSlotControl.class);
    }
}