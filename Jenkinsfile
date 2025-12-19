pipeline {
  agent any
  options { 
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '2'))
  }
  environment {
    // Rileva il branch Git corrente
    GIT_BRANCH_NAME = sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()

    JACOCO_EXEC    = "/tmp/jacoco.exec"
    JACOCO_XML     = "target/jacoco.xml"
    JACOCO_HTML    = "target/jacoco-html"
    JACOCO_CSV     = "target/jacoco.csv"

    // sudo docker compose path
    DOCKER_COMPOSE_DIR = "/etc/govpay/docker"
  }
  stages {
    stage('info') {
      steps {
        script {
          echo "================================"
          echo "Pipeline Build Information"
          echo "================================"
          echo "Git Branch: ${env.GIT_BRANCH_NAME}"
          echo "Build Number: ${env.BUILD_NUMBER}"
          echo "Job Name: ${env.JOB_NAME}"
          echo "Workspace: ${env.WORKSPACE}"
          echo "================================"
        }
      }
    }
    stage('cleanup') {
      steps {
        sh 'sh ./src/main/resources/scripts/jenkins.cleanup.sh'
        sh 'cd ${DOCKER_COMPOSE_DIR} && sudo docker compose down -v && cd - || true'
        sh '/opt/apache-maven-3.6.3/bin/mvn clean'
      }
    }
    stage('build') {
      steps {
	sh 'JAVA_HOME=/usr/lib/jvm/java-21-openjdk /opt/apache-maven-3.6.3/bin/mvn install spotbugs:spotbugs -Denv=installer_template -DnvdApiKey=$NVD_API_KEY -DossIndexUsername=$OSS_INDEX_USER -DossIndexPassword=$OSS_INDEX_PASSWORD' 
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
        sh 'sudo systemctl start wildfly-28.0.1.Final@ndpsym tomcat_govpay'
        sh 'sudo docker start mailhog'
        sh 'cd ${DOCKER_COMPOSE_DIR} && sudo docker compose up -d && cd -'
	    sh 'sh ./src/main/resources/scripts/jenkins.checkgp.sh'
      }
    }
    stage('test') {
      steps {
        sh 'cd ./integration-test; JAVA_HOME=/etc/alternatives/jre_1.8.0 /opt/apache-maven-3.6.3/bin/mvn clean test -Dkarate.options="classpath:test/api/backoffice/v1/flussiRendicontazione/get/flussiRendicontazione-find-auth-uo.feature" -Dtest=test.workflow.WorkflowTest' 
      }
      post {
        always {
			sh 'cd ${DOCKER_COMPOSE_DIR} && sudo docker compose down -v && cd -'
			sh 'sudo systemctl stop wildfly@govpay wildfly-26.1.3.Final@standalone wildfly-26.1.3.Final@ndpsym wildfly-28.0.1.Final@ndpsym tomcat_govpay'
			sh 'sudo docker stop mailhog'
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
	    	XML_REPORT=\$(pwd)/${JACOCO_XML}

	    	JAVA_HOME=/usr/lib/jvm/java-21-openjdk /opt/apache-maven-3.6.3/bin/mvn sonar:sonar \\
	    	-Dsonar.projectKey=link-it_govpay -Dsonar.organization=link-it -Dsonar.token=$SONAR_CLOUD_TOKEN \\
	    	-Dsonar.java.source=21 -Dsonar.host.url=https://sonarcloud.io -Dsonar.coverage.jacoco.xmlReportPaths=\${XML_REPORT} \\
	    	-Dsonar.nodejs.executable=/opt/nodejs/22.14.0/bin/node \\
	    	-Dsonar.qualitygate.wait=true
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
