#!/bin/bash

# Usage: Initial script put in `docker-entrypoint-initdb.d` directory
# Use bash for least error prones caused by shell syntax.

set -o errexit
set -ex

readonly REQUIRED_ENV_VARS=(
    "POSTGRES_DB"
    "POSTGRES_USER"
    "POSTGRES_PASSWORD"
    "KEYCLOAK_DB_NAME"
    "KEYCLOAK_DB_USER"
    "KEYCLOAK_DB_PASS"
    "PRODUCT_DB_NAME"
    "PRODUCT_DB_USER"
    "PRODUCT_DB_PASS"
    "INVENTORY_DB_NAME"
    "INVENTORY_DB_USER"
    "INVENTORY_DB_PASS"
    "PAYMENT_DB_NAME"
    "PAYMENT_DB_USER"
    "PAYMENT_DB_PASS"
    "ORDER_DB_NAME"
    "ORDER_DB_USER"
    "ORDER_DB_PASS")

main() {
    check_env_vars_set

    if [ -n "$KEYCLOAK_DB_NAME" ]; then
        create_user_and_database "$KEYCLOAK_DB_NAME" "$KEYCLOAK_DB_USER" "$KEYCLOAK_DB_PASS"
    fi

    if [ -n "$PRODUCT_DB_NAME" ]; then
        create_user_and_database "$PRODUCT_DB_NAME" "$PRODUCT_DB_USER" "$PRODUCT_DB_PASS"
    fi

    if [ -n "$INVENTORY_DB_NAME" ]; then
        create_user_and_database "$INVENTORY_DB_NAME" "$INVENTORY_DB_USER" "$INVENTORY_DB_PASS"
    fi

    if [ -n "$PAYMENT_DB_NAME" ]; then
        create_user_and_database "$PAYMENT_DB_NAME" "$PAYMENT_DB_USER" "$PAYMENT_DB_PASS"
    fi

    if [ -n "$ORDER_DB_NAME" ]; then
        create_user_and_database "$ORDER_DB_NAME" "$ORDER_DB_USER" "$ORDER_DB_PASS"
    fi
}

check_env_vars_set() {
    for required_env_var in "${REQUIRED_ENV_VARS[@]}"; do
        if [[ -z "${!required_env_var}" ]]; then
            echo "Error:
    Environment variable '$required_env_var' not set.
    Make sure you have the following environment variables set:"
            "${REQUIRED_ENV_VARS[@]}"
            "Aborting."
            exit 1
        fi
    done
}

function create_user_and_database() {
    local DB_DATABASE=$1
    local DB_USER=$2
    local DB_PASS=$3
    echo "  Creating user and database $DB_DATABASE"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        CREATE USER "$DB_USER" WITH ENCRYPTED PASSWORD '${DB_PASS}' LOGIN CREATEDB REPLICATION;
        CREATE DATABASE "$DB_DATABASE" OWNER "$DB_USER";
        GRANT ALL PRIVILEGES ON DATABASE "$DB_DATABASE" TO "$DB_USER";
EOSQL
}

# Executes the main routine with environment variables
main "$@"
