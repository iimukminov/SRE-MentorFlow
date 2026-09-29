<#import "base.ftl" as b>

<@b.layout title="Встречи - MentorFlow">
    <div class="container pb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold text-dark">Ваши встречи</h2>
            <#if isMentor>
                <button class="btn btn-primary fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#createConsultationModal">
                    <i class="bi bi-calendar-plus me-1"></i> Назначить встречу
                </button>
            </#if>
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <table class="table table-hover mb-0">
                    <thead class="table-light">
                    <tr>
                        <th class="ps-4">Тема / Ссылка</th>
                        <th>Дата и время</th>
                        <th>Статус</th>
                        <th class="text-end pe-4">Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list consultations as c>
                        <tr>
                            <td class="ps-4 align-middle">
                                <a href="${(c.getMeetLink())!""}" target="_blank" class="fw-bold text-decoration-none">
                                    <i class="bi bi-camera-video me-2"></i> Перейти в Meet
                                </a>
                            </td>
                            <td class="align-middle">
                                ${c.getScheduledTime()}
                            </td>
                            <td class="align-middle">
                                <span class="badge ${(c.getStatus() == 'SCHEDULED')?string('bg-primary', 'bg-secondary')}">${c.getStatus()}</span>
                            </td>
                            <td class="text-end pe-4">
                                <#if isMentor && c.getStatus() == 'SCHEDULED'>
                                    <button class="btn btn-sm btn-outline-danger" onclick="updateStatus(${c.getId()}, 'CANCELLED')">Отменить</button>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="modal fade" id="createConsultationModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content border-0 shadow">
                <div class="modal-header bg-primary text-white border-0">
                    <h5 class="modal-title fw-bold">Новая встреча</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form id="createConsultationForm">
                    <div class="modal-body p-4">
                        <input type="hidden" name="mentorId" value="${currentUserId}">

                        <div class="mb-3">
                            <label class="form-label fw-bold">Ученик (Менти)</label>
                            <select class="form-select" name="menteeId" required>
                                <option value="" disabled selected>Выберите ученика...</option>
                                <#if mentees??>
                                    <#list mentees as mentee>
                                        <option value="${mentee.getId()}">@${mentee.getUsername()}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Дата и время</label>
                            <input type="datetime-local" class="form-control" name="scheduledTime" required>
                        </div>
                    </div>
                    <div class="modal-footer bg-light border-0">
                        <button type="submit" class="btn btn-primary w-100 fw-bold">Создать</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        document.getElementById('createConsultationForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const data = Object.fromEntries(new FormData(e.target));

            if (data.scheduledTime) {
                data.scheduledTime = data.scheduledTime + ":00Z";
            }

            data.mentorId = parseInt(data.mentorId);
            data.menteeId = parseInt(data.menteeId);

            const res = await fetch('/api/v1/consultations', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(data)
            });
            if(res.ok) window.location.reload();
        });

        async function updateStatus(id, status) {
            await fetch('/api/v1/consultations/' + id, {
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({status: status})
            });
            window.location.reload();
        }
    </script>
</@b.layout>