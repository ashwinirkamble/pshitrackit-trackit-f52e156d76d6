
CREATE TABLE gov_property (
  gov_property_pk INTEGER PRIMARY KEY NOT NULL,
  date_listed DATETIME NOT NULL,
  national_stock_number VARCHAR(127) NOT NULL,
  id_number VARCHAR(127),
  description TEXT,
  project_contract VARCHAR(127),
  received INTEGER,
  issued INTEGER,
  transferred INTEGER,
  on_hand INTEGER,
  location VARCHAR(127),
  disposition VARCHAR(127),
  created_by_fk INTEGER NOT NULL,
  created_date DATETIME NOT NULL,
  last_updated_by_fk INTEGER NOT NULL,
  last_updated_date DATETIME NOT NULL
);