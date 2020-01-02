CREATE TABLE issue2 (
  issue_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  project_fk INTEGER NOT NULL,
  ship_fk INTEGER,
  title TEXT NOT NULL,
  description TEXT,
  status TEXT NOT NULL,
  priority TEXT,
  category TEXT NOT NULL,
  phase TEXT,
  opened_by TEXT NOT NULL,
  opened_date DATETIME NOT NULL,
  closed_date DATETIME,
  person_assigned TEXT,
  support_visit_date DATE,
  support_visit_loc TEXT,
  support_visit_time INTEGER,
  trainer TEXT,
  resolution TEXT,
  total_time INTEGER,
  created_by TEXT NOT NULL,
  created_date DATETIME NOT NULL,
  last_updated_by TEXT NOT NULL,
  last_updated_date DATETIME NOT NULL
  );

INSERT INTO issue2(issue_pk, project_fk, ship_fk, title, description, status, priority, category, phase, opened_by, opened_date, closed_date, person_assigned, support_visit_date, support_visit_time, trainer, resolution, total_time, created_by, created_date, last_updated_by, last_updated_date)
SELECT issue_pk, project_fk, ship_fk, title, description, status, priority, category, phase, opened_by, opened_date, closed_date, person_assigned, support_visit_date, support_visit_time, trainer, resolution, total_time, created_by, created_date, last_updated_by, last_updated_date FROM issue;

DROP TABLE issue;
ALTER TABLE issue2 RENAME TO issue;

--Run these AFTER data has been imported
CREATE TRIGGER insert_issue AFTER INSERT ON issue
BEGIN
  INSERT INTO issue_comments (issue_fk, comments, created_by, created_date)
  SELECT new.issue_pk, 'Support issue created', new.created_by, new.created_date;
END;

CREATE TRIGGER update_issue AFTER UPDATE ON issue
WHEN old.status <> new.status
BEGIN
    INSERT INTO issue_comments (issue_fk, comments, created_by, created_date)
    SELECT new.issue_pk, 'Status changed to ' || new.status, new.last_updated_by, new.last_updated_date;
END;

CREATE TRIGGER delete_issue BEFORE DELETE ON issue
BEGIN
  DELETE FROM issue_comments WHERE issue_fk = old.issue_pk;
  DELETE FROM issue_file WHERE issue_fk = old.issue_pk;
  DELETE FROM issue_related WHERE issue_fk = old.issue_pk;
END;

ALTER TABLE issue ADD initiated_by TEXT;
ALTER TABLE issue ADD dept TEXT;
ALTER TABLE issue ADD is_email_sent TEXT;
ALTER TABLE issue ADD is_email_responded TEXT;
ALTER TABLE issue ADD is_training_provided TEXT;
ALTER TABLE issue ADD is_training_onsite TEXT;
ALTER TABLE issue ADD ato_fk INTEGER;
CREATE INDEX issue_ato_ndx ON issue (ato_fk);

ALTER TABLE issue ADD auto_close_date DATE;

CREATE INDEX issue_category_ndx ON issue (category);
CREATE INDEX issue_status_ndx ON issue (status);
CREATE INDEX issue_ndx_project_fk ON issue (project_fk);


WHERE s.uic IN (SELECT c.uic FROM configured_system_vw c WHERE c.contract_number = '');



UPDATE issue SET status = '7 - Closed (No Response)', closed_date = auto_close_date, resolution = 'Automatically closed by the system', auto_close_date = null, last_updated_by = 'SYSTEM', last_updated_date = date('now', '-10 hours') WHERE auto_close_date <= date('now', '-10 hours');




UPDATE issue SET ato_fk = 1 WHERE title = 'Monthly ATO Maintenance Release 9/16/2014';
UPDATE issue SET ato_fk = 1 WHERE title = 'Monthly ATO Maintenance Release';
UPDATE issue SET ato_fk = 1 WHERE title = 'ATO Update 20140916';

