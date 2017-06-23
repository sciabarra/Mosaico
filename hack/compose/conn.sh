docker run --rm -ti -p 4041:4040 -e SPARK_MASTER=spark://spark-master:7077 --network ${2:-compose}_spark-network register.loc:500/zeppelin:4 /usr/bin/spark-shell
