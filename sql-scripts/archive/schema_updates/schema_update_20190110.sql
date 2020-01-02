
-- merging "poc" with "ship_poc"
ALTER TABLE poc ADD work_number_ext INTEGER;
ALTER TABLE poc ADD ship_fk INTEGER;
ALTER TABLE poc ADD is_primary_poc TINYINT DEFAULT 0;
ALTER TABLE poc ADD alt_email VARCHAR(255);
ALTER TABLE poc ADD dept VARCHAR(50);

INSERT INTO poc (project_fk, ship_fk, last_name, first_name, organization, title, rank, email, work_number,
fax_number, cell_number, last_updated_by, last_updated_date, dept, alt_email, is_primary_poc)
SELECT
  1 AS project_fk,
  ship_fk,
  last_name,
  first_name,
  'null' AS organization,
  title,
  rank,
  email,
  work_number,
  fax_number,
  cell_number,
  last_updated_by,
  last_updated_date,
  dept,
  alt_email,
  CASE WHEN is_primary_poc = 'Y' THEN 1 ELSE 0 END AS is_primary_poc
FROM ship_poc;

INSERT INTO poc (project_fk, organization_fk, last_name, first_name, organization, title, email, work_number, work_number_ext,
fax_number, cell_number, last_updated_by, last_updated_date)
SELECT
  1 AS project_fk,
  1 AS organization_fk,
  last_name,
  first_name,
  'null' AS organization,
  title,
  email,
  work_number,
  quick_dial AS work_number_ext,
  fax_number,
  cell_number,
  last_updated_by,
  last_updated_date
FROM users;

ALTER TABLE organization ADD project_fk INTEGER DEFAULT 1;
