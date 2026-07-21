CREATE EXTENSION IF NOT EXISTS vector;

CREATE INDEX IF NOT EXISTS idx_biometria_embedding_hnsw
    ON BIOMETRIA_FACIAL
    USING hnsw (embedding vector_cosine_ops);