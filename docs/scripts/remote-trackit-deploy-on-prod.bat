@echo off

::SET batch_path="C:\sqlite3_db\bak\test.bat"
SET batch_path="C:\sqlite3_db\bak\trackit-backup-and-deploy.bat"
SET remote_machine=10.28.31.55
SET username=PREMIERSOL-WEB\Lewis
SET task_name="Deploy TrackIT"
SET /p password= "Enter Password for '%username%':"

SCHTASKS /s %remote_machine% /U %username% /P %password% /create /tn %task_name% /tr %batch_path% /sc ONCE /sd 01/01/1910 /st 00:00

SCHTASKS /s %remote_machine% /U %username% /P %password% /run /TN %task_name%

PAUSE;