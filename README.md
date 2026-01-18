# Java_FileBackupApp
File backup application to back up files from computer to external hard drive, synchronize changes, restore backups,
validate backup integrity using data checksums and much more.

> [!IMPORTANT]
> This project is in design stage. The README file is a draft and may change significantly in future versions.

# Vision
Application to create transparent file backups of important files in new locations (e.g. external hard drives)
to prevent data loss in case of hardware failure, accidental deletion or file corruption.
The application should be easy to use, reliable and support self-healing of corrupted or tampered files.

# Features
- [ ] Specification of multiple backup rules
- [ ] Support backup rule inheritance: more specific rules override overlapping less specific ones, but inherit unspecified options.
- [ ] Create backups based on selection of backup rules
- [ ] Restore backup based on selection of backup rule
- [ ] Validate data integrity of backups based on selection of backup rules
- [ ] Validate data integrity of files listed in a provided YAML file
- [ ] Skip processing of disabled backup rules
- [ ] Detection of changed files based on last modified time and file size
- [ ] Fallback detection of changed files based on checksums (in case last modified time and file size are unchanged)
  - [ ] Policy for how to handle deviating checksums:
    - [ ] Log only (default)
    - [ ] Handle as legitimate change and back up anyway
    - [ ] Handle as corruption and self-heal using backup copy
  - [ ] Manual conflict resolution for deviating checksum conflicts not solved by policy (choose to do nothing (default) / back up anyway / self-heal using backup)
- [ ] Detection of moved files to increase performance
- [ ] Backup file-level tampering detection (using last modified time and file size)
  - [ ] Restore policy for tampered backup files:
    - [ ] Log only (default)
    - [ ] Restore tampered backup files if their corresponding source file is missing
    - [ ] Self-heal tampered backup files using their corresponding source file
  - [ ] Manual conflict resolution for tampered backup files not solved by policy (choose to do nothing (default) / Restore if source is missing / self-heal using source / overwrite source)
- [ ] Backup file-level corruption detection (using checksums)
  - [ ] Restore policy for corrupted backup files:
    - [ ] Log only (default)
    - [ ] Restore corrupted backup files if their corresponding source file is missing
    - [ ] Self-heal corrupted backup files using their corresponding source file
  - [ ] Manual conflict resolution for corrupted backup files not solved by policy (choose to do nothing (default) / Restore if source is missing / self-heal using source / overwrite source)
- [ ] Backup file-level compression
  - [ ] Compression file size boundaries
- [ ] Version control for backups
  - [ ] Use hard links where possible to save disk space
  - [ ] Tracking policy for version control (track all files or only deleted ones)
  - [ ] Cleanup policy for version control:
    - [ ] Maximum number of versions to keep
    - [ ] Maximum age of versions to keep
    - [ ] Maximum file size of versions to keep
    - [ ] Manual cleanup of old versions not deleted by policy
- [ ] Backup scheduling via background service
  - [ ] Pre-defined schedules (daily, weekly, monthly)
  - [ ] Custom schedules using cron expressions
  - [ ] Pause scheduler on manual backup operation
  - [ ] Handle simultaneous backups for same directories (directory creation conflicts)
  - [ ] Notify for missed schedules due to system shutdown
- [ ] Storage of last modified time, file size and checksum from moment of backup in SQLite database (for change, tampering and corruption detection)
- [ ] Atomic-IO operations (use temp files for writes to prevent data loss on system failure)
- [ ] Crash recovery:
  - [ ] Automatic crash detection based on state in SQLite database
  - [ ] Automatic resumption based on state in SQLite database
- [ ] File-level logging into separate logfiles per operation
- [ ] Auto back-up SQLite database to database back-up path (on changes)
- [ ] Control program by means of a configuration file
- [ ] Command line user interface (LUI)
- [ ] Graphical user interface (GUI)

# Validation
Validation consists of the following steps:
1. Deviating checksum detection for source files
2. Restore missing back-up files using their corresponding source file (if not in conflict with step 1)
3. Backup file-level tampering detection
4. Backup file-level corruption detection
5. Manual conflict resolution

