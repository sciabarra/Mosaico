#!/bin/bash
# workaround for a docker bug in aws
if ! grep 8.8.8.8 /etc/resolv.conf
then echo "nameserver 8.8.8.8" | sudo tee -a /etc/resolv.conf
fi
CONF=/usr/zeppelin/conf/zeppelin-env.sh
echo export SPARK_HOME=/usr/spark >$CONF
echo export HADOOP_HOME=/usr/hadoop >>$CONF
echo export PATH=$HADOOP_HOME/bin:$SPARK_HOME/bin:$PATH >>$CONF
echo export SPARK_DIST_CLASSPATH=$(/usr/hadoop/bin/hadoop classpath) >>$CONF
if test -n "$SPARK_MASTER"
then echo "export MASTER=$SPARK_MASTER" >>$CONF
fi
cd /usr/zeppelin
/usr/zeppelin/bin/zeppelin.sh
