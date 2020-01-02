
--Change from depending on "uic" to foreign key.
ALTER TABLE configured_system ADD ship_fk INTEGER;

--Based on the UIC, we can set the foreign key values to the column.
INSERT OR REPLACE INTO configured_system
SELECT configured_system_pk,laptop_fk,scanner_fk,kofax_license_fk,ms_office_license_fk,ship.uic,
facet_version,facet_version_history,kofax_product_name,kofax_version,kofax_version_history,
access_version,access_version_history,documentation_version,documentation_version_history,notes,
last_updated_by,last_updated_date,ghost_version,is_prepped_ind,dummy_database_version,
network_adapter,admin_password,dms_version,s2_closure_date,fuel_closure_date,
multi_ship_ind,multi_ship_uic_list,nwcf_ind,contract_number,hardware_file_fk,
training_file_fk,laptop1_file_fk,laptop2_file_fk,post_install_file_fk,vrs_license_fk,
form_1348_no_location_ind,form_1348_no_class_ind,os_version,ship_pk AS ship_fk
FROM configured_system JOIN ship
ON ship.uic = configured_system.uic


-- Update view to support the new "ship_fk" column
DROP VIEW configured_system_vw;
DROP VIEW laptop_vw;
DROP VIEW scanner_vw;
DROP VIEW vrs_license_vw;
DROP VIEW kofax_license_vw;

CREATE VIEW configured_system_vw AS
SELECT cs.configured_system_pk,
  cs.laptop_fk,
  l.computer_name,
  l.tag AS laptop_tag,
  l.product_name AS laptop_product_name,
  l.serial_number AS laptop_serial_number,
  l.status AS laptop_status,
  l.status_notes AS laptop_status_notes,
  cs.os_version,
  cs.facet_version,
  cs.facet_version_history,
  cs.dummy_database_version,
  cs.scanner_fk,
  s.tag AS scanner_tag,
  s.product_name AS scanner_product_name,
  s.serial_number AS scanner_serial_number,
  s.status AS scanner_status,
  s.status_notes AS scanner_status_notes,
  cs.kofax_license_fk,
  cs.kofax_product_name,
  k.license_key AS kofax_license_key,
  k.product_code AS kofax_product_code,
  cs.kofax_version,
  cs.kofax_version_history,
  cs.vrs_license_fk,
  v.license_key AS vrs_license_key,
  v.product_code AS vrs_product_code,
  cs.ms_office_license_fk,
  m.product_name AS ms_office_product_name,
  m.license_key AS ms_office_license_key,
  cs.access_version,
  cs.access_version_history,
  h.ship_pk,
  cs.ship_fk,
  h.uic,
  h.ship_name,
  h.type,
  h.hull,
  h.rsupply,
  h.homeport,
  h.service_code,
  h.tycom,
  h.tycom_display,
  d.decom_date,
  strftime('%m/%d/%Y', d.decom_date) AS decom_date_fmt,
  cs.dms_version,
  cs.s2_closure_date,
  strftime('%m/%d/%Y', cs.s2_closure_date) AS s2_closure_date_fmt,
  cs.fuel_closure_date,
  strftime('%m/%d/%Y', cs.fuel_closure_date) AS fuel_closure_date_fmt,
  cs.documentation_version,
  cs.documentation_version_history,
  cs.ghost_version,
  cs.is_prepped_ind,
  cs.network_adapter,
  cs.admin_password,
  cs.notes,
  cs.multi_ship_ind,
  cs.multi_ship_uic_list,
  cs.nwcf_ind,
  cs.contract_number,
  cs.hardware_file_fk,
  f1.extension AS hardware_file_extension,
  f1.uploaded_date AS hardware_file_uploaded_date,
  cs.training_file_fk,
  f2.extension AS training_file_extension,
  f2.uploaded_date AS training_file_uploaded_date,
  cs.laptop1_file_fk,
  f3.extension AS laptop1_file_extension,
  f3.uploaded_date AS laptop1_file_uploaded_date,
  cs.laptop2_file_fk,
  f4.extension AS laptop2_file_extension,
  f4.uploaded_date AS laptop2_file_uploaded_date,
  cs.post_install_file_fk,
  f5.extension AS post_install_file_extension,
  f5.uploaded_date AS post_install_file_uploaded_date,
  cs.form_1348_no_location_ind,
  cs.form_1348_no_class_ind,
  cs.last_updated_by,
  cs.last_updated_date
