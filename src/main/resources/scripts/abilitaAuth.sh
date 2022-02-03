#!/bin/bash

pushd () {
    command pushd "$@" > /dev/null
}

popd () {
    command popd "$@" > /dev/null
}

# DEFAULTS

BACKOFFICE=basic,ssl
PAGAMENTI=basic,ssl
PENDENZE=basic,ssl
RAGIONERIA=basic,ssl
USER=
PAGOPA=ssl
LEGACY=ssl
APIDEFAULT=none
GOVPAY_SRC_DIR="ear/target/"
GOVPAY_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)
POSITIONAL=()
while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    -bo|--backoffice)
    BACKOFFICE="$2"
    shift # past argument
    shift # past value
    ;;
    -pag|--pagamenti)
    PAGAMENTI="$2"
    shift # past argument
    shift # past value
    ;;
    -pen|--pendenze)
    PENDENZE="$2"
    shift # past argument
    shift # past value
    ;;
    -rag|--ragioneria)
    RAGIONERIA="$2"
    shift # past argument
    shift # past value
    ;;
    -usr|--user)
    USER="$2"
    shift # past argument
    shift # past value
    ;;
    -pp|--pagopa)
    PAGOPA="$2"
    shift # past argument
    shift # past value
    ;;
    -leg|--legacy)
    LEGACY="$2"
    shift # past argument
    shift # past value
    ;;
    -src|--sourcedir)
    GOVPAY_SRC_DIR="$2"
    shift # past argument
    shift # past value
    ;;
    -v|--version)
    GOVPAY_VERSION="$2"
    shift # past argument
    shift # past value
    ;;
    -d|--default)
    APIDEFAULT="$2"
    shift # past argument
    shift # past value
    ;;
    *)    # unknown option
    echo "Opzione non riconosciuta $1"
    echo "usage:"
    echo "   -bo <args> : lista di autenticazioni da abilitare sulle api di backoffice (spid,header,wildfly,basic,ssl,hdrcert,public,session). Default: basic,ssl" 
    echo "   -pag <args> : lista di autenticazioni da abilitare sulle api di pagamento (spid,header,wildfly,basic,ssl,hdrcert,public,session). Default: basic,ssl"
    echo "   -pen <args> : lista di autenticazioni da abilitare sulle api di pendenza (basic,ssl). Default: basic,basic-gp,ssl,hdrcert"
    echo "   -rag <args> : lista di autenticazioni da abilitare sulle api di ragioneria (basic,ssl). Default: basic,basic-gp,ssl,hdrcert"
    echo "   -usr <args> : lista di autenticazioni da abilitare sulle api di user (spid). Default: spid"
    echo "   -pp <args> : autenticazione da abilitare sulle api di pagopa (basic,basic-gp,ssl). Default: ssl,hdrcert"
    echo "   -leg <args> : autenticazione da abilitare sulle api legacy (basic,basic-gp,ssl,hdrcert,header). Default: ssl"
    echo "   -d <args> : autenticazione da abilitare sui contesti senza autenticazione per retro-compatibilita (basic,ssl,hdrcert). Default: none"
    exit 2;
    ;;
esac
done
set -- "${POSITIONAL[@]}" # restore positional parameters

BACKOFFICE_BASIC_WF=false
BACKOFFICE_BASIC_GP=false
[[ $BACKOFFICE == *"wildfly"* ]] && BACKOFFICE_BASIC_WF=true 
[[ $BACKOFFICE == *"basic"* ]] && { BACKOFFICE_BASIC_WF=false; BACKOFFICE_BASIC_GP=true; }
[[ $BACKOFFICE == *"ssl"* ]] && BACKOFFICE_SSL=true || BACKOFFICE_SSL=false
[[ $BACKOFFICE == *"hdrcert"* ]] && BACKOFFICE_SSL_HEADER=true || BACKOFFICE_SSL_HEADER=false
[[ $BACKOFFICE == *"header"* ]] && BACKOFFICE_HEADER=true || BACKOFFICE_HEADER=false
[[ $BACKOFFICE == *"spid"* ]] && BACKOFFICE_SPID=true || BACKOFFICE_SPID=false
[[ $PAGAMENTI == *"public"* ]] && BACKOFFICE_PUBLIC=true || BACKOFFICE_PUBLIC=false
[[ $BACKOFFICE == *"session"* ]] && BACKOFFICE_SESSION=true || BACKOFFICE_SESSION=false

