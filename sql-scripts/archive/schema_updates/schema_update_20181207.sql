
-- For ATO Update, we're now tracking by Configured System, not by ships (units)
ALTER TABLE issue ADD configured_system_fk INTEGER;
ALTER TABLE issue ADD status_code INTEGER;

INSERT OR REPLACE INTO issue
SELECT issue_pk,project_fk,ship_fk,title,description,status,priority,category,phase,opened_by,
opened_date,closed_date,person_assigned,support_visit_date,support_visit_loc,
support_visit_time,trainer,resolution,total_time,created_by,created_date,last_updated_by,
last_updated_date,initiated_by,dept,is_email_sent,is_email_responded,is_training_provided,
is_training_onsite,ato_fk,auto_close_date,issue_category_fk,priority_reason,
support_visit_end_time,support_visit_reason,laptop_issue,scanner_issue,software_issue,
support_visit_loc_notes,auto_close_status,update_facet_version,update_os_version,
update_configured_system_ind,configured_system_fk, (
  CASE status
    WHEN '0 - Unknown' THEN 0
    WHEN '1 - New' THEN 1
    WHEN '2 - Active' THEN 2
    WHEN '3 - Resolved' THEN 3
    WHEN '4 - Pending Possible Resolution' THEN 4
    WHEN '5 - Closed' THEN 5
    WHEN '6 - Closed (Successful)' THEN 6
    WHEN '7 - Closed (No Response)' THEN 7
    WHEN '8 - Closed (Unavailable)' THEN 8
    ELSE 0
  END
) AS status_code
FROM issue

DROP VIEW issue_vw;

CREATE VIEW issue_vw AS
SELECT
  i.issue_pk,
  i.project_fk,
  i.ship_fk,
  s.uic,
  s.ship_name,
  s.type,
  s.hull,
  s.tycom,
  s.homeport,
  s.rsupply,
  i.title,
  i.description,
  i.status,
  i.priority,
  i.priority_reason,
  i.issue_category_fk,
  c.category,
  i.phase,
  i.opened_by,
  i.opened_date,
  strftime('%m/%d/%Y', i.opened_date) AS opened_date_fmt,
  i.closed_date,
  strftime('%m/%d/%Y', i.closed_date) AS closed_date_fmt,
  i.person_assigned,
  i.resolution,
  i.total_time,
  i.created_by,
  i.created_date,
  strftime('%m/%d/%Y %H:%M:%S', i.created_date) AS created_date_fmt,
  i.support_visit_date,
  strftime('%m/%d/%Y', i.support_visit_date) AS support_visit_date_fmt,
  i.support_visit_time,
  i.support_visit_end_time,
  i.support_visit_reason,
  i.trainer,
  support_visit_loc,
  i.initiated_by,
  i.dept,
  i.is_email_sent,
  i.is_email_responded,
  is_training_provided,
  i.is_training_onsite,
  i.ato_fk,
  i.auto_close_date,
  strftime('%m/%d/%Y', i.auto_close_date) AS auto_close_date_fmt,
  i.auto_close_status,
  i.laptop_issue,
  i.scanner_issue,
  i.software_issue,
  support_visit_loc_notes,
  i.update_configured_system_ind,
  i.update_facet_version,
  i.update_os_version,
  i.configured_system_fk,
  i.last_updated_by,
  i.last_updated_date,
  strftime('%m/%d/%Y %H:%M:%S', i.last_updated_date) AS last_updated_date_fmt
FROM issue i
LEFT OUTER JOIN issue_category c ON i.issue_category_fk = c.issue_category_pk
LEFT OUTER JOIN ship s ON i.ship_fk = s.ship_pk


DROP TABLE managed_list_item;
CREATE TABLE managed_list_item (
  managed_list_item_pk INTEGER PRIMARY KEY NOT NULL,
  managed_list_code INTEGER NOT NULL,
  item_value TEXT NOT NULL,
  current_default TINYINT NOT NULL DEFAULT 0,
  hidden TINYINT NOT NULL DEFAULT 0,
  sort_order INTEGER NOT NULL DEFAULT 0,
  project_fk INTEGER NOT NULL,
  last_updated_by_fk INTEGER NOT NULL,
  last_updated_date DATETIME NOT NULL,
  FOREIGN KEY (project_fk) REFERENCES project(project_pk)
);

