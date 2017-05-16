#!/bin/bash
# workaround for a docker bug in aws
if ! grep 8.8.8.8 /etc/resolv.conf
then echo "nameserver 8.8.8.8" | sudo tee -a /etc/resolv.conf
fi
cd /usr/spark
env \
  MASTER="${SPARK_MASTER:-local[*]}" \
  SPARK_HOME=/usr/spark \
  HADOOP_HOME=/usr/hadoop \
  PATH="$HADOOP_HOME/bin:$SPARK_HOME/bin:$PATH" \
  SPARK_DIST_CLASSPATH=$(/usr/hadoop/bin/hadoop classpath) \
  bin/spark-shell
