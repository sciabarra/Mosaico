#!/usr/bin/env bash
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DIR=helm-classic
CMD=helmc
VERSION=latest
which curl >/dev/null || ( echo "you need curl in path" ; exit 1)
export ARCH=$(uname -m)  
if test "$ARCH" = "x86_64" 
then export ARCH=amd64
fi
if [ "$(uname)" == "Darwin" ]; then
    export OS=darwin
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    export OS=linux
else
    echo "OS not supported" ; exit 1
fi
if ! test -e $HERE/$CMD ; then
  URL=http://storage.googleapis.com/$DIR/$CMD-${VERSION}-${OS}-${ARCH}
  echo "Downloading $URL"
  curl "$URL" >$HERE/$CMD
  chmod +x $HERE/$CMD
fi
if test -x "$HERE/$CMD" 
then sudo cp "$HERE/$CMD" /usr/local/bin/
else echo "Cannot download $CMD"
fi
