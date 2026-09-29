<#macro layout title="MentorFlow" showNav=true>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${title}</title>

        <meta name="_csrf" content="${_csrf.token!''}"/>
        <meta name="_csrf_header" content="${_csrf.headerName!''}"/>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <style>
            body { background-color: #f4f6f9; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
            .navbar { box-shadow: 0 2px 4px rgba(0,0,0,.08); }
            .card { transition: transform 0.2s, box-shadow 0.2s; border: none; border-radius: 12px; }
            a { text-decoration: none; }
        </style>
    </head>
    <body>

    <#if showNav>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4 py-3">
            <div class="container">
                <a class="navbar-brand fw-bold" href="/"><i class="bi bi-layers-fill text-primary me-2"></i>MentorFlow</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item"><a class="nav-link" href="/roadmap">Мои карты</a></li>
                        <li class="nav-item"><a class="nav-link" href="/board">Биржа</a></li>
                        <li class="nav-item"><a class="nav-link" href="/notifications">Отклики</a></li>
                        <li class="nav-item"><a class="nav-link" href="/consultations">Встречи</a></li>
                    </ul>
                    <form action="/logout" method="post" class="d-flex m-0">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                        <button type="submit" class="btn btn-outline-light btn-sm rounded-pill px-4">
                            <i class="bi bi-box-arrow-right me-1"></i> Выйти
                        </button>
                    </form>
                </div>
            </div>
        </nav>
    </#if>

    <#-- Контент конкретной страницы -->
    <#nested>

    <#-- Глобальный Toast для уведомлений -->
    <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1055;">
        <div id="globalToast" class="toast align-items-center border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body fw-bold text-white" id="globalToastMessage"></div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Глобальная функция уведомлений
        window.showToast = function(msg, type) {
            const toastEl = document.getElementById('globalToast');
            toastEl.className = 'toast align-items-center border-0 text-bg-' + type;
            document.getElementById('globalToastMessage').innerText = msg;
            new bootstrap.Toast(toastEl).show();
        };

        // Автоматическая подстановка CSRF во все fetch-запросы
        const csrfMeta = document.querySelector('meta[name="_csrf"]');
        const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');

        if (csrfMeta && csrfHeaderMeta && csrfMeta.content) {
            const csrfToken = csrfMeta.content;
            const csrfHeader = csrfHeaderMeta.content;

            const originalFetch = window.fetch;
            window.fetch = function(url, options = {}) {
                options.headers = {
                    ...options.headers,
                    [csrfHeader]: csrfToken
                };
                return originalFetch(url, options);
            };
        }
    </script>

    </body>
    </html>
</#macro>