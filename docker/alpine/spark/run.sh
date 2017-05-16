#!/bin/bash
# workaround for a docker bug in aws
if ! grep 8.8.8.8 /etc/resolv.conf
then echo "nameserver 8.8.8.8" | sudo tee -a /etc/resolv.conf
fi
# configure env for spark
CFG=/usr/spark/conf/spark-env.sh
echo "*** spark-env ***"
env \
 SPARK_HOME=/usr/spark \
 HADOOP_HOME=/usr/hadoop \
 PATH="/usr/hadoop/bin:/usr/spark/bin:$PATH" \
 SPARK_DIST_CLASSPATH="$(/usr/hadoop/bin/hadoop classpath)" \
 | sed -E -e 's/"/\\\"/g' -e 's/^(\w+)=/export \1="/' -e 's/$/"/' \
 | tee $CFG
chmod +x $CFG
echo "*** spark ***"
cd /usr/spark
mkdir logs
if test -z "$SPARK_MASTER"
then exec bin/spark-class org.apache.spark.deploy.master.Master
else exec bin/spark-class org.apache.spark.deploy.worker.Worker "$SPARK_MASTER"
fi