ATO Update 20140916


UPDATE issue SET ato_fk = 2 WHERE title = 'Monthly ATO updates 10/24/2014';

UPDATE issue SET ato_fk = 3 WHERE title = 'ATO Maintenance Release 12/2014';
UPDATE issue SET ato_fk = 3 WHERE title = 'ATO updates 12/2014';

UPDATE issue SET ato_fk = 3 WHERE title = 'ATO Mainenance Release 12/2014';
UPDATE issue SET ato_fk = 3 WHERE title = 'ATO Maintenace Release 12/2014';
UPDATE issue SET ato_fk = 3 WHERE title = 'ATO Maintenace Release 12/2014';
UPDATE issue SET ato_fk = 3 WHERE title = 'ATO Maintenance Release 12/2014';
UPDATE issue SET ato_fk = 3 WHERE title = 'ATO Maintenance Release 12/2014';
UPDATE issue SET ato_fk = 3 WHERE title = 'ATO maintenance release 12/2014';
UPDATE issue SET ato_fk = 3 WHERE title = 'ATO Maintenance release 12/2014';
UPDATE issue SET ato_fk = 3 WHERE title = 'ATO Maintenance Release 12/2014';



DROP VIEW ship_visit_schedule_vw;
CREATE VIEW ship_visit_schedule_vw AS
SELECT 'SUPPORT' AS category, issue_pk AS category_fk, strftime('%m', support_visit_date) AS month, strftime('%d', support_visit_date) AS day, strftime('%Y', support_visit_date) AS year,
support_visit_date AS visit_date, support_visit_date_fmt AS visit_date_fmt, support_visit_time AS visit_time, trainer,
ship_fk, uic, ship_name, type, hull, IFNULL(support_visit_loc, homeport) AS location, tycom, rsupply from issue_vw WHERE uic IS NOT NULL AND support_visit_date IS NOT NULL
UNION ALL
SELECT 'TRAINING' AS category, training_workflow_pk AS category_fk, strftime('%m', sched_training_date) AS month, strftime('%d', sched_training_date) AS day, strftime('%Y', sched_training_date) AS year,
sched_training_date AS visit_date, sched_training_date_fmt AS visit_date_fmt, sched_training_time AS visit_time, trainer,
ship_fk, uic, ship_name, type, hull, IFNULL(sched_training_loc, homeport) AS location, tycom, rsupply from training_workflow_vw WHERE sched_training_date IS NOT NULL;



UPDATE issue SET category = 'LOGCOP Inactivity' WHERE category = 'No Activity';





SELECT ship_name, type, hull, homeport, COUNT(1) AS issue_cnt, SUM(closed_date IS NULL) AS open_issue_cnt,
SUM(CASE WHEN category = 'LOGCOP' THEN 1 ELSE 0 END) AS logcop_cnt,
SUM(CASE WHEN category = 'FACET DB' THEN 1 ELSE 0 END) AS facet_db_cnt,
SUM(CASE WHEN category = 'Kofax' THEN 1 ELSE 0 END) AS kofax_cnt,
SUM(CASE WHEN category = 'Updates' THEN 1 ELSE 0 END) AS update_cnt,
SUM(CASE WHEN category = 'FACET Laptop' THEN 1 ELSE 0 END) AS facet_laptop_cnt,
SUM(CASE WHEN category = 'Training' THEN 1 ELSE 0 END) AS training_cnt,
SUM(CASE WHEN category = 'Backfile' THEN 1 ELSE 0 END) AS backfile_cnt,
SUM(CASE WHEN category NOT IN ('LOGCOP', 'FACET DB', 'Kofax', 'Updates', 'FACET Laptop', 'Training', 'Backfile') THEN 1 ELSE 0 END) AS other_cnt
FROM issue_vw WHERE project_fk = 1 GROUP BY ship_fk, ship_name, type, hull, homeport ORDER BY issue_cnt DESC, ship_name;

