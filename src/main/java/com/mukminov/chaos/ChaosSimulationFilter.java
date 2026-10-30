package com.mukminov.chaos;

import com.mukminov.metrics.MentorFlowMetrics;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * HTTP-фильтр симуляции задержек и ошибок HTTP 500 для демонстрации SLI/SLO/SLA.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@RequiredArgsConstructor
public class ChaosSimulationFilter extends OncePerRequestFilter {

    private final ChaosState chaosState;
    private final MentorFlowMetrics mentorFlowMetrics;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") || path.startsWith("/api/v1/chaos");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Искусственная задержка (Latency SLI)
        int delay = chaosState.getLatencyMs();
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Демонстрационная фиксация таймера GitHub Sync
        if (chaosState.isDegradeBusinessMetrics()) {
            mentorFlowMetrics.recordGitHubSyncDuration(1500L);
        } else {
            mentorFlowMetrics.recordGitHubSyncDuration(80L);
        }

        // Инжекция HTTP 500 (Availability SLI)
        int errorRate = chaosState.getErrorRatePercent();
        if (errorRate > 0 && ThreadLocalRandom.current().nextInt(100) < errorRate) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":true,\"message\":\"Chaos: Simulated 500 Error\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}