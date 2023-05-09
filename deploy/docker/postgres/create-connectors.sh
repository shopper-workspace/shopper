#!/bin/bash

# Variables
COLLECTOR_FILE_PATH="$(
    cd -- "$(dirname "$0")" >/dev/null 2>&1
    pwd -P
)"

curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type: application/json' \
    -H 'Accept: application/json' \
    -d @${COLLECTOR_FILE_PATH}/outbox_product_connector.json

curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type: application/json' \
    -H 'Accept: application/json' \
    -d @${COLLECTOR_FILE_PATH}/outbox_inventory_connector.json

curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type: application/json' \
    -H 'Accept: application/json' \
    -d @${COLLECTOR_FILE_PATH}/outbox_payment_connector.json

curl -X POST http://localhost:8083/connectors \
    -H 'Content-Type: application/json' \
    -H 'Accept: application/json' \
    -d @${COLLECTOR_FILE_PATH}/outbox_order_connector.json