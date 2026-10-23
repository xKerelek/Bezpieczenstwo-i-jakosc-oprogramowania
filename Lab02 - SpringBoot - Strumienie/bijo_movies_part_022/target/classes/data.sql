DROP TABLE IF EXISTS movies;


CREATE TABLE movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    image VARCHAR(300),
    publication_year INT,
    video_id VARCHAR(300)
);

INSERT INTO movies (title, image, publication_year,video_id) VALUES ('System', './img/movies/system.jpg', 1995,'s0f3JeDVeEo');
INSERT INTO movies (title, image, publication_year,video_id) VALUES ('Piraci z Krzemowej Doliny', './img/movies/pirates.jpg', 1999,'s0f3JeDVeEo');
INSERT INTO movies (title, image, publication_year,video_id) VALUES ('Kontrola absolutna', './img/movies/control.jpg', 2016,'s0f3JeDVeEo');
INSERT INTO movies (title, image, publication_year,video_id) VALUES ('Kod nieśmiertelności', './img/movies/sourcecode.jpg', 2011,'s0f3JeDVeEo');
INSERT INTO movies (title, image, publication_year,video_id) VALUES ('Ex Machina', './img/movies/exmachina.jpg', 2015,'s0f3JeDVeEo');
INSERT INTO movies (title, image, publication_year,video_id) VALUES ('Hakerzy', './img/movies/hackers.jpg', 1995,'s0f3JeDVeEo');
INSERT INTO movies (title, image, publication_year,video_id) VALUES ('Tron', './img/movies/tronclassic.jpg', 1982,'s0f3JeDVeEo');
INSERT INTO movies (title, image, publication_year,video_id) VALUES ('Tron: Dziedzictwo', './img/movies/tron.jpg', 2010,'s0f3JeDVeEo');
INSERT INTO movies (title, image, publication_year,video_id) VALUES ('Ja, robot', './img/movies/robot.jpg', 2004, 's0f3JeDVeEo');
