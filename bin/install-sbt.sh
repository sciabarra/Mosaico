#!/bin/bash
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE=$(dirname $HERE)
which curl >/dev/null || ( echo "you need curl in path" ; exit 1)
which java >/dev/null || ( echo "you need java in path" ; exit 1)
#test -e $HERE/sbt-launch.jar || curl -o $HERE/sbt-launch.jar https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.13.11/sbt-launch.jar
sudo cp $HERE/sbt /usr/local/bin/
cd $HERE/../docker
sbt exit  
cd -

