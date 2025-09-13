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
                echo "Listing workspace contents for debugging:"
                sh 'ls -R $WORKSPACE'
            }
        }

        stage('Build JAR with Maven') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17'
                    args '--rm -v $WORKSPACE:$WORKSPACE -v /Users/Shared/m2-repo:/root/.m2'
                }
            }
            steps {
                script {
                    // Try to locate pom.xml automatically
                    def pomPath = sh(
                        script: "find $WORKSPACE -name pom.xml | head -n 1",
                        returnStdout: true
                    ).trim()

                    if (!pomPath) {
                        error "No pom.xml found in workspace!"
                    }

                    echo "Using POM: ${pomPath}"

                    sh "mvn clean install -DskipTests -f \"${pomPath}\""
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Find the JAR dynamically so you don’t need to hardcode version
                    def jarFile = sh(
                        script: "ls target/*.jar | grep -v 'original' | head -n 1",
                        returnStdout: true
                    ).trim()

                    if (!jarFile) {
                        error "No JAR built in target/"
                    }

                    echo "Building Docker image with JAR: ${jarFile}"
                    docker.build(IMAGE_NAME, "--build-arg JAR_FILE=${jarFile} .")
                }
            }
        }

        stage('Run Container') {
            steps {
                script {
                    sh "docker rm -f ${CONTAINER_NAME} || true"
                    sh "sleep 2"
                    def runOutput = sh(
                        script: "docker run -d -p 8081:8080 --name ${CONTAINER_NAME} ${IMAGE_NAME}",
                        returnStdout: true
                    ).trim()
                    echo "Container started with ID: ${runOutput}"
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
