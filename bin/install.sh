#!/bin/bash
OLDPWD="$(pwd)"
cd $(dirname $0)
for i in sbt kubectl minikube helmc 
do echo ">>> $i" 
   bash install-$i.sh
   echo "<<< $i"
done
cd "$OLDPWD"
