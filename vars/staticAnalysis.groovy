#!/usr/bin/env groovy

def call(boolean abortPipeline = false) {
    withSonarQubeEnv('SonarQube') {
        sh 'echo "Ejecución de las pruebas de calidad de código"'
    }
    timeout(time: 5, unit: 'MINUTES') {
        waitForQualityGate abortPipeline: abortPipeline
    }
}
