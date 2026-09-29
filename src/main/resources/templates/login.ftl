<#import "base.ftl" as b>

<@b.layout title="Вход - MentorFlow" showNav=false>
    <div class="d-flex align-items-center justify-content-center" style="min-height: 100vh;">
        <div class="card p-4 shadow" style="width: 100%; max-width: 400px;">
            <div class="text-center mb-4">
                <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3" style="width: 60px; height: 60px; font-size: 28px; font-weight: bold;">M</div>
                <h3 class="fw-bold text-dark">MentorFlow</h3>
                <p class="text-muted">Добро пожаловать назад</p>
            </div>

            <#if RequestParameters?? && RequestParameters.error??>
                <div class="alert alert-danger py-2"><small>Неверный логин или пароль!</small></div>
            </#if>
            <#if RequestParameters?? && RequestParameters.logout??>
                <div class="alert alert-success py-2"><small>Вы успешно вышли из системы.</small></div>
            </#if>
            <#if RequestParameters?? && RequestParameters.registered??>
                <div class="alert alert-success py-2"><small>Регистрация успешна! Войдите в аккаунт.</small></div>
            </#if>

            <form action="/login" method="post">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                <div class="form-floating mb-3">
                    <input type="text" class="form-control" id="username" name="username" placeholder="Логин" required>
                    <label for="username">Логин</label>
                </div>
                <div class="form-floating mb-4">
                    <input type="password" class="form-control" id="password" name="password" placeholder="Пароль" required>
                    <label for="password">Пароль</label>
                </div>
                <button class="btn btn-primary w-100 py-2 mb-3 fw-bold" type="submit">Войти</button>
                <div class="text-center">
                    <span class="text-muted small">Нет аккаунта?</span>
                    <a href="/register" class="text-decoration-none fw-bold small">Зарегистрироваться</a>
                </div>
            </form>
        </div>
    </div>
</@b.layout>