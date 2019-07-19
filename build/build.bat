TITLE BUILD LogMeIn-CardGame
color 17
@ECHO OFF
cls

ECHO   ====================================================================================================================
ECHO   =  MENU                                                                                                            =
ECHO   ====================================================================================================================
ECHO   =  1 - Normal (PAUSE)                                                                                              =
ECHO   =  2 - Fast (no PAUSE)                                                                                             =
ECHO   ====================================================================================================================
SET /P M=Choice: 
CLS

IF "%JAVA_HOME%" == "C:\Program Files\Java\jdk1.8.0_201" (
	ECHO JAVAHOME = %JAVA_HOME%
) ELSE (
    ECHO   ====================================================================================================================
    ECHO   =  JAVA JDK 1.8 (201) Needed at "C:\Program Files\Java\jdk1.8.0_201"
    ECHO   ====================================================================================================================
	SETX /M JAVA_HOME "C:\Program Files\Java\jdk1.8.0_201"
	PAUSE
	EXIT
)
IF %M%==1 PAUSE

IF %M%==1 CLS
ECHO   ====================================================================================================================
ECHO   =  cd ..\backend
ECHO   =  CALL mvn clean install package                                                                                  =
ECHO   ====================================================================================================================
cd ..\backend
CALL mvn clean install package
IF %M%==1 PAUSE

IF %M%==1 CLS
ECHO   ====================================================================================================================
ECHO   =  cd ..\frontend
ECHO   =  rmdir java /S /Q
ECHO   =  mkdir java
ECHO   =  copy ..\backend\target\CardGame-*.jar java                                                          =
ECHO   =  rmdir dist /S /Q
ECHO   =  CALL npm run dist
ECHO   ====================================================================================================================
cd ..\frontend
rmdir java /S /Q
mkdir java
copy ..\backend\target\CardGame-*.jar .\java
rmdir dist /S /Q
CALL npm run dist
IF %M%==1 PAUSE

ECHO   ====================================================================================================================
ECHO   =  Type the version number                                                               =
ECHO   ====================================================================================================================
SET /P V=Version: 

IF %M%==1 cls
set datestr=%date:~-4,4%-%date:~-7,2%-%date:~-10,2%
echo   ====================================================================================================================
echo   =  Copying to ..\build\%datestr%                                                               =
echo   ====================================================================================================================
echo   =  cd ..\build                                                                                  =
ECHO   =  rmdir %datestr%(%V%) /S /Q
echo   =  mkdir %datestr%(%V%)                                                                                                =
echo   =  copy ..\frontend\dist\*.exe ..\build\%datestr%                        =
echo   ====================================================================================================================
cd ..\build
rmdir %datestr%(%V%) /S /Q
mkdir %datestr%(%V%)
copy ..\frontend\dist\*.exe ..\build\%datestr%(%V%)
IF %M%==1 PAUSE

IF %M%==1 cls
echo   ====================================================================================================================
echo   =                                           Build done!                                                            =
echo   ====================================================================================================================
dir /s ..\build\%datestr%
PAUSE