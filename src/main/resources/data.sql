-- API Keys
CREATE TABLE IF NOT EXISTS api_key_types
(
    id   INT          NOT NULL AUTO_INCREMENT,
    type VARCHAR2(20) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_api_type UNIQUE (type)
);

MERGE INTO api_key_types
    KEY (id)
    VALUES (0, 'PLEX'),
           (1, 'TMDB');

CREATE TABLE IF NOT EXISTS api_keys
(
    id   INT           NOT NULL,
    name VARCHAR2(50)  NOT NULL,
    key  VARCHAR2(255) NOT NULL,
    url  VARCHAR2(150) NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (name) REFERENCES api_key_types (type),
    CONSTRAINT unique_api_key UNIQUE (name, key)
);
