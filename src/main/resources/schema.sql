CREATE TABLE IF NOT EXISTS users (
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  email     VARCHAR(255) NOT NULL,
  login     VARCHAR(255) NOT NULL,
  name      VARCHAR(255),
  birthday  DATE
);

CREATE TABLE IF NOT EXISTS films (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(255) NOT NULL,
  description   TEXT,
  release_date  DATE,
  duration      INT,
  mpa_rating    CHAR(5)
);

CREATE TABLE IF NOT EXISTS genres (
  id   INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
  film_id  BIGINT,
  genre_id INT,
  PRIMARY KEY (film_id, genre_id),
  FOREIGN KEY (film_id) REFERENCES films(id),
  FOREIGN KEY (genre_id) REFERENCES genres(id)
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
  code        CHAR(5) PRIMARY KEY,
  description VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS likes (
  film_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (film_id, user_id),
  FOREIGN KEY (film_id) REFERENCES films(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS friendships (
  user_id   BIGINT,
  friend_id BIGINT,
  status    VARCHAR(20),
  PRIMARY KEY (user_id, friend_id),
  FOREIGN KEY (user_id)   REFERENCES users(id),
  FOREIGN KEY (friend_id) REFERENCES users(id)
);
