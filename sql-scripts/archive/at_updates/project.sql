CREATE TABLE project (
project_pk 				INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
project_name			VARCHAR(75) NOT NULL,
description				TEXT,
customer					VARCHAR(75) NOT NULL
);

DROP VIEW project_vw;

CREATE VIEW project_vw AS
SELECT p.project_pk, p.project_name, p.description, p.customer, SUM(CASE WHEN t.status <> 'Completed' THEN 1 ELSE 0 END) AS current_task_cnt, SUM(CASE WHEN t.status == 'Completed' THEN 1 ELSE 0 END) AS completed_task_cnt FROM project p LEFT OUTER JOIN task t ON p.project_pk = t.project_fk GROUP BY p.project_pk, p.project_name, p.description, p.customer;

SELECT count(1) FROM project;

CREATE TRIGGER delete_project BEFORE DELETE ON project
BEGIN
	DELETE FROM task WHERE project_fk = old.project_pk;
END;

ALTER TABLE project ADD created_by INTEGER;

CREATE TRIGGER insert_project AFTER INSERT ON project WHEN new.created_by IS NOT NULL
BEGIN
	INSERT INTO user_project (user_fk, project_fk, last_updated_by) VALUES (new.created_by, new.project_pk, new.created_by);
END;