FROM configured_system cs
LEFT OUTER JOIN laptop l ON l.laptop_pk = cs.laptop_fk
LEFT OUTER JOIN scanner s ON s.scanner_pk = cs.scanner_fk
LEFT OUTER JOIN kofax_license k ON k.kofax_license_pk = cs.kofax_license_fk
LEFT OUTER JOIN vrs_license v ON v.vrs_license_pk = cs.vrs_license_fk
LEFT OUTER JOIN ms_office_license m ON m.ms_office_license_pk = cs.ms_office_license_fk
LEFT OUTER JOIN ship_vw h ON h.ship_pk = cs.ship_fk
LEFT OUTER JOIN decom_workflow d ON d.ship_fk = h.ship_pk
LEFT OUTER JOIN file_info f1 ON cs.hardware_file_fk = f1.file_pk
LEFT OUTER JOIN file_info f2 ON cs.training_file_fk = f2.file_pk
LEFT OUTER JOIN file_info f3 ON cs.laptop1_file_fk = f3.file_pk
LEFT OUTER JOIN file_info f4 ON cs.laptop2_file_fk = f4.file_pk
LEFT OUTER JOIN file_info f5 ON cs.post_install_file_fk = f5.file_pk;


CREATE VIEW laptop_vw AS
SELECT
  l.laptop_pk, l.product_name, l.computer_name, l.tag, l.model_number, l.mac_address, l.serial_number,
  l.origin, l.received_date, l.prepped_date, l.notes, l.status, l.status_notes, l.customer, l.contract_number,
  s.uic, s.ship_name, s.type, s.hull, s.homeport, l.last_updated_by, l.last_updated_date
FROM laptop l
LEFT OUTER JOIN configured_system cs
  ON l.laptop_pk = cs.laptop_fk
LEFT OUTER JOIN ship s
  ON s.ship_pk = cs.ship_fk;

CREATE VIEW scanner_vw AS
SELECT
  sc.scanner_pk, sc.product_name, sc.tag, sc.model_number, sc.serial_number, sc.origin, sc.received_date,
  sc.prepped_date, sc.notes, sc.status, sc.status_notes, sc.customer, sc.contract_number,
  l.computer_name,
  s.uic,
  s.ship_name, s.type, s.hull, s.homeport,
  sc.last_updated_by, sc.last_updated_date
FROM scanner sc
LEFT OUTER JOIN configured_system cs
  ON sc.scanner_pk = cs.scanner_fk
LEFT OUTER JOIN laptop l
  ON cs.laptop_fk = l.laptop_pk
LEFT OUTER JOIN ship s
  ON cs.ship_fk = s.ship_pk;


CREATE VIEW vrs_license_vw AS
SELECT
  v.vrs_license_pk, v.license_key, v.product_code, v.customer, v.contract_number,
  l.computer_name,
  s.uic, s.ship_name, s.type, s.hull, s.homeport,
  v.received_date, v.notes, v.last_updated_by, v.last_updated_date
FROM vrs_license v
LEFT OUTER JOIN configured_system c
  ON v.vrs_license_pk = c.vrs_license_fk
LEFT OUTER JOIN laptop l
  ON c.laptop_fk = l.laptop_pk
LEFT OUTER JOIN ship s
  ON c.ship_fk = s.ship_pk;


CREATE VIEW kofax_license_vw AS
SELECT
  k.kofax_license_pk, k.license_key, k.product_code, k.customer, k.contract_number,
  l.computer_name,
  s.uic, s.ship_name, s.type, s.hull, s.homeport, k.received_date,
  k.received_date, strftime('%m/%d/%Y', k.received_date) AS received_date_fmt,
  k.license_expiry_date, strftime('%m/%d/%Y', k.license_expiry_date) AS license_expiry_date_fmt,
  k.internal_use_ind, k.notes, k.last_updated_by, k.last_updated_date
FROM kofax_license k
LEFT OUTER JOIN configured_system c
  ON k.kofax_license_pk = c.kofax_license_fk
LEFT OUTER JOIN laptop l
  ON c.laptop_fk = l.laptop_pk
LEFT OUTER JOIN ship s
  ON c.ship_fk = s.ship_pk;
