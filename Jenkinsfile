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

    // Bersagli delle analisi che girano ad ambiente vivo (ZAP, accessibilita')
    URL_CONSOLE_ATTUALE = "http://localhost:8080/govpay-console"
    URL_CONSOLE_NUOVA   = "http://localhost:10012"
    URL_PORTALE         = "http://localhost:10011"

    // Immagine ufficiale di ZAP: le vecchie owasp/zap2docker-* sono ritirate
    ZAP_IMAGE     = "ghcr.io/zaproxy/zaproxy:stable"
    // gov-a11y installato una volta sulla macchina: clonarlo a ogni build
    // significherebbe riscaricare Chromium ogni volta
    GOV_A11Y_HOME = "/opt/gov-a11y"
    NODE_BIN      = "/opt/nodejs/22.14.0/bin/node"
  }
  stages {
    stage('info') {
      steps {
        script {
          echo "================================"
          echo "Pipeline Build Information"
          echo "================================"
          echo "Git Branch: ${env.GIT_BRANCH_NAME}"
          echo "Project Version: ${env.PROJECT_VERSION}"
          echo "Build Number: ${env.BUILD_NUMBER}"
          echo "Job Name: ${env.JOB_NAME}"
          echo "Workspace: ${env.WORKSPACE}"
          echo "================================"
        }
      }
    }
    stage('cleanup') {
      steps {
        sh 'cd ${DOCKER_COMPOSE_DIR}/${PROJECT_VERSION} && sudo docker compose down -v && cd - || true'
        sh 'sh ./src/main/resources/scripts/jenkins.cleanup.sh'
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
        // Lo smontaggio non sta piu' qui: ZAP e l'analisi di accessibilita'
        // hanno bisogno delle applicazioni vive. Lo fa lo stage 'teardown',
        // con il post della pipeline a garantire che avvenga comunque.
        always {
            junit 'integration-test/target/surefire-reports/*.xml'
            sh 'tar -cvf ./integration-test/target/surefire-reports.tar ./integration-test/target/surefire-reports/ --transform s#./integration-test/target/##'
            sh 'gzip ./integration-test/target/surefire-reports.tar'
            archiveArtifacts 'integration-test/target/surefire-reports.tar.gz'
        }
      }
    }
    stage('analisi-ambiente-vivo') {
      // Le due analisi sono indipendenti fra loro e girano sullo stesso
      // ambiente gia' in piedi: in parallelo costano quanto la piu' lenta.
      // Entrambe marcano la build UNSTABLE invece di farla fallire: al primo
      // giro le segnalazioni sono molte e una pipeline rossa per cose note
      // smette di essere letta. Si stringe a FAILURE quando il rumore e' sotto
      // controllo.
      parallel {
        stage('zap') {
          steps {
            catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
              sh 'sh ./src/main/resources/scripts/jenkins.zap.sh'
            }
          }
          post {
            always {
              archiveArtifacts artifacts: 'target/zap/*', allowEmptyArchive: true
              publishHTML(target: [reportDir: 'target/zap', reportFiles: 'index.html',
                                   reportName: 'ZAP', keepAll: true, alwaysLinkToLastBuild: true,
                                   allowMissing: true])
            }
          }
        }
        stage('accessibilita') {
          environment {
            // Credenziale Jenkins di tipo "Username with password": espone
            // GOVPAY_CONSOLE_USR e GOVPAY_CONSOLE_PSW. La mappatura sui nomi
            // che gov-a11y si aspetta (A11Y_<TARGET>_USER/_PASS) la fa lo
            // script: dentro questo blocco le variabili appena definite non
            // sono ancora espandibili in modo affidabile.
            GOVPAY_CONSOLE = credentials('govpay-console')
          }
          steps {
            catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
              sh 'sh ./src/main/resources/scripts/jenkins.a11y.sh'
            }
          }
          post {
            always {
              junit allowEmptyResults: true, testResults: 'target/a11y/*/a11y-junit.xml'
              recordIssues sourceCodeRetention: 'LAST_BUILD', tools: [sarif(id: 'a11y', name: 'Accessibilita', pattern: 'target/a11y/*/a11y.sarif')]
              publishHTML(target: [reportDir: 'target/a11y', reportFiles: 'console-attuale/report.html, console-nuova/report.html, portale/report.html',
                                   reportName: 'Accessibilita', keepAll: true, alwaysLinkToLastBuild: true,
                                   allowMissing: true])
              archiveArtifacts artifacts: 'target/a11y/**', allowEmptyArchive: true
            }
          }
        }
      }
    }
    stage('teardown') {
      steps {
        sh 'sh ./src/main/resources/scripts/jenkins.teardown.sh'
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
  post {
    // Rete di sicurezza: finche' lo smontaggio stava nel post 'always' dello
    // stage test, un fallimento dei test lasciava comunque l'ambiente pulito.
    // Spostandolo in uno stage quella garanzia va ricreata qui, altrimenti un
    // errore nelle analisi lascerebbe acceso tutto fino alla build successiva.
    // Lo script e' idempotente: sul percorso felice gira due volte.
    always {
      sh 'sh ./src/main/resources/scripts/jenkins.teardown.sh'
    }
  }
}
