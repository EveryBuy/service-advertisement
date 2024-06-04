ALTER TABLE categories
    ADD COLUMN photo_url VARCHAR(500);

UPDATE categories
SET photo_url =
        CASE id
            WHEN 1 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Help.png'
            WHEN 2 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Childrens+World.jpeg'
            WHEN 3 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Real+Estate.png'
            WHEN 4 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Auto.png'
            WHEN 5 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Transport+Parts.jpeg'
            WHEN 6 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Jobs.png'
            WHEN 7 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Pets.jpeg'
            WHEN 8 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Home+%26+Garden.png'
            WHEN 9 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Electronics.jpeg'
            WHEN 10 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Business+%26+Services.png'
            WHEN 11 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Rentals.png'
            WHEN 12 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Fashion+%26+Style.jpeg'
            WHEN 13 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Hobbies%2C+Recreation+%26+Sports.png'
            WHEN 14 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Give+Away+for+Free.png'
            WHEN 15 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Exchange.png'
            WHEN 16 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Auto+for+the+Military.png'
            WHEN 17 THEN 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Service+in+the+Defense+Forces.png'
            ELSE NULL
            END;