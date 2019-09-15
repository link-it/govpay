export JAVA_HOME=/opt/jdk1.8.0_191/
pushd src/main/resources/setup/
rm -rf target
sh prepareSetup.sh
