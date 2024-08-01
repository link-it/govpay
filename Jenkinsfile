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
	sh 'JAVA_HOME=/usr/lib/jvm/java-11-openjdk /var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.1/bin/mvn install -Denv=installer_template -DnvdApiKey=$NVD_API_KEY'
	sh 'sh ./src/main/resources/scripts/jenkins.build.sh'
      }
      post {
        success {
          archiveArtifacts 'src/main/resources/setup/target/*.tgz'
        }
      }
    }
    stage('dependency-check') {
      steps {
		dependencyCheckPublisher pattern: 'target/dependency-check-report.xml'
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
        sh 'cd ./integration-test; JAVA_HOME=/etc/alternatives/jre_1.8.0 /var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.1/bin/mvn clean test' 
      }
      post {
        always {
            junit 'integration-test/target/surefire-reports/*.xml'
            sh 'tar -cvf ./integration-test/target/surefire-reports.tar ./integration-test/target/surefire-reports/ --transform s#./integration-test/target/##'
            sh 'tar -uvf ./integration-test/target/test-logs.tar /var/log/govpay/* --transform s#var/log/##'
            sh 'tar -uvf ./integration-test/target/test-logs.tar /opt/wildfly/standalone_govpay/log/* --transform s#opt/wildfly/standalone_govpay/log/#wildfly/#'
            sh 'gzip ./integration-test/target/surefire-reports.tar'
            archiveArtifacts 'integration-test/target/surefire-reports.tar.gz'
        }
      }
    }
  }
}
