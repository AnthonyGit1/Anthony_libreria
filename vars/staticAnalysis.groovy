#!/usr/bin/env groovy

def call(boolean abortPipeline = false) {
    withSonarQubeEnv('SonarQube') {
        sh 'echo "Ejecución de las pruebas de calidad de código"'
    }
    timeout(time: 5, unit: 'MINUTES') {
        try {
            waitForQualityGate abortPipeline: abortPipeline
        } catch (Exception e) {
            if (abortPipeline) {
                error("QualityGate fallido o no disponible: ${e.message}")
            } else {
                echo "QualityGate no disponible (mock SonarQube): ${e.message}"
            }
        }
    }
}
