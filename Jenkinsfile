pipeline {
    agent any

    tools {
        maven 'myMaven'
        jdk 'jdk'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/Sathish-Sekar-Source/DockerRepo.git'
            }
        }

        stage('Start Selenium Grid') {
            steps {
                bat 'docker-compose -f docker-compose.yml up -d'
                bat 'powershell -Command "Start-Sleep -Seconds 15"'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Stop Selenium Grid') {
            steps {
                bat 'docker-compose -f docker-compose.yml down -v'
            }
        }
    }

    post {
            always {
                // Archive ExtentReport.html
                archiveArtifacts artifacts: 'target/ExtentReport.html', allowEmptyArchive: true

                // Optionally, publish HTML report in Jenkins UI
                publishHTML([
                    reportDir: 'target',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report',
                    allowMissing: true,
                    alwaysLinkToLastBuild: true
                ])
            }
        }
}
