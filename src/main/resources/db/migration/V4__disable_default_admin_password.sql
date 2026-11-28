-- Disable only the bootstrap account that still uses the original seed password.
UPDATE tb_user
SET status = 'INATIVO'
WHERE email = 'admin@taskq.com'
  AND password_hash = '$2a$10$lMq1/3CKYW4zcqaLUN8B9eY/RydOlT/Josy6ff.9PR3IeFmpnOaLa';
