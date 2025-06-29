-- data.sql для H2

MERGE INTO genres (id, name) KEY(id) VALUES
  (1, 'Комедия');

MERGE INTO genres (id, name) KEY(id) VALUES
  (2, 'Драма');

MERGE INTO genres (id, name) KEY(id) VALUES
  (3, 'Мультфильм');

MERGE INTO genres (id, name) KEY(id) VALUES
  (4, 'Триллер');

MERGE INTO genres (id, name) KEY(id) VALUES
  (5, 'Документальный');

MERGE INTO genres (id, name) KEY(id) VALUES
  (6, 'Боевик');


MERGE INTO mpa_ratings (code, description) KEY(code) VALUES
  ('G',    'Без возрастных ограничений');

MERGE INTO mpa_ratings (code, description) KEY(code) VALUES
  ('PG',   'С родителями');

MERGE INTO mpa_ratings (code, description) KEY(code) VALUES
  ('PG-13','Дети до 13 не желательны');

MERGE INTO mpa_ratings (code, description) KEY(code) VALUES
  ('R',    '17+ с взрослыми');

MERGE INTO mpa_ratings (code, description) KEY(code) VALUES
  ('NC-17','18+');
