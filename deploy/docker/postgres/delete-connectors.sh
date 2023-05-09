#!/bin/bash

curl -X DELETE http://localhost:8083/connectors/outbox_product_connector

curl -X DELETE http://localhost:8083/connectors/outbox_inventory_connector

curl -X DELETE http://localhost:8083/connectors/outbox_payment_connector

curl -X DELETE http://localhost:8083/connectors/outbox_order_connector
