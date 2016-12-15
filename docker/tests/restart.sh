#!/bin/bash
TEST=${1:?test file without test- and .yml}
shift
docker-compose -f test-$TEST.yml kill
docker-compose -f test-$TEST.yml rm -f
docker-compose -f test-$TEST.yml up "$@"
