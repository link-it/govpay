#!/bin/sh

# The default case runs the installer in an X session if it can
# falling back to text if the Graphics environment is not available
# This can be changed by uncommenting the if statement and the default
# can be changed by replacing "text" with "swing" below
# or passing the required option on the command line

GUI=$1
if [ "$GUI" = "" ] ; then
	GUI=default;
fi

# controllo JAVA_HOME
if [ "$JAVA_HOME" = "" ] ; then
	echo "L'installazione richiede l'installazione di java http://java.sun.com"
	echo "Se la jvm e' gia installata provare a settare la variabile JAVA_HOME"
	exit 1;
fi

if [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    _java="$JAVA_HOME/bin/java"
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    if [[ ! $version = 1.8* ]] && [[ $version = 1.* ]]; then
        echo "E' necessaro java 1.8 o successivo"
        exit 1;
    fi
fi



ROOT_OPENSPCOOP=.
LIBRARIES=${ROOT_OPENSPCOOP}/installer/lib
ANTINSTALLER_LIBRARIES=${LIBRARIES}/antinstaller

# Installer from command line classpath
CLASSPATH=${LIBRARIES}/shared/xercesImpl-2.9.1.jar
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/xml-apis_antinstaller0.8b.jar
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/ant-installer-0.8b.jar
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/ai-icons-eclipse_antinstaller0.8b.jar

# JGoodies Look And Feel
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/jgoodies-edited-1_2_2.jar

# minimal ANT classpath requirements
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/ant-1.8.0.jar
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/ant-launcher-1.8.0.jar
CLASSPATH=${CLASSPATH}:${ROOT_OPENSPCOOP}/installer/setup/deploy/resources

# minimal regular expression env
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/ant-apache-regexp-1.8.0.jar
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/jakarta-regexp-1.5.jar

# unix tasks
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/ant-nodeps-1.8.0.jar
CLASSPATH=${CLASSPATH}:${ANTINSTALLER_LIBRARIES}/ant-trax.jar

COMMAND=$JAVA_HOME/bin/java

$COMMAND -classpath $CLASSPATH  org.tp23.antinstaller.runtime.ExecInstall $GUI ${ROOT_OPENSPCOOP}/installer/setup
