#!/bin/bash
set -e

source $(dirname $0)/common.sh
repository=$(pwd)/distribution-repository
echo distribution-repository ${repository}
echo xxx1
ls

pushd git-repo > /dev/null
echo xxx2
pwd
echo xxx3
ls
cd custom-apps/skipper-server-with-drivers && ./gradlew clean build install -Dmaven.repo.local=${repository}
echo xxx4
find ${repository}
echo xxx5
ls
popd > /dev/null
