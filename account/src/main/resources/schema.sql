CREATE TABLE IF NOT EXISTS account (
    id VARCHAR(255) PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    dob TIMESTAMP,
    role VARCHAR(255)
);