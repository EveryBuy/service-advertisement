CREATE TABLE subcategories (
                               id SERIAL PRIMARY KEY,
                               category_id INTEGER NOT NULL,
                               name_en VARCHAR (100) NOT NULL,
                               name_ukr VARCHAR (100) NOT NULL,
                               FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