PAGAMENTI_BASIC_WF=false
PAGAMENTI_BASIC_GP=false
[[ $PAGAMENTI == *"wildfly"* ]] && { PAGAMENTI_BASIC_WF=true; PAGAMENTI_BASIC_GP=false; }
[[ $PAGAMENTI == *"basic"* ]] && { PAGAMENTI_BASIC_GP=true; PAGAMENTI_BASIC_WF=false; }
[[ $PAGAMENTI == *"ssl"* ]] && PAGAMENTI_SSL=true || PAGAMENTI_SSL=false
[[ $PAGAMENTI == *"hdrcert"* ]] && PAGAMENTI_SSL_HEADER=true || PAGAMENTI_SSL_HEADER=false
[[ $PAGAMENTI == *"header"* ]] && PAGAMENTI_HEADER=true || PAGAMENTI_HEADER=false
[[ $PAGAMENTI == *"spid"* ]] && PAGAMENTI_SPID=true || PAGAMENTI_SPID=false
[[ $PAGAMENTI == *"public"* ]] && PAGAMENTI_PUBLIC=true || PAGAMENTI_PUBLIC=false
[[ $PAGAMENTI == *"session"* ]] && PAGAMENTI_SESSION=true || PAGAMENTI_SESSION=false

PENDENZE_BASIC_WF=false
PENDENZE_BASIC_GP=false
[[ $PENDENZE == *"wildfly"* ]] && { PENDENZE_BASIC_WF=true; PENDENZE_BASIC_GP=false; }
[[ $PENDENZE == *"basic"* ]] && { PENDENZE_BASIC_GP=true; PENDENZE_BASIC_WF=false; } 
[[ $PENDENZE == *"ssl"* ]] && PENDENZE_SSL=true || PENDENZE_SSL=false
[[ $PENDENZE == *"hdrcert"* ]] && PENDENZE_SSL_HEADER=true || PENDENZE_SSL_HEADER=false
[[ $PENDENZE == *"header"* ]] && PENDENZE_HEADER=true || PENDENZE_HEADER=false

RAGIONERIA_BASIC_WF=false
RAGIONERIA_BASIC_GP=false
[[ $RAGIONERIA == *"wildfly"* ]] && { RAGIONERIA_BASIC_WF=true; RAGIONERIA_BASIC_GP=false; } 
[[ $RAGIONERIA == *"basic"* ]] && { RAGIONERIA_BASIC_GP=true; RAGIONERIA_BASIC_WF=false; }
[[ $RAGIONERIA == *"ssl"* ]] && RAGIONERIA_SSL=true || RAGIONERIA_SSL=false
[[ $RAGIONERIA == *"hdrcert"* ]] && RAGIONERIA_SSL_HEADER=true || RAGIONERIA_SSL_HEADER=false
[[ $RAGIONERIA == *"header"* ]] && RAGIONERIA_HEADER=true || RAGIONERIA_HEADER=false

[[ $USER == *"spid"* ]] && UTENTE_SPID=true || UTENTE_SPID=false

[[ $PAGOPA == *"basic"* ]] && PAGOPA_BASIC=true || PAGOPA_BASIC=false
[[ $PAGOPA == *"hdrcert"* ]] && PAGOPA_SSL_HEADER=true || PAGOPA_SSL_HEADER=false

LEGACY_BASIC_WF=false
LEGACY_BASIC_GP=false

[[ $LEGACY == *"wildfly"* ]] && { LEGACY_BASIC_WF=true; LEGACY_BASIC_GP=false; }
[[ $LEGACY == *"basic"* ]] && { LEGACY_BASIC_GP=true; LEGACY_BASIC_WF=false; }
[[ $LEGACY == *"hdrcert"* ]] && LEGACY_SSL_HEADER=true || LEGACY_SSL_HEADER=false
[[ $LEGACY == *"header"* ]] && LEGACY_HEADER=true || LEGACY_HEADER=false

