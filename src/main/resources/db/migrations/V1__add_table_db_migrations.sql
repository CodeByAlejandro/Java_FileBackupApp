CREATE TABLE db_migrations
(
    id             INTEGER PRIMARY KEY,

    migration_file TEXT    NOT NULL,
    applied_at     INTEGER NOT NULL,

    CHECK (migration_file != '')
);

CREATE UNIQUE INDEX idx_db_changes_change_file
    ON db_migrations (migration_file);