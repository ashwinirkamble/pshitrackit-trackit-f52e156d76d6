CREATE TABLE backfile_workflow (
  backfile_workflow_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  ship_fk INTEGER NOT NULL,
  fy14_box_cnt INTEGER,
  fy13_box_cnt INTEGER,
  fy12_box_cnt INTEGER,
  fy11_box_cnt INTEGER,
  fy10_box_cnt INTEGER,
  requested_date DATE,
  received_date DATE,
  scanning_delivered_date DATE,
  fy1314_burned_date DATE,
  fy1314_mailed_date DATE,
  fy1314_completed_date DATE,
  fy1112_completed_date DATE,
  logcop_delivered_date DATE,
  logcop_uploaded_date DATE,
  laptop_installed_date DATE,
  final_report_date DATE,
  destruction_date DATE,
  est_completed_date DATE,
  est_fy1314_completed_date DATE,
  est_fy1112_completed_date DATE,
  last_updated_by VARCHAR(50),
  last_updated_date DATETIME
  );

CREATE UNIQUE INDEX backfile_workflow_ndx ON backfile_workflow (ship_fk);

ALTER TABLE backfile_workflow ADD fy14_pshi_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD fy13_pshi_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD fy12_pshi_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD fy11_pshi_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD fy10_pshi_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD extract_date DATE;
ALTER TABLE backfile_workflow ADD comments TEXT;


ALTER TABLE backfile_workflow ADD other_pshi_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD other_box_cnt INTEGER;








ALTER TABLE backfile_workflow ADD requested_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD received_date_css VARCHAR(50);

ALTER TABLE backfile_workflow ADD scanning_delivered_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD fy1314_burned_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD fy1314_mailed_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD fy1314_completed_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD fy1112_completed_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD extract_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD logcop_delivered_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD logcop_uploaded_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD laptop_installed_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD final_report_date_css VARCHAR(50);
ALTER TABLE backfile_workflow ADD destruction_date_css VARCHAR(50);





ALTER TABLE backfile_workflow ADD fy15_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD fy16_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD fy15_pshi_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD fy16_pshi_box_cnt INTEGER;
ALTER TABLE backfile_workflow ADD return_ind TEXT;
ALTER TABLE backfile_workflow ADD returned_date DATE;
ALTER TABLE backfile_workflow ADD returned_date_css TEXT;
ALTER TABLE backfile_workflow ADD return_confirmed_date DATE;
ALTER TABLE backfile_workflow ADD return_confirmed_date_css TEXT;

ALTER TABLE backfile_workflow ADD fy16_completed_date DATE;
ALTER TABLE backfile_workflow ADD fy16_completed_date_css TEXT;
ALTER TABLE backfile_workflow ADD fy16_mailed_date DATE;
ALTER TABLE backfile_workflow ADD fy16_mailed_date_css TEXT;
ALTER TABLE backfile_workflow ADD fy15_completed_date DATE;
ALTER TABLE backfile_workflow ADD fy15_completed_date_css TEXT;
ALTER TABLE backfile_workflow ADD fy15_mailed_date DATE;
ALTER TABLE backfile_workflow ADD fy15_mailed_date_css TEXT;

ALTER TABLE backfile_workflow ADD is_required TEXT;

UPDATE backfile_workflow SET is_required = 'Y';

