docker-compose -f cloud/compose/spark.yml kill
yes | docker-compose -f cloud/compose/spark.yml rm
sbt ${1:-all}/docker
