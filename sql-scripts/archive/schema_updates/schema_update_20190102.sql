
-- new table for organizations
CREATE TABLE organization (
  organization_pk INTEGER PRIMARY KEY NOT NULL,
  name VARCHAR(127) NOT NULL,
  address_1 VARCHAR(127),
  address_2 VARCHAR(127),
  zip VARCHAR(10),
  city VARCHAR(127),
  state_province VARCHAR(127),
  country VARCHAR(127),
  email VARCHAR(127),
  url VARCHAR(127),
  phone VARCHAR(127),
  fax_number VARCHAR(127),
  notes TEXT,
  primary_poc_fk INTEGER,
  created_by_fk INTEGER NOT NULL,
  created_date DATETIME NOT NULL,
  last_updated_by_fk INTEGER NOT NULL,
  last_updated_date DATETIME NOT NULL,
  deleted TINYINT NOT NULL DEFAULT 0,
  FOREIGN KEY (primary_poc_fk) REFERENCES poc(poc_pk)
);

INSERT INTO organization (name,address_1,address_2,zip,city,state_province,country,email,url,phone,fax_number,notes,
primary_poc_fk,created_by_fk,created_date,last_updated_by_fk,last_updated_date)
VALUES ('Premier Solutions Hi, LLC','745 Fort Street, Suite 800','','96813','Honolulu ','HI','USA','',
'https://www.premiersolutionshi.com','(808) 396-4444',null,null,null,24,datetime('now', 'localtime'),24,datetime('now', 'localtime'))
,('Commander U.S. Pacific Fleet','250 Makalapa Drive Building 251','Second Floor','96860-3131','Pearl Harbor','HI','USA','',
null,null,null,null,null,24,datetime('now', 'localtime'),24,datetime('now', 'localtime'));


INSERT OR REPLACE INTO organization 
SELECT null,organization AS name,null,null,null,null,null,null,null,null,null,null,null,null,24,datetime('now', 'localtime'),24,datetime('now', 'localtime'),0
FROM poc
WHERE organization IS NOT NULL GROUP BY organization

ALTER TABLE poc ADD organization_fk INTEGER;

UPDATE poc SET organization_fk = (
  SELECT organization_pk FROM organization WHERE organization.name = poc.organization
);


INSERT INTO managed_list_item (managed_list_code,item_value,last_updated_by_fk,last_updated_date,hidden)
VALUES
(11,'AL',24,datetime('now', 'localtime'),0)
,(11,'AK',24,datetime('now', 'localtime'),0)
,(11,'AZ',24,datetime('now', 'localtime'),0)
,(11,'AR',24,datetime('now', 'localtime'),0)
,(11,'CA',24,datetime('now', 'localtime'),0)
,(11,'CO',24,datetime('now', 'localtime'),0)
,(11,'CT',24,datetime('now', 'localtime'),0)
,(11,'DE',24,datetime('now', 'localtime'),0)
,(11,'FL',24,datetime('now', 'localtime'),0)
,(11,'GA',24,datetime('now', 'localtime'),0)
,(11,'HI',24,datetime('now', 'localtime'),1)
,(11,'ID',24,datetime('now', 'localtime'),0)
,(11,'IL',24,datetime('now', 'localtime'),0)
,(11,'IN',24,datetime('now', 'localtime'),0)
,(11,'IA',24,datetime('now', 'localtime'),0)
,(11,'KS',24,datetime('now', 'localtime'),0)
,(11,'KY',24,datetime('now', 'localtime'),0)
,(11,'LA',24,datetime('now', 'localtime'),0)
,(11,'ME',24,datetime('now', 'localtime'),0)
,(11,'MD',24,datetime('now', 'localtime'),0)
,(11,'MA',24,datetime('now', 'localtime'),0)
,(11,'MI',24,datetime('now', 'localtime'),0)
,(11,'MN',24,datetime('now', 'localtime'),0)
,(11,'MS',24,datetime('now', 'localtime'),0)
,(11,'MO',24,datetime('now', 'localtime'),0)
,(11,'MT',24,datetime('now', 'localtime'),0)
,(11,'NE',24,datetime('now', 'localtime'),0)
,(11,'NV',24,datetime('now', 'localtime'),0)
,(11,'NH',24,datetime('now', 'localtime'),0)
,(11,'NJ',24,datetime('now', 'localtime'),0)
,(11,'NM',24,datetime('now', 'localtime'),0)
,(11,'NY',24,datetime('now', 'localtime'),0)
,(11,'NC',24,datetime('now', 'localtime'),0)
,(11,'ND',24,datetime('now', 'localtime'),0)
,(11,'OH',24,datetime('now', 'localtime'),0)
,(11,'OK',24,datetime('now', 'localtime'),0)
,(11,'OR',24,datetime('now', 'localtime'),0)
,(11,'PA',24,datetime('now', 'localtime'),0)
,(11,'RI',24,datetime('now', 'localtime'),0)
,(11,'SC',24,datetime('now', 'localtime'),0)
,(11,'SD',24,datetime('now', 'localtime'),0)
,(11,'TN',24,datetime('now', 'localtime'),0)
,(11,'TX',24,datetime('now', 'localtime'),0)
,(11,'UT',24,datetime('now', 'localtime'),0)
,(11,'VT',24,datetime('now', 'localtime'),0)
,(11,'VA',24,datetime('now', 'localtime'),0)
,(11,'WA',24,datetime('now', 'localtime'),0)
,(11,'WV',24,datetime('now', 'localtime'),0)
,(11,'WI',24,datetime('now', 'localtime'),0)
,(11,'WY',24,datetime('now', 'localtime'),0)
,(11,'AS',24,datetime('now', 'localtime'),0)
,(11,'DC',24,datetime('now', 'localtime'),0)
,(11,'FM',24,datetime('now', 'localtime'),0)
,(11,'GU',24,datetime('now', 'localtime'),0)
,(11,'MH',24,datetime('now', 'localtime'),0)
,(11,'MP',24,datetime('now', 'localtime'),0)
,(11,'PW',24,datetime('now', 'localtime'),0)
,(11,'PR',24,datetime('now', 'localtime'),0)
,(11,'VI',24,datetime('now', 'localtime'),0)
;
