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

        stage('Build & Package Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Run Container') {
            steps {
                sh "docker rm -f ${CONTAINER_NAME} || true"
                sh "docker run -d -p 8082:8080 --name ${CONTAINER_NAME} ${IMAGE_NAME}"
            }
        }
    }

    post {
        success {
            echo '✅ Application built and deployed successfully!'
        }
        failure {
            echo '❌ Pipeline failed.'
        }
    }
}
