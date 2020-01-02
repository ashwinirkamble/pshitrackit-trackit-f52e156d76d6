--add unique constraint to ship.ship_name
--add unique constraint to managed_list_item: item_value, managed_list_code, project_fk


-----------------------------------------------------------------------------------------
-- DO NOT RUN UNTIL "Status Reports" feature is completed.
-- add "Notes" column to POC
ALTER TABLE poc ADD notes TEXT;

DROP TABLE status_report;
CREATE TABLE status_report (
  status_report_pk INTEGER PRIMARY KEY NOT NULL,
  project_name VARCHAR(127) NOT NULL,
  status_report_type_code INTEGER NOT NULL,
  contract_fk INTEGER NOT NULL,
  report_start_date DATE NOT NULL,
  report_end_date DATE NOT NULL,
  organization_fk INTEGER NOT NULL,
  contractor_org_fk INTEGER NOT NULL,
  name VARCHAR(127),
  project_manager VARCHAR(127),
  contracting_officer_cotr VARCHAR(127),
  contracting_officer_cor VARCHAR(127),
  objective TEXT,
  summary TEXT,
  created_by_fk INTEGER NOT NULL,
  created_date DATETIME NOT NULL,
  last_updated_by_fk INTEGER NOT NULL,
  last_updated_date DATETIME NOT NULL,
  deleted TINYINT DEFAULT 0,
  FOREIGN KEY (contract_fk) REFERENCES contract(contract_pk),
  FOREIGN KEY (contractor_org_fk) REFERENCES organization(organization_pk),
  FOREIGN KEY (organization_fk) REFERENCES organization(organization_pk)
);

CREATE TABLE status_report_contract_join (
  status_report_contract_join_pk INTEGER PRIMARY KEY NOT NULL,
  status_report_fk INTEGER NOT NULL,
  contract_fk INTEGER NOT NULL,
  FOREIGN KEY (status_report_fk) REFERENCES status_report(status_report_pk),
  FOREIGN KEY (contract_fk) REFERENCES contract(contract_pk),
  UNIQUE(status_report_fk, contract_fk)
);
-----------------------------------------------------------------------------------------



-----------------------------------------------------------------------------------------
-- DO NOT RUN UNTIL...
-- There are existing duplicate serial_number values that prevents this from working.
--ALTER TABLE scanner RENAME TO old_scanner;

--CREATE TABLE scanner (
  scanner_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  product_name VARCHAR(50) NOT NULL,
  tag VARCHAR(50),
  model_number VARCHAR(50),
  serial_number VARCHAR(50),
  origin VARCHAR(50),
  received_date DATE,
  notes TEXT,
  last_updated_by VARCHAR(75),
  last_updated_date DATETIME,
  prepped_date DATE,
  status TEXT,
  status_notes TEXT,
  customer TEXT,
  contract_number TEXT,
  CONSTRAINT scanner_unique_serial_number UNIQUE (serial_number)
--);

--INSERT INTO scanner SELECT * FROM old_scanner;

--DROP TABLE old_scanner;
-- END - DO NOT RUN UNTIL existing duplicate serial numbers are resolved.
-----------------------------------------------------------------------------------------