CREATE TABLE issue_comments (
  issue_comments_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  issue_fk INTEGER NOT NULL,
  comments TEXT NOT NULL,
  created_by VARCHAR(75) NOT NULL,
  created_date DATETIME NOT NULL
  );

CREATE TABLE issue_file (
  issue_file_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  issue_fk INTEGER NOT NULL,
  file_fk INTEGER NOT NULL
  );

CREATE TABLE issue_related (
  issue_related_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  issue_fk INTEGER NOT NULL,
  related_issue_fk INTEGER NOT NULL
  );

CREATE INDEX issue_comments_ndx ON issue_comments (issue_fk);
CREATE UNIQUE INDEX issue_file_ndx ON issue_file (issue_fk, file_fk);
CREATE UNIQUE INDEX issue_related_ndx ON issue_related (issue_fk, related_issue_fk);

CREATE VIEW issue_file_vw AS
SELECT i.issue_file_pk, i.issue_fk, i.file_fk, f.filename, f.extension, f.content_type, f.filesize, f.uploaded_by, f.uploaded_date
FROM issue_file i INNER JOIN file_info f ON i.file_fk = f.file_pk;

CREATE TRIGGER delete_issue_file BEFORE DELETE ON issue_file
BEGIN
  UPDATE file_info SET deleted = 1 WHERE file_pk = old.file_fk;
END;



UPDATE issue set status = '4 - Pending Possible Resolution' WHERE status = '5 - Pending Possible Resolution';
UPDATE issue set status = '5 - Closed' WHERE status = '4 - Closed';

UPDATE issue SET category = 'Follow-Up Training' WHERE category = 'Training';
UPDATE issue SET category = 'User Accounts' WHERE category = 'Laptop';
UPDATE issue SET category = 'FACET Update' WHERE category = 'Updates';
UPDATE issue SET category = 'LOGCOP Missing Transmittals' WHERE category = 'Missing Transmittals';
UPDATE issue SET category = '' WHERE category = '';
UPDATE issue SET category = '' WHERE category = '';
UPDATE issue SET category = '' WHERE category = '';
UPDATE issue SET category = '' WHERE category = '';
UPDATE issue SET category = '' WHERE category = '';
UPDATE issue SET category = '' WHERE category = '';
UPDATE issue SET category = '' WHERE category = '';
Missing Transmittals” to “LOGCOP Missing Transmittals”
 “Updates” issues to “FACET System Update”

---

CREATE TABLE issue_category (
	issue_category_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	project_fk INTEGER NOT NULL,
	category TEXT NOT NULL,
	last_updated_by TEXT NOT NULL,
	last_updated_date DATETIME NOT NULL
	);

INSERT INTO issue_category (project_fk, category, last_updated_by, last_updated_date)
SELECT DISTINCT 1 AS project_fk, category, 'SYSTEM' AS last_updated_by, DATETIME('now', '-10 hours') AS last_updated_date FROM issue ORDER BY category;

ALTER TABLE issue ADD issue_category_fk INTEGER NOT NULL DEFAULT -1;
CREATE INDEX issue_category_fk_ndx ON issue (issue_category_fk);

DROP INDEX issue_category_ndx;
CREATE UNIQUE INDEX issue_category_ndx ON issue_category (category);

SELECT DISTINCT issue_category_fk FROM issue;

UPDATE issue SET issue_category_fk = (SELECT c.issue_category_pk FROM issue_category c WHERE c.category = issue.category LIMIT 1);

SELECT DISTINCT issue_category_fk FROM issue;

ALTER TABLE issue ADD priority_reason TEXT;
ALTER TABLE issue ADD support_visit_end_time INTEGER;
ALTER TABLE issue ADD support_visit_reason TEXT;


--UPDATE issue SET category = 'null';


