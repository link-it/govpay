function() {    
	var env = karate.env; // get system property 'karate.env'
	karate.log('karate.env system property was:', env);
	if (!env) {
		env = 'dev';
	}
	if (env == 'dev') {
		// customize
		// e.g. config.foo = 'bar';
	} else if (env == 'e2e') {
		// customize
	}
	karate.configure('connectTimeout', 20000);
	karate.configure('readTimeout', 20000);  
	var protocol = 'http';
	var servizioNotificaVerificaPort = '8888';
	var govpayUrl = 'http://localhost:8080';

	var config = {
			env: env,
			govpay_url: 'http://localhost:8080',
			govpay_backoffice_user: 'gpadmin',
			govpay_backoffice_password: 'Password1!',
			ndpsym_url: 'http://localhost:8180/govpay-ndpsym',
			ndpsym_user: 'ndpsym',
			ente_api_url: 'http://localhost:8888/paServiceImpl',
			ente_api_rendicontazioni_url: 'http://localhost:8888/enteRendicontazioni',
			pagopa_api_url: 'http://localhost:8888/pagopa',
			recaptcha_api_url: 'http://localhost:8888/recaptcha',
			appio_api_url: 'http://localhost:8888/appio',
			maggioli_url: 'http://localhost:8888/maggioli',
			deploy_ear: false,
			govpay_api_backoffice_context : '/govpay-api-backoffice',
			govpay_api_pagamento_context : '/govpay-api-pagamento',
			govpay_api_pendenze_context : '/govpay-api-pendenze',
			govpay_api_ragioneria_context : '/govpay-api-ragioneria',
			govpay_api_pagopa_context : '/govpay-api-pagopa',
			govpay_api_jppapdp_context : '/govpay-api-jppapdp',
			govpay_api_user_context : '/govpay-api-user',
			govpay_web_connector_context : '/govpay-web-connector',
			govpay_api_backoffice_url : 'http://localhost:8080/govpay-api-backoffice',
			govpay_api_pagamento_url : 'http://localhost:8080/govpay-api-pagamento',
			govpay_api_pendenze_url : 'http://localhost:8080/govpay-api-pendenze',
			govpay_api_ragioneria_url : 'http://localhost:8080/govpay-api-ragioneria',
			govpay_api_pagopa_url : 'http://localhost:8080/govpay-api-pagopa',
			govpay_api_jppapdp_url : 'http://localhost:8080/govpay-api-jppapdp',
			govpay_api_user_url : 'http://localhost:8080/govpay-api-user',
			govpay_web_connector_url : 'http://localhost:8080/govpay-web-connector',
	};
	
	if (env == 'wildfly') {
		config.deploy_ear = true;
		config.ndpsym_url = 'http://localhost:8080/govpay-ndpsym';
		config.govpay_api_backoffice_context = '/govpay/backend/api/backoffice';
		config.govpay_api_pagamento_context = '/govpay/frontend/api/pagamento';
		config.govpay_api_pendenze_context = '/govpay/backend/api/pendenze';
		config.govpay_api_ragioneria_context = '/govpay/backend/api/ragioneria';
		config.govpay_api_pagopa_context = '/govpay/frontend/api/pagopa';
		config.govpay_api_jppapdp_context = '/govpay/backend/api/jppapdp';
		config.govpay_api_user_context = '/govpay/frontend/api/user';
		config.govpay_web_connector_context = '/govpay/frontend/web/connector';
		config.govpay_api_backoffice_url = 'http://localhost:8080/govpay/backend/api/backoffice';
		config.govpay_api_pagamento_url = 'http://localhost:8080/govpay/frontend/api/pagamento';
		config.govpay_api_pendenze_url = 'http://localhost:8080/govpay/backend/api/pendenze';
		config.govpay_api_ragioneria_url = 'http://localhost:8080/govpay/backend/api/ragioneria';
		config.govpay_api_pagopa_url = 'http://localhost:8080/govpay/frontend/api/pagopa';
		config.govpay_api_jppapdp_url = 'http://localhost:8080/govpay/backend/api/jppapdp';
		config.govpay_api_user_url = 'http://localhost:8080/govpay/frontend/api/user';
		config.govpay_web_connector_url = 'http://localhost:8080/govpay/frontend/web/connector';
	}
	
	return config;
}