CREATE TABLE snapshots
(
    id            INTEGER PRIMARY KEY,

    backup_id     INTEGER NOT NULL,

    snapshot_path TEXT    NOT NULL,

    origin        INTEGER NOT NULL,
    -- 0 = MANUAL
    -- 1 = SCHEDULED

    is_live       INTEGER NOT NULL DEFAULT 1,
    -- 0 = HISTORICAL
    -- 1 = LIVE

    created_at    INTEGER NOT NULL,
    updated_at    INTEGER NOT NULL,

    UNIQUE (snapshot_path),
    CHECK (snapshot_path != ''),
    CHECK (origin IN (0, 1)),
    CHECK (is_live IN (0, 1)),

    FOREIGN KEY (backup_id)
        REFERENCES backups (id)
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_snapshots_backup_active
    ON snapshots (backup_id, is_live)
    WHERE is_live = 1;

CREATE INDEX idx_snapshots_backup_created
    ON snapshots (backup_id, created_at);

CREATE INDEX idx_snapshots_backup_type_created
    ON snapshots (backup_id, origin, created_at);