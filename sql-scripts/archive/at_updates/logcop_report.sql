DROP TABLE logcop_report;
DROP TABLE logcop_report_prev;
DROP TABLE load_logcop_report;

CREATE TABLE logcop_report (
	report_date DATE NOT NULL,
	ship_name TEXT NOT NULL,
	transmittal_num INTEGER NOT NULL,
	doc_type TEXT,
	facet_version TEXT,
	upload_date DATE NOT NULL,
	upload_user TEXT NOT NULL,
	doc_cnt INTEGER NOT NULL
  );

CREATE TABLE logcop_report_prev (
	report_date DATE NOT NULL,
	ship_name TEXT NOT NULL,
	transmittal_num INTEGER NOT NULL,
	doc_type TEXT,
	facet_version TEXT,
	upload_date DATE NOT NULL,
	upload_user TEXT NOT NULL,
	doc_cnt INTEGER NOT NULL
  );

CREATE TABLE load_logcop_report (
	report_date DATE NOT NULL,
	ship_name TEXT NOT NULL,
	transmittal_num INTEGER NOT NULL,
	doc_type TEXT,
	facet_version TEXT,
	upload_date DATE NOT NULL,
	upload_user TEXT NOT NULL,
	doc_cnt INTEGER NOT NULL
  );

CREATE INDEX logcop_report_ndx ON logcop_report (ship_name);
CREATE INDEX logcop_report_prev_ndx ON logcop_report_prev (ship_name);

ALTER TABLE logcop_report ADD transmittal_file TEXT;
ALTER TABLE logcop_report_prev ADD transmittal_file TEXT;
ALTER TABLE load_logcop_report ADD transmittal_file TEXT;

ALTER TABLE logcop_report ADD computer_name TEXT;
ALTER TABLE logcop_report_prev ADD computer_name TEXT;
ALTER TABLE load_logcop_report ADD computer_name TEXT;

CREATE INDEX logcop_report_ndx2 ON logcop_report (computer_name);
CREATE INDEX logcop_report_prev_ndx2 ON logcop_report_prev (computer_name);

DROP VIEW logcop_report_prev_vw;
CREATE VIEW logcop_report_prev_vw AS
SELECT  l.report_date, strftime('%m/%d/%Y', l.report_date) AS report_date_fmt,
				s.ship_pk, s.uic, IFNULL(s.ship_name, l.ship_name) AS ship_name, s.computer_name, s.type, s.hull, homeport, l.transmittal_num, CASE l.transmittal_num WHEN 0 THEN -2 WHEN 999999 THEN -1 ELSE transmittal_num END AS transmittal_order,
				l.doc_type, CASE l.doc_type
										WHEN '1348-1A' THEN 1
										WHEN '1149' THEN 2
										WHEN 'Price Change Report' THEN 3
										WHEN 'Monthly_CN Report' THEN 4
										WHEN 'MLN_CN Report' THEN 4
										WHEN 'APC Report' THEN 5
										WHEN 'SFOEDL Report' THEN 6
										WHEN 'UOL Report' THEN 7
										WHEN 'PCard-Admin' THEN 8
										WHEN 'PCard-Invoice' THEN 9
										WHEN 'Food Requisition' THEN 10
										WHEN 'Food Receipt' THEN 11
										WHEN 'AVPUK' THEN 12
										WHEN 'Storeroom Issue' THEN 12
										WHEN 'CrossDeck DD 1149' THEN 12
										WHEN 'Survey (DD 200)' THEN 12
										WHEN 'Shipping Docs' THEN 12
										WHEN 'Miscellaneous' THEN 13
										ELSE 14 END AS doc_type_order,
				l.facet_version, l.upload_date, strftime('%m/%d/%Y', l.upload_date) AS upload_date_fmt,
				l.upload_user, l.doc_cnt, l.transmittal_file
FROM logcop_report_prev l LEFT OUTER JOIN configured_system_vw s ON l.ship_name = s.ship_name AND l.computer_name = s.computer_name;

