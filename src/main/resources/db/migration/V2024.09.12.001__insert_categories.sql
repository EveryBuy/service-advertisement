TRUNCATE TABLE categories RESTART IDENTITY CASCADE;
TRUNCATE TABLE subcategories RESTART IDENTITY CASCADE;

INSERT INTO categories (name_en, name_ukr, photo_url) VALUES
('Fashion and Style', 'Мода і стиль', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Fashion+%26+Style.jpeg'),
('Kids World', 'Дитячий світ', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Childrens+World.jpeg'),
('Hobbies and Sports', 'Хоббі та спорт', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Hobbies%2C+Recreation+%26+Sports.png'),
('Transport', 'Транспорт', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Transport+Parts.jpeg'),
('Electronics', 'Електроніка', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Electronics.jpeg'),
('Home and Garden', 'Дім і сад', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Home+%26+Garden.png'),
('Real Estate', 'Нерухомість', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Real+Estate.png'),
('Pet Supplies', 'Зоотовари', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Pets.jpeg'),
('Business and Services', 'Бізнес та послуги', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Business+%26+Services.png'),
('Jobs', 'Робота', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Jobs.png'),
('Help/Exchange', 'Допомога/обмін', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Help.png'),
('Rental and Leasing', 'Оренда та прокат', 'https://everybuy-category.s3.eu-north-1.amazonaws.com/Rentals.png');

