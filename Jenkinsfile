pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps { 
                sh './sbt -Dprofile=register all/dockerBuildAndPush'
            }
        }
   }
}
