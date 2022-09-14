CREATE TABLE IF NOT EXISTS products(
   id SERIAL PRIMARY KEY,
   name VARCHAR(100),
   price NUMERIC(9,2),
   creation_date TIMESTAMP,
   description varchar(500)
);