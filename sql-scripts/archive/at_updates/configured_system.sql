DROP TABLE configured_system;
DROP VIEW configured_system_vw;

CREATE TABLE configured_system (
  configured_system_pk          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  laptop_fk                     INTEGER,
  scanner_fk                    INTEGER,
  kofax_license_fk              INTEGER,
  ms_office_license_fk          INTEGER,
  uic                           VARCHAR(5),
  facet_version                 VARCHAR(50),
  facet_version_history         TEXT,
  kofax_product_name            VARCHAR(50),
  kofax_version                 VARCHAR(50),
  kofax_version_history         TEXT,
  access_version                VARCHAR(50),
  access_version_history        TEXT,
  documentation_version         VARCHAR(50),
  documentation_version_history TEXT,
  notes                         TEXT,
  last_updated_by               VARCHAR(75),
  last_updated_date             DATETIME
);

CREATE UNIQUE INDEX configured_system_laptop_fk_ndx ON configured_system (laptop_fk);
CREATE UNIQUE INDEX configured_system_scanner_fk_ndx ON configured_system (scanner_fk);
CREATE UNIQUE INDEX configured_system_kofax_license_fk_ndx ON configured_system (kofax_license_fk);


CREATE INDEX configured_system_ms_office_license_fk_ndx ON configured_system (ms_office_license_fk);
CREATE INDEX configured_system_uic_ndx ON configured_system (uic);

CREATE INDEX configured_system_ndx_is_prepped_ind ON configured_system (is_prepped_ind);

CREATE TABLE configured_system_file (
  configured_system_file_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  configured_system_fk INTEGER NOT NULL,
  file_fk INTEGER NOT NULL
  );

CREATE UNIQUE INDEX configured_system_file_ndx ON configured_system_file (configured_system_fk, file_fk);

CREATE VIEW configured_system_file_vw AS
SELECT s.configured_system_file_pk, s.configured_system_fk, s.file_fk, f.filename, f.extension, f.content_type, f.filesize, f.uploaded_by, f.uploaded_date
FROM configured_system_file s INNER JOIN file_info f ON s.file_fk = f.file_pk;

CREATE TRIGGER delete_configured_system_file BEFORE DELETE ON configured_system_file
BEGIN
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.file_fk;
END;


ALTER TABLE configured_system ADD is_prepped_ind VARCHAR(1);

ALTER TABLE configured_system ADD dummy_database_version VARCHAR(50);

ALTER TABLE configured_system ADD network_adapter TEXT;

ALTER TABLE configured_system ADD admin_password TEXT;


ALTER TABLE configured_system ADD dms_version TEXT;
ALTER TABLE configured_system ADD s2_closure_date DATE;
ALTER TABLE configured_system ADD fuel_closure_date DATE;

ALTER TABLE configured_system ADD multi_ship_ind TEXT;
ALTER TABLE configured_system ADD multi_ship_uic_list TEXT;


ALTER TABLE configured_system ADD nwcf_ind TEXT;
ALTER TABLE configured_system ADD contract_number TEXT;


ALTER TABLE configured_system ADD hardware_file_fk INTEGER;
ALTER TABLE configured_system ADD training_file_fk INTEGER;
ALTER TABLE configured_system ADD laptop1_file_fk INTEGER;
ALTER TABLE configured_system ADD laptop2_file_fk INTEGER;

CREATE UNIQUE INDEX configured_system_file1_ndx ON configured_system (hardware_file_fk);
CREATE UNIQUE INDEX configured_system_file2_ndx ON configured_system (training_file_fk);
CREATE UNIQUE INDEX configured_system_file3_ndx ON configured_system (laptop1_file_fk);
CREATE UNIQUE INDEX configured_system_file4_ndx ON configured_system (laptop2_file_fk);


ALTER TABLE configured_system ADD post_install_file_fk INTEGER;

CREATE UNIQUE INDEX configured_system_file5_ndx ON configured_system (post_install_file_fk);


--2016.10.29 ATT :: Adding VRS License
ALTER TABLE configured_system ADD vrs_license_fk INTEGER;

CREATE INDEX configured_system_vrs_license_fk_ndx ON configured_system (vrs_license_fk);

--2017.06.06 ATT :: Adding 1348 no location and 1348 no class ind fields
ALTER TABLE configured_system ADD form_1348_no_location_ind TEXT;
ALTER TABLE configured_system ADD form_1348_no_class_ind TEXT;


