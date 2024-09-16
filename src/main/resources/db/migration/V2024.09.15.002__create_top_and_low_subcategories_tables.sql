CREATE TABLE top_level_subcategories
(
    id          SERIAL PRIMARY KEY,
    name_en     VARCHAR(100) NOT NULL,
    name_ukr    VARCHAR(100) NOT NULL,
    category_id INT          NOT NULL,
    CONSTRAINT fk_top_level_category
        FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE low_level_subcategories
(
    id                       SERIAL PRIMARY KEY,
    name_en                  VARCHAR(100) NOT NULL,
    name_ukr                 VARCHAR(100) NOT NULL,
    top_level_subcategory_id INT          NOT NULL,
    CONSTRAINT fk_low_level_top_level_subcategory
        FOREIGN KEY (top_level_subcategory_id) REFERENCES top_level_subcategories (id) ON DELETE CASCADE
);

INSERT INTO top_level_subcategories (id, name_en, name_ukr, category_id)
SELECT id, name_en, name_ukr, category_id
FROM subcategories WHERE parent_id IS NULL;

INSERT INTO low_level_subcategories (id, name_en, name_ukr, top_level_subcategory_id)
SELECT id, name_en, name_ukr, parent_id
FROM subcategories WHERE parent_id IS NOT NULL;

DROP TABLE subcategories CASCADE;
