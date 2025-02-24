-- API Keys
CREATE TABLE IF NOT EXISTS api_key_types
(
    id   VARCHAR2(255) NOT NULL,
    type VARCHAR2(20)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_api_type UNIQUE (type)
);

MERGE INTO api_key_types (id, type)
    KEY (type)
    VALUES ('01951b4b-d0f1-7bef-b5c3-9a25329b363a', 'PLEX')
         , ('01951b4b-d0f1-74fa-bbd0-d059bdf8ee90', 'TMDB')
         , ('01951b4b-d0f1-72bf-ad21-743f315d4135', 'TRAKT')
         , ('01951b4b-d0f1-7108-ac40-e3e456861748', 'ANILIST')
         , ('01951b4c-37b3-76e2-9076-de5689a110db', 'ANIDB');

CREATE TABLE IF NOT EXISTS api_keys
(
    id   VARCHAR2(255) NOT NULL,
    name VARCHAR2(50)  NOT NULL,
    key  VARCHAR2(255) NOT NULL,
    url  VARCHAR2(150) NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (name) REFERENCES api_key_types (type),
    CONSTRAINT unique_api_key UNIQUE (name, key)
);

-- Setting Options
-- CREATE TABLE IF NOT EXISTS setting_options
-- (
--     id      VARCHAR2(255) NOT NULL,
--     name    VARCHAR2(255) NOT NULL,
--     type    VARCHAR2(10)  NOT NULL,
--     version varchar2(50)  NOT NULL,
--     PRIMARY KEY (id),
--     CONSTRAINT unique_setting_option UNIQUE (name)
-- );
--
-- MERGE INTO setting_options (id, name, type, version)
--     KEY (name)
--     VALUES ('01951b4d-54ce-7e77-be05-13ebc39d8d84', 'AssetDirectory', 'STRING', '0.0.1')
--          , ('01951b4d-54ce-7548-b102-e228fc8a6060', 'CollectionOrderDefault', 'STRING', '0.0.1')
--          , ('01951b4d-54ce-77f8-83dc-e0462756b331', 'RemoveBelowMinimum', 'BOOLEAN', '0.0.1')
--          , ('01951b4d-54ce-7173-b21d-ef91b99e79d4', 'RemoveNotScheduled', 'BOOLEAN', '0.0.1')
--          , ('01951b4d-54ce-7a74-aa46-7f43dc9cc131', 'RemoveNotManaged', 'BOOLEAN', '0.0.1')
--          , ('01951b4d-54ce-79c1-8025-c4ab49598986', 'ShowNotManaged', 'BOOLEAN', '0.0.1')
--          , ('01951b4d-54ce-79e3-b37b-22cf3ecb4e6c', 'Schedule', 'CRON', '0.0.1')
--          , ('01951b4d-54ce-76c8-a79f-46c7cc9a522a', 'SyncMode', 'STRING', '0.0.1');

