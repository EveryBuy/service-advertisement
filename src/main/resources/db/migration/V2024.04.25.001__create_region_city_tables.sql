CREATE TABLE IF NOT EXISTS regions(
                                   id SERIAL PRIMARY KEY,
                                   region_name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS cities(
  id SERIAL PRIMARY KEY,
  city_name VARCHAR(100),
    region_id INT,
    CONSTRAINT fk_city_region FOREIGN KEY (region_id) REFERENCES regions(id)
);