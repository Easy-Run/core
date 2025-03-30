CREATE TABLE config_category (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT NOT NULL UNIQUE,
    name TEXT,
    description TEXT
);

INSERT INTO config_category (code, name, description) VALUES
('CORE', 'Core', 'Paramètres généraux de l''application');
-- (SELECT id FROM config_category WHERE code = 'CORE')
INSERT INTO config_category (code, name, description) VALUES
('PDA_MANAGER', 'PDA Manager', 'Paramètres relatifs à la gestion des PDA');
-- (SELECT id FROM config_category WHERE code = 'PDA_MANAGER')
INSERT INTO config_category (code, name, description) VALUES
('UPDATE', 'Mise à jour', 'Paramètres relatifs aux mises à jour de l''application');
-- (SELECT id FROM config_category WHERE code = 'UPDATE')

CREATE TABLE config_param (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT NOT NULL UNIQUE,
    value TEXT,
    description TEXT,
    is_visible INTEGER NOT NULL,
    editable INTEGER NOT NULL,
    type TEXT,
	idconfig_category INTEGER,
	FOREIGN KEY (idconfig_category) REFERENCES config_category(id)
);

INSERT INTO config_param (code, value, description, is_visible, editable, type, idconfig_category) VALUES
('APP_VERSION', NULL, 'Version de l''application', 1, 0, NULL, (SELECT id FROM config_category WHERE code = 'CORE'));

INSERT INTO config_param (code, value, description, is_visible, editable, type, idconfig_category) VALUES
('APP_DIR', null, 'Chemin vers le dossier d''installation', 1, 0, NULL, (SELECT id FROM config_category WHERE code = 'CORE'));

INSERT INTO config_param (code, value, description, is_visible, editable, type, idconfig_category) VALUES
('CHANGELOG', null, 'Lien vers le CHANGELOG', 1, 0, NULL, (SELECT id FROM config_category WHERE code = 'CORE'));

INSERT INTO config_param (code, value, description, is_visible, editable, type, idconfig_category) VALUES
('DEFAULT_PDA', null, 'PDA par défaut configuré', 1, 1, 'text', (SELECT id FROM config_category WHERE code = 'PDA_MANAGER'));

INSERT INTO config_param (code, value, description, is_visible, editable, type, idconfig_category) VALUES
('TIME_BEFORE_CHECK_UPDATE', '4', 'Temps (en heures) avant de rechercher une nouvelle mise à jour', 1, 1, 'number', (SELECT id FROM config_category WHERE code = 'UPDATE'));

INSERT INTO config_param (code, value, description, is_visible, editable, type, idconfig_category) VALUES
('REQUIRE_UPDATE', 1, 'Vérification automatique d''une mise à jour', 1, 1, 'boolean', (SELECT id FROM config_category WHERE code = 'UPDATE'));

INSERT INTO config_param (code, value, description, is_visible, editable, type, idconfig_category) VALUES
('LAST_CHECK_UPDATE', NULL, 'Date de la dernière recherche d''une mise à jour', 1, 0, NULL, (SELECT id FROM config_category WHERE code = 'UPDATE'));

INSERT INTO config_param (code, value, description, is_visible, editable, type, idconfig_category) VALUES
('DEBUG_LEVEL', '0', 'Niveau de debug pour l''écriture des logs', 1, 1, 'number', (SELECT id FROM config_category WHERE code = 'CORE'));

CREATE TABLE application (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) DEFAULT NULL,
    package_name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    is_default BOOLEAN DEFAULT NULL,
    build_path TEXT DEFAULT NULL
);