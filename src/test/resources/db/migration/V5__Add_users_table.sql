CREATE TABLE IF NOT EXISTS users(
   id SERIAL PRIMARY KEY,
   name VARCHAR(50),
   last_name VARCHAR(50),
   email VARCHAR(50) UNIQUE,
   login VARCHAR(50) UNIQUE,
   password VARCHAR(50),
   salt VARCHAR(50)
);