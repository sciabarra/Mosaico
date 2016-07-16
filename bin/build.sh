#!/bin/bash
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE=$(dirname $HERE)
cd $BASE/docker
if test -z "$1"
then IMAGES=$(echo */docker.sbt | sed -e 's!/docker.sbt!!g' -e 's/-/_/g')
else IMAGES="$@"
fi
for i in $IMAGES
do prj=$(echo $i | sed -e 's/-/_/g')
   sbt "$prj/docker"
done
