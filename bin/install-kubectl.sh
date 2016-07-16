#!/usr/bin/env bash
export K8S_VERSION=v1.3.0
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
which docker >/dev/null || ( echo "you need docker in path" ; exit 1)
which curl >/dev/null || ( echo "you neeed curl in path" ; exit 1)
export ARCH=$(uname -m)  
export EXT=""
if test "$ARCH" = "x86_64" ; then export ARCH=amd64; fi
if [ "$(uname)" == "Darwin" ]; then
    export GOOS=darwin
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    export GOOS=linux
elif [ "$(expr substr $(uname -s) 1 5)" == "MINGW" ]; then
    export GOOS=windows  
    export EXT=".exe"
else
    echo "Cannot determine a supported OS" ; exit 1
fi
if ! test -e $HERE/kubectl ; then
  URL=http://storage.googleapis.com/kubernetes-release/release/${K8S_VERSION}/bin/${GOOS}/${ARCH}/kubectl$EXT
  echo "Downloading $URL"
  curl "$URL" >$HERE/kubectl$EXT
  chmod +x $HERE/kubectl$EXT
fi
if test -x "$HERE/kubectl$EXT" 
then sudo cp $HERE/kubectl$EXT /usr/local/bin/
else echo "Cannot download kubectl"
fi
