# search-service
```
to build the project run './mvnw clean package' from root folder
```
```
to run built project run 'java -jar target/search-service-1.0-SNAPSHOT.jar' from root folder
```

Application starts on port -8080 (can be customized in application.yaml). 
Test request is placed in test-request.xml.

DataSource is customized in application.yaml

Mysql database is starting in docker with run script 'config/docker/mysql/run.sh', DDL applies automatically.  