ALTER TABLE advertisements
    ADD COLUMN is_negotiable BOOLEAN;

UPDATE advertisements
SET is_negotiable = false
WHERE is_negotiable IS NULL;

ALTER TABLE advertisements
    ALTER COLUMN is_negotiable SET NOT NULL;