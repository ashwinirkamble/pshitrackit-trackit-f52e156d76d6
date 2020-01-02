--2016.11.04 ATT :: Create additional lookup tables
CREATE TABLE location (
	location_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	project_fk INTEGER NOT NULL,
	location TEXT NOT NULL,
	last_updated_by TEXT NOT NULL,
	last_updated_date DATETIME NOT NULL
	);

CREATE INDEX location_ndx_project_fk ON location (project_fk);

BEGIN;
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Bangor, WA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Beaufort, SC', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Bremerton, WA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Camp Pendleton, CA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Cherry Point, NC', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'China Lake, CA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Diego Garcia', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Dam Neck, VA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Everett, WA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Gaeta, Italy', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Groton, CT', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Guam', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Gulfport, MS', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Jacksonville, FL', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Jacksonville, NC', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Key West, FL', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Kings Bay, GA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Little Creek, VA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Manama, Bahrain', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Mayport, FL', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Newport News, VA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Newport, RI', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Norfolk, VA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'NAS Oceana, VA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Okinawa, Japan', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'PSHI Office VA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'PSHI Office HI', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Pearl Harbor, HI', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Port Hueneme, CA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Portsmouth, VA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Quantico, VA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Romania', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Rota, Spain', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'San Diego, CA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Sasebo, Japan', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Vigor Shipyard, WA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Williamsburg, VA', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Yokosuka, Japan', 'Anthony Tsuhako', DATE());
INSERT INTO location (project_fk, location, last_updated_by, last_updated_date) VALUES (1, 'Yorktown, VA', 'Anthony Tsuhako', DATE());
END;

CREATE TABLE laptop_issue (
	laptop_issue_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	project_fk INTEGER NOT NULL,
	laptop_issue TEXT NOT NULL,
	last_updated_by TEXT NOT NULL,
	last_updated_date DATETIME NOT NULL
	);

CREATE INDEX laptop_issue_ndx_project_fk ON laptop_issue (project_fk);

CREATE TABLE scanner_issue (
	scanner_issue_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	project_fk INTEGER NOT NULL,
	scanner_issue TEXT NOT NULL,
	last_updated_by TEXT NOT NULL,
	last_updated_date DATETIME NOT NULL
	);

CREATE INDEX scanner_issue_ndx_project_fk ON scanner_issue (project_fk);

CREATE TABLE software_issue (
	software_issue_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	project_fk INTEGER NOT NULL,
	software_issue TEXT NOT NULL,
	last_updated_by TEXT NOT NULL,
	last_updated_date DATETIME NOT NULL
	);

CREATE INDEX software_issue_ndx_project_fk ON software_issue (project_fk);

DROP TABLE facet_version;

CREATE TABLE facet_version (
	facet_version_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	project_fk INTEGER NOT NULL,
	facet_version TEXT NOT NULL,
	is_curr TEXT,
	sort_order INTEGER NOT NULL,
	last_updated_by TEXT NOT NULL,
	last_updated_date DATETIME NOT NULL
	);

CREATE INDEX facet_version_ndx_project_fk ON facet_version (project_fk);
