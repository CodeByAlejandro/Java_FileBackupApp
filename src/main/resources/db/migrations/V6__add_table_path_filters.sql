CREATE TABLE path_filters
(
    id          INTEGER PRIMARY KEY,

    backup_id   INTEGER NOT NULL,

    path_filter TEXT    NOT NULL,
    -- Relative to backups.source_path
    -- Supports *, **, ?, trailing /

    filter_type INTEGER NOT NULL,
    -- 0 = EXCLUDE
    -- 1 = INCLUDE

    order_index INTEGER NOT NULL,

    created_at  TEXT    NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    updated_at  TEXT    NOT NULL,

    UNIQUE (backup_id, path_filter),
    CHECK (path_filter != ''),
    CHECK (filter_type IN (0, 1)),
    CHECK (order_index >= 0),

    FOREIGN KEY (backup_id)
        REFERENCES backups (id)
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_path_filters_backup_order
    ON path_filters (backup_id, order_index);