@echo off

REM ===============================
REM CONFIGURAÇÕES
REM ===============================
set IMAGE_NAME=projeto-piloto
set IMAGE_TAG=v.1.0
set CONTAINER_NAME=projeto-piloto

echo =========================================
echo 1. BUILD DA IMAGEM DOCKER
echo =========================================
docker build -t %IMAGE_NAME%:%IMAGE_TAG% .
IF %ERRORLEVEL% NEQ 0 (
  echo.
  echo [ERRO] Falha crítica no build da imagem. Verifique o Dockerfile ou Maven.
  pause
  exit /b 1
)

echo =========================================
echo 2. LIMPANDO AMBIENTE ANTERIOR
echo =========================================
docker stop %CONTAINER_NAME% >nul 2>&1
docker rm %CONTAINER_NAME% >nul 2>&1

echo =========================================
echo 3. INICIANDO CONTAINER LOCAL
echo =========================================
docker run -d ^
  --name %CONTAINER_NAME% ^
  -p 8080:8080 ^
  -e "SPRING_PROFILES_ACTIVE=docker" ^
  --restart unless-stopped ^
  %IMAGE_NAME%:%IMAGE_TAG%

IF %ERRORLEVEL% NEQ 0 (
  echo.
  echo [ERRO] Nao foi possivel subir o container. Verifique a porta 8080.
  pause
  exit /b 1
)

echo =========================================
echo DEPLOY REALIZADO COM SUCESSO!
echo =========================================
echo Status atual do container:
docker ps --filter "name=%CONTAINER_NAME%"
echo.
echo Log de inicializacao (ultimas 10 linhas):
docker logs --tail 10 %CONTAINER_NAME%

echo.
echo O terminal fechara automaticamente em 5 segundos...
REM O timeout espera 5 segundos e prossegue sozinho
timeout /t 5
REM O exit fecha a janela do prompt de comando
exit

