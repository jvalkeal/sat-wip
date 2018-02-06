#!/bin/bash
set -e

source $(dirname $0)/common.sh
repository=$(pwd)/distribution-repository

pushd git-repo > /dev/null
echo $ARTIFACTORY_USERNAME $BUILD_NAME
echo $ARTIFACTORY_PASSWORD | docker login -u $ARTIFACTORY_USERNAME --password-stdin springsource-docker-private-local.jfrog.io
./gradlew clean build
tar zcf ${repository}/spring-cloud-skipper-acceptance-tests.tar.gz spring-cloud-skipper-acceptance-tests/build/test-docker-logs
popd > /dev/null
