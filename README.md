# oauth2-spring-security-demo

SPRING BOOT OAUTH2 DEMO (GrantType=Password)

Components: Zuul Proxy, OAuth2Server, 2 Resource Servers
Zuul proxy configured to forward OAuth2 token to resource servers
Resource servers:  OAuth2 token against validation + method authorization using Spring Security context 

Test oauth2 server
curl -v oauth2-secret:bbT3W5k8ZW6W@localhost:9083/auth-service/oauth/token -d grant_type=password -d username=admin -d password=admin
curl -v oauth2-secret:bbT3W5k8ZW6W@localhost:9083/auth-service/oauth/token -d grant_type=password -d username=user -d password=password
curl -v oauth2-secret:bbT3W5k8ZW6W@localhost:9083/auth-service/oauth/token -d grant_type=password -d username=disabled -d password=password

Test resource server
curl -H "Authorization: bearer 7e507846-9be9-49c7-b685-6b3622275392" http://localhost:9081/resource-spl-1

Test using Zuul proxy
curl -H "Authorization: bearer 7e507846-9be9-49c7-b685-6b3622275392" http://localhost:9080/proxy/persons-spl-1
curl -H "Authorization: bearer c7f47e77-43dc-4fbf-93da-550959e80992" http://localhost:9080/proxy/persons-spl-2
curl -H "Authorization: bearer c7f47e77-43dc-4fbf-93da-550959e80992" http://localhost:9080/proxy/protected-spl-2

curl -H "Authorization: bearer 6a8e0dcd-cdd7-448a-90aa-6d5fa04a3d03" http://localhost:9080/proxy/user

Custom OAuth2 Client in Proxy (optional)
curl -v localhost:9083/proxy/login -d username=user -d password=userpwd
curl -v localhost:9083/proxy/refreshToken -d refreshToken=2bee482a-e8c3-4f6f-8c33-8c2a00248e30
