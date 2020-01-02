CREATE TABLE file_info (
	file_pk				INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	filename			VARCHAR(75) NOT NULL,
	extension     VARCHAR(4),
	content_type	VARCHAR(25) NOT NULL,
	filesize			INTEGER NOT NULL,
	deleted				INTEGER NOT NULL DEFAULT 0,
	uploaded_by		VARCHAR(75) NOT NULL,
	uploaded_date DATETIME NOT NULL
);

CREATE TABLE task_file (
	task_file_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	task_fk      INTEGER,
	file_fk      INTEGER
);

CREATE VIEW task_file_vw AS
SELECT t.task_file_pk, t.task_fk, t.file_fk, f.filename, f.extension, f.content_type, f.filesize, f.uploaded_by, f.uploaded_date
FROM task_file t INNER JOIN file_info f ON t.file_fk = f.file_pk;

CREATE TRIGGER delete_task_file BEFORE DELETE ON task_file
BEGIN
	UPDATE file_info SET deleted = 1 WHERE file_pk = old.file_fk;
END;

DROP TRIGGER delete_task;

CREATE TRIGGER delete_task BEFORE DELETE ON task
BEGIN
	DELETE FROM sub_task WHERE task_fk = old.task_pk;
	DELETE FROM task_file WHERE task_fk = old.task_pk;
END;
