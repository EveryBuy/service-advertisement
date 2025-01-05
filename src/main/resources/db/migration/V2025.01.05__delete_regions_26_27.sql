DELETE FROM cities WHERE region_id = 26;

DELETE FROM cities WHERE region_id = 27;

DELETE FROM regions
WHERE id = 26;

DELETE FROM regions
WHERE id = 27;

INSERT INTO cities(city_name, region_id)
VALUES('Київ', 15)