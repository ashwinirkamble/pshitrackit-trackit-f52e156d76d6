DROP TABLE task;
CREATE TABLE task (
task_pk           INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
project_fk        INTEGER,
title             VARCHAR(75) NOT NULL,
description       TEXT,
source            VARCHAR(25),
category          VARCHAR(25),
status            VARCHAR(25) NOT NULL,
priority          VARCHAR(25),
uic               VARCHAR(5),
person_assigned   VARCHAR(75),
is_internal       VARCHAR(1),
notes             TEXT,
created_by        VARCHAR(75),
created_date      DATE,
follow_up_date    DATE,
due_date          DATE,
completed_date    DATE,
staff_meeting_ind VARCHAR(1),
client_meeting_ind VARCHAR(1),
last_updated_by   VARCHAR(75),
last_updated_date DATETIME
);

CREATE TABLE sub_task (
sub_task_pk           INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
task_fk       INTEGER,
description       TEXT NOT NULL,
status            VARCHAR(25),
person_assigned   VARCHAR(75),
due_date    DATE,
completed_date    DATE
);

CREATE TRIGGER delete_task BEFORE DELETE ON task
BEGIN
  DELETE FROM sub_task WHERE task_fk = old.task_pk;
END;


ALTER TABLE task ADD sub_tasks TEXT;

ALTER TABLE task ADD effort_area VARCHAR(25);
ALTER TABLE task ADD loe VARCHAR(10);
ALTER TABLE task ADD effort_type VARCHAR(25);
ALTER TABLE task ADD quarter_year VARCHAR(6);
ALTER TABLE task ADD doc_notes TEXT;
ALTER TABLE task ADD version_included TEXT;

ALTER TABLE task ADD is_client_approved TEXT;
ALTER TABLE task ADD is_pshi_approved TEXT;
ALTER TABLE task ADD recommendation TEXT;

UPDATE task SET quarter_year = 'Sustainment' WHERE quarter_year = '2015Q1';

ALTER TABLE task ADD client_priority TEXT;

ALTER TABLE task ADD contract_number TEXT;

ALTER TABLE task ADD doc_updated_ind TEXT;
ALTER TABLE task ADD deployed_date DATE;

DROP VIEW task_vw;
CREATE VIEW task_vw AS
SELECT t.task_pk, t.project_fk, t.title, t.description, t.sub_tasks, t.source, t.category, t.status, t.priority,
      t.uic, s.ship_name, s.ship_name, s.type, s.hull, s.homeport,
      t.person_assigned, t.is_internal, t.notes, t.created_by,
      t.created_date, strftime('%m/%d/%Y', t.created_date) AS created_date_fmt,
      t.follow_up_date, strftime('%m/%d/%Y', t.follow_up_date) AS follow_up_date_fmt,
      t.due_date, strftime('%m/%d/%Y', t.due_date) AS due_date_fmt,
      t.completed_date, strftime('%m/%d/%Y', t.completed_date) AS completed_date_fmt,
      t.staff_meeting_ind, t.client_meeting_ind,
      t.effort_area, t.loe, t.effort_type, t.quarter_year, t.doc_notes, t.version_included,
      t.is_client_approved, t.client_priority, t.is_pshi_approved, t.recommendation, t.contract_number,
      t.doc_updated_ind,
      t.deployed_date, strftime('%m/%d/%Y', t.deployed_date) AS deployed_date_fmt,
      t.last_updated_by, t.last_updated_date, strftime('%m/%d/%Y', t.last_updated_date) AS last_updated_date_fmt
FROM task t LEFT OUTER JOIN ship_vw s ON t.uic = s.uic;

UPDATE task set effort_area = 'FACET DB, Kofax' WHERE effort_area = 'MS Access & Kofax';
UPDATE task set effort_area = 'FACET DB' WHERE effort_area = 'MS Access';

ALTER TABLE sub_task ADD sub_task_id INTEGER;

UPDATE sub_task set sub_task_id = sub_task_pk;