DEFAULT_BASIC=false
DEFAULT_SSL=false
[[ $APIDEFAULT == *"basic"* ]] && DEFAULT_BASIC=true || DEFAULT_BASIC=false
[[ $APIDEFAULT == *"ssl"* ]] && DEFAULT_SSL=true || DEFAULT_SSL=false




GOVPAY_WORK_DIR="govpay_ear_tmp"
GOVPAY_EAR_NAME="govpay.ear"
GOVPAY_TMP_DIR="GOVPAY_EAR"
APP_CONTEXT_BASE_DIR="WEB-INF"
CONTEXT_SECURITY_XML_SUFFIX="applicationContext-security.xml"

rm -rf $GOVPAY_WORK_DIR
mkdir $GOVPAY_WORK_DIR

cp $GOVPAY_SRC_DIR$GOVPAY_EAR_NAME $GOVPAY_WORK_DIR 
pushd $GOVPAY_WORK_DIR

unzip -q -d $GOVPAY_TMP_DIR $GOVPAY_EAR_NAME
pushd $GOVPAY_TMP_DIR

# API-Backoffice
API_PREFIX="api-backoffice-"
unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX


if ! $BACKOFFICE_BASIC_GP
then
  echo "API-Backoffice disabilitazione autenticazione basic govpay...";
  sed -i -e "s#BASIC_GOVPAY_PROVIDER_START -->#BASIC_GOVPAY_PROVIDER_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- BASIC_GOVPAY_PROVIDER_END#BASIC_GOVPAY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice disabilitazione autenticazione basic govpay completata.";
fi

if $BACKOFFICE_BASIC_WF
then
  echo "API-Backoffice abilitazione autenticazione basic wildfly...";
  sed -i -e "s# BASIC_WILDFLY_PROVIDER_START# BASIC_WILDFLY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s# BASIC_WILDFLY_PROVIDER_END# <!-- BASIC_WILDFLY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice abilitazione basic wildfly completata.";
fi

if ! $BACKOFFICE_SSL
then
  echo "API-Backoffice disabilitazione autenticazione ssl...";
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice disabilitazione ssl completata.";
fi

if $BACKOFFICE_SSL_HEADER
then
  echo "API-Backoffice abilitazione autenticazione hdrcert...";
  sed -i -e "s#SSL_HDR_START#SSL_HDR_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SSL_HDR_END#<!-- SSL_HDR_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice abilitazione hdrcert completata.";
fi

if $BACKOFFICE_SPID
then 
  echo "API-Backoffice abilitazione autenticazione spid...";
  sed -i -e "s#SPID_START#SPID_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SPID_END#<!-- SPID_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice abilitazione spid completata.";
fi

if $BACKOFFICE_HEADER
then
  echo "API-Backoffice abilitazione HTTP Header-auth ...";
  sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice abilitazione HTTP Header-auth completata.";
fi
if $BACKOFFICE_SESSION
then
  echo "API-Backoffice abilitazione Session auth ...";
  sed -i -e "s#SESSION_START#HEADER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SESSION_END#<!-- SESSION_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice abilitazione Session auth completata.";
fi
if $BACKOFFICE_PUBLIC
then
  echo "API-Backoffice abilitazione accesso in forma anonima...";
  sed -i -e "s#PUBLIC_START#PUBLIC_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#PUBLIC_END#<!-- PUBLIC_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice abilitazione accesso in forma anonima completata.";
fi
if $DEFAULT_BASIC
then
  echo "API-Backoffice abilitazione default HTTP BASIC ...";
  sed -i -e "s#DEFAULT_BASIC_WILDFLY_PROVIDER_START#DEFAULT_BASIC_WILDFLY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_BASIC_WILDFLY_PROVIDER_END#<!-- DEFAULT_BASIC_WILDFLY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice abilitazione default HTTP BASIC completata.";
fi
if $DEFAULT_SSL
then
  echo "API-Backoffice abilitazione default SSL ...";
  sed -i -e "s#DEFAULT_SSL_START#DEFAULT_SSL_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_SSL_END#<!-- DEFAULT_SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Backoffice abilitazione default SSL completata.";
