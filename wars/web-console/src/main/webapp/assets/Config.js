(function (global) {

  function addScript(file) {
    document.write('<script src="' + file + '"></script>');
  }

  function hostname() {
    return this.location.protocol + '//' + this.location.host;
  }

  function httpRootService() {
    return '${it.govpay.backoffice.gui.apiBaseUrl}'; // Root URL servizi pubblici
  }

  function httpLogOutService() {
    return '${it.govpay.backoffice.gui.apiBaseUrl}/logout'; // URL per il servizio di 'logout' pubblico
  }

  function httpDocumentsRoot() {
    return hostname() + '/govpay/backend/api/backoffice/public'; // Root URL informazioni/documenti
  }

  // Map key generators
  function _mkg() {
    return {
      ANGULAR2_JSON_SCHEMA_FORM: 'angular2-json-schema-form',
      SURVEYJS_FORM: 'surveyjs'
    };
  }

  // JSON Schema form generators list selection
  function generatori() {
    return [
      { label: 'Angular Json schema form', value: _mkg().ANGULAR2_JSON_SCHEMA_FORM },
      { label: 'SurveyJS', value: _mkg().SURVEYJS_FORM }
    ];
  }

  // Default application name/title
  function _applicationName() {
    const _NAME = '${it.govpay.backoffice.gui.appName}';
    document.title = _NAME;
    return _NAME;
  }

  const _HTTP_DOCUMENTS_ROOT = httpDocumentsRoot();

  global.GovPayConfig = {
    HOST_NAME: hostname(),
    INFO: {
      DOCUMENTS: {
        ENABLED: ${it.govpay.backoffice.gui.info.enabled},
        HTTP_DOCUMENTS_ROOT: _HTTP_DOCUMENTS_ROOT,
        LICENSE: 'https://github.com/link-it/govpay/blob/master/LICENSE',
        MANUALE_UTENTE: 'https://govpay.readthedocs.io/it/master/index.html'
      },
      NEWS: {
        ENABLED: ${it.govpay.backoffice.gui.news.enabled},
        URL: 'https://api.github.com/repos/link-it/GovPay/releases' // URL GovPay Github distribuzioni
      },
      APP_NAME: _applicationName(), // Default application name
      PROGETTO_GOVPAY: 'https://github.com/link-it/GovPay' // URL GovPay Github
    },
    BADGE_FILTER: {
      HOUR: 1, // Ore, (filtro badge)
      TIMER: 30000, // Millisecondi, (timer badge)
      QUERY_PARAMETERS: {
        IN_CORSO: '',
        IN_CORSO_VERIFICATI: '',
        FALLITI: '&severitaDa=4',
        FALLITI_VERIFICATI: '&severitaDa=4'
      }
    },
    GENERATORI: generatori(),
    MGK: _mkg(),
    EXTERNAL_JS_PROCEDURE_URL: '${it.govpay.backoffice.gui.transformerJS}', //Http URL al file Js per conversioni esterne (caricamento tracciati)
    BASIC: {
      ENABLED: ${it.govpay.backoffice.gui.basic.enabled},
      HTTP_ROOT_SERVICE: httpRootService(),
      HTTP_LOGOUT_SERVICE: httpLogOutService()
    },
    SPID: {
      ENABLED: ${it.govpay.backoffice.gui.spid.enabled},
      HTTPS_ROOT_SERVICE: '${it.govpay.backoffice.gui.spid.apibaseurl}',
      HTTPS_LOGOUT_SERVICE: '${it.govpay.backoffice.gui.spid.logout}',
      SERVICE_TARGET: '${it.govpay.backoffice.gui.spid.return}',
      ACTION_FORM_URL: '${it.govpay.backoffice.gui.spid.formaction}',
      PROVIDERS: {
        SPID_TEST: '${it.govpay.backoffice.gui.spid.testprovider}',
        ARUBA: 'https://sp.agenziaentrate.gov.it/rp/aruba/s3',
        INFOCERT: 'https://sp.agenziaentrate.gov.it/rp/infocert/s3',
        INTESA: 'https://sp.agenziaentrate.gov.it/rp/intesa/s3',
        LEPIDA: 'https://sp.agenziaentrate.gov.it/rp/lepida/s3',
        NAMIRIAL: 'https://sp.agenziaentrate.gov.it/rp/namirial/s3',
        POSTE: 'https://sp.agenziaentrate.gov.it/rp/poste/s3',
        REGISTER: 'https://sp.agenziaentrate.gov.it/rp/register/s3',
        SIELTE: 'https://sp.agenziaentrate.gov.it/rp/sielte/s3',
        TIM: 'https://sp.agenziaentrate.gov.it/rp/titt/s3'
      }
    },
    IAM: {
      ENABLED: ${it.govpay.backoffice.gui.iam.enabled},
      LOGIN_URL: '${it.govpay.backoffice.gui.iam.login}',
      ROOT_SERVICE: '${it.govpay.backoffice.gui.iam.apibaseurl}',
      LOGOUT_SERVICE: '${it.govpay.backoffice.gui.iam.logout}'
    },
    OAUTH2: {
      ENABLED: ${it.govpay.backoffice.gui.oauth2.enabled},
      LOGIN_URL: '${it.govpay.backoffice.gui.oauth2.login}',
      ROOT_SERVICE: '${it.govpay.backoffice.gui.oauth2.apibaseurl}',
      LOGOUT_SERVICE: '${it.govpay.backoffice.gui.oauth2.logout}',
      REDIRECT_URI: '${it.govpay.backoffice.gui.oauth2.redirecturl}',
      CLIENT_ID: '${it.govpay.backoffice.gui.oauth2.clientid}',
      GRANT_TYPE: '${it.govpay.backoffice.gui.oauth2.granttype}',
      TOKEN_URL: '${it.govpay.backoffice.gui.oauth2.tokenurl}',
      CODE_CHALLENGE_METHOD: '${it.govpay.backoffice.gui.oauth2.codechallengemethod}',
      SCOPE: '${it.govpay.backoffice.gui.oauth2.scope}',
      RESPONSE_TYPE: '${it.govpay.backoffice.gui.oauth2.responsetype}',
      TOKEN_KEY: '${it.govpay.backoffice.gui.oauth2.tokenkey}',
      BOX_TITLE: '${it.govpay.backoffice.gui.oauth2.boxtitle}',
      BUTTON_LABEL: '${it.govpay.backoffice.gui.oauth2.buttonlabel}'
    },
    GESTIONE_PASSWORD: {
      ENABLED: ${it.govpay.backoffice.gui.gestionepassword.enabled}
    },
    PREFERENCES: {
      TIMEOUT: ${it.govpay.backoffice.gui.export.timeout},            // Http timeout NN(millisec)|false
      MAX_EXPORT_LIMIT: ${it.govpay.backoffice.gui.export.limit},     // Max page elements
      MAX_THREAD_EXPORT_LIMIT: ${it.govpay.backoffice.gui.export.thread}, // Max sincro calls
      POLLING_TRACCIATI: ${it.govpay.backoffice.gui.export.polling}   // Timeout per tracciati NN(millisec)|false
    },
    GESTIONE_PAGAMENTI: {
      ENABLED: ${it.govpay.backoffice.gui.sezionePagamenti.enabled}  // abilita la sezione pagamenti
    },
    GESTIONE_RISCOSSIONI: {
      ENABLED: ${it.govpay.backoffice.gui.sezioneRiscossioni.enabled}  // abilita la sezione riscossioni
    }
  };

  addScript('assets/config/app-config.js');
  addScript('assets/config/mappingTipiEvento.js');

})(window);
