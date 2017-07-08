pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps { 
	      sh 'docker login -u register -p password register.loc:500 && ./sbt all/dockerBuildAndPush'
            }
        }
   }
}
