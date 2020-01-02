CREATE TABLE decom_workflow (
	decom_workflow_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	ship_fk INTEGER NOT NULL,
	system_received_date DATE,
	decom_date DATE,
	s1_completed_date DATE,
	s2_completed_date DATE,
	system_returned_date DATE,
	backup_date DATE,
	transmittal_check_date DATE,
	comments TEXT,
  last_updated_by VARCHAR(50),
  last_updated_date DATETIME
  );

CREATE UNIQUE INDEX decom_workflow_ndx ON decom_workflow (ship_fk);

ALTER TABLE decom_workflow ADD ship_contacted_date TEXT;
ALTER TABLE decom_workflow ADD transmittal_recon_date TEXT;

ALTER TABLE decom_workflow ADD hardware_status TEXT;
ALTER TABLE decom_workflow ADD hardware_status_notes TEXT;
ALTER TABLE decom_workflow ADD laptop_reset_date TEXT;

DROP VIEW decom_workflow_vw;
CREATE VIEW decom_workflow_vw AS
SELECT 	DISTINCT w.decom_workflow_pk, w.ship_fk, s.uic, s.ship_name, s.homeport, s.rsupply, s.type, s.hull, s.tycom, s.tycom_display,
				c.computer_name, c.laptop_tag, c.scanner_tag,
				w.system_received_date,		strftime('%m/%d/%Y', w.system_received_date) AS system_received_date_fmt,
				w.decom_date,							strftime('%m/%d/%Y', w.decom_date) AS decom_date_fmt,
				w.ship_contacted_date,		strftime('%m/%d/%Y', w.ship_contacted_date) AS ship_contacted_date_fmt,
				w.system_returned_date,		strftime('%m/%d/%Y', w.system_returned_date) AS system_returned_date_fmt,
				w.backup_date,						strftime('%m/%d/%Y', w.backup_date) AS backup_date_fmt,
				w.transmittal_recon_date,	strftime('%m/%d/%Y', w.transmittal_recon_date) AS transmittal_recon_date_fmt,
				w.transmittal_check_date,	strftime('%m/%d/%Y', w.transmittal_check_date) AS transmittal_check_date_fmt,
				w.hardware_status, w.hardware_status_notes,
				w.laptop_reset_date,	strftime('%m/%d/%Y', w.laptop_reset_date) AS laptop_reset_date_fmt,
				w.comments, w.last_updated_by, w.last_updated_date
FROM decom_workflow w INNER JOIN ship_vw s ON w.ship_fk = s.ship_pk
											LEFT OUTER JOIN configured_system_vw c ON s.ship_pk = c.ship_pk;
