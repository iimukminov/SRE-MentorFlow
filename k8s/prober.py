"""
Синтетический Blackbox-пробер для сервиса sre-mentorflow.
Опрашивает UI (/login) и REST API (/api/v1/users), проверяя HTTP-статус и время ответа <= 250 мс.
"""

import time
import threading
import urllib.request
import urllib.error
from http.server import BaseHTTPRequestHandler, HTTPServer

TARGET_BASE = "http://mentorflow-service.mentorflow.svc.cluster.local:8080"
TIMEOUT_SEC = 0.5
LATENCY_SLO_SEC = 0.25

metrics_lock = threading.Lock()
state = {
    "probes_total": {"ui": 0, "api": 0},
    "probes_success_total": {"ui": 0, "api": 0},
    "last_status": {"ui": 1, "api": 1},
    "last_duration_seconds": {"ui": 0.01, "api": 0.01},
}


def check_endpoint(name: str, path: str) -> None:
    url = f"{TARGET_BASE}{path}"
    start = time.time()
    success = 0
    try:
        req = urllib.request.Request(url, method="GET")
        with urllib.request.urlopen(req, timeout=TIMEOUT_SEC) as resp:
            duration = time.time() - start
            if 200 <= resp.status < 400 and duration <= LATENCY_SLO_SEC:
                success = 1
    except Exception:
        duration = time.time() - start
        success = 0

    with metrics_lock:
        state["probes_total"][name] += 1
        if success == 1:
            state["probes_success_total"][name] += 1
        state["last_status"][name] = success
        state["last_duration_seconds"][name] = round(duration, 4)


def prober_loop() -> None:
    while True:
        check_endpoint("ui", "/login")
        check_endpoint("api", "/api/v1/users")
        time.sleep(2)


class MetricsHandler(BaseHTTPRequestHandler):
    def do_GET(self) -> None:
        if self.path != "/metrics":
            self.send_response(404)
            self.end_headers()
            return

        lines = [
            "# HELP mentorflow_prober_requests_total Total synthetic probes executed",
            "# TYPE mentorflow_prober_requests_total counter",
            "# HELP mentorflow_prober_success_total Total successful synthetic probes",
            "# TYPE mentorflow_prober_success_total counter",
            "# HELP mentorflow_prober_last_status Last probe status (1=up, 0=down)",
            "# TYPE mentorflow_prober_last_status gauge",
            "# HELP mentorflow_prober_duration_seconds Last probe duration in seconds",
            "# TYPE mentorflow_prober_duration_seconds gauge",
        ]

        with metrics_lock:
            for check in ("ui", "api"):
                lines.append(
                    f'mentorflow_prober_requests_total{{check="{check}"}} {state["probes_total"][check]}'
                )
                lines.append(
                    f'mentorflow_prober_success_total{{check="{check}"}} {state["probes_success_total"][check]}'
                )
                lines.append(
                    f'mentorflow_prober_last_status{{check="{check}"}} {state["last_status"][check]}'
                )
                lines.append(
                    f'mentorflow_prober_duration_seconds{{check="{check}"}} {state["last_duration_seconds"][check]}'
                )

        body = ("\n".join(lines) + "\n").encode("utf-8")
        self.send_response(200)
        self.send_header("Content-Type", "text/plain; version=0.0.4; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def log_message(self, format: str, *args) -> None:
        pass


if __name__ == "__main__":
    worker = threading.Thread(target=prober_loop, daemon=True)
    worker.start()
    server = HTTPServer(("0.0.0.0", 8000), MetricsHandler)
    server.serve_forever()