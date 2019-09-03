pipeline {
  agent any
  stages {
    stage('build') {
      steps {
	sh '/var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.1/bin/mvn clean install -Denv=installer_template'
	sh 'sh ./src/main/resources/scripts/jenkins.build.sh'
      }
      post {
        success {
          archiveArtifacts 'src/main/resources/setup/target/*.tgz'
        }
      }
    }
    stage('install') {
      steps {
	sh 'sh ./src/main/resources/scripts/jenkins.cleanup.sh'
        sh 'sh ./src/main/resources/scripts/jenkins.install.sh'
        sh 'sudo systemctl start wildfly@govpay'
	sh 'sh ./src/main/resources/scripts/jenkins.checkgp.sh'
      }
    }
    stage('test') {
      steps {
        sh 'cd ./integration-test; /var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.1/bin/mvn clean test'
      }
      post {
        always {
            junit 'integration-test/target/surefire-reports/*.xml'
            sh 'tar -czvf ./integration-test/target/surefire-reports.tar.gz ./integration-test/target/surefire-reports/'
            archiveArtifacts 'integration-test/target/surefire-reports.tar.gz'
        }
      }
    }
  }
}
