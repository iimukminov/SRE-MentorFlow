<#import "../base.ftl" as b>

<@b.layout title="Ошибка сервера - MentorFlow" showNav=false>
    <div class="d-flex align-items-center justify-content-center" style="min-height: 100vh;">
        <div class="text-center p-5 bg-white rounded shadow-sm" style="max-width: 600px;">
            <h1 class="display-1 fw-bold text-danger mb-3">500</h1>
            <h3 class="fw-bold text-dark">Ой, что-то пошло не так! 🛠️</h3>
            <p class="text-muted mb-4">Произошла непредвиденная ошибка. Мы уже работаем над её устранением.</p>

            <#if errorMessage??>
                <div class="text-start bg-light p-3 rounded mb-4 text-muted font-monospace small" style="word-wrap: break-word;">
                    <strong>Детали:</strong><br>${errorMessage}
                </div>
            </#if>

            <a href="/" class="btn btn-primary fw-bold px-4 py-2">Вернуться на главную</a>
        </div>
    </div>
</@b.layout>