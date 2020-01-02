CREATE TABLE laptop (
laptop_pk 				INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
product_name			VARCHAR(50) NOT NULL,
computer_name			VARCHAR(50),
tag								VARCHAR(50),
model_number			VARCHAR(50),
serial_number			VARCHAR(50),
origin						VARCHAR(50),
received_date			DATE,
notes 						TEXT,
last_updated_by		VARCHAR(75),
last_updated_date DATETIME
);

CREATE UNIQUE INDEX laptop_ndx ON laptop (serial_number);

CREATE TABLE scanner (
scanner_pk 				INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
product_name			VARCHAR(50) NOT NULL,
tag								VARCHAR(50),
model_number			VARCHAR(50),
serial_number			VARCHAR(50),
origin						VARCHAR(50),
received_date			DATE,
notes 						TEXT,
last_updated_by		VARCHAR(75),
last_updated_date DATETIME
);

DROP TABLE kofax_license;

CREATE TABLE kofax_license (
kofax_license_pk	INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
license_key				VARCHAR(50) NOT NULL,
product_code			VARCHAR(50) NOT NULL,
received_date			DATE,
notes 						TEXT,
last_updated_by		VARCHAR(75),
last_updated_date DATETIME
);


CREATE TABLE ms_office_license (
ms_office_license_pk	INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
product_name					VARCHAR(50) NOT NULL,
license_key						VARCHAR(50) NOT NULL,
received_date					DATE,
notes 								TEXT,
last_updated_by				VARCHAR(75),
last_updated_date 		DATETIME
);


CREATE UNIQUE INDEX laptop_tag_ndx ON laptop (tag);
CREATE UNIQUE INDEX scanner_tag_ndx ON scanner (tag);

CREATE UNIQUE INDEX kofax_license_ndx ON kofax_license (license_key, product_code);
CREATE UNIQUE INDEX ms_office_license_ndx ON ms_office_license (license_key);

ALTER TABLE laptop ADD prepped_date DATE;

ALTER TABLE scanner ADD prepped_date DATE;

ALTER TABLE laptop ADD mac_address VARCHAR(17);

ALTER TABLE laptop ADD status TEXT;
ALTER TABLE laptop ADD status_notes TEXT;
ALTER TABLE scanner ADD status TEXT;
ALTER TABLE scanner ADD status_notes TEXT;



ALTER TABLE laptop ADD customer TEXT;
ALTER TABLE laptop ADD contract_number TEXT;
ALTER TABLE scanner ADD customer TEXT;
ALTER TABLE scanner ADD contract_number TEXT;
ALTER TABLE kofax_license ADD customer TEXT;
ALTER TABLE kofax_license ADD contract_number TEXT;
ALTER TABLE ms_office_license ADD customer TEXT;
ALTER TABLE ms_office_license ADD contract_number TEXT;

DROP VIEW laptop_vw;
CREATE VIEW laptop_vw AS
SELECT l.laptop_pk, l.product_name, l.computer_name, l.tag, l.model_number, l.mac_address, l.serial_number, l.origin, l.received_date, l.prepped_date, l.notes, l.status, l.status_notes, l.customer, l.contract_number, c.uic, s.ship_name, s.type, s.hull, s.homeport, l.last_updated_by, l.last_updated_date
 FROM laptop l LEFT OUTER JOIN configured_system c ON l.laptop_pk = c.laptop_fk
                     LEFT OUTER JOIN ship s ON s.uic = c.uic;

DROP VIEW scanner_vw ;
CREATE VIEW scanner_vw AS
SELECT s.scanner_pk, s.product_name, s.tag, s.model_number, s.serial_number, s.origin, s.received_date, s.prepped_date, s.notes, s.status, s.status_notes, s.customer, s.contract_number, c.uic, l.computer_name, h.ship_name, h.type, h.hull, h.homeport, s.last_updated_by, s.last_updated_date
FROM scanner s  LEFT OUTER JOIN configured_system c ON s.scanner_pk = c.scanner_fk
                LEFT OUTER JOIN laptop l ON c.laptop_fk = l.laptop_pk
							 LEFT OUTER JOIN ship h ON h.uic = c.uic;


DROP VIEW ms_office_license_vw;
CREATE VIEW ms_office_license_vw AS
SELECT m.ms_office_license_pk, m.product_name, m.license_key, COUNT(DISTINCT c.configured_system_pk) AS installed_cnt, m.received_date, strftime('%m/%d/%Y', m.received_date) AS received_date_fmt, m.notes, m.customer, m.contract_number, m.last_updated_by, m.last_updated_date
FROM ms_office_license m LEFT OUTER JOIN configured_system c ON m.ms_office_license_pk = c.ms_office_license_fk
GROUP BY m.ms_office_license_pk, m.product_name, m.license_key, m.received_date, m.notes, m.last_updated_by, m.last_updated_date;


DROP TABLE misc_hardware;
DROP VIEW misc_hardware_vw;

DROP TABLE misc_license;
DROP VIEW misc_license_vw;


CREATE TABLE misc_hardware (
misc_hardware_pk     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
product_type         TEXT NOT NULL,
product_name         TEXT NOT NULL,
tag                  TEXT,
model_number         TEXT,
serial_number        TEXT,
origin               TEXT,
received_date        DATE,
warranty_expiry_date DATE,
status               TEXT,
status_notes         TEXT,
customer             TEXT,
contract_number      TEXT,
notes                TEXT,
last_updated_by      TEXT,
last_updated_date    DATETIME
);

