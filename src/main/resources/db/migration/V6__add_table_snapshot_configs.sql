CREATE TABLE snapshot_configs
(
    id                             INTEGER PRIMARY KEY,

    backup_id                      INTEGER NOT NULL,

    tracking_policy                INTEGER NOT NULL DEFAULT 0,
    -- 0 = ALL_FILES
    -- 1 = DELETED_FILES_ONLY

    hardlink_deduplication         INTEGER NOT NULL DEFAULT 1,
    -- 0 = DISABLED
    -- 1 = ENABLED

    snapshot_amount_limit          INTEGER,
    snapshot_age_limit             INTEGER,
    snapshot_cumulative_size_limit INTEGER,

    created_at                     TEXT    NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    updated_at                     TEXT    NOT NULL,

    CHECK (tracking_policy IN (0, 1)),
    CHECK (hardlink_deduplication IN (0, 1)),
    CHECK (snapshot_amount_limit IS NULL OR snapshot_amount_limit >= 1),
    CHECK (snapshot_age_limit IS NULL OR snapshot_age_limit >= 0),
    CHECK (snapshot_cumulative_size_limit IS NULL OR snapshot_cumulative_size_limit >= 0),

    FOREIGN KEY (backup_id)
        REFERENCES backups (id)
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_snapshot_configs_backup
    ON snapshot_configs (backup_id);