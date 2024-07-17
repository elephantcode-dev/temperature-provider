./gradlew bootJar
docker build -t temperature-provider:latest .
docker run -p 8080:8080 -v "$(pwd)/data-file":/home/nonroot/data-file -d --name temperature-provider temperature-provider:latest