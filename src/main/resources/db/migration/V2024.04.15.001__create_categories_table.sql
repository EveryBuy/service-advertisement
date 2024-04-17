CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name_en VARCHAR (100) NOT NULL,
    name_ukr VARCHAR (100) NOT NULL
);

INSERT INTO categories (name_en, name_ukr) VALUES
                                               ('Clothing', 'Одяг'),
                                               ('Footwear', 'Взуття'),
                                               ('Sports Equipment', 'Все для спорту'),
                                               ('Children''s World', 'Дитячий світ'),
                                               ('Home & Garden', 'Все для дому'),
                                               ('Hobby', 'Хобі'),
                                               ('Pets', 'Тварини'),
                                               ('Transport', 'Транспорт'),
                                               ('Electronics', 'Електроніка'),
                                               ('Garden & Outdoors', 'Сад та город'),
                                               ('Beauty & Health', 'Краса та здоров''я'),
                                               ('Construction', 'Будівництво'),
                                               ('Accessories', 'Аксесуари'),
                                               ('Charity', 'Благодійність'),
                                               ('Other', 'Інше');