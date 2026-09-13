CREATE TABLE tb_task (
   id BIGSERIAL PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   description TEXT NOT NULL,
   status VARCHAR(50) NOT NULL,
   priority VARCHAR(50) NOT NULL,
   assignee_id BIGINT NOT NULL,
   creator_id BIGINT NOT NULL,
   dead_line DATE NOT NULL,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   completed_at TIMESTAMP
);