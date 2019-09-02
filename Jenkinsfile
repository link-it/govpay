pipeline {
  agent any
  stages {
    stage('compile') {
      steps {
        sh '/var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.1/bin/mvn clean install -Denv=installer_template'
      }
    }
    stage('build') {
      steps {
	sh './src/main/resources/scripts/jenkins.build.sh'
      }
      success{
        archiveArtifacts 'src/main/resources/setup/target/*.tgz'
      }
    }
    stage('install') {
      steps {
        sh 'sudo systemctl start wildfly@govpay'
	sh './src/main/resources/scripts/jenkins-checkgp.sh'
      }
    }
    stage('test') {
      steps {
        sh '''cd ./integration-test; /var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.1/bin/mvn clean test'''
      }
      post {
        always {
            junit 'integration-test/target/surefire-reports/*.xml'
        }
      }
    }
  }
}
