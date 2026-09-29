<#import "base.ftl" as b>

<@b.layout title="Уведомления - MentorFlow">
    <div class="container pb-5">
        <div class="mb-4">
            <h2 class="fw-bold text-dark mb-1">Входящие отклики</h2>
            <p class="text-muted mb-0">Люди, которые хотят с вами работать</p>
        </div>

        <div class="row" id="requestsContainer">
            <div class="col-12 text-center py-5" id="loadingSpinner">
                <div class="spinner-border text-primary"></div>
            </div>
        </div>
    </div>

    <script>
        const currentUserId = ${currentUserId};
        const container = document.getElementById('requestsContainer');

        document.addEventListener('DOMContentLoaded', loadRequests);

        <#noparse>
        async function loadRequests() {
            try {
                const res = await fetch('/api/v1/connections/requests?userId=' + currentUserId);
                if (!res.ok) throw new Error();
                const requests = await res.json();

                if (requests.length === 0) {
                    container.innerHTML = '<div class="col-12 text-center py-5 text-muted"><i class="bi bi-envelope-open fs-1"></i><p>Нет новых откликов</p></div>';
                    return;
                }

                container.innerHTML = requests.map(req => {
                    let btns = req.status === 'PENDING'
                        ? `<button class="btn btn-success fw-bold w-100 mb-2" onclick="updateStatus(${req.id}, 'ACCEPTED')">Принять</button>
                       <button class="btn btn-outline-danger fw-bold w-100" onclick="updateStatus(${req.id}, 'REJECTED')">Отказать</button>`
                        : `<span class="badge w-100 p-2 ${req.status === 'ACCEPTED' ? 'bg-success' : 'bg-danger'}">${req.status === 'ACCEPTED' ? 'Принято' : 'Отклонено'}</span>`;

                    return `
                    <div class="col-12 mb-3">
                        <div class="card shadow-sm ${req.status === 'ACCEPTED' ? 'border-success' : ''}">
                            <div class="card-body p-4 d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
                                <div class="flex-grow-1">
                                    <div class="mb-2"><span class="badge bg-secondary me-2">${new Date(req.createdAt).toLocaleDateString()}</span><small class="text-muted">На: <strong>${req.advertisementTitle}</strong></small></div>
                                    <h5 class="fw-bold mb-2">@${req.senderUsername}</h5>
                                    <div class="p-3 bg-light rounded text-dark fst-italic">"${req.coverLetter}"</div>
                                </div>
                                <div style="min-width: 150px;">${btns}</div>
                            </div>
                        </div>
                    </div>`;
                }).join('');
            } catch (e) { container.innerHTML = '<div class="col-12 text-center text-danger py-4">Ошибка загрузки</div>'; }
        }
        </#noparse>

        async function updateStatus(id, status) {
            try {
                const res = await fetch('/api/v1/connections/requests/' + id, { method: 'PATCH', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ status }) });
                if (res.ok) { window.showToast(status === 'ACCEPTED' ? 'Принято!' : 'Отклонено', status === 'ACCEPTED' ? 'success' : 'secondary'); loadRequests(); }
                else throw new Error();
            } catch (e) { window.showToast('Ошибка', 'danger'); }
        }
    </script>
</@b.layout>