fi

zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
rm -rf $APP_CONTEXT_BASE_DIR



# API-Pendenze

API_PREFIX="api-pendenze-"
unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

if ! $PENDENZE_BASIC_GP
then
  echo "API-Pendenze disabilitazione autenticazione basic govpay...";
  sed -i -e "s#BASIC_GOVPAY_PROVIDER_START -->#BASIC_GOVPAY_PROVIDER_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- BASIC_GOVPAY_PROVIDER_END#BASIC_GOVPAY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pendenze disabilitazione autenticazione basic govpay completata.";
fi

if $PENDENZE_BASIC_WF
then
  echo "API-Pendenze abilitazione autenticazione basic wildfly...";
  sed -i -e "s# BASIC_WILDFLY_PROVIDER_START# BASIC_WILDFLY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s# BASIC_WILDFLY_PROVIDER_END# <!-- BASIC_WILDFLY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pendenze abilitazione basic wildfly completata.";
fi

if ! $PENDENZE_SSL
then
  echo "API-Pendenze disabilitazione autenticazione ssl...";
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pendenze disabilitazione ssl completata.";
fi

if $PENDENZE_SSL_HEADER
then
  echo "API-Pendenze abilitazione autenticazione hdrcert...";
  sed -i -e "s#SSL_HDR_START#SSL_HDR_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SSL_HDR_END#<!-- SSL_HDR_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pendenze abilitazione hdrcert completata.";
fi

if $PENDENZE_HEADER
then
  echo "API-Pendenze abilitazione HTTP Header-auth ...";
  sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pendenze abilitazione HTTP Header-auth  completata.";
fi
if $DEFAULT_BASIC
then
  echo "API-Pendenze abilitazione default HTTP BASIC ...";
  sed -i -e "s#DEFAULT_BASIC_WILDFLY_PROVIDER_START#DEFAULT_BASIC_WILDFLY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_BASIC_WILDFLY_PROVIDER_END#<!-- DEFAULT_BASIC_WILDFLY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pendenze abilitazione default HTTP BASIC completata.";
fi
if $DEFAULT_SSL
then
  echo "API-Pendenze abilitazione default SSL ...";
  sed -i -e "s#DEFAULT_SSL_START#DEFAULT_SSL_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_SSL_END#<!-- DEFAULT_SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pendenze abilitazione default SSL completata.";
fi

zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
rm -rf $APP_CONTEXT_BASE_DIR



# API-Ragioneria

API_PREFIX="api-ragioneria-"
unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

if ! $RAGIONERIA_BASIC_GP
then
  echo "API-Ragioneria disabilitazione autenticazione basic govpay...";
  sed -i -e "s#BASIC_GOVPAY_PROVIDER_START -->#BASIC_GOVPAY_PROVIDER_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- BASIC_GOVPAY_PROVIDER_END#BASIC_GOVPAY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Ragioneria disabilitazione autenticazione basic govpay completata.";
fi

if $RAGIONERIA_BASIC_WF
then
  echo "API-Ragioneria abilitazione autenticazione basic wildfly...";
  sed -i -e "s# BASIC_WILDFLY_PROVIDER_START# BASIC_WILDFLY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s# BASIC_WILDFLY_PROVIDER_END# <!-- BASIC_WILDFLY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Ragioneria abilitazione basic wildfly completata.";
fi

if ! $RAGIONERIA_SSL
then
  echo "API-Ragioneria disabilitazione autenticazione ssl...";
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Ragioneria disabilitazione ssl completata.";
fi

if $RAGIONERIA_SSL_HEADER
then
  echo "API-Ragioneria abilitazione autenticazione hdrcert...";
  sed -i -e "s#SSL_HDR_START#SSL_HDR_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SSL_HDR_END#<!-- SSL_HDR_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Ragioneria abilitazione hdrcert completata.";
fi

if $RAGIONERIA_HEADER
then
  echo "API-Ragioneria abilitazione HTTP Header-auth ...";
  sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Ragioneria abilitazione HTTP Header-auth  completata.";
