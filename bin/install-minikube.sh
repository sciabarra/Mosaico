#!/usr/bin/env bash
export MINIKUBE_VERSION=v0.6.0
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
which curl >/dev/null || ( echo "you need curl in path" ; exit 1)
export ARCH=$(uname -m)  
if test "$ARCH" = "x86_64" 
then export ARCH=amd64
else echo "Only x86_64 is supported" ; exit 1
fi
if [ "$(uname)" == "Darwin" ]; then
    export GOOS=darwin
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    export GOOS=linux
else
    echo "OS not supported" ; exit 1
fi
if ! test -e $HERE/minikube ; then
  URL=http://storage.googleapis.com/minikube/releases/${MINIKUBE_VERSION}/minikube-${GOOS}-${ARCH}
  echo "Downloading $URL"
  curl "$URL" >$HERE/minikube
  chmod +x $HERE/minikube
fi
test -x "$HERE/minikube" || ( echo "Cannot download minikube"; exit 1)
