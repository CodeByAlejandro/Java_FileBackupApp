SELECT f.*
FROM files f
         JOIN snapshots s ON s.id = f.snapshot_id
         JOIN backups b ON b.id = s.backup_id
WHERE f.reliability = 1
  AND (f.last_verified_at IS NULL OR f.last_verified_at < ?1)
  AND b.scrub_integrity_checks = 1
ORDER BY s.created_at DESC
LIMIT ?2;