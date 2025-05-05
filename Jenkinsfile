pipeline {
  agent any
  options { 
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '2'))
  }
  environment {
    JACOCO_AGENT   = "/opt/jacoco-0.8.13/lib/jacocoagent.jar"
    // dove Tomcat deposita il .exec
    JACOCO_EXEC    = "/tmp/jacoco.exec"
    // percorso al Jacoco CLI per generare l’XML
    JACOCO_CLI     = "/opt/jacoco-0.8.13/lib/jacococli.jar"
    // dove mettere l’XML
    JACOCO_XML     = "target/jacoco.xml"
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
    stage('Generate JaCoCo XML Report') {
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
	
          JAVA_HOME=/usr/lib/jvm/java-21-openjdk java -jar ${JACOCO_CLI} report ${JACOCO_EXEC} \$classArgs \$srcArgs --xml ${JACOCO_XML}
        """
      }
    }
    stage('SonarQube Analysis') {
	  steps {
	    sh """
	    	JAVA_HOME=/usr/lib/jvm/java-21-openjdk /opt/apache-maven-3.6.3/bin/mvn sonar:sonar \\
	    	-Dsonar.projectKey=GovPay \\
	        -Dsonar.token=$GOVPAY_SONAR_TOKEN \\
	        -Dsonar.host.url=http://localhost:9000 \\
	        -Dsonar.coverage.jacoco.xmlReportPaths=${JACOCO_XML}
	       """
	  }
	  post {
        always {
		  archiveArtifacts 'target/jacoco.xml'
		}
	  }
	}
  }
}
