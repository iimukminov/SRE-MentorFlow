-- 1. Таблица Ролей
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

-- 2. Таблица Пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       github_username VARCHAR(255) UNIQUE
);

-- 3. Связующая таблица для Многие-ко-Многим (User <-> Role)
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 4. Таблица Дорожных карт (Roadmaps)
CREATE TABLE roadmaps (
                          id BIGSERIAL PRIMARY KEY,
                          uuid UUID NOT NULL UNIQUE,
                          title VARCHAR(255) NOT NULL,
                          description TEXT,
                          created_at TIMESTAMP WITHOUT TIME ZONE,
                          mentor_id BIGINT NOT NULL,
                          mentee_id BIGINT NOT NULL,
                          CONSTRAINT fk_roadmaps_mentor FOREIGN KEY (mentor_id) REFERENCES users(id),
                          CONSTRAINT fk_roadmaps_mentee FOREIGN KEY (mentee_id) REFERENCES users(id)
);

-- 5. Таблица Шагов дорожной карты (Roadmap Steps)
CREATE TABLE roadmap_steps (
                               id BIGSERIAL PRIMARY KEY,
                               uuid UUID NOT NULL UNIQUE,
                               step_order INTEGER NOT NULL,
                               title VARCHAR(255) NOT NULL,
                               content_link VARCHAR(255),
                               required_commits INTEGER,
                               status VARCHAR(50) NOT NULL,
                               roadmap_id BIGINT NOT NULL,
                               started_at TIMESTAMP WITHOUT TIME ZONE,
                               CONSTRAINT fk_roadmap_steps_roadmap FOREIGN KEY (roadmap_id) REFERENCES roadmaps(id) ON DELETE CASCADE
);

-- 6. Таблица Отзывов Ментора (Review Feedbacks)
CREATE TABLE review_feedbacks (
                                  id BIGSERIAL PRIMARY KEY,
                                  uuid UUID NOT NULL UNIQUE,
                                  comments TEXT,
                                  is_approved BOOLEAN NOT NULL,
                                  created_at TIMESTAMP WITHOUT TIME ZONE,
                                  roadmap_step_id BIGINT NOT NULL UNIQUE,
                                  mentor_id BIGINT NOT NULL,
                                  CONSTRAINT fk_review_feedbacks_step FOREIGN KEY (roadmap_step_id) REFERENCES roadmap_steps(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_review_feedbacks_mentor FOREIGN KEY (mentor_id) REFERENCES users(id)
);

-- 7. Таблица Консультаций (Consultations)
CREATE TABLE consultations (
                               id BIGSERIAL PRIMARY KEY,
                               uuid UUID NOT NULL UNIQUE,
                               scheduled_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                               meet_link VARCHAR(255),
                               status VARCHAR(50) NOT NULL,
                               mentor_id BIGINT NOT NULL,
                               mentee_id BIGINT NOT NULL,
                               CONSTRAINT fk_consultations_mentor FOREIGN KEY (mentor_id) REFERENCES users(id),
                               CONSTRAINT fk_consultations_mentee FOREIGN KEY (mentee_id) REFERENCES users(id)
);

CREATE INDEX idx_roadmaps_mentor_id ON roadmaps(mentor_id);
CREATE INDEX idx_roadmaps_mentee_id ON roadmaps(mentee_id);

CREATE INDEX idx_roadmap_steps_roadmap_id ON roadmap_steps(roadmap_id);
CREATE INDEX idx_roadmap_steps_status ON roadmap_steps(status);

CREATE INDEX idx_review_feedbacks_mentor_id ON review_feedbacks(mentor_id);

CREATE INDEX idx_consultations_mentor_id ON consultations(mentor_id);
CREATE INDEX idx_consultations_mentee_id ON consultations(mentee_id);

INSERT INTO roles (name) VALUES ('ROLE_MENTEE') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_MENTOR') ON CONFLICT DO NOTHING;