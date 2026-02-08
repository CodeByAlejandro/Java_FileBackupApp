CREATE TABLE db_migrations
(
    id             INTEGER PRIMARY KEY,

    migration_file TEXT NOT NULL,
    applied_at     TEXT NOT NULL DEFAULT (CURRENT_TIMESTAMP),

    UNIQUE (migration_file),
    CHECK (migration_file != '')
);

CREATE INDEX idx_db_migrations_applied_at_migration_file
    ON db_migrations (applied_at, migration_file);