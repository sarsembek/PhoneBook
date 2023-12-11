CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username varchar,
                       password varchar
);
CREATE TABLE contacts (
                          contact_id SERIAL PRIMARY KEY,
                          address VARCHAR(255),
                          phone_number VARCHAR(255),
                          user_id  int NOT NULL REFERENCES users(id)
);

