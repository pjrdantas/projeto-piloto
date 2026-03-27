@echo off
echo ==========================================
echo INICIANDO AMBIENTE LOCAL KUBERNETES
echo ==========================================

echo Gerando novo JAR...
call ./mvnw clean package -DskipTests

echo Gerando nova imagem Docker...
docker build -t projeto-piloto:latest .

kubectl config use-context docker-desktop

kubectl apply -f k8s/nginx-config.yaml
kubectl apply -f k8s/deployment.yaml

:: Reinicia o DEPLOYMENT (correto)
kubectl rollout restart deployment/projeto-piloto

echo Aguardando inicializacao...
timeout /t 15

kubectl get pods

echo ==========================================
echo PRONTO! Swagger: http://localhost/swagger-ui/index.html
echo ==========================================
pause