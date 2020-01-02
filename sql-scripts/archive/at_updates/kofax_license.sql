BEGIN;

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

ALTER TABLE kofax_license ADD license_expiry_date DATE;

ALTER TABLE kofax_license ADD internal_use_ind TEXT;

DROP VIEW kofax_license_vw;
CREATE VIEW kofax_license_vw AS
SELECT k.kofax_license_pk, k.license_key, k.product_code, k.customer, k.contract_number, c.uic, l.computer_name, s.ship_name, s.type, s.hull, s.homeport, k.received_date,
			 k.received_date, strftime('%m/%d/%Y', k.received_date) AS received_date_fmt,
			 k.license_expiry_date, strftime('%m/%d/%Y', k.license_expiry_date) AS license_expiry_date_fmt,
			 k.internal_use_ind, k.notes, k.last_updated_by, k.last_updated_date
FROM kofax_license k LEFT OUTER JOIN configured_system c ON k.kofax_license_pk = c.kofax_license_fk
                     LEFT OUTER JOIN laptop l ON c.laptop_fk = l.laptop_pk
                     LEFT OUTER JOIN ship s ON s.uic = c.uic;
