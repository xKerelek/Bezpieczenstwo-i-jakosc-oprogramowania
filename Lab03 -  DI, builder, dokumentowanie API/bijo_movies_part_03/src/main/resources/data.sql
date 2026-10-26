DROP TABLE IF EXISTS movies;


CREATE TABLE movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    image VARCHAR(300),
    publication_year INT,
    video_id VARCHAR(20)
);

INSERT INTO movies (title, image, publication_year, video_id) VALUES ('System', './img/movies/system.jpg', 1995, '46qKHq7REI4');
INSERT INTO movies (title, image, publication_year, video_id) VALUES ('Piraci z Krzemowej Doliny', './img/movies/pirates.jpg', 1999, 'lEyrivrjAuU');
INSERT INTO movies (title, image, publication_year, video_id) VALUES ('Kontrola absolutna', './img/movies/control.jpg', 2016, '_W_1dmcSthE');
INSERT INTO movies (title, image, publication_year, video_id) VALUES ('Kod nieśmiertelności', './img/movies/sourcecode.jpg', 2011, 'yG4omp0xO6o');
INSERT INTO movies (title, image, publication_year, video_id) VALUES ('Ex Machina', './img/movies/exmachina.jpg', 2015, 'OD_vO9-Ztr8');
INSERT INTO movies (title, image, publication_year, video_id) VALUES ('Hakerzy', './img/movies/hackers.jpg', 1995, 'Rn2cf_wJ4f4');
INSERT INTO movies (title, image, publication_year, video_id) VALUES ('Tron', './img/movies/tronclassic.jpg', 1982, 'NgC6yQmHu2Q');
INSERT INTO movies (title, image, publication_year, video_id) VALUES ('Tron: Dziedzictwo', './img/movies/tron.jpg', 2010, 'hsbcBq2uVxw');
INSERT INTO movies (title, image, publication_year, video_id) VALUES ('Ja, robot', './img/movies/robot.jpg', 2004, 's0f3JeDVeEo');
