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

## upgrading

After changing spring boot starter from 2.5.5 to 2.5.6, project will no longer start.
It will fail with:
`Caused by: java.lang.IllegalStateException: Spring Data REST controller sk.qpp.poc.security.controller.AuthController must not use @RequestMapping on class level as this would cause double registration with Spring MVC!`
error.
This commit tries to solve problem by removing `@RequestMapping("/auth")` annotation from `AuthController` class.
To correct paths for controller, I believe that `/auth` needs to be added to all methods of the controller.

Problem comes when testing change.
After starting project (correctly), we issue "signup" request using curl:
```shell
curl 'http://localhost:9091/auth/signup' -iv -X POST -H 'Content-Type: application/json' -d '{"name":"some name","email":"qwer123@asdf.sk","password":"qwertu"}'
```

Instead of "created user" response we see problem with routing:
```json
{"timestamp":"2021-11-29T08:44:02.106+00:00","status":404,"error":"Not Found","message":"No message available","path":"/auth/signup"}
```
