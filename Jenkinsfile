pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-17'
        }
    }
    stages {
        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Docker Image') {
            agent any
            steps {
                sh 'docker build -t springboot-app .'
            }
        }
        stage('Run Docker Container') {
            agent any
            steps {
                sh '''
                  docker rm -f springboot-app || true
                  docker run -d --name springboot-app -p 8081:8080 springboot-app
                '''
            }
        }
    }
}