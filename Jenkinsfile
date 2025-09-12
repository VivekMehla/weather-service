pipeline {
    agent any

    environment {
        IMAGE_NAME = "weather-service-image"
        CONTAINER_NAME = "weather-service-container"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR with Maven') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17'
                    args '-v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    app = docker.build("${IMAGE_NAME}")
                }
            }
        }

        stage('Run Container') {
            steps {
                script {
                    sh "docker rm -f ${CONTAINER_NAME} || true"
                    app.run("-d -p 8080:8080 --name ${CONTAINER_NAME}")
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished (success or failure)'
        }
        success {
            echo 'Application built and deployed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
