BEGIN;

DROP TABLE ship;
CREATE TABLE ship (
ship_pk 				INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
uic							VARCHAR(5) NOT NULL,
ship_name				VARCHAR(75) NOT NULL,
type						VARCHAR(15),
hull						INTEGER,
service_code		VARCHAR(1) NOT NULL,
tycom						VARCHAT(25),
homeport				VARCHAR(50)
);

CREATE UNIQUE INDEX ship_ndx ON ship (uic);

CREATE INDEX ship_ndx_ship_name ON ship (ship_name);

CREATE INDEX ship_ndx_service_code_tycom ON ship (service_code, tycom);

DROP VIEW ship_vw;

CREATE VIEW ship_vw AS
SELECT ship_pk, uic, ship_name, type, hull, service_code, homeport, tycom,
CASE WHEN service_code = 'R' and tycom = 'AIRFOR' THEN 'AIRPAC'
		 WHEN service_code = 'R' and tycom = 'SURFOR' THEN 'SURFPAC'
		 WHEN service_code = 'R' and tycom = 'SUBFOR' THEN 'SUBPAC'
		 WHEN service_code = 'V' and tycom = 'AIRFOR' THEN 'AIRLANT'
		 WHEN service_code = 'V' and tycom = 'SURFOR' THEN 'SURFLANT'
		 WHEN service_code = 'V' and tycom = 'SUBFOR' THEN 'SUBLANT'
		 END as tycom_display
FROM ship;

COMMIT;
SELECT count(1) FROM ship;

ALTER TABLE ship ADD rsupply VARCHAR(15);

DROP VIEW ship_vw;

CREATE VIEW ship_vw AS
SELECT ship_pk, uic, ship_name, type, hull, service_code, homeport, rsupply, tycom,
CASE WHEN service_code = 'R' and tycom = 'AIRFOR' THEN 'AIRPAC'
		 WHEN service_code = 'R' and tycom = 'SURFOR' THEN 'SURFPAC'
		 WHEN service_code = 'R' and tycom = 'SUBFOR' THEN 'SUBPAC'
		 WHEN service_code = 'V' and tycom = 'AIRFOR' THEN 'AIRLANT'
		 WHEN service_code = 'V' and tycom = 'SURFOR' THEN 'SURFLANT'
		 WHEN service_code = 'V' and tycom = 'SUBFOR' THEN 'SUBLANT' END as tycom_display
FROM ship;


DROP VIEW ship_vw;

CREATE VIEW ship_vw AS
SELECT ship_pk, uic, ship_name, type, hull, service_code, homeport, rsupply, tycom, tycom AS tycom_display
FROM ship;
