CREATE DATABASE IF NOT EXISTS venue_db;
USE venue_db;

DROP TABLE IF EXISTS venue;
CREATE TABLE venue (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    type ENUM('RM','MTF','OTF') NOT NULL,
    status ENUM('OPEN','CLOSED') NOT NULL
);

DROP TABLE IF EXISTS instrument;
CREATE TABLE instrument (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    symbol VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    venue_id BIGINT NOT NULL,
    type ENUM('STOCK','BOND','DERIVATIVE','FX') NOT NULL,
    FOREIGN KEY (venue_id) REFERENCES venue(id)
);

INSERT INTO venue (name, location, type, status) VALUES
('NYSE', 'New York, USA', 'RM', 'OPEN'),
('NASDAQ', 'New York, USA', 'MTF', 'OPEN'),
('UBS Trading Venue', 'Zurich, Switzerland', 'OTF', 'CLOSED');

INSERT INTO instrument (symbol, name, venue_id, type) VALUES
('AAPL', 'Apple Inc.', 2, 'STOCK'),
('GOOGL', 'Alphabet Inc.', 2, 'STOCK'),
('UBS1', 'UBS Bond 2025', 3, 'BOND'),
('TSLA', 'Tesla Inc.', 2, 'STOCK');

