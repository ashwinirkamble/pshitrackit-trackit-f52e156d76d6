@echo OFF

ECHO --------------------------------------------------------------------------
ECHO - Checking status of Tomcat8
ECHO --------------------------------------------------------------------------
sc \\10.28.31.55 query Tomcat8

ECHO --------------------------------------------------------------------------
ECHO - Are you sure you want to restart Tomcat?
ECHO --------------------------------------------------------------------------
PAUSE;

sc \\10.28.31.55 stop Tomcat8

ECHO - Waiting 15 seconds to allow it to properly close.
TIMEOUT 15

sc \\10.28.31.55 start Tomcat8

PAUSE;