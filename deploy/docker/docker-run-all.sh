#!/bin/sh

set -o nounset
set -o pipefail
set -o errexit
set -o errtrace

# Variables
COMPOSE_FILE_PATH=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )/docker-compose.yml
INFRA_WAIT_DURATION=10s

# Messages
MSG_BUILD_INIT="Building service images"
MSG_BUILD_FAIL="Building projects failed"
MSG_INFRA_INIT="Creating infrastructure containers"
MSG_INFRA_INIT_FAIL="Unable to create infrastructure containers"
MSG_INFRA_WAIT="Waiting ${INFRA_WAIT_DURATION} for infrastructure containers to initialize"
MSG_BACKEND_INIT="Creating backend service containers"
MSG_BACKEND_INIT_FAIL="Unable to create infrastructure containers"

# Pipeline
echo "${MSG_BUILD_INIT}" && mvn clean package -DSkipTests || (echo "${MSG_BUILD_FAIL}" && exit 1)
echo "${MSG_INFRA_INIT}" && docker-compose -f "${COMPOSE_FILE_PATH}" --profile infra up -d || (echo "${MSG_INFRA_INIT_FAIL}" && exit 1)
echo "${MSG_INFRA_WAIT}" && sleep ${INFRA_WAIT_DURATION}
echo "${MSG_BACKEND_INIT}" && docker-compose -f "${COMPOSE_FILE_PATH}" --profile backend up -d || (echo "${MSG_BACKEND_INIT_FAIL}" && exit 1)
