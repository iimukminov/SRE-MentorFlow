import time
import threading
import urllib.request

TARGET_URLS = [
    "http://localhost:8080/login",
    "http://localhost:8080/api/v1/users",
]
TARGET_RPS = 60
RUNNING = True


def worker():
    while RUNNING:
        start = time.time()
        for url in TARGET_URLS:
            try:
                with urllib.request.urlopen(url, timeout=1.5) as resp:
                    resp.read()
            except Exception:
                pass
        elapsed = time.time() - start
        sleep_sec = max(0.0, (len(TARGET_URLS) * 6 / TARGET_RPS) - elapsed)
        if sleep_sec > 0:
            time.sleep(sleep_sec)


if __name__ == "__main__":
    print(f"Генератор нагрузки запущен (~{TARGET_RPS} RPS). Нажмите Ctrl+C для выхода.")
    for _ in range(6):
        threading.Thread(target=worker, daemon=True).start()
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        RUNNING = False