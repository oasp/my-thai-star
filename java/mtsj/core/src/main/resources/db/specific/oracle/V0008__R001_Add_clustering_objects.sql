CREATE TABLE Addresses (
  idUser BIGINT NOT NULL,
  street VARCHAR (255),
  housenumber VARCHAR (255),
  plz VARCHAR (255),
  city VARCHAR (255),
  country VARCHAR (255),
  geocode SDO_GEOMETRY,
  CONSTRAINT PK_Addresses_idUser FOREIGN KEY(idUser) REFERENCES USER(id) NOCHECK
);

CREATE VIEW GeoBooking AS SELECT
  OrderLine.idDish,
  Dish.name AS dishName,
  Booking.bookingDate,
  Addresses.geocode,
  OrderLine.amount
FROM Booking
JOIN USER ON USER.id = Booking.idUser
JOIN Addresses ON USER.id = Addresses.idUser
JOIN Orders ON Orders.idBooking = Booking.id
JOIN OrderLine ON Orders.id = OrderLine.idOrder
JOIN Dish ON Dish.id = OrderLine.idDish;
