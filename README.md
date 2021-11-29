# poc-springRequestMapping
Proof of concept for problem raised when upgrading spring version.
Problem has raised when upgraded from spring boot 2.5.5 to 2.5.6.

## running example
```shell script
mvn clean spring-boot:run 
```

## signup and login
```shell script
curl 'http://localhost:9091/auth/signup' -iv -X POST -H 'Content-Type: application/json' -d '{"name":"some name","email":"qwer123@asdf.sk","password":"qwertu"}'
curl 'http://localhost:9091/auth/login' -iv -X POST -H 'Content-Type: application/json' -d '{"email":"qwer123@asdf.sk","password":"qwertu"}'
curl 'http://localhost:9091/api/secreetResource/42' -iv -H 'Authorization: Bearer eyJshort.eyJzdWIiOzOTl9.aetssVVVVVVVVVVdui-HlZTFFFFFFFFFig'
```
