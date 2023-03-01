pipeline {
  agent any
  options { 
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '2'))
  }
  stages {
    stage('cleanup') {
      steps {
        sh 'sh ./src/main/resources/scripts/jenkins.cleanup.sh'
        sh '/var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.1/bin/mvn clean'
      }
    }
    stage('build') {
      steps {
	sh 'JAVA_HOME=/usr/lib/jvm/java-11 /var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.1/bin/mvn install -Denv=installer_template'
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
        sh 'sh ./src/main/resources/scripts/jenkins.install.sh'
        sh 'sudo systemctl start wildfly-26.1.3.Final@standalone'
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
            sh 'tar -cvf ./integration-test/target/test-logs.tar ./integration-test/target/surefire-reports/ --transform s#./integration-test/target/##'
            sh 'gzip ./integration-test/target/test-logs.tar'
            archiveArtifacts 'integration-test/target/test-logs.tar.gz'
        }
      }
    }
  }
}
