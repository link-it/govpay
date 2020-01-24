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
    echo "   -bo <args> : lista di autenticazioni da abilitare sulle api di backoffice (spid,header,basic,ssl,session). Default: basic,ssl" 
    echo "   -pag <args> : lista di autenticazioni da abilitare sulle api di pagamento (spid,header,basic,ssl,public,session). Default: basic,ssl"
    echo "   -pen <args> : lista di autenticazioni da abilitare sulle api di pendenza (basic,ssl). Default: basic,ssl"
    echo "   -rag <args> : lista di autenticazioni da abilitare sulle api di ragioneria (basic,ssl). Default: basic,ssl"
    echo "   -usr <args> : lista di autenticazioni da abilitare sulle api di user (spid). Default: "
    echo "   -pp <args> : autenticazione da abilitare sulle api di pagopa (basic,ssl). Default: ssl"
    echo "   -d <args> : autenticazione da abilitare sui contesti senza autenticazione per retro-compatibilita (basic,ssl). Default: none"
    exit 2;
    ;;
esac
done
set -- "${POSITIONAL[@]}" # restore positional parameters


BACKOFFICE_BASIC=true
BACKOFFICE_SSL=true
[[ $BACKOFFICE == *"header"* ]] && BACKOFFICE_HEADER=true || BACKOFFICE_HEADER=false
[[ $BACKOFFICE == *"spid"* ]] && BACKOFFICE_SPID=true || BACKOFFICE_SPID=false
[[ $BACKOFFICE == *"session"* ]] && BACKOFFICE_SESSION=true || BACKOFFICE_SESSION=false

PAGAMENTI_BASIC=true
PAGAMENTI_SSL=true
[[ $PAGAMENTI == *"header"* ]] && PAGAMENTI_HEADER=true || PAGAMENTI_HEADER=false
[[ $PAGAMENTI == *"spid"* ]] && PAGAMENTI_SPID=true || PAGAMENTI_SPID=false
[[ $PAGAMENTI == *"public"* ]] && PAGAMENTI_PUBLIC=true || PAGAMENTI_PUBLIC=false
[[ $PAGAMENTI == *"session"* ]] && PAGAMENTI_SESSION=true || PAGAMENTI_SESSION=false

PENDENZE_BASIC=true
PENDENZE_SSL=true
[[ $PENDENZE == *"header"* ]] && PENDENZE_HEADER=true || PENDENZE_HEADER=false

RAGIONERIA_BASIC=true
RAGIONERIA_SSL=true
[[ $RAGIONERIA == *"header"* ]] && RAGIONERIA_HEADER=true || RAGIONERIA_HEADER=false

[[ $UTENTE == *"spid"* ]] && UTENTE_SPID=true || UTENTE_SPID=false

[[ $PAGOPA == *"basic"* ]] && PAGOPA_BASIC=true || PAGOPA_BASIC=false

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
if $DEFAULT_BASIC
then
  echo "API-Backoffice abilitazione default HTTP BASIC ...";
  sed -i -e "s#DEFAULT_BASIC_START#DEFAULT_BASIC_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_BASIC_END#<!-- DEFAULT_BASIC_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
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
  sed -i -e "s#DEFAULT_BASIC_START#DEFAULT_BASIC_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_BASIC_END#<!-- DEFAULT_BASIC_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
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
  sed -i -e "s#DEFAULT_BASIC_START#DEFAULT_BASIC_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_BASIC_END#<!-- DEFAULT_BASIC_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
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
  sed -i -e "s#DEFAULT_BASIC_START#DEFAULT_BASIC_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#DEFAULT_BASIC_END#<!-- DEFAULT_BASIC_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
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

  # spengo la modalita' ssl 
  sed -i -e "s#BASIC_START#BASIC_START -->#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#BASIC_END#<!-- BASIC_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # accendo modalita basic
  sed -i -e "s#SSL_START -->#SSL_START#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX
  sed -i -e "s#<!-- SSL_END#SSL_END#g" $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # ripristino file
  zip -qr $API_PREFIX$GOVPAY_VERSION.war $APP_CONTEXT_BASE_DIR/$API_PREFIX$CONTEXT_SECURITY_XML_SUFFIX

  # eliminazione dei file temporanei
  rm -rf $APP_CONTEXT_BASE_DIR

  echo "API-PagoPA abilitazione HTTP Basic-auth completata.";
fi

zip -qr $GOVPAY_EAR_NAME *

popd

mv $GOVPAY_TMP_DIR/$GOVPAY_EAR_NAME .
rm -rf $GOVPAY_TMP_DIR

popd

cp $GOVPAY_WORK_DIR/$GOVPAY_EAR_NAME $GOVPAY_SRC_DIR
rm -rf $GOVPAY_WORK_DIR

echo "Configurazione govpay.ear completata";

