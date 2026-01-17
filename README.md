# Java_FileBackupApp
File backup application to backup files from computer to external hard drive, synchronize changes, restore backups, validate backup integrity using data checksums and much more.

> [!IMPORTANT]
> This project is in early development.

# Features
- [ ] Backup mode (default):
  - [ ] Omission of disabled backups
  - [ ] Full backup strategy to copy all files (default)
  - [ ] Synchronization strategy to copy only changed files:
    - [ ] Detection of changed files based on last modified time, size and checksums
    - [ ] Ability to retain, delete or archive orphaned files (files missing from source)
    - [ ] Detection of moved files to increase performance (different location on backup)
  - [ ] Atomic-IO operations (no backup data is lost on system failure)
  - [ ] Storage of file-level checksums in SQLite database
  - [ ] Storage of last status in SQLite database (proceed after failure)
  - [ ] Destination file compression
  - [ ] Backup schedules
    - [ ] Backup service running in background
    - [ ] Pre-defined schedules
    - [ ] Cron expressions (For more flexibility)
    - [ ] Pause scheduler on manual backup operation
    - [ ] Prevention of simultaneous backups for same directories
    - [ ] Handling of missed schedules due to system shutdown
- [ ] Restore mode:
  - [ ] Omission of unselected backups
  - [ ] Full restore strategy to copy all files (default)
  - [ ] Synchronization strategy to restore only changed files:
    - [ ] Detection of changed files based on last modified time, size and checksums
    - [ ] Ability to retain, delete or archive orphaned files (files missing from backup)
    - [ ] Detection of moved files to increase performance (different location on source)
  - [ ] Atomic-IO operations (no source data is lost on system failure)
  - [ ] Storage of last status in SQLite database (proceed after failure)
  - [ ] Corrupt backup file strategy:
    - [ ] Ability to restore anyway, ask before restoring or never restore
- [ ] Validation mode:
  - [ ] Omission of unselected backups
  - [ ] Full backup integrity validation using checksums stored in SQLite database
  - [ ] File list backup integrity validation using checksums stored in SQLite database
- [ ] Crash recovery:
  - [ ] Automatic crash detection based on state in SQLite database
  - [ ] Automatic resumption based on state in SQLite database
- [ ] File-level logging into separate logfiles per operation
- [ ] Auto back-up SQLite database to database back-up path (on changes)
- [ ] Configuration file to control all application features
- [ ] Start via command line arguments
- [ ] Command line user interface (LUI)
- [ ] Graphical user interface (GUI)

# Configuration
YAML application configuration file with the following structure:
- Backups (source ↔ backup mappings):
  - Source location
  - Backup location
  - Backup mode:
    - Activation of backup: Enabled | Disabled
    - Backup strategy: Full | Synchronization
    - Orphan file protocol: Retain | Delete | Archive (only for synchronization)
    - Use file size for change detection: Enabled | Disabled
    - Use checksums for change detection: Enabled | Disabled
    - Destination file compression: Enabled | Disabled
    - Compression file size lower bound (only for compression)
    - Compression file size upper bound (only for compression)
    - Backup schedules:
      - Pre-defined schedule selection: Daily | Weekly | Monthly | Custom
      - Time of day (only for non-custom schedules)
      - Day of week (only for weekly schedule)
      - Day of month: First | Last | Other (only for monthly schedule)
      - Other day of month (only for other selection in monthly schedule)
      - Custom cron expressions (only for custom schedule)
  - Restore mode:
    - Selection for restore mode: Active | Inactive
    - Restore strategy: Full | Synchronization
    - Orphan file protocol: Retain | Delete | Archive (only for synchronization)
    - Use file size for change detection: Enabled | Disabled
    - Use checksums for change detection: Enabled | Disabled
    - Corrupt backup file strategy: Always restore | Always ask | Never restore
  - Validation mode: 
    - Selection for validation mode: Active | Inactive
- Archival directory
- SQLite database file
- Back-up SQLite database file (used for auto back-up on changes)

# Command line arguments
The following positional command line arguments are accepted:

| Position | Name                           | Value                                    | Remarks                           |
| -------- |--------------------------------|------------------------------------------|-----------------------------------|
| 1        | Mode                           | BACKUP \| RESTORE \| VALIDATE \| SERVICE | SERVICE starts background service |
| 2        | Integrity validation file list | YAML file with list of files             | Only when mode is set to VALIDATE |


