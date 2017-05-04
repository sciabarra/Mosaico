#!/bin/bash
export SPARK_HOME=/usr/spark
export HADOOP_HOME=/usr/hadoop
export PATH=$HADOOP_HOME/bin:$SPARK_HOME/bin:$PATH
export SPARK_DIST_CLASSPATH=$(hadoop classpath)
cd /usr/zeppelin
if test -n "$SPARK_MASTER"
then echo "export MASTER=$SPARK_MASTER" >/usr/zeppelin/conf/zeppelin-env.sh
fi
/usr/zeppelin/bin/zeppelin.sh
