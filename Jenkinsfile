pipeline {

    agent any

    
    stages {

        stage('Packaging') {

            steps {
                
                sh 'docker build --pull --rm -f Dockerfile -t fublogapi:latest .'
                
            }
        }

        stage('Push to DockerHub') {

            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                    sh 'docker tag fublogapi:latest chalsfptu/fublogapi:latest'
                    sh 'docker push chalsfptu/fublogapi:latest'
                }
            }
        }

        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull chalsfptu/fublogapi:latest'
                sh 'docker container stop fubloglogin || echo "this container does not exist" '
                sh 'echo y | sudo docker container prune '
                sh 'docker container run -d --rm --name fubloglogin -p 8084:8080  chalsfptu/fublogapi '
            }
        }
        
 
    }
    post {
        // Clean after build
        always {
            cleanWs()
        }
    }
}
