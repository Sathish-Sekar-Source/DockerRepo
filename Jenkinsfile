pipeline {
    agent any

    tools {
        maven 'myMaven' // configure Maven under Jenkins -> Global Tool Configuration
        jdk 'jdk'      // configure JDK (Java 11 or 17) in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/Sathish-Sekar-Source/DockerRepo.git'
            }
        }

        stage('Start Selenium Grid') {
            steps {
                sh 'docker compose -f docker-compose.yml up -d'
                sh 'sleep 15' // wait for Grid to be fully ready
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Stop Selenium Grid') {
            steps {
                sh 'docker compose -f docker-compose.yml down -v'
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
