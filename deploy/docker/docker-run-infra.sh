#!/bin/sh

set -o nounset
set -o pipefail
set -o errexit
set -o errtrace

# Variables
COMPOSE_FILE_PATH=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )/docker-compose.yml

# Messages
MSG_INFRA_INIT="Creating infrastructure containers"
MSG_INFRA_INIT_FAIL="Unable to create infrastructure containers"

# Pipeline
echo "$MSG_INFRA_INIT" && docker-compose -f "${COMPOSE_FILE_PATH}" --profile infra up -d || echo "${MSG_INFRA_INIT_FAIL}"