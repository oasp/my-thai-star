-- This is the SQL script for setting up the DDL for the PostGreSQL database
-- TODO customize SQL for PostGreSQL and test it properly
-- In a typical project you would only distinguish between main and test for flyway SQLs
-- However, in this sample application we provde support for multiple databases in parallel
-- You can simply choose the DB of your choice by providing -Pmysql, -Ppostgresql, ... in your maven build

CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1000000;

-- *** Table ***
CREATE TABLE Table (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  seatsNumber INTEGER NOT NULL,
  CONSTRAINT PK_Table PRIMARY KEY(id)
);

-- *** UserRole ***
CREATE TABLE UserRole (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  name VARCHAR (255),
  active BOOLEAN,
  CONSTRAINT PK_UserRole PRIMARY KEY(id)
);

-- *** User ***
CREATE TABLE User (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  username VARCHAR (255) NULL,
  password VARCHAR (255) NULL,
  email VARCHAR (120) NULL,
  idRole BIGINT NOT NULL,
  CONSTRAINT PK_User PRIMARY KEY(id),
  CONSTRAINT PK_User_idRole FOREIGN KEY(idRole) REFERENCES UserRole(id) NOCHECK
);

-- *** Booking ***
CREATE TABLE Booking (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  idUser BIGINT,
  name VARCHAR (255) NOT NULL,
  bookingToken VARCHAR (60),
  comment VARCHAR (4000),
  email VARCHAR(255) NOT NULL,
  bookingDate TIMESTAMP NOT NULL,
  expirationDate TIMESTAMP,
  creationDate TIMESTAMP,
  canceled BOOLEAN NOT NULL DEFAULT ((0)) ,
  bookingType INTEGER,
  idTable BIGINT,
  idOrder BIGINT,
  assistants INTEGER,
  CONSTRAINT PK_Booking PRIMARY KEY(id),
  CONSTRAINT FK_Booking_idUser FOREIGN KEY(idUser) REFERENCES User(id) NOCHECK,
  CONSTRAINT FK_Booking_idTable FOREIGN KEY(idTable) REFERENCES Table(id) NOCHECK
);

-- *** InvitedGuest ***
CREATE TABLE InvitedGuest (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  idBooking BIGINT NOT NULL,
  guestToken VARCHAR (60),
  email VARCHAR (60),
  accepted BOOLEAN,
  modificationDate TIMESTAMP,
  idOrder BIGINT,
  CONSTRAINT PK_InvitedGuest PRIMARY KEY(id),
  CONSTRAINT FK_InvitedGuest_idBooking FOREIGN KEY(idBooking) REFERENCES Booking(id) NOCHECK
);

-- *** OrderDish ***
CREATE TABLE Orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  idBooking BIGINT NOT NULL,
  idInvitedGuest BIGINT,
  idHost BIGINT,
  CONSTRAINT PK_Order PRIMARY KEY(id),
  CONSTRAINT FK_Order_idBooking FOREIGN KEY(idBooking) REFERENCES Booking(id) NOCHECK,
  CONSTRAINT FK_Order_idInvitedGuest FOREIGN KEY(idInvitedGuest) REFERENCES InvitedGuest(id) NOCHECK
);

-- *** Category ***
CREATE TABLE Category (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  name VARCHAR (255),
  description VARCHAR (4000),
  showOrder INTEGER,
  CONSTRAINT PK_Category PRIMARY KEY(id),
);

-- *** Image ***
CREATE TABLE Image (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  name VARCHAR(255),
  content VARCHAR(2147483647),
  contentType INTEGER,
  mimeType VARCHAR(255),
  CONSTRAINT PK_Image PRIMARY KEY(id)
);

-- *** Dish ***
CREATE TABLE Dish (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  name VARCHAR (255),
  description VARCHAR (4000),
  price DECIMAL (16,10),
  idImage BIGINT UNIQUE NOT NULL,
  CONSTRAINT PK_Dish PRIMARY KEY(id),
  CONSTRAINT FK_Dish_idImage FOREIGN KEY(idImage) REFERENCES Image(id) NOCHECK,
);

-- *** DishCategory ***
CREATE TABLE DishCategory (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  idDish BIGINT NOT NULL,
  idCategory BIGINT NOT NULL,
  CONSTRAINT PK_DishCategory PRIMARY KEY(id),
  CONSTRAINT FK_DishCategory_idDish FOREIGN KEY(idDish) REFERENCES Dish(id) NOCHECK,
  CONSTRAINT FK_DishCategory_idCategory FOREIGN KEY(idCategory) REFERENCES Category(id) NOCHECK
);

-- *** Ingredient ***
CREATE TABLE Ingredient (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  name VARCHAR (255),
  description VARCHAR (4000),
  price DECIMAL (16,10),
  CONSTRAINT PK_Ingredient PRIMARY KEY(id)
);

