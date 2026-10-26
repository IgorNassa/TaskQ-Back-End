CREATE TABLE IF NOT EXISTS tb_cargo (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tb_user (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    avatar_url VARCHAR(500),
    cargo_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    xp BIGINT NOT NULL DEFAULT 0,
    elo VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_user_cargo
    FOREIGN KEY (cargo_id) REFERENCES tb_cargo(id)
);

CREATE TABLE IF NOT EXISTS tb_project (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(100) NOT NULL,
    priority VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    dead_line DATE NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_project_owner
    FOREIGN KEY (owner_id) REFERENCES tb_user(id)
);

CREATE TABLE IF NOT EXISTS tb_task (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(250) NOT NULL,
    assignee_id BIGINT NOT NULL,
    creator_id BIGINT NOT NULL,
    dead_line DATE NOT NULL,
    completed_at TIMESTAMP,
    project_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_task_assignee
    FOREIGN KEY (assignee_id) REFERENCES tb_user(id),
    CONSTRAINT fk_task_creator
    FOREIGN KEY (creator_id) REFERENCES tb_user(id),
    CONSTRAINT fk_task_project
    FOREIGN KEY (project_id) REFERENCES tb_project(id)
);

CREATE TABLE IF NOT EXISTS tb_xp_transaction (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount INTEGER NOT NULL,
    reason VARCHAR(250) NOT NULL,
    reference_type VARCHAR(100),
    reference_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_xp_transaction_user
    FOREIGN KEY (user_id) REFERENCES tb_user(id)
);