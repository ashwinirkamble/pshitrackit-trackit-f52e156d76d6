@echo off
setlocal
SET package=C:\sqlite3_db\pshi.zip
SET zipExe=C:\sqlite3_db\bak\7z-x64\7z.exe
SET appPath=C:\cloaked\pshi
SET appBakPath=%appPath%_bak
SET d=%DATE:~-4%-%DATE:~4,2%-%DATE:~7,2%
SET t=%time::=:% 
SET t=%t: =%
SET START_TIMESTAMP=%d% %t%
ECHO -
ECHO ==========================================================================
ECHO = Deploying TrackIT
ECHO - START: %START_TIMESTAMP%
ECHO ==========================================================================
ECHO -
REM :: pshi.zip
IF NOT EXIST %package% (
  ECHO - NOT FOUND "%package%". Cannot run process. Script will exit.
  ECHO -
  PAUSE;
  EXIT;
)
ECHO - FOUND "%package%"
ECHO -
REM :: 7z.exe
IF NOT EXIST %zipExe% (
  ECHO - NOT FOUND "%zipExe%". Cannot run process. Script will exit.
  ECHO -
  PAUSE;
  EXIT;
)
ECHO - FOUND "%zipExe%"
ECHO -
REM :: pshi
IF NOT EXIST %appPath% (
  ECHO - NOT FOUND "%appPath%". Cannot run process. Script will exit.
  PAUSE;
  EXIT;
)
ECHO - FOUND "%appPath%"
ECHO -
REM :: pshi_bak
IF NOT EXIST %appBakPath% (
  ECHO - NOT FOUND "%appBakPath%"
  ECHO -
)
ECHO - FOUND "%appBakPath%"
ECHO -
ECHO - Deployment is ready.
ECHO -
PAUSE;

::stop tomcat
ECHO --------------------------------------------------------------------------
ECHO - Stopping Tomcat8
ECHO --------------------------------------------------------------------------
ECHO -
sc stop Tomcat8
ECHO -
ECHO - Pausing for 5 seconds to allow proper stopping of Tomcat.
ECHO -
TIMEOUT 5
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Rename app folder for quick revert (should be backed up too)
ECHO --------------------------------------------------------------------------
ECHO - If exists, delete old "pshi_bak"
ECHO -
IF EXIST %appBakPath% (
  REM :: Delete C:\cloaked\pshi_bak
  REM ::   /S Removes a directory tree and its files.
  REM ::   /Q Quiet mode, does not prompt user.
  RMDIR /S /Q %appBakPath%
  IF EXIST %appBakPath% (
    ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    ECHO ! FAILED to delete "%appBakPath%". Starting Tomcat.
    ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    ECHO -
    GOTO :start_tomcat
  ) ELSE (
    ECHO - SUCCESSFULLY deleted "%appBakPath%"
    ECHO -
  )
)
ECHO - Rename "%appPath%" to "C:\cloaked\pshi_bak"
ECHO -
IF EXIST %appPath% (
  ::rename "C:\cloaked\pshi" to "C:\cloaked\pshi_bak"
  ::MOVE C:\cloaked\pshi C:\cloaked\pshi_bak
  MOVE %appPath% %appBakPath%
  IF EXIST %appBakPath% (
    ECHO - SUCCESSFULLY renamed app folder to "%appBakPath%"
    ECHO -
  ) ELSE (
    ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    ECHO ! FAILED to rename app folder to "%appBakPath%". Starting Tomcat.
    ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    ECHO -
    GOTO :start_tomcat
  )
)
ECHO --------------------------------------------------------------------------
ECHO - Prepare deployment folder
ECHO --------------------------------------------------------------------------
ECHO -
ECHO - Create folder "%appPath%"
ECHO -
::recreate the directory
MKDIR %appPath%
IF EXIST %appPath% (
  ECHO - SUCCESSFULLY created app folder "%appPath%"
  ECHO -
) ELSE (
  ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  ECHO ! FAILED create app folder to "%appPath%". Script will exit. Tomcat is still STOPPED.
  ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  ECHO -
  PAUSE;
  EXIT;
)
ECHO --------------------------------------------------------------------------
ECHO - Move "%package%" to "%appPath%"
ECHO --------------------------------------------------------------------------
ECHO -
ECHO - Moving zip file to app folder "pshi"
ECHO -
::move "C:\sqlite3_db\pshi.zip" to "C:\cloaked\pshi"
MOVE %package% %appPath%
IF EXIST %appPath%\pshi.zip (
  ECHO - SUCCESSFULLY moved "pshi.zip" to "%appPath%"
  ECHO -
) ELSE (
  REM :: at this point, we have to figure out what went wrong.
  ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  ECHO ! FAILED to move "pshi.zip" to "%appPath%". Script will exit. Tomcat is still STOPPED.
  ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  ECHO -
  PAUSE;
  EXIT;
)
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Unzip "%appPath%\pshi.zip" in "%appPath%"
ECHO --------------------------------------------------------------------------
ECHO -
::unzip "C:\cloaked\pshi\pshi.zip" there
%zipExe% x %appPath%\pshi.zip -o%appPath%
ECHO -
ECHO --------------------------------------------------------------------------
ECHO - Delete "%appPath%\pshi.zip"
ECHO --------------------------------------------------------------------------
ECHO - 
DEL %appPath%\pshi.zip
REM :: pshi
IF EXIST %appPath%\pshi.zip (
  ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  ECHO ! FAILED to delete "%appPath%\pshi.zip"
  ECHO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
)
ECHO - 
:start_tomcat
ECHO --------------------------------------------------------------------------
ECHO - Start Tomcat8
ECHO --------------------------------------------------------------------------
ECHO -
sc start Tomcat8
ECHO -
SET d=%DATE:~-4%-%DATE:~4,2%-%DATE:~7,2%
SET t=%time::=:% 
SET t=%t: =%
SET END_TIMESTAMP=%d% %t%
ECHO - START: %START_TIMESTAMP%
ECHO - END  : %END_TIMESTAMP%
ECHO -
PAUSE;