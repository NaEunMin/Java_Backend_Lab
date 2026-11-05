CREATE TABLE IF NOT EXISTS categories (
    id   BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    price       INTEGER NOT NULL,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
