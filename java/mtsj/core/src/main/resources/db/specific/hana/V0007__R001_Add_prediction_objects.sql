CREATE COLUMN TABLE DateInfo(
  "DATE" DATE,
  TEMPERATURE DOUBLE,
  DESIGNATION VARCHAR(255)
);

CREATE VIEW DailyOrderedDishes AS SELECT
  "ORDERLINE"."IDDISH" AS "IDDISH",
  "BOOKING"."BOOKINGDATE" AS "BOOKINGDATE",
  SUM("ORDERLINE"."AMOUNT") AS "AMOUNT"
FROM "ORDERLINE" AS "ORDERLINE"
JOIN "ORDERS" AS "ORDERS" ON "ORDERLINE"."IDORDER" = "ORDERS"."ID"
JOIN "BOOKING" AS "BOOKING" ON "BOOKING"."ID" = "ORDERS"."IDBOOKING"
GROUP BY "BOOKING"."BOOKINGDATE", "ORDERLINE"."IDDISH"
ORDER BY "BOOKINGDATE";

CREATE VIEW MonthlyOrderedDishes AS SELECT
  "ORDERLINE"."IDDISH" AS "IDDISH",
  TO_TIMESTAMP(TO_VARCHAR("BOOKING"."BOOKINGDATE", 'YYYY-MM'), 'YYYY-MM') AS "BOOKINGDATE",
  ROUND(AVG("DATEINFO"."TEMPERATURE"),1) AS "TEMPERATURE",
  SUM("ORDERLINE"."AMOUNT") AS "AMOUNT"
FROM "ORDERLINE" AS "ORDERLINE"
JOIN "ORDERS" AS "ORDERS" ON "ORDERLINE"."IDORDER" = "ORDERS"."ID"
JOIN "BOOKING" AS "BOOKING" ON "BOOKING"."ID" = "ORDERS"."IDBOOKING"
JOIN "DATEINFO" AS "DATEINFO" ON TO_VARCHAR("DATEINFO"."DATE", 'YYYY-MM') = TO_VARCHAR("BOOKING"."BOOKINGDATE", 'YYYY-MM')
GROUP BY TO_TIMESTAMP(TO_VARCHAR("BOOKING"."BOOKINGDATE", 'YYYY-MM'), 'YYYY-MM'), "ORDERLINE"."IDDISH"
ORDER BY "BOOKINGDATE";

CREATE VIEW OrderedDishesPerDay AS SELECT
  TO_BIGINT(TO_DATS("DAILYORDEREDDISHES"."BOOKINGDATE") || "DAILYORDEREDDISHES"."IDDISH") AS "ID",
  "DISH"."MODIFICATIONCOUNTER",
  "DATEINFO"."TEMPERATURE",
  "DATEINFO"."DESIGNATION",
  "DAILYORDEREDDISHES".*
FROM "DAILYORDEREDDISHES" AS "DAILYORDEREDDISHES"
JOIN "DISH" AS "DISH" ON "DAILYORDEREDDISHES"."IDDISH" = "DISH"."ID"
JOIN "DATEINFO" AS "DATEINFO" ON TO_VARCHAR("DATEINFO"."DATE", 'YYYY-MM-DD') = TO_VARCHAR("DAILYORDEREDDISHES"."BOOKINGDATE", 'YYYY-MM-DD');

CREATE VIEW OrderedDishesPerMonth AS SELECT
  TO_BIGINT(TO_DATS("MONTHLYORDEREDDISHES"."BOOKINGDATE") || "MONTHLYORDEREDDISHES"."IDDISH") AS "ID",
  "DISH"."MODIFICATIONCOUNTER",
  "MONTHLYORDEREDDISHES".*
FROM "MONTHLYORDEREDDISHES" AS "MONTHLYORDEREDDISHES"
JOIN "DISH" AS "DISH" ON "MONTHLYORDEREDDISHES"."IDDISH" = "DISH"."ID";

CREATE COLUMN TABLE TMP_PREDICTION_DATA ("TIMESTAMP" INTEGER, "AMOUNT" INTEGER, "TEMPERATURE" DOUBLE, "HOLIDAY" INTEGER);
CREATE COLUMN TABLE TMP_PREDICTION_FIT ("TIMESTAMP" INTEGER, "FITTED" DOUBLE, "RESIDUALS" DOUBLE);
CREATE COLUMN TABLE TMP_PREDICTION_MODEL ("KEY" VARCHAR(100), "VALUE" VARCHAR(5000));

CREATE COLUMN TABLE PREDICTION_ALL_MODELS (
	id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	modificationCounter INTEGER DEFAULT 0 NOT NULL,
	idDish BIGINT NOT NULL,
	"KEY" VARCHAR(100),
	"VALUE" VARCHAR(5000),
	PRIMARY KEY(id)
);

CREATE COLUMN TABLE PREDICTION_FORECAST_DATA (
	"TIMESTAMP" INTEGER,
	"TEMPERATURE" DOUBLE,
	"HOLIDAY" INTEGER,
PRIMARY KEY("TIMESTAMP"));

CREATE COLUMN TABLE TMP_PREDICTION_FORECAST ("TIMESTAMP" INTEGER, "FORECAST" DOUBLE, "SE" DOUBLE, "LO80" DOUBLE, "HI80" DOUBLE, "LO95" DOUBLE, "HI95" DOUBLE);

CREATE COLUMN TABLE PREDICTION_ALL_FORECAST (
  id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  modificationCounter INTEGER DEFAULT 0 NOT NULL,
  idDish BIGINT NOT NULL,
  dishName VARCHAR(255),
  timestamp INTEGER,
  forecast DOUBLE,
  PRIMARY KEY(id)
);

CREATE COLUMN TABLE PREDICTION_AUTOARIMA_PARAMS ("NAME" VARCHAR(50), "INT_VALUE" INTEGER, "DOUBLE_VALUE" DOUBLE, "STRING_VALUE" VARCHAR(100));
INSERT INTO PREDICTION_AUTOARIMA_PARAMS VALUES ('D', 0, NULL, NULL);
INSERT INTO PREDICTION_AUTOARIMA_PARAMS VALUES ('MAX_P', 1, NULL, NULL);
INSERT INTO PREDICTION_AUTOARIMA_PARAMS VALUES ('MAX_Q', 1, NULL, NULL);
INSERT INTO PREDICTION_AUTOARIMA_PARAMS VALUES ('SEASONAL_PERIOD', 7, NULL, NULL);
INSERT INTO PREDICTION_AUTOARIMA_PARAMS VALUES ('MAX_SEASONAL_P', 1, NULL, NULL);
INSERT INTO PREDICTION_AUTOARIMA_PARAMS VALUES ('MAX_SEASONAL_Q', 1, NULL, NULL);

CREATE COLUMN TABLE PREDICTION_ARIMA_PARAMS ("NAME" VARCHAR(50), "INT_VALUE" INTEGER, "DOUBLE_VALUE" DOUBLE, "STRING_VALUE" VARCHAR(100));
INSERT INTO PREDICTION_ARIMA_PARAMS VALUES ('FORECAST_METHOD', 1, NULL, NULL);
