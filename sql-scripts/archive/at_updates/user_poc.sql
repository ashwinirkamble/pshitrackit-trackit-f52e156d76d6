create table users (
	user_pk 				INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  username      VARCHAR(25) NOT NULL,
  password      VARCHAR(64) NOT NULL,
  last_name			VARCHAR(50) NOT NULL,
  first_name		VARCHAR(50) NOT NULL,
  organization	VARCHAR(50) NOT NULL,
  title					VARCHAR(50),
  email					VARCHAR(255),
  work_number		VARCHAR(25),
  quick_dial		VARCHAR(10),
  fax_number		VARCHAR(25),
  cell_number		VARCHAR(25),
  enabled				VARCHAR(1) NOT NULL DEFAULT 'Y',
  last_updated_by VARCHAR(50),
  last_updated_date DATETIME
);

CREATE UNIQUE INDEX users_ndx ON users (username);

create table user_role (
  user_fk         INTEGER NOT NULL,
  rolename        VARCHAR(25) NOT NULL,
  last_updated_by INTEGER,
  PRIMARY KEY (user_fk, rolename)
);

CREATE VIEW user_role_vw AS
SELECT u.user_pk, u.username, r.rolename
FROM users u INNER JOIN user_role r ON u.user_pk = r.user_fk;

CREATE TABLE user_project (
  user_fk         INTEGER NOT NULL,
  project_fk      INTEGER NOT NULL,
  last_updated_by INTEGER,
  PRIMARY KEY (user_fk, project_fk)
);


CREATE TABLE poc (
	poc_pk 				INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  project_fk    INTEGER NOT NULL,
  last_name			VARCHAR(50) NOT NULL,
  first_name		VARCHAR(50) NOT NULL,
  organization	VARCHAR(50) NOT NULL,
  title					VARCHAR(50),
  email					VARCHAR(255),
  work_number		VARCHAR(25),
  fax_number		VARCHAR(25),
  cell_number		VARCHAR(25),
  last_updated_by VARCHAR(50),
  last_updated_date DATETIME
);

CREATE INDEX poc_ndx ON poc (project_fk);

DROP TABLE ship_poc;
CREATE TABLE ship_poc (
	ship_poc_pk 	INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  ship_fk		    INTEGER NOT NULL,
  last_name			VARCHAR(50) NOT NULL,
  first_name		VARCHAR(50) NOT NULL,
  rank					VARCHAR(10),
  title					VARCHAR(50),
  email					VARCHAR(255),
  work_number		VARCHAR(25),
  fax_number		VARCHAR(25),
  cell_number		VARCHAR(25),
  last_updated_by VARCHAR(50),
  last_updated_date DATETIME
);

CREATE INDEX ship_poc_ndx ON ship_poc (ship_fk);

ALTER TABLE poc ADD rank VARCHAR(10);

ALTER TABLE ship_poc ADD dept VARCHAR(50);

ALTER TABLE ship_poc ADD alt_email VARCHAR(255);

ALTER TABLE ship_poc ADD is_primary_poc TEXT;

DROP VIEW ship_poc_vw;
CREATE VIEW ship_poc_vw AS
SELECT p.ship_poc_pk, p.ship_fk, s.uic, s.ship_name, s.type, s.hull, s.tycom, s.homeport, p.last_name, p.first_name, p.rank, p.title,
			 p.dept, p.email, p.alt_email, p.work_number, p.fax_number, p.cell_number, p.is_primary_poc, p.last_updated_by, p.last_updated_date
FROM ship_poc p INNER JOIN ship s ON p.ship_fk = s.ship_pk;

update ship_poc set is_primary_poc = 'Y' WHERE title = 'SUPPO';

DROP TABLE pto_travel;
DROP VIEW pto_travel_vw;



CREATE TABLE pto_travel (
	pto_travel_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	user_fk INTEGER NOT NULL,
	start_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	leave_type TEXT NOT NULL,
	location TEXT,
  last_updated_by TEXT,
  last_updated_date DATETIME
	);

CREATE VIEW pto_travel_vw AS
SELECT p.pto_travel_pk, p.user_fk, u.first_name, u.last_name,
			 p.start_date, strftime('%m/%d/%Y', p.start_date) AS start_date_fmt,
			 p.end_date, strftime('%m/%d/%Y', p.end_date) AS end_date_fmt,
			 p.leave_type, p.location, p.last_updated_by, p.last_updated_date
FROM pto_travel p INNER JOIN users u ON p.user_fk = u.user_pk;

DROP TRIGGER delete_user;
CREATE TRIGGER delete_user BEFORE DELETE ON users
BEGIN
	DELETE FROM user_role WHERE user_fk = old.user_pk;
	DELETE FROM user_project WHERE user_fk = old.user_pk;
	DELETE FROM pto_travel WHERE user_fk = old.user_pk;
END;
