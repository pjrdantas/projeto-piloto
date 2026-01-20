@echo off
REM ===============================
REM Atualiza kubeconfig para o cluster de OHIO (Regiao 2)
REM ===============================
echo Conectando ao cluster de Ohio...
aws eks update-kubeconfig --region us-east-2 --name projeto-piloto-cluster

REM ===============================
REM Rebuild da imagem Docker
REM ===============================
echo Rebuild da imagem Docker...
docker build -t projeto-piloto:latest .

REM Tag da imagem para Docker Hub
echo Tag da imagem...
docker tag projeto-piloto:latest pjrdantas/projeto-piloto:latest

REM Push da imagem para Docker Hub
echo Push da imagem para Docker Hub...
docker push pjrdantas/projeto-piloto:latest

REM ===============================
REM Aplica os arquivos no Kubernetes (Modo Automatico)
REM ===============================
set DEPLOYMENT_NAME=projeto-piloto

echo Aplicando manifestos no Kubernetes...
REM Garante que o deployment e o service sejam criados/atualizados
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/nginx-config.yaml


echo Verificando status do rollout...
kubectl rollout status deployment/%DEPLOYMENT_NAME%

echo ==============================================
echo Deploy finalizado! 
echo Monitore o EXTERNAL-IP com: kubectl get svc
echo ==============================================
pause
