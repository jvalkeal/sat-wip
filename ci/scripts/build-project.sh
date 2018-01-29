#!/bin/bash
set -e

source $(dirname $0)/common.sh

pushd git-repo > /dev/null
echo $FOO_BAR
./gradlew clean build
popd > /dev/null
