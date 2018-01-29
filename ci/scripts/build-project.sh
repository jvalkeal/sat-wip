#!/bin/bash
set -e

source $(dirname $0)/common.sh

pushd git-repo > /dev/null
./gradlew clean build
popd > /dev/null
