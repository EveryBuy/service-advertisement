ALTER TABLE subcategories ADD COLUMN parent_id INTEGER;

ALTER TABLE subcategories ADD CONSTRAINT fk_parent
FOREIGN KEY (parent_id) REFERENCES subcategories(id)
ON DELETE SET NULL;

ALTER TABLE subcategories ADD CONSTRAINT fk_category
FOREIGN KEY (category_id) REFERENCES categories(id)
ON DELETE CASCADE;
