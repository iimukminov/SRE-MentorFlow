package com.mukminov.chaos;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Хранилище параметров управляемой деградации сервиса (HTTP 500, задержки, сбои бизнес-метрик) для проверки поведения SLI, SLO и SLA
 */
@Component
public class ChaosState {

    private final AtomicInteger errorRatePercent = new AtomicInteger(0);
    private final AtomicInteger latencyMs = new AtomicInteger(0);
    private final AtomicBoolean degradeBusinessMetrics = new AtomicBoolean(false);

    public int getErrorRatePercent() {
        return errorRatePercent.get();
    }

    public void setErrorRatePercent(int percent) {
        this.errorRatePercent.set(Math.max(0, Math.min(100, percent)));
    }

    public int getLatencyMs() {
        return latencyMs.get();
    }

    public void setLatencyMs(int ms) {
        this.latencyMs.set(Math.max(0, ms));
    }

    public boolean isDegradeBusinessMetrics() {
        return degradeBusinessMetrics.get();
    }

    public void setDegradeBusinessMetrics(boolean degrade) {
        this.degradeBusinessMetrics.set(degrade);
    }

    public void reset() {
        this.errorRatePercent.set(0);
        this.latencyMs.set(0);
        this.degradeBusinessMetrics.set(false);
    }
}