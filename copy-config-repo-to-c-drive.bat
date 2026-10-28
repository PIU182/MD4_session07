@echo off
if exist C:\medical-config-repo (
  echo C:\medical-config-repo already exists.
  exit /b 1
)
xcopy /E /I /H /Y medical-config-repo C:\medical-config-repo
echo Done. Config Server will use file:///C:/medical-config-repo
pause