DROP VIEW configured_system_vw;
CREATE VIEW configured_system_vw AS
SELECT y.configured_system_pk,
       y.laptop_fk, l.computer_name, l.tag AS laptop_tag, l.product_name AS laptop_product_name, l.serial_number AS laptop_serial_number, l.status AS laptop_status, l.status_notes AS laptop_status_notes,
       y.facet_version, y.facet_version_history, y.dummy_database_version,
       y.scanner_fk, s.tag AS scanner_tag, s.product_name AS scanner_product_name, s.serial_number AS scanner_serial_number, s.status AS scanner_status, s.status_notes AS scanner_status_notes,
       y.kofax_license_fk, y.kofax_product_name, k.license_key AS kofax_license_key, k.product_code AS kofax_product_code, y.kofax_version, y.kofax_version_history,
       y.vrs_license_fk, v.license_key AS vrs_license_key, v.product_code AS vrs_product_code,
       y.ms_office_license_fk, m.product_name AS ms_office_product_name, m.license_key AS ms_office_license_key, y.access_version, y.access_version_history,
       y.uic, h.ship_pk, h.ship_name, h.type, h.hull, h.rsupply, h.homeport, h.service_code, h.tycom, h.tycom_display, d.decom_date, strftime('%m/%d/%Y', d.decom_date) AS decom_date_fmt,
       y.dms_version,
       y.s2_closure_date, strftime('%m/%d/%Y', y.s2_closure_date) AS s2_closure_date_fmt,
       y.fuel_closure_date, strftime('%m/%d/%Y', y.fuel_closure_date) AS fuel_closure_date_fmt,
       y.documentation_version, y.documentation_version_history, y.ghost_version, y.is_prepped_ind, y.network_adapter, y.admin_password, y.notes,
       y.multi_ship_ind, y.multi_ship_uic_list, y.nwcf_ind, y.contract_number,
       y.hardware_file_fk, f1.extension AS hardware_file_extension, f1.uploaded_date AS hardware_file_uploaded_date,
       y.training_file_fk, f2.extension AS training_file_extension, f2.uploaded_date AS training_file_uploaded_date,
       y.laptop1_file_fk, f3.extension AS laptop1_file_extension, f3.uploaded_date AS laptop1_file_uploaded_date,
       y.laptop2_file_fk, f4.extension AS laptop2_file_extension, f4.uploaded_date AS laptop2_file_uploaded_date,
       y.post_install_file_fk, f5.extension AS post_install_file_extension, f5.uploaded_date AS post_install_file_uploaded_date,
       y.form_1348_no_location_ind, y.form_1348_no_class_ind,
       y.last_updated_by, y.last_updated_date
FROM configured_system y LEFT OUTER JOIN laptop l ON l.laptop_pk = y.laptop_fk
                         LEFT OUTER JOIN scanner s ON s.scanner_pk = y.scanner_fk
                         LEFT OUTER JOIN kofax_license k ON k.kofax_license_pk = y.kofax_license_fk
                         LEFT OUTER JOIN vrs_license v ON v.vrs_license_pk = y.vrs_license_fk
                         LEFT OUTER JOIN ms_office_license m ON m.ms_office_license_pk = y.ms_office_license_fk
                         LEFT OUTER JOIN ship_vw h ON h.uic = y.uic
                         LEFT OUTER JOIN decom_workflow d ON d.ship_fk = h.ship_pk
                         LEFT OUTER JOIN file_info f1 ON y.hardware_file_fk = f1.file_pk
                         LEFT OUTER JOIN file_info f2 ON y.training_file_fk = f2.file_pk
                         LEFT OUTER JOIN file_info f3 ON y.laptop1_file_fk = f3.file_pk
                         LEFT OUTER JOIN file_info f4 ON y.laptop2_file_fk = f4.file_pk
                         LEFT OUTER JOIN file_info f5 ON y.post_install_file_fk = f5.file_pk;

DROP VIEW configured_system_transmittal_vw;
CREATE VIEW configured_system_transmittal_vw AS
SELECT c.configured_system_pk,
       c.laptop_fk, c.computer_name, c.laptop_tag, c.laptop_product_name, c.laptop_serial_number, c.laptop_status, c.laptop_status_notes,
       c.facet_version, c_f.sort_order AS facet_version_order, c_f.is_curr AS facet_version_is_curr,
       c.facet_version_history, c.dummy_database_version,
       c.scanner_fk, c.scanner_tag, c.scanner_product_name, c.scanner_serial_number, c.scanner_status, c.scanner_status_notes,
       c.kofax_license_fk, c.kofax_product_name, c.kofax_license_key, c.kofax_product_code, c.kofax_version, c.kofax_version_history,
       c.vrs_license_fk, c.vrs_license_key, c.vrs_product_code,
       c.ms_office_license_fk, c.ms_office_product_name, c.ms_office_license_key, c.access_version, c.access_version_history,
       c.uic, c.ship_pk, c.ship_name, c.type, c.hull, c.rsupply, c.homeport, c.service_code, c.tycom, c.tycom_display, c.decom_date, c.decom_date_fmt,
       c.dms_version,
       c.s2_closure_date, c.s2_closure_date_fmt,
       c.fuel_closure_date, c.fuel_closure_date_fmt,
       c.documentation_version, c.documentation_version_history, c.ghost_version, c.is_prepped_ind, c.network_adapter, c.admin_password, c.notes,
       t.last_transmittal_num,
       t.transmittal_facet_version, t_f.sort_order AS transmittal_facet_version_order, t_f.is_curr AS transmittal_facet_version_is_curr,
       t.last_upload_date, strftime('%m/%d/%Y', t.last_upload_date) AS last_upload_date_fmt,
       t.form_1348_upload_date, strftime('%m/%d/%Y', t.form_1348_upload_date) AS form_1348_upload_date_fmt,
       t.form_1149_upload_date, strftime('%m/%d/%Y', t.form_1149_upload_date) AS form_1149_upload_date_fmt,
       t.food_approval_upload_date, strftime('%m/%d/%Y', t.food_approval_upload_date) AS food_approval_upload_date_fmt,
       t.food_receipt_upload_date, strftime('%m/%d/%Y', t.food_receipt_upload_date) AS food_receipt_upload_date_fmt,
       t.pcard_admin_upload_date, strftime('%m/%d/%Y', t.pcard_admin_upload_date) AS pcard_admin_upload_date_fmt,
       t.pcard_invoice_upload_date, strftime('%m/%d/%Y', t.pcard_invoice_upload_date) AS pcard_invoice_upload_date_fmt,
       t.price_change_upload_date, strftime('%m/%d/%Y', t.price_change_upload_date) AS price_change_upload_date_fmt,
       t.sfoedl_upload_date, strftime('%m/%d/%Y', t.sfoedl_upload_date) AS sfoedl_upload_date_fmt,
       t.uol_upload_date, strftime('%m/%d/%Y', t.uol_upload_date) AS uol_upload_date_fmt,
       c.multi_ship_ind, c.multi_ship_uic_list, nwcf_ind, contract_number,
       c.hardware_file_uploaded_date, c.training_file_uploaded_date, c.laptop1_file_uploaded_date, c.laptop2_file_uploaded_date, c.post_install_file_uploaded_date,
       c.form_1348_no_location_ind, c.form_1348_no_class_ind,
       c.last_updated_by, c.last_updated_date
