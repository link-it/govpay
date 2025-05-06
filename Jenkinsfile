pipeline {
  agent any
  options { 
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '2'))
  }
  environment {
    JACOCO_EXEC    = "/tmp/jacoco.exec"
    JACOCO_XML     = "target/jacoco.xml"
    JACOCO_HTML    = "target/jacoco-html"
    JACOCO_CSV     = "target/jacoco.csv"
  }
  stages {
    stage('cleanup') {
      steps {
        sh 'sh ./src/main/resources/scripts/jenkins.cleanup.sh'
        sh '/opt/apache-maven-3.6.3/bin/mvn clean'
      }
    }
    stage('build') {
      steps {
	sh 'JAVA_HOME=/usr/lib/jvm/java-21-openjdk /opt/apache-maven-3.6.3/bin/mvn install spotbugs:spotbugs -Denv=installer_template -DnvdApiKey=$NVD_API_KEY'
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
    stage('spotbugs-analysis') {
      steps {
      	recordIssues sourceCodeRetention: 'LAST_BUILD', tools: [spotBugs(id: 'spotbugs', name: 'SpotBugs', pattern: '**/target/spotbugsXml.xml', useRankAsPriority: true)]
      }
    }
    stage('install') {
      steps {
        sh 'sh ./src/main/resources/scripts/jenkins.install.sh'
        sh 'sudo systemctl start wildfly-26.1.3.Final@ndpsym tomcat_govpay'
	    sh 'sh ./src/main/resources/scripts/jenkins.checkgp.sh'
      }
    }
    stage('test') {
      steps {
        sh 'cd ./integration-test; JAVA_HOME=/etc/alternatives/jre_1.8.0 /opt/apache-maven-3.6.3/bin/mvn clean test -Dkarate.options="classpath:test/api/pagamento/v2/avvisi/get/verifica-avviso-basic.feature" -Dtest=test.workflow.WorkflowTest' 
      }
      post {
        always {
			sh 'sudo systemctl stop wildfly@govpay wildfly-26.1.3.Final@standalone wildfly-26.1.3.Final@ndpsym tomcat_govpay'
            junit 'integration-test/target/surefire-reports/*.xml'
            sh 'tar -cvf ./integration-test/target/surefire-reports.tar ./integration-test/target/surefire-reports/ --transform s#./integration-test/target/##'
            sh 'gzip ./integration-test/target/surefire-reports.tar'
            archiveArtifacts 'integration-test/target/surefire-reports.tar.gz'
        }
      }
    }
    stage('sonarqube-analysis') {
	  steps {
		sh """
          # raccogliamo tutte le classi dei moduli
	      classArgs=\$(find . -type d -path "*/target/classes" \
	                  | sed "s#^#--classfiles #" \
	                  | xargs)
	
	      # raccogliamo tutte le sorgenti dei moduli
	      srcArgs=\$(find . -type d -path "*/src/main/java" \
	                | sed "s#^#--sourcefiles #" \
	                | xargs)
	
          JAVA_HOME=/usr/lib/jvm/java-21-openjdk java -jar $JACOCO_CLI report ${JACOCO_EXEC} \$classArgs \$srcArgs --xml ${JACOCO_XML} --html ${JACOCO_HTML} --csv ${JACOCO_CSV} 
           """
	    sh """
	    	JAVA_HOME=/usr/lib/jvm/java-21-openjdk /opt/apache-maven-3.6.3/bin/mvn sonar:sonar -Dsonar.projectKey=GovPay -Dsonar.token=$GOVPAY_SONAR_TOKEN \\
	    	-Dsonar.login=$GOVPAY_SONAR_USER -Dsonar.password=$GOVPAY_SONAR_PWD \\
	    	 -Dsonar.host.url=http://localhost:9000 -Dsonar.coverage.jacoco.xmlReportPaths=${JACOCO_XML}
	       """
	  }
	  post {
        always {
		  archiveArtifacts 'target/jacoco.xml'
		}
	  }
	}
	stage('vulnerabilities-collector') {
		steps {
        sh 'sh ./src/main/resources/scripts/jenkins.vulnerabilities-collector.sh'
      }
	}
  }
}
