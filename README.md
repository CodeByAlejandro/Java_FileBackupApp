# Java_FileBackupApp
File backup application to backup files from computer to external hard drive, synchronize changes, validate backup integrity using data checksums and much more.

# Features
- [ ] Configuration file
- [ ] Input via command line arguments
- [ ] Backup mode (default):
  - [ ] Omission of disabled backups
  - [ ] Full backup strategy to copy all files (default)
  - [ ] Synchronization strategy to copy only changed files:
    - [ ] Detection of changed files based on last modified time, size and checksums
    - [ ] Detection of moved files to increase performance (different location on destination)
  - [ ] Copy validation using size and checksums
  - [ ] Atomic-IO operations (no backup data is lost on system failure)
  - [ ] Storage of file-level checksums on disk
  - [ ] Storage of last backup and last file processed in lock file
  - [ ] Deletion of destination files missing from source
  - [ ] Archival of destination files missing from source to archival directory
  - [ ] Backup schedules
- [ ] Restore mode:
  - [ ] Omission of unselected backups
  - [ ] Full backup strategy to copy all files (default)
  - [ ] Synchronization strategy to copy only changed files:
    - [ ] Detection of changed files based on last modified time, size and checksums
    - [ ] Detection of moved files to increase performance (different location on destination)
  - [ ] Copy validation using size and checksums
  - [ ] Atomic-IO operations (no backup data is lost on system failure)
  - [ ] Storage of last backup and last file processed in lock file
- [ ] Integrity validation mode:
  - [ ] Omission of unselected backups
  - [ ] Full backup integrity validation using checksums stored on disk
  - [ ] File list backup integrity validation using checksums stored on disk
- [ ] Crash recovery:
  - [ ] Automatic crash detection based on lock file
  - [ ] Automatic resumption based on lock file
- [ ] File-level logging into separate logfiles per operation
- [ ] Command line user interface (LUI)
- [ ] Graphical user interface (GUI)

# Configuration
YAML application configuration file with the following options:
- Backups (source <-> destination mappings):
  - Source location
  - Destination location
  - Backup mode:
    - Activation of backup
    - Backup strategy: Full backup or synchronization
    - Destination files missing from source: deletion or archival
    - Backup schedule
  - Restore mode:
    - Selection for restore mode
    - Restore strategy: Full backup or synchronization
  - Integrity validation mode: 
    - Selection for integrity validation mode
- Archival directory
- Checksum file
- Lock file
- Logfile

# Command line arguments
The following positional command line arguments are accepted:

| Position | Name                           | Value                         | Remarks                           |
| -------- |--------------------------------|-------------------------------|-----------------------------------|
| 1        | Mode                           | BACKUP \| RESTORE \| VALIDATE |                                   |
| 2        | Integrity validation file list | YAML file with list of files  | Only when mode is set to VALIDATE |
