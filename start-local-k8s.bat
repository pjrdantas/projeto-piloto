@echo off
echo ==========================================
echo INICIANDO AMBIENTE LOCAL KUBERNETES
echo ==========================================

:: 1. Compila o Java e gera o projeto-piloto-0.0.1-SNAPSHOT.jar
echo Gerando novo JAR...
call ./mvnw clean package -DskipTests

:: 2. Cria a imagem Docker que o Kubernetes vai usar
echo Gerando nova imagem Docker...
docker build -t projeto-piloto:latest .

:: 3. Garante que o contexto e o docker-desktop
kubectl config use-context docker-desktop

:: 4. Aplica as configuracoes (Nginx e App)
kubectl apply -f k8s/nginx-config.yaml
kubectl apply -f k8s/deployment.yaml

:: 5. Comando essencial: Força o K8s a ler a imagem que acabamos de criar
kubectl rollout restart deployment projeto-piloto-svc

:: Aguarda os pods subirem
echo Aguardando inicializacao...
timeout /t 15

:: Mostra como estao os pods
kubectl get pods

echo ==========================================
echo PRONTO! Swagger: http://localhost/swagger-ui/index.html
echo ==========================================
pause