fi
if $DEFAULT_BASIC
then
  echo "API-Ragioneria abilitazione default HTTP BASIC ...";
  sed -i -e "s#DEFAULT_BASIC_WILDFLY_PROVIDER_START#DEFAULT_BASIC_WILDFLY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_BASIC_WILDFLY_PROVIDER_END#<!-- DEFAULT_BASIC_WILDFLY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Ragioneria abilitazione default HTTP BASIC completata.";
fi
if $DEFAULT_SSL
then
  echo "API-Ragioneria abilitazione default SSL ...";
  sed -i -e "s#DEFAULT_SSL_START#DEFAULT_SSL_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_SSL_END#<!-- DEFAULT_SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Ragioneria abilitazione default SSL completata.";
fi

zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
rm -rf $APP_CONTEXT_BASE_DIR



# API-Pagamento
API_PREFIX="api-pagamento-"
unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

if ! $PAGAMENTI_BASIC_GP
then
  echo "API-Pagamenti disabilitazione autenticazione basic govpay...";
  sed -i -e "s#BASIC_GOVPAY_PROVIDER_START -->#BASIC_GOVPAY_PROVIDER_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- BASIC_GOVPAY_PROVIDER_END#BASIC_GOVPAY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamenti disabilitazione autenticazione basic govpay completata.";
fi

if $PAGAMENTI_BASIC_WF
then
  echo "API-Pagamenti abilitazione autenticazione basic wildfly...";
  sed -i -e "s# BASIC_WILDFLY_PROVIDER_START# BASIC_WILDFLY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s# BASIC_WILDFLY_PROVIDER_END# <!-- BASIC_WILDFLY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamenti abilitazione basic wildfly completata.";
fi

if ! $PAGAMENTI_SSL
then
  echo "API-Pagamenti disabilitazione autenticazione ssl...";
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamenti disabilitazione ssl completata.";
fi

if $PAGAMENTI_SSL_HEADER
then
  echo "API-Pagamenti abilitazione autenticazione hdrcert...";
  sed -i -e "s#SSL_HDR_START#SSL_HDR_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SSL_HDR_END#<!-- SSL_HDR_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamenti abilitazione hdrcert completata.";
fi

if $PAGAMENTI_SPID
then
  echo "API-Pagamento abilitazione autenticazione SPID...";
  sed -i -e "s#SPID_START#SPID_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SPID_END#<!-- SPID_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamento abilitazione autenticazione SPID completata.";
fi
if $PAGAMENTI_SESSION
then
  echo "API-Pagamento abilitazione autenticazione Session...";
  sed -i -e "s#SESSION_START#SESSION_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SESSION_END#<!-- SESSION_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamento abilitazione autenticazione Session completata.";
fi
if $PAGAMENTI_HEADER
then
  echo "API-Pagamento abilitazione HTTP Header-auth ...";
  sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamento abilitazione HTTP Header-auth  completata.";
fi
if $PAGAMENTI_PUBLIC
then
  echo "API-Pagamento abilitazione pagamenti in forma anonima...";
  sed -i -e "s#PUBLIC_START#PUBLIC_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#PUBLIC_END#<!-- PUBLIC_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamento abilitazione pagamenti in forma anonima completata.";
fi
if $DEFAULT_BASIC
then
  echo "API-Pagamento abilitazione default HTTP BASIC ...";
  sed -i -e "s#DEFAULT_BASIC_WILDFLY_PROVIDER_START#DEFAULT_BASIC_WILDFLY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_BASIC_WILDFLY_PROVIDER_END#<!-- DEFAULT_BASIC_WILDFLY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamento abilitazione default HTTP BASIC completata.";
fi
if $DEFAULT_SSL
then
  echo "API-Pagamento abilitazione default SSL ...";
  sed -i -e "s#DEFAULT_SSL_START#DEFAULT_SSL_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_SSL_END#<!-- DEFAULT_SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Pagamento abilitazione default SSL completata.";
fi

zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
rm -rf $APP_CONTEXT_BASE_DIR

# API-Utente

API_PREFIX="api-user-"
unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

if $UTENTE_SPID
then
  echo "API-Utente abilitazione autenticazione SPID...";
  sed -i -e "s#SPID_START#SPID_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SPID_END#<!-- SPID_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  echo "API-Utente abilitazione autenticazione SPID completata.";
