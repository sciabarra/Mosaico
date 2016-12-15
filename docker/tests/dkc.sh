#!/bin/bash
if test -z "$DKC"
then echo Please set DKC to the compose file ; exit
fi
docker-compose -f $DKC "$@"