Amanda Crabtree-Loo
Lloyd Sueyoshi
Chivas Nousianen
Corey Kelley
Darby Meyer
Esmil Feliz
Joshua Crabtree
Michael Fernandez
Norman Newson
Rob Hardisty
Shandale Graham
Tiffaney Scott
Miracle Leao
Russell Houlton
Steve Brennen
Other


UPDATE issue SET person_assigned='Amanda Crabtree-Loo' WHERE person_assigned='Amanda Crabtree';
UPDATE issue SET person_assigned='Joshua Crabtree' WHERE person_assigned='Josh';
UPDATE issue SET person_assigned='Joshua Crabtree' WHERE person_assigned='josh';
UPDATE issue SET person_assigned='Joshua Crabtree' WHERE person_assigned='osh';
UPDATE issue SET person_assigned='Joshua Crabtree' WHERE person_assigned='Josh C';
UPDATE issue SET person_assigned='Darby Meyer' WHERE person_assigned='Darby/Shandale';
UPDATE issue SET person_assigned='Chivas Nousianen' WHERE person_assigned='Chivas';
UPDATE issue SET person_assigned='Other' WHERE person_assigned='Ricky/Dan';
UPDATE issue SET person_assigned='Other' WHERE person_assigned='Anthony Tsuhako';
UPDATE issue SET person_assigned='Other' WHERE person_assigned='Michel Tanouye';
UPDATE issue SET person_assigned='Other' WHERE person_assigned='Traci Hardisty';
UPDATE issue SET person_assigned='Other' WHERE person_assigned='Support Team';

UPDATE issue SET trainer='Other' WHERE trainer='1145';
UPDATE issue SET trainer='Other' WHERE trainer='1145 ';
UPDATE issue SET trainer='Other' WHERE trainer='1345';
UPDATE issue SET trainer='Other' WHERE trainer='1700';
UPDATE issue SET trainer='Other' WHERE trainer='Anthony';
UPDATE issue SET trainer='Corey Kelley' WHERE trainer='Core Kelley';
UPDATE issue SET trainer='Corey Kelley' WHERE trainer='Corey';
UPDATE issue SET trainer='Corey Kelley' WHERE trainer='Corey  Kelley';
UPDATE issue SET trainer='Corey Kelley' WHERE trainer='Corey Kelley';
UPDATE issue SET trainer='Corey Kelley' WHERE trainer='Corey Kelly';
UPDATE issue SET trainer='Darby Meyer' WHERE trainer='Darby';
UPDATE issue SET trainer='Esmil Feliz' WHERE trainer='Esmil/Tiffaney';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Josh';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Josh ';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Josh & Mike';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Josh / Mike';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Josh Crabtree';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Josh and Mike';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='josh';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='josh ';
UPDATE issue SET trainer='Michael Fernandez' WHERE trainer='mike ';
UPDATE issue SET trainer='Michael Fernandez' WHERE trainer='mike';
UPDATE issue SET trainer='Michael Fernandez' WHERE trainer='MIke';
UPDATE issue SET trainer='Michael Fernandez' WHERE trainer='MIke ';
UPDATE issue SET trainer='Michael Fernandez' WHERE trainer='Michael';
UPDATE issue SET trainer='Michael Fernandez' WHERE trainer='Mike';
UPDATE issue SET trainer='Michael Fernandez' WHERE trainer='Mike ';
UPDATE issue SET trainer='Michael Fernandez' WHERE trainer='Mike / Josh';
UPDATE issue SET trainer='Michael Fernandez' WHERE trainer='Mike / Josh ';
UPDATE issue SET trainer='Norman Newson' WHERE trainer='Norman';
UPDATE issue SET trainer='Norman Newson' WHERE trainer='Norman/Tiffaney';
UPDATE issue SET trainer='Rob Hardisty' WHERE trainer='Rob/Josh';
UPDATE issue SET trainer='Shandale Graham' WHERE trainer='Shanale';
UPDATE issue SET trainer='Shandale Graham' WHERE trainer='shanale';
UPDATE issue SET trainer='Shandale Graham' WHERE trainer='shandale ';
UPDATE issue SET trainer='Shandale Graham' WHERE trainer='shandale  ';
UPDATE issue SET trainer='Shandale Graham' WHERE trainer='shandale';
UPDATE issue SET trainer='Shandale Graham' WHERE trainer='Shandale';
UPDATE issue SET trainer='Shandale Graham' WHERE trainer='Shandale ';
UPDATE issue SET trainer='Other' WHERE trainer='TENTATITIVE';
UPDATE issue SET trainer='Other' WHERE trainer='TENTATIVE';
UPDATE issue SET trainer='Other' WHERE trainer='pier 13';
UPDATE issue SET trainer='Tiffaney Scott' WHERE trainer='Tiffaney';
UPDATE issue SET trainer='Tiffaney Scott' WHERE trainer='Tiffaney Scott ';
UPDATE issue SET trainer='Tiffaney Scott' WHERE trainer='Tiffaney/Norman';
UPDATE issue SET trainer='Tiffaney Scott' WHERE trainer='Tiffnaey Scott';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Josh / Mike ';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Josh/ Mike';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Josh/ Mike ';
UPDATE issue SET trainer='Joshua Crabtree' WHERE trainer='Joshua';
SELECT DISTINCT trainer FROM issue WHERE trainer IS NOT NULL;

