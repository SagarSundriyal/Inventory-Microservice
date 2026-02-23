CREATE TABLE inventory_batch (
    batch_id BIGINT PRIMARY KEY,
    product_id BIGINT,
    product_name VARCHAR(255),
    quantity INT,
    expiry_date DATE
);