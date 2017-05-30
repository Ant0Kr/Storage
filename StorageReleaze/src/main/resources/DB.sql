DROP DATABASE IF EXISTS storage;
CREATE DATABASE IF NOT EXISTS storage;
USE storage;

DROP TABLE IF EXISTS storage;
CREATE TABLE IF NOT EXISTS storage(
  id INT AUTO_INCREMENT,
  name VARCHAR(40),
  count INT,
  date VARCHAR(40),
  category VARCHAR(40),
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users(
  id INT AUTO_INCREMENT,
  login VARCHAR(40),
  password VARCHAR(40),
  rights VARCHAR(10),
  PRIMARY KEY (id)
);

USE storage;

INSERT INTO storage(name,count,date,category)VALUES ("Профиль ПВХ",100,"12.03.2016","Комплектующие");
INSERT INTO storage(name,count,date,category)VALUES ("Мячи футбольные",50,"10.03.2016","Спорт-инвентарь");
INSERT INTO users(login,password,rights)VALUES ('admin','admin','ADMIN');
INSERT INTO users(login,password,rights)VALUES ('user','user','User');