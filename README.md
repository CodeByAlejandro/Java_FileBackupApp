# Java_FileBackupApp
File backup application to backup files from computer to external hard drive, synchronize changes, validate backup integrity using data checksums and much more.

# Features
- [ ] Configuration file to provide input
- [ ] Skip disabled mappings
- [ ] Full backup mode (default)
- [ ] Synchronization mode (only copy changed files):
    - [ ] Detection of changed files based on last modified time, size and checksums
    - [ ] Detection of moved files to increase performance (different location on destination)
- [ ] Deletion of destination files missing from source
- [ ] Archival of destination files missing from source into separate recycle bin directory
- [ ] Integrity validation mode:
    - [ ] Storage of file-level checksums on disk
    - [ ] Full backup integrity validation using checksums stored on disk
    - [ ] Single file backup integrity validation using checksums stored on disk
- [ ] Restore mode (full or synchronization)
- [ ] Atomic-IO operations (so that no backup data is lost on system failure)
- [ ] Crash recovery:
    - [ ] Storage of last mapping and last file processed in lock file
    - [ ] Automatic crash detection based on lock file
    - [ ] Automatic resumption based on lock file
- [ ] Copy validation using size and checksums
- [ ] File-level logging into separate logfiles per operation
- [ ] Scheduling backups
- [ ] Graphical user interface (GUI)

# Configuration
- Configuration options:
    - source <-> destination mappings:
        - Source location
        - Destination location
        - Activation (allow to disable temporarily)
        - Full backup mode or synchronization mode
        - Deletion or archival of destination files missing from source
        - Integrity validation mode (check files instead of backing up files)
        - Restore mode
        - Backup schedule
    - List of individual files to validate integrity for
    - Logfile
    - Checksum file
    - Lock file
    - Recycle bin directory
    - Global backup schedule
