pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps { 
                sh 'docker run -d -p 500:5000 --rm --name registry registry:2'
                sh './sbt -Dprofile=register all/dockerBuildAndPush'
            }
        }
   }
}