DROP VIEW backfile_workflow_vw;
CREATE VIEW backfile_workflow_vw AS
SELECT b.backfile_workflow_pk, b.ship_fk, s.uic, s.ship_name, s.type, s.hull, s.tycom_display, s.homeport,
       t.sched_training_date, strftime('%m/%d/%Y', t.sched_training_date) AS sched_training_date_fmt,
       b.is_required,
       b.fy16_box_cnt, b.fy15_box_cnt, b.fy14_box_cnt, b.fy13_box_cnt, b.fy12_box_cnt, b.fy11_box_cnt, b.fy10_box_cnt, b.other_box_cnt,
       IFNULL(b.fy16_box_cnt, 0) + IFNULL(b.fy15_box_cnt, 0) + IFNULL(b.fy14_box_cnt, 0) + IFNULL(b.fy13_box_cnt, 0) + IFNULL(b.fy12_box_cnt, 0) + IFNULL(b.fy11_box_cnt, 0) + IFNULL(b.fy10_box_cnt, 0) + IFNULL(b.other_box_cnt, 0) AS total_box_cnt,
       b.fy16_pshi_box_cnt, b.fy15_pshi_box_cnt, b.fy14_pshi_box_cnt, b.fy13_pshi_box_cnt, b.fy12_pshi_box_cnt, b.fy11_pshi_box_cnt, b.fy10_pshi_box_cnt, b.other_pshi_box_cnt,
       IFNULL(b.fy16_pshi_box_cnt, 0) + IFNULL(b.fy15_pshi_box_cnt, 0) + IFNULL(b.fy14_pshi_box_cnt, 0) + IFNULL(b.fy13_pshi_box_cnt, 0) + IFNULL(b.fy12_pshi_box_cnt, 0) + IFNULL(b.fy11_pshi_box_cnt, 0) + IFNULL(b.fy10_pshi_box_cnt, 0) + IFNULL(b.other_pshi_box_cnt, 0) AS total_pshi_box_cnt,
       b.requested_date            , strftime('%m/%d/%Y', b.requested_date)            AS requested_date_fmt           , b.requested_date_css,
       b.received_date             , strftime('%m/%d/%Y', b.received_date)             AS received_date_fmt            , b.received_date_css,
       b.scanning_delivered_date   , strftime('%m/%d/%Y', b.scanning_delivered_date)   AS scanning_delivered_date_fmt  , b.scanning_delivered_date_css,
       b.fy16_completed_date       , strftime('%m/%d/%Y', b.fy16_completed_date)       AS fy16_completed_date_fmt      , b.fy16_completed_date_css,
       b.fy16_mailed_date          , strftime('%m/%d/%Y', b.fy16_mailed_date)          AS fy16_mailed_date_fmt         , b.fy16_mailed_date_css,
       b.fy15_completed_date       , strftime('%m/%d/%Y', b.fy15_completed_date)       AS fy15_completed_date_fmt      , b.fy15_completed_date_css,
       b.fy15_mailed_date          , strftime('%m/%d/%Y', b.fy15_mailed_date)          AS fy15_mailed_date_fmt         , b.fy15_mailed_date_css,
       b.fy1314_burned_date        , strftime('%m/%d/%Y', b.fy1314_burned_date)        AS fy1314_burned_date_fmt       , b.fy1314_burned_date_css,
       b.fy1314_mailed_date        , strftime('%m/%d/%Y', b.fy1314_mailed_date)        AS fy1314_mailed_date_fmt       , b.fy1314_mailed_date_css,
       b.fy1314_completed_date     , strftime('%m/%d/%Y', b.fy1314_completed_date)     AS fy1314_completed_date_fmt    , b.fy1314_completed_date_css,
       b.fy1112_completed_date     , strftime('%m/%d/%Y', b.fy1112_completed_date)     AS fy1112_completed_date_fmt    , b.fy1112_completed_date_css,
       b.extract_date              , strftime('%m/%d/%Y', b.extract_date)              AS extract_date_fmt             , b.extract_date_css,
       b.logcop_delivered_date     , strftime('%m/%d/%Y', b.logcop_delivered_date)     AS logcop_delivered_date_fmt    , b.logcop_delivered_date_css,
       b.logcop_uploaded_date      , strftime('%m/%d/%Y', b.logcop_uploaded_date)      AS logcop_uploaded_date_fmt     , b.logcop_uploaded_date_css,
       b.laptop_installed_date     , strftime('%m/%d/%Y', b.laptop_installed_date)     AS laptop_installed_date_fmt    , b.laptop_installed_date_css,
       b.final_report_date         , strftime('%m/%d/%Y', b.final_report_date)         AS final_report_date_fmt        , b.final_report_date_css,
       b.destruction_date          , strftime('%m/%d/%Y', b.destruction_date)          AS destruction_date_fmt         , b.destruction_date_css,
       b.returned_date             , strftime('%m/%d/%Y', b.returned_date)             AS returned_date_fmt            , b.returned_date_css,
       b.return_confirmed_date     , strftime('%m/%d/%Y', b.return_confirmed_date)     AS return_confirmed_date_fmt    , b.return_confirmed_date_css,
       b.fy16_completed_date       , strftime('%m/%d/%Y', b.fy16_completed_date)       AS fy16_completed_date_fmt      , b.fy16_completed_date_css,
       b.fy16_mailed_date          , strftime('%m/%d/%Y', b.fy16_mailed_date)          AS fy16_mailed_date_fmt         , b.fy16_mailed_date_css,
       b.fy15_completed_date       , strftime('%m/%d/%Y', b.fy15_completed_date)       AS fy15_completed_date_fmt      , b.fy15_completed_date_css,
       b.fy15_mailed_date          , strftime('%m/%d/%Y', b.fy15_mailed_date)          AS fy15_mailed_date_fmt         , b.fy15_mailed_date_css,
       b.est_completed_date        , strftime('%m/%d/%Y', b.est_completed_date)        AS est_completed_date_fmt       ,
       b.est_fy1314_completed_date , strftime('%m/%d/%Y', b.est_fy1314_completed_date) AS est_fy1314_completed_date_fmt,
       b.est_fy1112_completed_date , strftime('%m/%d/%Y', b.est_fy1112_completed_date) AS est_fy1112_completed_date_fmt,
       date(b.scanning_delivered_date, '+15 days') AS fy1314_due_date,   strftime('%m/%d/%Y', b.scanning_delivered_date, '+15 days') AS fy1314_due_date_fmt,
       date(b.scanning_delivered_date, '+45 days') AS due_date,          strftime('%m/%d/%Y', b.scanning_delivered_date, '+45 days') AS due_date_fmt,
       MAX (b.requested_date, b.received_date, b.scanning_delivered_date, b.fy1314_burned_date, b.fy1314_mailed_date, b.fy1314_completed_date, b.fy1112_completed_date, b.extract_date, b.logcop_delivered_date, b.logcop_uploaded_date, b.laptop_installed_date, b.final_report_date) AS completed_date,
       strftime('%m/%d/%Y', MAX (b.requested_date, b.received_date, b.scanning_delivered_date, b.fy1314_burned_date, b.fy1314_mailed_date, b.fy1314_completed_date, b.fy1112_completed_date, b.extract_date, b.logcop_delivered_date, b.logcop_uploaded_date, b.laptop_installed_date, b.final_report_date)) AS completed_date_fmt,
       b.return_ind, b.comments, b.last_updated_by, b.last_updated_date
FROM backfile_workflow b INNER JOIN ship_vw s ON b.ship_fk = s.ship_pk
                         LEFT OUTER JOIN training_workflow t ON b.ship_fk = t.ship_fk;

