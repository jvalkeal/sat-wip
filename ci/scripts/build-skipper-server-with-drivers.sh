#!/bin/bash
set -e

source $(dirname $0)/common.sh
repository=$(pwd)/distribution-repository
echo distribution-repository ${repository}
echo ls1
ls

pushd git-repo > /dev/null
cd custom-apps/skipper-server-with-drivers && ./gradlew clean build install -Dmaven.repo.local=${repository}
echo ls2
ls
popd > /dev/null
