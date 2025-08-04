UPDATE categories
SET name_ukr = 'Зоосвіт'
WHERE name_ukr = 'Зоотовари';

UPDATE categories
SET name_ukr = 'Допомога та обмін'
WHERE name_ukr = 'Допомога/обмін';

UPDATE categories
SET photo_url = 'https://everybuy-category.s3.eu-north-1.amazonaws.com/apartments.jpg'
WHERE name_ukr = 'Нерухомість';

UPDATE categories
SET photo_url = 'https://everybuy-category.s3.eu-north-1.amazonaws.com/help.jpeg'
WHERE name_ukr = 'Допомога та обмін';
