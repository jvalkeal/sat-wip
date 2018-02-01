#!/bin/bash
set -e

source $(dirname $0)/common.sh
repository=$(pwd)/distribution-repository


pushd git-repo > /dev/null
cd custom-apps/skipper-server-with-drivers && ./gradlew clean build install -Dmaven.repo.local=${repository}
popd > /dev/null
