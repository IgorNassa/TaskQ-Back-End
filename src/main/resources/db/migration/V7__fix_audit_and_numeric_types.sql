-- Sem autenticação, o autor da operação ainda pode ser desconhecido.
ALTER TABLE tb_user ALTER COLUMN created_by DROP NOT NULL;
ALTER TABLE tb_task ALTER COLUMN created_by DROP NOT NULL;

-- IDs e saldo de XP são Long nas entidades Java.
ALTER TABLE tb_cargo ALTER COLUMN id TYPE BIGINT;
ALTER TABLE tb_user ALTER COLUMN id TYPE BIGINT;
ALTER TABLE tb_user ALTER COLUMN cargo_id TYPE BIGINT;
ALTER TABLE tb_user ALTER COLUMN xp TYPE BIGINT;
ALTER TABLE tb_user ALTER COLUMN created_by TYPE BIGINT;
ALTER TABLE tb_user ALTER COLUMN updated_by TYPE BIGINT;
ALTER TABLE tb_project ALTER COLUMN id TYPE BIGINT;
ALTER TABLE tb_project ALTER COLUMN owner_id TYPE BIGINT;
ALTER TABLE tb_task ALTER COLUMN id TYPE BIGINT;
ALTER TABLE tb_task ALTER COLUMN assignee_id TYPE BIGINT;
ALTER TABLE tb_task ALTER COLUMN creator_id TYPE BIGINT;
ALTER TABLE tb_task ALTER COLUMN project_id TYPE BIGINT;
ALTER TABLE tb_task ALTER COLUMN created_by TYPE BIGINT;
ALTER TABLE tb_task ALTER COLUMN updated_by TYPE BIGINT;
ALTER TABLE tb_xp_transaction ALTER COLUMN id TYPE BIGINT;
ALTER TABLE tb_xp_transaction ALTER COLUMN user_id TYPE BIGINT;
ALTER TABLE tb_xp_transaction ALTER COLUMN reference_id TYPE BIGINT;

-- Senhas de pessoas diferentes não precisam ser únicas.
ALTER TABLE tb_user DROP CONSTRAINT IF EXISTS usuario_usuariosenharest_key;

-- Apenas uma recompensa por tarefa, inclusive em requisições concorrentes.
CREATE UNIQUE INDEX uk_xp_task_reward
    ON tb_xp_transaction (reference_id)
    WHERE reference_type = 'TASK';
