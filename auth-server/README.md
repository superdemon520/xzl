# Authorize Server for Backend Services

Authorize Server for backend services.

# Build and Run
## Local Run
`./gradlew bootRun`

## Build for remote run
* build and package  
`./gradlew build`

* build into docker image  
`docker build -t registry.xzlcorp.com/corp/auth-server:latest .`

* push into docker registry  
`docker push registry.xzlcorp.com/corp/auth-server`

* log on to remote host, run docker container  
`docker run -e HOST_IP -dp 8060:8060 --name auth_srv registry.xzlcorp.com/corp/auth-server`

**NOTE** you might need to login to docker registry if not yet:  
`docker login registry.xzlcorp.com`