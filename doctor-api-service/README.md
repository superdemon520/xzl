# Doctor API Service

API server for doctor end. Provides API service, authenticating, etc

# Build and Run
## Local Run
`./gradlew bootRun`

## Build for remote run
* build and package
`./gradlew build`

* build into docker image
`docker build -t registry.xzlcorp.com/corp/doctor-api-service:latest .`

* push into docker registry
`docker push registry.xzlcorp.com/corp/doctor-api-service`

* log on to remote host, run docker container
`docker run -e HOST_IP -dp 9010:9010 --name doctor_api_svc registry.xzlcorp.com/corp/doctor-api-service`

**NOTE** you might need to login to docker registry if not yet:
`docker login registry.xzlcorp.com
`

## API Doc
[This is api doc url](http://172.16.10.14:9007/docs/index.html)


