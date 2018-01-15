

CREATE DATABASE IF NOT EXISTS hymarket DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE TABLE sellers
(
    seller_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    videos VARCHAR(255)
)AUTO_INCREMENT=1000;

CREATE TABLE orders
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    customerId INT ,
    sellerName VARCHAR (255)
)AUTO_INCREMENT=1000;

CREATE  TABLE  products
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    pname VARCHAR(255) ,
    pvideo VARCHAR(255)
)AUTO_INCREMENT=1000;

CREATE TABLE  protypes
(
   id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
   color VARCHAR (255),
   size  VARCHAR (255),
   amount INT
)AUTO_INCREMENT=1000;

INSERT INTO sellers(seller_name,seller_pass) VALUES ("admin1","admin1");
INSERT INTO sellers(seller_name,seller_pass) VALUES ("admin2","admin2");

INSERT INTO sellers(seller_name,seller_pass,seller_phone,seller_email) VALUES ("admin","admin123","12345678901","123456@qq.com");
INSERT INTO

-- CREATE INDEX index01
-- ON sellers (seller_id);


-- create table tst
-- (
-- id int not null ,
-- name varchar(255),
-- crtime datetime default now()
-- );
