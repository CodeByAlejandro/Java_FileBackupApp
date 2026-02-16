CREATE TABLE files
(
    id               INTEGER PRIMARY KEY,

    snapshot_id      INTEGER NOT NULL,

    -- Relative to snapshots.snapshot_path
    relative_path    TEXT    NOT NULL,

    mtime            TEXT    NOT NULL,
    size             INTEGER NOT NULL,
    checksum         BLOB    NOT NULL,

    state            INTEGER NOT NULL,
    -- 0 = CREATED
    -- 1 = STALE
    -- 2 = MODIFIED
    -- 3 = MOVED
    -- 4 = REMOVED

    -- Points to record with state = MOVED (the old location)
    moved_from_id    INTEGER,

    reliability      INTEGER NOT NULL DEFAULT 1,
    -- 1 = RELIABLE
    -- 2 = UNRELIABLE

    last_verified_at INTEGER,

    created_at       TEXT    NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    updated_at       TEXT    NOT NULL,

    CHECK (size >= 0),
    CHECK (state IN (0, 1, 2, 3, 4)),
    CHECK (reliability IN (1, 2)),

    FOREIGN KEY (snapshot_id)
        REFERENCES snapshots (id)
        ON DELETE CASCADE,

    FOREIGN KEY (moved_from_id)
        REFERENCES files (id)
        ON DELETE SET NULL -- Prevent cascading delete errors due to self-referencing
);

CREATE UNIQUE INDEX idx_files_snapshot_path
    ON files (snapshot_id, relative_path);

CREATE INDEX idx_files_snapshot_state
    ON files (snapshot_id, state);

CREATE INDEX idx_files_moved_from
    ON files (moved_from_id);

CREATE INDEX idx_files_checksum
    ON files (checksum);

CREATE INDEX idx_files_scrub
    ON files (reliability, last_verified_at);

CREATE INDEX idx_files_unreliable
    ON files (snapshot_id)
    WHERE reliability = 2;