-- Servers
CREATE TABLE IF NOT EXISTS servers
(
    id         VARCHAR2(255) NOT NULL,
    name       VARCHAR2(50)  NOT NULL,
    api_key_id VARCHAR2(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (api_key_id) REFERENCES api_keys (id),
    CONSTRAINT unique_server UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS server_settings
(
    id        VARCHAR2(255) NOT NULL,
    server_id VARCHAR2(255) NOT NULL,
    type      VARCHAR2(10)  NOT NULL,
    name      VARCHAR2(255) NOT NULL,
    val       VARCHAR2(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (server_id) REFERENCES servers (id),
--     FOREIGN KEY (name, type) REFERENCES setting_options (name, type),
    CONSTRAINT unique_server_setting UNIQUE (server_id, name)
);

-- Libraries
CREATE TABLE IF NOT EXISTS libraries
(
    id           VARCHAR2(255) NOT NULL,
    server_id    VARCHAR2(255) NOT NULL,
    reference_id VARCHAR2(255),
    type         VARCHAR2(10)  NOT NULL, -- movie, series, music, etc
    name         VARCHAR2(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (server_id) REFERENCES servers (id),
    CONSTRAINT unique_library UNIQUE (server_id, type, name)
);

CREATE TABLE IF NOT EXISTS library_settings
(
    id         VARCHAR2(255) NOT NULL,
    library_id VARCHAR2(255) NOT NULL,
    type       VARCHAR2(10)  NOT NULL,
    name       VARCHAR2(255) NOT NULL,
    val        VARCHAR2(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (library_id) REFERENCES libraries (id),
--     FOREIGN KEY (name, type) REFERENCES setting_options (name, type),
    CONSTRAINT unique_library_setting UNIQUE (library_id, name)
);

-- Setting Options
-- CREATE TABLE IF NOT EXISTS action_options
-- (
--     id      VARCHAR2(255) NOT NULL,
--     name    VARCHAR2(255) NOT NULL,
--     type    VARCHAR2(10)  NOT NULL,
--     version varchar2(50)  NOT NULL,
--     PRIMARY KEY (id),
--     CONSTRAINT unique_action_option UNIQUE (name)
-- );
--
-- MERGE INTO action_options (id, name, type, version)
--     KEY (name)
--     VALUES ('019524e1-ff11-7874-a0a0-09100c7eabcc', 'BackupAssets', 'CRON', '0.0.1');

CREATE TABLE IF NOT EXISTS library_actions
(
    id         VARCHAR2(255) NOT NULL,
    library_id VARCHAR2(255) NOT NULL,
    type       VARCHAR2(10)  NOT NULL,
    name       VARCHAR2(255) NOT NULL,
    val        VARCHAR2(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (library_id) REFERENCES libraries (id),
--     FOREIGN KEY (name, type) REFERENCES action_options (name, type),
    CONSTRAINT unique_library_action UNIQUE (library_id, name)
);

--  Builders
-- CREATE TABLE IF NOT EXISTS builder_types
-- (
--     id   VARCHAR2(255) NOT NULL,
--     name VARCHAR2(255) NOT NULL,
--     PRIMARY KEY (id),
--     CONSTRAINT unique_builder_type UNIQUE (name)
-- );
--
-- MERGE INTO builder_types (id, name)
--     KEY (name)
--     VALUES ('01952534-6634-7458-aace-b7b797071466', 'Plex')
--          , ('01952536-78db-7f5b-9179-d01c13830b8b', 'TMDB');
-- --           ,('', 'TVDB')
-- --           ,('', 'IMDB')
-- --           ,('', 'TRAKT')
-- --           ,('', 'AniDB')
-- --           ,('', 'AniList')
-- --           ,('', 'MyAnimeList')
-- --           ,('', 'MDBList')
-- --           ,('', 'Letterboxd')
-- --           ,('', 'iCheckMovies')
-- --           ,('', 'FlixPatrol')
-- --           ,('', 'Reciperr')
-- --           ,('', 'stevenLu')
-- --           ,('', 'Sonarr')
-- --           ,('', 'Radarr')
-- --           ,('', 'Tautulli')
--
-- CREATE TABLE IF NOT EXISTS builder_categories
-- (
--     id   VARCHAR2(255) NOT NULL,
--     name VARCHAR2(255) NOT NULL,
--     PRIMARY KEY (id),
--     CONSTRAINT unique_builder_category UNIQUE (name)
-- );
--
-- MERGE INTO builder_categories (id, name)
--     KEY (name)
--     VALUES ('01952553-009d-79ec-a426-614b58897837', '')
--          , ('01952553-009d-7b6f-9f6f-a3106ede3e07', '')
--          , ('01952553-009d-7acc-ab3b-40f61d654bfe', '')
--          , ('01952553-009d-7270-8230-f51b21589054', '')
--          , ('01952553-009d-7f39-a7b9-33d9f7dd229a', '')
--          , ('01952553-009d-7f13-94b2-437e5a52ec21', '')
--          , ('01952553-009d-7fc4-b76a-0b339acbd7b4', '');

CREATE TABLE IF NOT EXISTS builders
(
    id       VARCHAR2(255) NOT NULL,
    type     VARCHAR2(255) NOT NULL,
    category VARCHAR2(255) NOT NULL,
    name     VARCHAR2(255) NOT NULL,
    version  VARCHAR2(50)  NOT NULL,
    PRIMARY KEY (id),
--     FOREIGN KEY (type) REFERENCES builder_types (name),
--     FOREIGN KEY (category) REFERENCES builder_categories (name),
    CONSTRAINT unique_builder UNIQUE (type, category, name)
);

MERGE INTO builders (id, type, category, name, version)
    KEY (type, category, name)
    VALUES ('01952577-e1ed-775a-8fef-ca56b6a4eff9', 'Plex', 'Label', 'Plex Smart Label', '0.0.1')
         , ('01952577-e1ed-7c9a-8dec-1af5a41abb63', 'Plex', 'Filter', 'Plex Smart Filter', '0.0.1')
         , ('01952577-e1ed-7e47-9e8c-3b5225b0b33c', 'TMDB', 'Data', 'Popular', '0.0.1')
         , ('01952577-e1ed-7b45-a377-1cc9cdfccf5d', 'TMDB', 'List', 'Collection', '0.0.1')
         , ('01952577-e1ed-721e-9458-32d05cc82a4d', 'TMDB', 'Media', 'Movie', '0.0.1')
         , ('01952577-e1ed-71fe-9922-3d15f31c1eb6', 'TMDB', 'Media', 'Show', '0.0.1')
         , ('01952577-e1ed-769c-b2e6-52c9bfe552e1', 'TMDB', 'List', 'List', '0.0.1')
         , ('01952577-e1ed-7c31-8d32-d07fe06e9bca', 'TMDB', 'Label', 'Actor', '0.0.1')
         , ('01952577-e1ed-71b9-89c6-e88d4df78a55', 'TMDB', 'Label', 'Director', '0.0.1')
         , ('01952577-e1ed-740d-9a51-885ce4202eb5', 'TMDB', 'Label', 'Company', '0.0.1')
         , ('01952577-e1ed-7ca6-bb7d-a4f7e53c49fe', 'TMDB', 'Label', 'Network', '0.0.1');

CREATE TABLE IF NOT EXISTS collections
(
    id         VARCHAR2(255) NOT NULL,
    library_id VARCHAR2(255) NOT NULL,
    name       VARCHAR2(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (library_id) REFERENCES libraries (id),
    CONSTRAINT unique_collection UNIQUE (library_id, name)
);

CREATE TABLE IF NOT EXISTS collection_builders
(
    id                 VARCHAR2(255) NOT NULL,
    collection_id      VARCHAR2(255) NOT NULL,
    builder_id         VARCHAR2(255) NOT NULL,
    builder_attributes VARCHAR2(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (collection_id) REFERENCES collections (id),
    FOREIGN KEY (builder_id) REFERENCES builders (id)
);

-- Metadata fields: Title, Sort Title, Content Rating, Summary, Labels, Poster, Background, Tags
-- metadata movie: Original Title, Edition, Originally Available, Rating, Studio, Tagline
-- metadata show: Original Title, Originally Available, Rating, Studio, Tagline
-- metadata season: Title, Summary ONLY (no tags)
-- metadata episode: (Originally Available, Rating)
-- tags movie: Directors, Country, Genres, Writers, Producers, Collections
-- tags show: Genres, Collections
-- tags episode: Directors, Writers
-- advanced collection: Collection Mode, Collection Order
-- advanced movie: Metadata language(Library default, ...), Use original title(Library default, Yes, No), Enable credits detection(Library default, disabled)
-- advanced show: episode sorting (Library default, Oldest first, Newest first), Keep(All episodes, 5 latest episodes, 3 latest episodes, latest episode, Episodes added in the past 3 days, Episodes added in the past 7 days, Episodes added in the past 30 days)
--                Delete episodes after playing(Never, After a day, After a week, After a month, On next refresh), Seasons(Library default, Show, Hide), Episode ordering(Library default, The Movie Database (Aired), TheTvDB (Aired), TheTvDB (Absolute))
--                Metadata language(Library default, ...), Use original title(Library default, Yes, No), Enable credits detection(Library default, disabled), Preferred audio language(Library default, ...), Preferred subtitle language(Library default, ...)
--                Auot-select subtitle mode(Account default, Manually selected, Show with foreign audio, Always enabled)
-- mode: Library default, Hide items in this collection, Show this collection and its items, Hide this collection but show its items
-- order: Release date, Alphabetical, Custom

CREATE TABLE IF NOT EXISTS collection_metadata
(
    id            VARCHAR2(255)  NOT NULL,
    collection_id VARCHAR2(255)  NOT NULL,
    name          VARCHAR2(255)  NOT NULL,
    val           VARCHAR2(1000) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (collection_id) REFERENCES collections (id)
);

// NOTE: reference_id = tmdb/anidb
CREATE TABLE IF NOT EXISTS movie_metadata
(
    id           VARCHAR2(255)  NOT NULL,
    reference_id VARCHAR2(255)  NOT NULL,
    name         VARCHAR2(255)  NOT NULL,
    val          VARCHAR2(1000) NOT NULL,
    PRIMARY KEY (id)
);

// NOTE: reference_id = tmdb/anidb
CREATE TABLE IF NOT EXISTS series_metadata
(
    id           VARCHAR2(255)  NOT NULL,
    reference_id VARCHAR2(255)  NOT NULL,
    name         VARCHAR2(255)  NOT NULL,
    val          VARCHAR2(1000) NOT NULL,
    PRIMARY KEY (id)
);

// NOTE: reference_id = tmdb/anidb, season number
CREATE TABLE IF NOT EXISTS season_metadata
(
    id           VARCHAR2(255)  NOT NULL,
    reference_id VARCHAR2(255)  NOT NULL,
    name         VARCHAR2(255)  NOT NULL,
    val          VARCHAR2(1000) NOT NULL,
    PRIMARY KEY (id)
);

// NOTE: reference_id = tmdb/anidb, season number, episode number
CREATE TABLE IF NOT EXISTS episode_metadata
(
    id           VARCHAR2(255)  NOT NULL,
    reference_id VARCHAR2(255)  NOT NULL,
    name         VARCHAR2(255)  NOT NULL,
    val          VARCHAR2(1000) NOT NULL,
    PRIMARY KEY (id)
);
