ALTER TABLE advertisements ALTER COLUMN price TYPE BIGINT;
UPDATE advertisements SET price = ROUND(price * 100);
