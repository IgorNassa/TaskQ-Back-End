-- O acesso aos dados ocorre pela API Spring, usando a conexão do backend.
-- Sem políticas públicas, a Data API do Supabase não expõe estes registros.
ALTER TABLE tb_cargo ENABLE ROW LEVEL SECURITY;
ALTER TABLE tb_user ENABLE ROW LEVEL SECURITY;
ALTER TABLE tb_project ENABLE ROW LEVEL SECURITY;
ALTER TABLE tb_task ENABLE ROW LEVEL SECURITY;
ALTER TABLE tb_xp_transaction ENABLE ROW LEVEL SECURITY;
