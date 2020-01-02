CREATE TABLE contract (
  contract_pk INTEGER PRIMARY KEY NOT NULL,
  contract_number VARCHAR(255) NOT NULL UNIQUE,
  name VARCHAR(255),
  notes TEXT,
  organization_fk INTEGER,
  start_date DATE,
  end_date DATE,
  created_by_fk INTEGER NOT NULL,
  created_date DATETIME NOT NULL,
  last_updated_by_fk INTEGER NOT NULL,
  last_updated_date DATETIME NOT NULL,
  FOREIGN KEY (organization_fk) REFERENCES organization(organization_pk)
);

INSERT INTO contract (contract_number, created_by_fk, created_date, last_updated_by_fk, last_updated_date) 
VALUES
('N00189-15-P-1314',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00406-18-P-1169',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00604-12-C-3042',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00604-13-P-3381',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00604-14-P-3431',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00604-14-P-3497',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00604-15-P-3125',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00604-15-P-3368',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00604-16-P-3377',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00604-17-P-4114',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N00604-18-P-4038',24,datetime('now', 'localtime'),24,datetime('now', 'localtime')),
('N61331-18-C-0005',24,datetime('now', 'localtime'),24,datetime('now', 'localtime'))
;
