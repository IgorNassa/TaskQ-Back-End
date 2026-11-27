INSERT INTO tb_cargo (name)
SELECT 'Administrador'
WHERE NOT EXISTS (SELECT 1 FROM tb_cargo WHERE name = 'Administrador');

INSERT INTO tb_user (name, email, password_hash, cargo_id, status, xp, elo, perfil)
SELECT 'Administrador', 'admin@taskq.com', '$2a$10$lMq1/3CKYW4zcqaLUN8B9eY/RydOlT/Josy6ff.9PR3IeFmpnOaLa', c.id, 'ATIVO', 0, 'INICIANTE', 'ADMIN'
FROM tb_cargo c
WHERE c.name = 'Administrador'
  AND NOT EXISTS (SELECT 1 FROM tb_user WHERE email = 'admin@taskq.com');
