CREATE COLUMN TABLE Addresses (
  idUser BIGINT NOT NULL,
  street VARCHAR (255),
  housenumber VARCHAR (255),
  plz VARCHAR (255),
  city VARCHAR (255),
  country VARCHAR (255),
  geocode ST_POINT (4326),
  CONSTRAINT PK_Addresses_idUser FOREIGN KEY(idUser) REFERENCES USER(id)
);

CREATE GEOCODE INDEX user_geocode_index ON Addresses (
  street STREET,
  housenumber HOUSE_NUMBER,
  plz POSTAL_CODE,
  city CITY,
  country COUNTRY,
  geocode GEOCODE
) ASYNC FLUSH QUEUE EVERY 1 MINUTES OR AFTER 1 ROWS;

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
