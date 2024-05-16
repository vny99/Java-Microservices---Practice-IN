CREATE TABLE IF NOT EXISTS purchase (
    id VARCHAR(255) PRIMARY KEY,
    productIds VARCHAR(1024),
    userName VARCHAR(255),
    quantity INT,
    purchaseTime DATE,
    totalAmount DOUBLE PRECISION,
    isNew BOOLEAN
);