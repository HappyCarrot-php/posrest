@echo off
title Actualizador Automático - Proyecto POS REST
color 0A

echo ================================================
echo     ACTUALIZADOR DE PROYECTO JAVA POS REST
echo ================================================
echo.

REM Ruta del proyecto local
set PROJECT_PATH=C:\Users\ricky\Documents\Programacion\Java\proyecto_pos

REM Nombre del repositorio remoto
set REMOTE_REPO=https://github.com/HappyCarrot-php/posrest.git

cd /d "%PROJECT_PATH%"
if %errorlevel% neq 0 (
    echo [ERROR] No se pudo acceder a la carpeta del proyecto.
    pause
    exit /b
)

echo.
echo [1/4] Verificando cambios locales...
git status

echo.
echo [2/4] Agregando cambios...
git add .

echo.
set /p COMMIT_MSG=Escribe el mensaje del commit (por defecto: "Actualizacion automatica"): 
if "%COMMIT_MSG%"=="" set COMMIT_MSG=Actualizacion automatica

git commit -m "%COMMIT_MSG%"
if %errorlevel% neq 0 (
    echo [!] No hay cambios nuevos para subir.
    goto push_changes
)

:push_changes
echo.
echo [3/4] Enviando cambios a GitHub...
git push origin master

if %errorlevel% neq 0 (
    echo [ERROR] No se pudo hacer push. Revisa tu conexión o credenciales.
    pause
    exit /b
)

echo.
echo [4/4] Actualización completada correctamente.
echo.
echo ================================================
echo        REPOSITORIO SINCRONIZADO CON ÉXITO
echo ================================================
pause
exit
