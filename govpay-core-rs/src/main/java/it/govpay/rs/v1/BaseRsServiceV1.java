/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.rs.v1;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.JsonValidatorAPI.ApiName;
import org.openspcoop2.utils.openapi.validator.OpenapiApiValidatorConfig;
import org.openspcoop2.utils.openapi.validator.Validator;
import org.openspcoop2.utils.rest.ApiFactory;
import org.openspcoop2.utils.rest.ApiFormats;
import org.openspcoop2.utils.rest.ApiReaderConfig;
import org.openspcoop2.utils.rest.IApiReader;
import org.openspcoop2.utils.rest.api.Api;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.rs.BaseRsService;

public class BaseRsServiceV1 extends BaseRsService {
	
	public static Validator validator;
	private static boolean validatorInitialized = false;

	public static synchronized void initValidator(Logger log, byte[] swaggerBackOffice) throws Exception{
		log.info("Init modulo di validazione in corso...");
		if(GovpayConfig.getInstance().isValidazioneAPIRestAbilitata() && !validatorInitialized) {
			try {
				IApiReader apiReader = ApiFactory.newApiReader(ApiFormats.OPEN_API_3);

				apiReader.init(log, swaggerBackOffice, new ApiReaderConfig());
				Api api = apiReader.read();
				
				validator = (Validator) ApiFactory.newApiValidator(ApiFormats.OPEN_API_3);
				OpenapiApiValidatorConfig config = new OpenapiApiValidatorConfig();
				config.setJsonValidatorAPI(ApiName.FGE);
				Logger logger = LoggerWrapperFactory.getLogger(BaseRsService.class);
				validator.init(logger, api, config);
				validatorInitialized = true;
			} catch(Throwable e) {
				log.error("Errore durante l'init del modulo di validazione: " + e.getMessage(), e);
				throw e;
			}
		}
		log.info("Init modulo di validazione completato.");
	}
	
	public BaseRsServiceV1(String nomeServizio) throws ServiceException {
		super(nomeServizio);
	}
	
	@Override
	public int getVersione() {
		return 1;
	}

	public static boolean isInitialized() {
		return BaseRsServiceV1.validatorInitialized;
	}
}
