@echo off

:: This creates copies of the database and application, and zips them into the "bak" directory.

SET mydate=%date:~10,4%%date:~4,2%%date:~7,2%
SET mytime=%time:~0,2%
if %mytime% LEQ 9 (
  SET mytime=%time:~1,1%
)
SET mytime=%mytime%%time:~3,2%
SET datetime=%mydate%-%mytime%
SET zipExe=%bakPath%7z-x64\7z.exe
SET bakPath=C:\sqlite3_db\bak\

SET dbFile=C:\sqlite3_db\pshi_wiki.sqlite
SET dbFileCopy=%bakPath%wiki\pshi_wiki.sqlite
SET dbZip=%bakPath%wiki\pshi-wiki-db_%datetime%.zip

SET wikiPath=C:\cloaked\www\wiki
SET wikiCopy=%bakPath%wiki\wiki
SET wikiZip=%bakPath%wiki\pshi_wiki_%datetime%.zip

ECHO ==========================================================================
ECHO = Backing up the database "%dbFile%"
ECHO ==========================================================================
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Copying "%dbFile%" to "%dbFileCopy%"
ECHO --------------------------------------------------------------------------
copy %dbFile% %dbFileCopy%
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Zipping "%dbFileCopy%" to "%dbZip%"
ECHO --------------------------------------------------------------------------
%zipExe% a -tzip %dbZip% %dbFileCopy%
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Deleting copy "%dbFileCopy%"
ECHO --------------------------------------------------------------------------
del %dbFileCopy%
ECHO -
ECHO -

:: Backup the wiki

ECHO ==========================================================================
ECHO = Backing up the wiki "%wikiPath%"
ECHO ==========================================================================
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Copying "%wikiPath%" to "%wikiCopy%"
ECHO --------------------------------------------------------------------------
xcopy %wikiPath% %wikiCopy% /O /X /E /H /K /I /q /Y
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Zipping "%wikiCopy%" to "%wikiZip%"
ECHO --------------------------------------------------------------------------
%zipExe% a -tzip %wikiZip% %wikiCopy%
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Deleting copy "%wikiCopy%"
ECHO --------------------------------------------------------------------------
rmdir %wikiCopy% /s /q
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - 
ECHO - Completed backup for "%datetime%"
ECHO -
ECHO - Wiki:     %wikiZip%
ECHO -
ECHO - Database: %dbZip%
ECHO -
ECHO --------------------------------------------------------------------------
PAUSE;