fi
zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
rm -rf $APP_CONTEXT_BASE_DIR


# API-PagoPA

if $PAGOPA_BASIC
then
  echo "API-PagoPA abilitazione HTTP Basic-auth...";

  API_PREFIX="api-pagopa-"
  unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # accendo la modalita' basic
  sed -i -e "s#BASIC_START#BASIC_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#BASIC_END#<!-- BASIC_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # spengo modalita ssl
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # ripristino file
  zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # eliminazione dei file temporanei
  rm -rf $APP_CONTEXT_BASE_DIR

  echo "API-PagoPA abilitazione HTTP Basic-auth completata.";
fi

if $PAGOPA_SSL_HEADER
then
  echo "API-PagoPA abilitazione hdrcert...";

  API_PREFIX="api-pagopa-"
  unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # spengo modalita ssl
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # accendo modalita hdrcert
  sed -i -e "s#SSL_HDR_START#SSL_HDR_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SSL_HDR_END#<!-- SSL_HDR_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # ripristino file
  zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # eliminazione dei file temporanei
  rm -rf $APP_CONTEXT_BASE_DIR

  echo "API-PagoPA abilitazione hdrcert completata.";
fi

# API-Legacy

if $LEGACY_BASIC_WF
then
  echo "API-Legacy abilitazione HTTP Basic-auth wildfly...";

  API_PREFIX="api-legacy-"
  unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # accendo la modalita' basic
  sed -i -e "s#BASIC_WILDFLY_PROVIDER_START#BASIC_WILDFLY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#BASIC_WILDFLY_PROVIDER_END#<!-- BASIC_WILDFLY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # spengo modalita ssl
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # ripristino file
  zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # eliminazione dei file temporanei
  rm -rf $APP_CONTEXT_BASE_DIR

  echo "API-Legacy abilitazione HTTP Basic-auth wildfly completata.";
fi

if $LEGACY_BASIC_GP
then
  echo "API-Legacy abilitazione HTTP Basic-auth GovPay...";

  API_PREFIX="api-legacy-"
  unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # accendo la modalita' basic
  sed -i -e "s#BASIC_GOVPAY_PROVIDER_START#BASIC_GOVPAY_PROVIDER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#BASIC_GOVPAY_PROVIDER_END#<!-- BASIC_GOVPAY_PROVIDER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # spengo modalita ssl
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # ripristino file
  zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # eliminazione dei file temporanei
  rm -rf $APP_CONTEXT_BASE_DIR

  echo "API-Legacy abilitazione HTTP Basic-auth GovPay completata.";
fi

if $LEGACY_HEADER
then
  echo "API-Legacy abilitazione HTTP Header-auth...";

  API_PREFIX="api-legacy-"
  unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # spengo modalita ssl
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # accendo modalita hdrcert
  sed -i -e "s#HEADER_START#HEADER_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#HEADER_END#<!-- HEADER_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # ripristino file
  zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # eliminazione dei file temporanei
  rm -rf $APP_CONTEXT_BASE_DIR

  echo "API-Legacy abilitazione HTTP Header-auth completata.";
fi

if $LEGACY_SSL_HEADER
then
  echo "API-Legacy abilitazione hdrcert...";

  API_PREFIX="api-legacy-"
  unzip -q $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # spengo modalita ssl
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # accendo modalita hdrcert
  sed -i -e "s#SSL_HDR_START#SSL_HDR_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#SSL_HDR_END#<!-- SSL_HDR_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # ripristino file
  zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # eliminazione dei file temporanei
  rm -rf $APP_CONTEXT_BASE_DIR

  echo "API-Legacy abilitazione hdrcert completata.";
fi

zip -qr $GOVPAY_EAR_NAME *

popd

mv $GOVPAY_TMP_DIR/$GOVPAY_EAR_NAME .
rm -rf $GOVPAY_TMP_DIR

popd

cp $GOVPAY_WORK_DIR/$GOVPAY_EAR_NAME $GOVPAY_SRC_DIR
rm -rf $GOVPAY_WORK_DIR

echo "Configurazione govpay.ear completata";