FROM configured_system_vw c LEFT OUTER JOIN transmittal_summary_vw t ON c.computer_name = t.computer_name
                            LEFT OUTER JOIN facet_version c_f ON c.facet_version = c_f.facet_version
                            LEFT OUTER JOIN facet_version t_f ON t.transmittal_facet_version = t_f.facet_version;

select facet_version, facet_version_order, facet_version_is_curr, transmittal_facet_version, transmittal_facet_version_order, transmittal_facet_version_is_curr from configured_system_transmittal_vw;

CREATE TABLE configured_system_loc_hist (
  configured_system_loc_hist_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  configured_system_fk INTEGER NOT NULL,
  location VARCHAR(25) NOT NULL,
  location_date DATE,
  reason TEXT,
  created_by VARCHAR(75) NOT NULL,
  created_date DATETIME NOT NULL
  );

CREATE TRIGGER update_configured_system_hardware_file BEFORE UPDATE ON configured_system
WHEN old.hardware_file_fk <> new.hardware_file_fk OR (old.hardware_file_fk IS NOT NULL AND new.hardware_file_fk IS NULL)
BEGIN
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.hardware_file_fk;
END;

CREATE TRIGGER update_configured_system_training_file BEFORE UPDATE ON configured_system
WHEN old.training_file_fk <> new.training_file_fk OR (old.training_file_fk IS NOT NULL AND new.training_file_fk IS NULL)
BEGIN
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.training_file_fk;
END;

CREATE TRIGGER update_configured_system_laptop1_file BEFORE UPDATE ON configured_system
WHEN old.laptop1_file_fk <> new.laptop1_file_fk OR (old.laptop1_file_fk IS NOT NULL AND new.laptop1_file_fk IS NULL)
BEGIN
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.laptop1_file_fk;
END;

CREATE TRIGGER update_configured_system_laptop2_file BEFORE UPDATE ON configured_system
WHEN old.laptop2_file_fk <> new.laptop2_file_fk OR (old.laptop2_file_fk IS NOT NULL AND new.laptop2_file_fk IS NULL)
BEGIN
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.laptop2_file_fk;
END;

CREATE TRIGGER update_configured_system_post_install_file BEFORE UPDATE ON configured_system
WHEN old.post_install_file_fk <> new.post_install_file_fk OR (old.post_install_file_fk IS NOT NULL AND new.post_install_file_fk IS NULL)
BEGIN
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.post_install_file_fk;
END;

DROP TRIGGER delete_configured_system;
CREATE TRIGGER delete_configured_system BEFORE DELETE ON configured_system
BEGIN
  DELETE FROM configured_system_loc_hist WHERE configured_system_fk = old.configured_system_pk;
  DELETE FROM configured_system_file WHERE configured_system_fk = old.configured_system_pk;

  UPDATE file_info SET deleted = 1 WHERE file_pk = old.hardware_file_fk;
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.training_file_fk;
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.laptop1_file_fk;
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.laptop2_file_fk;
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.post_install_file_fk;
END;


CREATE TABLE system_variables (
  system_variables_pk          INTEGER PRIMARY KEY NOT NULL,
  facet_version                 TEXT,
  last_updated_by               TEXT,
  last_updated_date             DATETIME
);

CREATE UNIQUE INDEX system_variables_pk_ndx ON system_variables (system_variables_pk);
