#!/bin/bash
cd /etc/serf
join=""
for i in ${SERF:-$(hostname)}
do join="$join --join $i"
done
exec /usr/bin/serf agent \
  -bind $(hostname -i) \
  $join
