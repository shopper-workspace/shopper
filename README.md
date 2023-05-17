[![Build Status](https://dev.azure.com/erenpinaz/shopper/_apis/build/status%2Fshopper-workspace.shopper?branchName=master)](https://dev.azure.com/erenpinaz/shopper/_build/latest?definitionId=1&branchName=master)

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

## Register Debezium connectors

Register debezium connectors to start producing outbox events.

```
bash ./deploy/docker/postgres/create-connectors.sh
```

## Cleanup

To clean service containers:

```
docker-compose -f ./deploy/docker/docker-compose.yml --profile backend --profile infra down
```
