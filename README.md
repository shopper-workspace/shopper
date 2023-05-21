[![Build Status](https://dev.azure.com/erenpinaz-devops/shopper/_apis/build/status%2Fshopper-workspace.shopper?branchName=master)](https://dev.azure.com/erenpinaz-devops/shopper/_build/latest?definitionId=2&branchName=master)

[![shopper-product-service](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-product-service.yml/badge.svg)](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-product-service.yml)
[![shopper-payment-service](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-payment-service.yml/badge.svg)](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-payment-service.yml)
[![shopper-order-service](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-order-service.yml/badge.svg)](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-order-service.yml)
[![shopper-notification-service](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-notification-service.yml/badge.svg)](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-notification-service.yml)
[![shopper-api-gateway](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-api-gateway.yml/badge.svg)](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-api-gateway.yml)
[![shopper-discovery-server](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-discovery-server.yml/badge.svg)](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-discovery-server.yml)
[![shopper-inventory-service](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-inventory-service.yml/badge.svg)](https://github.com/shopper-workspace/shopper/actions/workflows/shopper-inventory-service.yml)

# shopper

Monorepo for shopper microservices

## Start Docker Containers for Local Testing

Start infrastructure containers.

```
docker-compose -f ./deploy/docker/docker-compose.yml --profile infra up -d
```

Create all the backend service images (images won't be pushed to hub).

```
export CONTAINER_REGISTRY=shopper
mvn clean package jib:dockerBuild -am -ntp -DskipTests
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