# Configuration
YAML application configuration file with the following structure:
- Log directory: `<Path to log directory>`
- Database file: `<Path to database file>`
- Database backup file (used for auto backup): `<Path to database backup file>`
- Backup rules (list):
  - ID (required): `1-2147483647`
  - Name: `<Backup name>`
  - Source path (required): `<Path to source directory or file>`
  - Backup path (required): `<Path to backup directory or file>`
  - Status: `Enabled` (default) | `Disabled`
  - Change detection:
    - Use file size: `Enabled` (default) | `Disabled`
    - Use checksums (only as fallback): `Enabled` (default) | `Disabled`
    - Deviating checksums policy: `Log only` (default) | `Backup anyway` | `Self heal with backup`
    - Track moved files: `Enabled` (default) | `Disabled`
  - Backup tampering detection (using last modified time and file size):
    - Status: `Enabled` (default) | `Disabled`
    - Restore policy: `Log only` (default) | `Restore if source is missing` | `Self heal with source`
  - Backup corruption detection (using checksums):
    - Status: `Enabled` (default) | `Disabled`
    - Restore policy: `Log only` (default) | `Restore if source is missing` | `Self heal with source`
  - File compression:
    - Status: `Enabled` | `Disabled` (default)
    - Compression file size lower bound: `500 MiB` (default) | `<Kibibytes> KiB` | `<Mebibytes> MiB` | `<Gibibytes> GiB` | `<Tebibytes> TiB`
    - Compression file size upper bound: `10 GiB` (default) | `<Kibibytes> KiB` | `<Mebibytes> MiB` | `<Gibibytes> GiB` | `<Tebibytes> TiB`
  - Version control:
    - Status: `Enabled` | `Disabled` (default)
    - Use hard links where possible: `Enabled` (default) | `Disabled`
    - Tracking policy: `All files` (default) | `Deleted files only`
    - Cleanup policy:
      - Maximum number of versions to keep: `180` (default)
      - Maximum age of versions to keep (in days): `180` (default)
      - Maximum file size of versions to keep: `Filesystem size` (default) | `<Gibibytes> GiB` | `<Tebibytes> TiB`
  - Schedules (list):
    - Status: `Enabled` (default) | `Disabled`
    - Schedule: `Daily` | `Weekly` | `Monthly` | `Custom`
    - Day of month (required for monthly schedule): `First` | `Last` | `1-31`
    - Day of week (required for weekly schedule): `Monday` | `Tuesday` | `Wednesday` | `Thursday` | `Friday` | `Saturday` | `Sunday` | `1-7` (1 = Monday)
    - Time of day (required for non-custom schedule): `Midnight` | `Noon` | `HH:MM` (24-hour format) | `HH:MM AM/PM` (12-hour format)
    - Custom cron expression (required for custom schedule): `<Cron expression string>`

# Command line interface
The following commands are supported:
  - `backup [--ids <list>]`: Create backups as per the backup rules in the configuration file.
  - `restore [--backup-version <version>] <id>`: Restore backup for one of the backup rules in the configuration file.
  - `validate [--ids <list>]`: Validate the integrity of backups as per the backup rules in the configuration file.
  - `validate-list <path>`: Validate the integrity of files listed in a provided YAML file.
  - `set`: Set or update configuration options.
  - `service`: Manage the background service to handle scheduled backups.
  - `help [--command <command>]`: Display help information about command line arguments.

The `set` command supports the following sub-commands:
  - `config <path>`: Set the path to the configuration file.
  - `log-dir <path>`: Set the path to the log directory.
  - `db <path>`: Set the path to the database file.
  - `db-backup <path>`: Set the path to the database backup file.

The `service` command supports the following sub-commands:
  - `start`: Start the background service to handle scheduled backups.
  - `stop`: Stop the background service.
  - `restart`: Restart the background service.
  - `status`: Display the status of the background service.
  - `enable`: Enable the background service to start on system boot.
  - `disable`: Disable the background service from starting on system boot.

## Backup command arguments
The `backup [--ids <list>]` command supports the following arguments:
  - `--ids <list>`: Comma-separated list of backup rule IDs to create backups for. Ranges are denoted with dashes (e.g. `1,3-5,8,10-12`). If omitted, all backup rules are processed.

## Restore command arguments
The `restore [--backup-version <version>] <id>` command supports the following arguments:
  - `--backup-version <version>`: Specify the backup version to restore. If omitted, the latest version is restored.
  - `<id>`: Backup rule ID to restore backup for.

## Validate command arguments
The `validate [--ids <list>]` command supports the following arguments:
  - `--ids <list>`: Comma-separated list of backup rule IDs to validate the integrity for. Ranges are denoted with dashes (e.g. `1,3-5,8,10-12`). If omitted, all backup rules are processed.

## Validate-list command arguments
The `validate-list <path>` command supports the following arguments:
  - `<path>`: Path to the YAML file containing the list of files to validate. The list must contain absolute file paths to either source or backup files.

## Help command arguments
The `help [--command <command>]` command supports the following arguments:

  - `--command <command>`: Display help information about a specific command. If omitted, general help information is displayed.
