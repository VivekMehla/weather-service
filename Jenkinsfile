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
                    args '--rm -v /Users/Shared/m2-repo:/root/.m2'
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
                stash includes: 'target/*.jar', name: 'app-jar'
            }
        }

        stage('Build Docker Image') {
            steps {
                unstash 'app-jar'
                sh 'ls -l target'   // ✅ debug check
                sh "docker build --no-cache -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Run Container') {
            steps {
                script {
                    sh "docker rm -f ${CONTAINER_NAME} || true"
                    sh "docker run -d -p 8081:8080 --name ${CONTAINER_NAME} ${IMAGE_NAME}"
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