INSERT INTO managed_list_item (managed_list_code, item_value, sort_order, project_fk, last_updated_by_fk, last_updated_date)
VALUES (7, 'S-1', 0, 1, 24, datetime('now', 'localtime'))
,(7, 'S-2', 1, 1, 24, datetime('now', 'localtime'))
,(7, 'S-3', 2, 1, 24, datetime('now', 'localtime'))
,(7, 'S-4', 3, 1, 24, datetime('now', 'localtime'))
,(7, 'S-6', 4, 1, 24, datetime('now', 'localtime'))
,(7, 'S-8', 5, 1, 24, datetime('now', 'localtime'))
,(7, 'N/A', 6, 1, 24, datetime('now', 'localtime'))
;

--Moving "location" to managed_list_item
INSERT INTO managed_list_item (managed_list_code, item_value, project_fk, last_updated_by_fk, last_updated_date)
SELECT
  2 AS managed_list_code,
  location AS item_value,
  1 AS project_fk,
  IFNULL((SELECT user_pk FROM users WHERE si.last_updated_by = first_name || ' ' || last_name), 24) AS last_updated_by_fk,
  strftime('%Y-%m-%d %H:%M:%S.000', last_updated_date) AS last_updated_date
FROM location si WHERE true;

--Moving "laptop_issue" to managed_list_item
INSERT INTO managed_list_item (managed_list_code, item_value, project_fk, last_updated_by_fk, last_updated_date)
SELECT
  3 AS managed_list_code,
  laptop_issue AS item_value,
  1 AS project_fk,
  IFNULL((SELECT user_pk FROM users WHERE si.last_updated_by = first_name || ' ' || last_name), 24) AS last_updated_by_fk,
  strftime('%Y-%m-%d %H:%M:%S.000', last_updated_date) AS last_updated_date
FROM laptop_issue si WHERE true;

--Moving "scanner_issue" to managed_list_item
INSERT INTO managed_list_item (managed_list_code, item_value, project_fk, last_updated_by_fk, last_updated_date)
SELECT
  4 AS managed_list_code,
  scanner_issue AS item_value,
  1 AS project_fk,
  IFNULL((SELECT user_pk FROM users WHERE si.last_updated_by = first_name || ' ' || last_name), 24) AS last_updated_by_fk,
  strftime('%Y-%m-%d %H:%M:%S.000', last_updated_date) AS last_updated_date
FROM scanner_issue si WHERE true;

--Moving "software_issue" to managed_list_item
INSERT INTO managed_list_item (managed_list_code, item_value, project_fk, last_updated_by_fk, last_updated_date)
SELECT
  5 AS managed_list_code,
  software_issue AS item_value,
  1 AS project_fk,
  IFNULL((SELECT user_pk FROM users WHERE si.last_updated_by = first_name || ' ' || last_name), 24) AS last_updated_by_fk,
  strftime('%Y-%m-%d %H:%M:%S.000', last_updated_date) AS last_updated_date
FROM software_issue si WHERE true;

--Moving "support_team" to managed_list_item
INSERT INTO managed_list_item (managed_list_code, item_value, project_fk, last_updated_by_fk, last_updated_date)
SELECT
  6 AS managed_list_code,
  full_name AS item_value,
  1 AS project_fk,
  IFNULL((SELECT user_pk FROM users WHERE st.last_updated_by = first_name || ' ' || last_name), 24) AS last_updated_by_fk,
  strftime('%Y-%m-%d %H:%M:%S.000', last_updated_date) AS last_updated_date
FROM support_team st WHERE true;

--Moving "facet_version" to managed_list_item
INSERT INTO managed_list_item (managed_list_code, item_value, project_fk, sort_order, current_default, last_updated_by_fk, last_updated_date)
SELECT
  8 AS managed_list_code,
  facet_version AS item_value,
  1 AS project_fk,
  sort_order,
  CASE WHEN IFNULL(is_curr, 'N') = 'Y' THEN 1 ELSE 0 END AS current_default,
  IFNULL((SELECT user_pk FROM users WHERE last_updated_by = first_name || ' ' || last_name), 24) AS last_updated_by_fk,
  strftime('%Y-%m-%d %H:%M:%S.000', last_updated_date) AS last_updated_date
FROM facet_version WHERE true;

--Moving "os_version" to managed_list_item
INSERT INTO managed_list_item (managed_list_code, item_value, project_fk, sort_order, current_default, last_updated_by_fk, last_updated_date)
SELECT
  9 AS managed_list_code,
  os_version AS item_value,
  1 AS project_fk,
  sort_order,
  CASE WHEN IFNULL(is_curr, 'N') = 'Y' THEN 1 ELSE 0 END AS current_default,
  IFNULL((SELECT user_pk FROM users WHERE last_updated_by = first_name || ' ' || last_name), 24) AS last_updated_by_fk,
  strftime('%Y-%m-%d %H:%M:%S.000', last_updated_date) AS last_updated_date
FROM os_version WHERE true;