CREATE VIEW misc_hardware_vw AS
SELECT m.misc_hardware_pk, m.product_type, m.product_name, m.tag, m.model_number, m.serial_number, m.origin, m.status, m.status_notes, m.customer, m.contract_number, m.notes,
			 m.received_date, strftime('%m/%d/%Y', m.received_date) AS received_date_fmt,
			 m.warranty_expiry_date, strftime('%m/%d/%Y', m.warranty_expiry_date) AS warranty_expiry_date_fmt,
			 m.last_updated_by, m.last_updated_date
 FROM misc_hardware m;

CREATE TABLE misc_license (
misc_license_pk       INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
product_name          TEXT NOT NULL,
product_key           TEXT,
installed_cnt         INTEGER NOT NULL DEFAULT 0,
received_date         DATE,
license_expiry_date   DATE,
status                TEXT,
status_notes          TEXT,
customer              TEXT,
contract_number       TEXT,
notes                 TEXT,
last_updated_by       TEXT,
last_updated_date     DATETIME
);

CREATE VIEW misc_license_vw AS
SELECT m.misc_license_pk, m.product_name, m.product_key, m.installed_cnt, m.status, m.status_notes, m.customer, m.contract_number, m.notes,
			 m.received_date, strftime('%m/%d/%Y', m.received_date) AS received_date_fmt,
			 m.license_expiry_date, strftime('%m/%d/%Y', m.license_expiry_date) AS license_expiry_date_fmt,
			 m.last_updated_by, m.last_updated_date
 FROM misc_license m;

				if ($(this).val() == '1 - In Transit to PSHI') {
					$('.statusNotesTd').show();
					$('#statusNotes').val('From Location: ');
				} else if ($(this).val() == '2 - In Transit to manufacturer') {
					$('.statusNotesTd').show();
					$('#statusNotes').val('Location: \r\nContact: ');
				} else if ($(this).val() == '3 - In Transit to vessel') {
					$('.statusNotesTd').show();
					$('#statusNotes').val('To Location: \r\nContact: ');
				} else if ($(this).val() == '4 - Under Repair at PSHI') {
					$('.statusNotesTd').show();
					$('#statusNotes').val('Tech Assigned: ');
				} else if ($(this).val() == '5 - Defective (No repair)') {
					$('.statusNotesTd').show();
					$('#statusNotes').val('PSHI Confirm By: \r\nManufacturer Confirm By: \r\nNotified Client: ');
				} else if ($(this).val() == '5 - Testing & Prep') {
					$('.statusNotesTd').hide();
					$('#statusNotes').val('');
				} else if ($(this).val() == '6 - Available & Ready') {
					$('.statusNotesTd').hide();
					$('#statusNotes').val('');
				} else if ($(this).val() == '7 - Deployed / Onboard Vessel') {
					$('.statusNotesTd').show();
					$('#statusNotes').val('Name of Vessel: ');
				} else if ($(this).val() == '8 - Not Returned to PSHI') {


UPDATE scanner set status = '2 - In Transit to manufacturer' WHERE status like '2%';
UPDATE scanner set status = '3 - In Transit to vessel' WHERE status like '3%';
UPDATE scanner set status = '5 - Defective (No repair)' WHERE status like '%Defective (No repair)';
UPDATE scanner set status = '5 - Testing & Prep' WHERE status like '%Testing & Prep';
UPDATE scanner set status = '8 - Not Returned to PSHI' WHERE status like '8%';


UPDATE scanner set status = '1 - In Transit to PSHI' WHERE status like '%In Transit to PSHI';
UPDATE scanner set status = '4 - Under Repair at PSHI' WHERE status like '%Under Repair at PSHI';
UPDATE scanner set status = '6 - Available & Ready' WHERE status like '%Available & Ready';
UPDATE scanner set status = '7 - Deployed / Onboard Vessel' WHERE status like '%Deployed / Onboard Vessel';


UPDATE laptop set status = '1 - In Transit to PSHI' WHERE status like '1%';
UPDATE laptop set status = '2 - In Transit to manufacturer' WHERE status like '2%';
UPDATE laptop set status = '3 - In Transit to vessel' WHERE status like '3%';
UPDATE laptop set status = '4 - Under Repair at PSHI' WHERE status like '4%';
UPDATE laptop set status = '5 - Defective (No repair)' WHERE status like '%Defective (No repair)';
UPDATE laptop set status = '5 - Testing & Prep' WHERE status like '%Testing & Prep';
UPDATE laptop set status = '6 - Available & Ready' WHERE status like '6%';
UPDATE laptop set status = '7 - Deployed / Onboard Vessel' WHERE status like '7%';
UPDATE laptop set status = '8 - Not Returned to PSHI' WHERE status like '8%';

DROP TABLE vrs_license;

CREATE TABLE vrs_license (
vrs_license_pk    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
license_key       TEXT NOT NULL,
product_code      TEXT NOT NULL,
received_date     DATE,
customer          TEXT,
contract_number   TEXT,
notes             TEXT,
last_updated_by   TEXT NOT NULL,
last_updated_date DATETIME NOT NULL
);

CREATE UNIQUE INDEX vrs_license_ndx ON vrs_license (license_key, product_code);

DROP VIEW vrs_license_vw;
CREATE VIEW vrs_license_vw AS
SELECT v.vrs_license_pk, v.license_key, v.product_code, v.customer, v.contract_number, c.uic, l.computer_name, s.ship_name, s.type, s.hull, s.homeport, v.received_date, v.notes, v.last_updated_by, v.last_updated_date
FROM vrs_license v LEFT OUTER JOIN configured_system c ON v.vrs_license_pk = c.vrs_license_fk
                     LEFT OUTER JOIN laptop l ON c.laptop_fk = l.laptop_pk
                     LEFT OUTER JOIN ship s ON s.uic = c.uic;

