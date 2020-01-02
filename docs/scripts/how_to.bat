@ECHO OFF

ECHO This is just quick reference on how to use Batch scripts.
EXIT;

:ask_again

SET /p confirm="- Do you want be asked this question again? (Y/N):"
IF "%confirm%"=="Y" (
  GOTO :ask_again
)

SET /p confirm="- Do you want to do the extra part? (Y/N):"

IF "%confirm%"=="Y" (
  GOTO :extra_part
) ELSE (
  ECHO - Going to end of program
  GOTO :end_of_program
)

ECHO - This will never be seen.

:extra_part


SET /p confirm="- Do you want to see "end of program? (Y/N):"
IF "%confirm%"=="Y" (
  ECHO - You entered "Y"
) ELSE (
  ECHO - You won't see "end of program"
  PAUSE;
  EXIT;
)

:end_of_program
ECHO - end of program

PAUSE;
