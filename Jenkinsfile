pipeline {
  agent any
  options {
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '2'))
  }
  environment {
    // Rileva il branch Git corrente
    GIT_BRANCH_NAME = sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
    // Rileva la versione del progetto dal pom.xml
    PROJECT_VERSION = sh(script: 'JAVA_HOME=/usr/lib/jvm/java-21-openjdk /opt/apache-maven-3.6.3/bin/mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()

    JACOCO_EXEC    = "/tmp/jacoco.exec"
    JACOCO_XML     = "target/jacoco.xml"
    JACOCO_HTML    = "target/jacoco-html"
    JACOCO_CSV     = "target/jacoco.csv"

    // sudo docker compose path
    DOCKER_COMPOSE_DIR = "/etc/govpay/docker"

    // Cron expression per il check di reset della cache anagrafica (default: ogni 10 secondi per la testsuite)
    CACHE_CHECK_CRON = "0/10 * * * * ?"

    // Immagini docker: repository di destinazione e credenziali del registry.
    // Le immagini di sviluppo e quelle di release vanno in due repository
    // distinti, cosi' i cataloghi restano separati e i tag non collidono.
    // DOCKERHUB_CREDENTIALS_ID deve corrispondere all'id delle credenziali
    // "Username with password" configurate in Jenkins per Docker Hub.
    DOCKER_IMAGE_BASE        = "linkitaly/govpay"
    DOCKER_IMAGE_BASE_DEV    = "linkitaly/govpay-dev"
    DOCKERHUB_CREDENTIALS_ID = "dockerhub"
    // Sull'agent il demone docker richiede sudo, come negli stage install e test
    DOCKER_BIN = "sudo docker"
  }
  stages {
    stage('info') {
      steps {
        script {
          // Branch di riferimento, solo informativo: git rev-parse restituisce
          // "HEAD" quando il checkout e' detached, quindi si preferiscono
          // BRANCH_NAME (multibranch) e GIT_BRANCH (git plugin), che sopravvivono.
          if (env.BRANCH_NAME) {
            env.GOVPAY_BRANCH = env.BRANCH_NAME
          } else if (env.GIT_BRANCH) {
            env.GOVPAY_BRANCH = env.GIT_BRANCH.startsWith('origin/') ? env.GIT_BRANCH.substring('origin/'.length()) : env.GIT_BRANCH
          } else {
            env.GOVPAY_BRANCH = env.GIT_BRANCH_NAME
          }

          // Insieme di immagini da pubblicare. E' una release solo se il commit in
          // costruzione porta un tag git uguale alla versione del pom: ogni altro
          // build, compreso un push su master, e' uno sviluppo. Non si guarda il
          // nome del branch perche' con il checkout detached non e' affidabile, e
          // non basta la versione del pom perche' master ne porta una fissa anche
          // fra due release, quindi ogni push la ripubblicherebbe.
          // NOTA: richiede che il job scarichi i tag dal remoto.
          env.GOVPAY_RELEASE_TAG = sh(script: "git tag --points-at HEAD | grep -Fx '${env.PROJECT_VERSION}' || true", returnStdout: true).trim()
          env.GOVPAY_DOCKER_SET = env.GOVPAY_RELEASE_TAG ? 'release' : 'dev'
          env.GOVPAY_INSTALLER = "src/main/resources/setup/target/govpay-installer-${env.PROJECT_VERSION}.tgz"

          echo "================================"
          echo "Pipeline Build Information"
          echo "================================"
          echo "Git Branch: ${env.GIT_BRANCH_NAME}"
          echo "Branch: ${env.GOVPAY_BRANCH}"
          echo "Project Version: ${env.PROJECT_VERSION}"
          echo "Tag di release su HEAD: ${env.GOVPAY_RELEASE_TAG ?: 'nessuno'}"
          echo "Immagini docker: ${env.GOVPAY_DOCKER_SET}"
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
        sh 'cd ${DOCKER_COMPOSE_DIR}/${PROJECT_VERSION} && sudo docker compose down -v && cd - || true'
        sh '/opt/apache-maven-3.6.3/bin/mvn clean'
      }
    }
    stage('build') {
      steps {
	sh 'JAVA_HOME=/usr/lib/jvm/java-21-openjdk /opt/apache-maven-3.6.3/bin/mvn install spotbugs:spotbugs -Denv=installer_template -D"it.govpay.batch.cacheCheck.cron=${CACHE_CHECK_CRON}" -DnvdApiKey=$NVD_API_KEY -DossIndexUsername=$OSS_INDEX_USER -DossIndexPassword=$OSS_INDEX_PASSWORD'
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
        sh 'sudo systemctl start wildfly-28.0.1.Final@ndpsym tomcat_govpay_jdk25'
        sh 'sudo docker start mailhog'
        sh 'cd ${DOCKER_COMPOSE_DIR}/${PROJECT_VERSION} && sudo docker compose up -d && cd -'
	    sh 'sh ./src/main/resources/scripts/jenkins.checkgp.sh'
      }
    }
    stage('test') {
      steps {
        sh 'cd ./integration-test; JAVA_HOME=/etc/alternatives/jre_1.8.0 /opt/apache-maven-3.6.3/bin/mvn clean test'
      }
      post {
        always {
			sh 'cd ${DOCKER_COMPOSE_DIR}/${PROJECT_VERSION} && sudo docker compose down -v && cd -'
			sh 'sudo systemctl stop wildfly@govpay wildfly-26.1.3.Final@standalone wildfly-26.1.3.Final@ndpsym wildfly-28.0.1.Final@ndpsym tomcat_govpay tomcat_govpay_jdk25'
			sh 'sudo docker stop mailhog'
            junit 'integration-test/target/surefire-reports/*.xml'
            sh 'tar -cvf ./integration-test/target/surefire-reports.tar ./integration-test/target/surefire-reports/ --transform s#./integration-test/target/##'
            sh 'gzip ./integration-test/target/surefire-reports.tar'
            archiveArtifacts 'integration-test/target/surefire-reports.tar.gz'
        }
      }
    }
    // Immagini docker. Gli stage sono posizionati dopo 'test' e prima dell'analisi
    // Sonar: una testsuite rossa deve impedire la pubblicazione, un quality gate
    // rosso no, altrimenti ogni oscillazione del gate blocca le immagini di sviluppo.
    // Le immagini sono costruite dall'installer prodotto dallo stage 'build'.
    stage('docker-dev') {
      when {
        expression { env.GOVPAY_DOCKER_SET == 'dev' }
      }
      steps {
        withCredentials([usernamePassword(credentialsId: env.DOCKERHUB_CREDENTIALS_ID, usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_TOKEN')]) {
          sh '''
            set -e
            echo "$DOCKERHUB_TOKEN" | $DOCKER_BIN login -u "$DOCKERHUB_USER" --password-stdin
            trap "$DOCKER_BIN logout" EXIT
            ./docker/build-images.sh \
              --version "${PROJECT_VERSION}" \
              --installer "${GOVPAY_INSTALLER}" \
              --set dev \
              --image-base "${DOCKER_IMAGE_BASE_DEV}" \
              --push
          '''
        }
      }
    }
    stage('docker') {
      when {
        expression { env.GOVPAY_DOCKER_SET == 'release' }
      }
      steps {
        withCredentials([usernamePassword(credentialsId: env.DOCKERHUB_CREDENTIALS_ID, usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_TOKEN')]) {
          sh '''
            set -e
            echo "$DOCKERHUB_TOKEN" | $DOCKER_BIN login -u "$DOCKERHUB_USER" --password-stdin
            trap "$DOCKER_BIN logout" EXIT
            ./docker/build-images.sh \
              --version "${PROJECT_VERSION}" \
              --installer "${GOVPAY_INSTALLER}" \
              --set release \
              --image-base "${DOCKER_IMAGE_BASE}" \
              --latest \
              --push
          '''
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
