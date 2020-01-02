

-- remove UNIQUE constraint on "uic" column on "ship" table
-- converted "hull" datatype from INTEGER to VARCHAR(6)
-- create new table without constraint and copy data over.
ALTER TABLE ship RENAME TO old_ship;

CREATE TABLE ship (
  ship_pk         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  uic             VARCHAR(5) NOT NULL,
  ship_name       VARCHAR(75) NOT NULL,
  type            VARCHAR(15),
  hull            VARCHAR(6),
  service_code    VARCHAR(1) NOT NULL,
  tycom           VARCHAR(25),
  homeport        VARCHAR(50),
  rsupply         VARCHAR(15)
);

INSERT INTO ship SELECT * FROM old_ship;

DROP TABLE old_ship;

-- allowing project_fk to be null so that it becomes a "global" system variable and not project specific
ALTER TABLE managed_list_item RENAME TO old_managed_list_item;

CREATE TABLE managed_list_item (
  managed_list_item_pk INTEGER PRIMARY KEY NOT NULL,
  managed_list_code INTEGER NOT NULL,
  item_value TEXT NOT NULL,
  current_default TINYINT NOT NULL DEFAULT 0,
  hidden TINYINT NOT NULL DEFAULT 0,
  sort_order INTEGER NOT NULL DEFAULT 0,
  project_fk INTEGER, --null means "global"
  last_updated_by_fk INTEGER NOT NULL,
  last_updated_date DATETIME NOT NULL,
  FOREIGN KEY (project_fk) REFERENCES project(project_pk)
);

INSERT INTO managed_list_item SELECT * FROM old_managed_list_item;

DROP TABLE old_managed_list_item;

-- new "global" type of items for Ship Types and Locations
INSERT INTO managed_list_item (managed_list_code, item_value, last_updated_by_fk, last_updated_date)
VALUES (10, 'ACB', 24, datetime('now', 'localtime'))
,(10, 'ACU', 24, datetime('now', 'localtime'))
,(10, 'ARDM', 24, datetime('now', 'localtime'))
,(10, 'AS', 24, datetime('now', 'localtime'))
,(10, 'ATG', 24, datetime('now', 'localtime'))
,(10, 'BMU', 24, datetime('now', 'localtime'))
,(10, 'CBMU', 24, datetime('now', 'localtime'))
,(10, 'CG', 24, datetime('now', 'localtime'))
,(10, 'CNBG', 24, datetime('now', 'localtime'))
,(10, 'CNSL', 24, datetime('now', 'localtime'))
,(10, 'CNSP', 24, datetime('now', 'localtime'))
,(10, 'COMLCSRON', 24, datetime('now', 'localtime'))
,(10, 'CRG', 24, datetime('now', 'localtime'))
,(10, 'CSDS', 24, datetime('now', 'localtime'))
,(10, 'CSG', 24, datetime('now', 'localtime'))
,(10, 'CSL', 24, datetime('now', 'localtime'))
,(10, 'CSS', 24, datetime('now', 'localtime'))
,(10, 'CTF', 24, datetime('now', 'localtime'))
,(10, 'CVN', 24, datetime('now', 'localtime'))
,(10, 'DDG', 24, datetime('now', 'localtime'))
,(10, 'EODG', 24, datetime('now', 'localtime'))
,(10, 'EODMU', 24, datetime('now', 'localtime'))
,(10, 'ESB', 24, datetime('now', 'localtime'))
,(10, 'FFG', 24, datetime('now', 'localtime'))
,(10, 'LCC', 24, datetime('now', 'localtime'))
,(10, 'LCS', 24, datetime('now', 'localtime'))
,(10, 'LHA', 24, datetime('now', 'localtime'))
,(10, 'LHD', 24, datetime('now', 'localtime'))
,(10, 'LPD', 24, datetime('now', 'localtime'))
,(10, 'LSD', 24, datetime('now', 'localtime'))
,(10, 'MALS', 24, datetime('now', 'localtime'))
,(10, 'MCM', 24, datetime('now', 'localtime'))
,(10, 'MDSU', 24, datetime('now', 'localtime'))
,(10, 'MST', 24, datetime('now', 'localtime'))
,(10, 'NCG', 24, datetime('now', 'localtime'))
,(10, 'NCR', 24, datetime('now', 'localtime'))
,(10, 'NECCPAC', 24, datetime('now', 'localtime'))
,(10, 'NMCB', 24, datetime('now', 'localtime'))
,(10, 'PC', 24, datetime('now', 'localtime'))
,(10, 'SSBN', 24, datetime('now', 'localtime'))
,(10, 'SSGN', 24, datetime('now', 'localtime'))
,(10, 'SSN', 24, datetime('now', 'localtime'))
,(10, 'TACRON', 24, datetime('now', 'localtime'))
,(10, 'UCT', 24, datetime('now', 'localtime'))
,(10, 'UIC', 24, datetime('now', 'localtime'))
;

INSERT INTO managed_list_item (managed_list_code, item_value, last_updated_by_fk, last_updated_date)
VALUES (2, 'Portsmouth, NH', 24, datetime('now', 'localtime'));

--locations is now global
UPDATE managed_list_item SET project_fk = NULL WHERE managed_list_code = 2


