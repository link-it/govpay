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
        sh 'cd ./integration-test; JAVA_HOME=/etc/alternatives/jre_1.8.0 /opt/apache-maven-3.6.3/bin/mvn clean test -Dkarate.options="classpath:test/api/backoffice/v1/applicazioni" -Dtest=test.workflow.WorkflowTest' 
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
	    	JAVA_HOME=/usr/lib/jvm/java-21-openjdk /opt/apache-maven-3.6.3/bin/mvn sonar:sonar \\
	    	-Dsonar.projectKey=link-it_govpay -Dsonar.organization=link-it -Dsonar.token=$SONAR_CLOUD_TOKEN \\
	    	-Dsonar.java.source=21 -Dsonar.host.url=https://sonarcloud.io -Dsonar.coverage.jacoco.xmlReportPaths=${JACOCO_XML} \\
	    	-Dsonar.nodejs.executable=/opt/nodejs/22.14.0/bin/node \\
	    	-Dsonar.modules=jars/api-commons,jars/appio-beans,jars/orm-beans,jars/client-api-ente,jars/xml-adapters,jars/core-beans,jars/jppapdp-beans,jars/pagopa-beans,jars/core,jars/stampe,jars/orm,wars/api-user,wars/api-pagamento,wars/web-console,wars/api-backoffice,wars/api-pendenze,wars/api-pagopa,wars/web-connector,wars/api-ragioneria,wars/api-jppapdp \\
			-Djars/api-commons.sonar.sources=src/main/java -Djars/api-commons.sonar.tests=src/test/java -Djars/api-commons.sonar.java.binaries=target/classes \\
			-Djars/appio-beans.sonar.sources=src/main/java -Djars/appio-beans.sonar.tests=src/test/java -Djars/appio-beans.sonar.java.binaries=target/classes \\
			-Djars/orm-beans.sonar.sources=src/main/java -Djars/orm-beans.sonar.tests=src/test/java -Djars/orm-beans.sonar.java.binaries=target/classes \\
			-Djars/client-api-ente.sonar.sources=src/main/java -Djars/client-api-ente.sonar.tests=src/test/java -Djars/client-api-ente.sonar.java.binaries=target/classes \\
			-Djars/xml-adapters.sonar.sources=src/main/java -Djars/xml-adapters.sonar.tests=src/test/java -Djars/xml-adapters.sonar.java.binaries=target/classes \\
			-Djars/core-beans.sonar.sources=src/main/java -Djars/core-beans.sonar.tests=src/test/java -Djars/core-beans.sonar.java.binaries=target/classes \\
			-Djars/jppapdp-beans.sonar.sources=src/main/java -Djars/jppapdp-beans.sonar.tests=src/test/java -Djars/jppapdp-beans.sonar.java.binaries=target/classes \\
			-Djars/pagopa-beans.sonar.sources=src/main/java -Djars/pagopa-beans.sonar.tests=src/test/java -Djars/pagopa-beans.sonar.java.binaries=target/classes \\
			-Djars/core.sonar.sources=src/main/java -Djars/core.sonar.tests=src/test/java -Djars/core.sonar.java.binaries=target/classes \\
			-Djars/stampe.sonar.sources=src/main/java -Djars/stampe.sonar.tests=src/test/java -Djars/stampe.sonar.java.binaries=target/classes \\
			-Djars/orm.sonar.sources=src/main/java -Djars/orm.sonar.tests=src/test/java -Djars/orm.sonar.java.binaries=target/classes \\
			-Dwars/api-user.sonar.sources=src/main/java -Dwars/api-user.sonar.tests=src/test/java -Dwars/api-user.sonar.java.binaries=target/classes \\
			-Dwars/api-pagamento.sonar.sources=src/main/java -Dwars/api-pagamento.sonar.tests=src/test/java -Dwars/api-pagamento.sonar.java.binaries=target/classes \\
			-Dwars/web-console.sonar.sources=src/main/java -Dwars/web-console.sonar.tests=src/test/java -Dwars/web-console.sonar.java.binaries=target/classes \\
			-Dwars/api-backoffice.sonar.sources=src/main/java -Dwars/api-backoffice.sonar.tests=src/test/java -Dwars/api-backoffice.sonar.java.binaries=target/classes \\
			-Dwars/api-pendenze.sonar.sources=src/main/java -Dwars/api-pendenze.sonar.tests=src/test/java -Dwars/api-pendenze.sonar.java.binaries=target/classes \\
			-Dwars/api-pagopa.sonar.sources=src/main/java -Dwars/api-pagopa.sonar.tests=src/test/java -Dwars/api-pagopa.sonar.java.binaries=target/classes \\
			-Dwars/web-connector.sonar.sources=src/main/java -Dwars/web-connector.sonar.tests=src/test/java -Dwars/web-connector.sonar.java.binaries=target/classes \\
			-Dwars/api-ragioneria.sonar.sources=src/main/java -Dwars/api-ragioneria.sonar.tests=src/test/java -Dwars/api-ragioneria.sonar.java.binaries=target/classes \\
			-Dwars/api-jppapdp.sonar.sources=src/main/java -Dwars/api-jppapdp.sonar.tests=src/test/java -Dwars/api-jppapdp.sonar.java.binaries=target/classes \\
	    	-Dsonar.exclusions=**/*.gitignore,**/.git/**,**/*.md,**/test/**,**/build-wrapper-dump.json
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
