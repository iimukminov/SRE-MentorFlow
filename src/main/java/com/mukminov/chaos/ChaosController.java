package com.mukminov.chaos;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST-контроллер управления параметрами сбоев для тестирования SLI, SLO и SLA
 */
@Hidden
@RestController
@RequestMapping("/api/v1/chaos")
@RequiredArgsConstructor
public class ChaosController {

    private final ChaosState chaosState;

    @PostMapping("/config")
    public ResponseEntity<?> configureChaos(
            @RequestParam(defaultValue = "0") int errorRatePercent,
            @RequestParam(defaultValue = "0") int latencyMs,
            @RequestParam(defaultValue = "false") boolean degradeBusiness) {
        chaosState.setErrorRatePercent(errorRatePercent);
        chaosState.setLatencyMs(latencyMs);
        chaosState.setDegradeBusinessMetrics(degradeBusiness);
        return ResponseEntity.ok(getCurrentStatus());
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetChaos() {
        chaosState.reset();
        return ResponseEntity.ok(getCurrentStatus());
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok(getCurrentStatus());
    }

    private Map<String, Object> getCurrentStatus() {
        return Map.of(
                "errorRatePercent", chaosState.getErrorRatePercent(),
                "latencyMs", chaosState.getLatencyMs(),
                "degradeBusinessMetrics", chaosState.isDegradeBusinessMetrics()
        );
    }
}