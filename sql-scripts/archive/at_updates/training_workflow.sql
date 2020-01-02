CREATE TABLE training_workflow (
  training_workflow_pk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  ship_fk INTEGER NOT NULL,
  backfile_recv_date DATE,
  backfile_completed_date DATE,
  loc_file_recv_date DATE,
  loc_file_rev_date DATE,
  pacflt_food_report_date DATE,
  system_ready_date DATE,
  computer_name_db_date DATE,
  computer_name_logcop_date DATE,
  training_kit_ready_date DATE,
  sched_training_date DATE,
  actual_training_date DATE,
  comments TEXT,
  last_updated_by VARCHAR(50),
  last_updated_date DATETIME
  );

CREATE UNIQUE INDEX training_workflow_ndx ON training_workflow (ship_fk);

ALTER TABLE training_workflow ADD sched_training_time INTEGER;

ALTER TABLE training_workflow ADD est_training_month TEXT;

ALTER TABLE training_workflow ADD trainer TEXT;

ALTER TABLE training_workflow ADD sched_training_loc TEXT;

CREATE INDEX training_workflow_ndx_dates ON training_workflow (actual_training_date, sched_training_date);

ALTER TABLE training_workflow ADD trainer_user_fk INTEGER;

--        c.computer_name, c.laptop_tag, c.scanner_tag,
--                         LEFT OUTER JOIN configured_system_vw c ON s.ship_pk = c.ship_pk

ALTER TABLE training_workflow ADD client_confirmed_ind TEXT;

-- ALTER TABLE training_workflow ADD contract_number TEXT;

DROP VIEW training_workflow_vw;
CREATE VIEW training_workflow_vw AS
SELECT  DISTINCT t.training_workflow_pk, t.ship_fk, s.uic, s.ship_name, s.homeport, s.rsupply, s.type, s.hull, s.tycom, s.tycom_display,
        b.received_date AS backfile_recv_date,            strftime('%m/%d/%Y', b.received_date) AS backfile_recv_date_fmt,
        MAX (b.requested_date, b.received_date, b.scanning_delivered_date, b.fy1314_burned_date, b.fy1314_mailed_date, b.fy1314_completed_date, b.fy1112_completed_date, b.extract_date, b.logcop_delivered_date, b.logcop_uploaded_date, b.laptop_installed_date, b.final_report_date) AS backfile_completed_date,
        strftime('%m/%d/%Y', MAX (b.requested_date, b.received_date, b.scanning_delivered_date, b.fy1314_burned_date, b.fy1314_mailed_date, b.fy1314_completed_date, b.fy1112_completed_date, b.extract_date, b.logcop_delivered_date, b.logcop_uploaded_date, b.laptop_installed_date, b.final_report_date)) AS backfile_completed_date_fmt,
        t.loc_file_recv_date,           strftime('%m/%d/%Y', t.loc_file_recv_date) AS loc_file_recv_date_fmt,
        t.loc_file_rev_date,            strftime('%m/%d/%Y', t.loc_file_rev_date) AS loc_file_rev_date_fmt,
        t.pacflt_food_report_date,      strftime('%m/%d/%Y', t.pacflt_food_report_date) AS pacflt_food_report_date_fmt,
        t.system_ready_date,            strftime('%m/%d/%Y', t.system_ready_date) AS system_ready_date_fmt,
        t.computer_name_db_date,        strftime('%m/%d/%Y', t.computer_name_db_date) AS computer_name_db_date_fmt,
        t.computer_name_logcop_date,    strftime('%m/%d/%Y', t.computer_name_logcop_date) AS computer_name_logcop_date_fmt,
        t.training_kit_ready_date,      strftime('%m/%d/%Y', t.training_kit_ready_date) AS training_kit_ready_date_fmt,
        t.sched_training_date,          strftime('%m/%d/%Y', t.sched_training_date) AS sched_training_date_fmt,
        t.sched_training_time, t.est_training_month, t.trainer_user_fk, u.first_name || ' ' || u.last_name AS trainer_full_name, u.email AS trainer_email, t.trainer,
        t.actual_training_date,         strftime('%m/%d/%Y', t.actual_training_date) AS actual_training_date_fmt,
        t.sched_training_loc, t.client_confirmed_ind, t.comments, t.last_updated_by, t.last_updated_date
FROM training_workflow t INNER JOIN ship_vw s ON t.ship_fk = s.ship_pk
                         LEFT OUTER JOIN backfile_workflow b ON s.ship_pk = b.ship_fk
                         LEFT OUTER JOIN users u ON u.user_pk = t.trainer_user_fk;



MAX (b.requested_date, b.received_date, b.scanning_delivered_date, b.fy1314_burned_date, b.fy1314_mailed_date, b.fy1314_completed_date, b.fy1112_completed_date, b.extract_date, b.logcop_delivered_date, b.logcop_uploaded_date, b.laptop_installed_date, b.final_report_date) AS completed_date
