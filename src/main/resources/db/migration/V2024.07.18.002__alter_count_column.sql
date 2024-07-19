ALTER TABLE favourites_advertisements
DROP COLUMN favourite_count;

ALTER TABLE advertisements
    ADD COLUMN favourite_count INT NOT NULL DEFAULT 0;