-- *** DishIngredient ***
CREATE TABLE DishIngredient (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  idDish BIGINT NOT NULL,
  idIngredient BIGINT NOT NULL,
  CONSTRAINT PK_DishIngredient PRIMARY KEY(id),
  CONSTRAINT FK_DishIngredient_idDish FOREIGN KEY(idDish) REFERENCES Dish(id) NOCHECK,
  CONSTRAINT FK_DishIngredient_idIngredient FOREIGN KEY(idIngredient) REFERENCES Ingredient(id) NOCHECK
);

-- *** OrderLine ***
CREATE TABLE OrderLine (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  idDish BIGINT NOT NULL,
  amount INTEGER,
  comment VARCHAR (255),
  idOrder BIGINT NOT NULL,
  CONSTRAINT PK_OrderLine PRIMARY KEY(id),
  CONSTRAINT FK_OrderLine_idDish FOREIGN KEY(idDish) REFERENCES Dish(id) NOCHECK,
  CONSTRAINT FK_OrderLine_idOrder FOREIGN KEY(idOrder) REFERENCES Orders(id) NOCHECK
);

-- *** OrderDishExtraIngredient ***
CREATE TABLE OrderDishExtraIngredient (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER,
  idOrderLine BIGINT NOT NULL,
  idIngredient BIGINT NOT NULL,
  CONSTRAINT PK_OrderDishExtraIngredient PRIMARY KEY(id),
  CONSTRAINT FK_OrderDishExtraIngredient_idOrderLine FOREIGN KEY(idOrderLine) REFERENCES OrderLine(id) NOCHECK,
  CONSTRAINT FK_OrderDishExtraIngredient_idIngredient FOREIGN KEY(idIngredient) REFERENCES Ingredient(id) NOCHECK
);

-- *** UserFavourite ***
CREATE TABLE UserFavourite (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  idUser BIGINT NOT NULL,
  idDish BIGINT NOT NULL,
  CONSTRAINT PK_UserFavourite PRIMARY KEY(id),
  CONSTRAINT FK_UserFavourite_idUser FOREIGN KEY(idUser) REFERENCES User(id) NOCHECK,
  CONSTRAINT FK_UserFavourite_idDish FOREIGN KEY(idDish) REFERENCES Dish(id) NOCHECK
);

-- *************************************************************************

-- *** BinaryObject (BLOBs) ***
CREATE TABLE BinaryObject (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  data BLOB(2147483647),
  size BIGINT NOT NULL,
  mimeType VARCHAR(255),
  PRIMARY KEY (ID)
);

-- *** RevInfo (Commit log for envers audit trail) ***
CREATE TABLE RevInfo(
  id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
  timestamp BIGINT NOT NULL,
  userLogin VARCHAR(255)
);

-- *** Category_AUD ***
CREATE TABLE PUBLIC.Category_AUD(
  id BIGINT NOT NULL,
  name VARCHAR (255),
  description VARCHAR (4000),
  showOrder INTEGER,
  revtype TINYINT,
  rev BIGINT NOT NULL
);

-- *** Image_AUD ***
CREATE TABLE PUBLIC.Image_AUD (
  id BIGINT NOT NULL,
  name VARCHAR(255),
  content clob,
  contentType INTEGER,
  mimeType VARCHAR(255),
  revtype TINYINT,
  rev BIGINT NOT NULL
);

-- *** Dish_AUD ***
CREATE TABLE PUBLIC.DISH_AUD(
  id BIGINT NOT NULL,
  name VARCHAR (255),
  description VARCHAR (4000),
  price DECIMAL (16,10),
  idImage BIGINT NOT NULL,
  revtype TINYINT,
  rev BIGINT NOT NULL
);

-- *** DishCategory_AUD ***
CREATE TABLE PUBLIC.DishCategory_AUD(
  id BIGINT NOT NULL,
  idDish BIGINT NOT NULL,
  idCategory BIGINT NOT NULL,
  revtype TINYINT,
  rev BIGINT NOT NULL
);

-- *** Ingredient_AUD ***
CREATE TABLE PUBLIC.Ingredient_AUD(
  id BIGINT NOT NULL,
  name VARCHAR (255),
  description VARCHAR (4000),
  price DECIMAL (16,10),
  revtype TINYINT,
  rev BIGINT NOT NULL
);

-- *** DishIngredient_AUD ***
CREATE TABLE PUBLIC.DishIngredient_AUD(
  id BIGINT NOT NULL,
  idDish BIGINT NOT NULL,
  idIngredient BIGINT NOT NULL,
  revtype TINYINT,
  rev BIGINT NOT NULL
);