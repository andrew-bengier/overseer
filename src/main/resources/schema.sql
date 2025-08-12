-- ApiKeys
CREATE TABLE IF NOT EXISTS api_keys
(
    id   VARCHAR2 (255) NOT NULL,
    type VARCHAR2 (50)  NOT NULL,
    name VARCHAR2 (255) NOT NULL,
    key  VARCHAR2 (255) NOT NULL,
    url  VARCHAR2 (150) NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_api_key UNIQUE (name, key)
);

-- Settings
CREATE TABLE IF NOT EXISTS settings
(
    id           VARCHAR2 (255) NOT NULL,
    reference_id VARCHAR2 (255) NOT NULL,
    type         VARCHAR2 (10)  NOT NULL,
    name         VARCHAR2 (255) NOT NULL,
    val          VARCHAR2 (255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_setting UNIQUE (reference_id, name)
);

-- Actions
CREATE TABLE IF NOT EXISTS actions
(
    id           VARCHAR2 (255) NOT NULL,
    reference_id VARCHAR2 (255) NOT NULL,
    type         VARCHAR2 (10)  NOT NULL,
    name         VARCHAR2 (255) NOT NULL,
    val          VARCHAR2 (255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_action UNIQUE (reference_id, name)
);

-- Libraries
CREATE TABLE IF NOT EXISTS servers
(
    id         VARCHAR2 (255) NOT NULL,
    api_key_id VARCHAR2 (255) NOT NULL,
    name       VARCHAR2 (50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_server UNIQUE (name)
);

-- Libraries
CREATE TABLE IF NOT EXISTS libraries
(
    id          VARCHAR2 (255) NOT NULL,
    server_id   VARCHAR2 (255) NOT NULL,
    external_id VARCHAR2 (255)     NULL,
    type        VARCHAR2 (50)  NOT NULL,
    name        VARCHAR2 (50)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_library UNIQUE (server_id, name)
);

-- Builders
CREATE TABLE IF NOT EXISTS builder_options
(
    id       VARCHAR2 (255) NOT NULL,
    type     VARCHAR2 (255) NOT NULL,
    category VARCHAR2 (255) NOT NULL,
    name     VARCHAR2 (255) NOT NULL,
    version  VARCHAR2 (50)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_builder UNIQUE (type, category, name)
);

MERGE INTO builder_options (id, type, category, name, version)
    KEY (type, category, name)
    VALUES
--     ('01952577-e1ed-775a-8fef-ca56b6a4eff9', 'Plex', 'Label', 'Plex Smart Label', '0.0.1'),
--     ('01952577-e1ed-7c9a-8dec-1af5a41abb63', 'Plex', 'Filter', 'Plex Smart Filter', '0.0.1'),
    ('01952577-e1ed-7b45-a377-1cc9cdfccf5d', 'TMDB', 'LIST',  'Collection', '0.0.1');
--     ('01952577-e1ed-721e-9458-32d05cc82a4d', 'TMDB', 'Media', 'Movie',      '0.0.1'),
--     ('01952577-e1ed-769c-b2e6-52c9bfe552e1', 'TMDB', 'List',  'List',       '0.0.1'),
--     ('01952577-e1ed-71fe-9922-3d15f31c1eb6', 'TMDB', 'Media', 'Show',       '0.0.1'),
--     ('01952577-e1ed-7c31-8d32-d07fe06e9bca', 'TMDB', 'Label', 'Actor',      '0.0.1'),
--     ('01952577-e1ed-71b9-89c6-e88d4df78a55', 'TMDB', 'Label', 'Director',   '0.0.1'),
--     ('01952577-e1ed-740d-9a51-885ce4202eb5', 'TMDB', 'Label', 'Company',    '0.0.1'),
--     ('01952577-e1ed-7ca6-bb7d-a4f7e53c49fe', 'TMDB', 'Label', 'Network',    '0.0.1'),
--     ('01952577-e1ed-7e47-9e8c-3b5225b0b33c', 'TMDB', 'Data',  'Popular',    '0.0.1');

-- Collections
CREATE TABLE IF NOT EXISTS collections
(
    id          VARCHAR2 (255) NOT NULL,
    library_id  VARCHAR2 (255) NOT NULL,
    external_id VARCHAR2 (255)     NULL,
    name        VARCHAR2 (50)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_collection UNIQUE (library_id, name)
);

-- Collection - Builders
CREATE TABLE IF NOT EXISTS collection_builders
(
    id            VARCHAR2 (255) NOT NULL,
    template_id   VARCHAR2 (255) NOT NULL,
    collection_id VARCHAR2 (255) NOT NULL,
    type          VARCHAR2 (255) NOT NULL,
    category      VARCHAR2 (255) NOT NULL,
    name          VARCHAR2 (255) NOT NULL,
    attributes    VARCHAR2 (255) NOT NULL,
    PRIMARY KEY (id)
);

-- Media
CREATE TABLE IF NOT EXISTS media
(
    id           VARCHAR2 (255) NOT NULL,
    library_id   VARCHAR2 (255) NOT NULL,
    external_id  VARCHAR2 (255) NOT NULL,
    type         VARCHAR2 (50)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_media UNIQUE (library_id, external_id, type)
);

-- Metadata
CREATE TABLE IF NOT EXISTS metadata
(
    id           VARCHAR2 (255) NOT NULL,
    reference_id VARCHAR2 (255) NOT NULL,
    name         VARCHAR2 (255) NOT NULL,
    val          VARCHAR2 (255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_metadata UNIQUE (reference_id, name)
);

-- media( id, lib, ratingKey(for movie), movie )
-- metadata( id, mediaId, "title", "Changed Title" )
