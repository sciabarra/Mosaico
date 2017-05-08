#!/bin/bash
# workaround for a docker bug in aws
if ! grep 8.8.8.8 /etc/resolv.conf
then echo "nameserver 8.8.8.8" | sudo tee -a /etc/resolv.conf
fi
export CONF=/usr/spark/conf/spark-env.sh
echo export SPARK_HOME=/usr/spark >$CONF
echo export HADOOP_HOME=/usr/hadoop >>$CONF
echo export PATH=\"/usr/hadoop/bin:/usr/spark/bin:$PATH\" >>$CONF
echo export SPARK_DIST_CLASSPATH=\"$(/usr/hadoop/bin/hadoop classpath)\" >>$CONF
echo export SPARK_DAEMON_MEMORY=1000m >>$CONF
echo export SPARK_WORKER_MEMORY=1000m >>$CONF
chmod +x $CONF
cd /usr/spark
if test -z "$SPARK_MASTER"
then exec bin/spark-class org.apache.spark.deploy.master.Master
else exec bin/spark-class org.apache.spark.deploy.worker.Worker "$SPARK_MASTER"
fi
