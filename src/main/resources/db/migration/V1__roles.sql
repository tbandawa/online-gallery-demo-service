CREATE TABLE IF NOT EXISTS roles (id INT PRIMARY KEY, name VARCHAR(5));
INSERT INTO roles (id, name) VALUES (1, 'admin');
INSERT INTO roles (id, name) VALUES (2, 'mod');
INSERT INTO roles (id, name) VALUES (3, 'user');