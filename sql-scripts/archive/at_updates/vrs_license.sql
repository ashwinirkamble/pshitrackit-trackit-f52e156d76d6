--2016.10.29 ATT :: Running VRS License sql statements (compiled from other text files)
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

ALTER TABLE configured_system ADD vrs_license_fk INTEGER;

CREATE INDEX configured_system_vrs_license_fk_ndx ON configured_system (vrs_license_fk);

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

