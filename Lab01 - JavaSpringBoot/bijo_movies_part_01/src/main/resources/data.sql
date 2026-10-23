DROP TABLE IF EXISTS movies;


CREATE TABLE movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    image VARCHAR(300),
    prod_year INT
);

INSERT INTO movies (title, image, prod_year) VALUES ('System', './img/movies/system.jpg', 1999);
INSERT INTO movies (title, image, prod_year) VALUES ('Piraci z Krzemowej Doliny', './img/movies/pirates.jpg', 2000);
INSERT INTO movies (title, image, prod_year) VALUES ('Kontrola absolutna', './img/movies/control.jpg', 2015);
INSERT INTO movies (title, image, prod_year) VALUES ('Kod nieśmiertelności', './img/movies/sourcecode.jpg', 2011);
INSERT INTO movies (title, image, prod_year) VALUES ('Ex Machina', './img/movies/exmachina.jpg', 1960);
INSERT INTO movies (title, image, prod_year) VALUES ('Hakerzy', './img/movies/hackers.jpg', 2026);
INSERT INTO movies (title, image, prod_year) VALUES ('Tron', './img/movies/tronclassic.jpg', 2020);
INSERT INTO movies (title, image, prod_year) VALUES ('Tron: Dziedzictwo', './img/movies/tron.jpg', 2010);
INSERT INTO movies (title, image, prod_year) VALUES ('Ja, robot', './img/movies/robot.jpg', 2019);
