# TEMPERATURE PROVIDER recruitment task

## requirements
* read file & present yearly average temperatures for a given city via endpoint
* file content may change during runtime

## how to run
* unix:
```bash
sh run.sh
```
Application starts on port 8080.

Data file is located in /date-file directory in repository.

## example request
```bash
curl --location 'http://localhost:8080/temperature-provider/api/cities/Warszawa/temperature/average' \
--header 'Accept: application/json'
```
or

[IntelliJ http request](http/temperature-provider-example.http)