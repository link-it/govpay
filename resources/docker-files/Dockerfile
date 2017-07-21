# Use latest jboss/base-jdk:7 image as the base
FROM jboss/base-jdk:7

# Set the JBOSS_VERSION env variable
ENV JBOSS_VERSION 7.1.1.Final
ENV JBOSS_HOME /opt/jboss/jboss-as-$JBOSS_VERSION
ENV JBOSS_USER gpadmin
ENV JBOSS_PASSWORD secret

# Set GovPay version
ENV GP_VERSION 2.4.1

# Set JDBC Driver version
ENV JDBC_VERSION 42.0.0.jre7

USER root

#psql client installation
RUN yum -y update \
    && yum -y install postgresql

RUN cd $HOME \
    && curl -O http://download.jboss.org/jbossas/7.1/jboss-as-$JBOSS_VERSION/jboss-as-$JBOSS_VERSION.tar.gz \
    && tar xf jboss-as-$JBOSS_VERSION.tar.gz \
    && mv $HOME/jboss-as-$JBOSS_VERSION $JBOSS_HOME \
    && rm jboss-as-$JBOSS_VERSION.tar.gz \
    && chown -R jboss:0 $JBOSS_HOME \
    && chmod -R g+rw $JBOSS_HOME

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

# Govpay download
RUN cd /tmp/ \
  && curl -L -O https://github.com/link-it/GovPay/releases/download/$GP_VERSION_MAJOR/govpay-installer-$GP_VERSION.tgz \
  && tar xf govpay-installer-$GP_VERSION.tgz \
  && mv govpay-installer-$GP_VERSION govpay
  
RUN sed -i -e 's/@PRINCIPAL@/'"$JBOSS_USER"'/g' -e 's/@RAGIONE_SOCIALE@/jboss_user/g' /tmp/govpay/installer/sql/init.sql

# Expose the ports we're interested in
EXPOSE 8080

# Add JBoss user
RUN $JBOSS_HOME/bin/add-user.sh --silent=true -a $JBOSS_USER $JBOSS_PASSWORD

#install postgres driver
RUN curl -L -o /tmp/psql-jdbc.jar http://jdbc.postgresql.org/download/postgresql-$JDBC_VERSION.jar
RUN mv /tmp/psql-jdbc.jar $JBOSS_HOME/standalone/deployments/

ADD command.sh /tmp/
CMD ["/tmp/command.sh", "psql-jdbc.jar"]

