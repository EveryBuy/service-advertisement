ALTER TABLE advertisements
ADD COLUMN top_level_subcategory_id BIGINT NOT NULL,
ADD COLUMN low_level_subcategory_id BIGINT NULL;

ALTER TABLE advertisements
DROP COLUMN subcategory_id;

ALTER TABLE advertisements
ADD CONSTRAINT fk_advertisement_top_level_subcategory
    FOREIGN KEY (top_level_subcategory_id)
    REFERENCES top_level_subcategories(id)
    ON DELETE CASCADE;

ALTER TABLE advertisements
ADD CONSTRAINT fk_advertisement_low_level_subcategory
    FOREIGN KEY (low_level_subcategory_id)
    REFERENCES low_level_subcategories(id)
    ON DELETE SET NULL;
