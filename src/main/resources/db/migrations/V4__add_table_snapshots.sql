CREATE TABLE snapshots
(
    id            INTEGER PRIMARY KEY,

    backup_id     INTEGER NOT NULL,

    snapshot_path TEXT    NOT NULL,

    type          INTEGER NOT NULL,
    -- 0 = MANUAL
    -- 1 = SCHEDULED

    active        INTEGER NOT NULL DEFAULT 0,
    -- 0 = INACTIVE
    -- 1 = ACTIVE

    created_at    INTEGER NOT NULL,
    updated_at    INTEGER NOT NULL,

    UNIQUE (snapshot_path),
    CHECK (snapshot_path != ''),
    CHECK (type IN (0, 1)),
    CHECK (active IN (0, 1)),

    FOREIGN KEY (backup_id)
        REFERENCES backups (id)
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_snapshots_backup_active
    ON snapshots (backup_id, active)
    WHERE active = 1;

CREATE INDEX idx_snapshots_backup_created
    ON snapshots (backup_id, created_at);

CREATE INDEX idx_snapshots_backup_type_created
    ON snapshots (backup_id, type, created_at);