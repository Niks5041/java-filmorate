--INSERT INTO mpa (name)
--VALUES
--    ('G'),
--    ('PG'),
--    ('PG-13'),
--    ('R'),
--    ('NC-17');

--INSERT INTO genres (name)
--VALUES
--    ('Комедия'),
--    ('Драма'),
--    ('Мультфильм'),
--    ('Триллер'),
--    ('Документальный'),
--    ('Боевик');




MERGE INTO genres AS target
USING (VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')) AS source (genre_name)
ON target.name = source.genre_name
WHEN NOT MATCHED THEN
  INSERT (name) VALUES (source.genre_name);

MERGE INTO mpa AS target
USING (VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17')) AS source (mpa_name)
ON target.name = source.mpa_name
WHEN NOT MATCHED THEN
  INSERT (name) VALUES (source.mpa_name);



