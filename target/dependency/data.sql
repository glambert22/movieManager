DROP TABLE IF EXISTS movies;

CREATE TABLE movies (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  genre VARCHAR(250) NOT NULL,
  year_released INT NOT NULL,
  rating FLOAT DEFAULT NULL,
  UNIQUE (name)
);

INSERT INTO movies (name, genre, year_released, rating) VALUES
  ('Avengers: Endgame', 'Action & Adventure, Drama, Science Fiction & Fantasy', 2019, 94.9),
  ('The Dark Knight', 'Action & Adventure, Drama, Science Fiction & Fantasy', 2008,  94.2),
  ('Avatar', 'Action & Adventure, Comedy, Mystery & Suspense, Science Fiction & Fantasy', 2009, 94.0);