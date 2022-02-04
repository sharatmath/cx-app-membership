#!/bin/bash
echo "building app...."
JAVA_HOME=/usr/lib/jvm/openjdk-11.0.2_linux-x64/jdk-11.0.2/ mvn clean package -Dmaven.test.skip=true

echo "creating docker...."
sudo sudo docker build -f Dockerfile -t fra.ocir.io/singteloracloud/singtelomcsit/prepaid-cx-membership-api:sit-1.1.1 .

echo "pushing docker...."
sudo docker push fra.ocir.io/singteloracloud/singtelomcsit/prepaid-cx-membership-api:sit-1.1.1

echo "Deleting Kubernetes deployment...."
kubectl delete deployment prepaid-cx-membership-api-deployment --kubeconfig /home/opc/.kube/config_prepaid_dev

echo "Deploying to Kubernetes...."
kubectl create -f services.yaml --kubeconfig /home/opc/.kube/config_prepaid_dev
