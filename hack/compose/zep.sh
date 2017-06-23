docker run --rm -ti -p 4041:4040 -e SPARK_MASTER=spark://spark-master:7077 --network ${1?:compose dir}_spark-network register.loc:500/zeppelin:4 
