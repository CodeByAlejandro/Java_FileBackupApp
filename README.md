# BacMan

File backup manager application to back up files from computer to external hard drive, synchronize changes, restore backups,
validate backup integrity using data checksums and much more.

> [!IMPORTANT]
> This project is in early development.

# Features roadmap

- [ ] Graphical user interface (GUI)
- [ ] Command line user interface (LUI)
- [ ] Define new backups by mapping a source path to a backup path
- [ ] Manually create new snapshot for defined backup(s)
- [ ] Schedule automatic backups for defined backup(s)
    - [ ] Pre-defined schedules (daily, weekly, monthly)
    - [ ] Custom schedules using cron expressions
    - [ ] Notify for missed schedules due to system shutdown
    - [ ] Enable/disable backup schedules
- [ ] Manually restore snapshot for a defined backup
- [ ] Manually validate data integrity of snapshot(s) for a defined backup
- [ ] Background service to scrub snapshots for data integrity using stored checksums
- [ ] Enable/disable change detection based on file size and/or checksum
- [ ] Automatic detection of moved/renamed files
- [ ] Atomic-IO operations (prevent data loss on system failure)
- [ ] Exclude/include files based on user-defined wildcard patterns
  for directories)
- [ ] Allow configuration of tracking policy for snapshots
    - [ ] Track all files
    - [ ] Track only deleted files
- [ ] Enable/disable deduplication by leveraging hard links
- [ ] Allow configuration of snapshot retention policy
    - [ ] Maximum number of snapshots to keep
    - [ ] Maximum age of snapshots to keep
    - [ ] Maximum cumulative size of snapshots to keep
- [ ] Logging system with configurable log levels and log rotation
- [ ] Automatic crash detection and resumption
- [ ] Automatic backup of database file
- [ ] File compression
    - [ ] Configurable compression algorithm and level
    - [ ] Configurable file size lower boundary
    - [ ] Configurable file size upper boundary

# Implementation details

- Store internal data in embedded database (SQLite)

- Support `*`, `**`, `?` wildcards and trailing `/` to denote directories in exclude/include patterns
