<#import "base.ftl" as b>

<@b.layout title="Регистрация - MentorFlow" showNav=false>
    <div class="d-flex align-items-center justify-content-center py-5" style="min-height: 100vh;">
        <div class="card p-4 shadow" style="width: 100%; max-width: 450px;">
            <div class="text-center mb-4">
                <div class="bg-success text-white rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3" style="width: 60px; height: 60px; font-size: 28px; font-weight: bold;">M</div>
                <h3 class="fw-bold text-dark">Регистрация</h3>
                <p class="text-muted">Начните свой путь в MentorFlow</p>
            </div>

            <div id="errorMessage" class="alert alert-danger d-none py-2"><small>Ошибка регистрации. Проверьте данные.</small></div>

            <form id="registerForm">
                <div class="form-floating mb-3">
                    <input type="text" class="form-control" name="username" placeholder="Логин" required>
                    <label>Логин *</label>
                </div>
                <div class="form-floating mb-3">
                    <input type="email" class="form-control" name="email" placeholder="Email" required>
                    <label>Email *</label>
                </div>
                <div class="form-floating mb-3">
                    <input type="password" class="form-control" name="password" placeholder="Пароль" required>
                    <label>Пароль *</label>
                </div>
                <div class="form-floating mb-4">
                    <input type="text" class="form-control" name="githubUsername" placeholder="GitHub">
                    <label>GitHub Username <span class="text-muted">(опц.)</span></label>
                </div>
                <button class="btn btn-success w-100 py-2 mb-3 fw-bold" type="submit" id="submitBtn">
                    <span id="btnText">Зарегистрироваться</span>
                    <span id="btnLoader" class="spinner-border spinner-border-sm d-none"></span>
                </button>
                <div class="text-center">
                    <span class="text-muted small">Уже есть аккаунт?</span>
                    <a href="/login" class="text-decoration-none fw-bold text-success small">Войти</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        document.getElementById('registerForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            const btn = document.getElementById('submitBtn'), btnText = document.getElementById('btnText'), btnLoader = document.getElementById('btnLoader'), err = document.getElementById('errorMessage');
            btn.disabled = true; btnText.classList.add('d-none'); btnLoader.classList.remove('d-none'); err.classList.add('d-none');

            try {
                const res = await fetch('/api/v1/users', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(Object.fromEntries(new FormData(this))) });
                if (res.ok) window.location.href = '/login?registered=true';
                else err.classList.remove('d-none');
            } catch (error) { err.innerHTML = '<small>Сервер недоступен.</small>'; err.classList.remove('d-none'); }
            finally { btn.disabled = false; btnText.classList.remove('d-none'); btnLoader.classList.add('d-none'); }
        });
    </script>
</@b.layout>