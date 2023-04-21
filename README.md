# shopper

Monorepo for shopper microservices

## Start Docker Containers for Local Testing

Start infrastructure containers.

```
docker-compose -f ./deploy/docker/docker-compose.yml --profile infra up -d
```

Create all the backend service images (images won't be pushed to hub).

```
mvn clean package -DskipTests
```

Then start backend service containers.

```
docker-compose -f ./deploy/docker/docker-compose.yml --profile backend up -d
```

## Stop Docker Containers

To clean service containers:

```
docker-compose -f ./deploy/docker/docker-compose.yml --profile backend --profile infra down
```