DROP VIEW logcop_report_vw;
CREATE VIEW logcop_report_vw AS
SELECT  l.report_date, strftime('%m/%d/%Y', l.report_date) AS report_date_fmt,
				s.configured_system_pk, s.ship_pk, s.uic, IFNULL(s.ship_name, l.ship_name) AS ship_name, s.computer_name, l.computer_name AS transmittal_computer_name, s.type, s.hull, s.homeport, l.transmittal_num, CASE l.transmittal_num WHEN 0 THEN -2 WHEN 999999 THEN -1 ELSE transmittal_num END AS transmittal_order,
				l.doc_type, CASE l.doc_type
										WHEN '1348-1A' THEN 1
										WHEN '1149' THEN 2
										WHEN 'Price Change Report' THEN 3
										WHEN 'Monthly_CN Report' THEN 4
										WHEN 'MLN_CN Report' THEN 4
										WHEN 'APC Report' THEN 5
										WHEN 'SFOEDL Report' THEN 6
										WHEN 'UOL Report' THEN 7
										WHEN 'PCard-Admin' THEN 8
										WHEN 'PCard-Invoice' THEN 9
										WHEN 'Food Requisition' THEN 10
										WHEN 'Food Receipt' THEN 11
										WHEN 'AVPUK' THEN 12
										WHEN 'Storeroom Issue' THEN 12
										WHEN 'CrossDeck DD 1149' THEN 12
										WHEN 'Survey (DD 200)' THEN 12
										WHEN 'Shipping Docs' THEN 12
										WHEN 'Miscellaneous' THEN 13
										ELSE 14 END AS doc_type_order,
				l.facet_version, f.sort_order AS facet_version_order, l.upload_date, strftime('%m/%d/%Y', l.upload_date) AS upload_date_fmt,
				l.upload_user, l.doc_cnt, l.transmittal_file
FROM logcop_report l LEFT OUTER JOIN configured_system_vw s ON l.computer_name = s.computer_name
										 LEFT OUTER JOIN facet_version f ON (CASE WHEN l.facet_version LIKE 'v%' THEN SUBSTR(l.facet_version, 2) ELSE l.facet_version END) = f.facet_version;

--Staging table to figure out latest FACET version based on sort_order from the lookup table
DROP VIEW transmittal_summary_staging_vw;
CREATE VIEW transmittal_summary_staging_vw AS
SELECT 	ship_pk, uic, ship_name, type, hull, homeport, computer_name,
				MIN(facet_version_order) AS facet_version_order,
				MAX(transmittal_num) AS last_transmittal_num,
				MAX(upload_date) AS last_upload_date,
				MAX(CASE WHEN doc_type = '1348-1A' THEN upload_date ELSE NULL END) AS form_1348_upload_date,
				MAX(CASE WHEN doc_type = '1149' THEN upload_date ELSE NULL END) AS form_1149_upload_date,
				MAX(CASE WHEN doc_type = 'Food Requisition' THEN upload_date ELSE NULL END) AS food_approval_upload_date,
				MAX(CASE WHEN doc_type = 'Food Receipt' THEN upload_date ELSE NULL END) AS food_receipt_upload_date,
				MAX(CASE WHEN doc_type = 'PCard-Admin' THEN upload_date ELSE NULL END) AS pcard_admin_upload_date,
				MAX(CASE WHEN doc_type = 'PCard-Invoice' THEN upload_date ELSE NULL END) AS pcard_invoice_upload_date,
				MAX(CASE WHEN doc_type IN ('Price Change Report', 'Monthly_CN Report', 'MLN_CN Report', 'APC Report') THEN upload_date ELSE NULL END) AS price_change_upload_date,
				MAX(CASE WHEN doc_type = 'SFOEDL Report' THEN upload_date ELSE NULL END) AS sfoedl_upload_date,
				MAX(CASE WHEN doc_type = 'UOL Report' THEN upload_date ELSE NULL END) AS uol_upload_date
 FROM logcop_report_vw
WHERE transmittal_num > 0 AND transmittal_num < 999999
GROUP BY ship_pk, uic, ship_name, type, hull, homeport, computer_name;

DROP VIEW transmittal_summary_vw;
CREATE VIEW transmittal_summary_vw AS
SELECT 	t.ship_pk, t.uic, t.ship_name, t.type, t.hull, t.homeport, t.computer_name, f.facet_version AS transmittal_facet_version,
				t.last_transmittal_num, t.last_upload_date, t.form_1348_upload_date, t.form_1149_upload_date, t.food_approval_upload_date, t.food_receipt_upload_date,
				t.pcard_admin_upload_date, t.pcard_invoice_upload_date, t.price_change_upload_date, t.sfoedl_upload_date, t.uol_upload_date
 FROM transmittal_summary_staging_vw t LEFT OUTER JOIN facet_version f ON t.facet_version_order = f.sort_order;


CREATE TABLE transmittal_exception (
	transmittal_exception	INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	ship_fk 					INTEGER NOT NULL,
	transmittal_num 	INTEGER NOT NULL,
	exception_reason 	TEXT NOT NULL
	);

CREATE UNIQUE INDEX transmittal_exception_ndx ON transmittal_exception (ship_fk, transmittal_num);


1348s
1149s
Food Receipts
Food Reqs
PCard-Admin
PCard-Invoice
Price Change Report
SFOEDL Report
UOL Report
