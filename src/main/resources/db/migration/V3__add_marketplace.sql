-- 1. Таблица объявлений (Анкеты менторов и запросы учеников)
CREATE TABLE advertisements (
                                id BIGSERIAL PRIMARY KEY,
                                uuid UUID NOT NULL UNIQUE,
                                author_id BIGINT NOT NULL,
                                type VARCHAR(50) NOT NULL, -- SEEKING_MENTOR, SEEKING_MENTEE
                                title VARCHAR(255) NOT NULL,
                                content TEXT NOT NULL,
                                status VARCHAR(50) NOT NULL, -- ACTIVE, CLOSED
                                created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_adv_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 2. Таблица откликов на объявления
CREATE TABLE connection_requests (
                                     id BIGSERIAL PRIMARY KEY,
                                     uuid UUID NOT NULL UNIQUE,
                                     advertisement_id BIGINT NOT NULL,
                                     sender_id BIGINT NOT NULL,
                                     cover_letter TEXT,
                                     status VARCHAR(50) NOT NULL, -- PENDING, ACCEPTED, REJECTED
                                     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_conn_adv FOREIGN KEY (advertisement_id) REFERENCES advertisements(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_conn_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Таблица утвержденных связей
CREATE TABLE user_connections (
                                  mentor_id BIGINT NOT NULL,
                                  mentee_id BIGINT NOT NULL,
                                  created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (mentor_id, mentee_id),
                                  CONSTRAINT fk_uc_mentor FOREIGN KEY (mentor_id) REFERENCES users(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_uc_mentee FOREIGN KEY (mentee_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_adv_type_status ON advertisements(type, status);
CREATE INDEX idx_conn_adv_id ON connection_requests(advertisement_id);