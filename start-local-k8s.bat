@echo off
echo ==========================================
echo INICIANDO AMBIENTE LOCAL KUBERNETES
echo ==========================================

:: Garante que o contexto e o docker-desktop
kubectl config use-context docker-desktop

:: Aplica as configuracoes
kubectl apply -f k8s/nginx-config.yaml
kubectl apply -f k8s/deployment.yaml

:: Aguarda os pods subirem
echo Aguardando inicializacao...
timeout /t 10

:: Mostra como estao os pods
kubectl get pods

echo ==========================================
echo PRONTO! Swagger: http://localhost/swagger-ui/index.html
echo ==========================================
pause
