#!/bin/bash
# workaround for a docker bug in aws
if ! grep 8.8.8.8 /etc/resolv.conf
then echo "nameserver 8.8.8.8" | sudo tee -a /etc/resolv.conf
fi
CFG=/usr/zeppelin/conf/zeppelin-env.sh
echo "*** zeppelin-env ***"
env \
  MASTER="${SPARK_MASTER:-local[*]}" \
  SPARK_HOME=/usr/spark \
  HADOOP_HOME=/usr/hadoop \
  PATH="$HADOOP_HOME/bin:$SPARK_HOME/bin:$PATH" \
  SPARK_DIST_CLASSPATH=$(/usr/hadoop/bin/hadoop classpath) \
 | sed -E -e 's/"/\\\"/g' -e 's/^(\w+)=/export \1="/' -e 's/$/"/' \
 | tee $CFG
chmod +x $CFG
echo "*** zeppelin ***"
cd /usr/zeppelin
/usr/zeppelin/bin/zeppelin.sh
