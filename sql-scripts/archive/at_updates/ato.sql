DROP VIEW ato_summary_vw;
DROP TABLE ato;

CREATE TABLE ato (
	ato_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	project_fk INTEGER NOT NULL,
	ato_date DATE NOT NULL,
	comments TEXT,
	opened_date DATE NOT NULL,
  last_updated_by TEXT NOT NULL,
  last_updated_date DATETIME NOT NULL
  );

CREATE INDEX ato_project_fk_ndx ON issue (project_fk);


CREATE TRIGGER delete_ato BEFORE DELETE ON ato
BEGIN
	DELETE FROM issue WHERE ato_fk = old.ato_pk;
END;

DROP VIEW ato_summary_vw;
CREATE VIEW ato_summary_vw AS
SELECT a.ato_pk, a.project_fk,
				a.ato_date, strftime('%m/%d/%Y', a.ato_date) AS ato_date_fmt, strftime('%Y%m%d', a.ato_date) AS ato_filename,
				a.comments,
				a.opened_date, strftime('%m/%d/%Y', a.opened_date) AS opened_date_fmt,
				COUNT(DISTINCT issue_pk) AS total_cnt,
				SUM(CASE WHEN i.status = '6 - Closed (Successful)' THEN 1 ELSE 0 END) AS applied_cnt,
				a.last_updated_by, a.last_updated_date, strftime('%m/%d/%Y %H:%M:%S', a.last_updated_date) AS last_updated_date_fmt
	FROM ato a LEFT OUTER JOIN issue i ON a.ato_pk = i.ato_fk
 GROUP BY a.ato_pk, a.project_fk, a.ato_date, a.last_updated_by, a.last_updated_date;

DROP VIEW ato_detail_vw;
CREATE VIEW ato_detail_vw AS
SELECT a.ato_pk, a.project_fk,
				a.ato_date, strftime('%m/%d/%Y', a.ato_date) AS ato_date_fmt, strftime('%Y%m%d', a.ato_date) AS ato_filename,
				a.comments,
				a.opened_date, strftime('%m/%d/%Y', a.opened_date) AS opened_date_fmt,
				i.issue_pk, i.ship_fk, i.uic, i.ship_name, i.type, i.hull, i.tycom, i.homeport, i.rsupply, i.status, i.opened_date, i.closed_date, i.closed_date_fmt
	FROM ato a INNER JOIN issue_vw i ON a.ato_pk = i.ato_fk;
