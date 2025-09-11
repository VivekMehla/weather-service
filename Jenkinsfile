pipeline {
    agent none
    stages {
        stage('Build with Maven') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-17'
                    args '-v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                echo "Building project with Maven..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            agent any
            steps {
                echo "Building Docker image..."
                sh '''
                  docker build -t springboot-app .
                '''
            }
        }

        stage('Run Docker Container') {
            agent any
            steps {
                echo "Stopping old container (if exists) and running new one..."
                sh '''
                  docker rm -f springboot-app || true
                  docker run -d --name springboot-app -p 8081:8080 springboot-app
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline finished."
        }
    }
}