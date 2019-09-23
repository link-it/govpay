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
			govpay_backoffice_password: 'password',
			ndpsym_url: 'http://localhost:8080/govpay-ndpsym',
			ndpsym_user: 'ndpsym',
			ente_api_url: 'http://localhost:8888/paServiceImpl',
			pagopa_api_url: 'http://localhost:8888/pagopa'
	};
	return config;
}