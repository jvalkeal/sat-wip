#!/bin/bash
set -e

source $(dirname $0)/common.sh

pushd git-repo > /dev/null
echo $ARTIFACTORY_USERNAME
echo $ARTIFACTORY_PASSWORD | docker login -u $ARTIFACTORY_USERNAME --password-stdin springsource-docker-private-local.jfrog.io
./gradlew clean build --refresh-dependencies
popd > /dev/null
