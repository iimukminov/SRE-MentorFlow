<#import "base.ftl" as b>

<@b.layout title="Доска заявок - MentorFlow">
    <div class="container pb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fw-bold text-dark mb-1">Доска заявок</h2>
                <p class="text-muted mb-0">Найдите своего идеального наставника или ученика</p>
            </div>
            <button class="btn btn-primary fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#createAdModal">
                <i class="bi bi-megaphone me-1"></i> Опубликовать анкету
            </button>
        </div>

        <ul class="nav nav-pills mb-4" id="boardTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active px-4 rounded-pill me-2" data-bs-toggle="pill" data-type="SEEKING_MENTOR" type="button">Ищут наставника (Джуны)</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link px-4 rounded-pill" data-bs-toggle="pill" data-type="SEEKING_MENTEE" type="button">Готовы обучать (Менторы)</button>
            </li>
        </ul>

        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4" id="adsContainer">
            <div class="col-12 text-center py-5 w-100">
                <div class="spinner-border text-primary" role="status"></div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="createAdModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow">
                <div class="modal-header bg-primary text-white border-0">
                    <h5 class="modal-title fw-bold"><i class="bi bi-pencil-square me-2"></i>Новая анкета</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form id="createAdForm">
                    <div class="modal-body p-4">
                        <input type="hidden" name="authorId" value="${currentUserId}">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Роль в заявке</label>
                            <#if isMentor>
                                <input type="hidden" name="type" value="SEEKING_MENTEE">
                                <input type="text" class="form-control bg-light text-muted" value="Ищу Ученика (Я ментор)" readonly>
                            <#else>
                                <input type="hidden" name="type" value="SEEKING_MENTOR">
                                <input type="text" class="form-control bg-light text-muted" value="Ищу Ментора (Я ученик)" readonly>
                            </#if>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Заголовок</label>
                            <input type="text" class="form-control" name="title" required placeholder="Например: Изучаю Java, Spring Boot">
                        </div>
                        <div class="mb-0">
                            <label class="form-label fw-bold">О себе</label>
                            <textarea class="form-control" name="content" rows="4" required placeholder="Опишите ваши навыки и пожелания..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer bg-light border-0">
                        <button type="button" class="btn btn-secondary fw-bold" data-bs-dismiss="modal">Отмена</button>
                        <button type="submit" class="btn btn-primary fw-bold px-4" id="submitAdBtn">Опубликовать</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="modal fade" id="replyModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow">
                <div class="modal-header bg-success text-white border-0">
                    <h5 class="modal-title fw-bold"><i class="bi bi-send-fill me-2"></i>Откликнуться</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form id="replyForm">
                    <div class="modal-body p-4">
                        <input type="hidden" id="replyAdId">
                        <input type="hidden" name="senderId" value="${currentUserId}">
                        <p class="text-muted small mb-3">Отклик для: <strong id="replyAuthorName"></strong></p>
                        <div class="mb-0">
                            <label class="form-label fw-bold">Сопроводительное письмо</label>
                            <textarea class="form-control" name="coverLetter" rows="3" required placeholder="Напишите, почему вы подходите..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer bg-light border-0">
                        <button type="button" class="btn btn-secondary fw-bold" data-bs-dismiss="modal">Отмена</button>
                        <button type="submit" class="btn btn-success fw-bold px-4" id="submitReplyBtn">Отправить</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        const currentUserId = ${currentUserId};
        const adsContainer = document.getElementById('adsContainer');

        <#noparse>
        async function loadAds(type) {
            adsContainer.innerHTML = '<div class="col-12 text-center py-5 w-100"><div class="spinner-border text-primary"></div></div>';
            try {
                const res = await fetch('/api/v1/board?type=' + type);
                if (!res.ok) throw new Error();
                const ads = await res.json();

                if (ads.length === 0) {
                    adsContainer.innerHTML = '<div class="col-12 text-center py-5 text-muted"><i class="bi bi-inbox fs-1"></i><p>В этой категории пока нет заявок</p></div>';
                    return;
                }

                adsContainer.innerHTML = ads.map(ad => `
                <div class="col">
                    <div class="card h-100 shadow-sm">
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-2">
                                <span class="badge bg-light text-dark border">@${ad.authorUsername}</span>
                                <small class="text-muted">${new Date(ad.createdAt).toLocaleDateString()}</small>
                            </div>
                            <h5 class="fw-bold">${ad.title}</h5>
                            <p class="text-muted small">${ad.content}</p>
                        </div>
                        <div class="card-footer bg-transparent border-top-0 pb-3 pt-0">
                            ${ad.authorId !== currentUserId ?
                    `<button class="btn btn-outline-primary w-100 fw-bold" onclick="openReplyModal(${ad.id}, '${ad.authorUsername}')">Откликнуться</button>` :
                    `<button class="btn btn-secondary w-100 fw-bold" disabled>Ваша анкета</button>`
                }
                        </div>
                    </div>
                </div>
            `).join('');
            } catch (e) { showToast('Ошибка загрузки заявок', 'danger'); }
        }
        </#noparse>

        document.querySelectorAll('.nav-link[data-bs-toggle="pill"]').forEach(tab => {
            tab.addEventListener('shown.bs.tab', (e) => loadAds(e.target.dataset.type));
        });

        document.getElementById('createAdForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            document.getElementById('submitAdBtn').disabled = true;
            const data = Object.fromEntries(new FormData(this).entries());
            data.authorId = parseInt(data.authorId);
            try {
                const res = await fetch('/api/v1/board', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) });
                if (res.ok) window.location.reload(); else throw new Error();
            } catch (e) { showToast('Ошибка публикации', 'danger'); document.getElementById('submitAdBtn').disabled = false; }
        });

        window.openReplyModal = function(adId, authorUsername) {
            document.getElementById('replyAdId').value = adId;
            document.getElementById('replyAuthorName').innerText = '@' + authorUsername;
            new bootstrap.Modal(document.getElementById('replyModal')).show();
        };

        document.getElementById('replyForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            document.getElementById('submitReplyBtn').disabled = true;
            const data = Object.fromEntries(new FormData(this).entries());
            data.senderId = parseInt(data.senderId);
            try {
                const res = await fetch('/api/v1/board/' + document.getElementById('replyAdId').value + '/reply', {
                    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)
                });
                if (res.ok) { showToast('Отклик отправлен!', 'success'); setTimeout(() => window.location.reload(), 1500); }
                else throw new Error();
            } catch (e) { showToast('Ошибка отправки', 'danger'); document.getElementById('submitReplyBtn').disabled = false; }
        });

        document.addEventListener('DOMContentLoaded', () => loadAds('SEEKING_MENTOR'));
    </script>
</@b.layout>