<#import "base.ftl" as b>

<@b.layout title="Панель обучения - MentorFlow">
    <div class="container pb-5">
        <div class="d-flex justify-content-between align-items-center mb-5 border-bottom pb-3">
            <div>
                <h2 class="fw-bold text-dark mb-1"><#if isMentor>Ваши дорожные карты<#else>Ваш план обучения</#if></h2>
                <p class="text-muted mb-0"><#if isMentor>Управление учениками<#else>Следите за прогрессом</#if></p>
            </div>
            <#if isMentor>
                <button class="btn btn-primary fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#createRoadmapModal">
                    <i class="bi bi-plus-lg me-1"></i> Создать карту
                </button>
            </#if>
        </div>

        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
            <#if roadmaps?? && roadmaps?size &gt; 0>
                <#list roadmaps as roadmap>
                    <div class="col">
                        <div class="card h-100 shadow-sm border-0">
                            <div class="card-body">
                                <div class="d-flex justify-content-between mb-3">
                                    <div class="bg-primary bg-opacity-10 text-primary rounded p-2"><i class="bi bi-map fs-4"></i></div>
                                    <span class="badge bg-success d-flex align-items-center">В процессе</span>
                                </div>
                                <h5 class="fw-bold mb-3">${roadmap.getTitle()}</h5>
                                <ul class="list-unstyled mb-4 text-muted small">
                                    <#if isMentor><li class="mb-2"><i class="bi bi-person me-2"></i>Студент: @${roadmap.getMenteeUsername()!''}</li>
                                    <#else><li class="mb-2"><i class="bi bi-person-badge me-2"></i>Наставник: @${roadmap.getMentorUsername()!''}</li></#if>
                                    <li class="mb-2"><i class="bi bi-hourglass me-2"></i>AFK: ${roadmap.getMaxAfkDays()!5} дней</li>
                                    <#if roadmap.getMeetLink()?? && roadmap.getMeetLink() != ""><li><i class="bi bi-link me-2"></i><a href="${roadmap.getMeetLink()}" target="_blank">Комната созвонов</a></li></#if>
                                </ul>

                                <hr class="text-muted opacity-25">
                                <h6 class="fw-bold mb-3 small">Программа обучения:</h6>
                                <div class="steps-container d-flex flex-column gap-2" style="max-height: 250px; overflow-y: auto;">
                                    <#if roadmap.getSteps()?? && roadmap.getSteps()?size &gt; 0>
                                        <#list roadmap.getSteps() as step>
                                            <div class="p-2 border rounded bg-light">
                                                <div class="d-flex justify-content-between align-items-center mb-1">
                                                    <span class="fw-bold small text-dark">Шаг ${step.getStepOrder()}: ${step.getTitle()}</span>

                                                    <#if step.getStatus() == "LOCKED">
                                                        <span class="badge bg-secondary"><i class="bi bi-lock-fill"></i> Закрыт</span>
                                                    <#elseif step.getStatus() == "IN_PROGRESS">
                                                        <span class="badge bg-primary"><i class="bi bi-play-fill"></i> В процессе</span>
                                                    <#elseif step.getStatus() == "REVIEW">
                                                        <span class="badge bg-warning text-dark"><i class="bi bi-eye-fill"></i> Ревью</span>
                                                    <#else>
                                                        <span class="badge bg-success"><i class="bi bi-check-all"></i> Готово</span>
                                                    </#if>
                                                </div>

                                                <div class="d-flex justify-content-between align-items-center mt-2">
                                                    <small class="text-muted"><a href="${step.getContentLink()}" target="_blank"><i class="bi bi-github"></i> Репозиторий</a></small>

                                                    <#if step.getStatus() == "IN_PROGRESS">
                                                        <div class="d-flex align-items-center gap-2">
                                                            <div class="progress" style="width: 80px; height: 10px;">
                                                                <#assign progress = ((step.getActualCommits()!0) / step.getRequiredCommits()) * 100>
                                                                <#if progress &gt; 100><#assign progress = 100></#if>
                                                                <div class="progress-bar bg-success" role="progressbar" style="width: ${progress}%"></div>
                                                            </div>
                                                            <small class="fw-bold">${step.getActualCommits()!0}/${step.getRequiredCommits()}</small>
                                                            <button class="btn btn-sm btn-outline-primary py-0 px-1" onclick="syncStep(${step.getId()})" title="Обновить прогресс">
                                                                <i class="bi bi-arrow-clockwise"></i>
                                                            </button>
                                                        </div>
                                                    <#elseif step.getStatus() == "REVIEW">
                                                        <#if isMentor>
                                                            <button class="btn btn-sm btn-warning fw-bold py-0" onclick="openReviewModal(${step.getId()}, '${step.getTitle()?js_string}')">
                                                                <i class="bi bi-pencil-square"></i> Проверить
                                                            </button>
                                                        <#else>
                                                            <small class="text-warning fw-bold"><i class="bi bi-hourglass-split"></i> Проверяется...</small>
                                                        </#if>
                                                    </#if>
                                                </div>
                                            </div>
                                            <#assign feedback = (step.getReviewFeedback())!>
                                            <#if feedback?has_content>
                                                <div class="mt-2 p-2 bg-warning bg-opacity-10 border-start border-warning border-3 rounded">
                                                    <small class="fw-bold d-block mb-1">
                                                        <i class="bi bi-chat-left-text"></i> Комментарий ментора:
                                                    </small>
                                                    <p class="mb-0 small">${(feedback.getComments())!""}</p>
                                                </div>
                                            </#if>
                                        </#list>
                                    <#else>
                                        <div class="text-muted small text-center py-2">Шагов пока нет</div>
                                    </#if>
                                </div>
                            </div>
                            <#if isMentor>
                                <div class="card-footer bg-transparent py-3">
                                    <button class="btn btn-sm btn-outline-primary fw-bold w-100" onclick="openAddStepModal(${roadmap.getId()}, '${roadmap.getTitle()?js_string}')">Добавить шаг</button>
                                </div>
                            </#if>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="col-12 text-center py-5">
                    <i class="bi bi-inbox text-muted" style="font-size: 4rem;"></i>
                    <h4 class="mt-3 text-muted">Карт пока нет</h4>
                </div>
            </#if>
        </div>
    </div>

    <#if isMentor>
        <div class="modal fade" id="createRoadmapModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow">
                    <div class="modal-header bg-primary text-white border-0">
                        <h5 class="modal-title fw-bold"><i class="bi bi-pencil-square me-2"></i>Новая дорожная карта</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <form id="createRoadmapForm">
                            <input type="hidden" name="mentorId" value="${currentUserId}">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Название курса</label>
                                <input type="text" class="form-control" name="title" required placeholder="Например: Разработка на Kafka">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Ученик (Менти)</label>
                                <select class="form-select" name="menteeId" required>
                                    <option value="" disabled selected>Выберите ученика...</option>
                                    <#if mentees??>
                                        <#list mentees as mentee>
                                            <#if mentee.getId() != currentUserId>
                                                <option value="${mentee.getId()}">@${mentee.getUsername()}</option>
                                            </#if>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                            <div class="p-3 bg-light rounded-3 border mb-3">
                                <h6 class="fw-bold mb-3 text-danger"><i class="bi bi-robot me-1"></i>Авто-контроль успеваемости</h6>
                                <div class="mb-3">
                                    <label class="form-label">Максимум дней простоя (AFK)</label>
                                    <input type="number" class="form-control" name="maxAfkDays" value="5" min="1" required>
                                </div>
                                <div>
                                    <label class="form-label">Ссылка для авто-созвона</label>
                                    <input type="url" class="form-control" name="meetLink" placeholder="https://meet.google.com/..." required>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary w-100 fw-bold py-2" id="submitBtn">Создать карту</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addStepModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow">
                    <div class="modal-header bg-success text-white border-0">
                        <h5 class="modal-title fw-bold"><i class="bi bi-node-plus-fill me-2"></i>Добавить шаг в <span id="stepRoadmapTitle"></span></h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <form id="addStepForm">
                            <input type="hidden" id="stepRoadmapId">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Порядковый номер шага</label>
                                <input type="number" class="form-control" name="stepOrder" min="1" value="1" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Заголовок задания</label>
                                <input type="text" class="form-control" name="title" required placeholder="Изучить Consumer Groups">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Ссылка на репозиторий проекта</label>
                                <input type="url" class="form-control" name="contentLink" required placeholder="https://github.com/...">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold text-success">Требуемое кол-во коммитов</label>
                                <input type="number" class="form-control" name="requiredCommits" min="1" value="3" required>
                            </div>
                            <button type="submit" class="btn btn-success w-100 fw-bold py-2" id="submitStepBtn">Сохранить шаг</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="reviewModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow">
                    <div class="modal-header bg-warning border-0">
                        <h5 class="modal-title fw-bold text-dark"><i class="bi bi-check2-square me-2"></i>Ревью шага <span id="reviewStepTitle"></span></h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <form id="reviewForm">
                            <input type="hidden" id="reviewStepId">
                            <input type="hidden" name="mentorId" value="${currentUserId}">

                            <div class="mb-3">
                                <label class="form-label fw-bold">Комментарий к коду</label>
                                <textarea class="form-control" name="comments" rows="4" required placeholder="Что сделано хорошо, а что нужно исправить?"></textarea>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">Вердикт</label>
                                <select class="form-select" name="isApproved" required>
                                    <option value="true">✅ Одобрить (Откроет следующий шаг)</option>
                                    <option value="false">❌ На доработку (Вернет шаг в работу)</option>
                                </select>
                            </div>

                            <button type="submit" class="btn btn-warning w-100 fw-bold py-2" id="submitReviewBtn">Отправить ревью</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </#if>

    <script>
        <#if isMentor>

        document.getElementById('createRoadmapForm')?.addEventListener('submit', async function(e) {
            e.preventDefault();
            const btn = document.getElementById('submitBtn');
            btn.disabled = true;

            const formData = new FormData(this);
            const data = Object.fromEntries(formData.entries());
            data.mentorId = parseInt(data.mentorId);
            data.menteeId = parseInt(data.menteeId);
            data.maxAfkDays = parseInt(data.maxAfkDays);

            try {
                const res = await fetch('/api/v1/roadmaps', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
                    body: JSON.stringify(data)
                });
                if (res.ok) window.location.reload();
                else window.showToast('Ошибка при создании', 'danger');
            } catch (error) { window.showToast('Сетевая ошибка', 'danger'); }
            finally { btn.disabled = false; }
        });

        window.openAddStepModal = function(roadmapId, roadmapTitle) {
            document.getElementById('stepRoadmapId').value = roadmapId;
            document.getElementById('stepRoadmapTitle').innerText = '"' + roadmapTitle + '"';
            new bootstrap.Modal(document.getElementById('addStepModal')).show();
        };

        document.getElementById('addStepForm')?.addEventListener('submit', async function(e) {
            e.preventDefault();
            const btn = document.getElementById('submitStepBtn');
            btn.disabled = true;

            const roadmapId = document.getElementById('stepRoadmapId').value;
            const data = Object.fromEntries(new FormData(this).entries());
            data.stepOrder = parseInt(data.stepOrder);
            data.requiredCommits = parseInt(data.requiredCommits);

            try {
                const res = await fetch('/api/v1/roadmaps/' + roadmapId + '/steps', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json', 'Accept': 'application/json'},
                    body: JSON.stringify(data)
                });
                if (res.ok) { window.showToast('Шаг добавлен!', 'success'); setTimeout(() => window.location.reload(), 1000); }
                else window.showToast('Ошибка', 'danger');
            } catch (error) { window.showToast('Сетевая ошибка', 'danger'); }
            finally { btn.disabled = false; }
        });

        window.openReviewModal = function(stepId, stepTitle) {
            document.getElementById('reviewStepId').value = stepId;
            document.getElementById('reviewStepTitle').innerText = '"' + stepTitle + '"';
            new bootstrap.Modal(document.getElementById('reviewModal')).show();
        };

        document.getElementById('reviewForm')?.addEventListener('submit', async function(e) {
            e.preventDefault();
            const btn = document.getElementById('submitReviewBtn');
            btn.disabled = true;

            const stepId = document.getElementById('reviewStepId').value;
            const formData = new FormData(this);
            const data = {
                mentorId: parseInt(formData.get('mentorId')),
                comments: formData.get('comments'),
                isApproved: formData.get('isApproved') === 'true'
            };

            try {
                const res = await fetch('/api/v1/steps/' + stepId + '/feedback', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });

                if (res.ok) {
                    window.showToast('Вердикт сохранен!', 'success');
                    setTimeout(() => window.location.reload(), 1000);
                } else {
                    window.showToast('Ошибка сохранения', 'danger');
                }
            } catch (error) {
                window.showToast('Сетевая ошибка', 'danger');
            } finally {
                btn.disabled = false;
            }
        });
        </#if>

        window.syncStep = async function(stepId) {
            try {
                const res = await fetch('/api/v1/steps/' + stepId + '/sync', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                });
                if (res.ok) {
                    window.showToast('Синхронизация завершена', 'success');
                    setTimeout(() => window.location.reload(), 1000);
                } else {
                    window.showToast('Ошибка при синхронизации', 'danger');
                }
            } catch (e) {
                window.showToast('Сетевая ошибка', 'danger');
            }
        };
    </script>
</@b.layout>