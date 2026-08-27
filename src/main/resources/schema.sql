CREATE EXTENSION IF NOT EXISTS vector;

CREATE INDEX IF NOT EXISTS idx_biometria_embedding_hnsw
    ON BIOMETRIA_FACIAL
    USING hnsw (embedding vector_cosine_ops);

CREATE TABLE IF NOT EXISTS logs_auditoria_chaves (
    id BIGSERIAL PRIMARY KEY,
    data_hora TIMESTAMPTZ NOT NULL,
    acao VARCHAR(60) NOT NULL,
    modulo VARCHAR(60) NOT NULL,
    entidade VARCHAR(120) NOT NULL,
    entidade_id BIGINT,
    descricao VARCHAR(2000) NOT NULL,
    usuario_responsavel_id BIGINT,
    usuario_responsavel_nome VARCHAR(255) NOT NULL,
    filial BIGINT,
    bloco_id BIGINT,
    armario_id BIGINT,
    chave_id BIGINT,
    valor_anterior JSONB,
    valor_novo JSONB,
    quantidade_anterior INTEGER,
    quantidade_nova INTEGER,
    endereco_ip VARCHAR(80),
    user_agent VARCHAR(1000)
);

COMMENT ON TABLE logs_auditoria_chaves IS 'Auditoria histórica das operações do módulo de controle de chaves.';
COMMENT ON COLUMN logs_auditoria_chaves.valor_anterior IS 'Snapshot sanitizado anterior à alteração, sem senha, token, biometria ou imagens completas.';
COMMENT ON COLUMN logs_auditoria_chaves.valor_novo IS 'Snapshot sanitizado posterior à alteração, sem senha, token, biometria ou imagens completas.';
COMMENT ON COLUMN logs_auditoria_chaves.usuario_responsavel_id IS 'ID histórico do usuário autenticado responsável; sem FK para preservar auditoria após exclusões.';

CREATE INDEX IF NOT EXISTS idx_logs_auditoria_chaves_data_hora
    ON logs_auditoria_chaves (data_hora DESC);

CREATE INDEX IF NOT EXISTS idx_logs_auditoria_chaves_acao
    ON logs_auditoria_chaves (acao);

CREATE INDEX IF NOT EXISTS idx_logs_auditoria_chaves_entidade
    ON logs_auditoria_chaves (entidade, entidade_id);

CREATE INDEX IF NOT EXISTS idx_logs_auditoria_chaves_usuario
    ON logs_auditoria_chaves (usuario_responsavel_id);

CREATE INDEX IF NOT EXISTS idx_logs_auditoria_chaves_filial
    ON logs_auditoria_chaves (filial);

CREATE INDEX IF NOT EXISTS idx_logs_auditoria_chaves_bloco
    ON logs_auditoria_chaves (bloco_id);

CREATE INDEX IF NOT EXISTS idx_logs_auditoria_chaves_armario
    ON logs_auditoria_chaves (armario_id);

CREATE INDEX IF NOT EXISTS idx_logs_auditoria_chaves_chave
    ON logs_auditoria_chaves (chave_id);
