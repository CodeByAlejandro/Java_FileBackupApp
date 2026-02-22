CREATE TABLE global_config
(
    id                   INTEGER PRIMARY KEY,

    log_directory_path   TEXT NOT NULL,
    database_backup_path TEXT,

    created_at           TEXT NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    updated_at           TEXT NOT NULL,

    CHECK (id = 1),
    CHECK (log_directory_path != ''),
    CHECK (database_backup_path IS NULL OR database_backup_path != '')
);