UPDATE issue SET trainer='' WHERE trainer='';
UPDATE issue SET trainer='' WHERE trainer='';
UPDATE issue SET trainer='' WHERE trainer='';
UPDATE issue SET trainer='' WHERE trainer='';
UPDATE issue SET trainer='' WHERE trainer='';
UPDATE issue SET trainer='' WHERE trainer='';
UPDATE issue SET trainer='' WHERE trainer='';

--2016.11.04 Adding new fields
ALTER TABLE issue ADD laptop_issue TEXT;
ALTER TABLE issue ADD scanner_issue TEXT;
ALTER TABLE issue ADD software_issue TEXT;
ALTER TABLE issue ADD support_visit_loc_notes TEXT;

ALTER TABLE issue ADD auto_close_status TEXT;

DROP VIEW issue_vw;
CREATE VIEW issue_vw AS
SELECT i.issue_pk, i.project_fk, i.ship_fk, s.uic, s.ship_name, s.type, s.hull, s.tycom, s.homeport, s.rsupply,
        i.title, i.description, i.status, i.priority, i.priority_reason, i.issue_category_fk, c.category, i.phase,
        i.opened_by, i.opened_date, strftime('%m/%d/%Y', i.opened_date) AS opened_date_fmt,
        i.closed_date, strftime('%m/%d/%Y', i.closed_date) AS closed_date_fmt,
        i.person_assigned, i.resolution, i.total_time, i.created_by, i.created_date, strftime('%m/%d/%Y %H:%M:%S', i.created_date) AS created_date_fmt,
        i.support_visit_date, strftime('%m/%d/%Y', i.support_visit_date) AS support_visit_date_fmt, i.support_visit_time, i.support_visit_end_time,
        i.support_visit_reason, i.trainer, support_visit_loc,
        i.initiated_by, i.dept, i.is_email_sent, i.is_email_responded, is_training_provided, i.is_training_onsite, i.ato_fk,
        i.auto_close_date, strftime('%m/%d/%Y', i.auto_close_date) AS auto_close_date_fmt,
        i.auto_close_status,
        i.laptop_issue, i.scanner_issue, i.software_issue, support_visit_loc_notes,
        i.last_updated_by, i.last_updated_date, strftime('%m/%d/%Y %H:%M:%S', i.last_updated_date) AS last_updated_date_fmt
  FROM issue i LEFT OUTER JOIN issue_category c ON i.issue_category_fk = c.issue_category_pk
               LEFT OUTER JOIN ship s ON i.ship_fk = s.ship_pk;
