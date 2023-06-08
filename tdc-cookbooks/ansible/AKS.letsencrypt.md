# Steps to get a valid SSL certificate from Letsencrypt to enable HTTPS
`helm version`  
`helm init`  
`helm version`  
`helm install stable/nginx-ingress --name nginx-ingress --namespace kube-system --set controller.service.loadBalancerIP="<ip>" --set rbac.create=false`  
`kubectl --namespace kube-system get services -o wide -w nginx-ingress-controller`  

`helm install stable/cert-manager --name cert-manager --namespace kube-system --set ingressShim.defaultIssuerName=letsencrypt-production --set ingressShim.defaultIssuerKind=ClusterIssuer --set rbac.create=false --set serviceAccount.create=false`  
`kubectl logs deployment/cert-manager cert-manager --namespace kube-system -f`  

`kubectl apply -f /tmp/cluster-issuer.play.yaml`  
`kubectl get clusterissuer letsencrypt-production`  

`kubectl apply -f /tmp/certificates.play.yaml`  

`ansible-playbook -i inventories/<environment> azure_kubernetes_backend_service.yml -e tdc_backend_image_version=<build number>`  

`kubectl apply -f /tmp/backend-ingress.play.yaml`  