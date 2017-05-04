#!/bin/bash
cd /usr/cassandra
mkdir /usr/cassandra/logs
touch /usr/cassandra/logs/system.log
sed -i -e "s/start_rpc: false/start_rpc: true/" /usr/cassandra/conf/cassandra.yaml
sed -i -e "s/listen_address: localhost/listen_address: $(hostname -i)/" /usr/cassandra/conf/cassandra.yaml
sed -i -e "s/rpc_address: localhost/rpc_address: $(hostname -i)/" /usr/cassandra/conf/cassandra.yaml
if test -n "$CASSANDRA_MASTER"
then sed -i -e 's/seeds: "127.0.0.1"/seeds: "$CASSANDRA_MASTER"/' /usr/cassandra/conf/cassandra.yaml
fi
sed -i -e "s/^#MAX_HEAP_SIZE=.*/MAX_HEAP_SIZE=1G/" /usr/cassandra/conf/cassandra-env.sh
sed -i -e "s/^#HEAP_NEWSIZE=.*/HEAP_NEWSIZE=100M/" /usr/cassandra/conf/cassandra-env.sh
/usr/cassandra/bin/cassandra -f -R
