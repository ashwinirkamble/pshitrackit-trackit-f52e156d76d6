@echo off

:: This is was in the "C:\sqlite3_db\bak"
:: This creates copies of the database and application, and zips them into the "bak" directory.

SET mydate=%date:~10,4%%date:~4,2%%date:~7,2%
SET mytime=%time:~0,2%
if %mytime% LEQ 9 (
  SET mytime=%time:~1,1%
)
SET mytime=%mytime%%time:~3,2%
SET datetime=%mydate%-%mytime%

:: Backup the database
SET bakPath=C:\sqlite3_db\bak\
SET zipExe=%bakPath%7z-x64\7z.exe

SET dbFile=C:\sqlite3_db\pshi.db
SET dbFileCopy=C:\sqlite3_db\bak\pshi.db
SET dbZip=%bakPath%database\pshi-db_%datetime%.zip

SET appPath=C:\cloaked\pshi
SET appCopy=%bakPath%app\pshi
SET appZip=%bakPath%app\pshi_%datetime%.zip

ECHO ==========================================================================
ECHO = Backing up the database "%dbFile%"
ECHO ==========================================================================
ECHO -
IF NOT EXIST %zipExe% (
  ECHO - "%zipExe%" was not found. Cannot run process. Script will exit.
  PAUSE;
  EXIT;
)
ECHO - FOUND "%zipExe%"
IF NOT EXIST %dbFile% (
  ECHO - "%dbFile%" was not found. Cannot run process. Script will exit.
  PAUSE;
  EXIT;
)
ECHO - FOUND "%dbFile%"
IF NOT EXIST %appPath% (
  ECHO - "%appPath%" was not found. Cannot run process. Script will exit.
  PAUSE;
  EXIT;
)
ECHO - FOUND "%appPath%"
PAUSE;
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Copying "%dbFile%" to "%dbFileCopy%"
ECHO --------------------------------------------------------------------------
copy %dbFile% %dbFileCopy%
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Zipping "%dbFileCopy%" to "%dbZip%"
ECHO - Using zipExe=%zipExe%
ECHO --------------------------------------------------------------------------
%zipExe% a -tzip %dbZip% %dbFileCopy%
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Deleting copy "%dbFileCopy%"
ECHO --------------------------------------------------------------------------

DEL %dbFileCopy%
ECHO -
ECHO -

:: Backup the app

ECHO ==========================================================================
ECHO = Backing up the app "%appPath%"
ECHO ==========================================================================
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Copying "%appPath%" to "%appCopy%"
ECHO --------------------------------------------------------------------------
XCOPY %appPath% %appCopy% /O /X /E /H /K /I /q /Y
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Zipping "%appCopy%" to "%appZip%"
ECHO --------------------------------------------------------------------------
%zipExe% a -tzip %appZip% %appCopy%
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Deleting copy "%appCopy%"
ECHO --------------------------------------------------------------------------
RMDIR %appCopy% /s /q
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - 
ECHO - Completed backup for "%datetime%"
ECHO -
ECHO - Application: %appZip%
ECHO - 
ECHO - Database:    %dbZip%
ECHO -
ECHO --------------------------------------------------------------------------
PAUSE;