docker rmi -f sciabarra/test-abuild:1 sciabarra/test-common:1
rm common/target/*.apk
sbt -Dmosaico.debug -Dmosaico.trace=prp common/docker
