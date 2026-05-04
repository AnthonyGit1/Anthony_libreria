#!/usr/bin/env groovy

def call(boolean abortPipeline = false, String branchName = null) {
    withSonarQubeEnv('SonarQube') {
        sh "${tool('SonarQubeScanner')}/bin/sonar-scanner"
    }

    boolean shouldAbort = abortPipeline

    if (!shouldAbort) {
        String branch = branchName ?: env.BRANCH_NAME ?: env.GIT_BRANCH ?: ''
        branch = branch.replaceAll('^origin/', '')
        echo "Rama detectada: '${branch}'"

        if (branch == 'master' || branch.startsWith('hotfix')) {
            shouldAbort = true
            echo "Rama '${branch}' coincide con la heurística → abortPipeline = true"
        } else {
            echo "Rama '${branch}' no requiere abortar el pipeline"
        }
    } else {
        echo "Parámetro abortPipeline=true → se abortará si falla el QualityGate"
    }

    timeout(time: 5, unit: 'MINUTES') {
        waitForQualityGate abortPipeline: shouldAbort
    }
}
