CREATE TABLE backups
(
    id                         INTEGER PRIMARY KEY,

    source_path                TEXT    NOT NULL,
    backup_path                TEXT    NOT NULL,

    file_size_change_detection INTEGER NOT NULL DEFAULT 1,
    -- 0 = DISABLED
    -- 1 = ENABLED

    checksum_change_detection  INTEGER NOT NULL DEFAULT 0,
    -- 0 = DISABLED
    -- 1 = ENABLED

    scrub_integrity_checks     INTEGER NOT NULL DEFAULT 0,
    -- 0 = DISABLED
    -- 1 = ENABLED

    created_at                 INTEGER NOT NULL,
    updated_at                 INTEGER NOT NULL,

    UNIQUE (backup_path),
    CHECK (source_path != ''),
    CHECK (backup_path != ''),
    CHECK (file_size_change_detection IN (0, 1)),
    CHECK (checksum_change_detection IN (0, 1)),
    CHECK (scrub_integrity_checks IN (0, 1))
);