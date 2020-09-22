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
package it.govpay.core.business;


import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Iuv.TipoIUV;

public class Iuv {

	private static Logger log = LoggerWrapperFactory.getLogger(Iuv.class);

	public Iuv() {
	}

	public String generaIUV(Applicazione applicazione, Dominio dominio, String codVersamentoEnte, TipoIUV type, BasicBD bd) throws GovPayException, ServiceException, UtilsException {

		// Build prefix
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		String prefix = GovpayConfig.getInstance().getDefaultCustomIuvGenerator().buildPrefix(applicazione, dominio, appContext.getPagamentoCtx().getAllIuvProps(applicazione));
		IuvBD iuvBD = null; 

		try {
			if(bd == null) {
				iuvBD = new IuvBD(configWrapper);
			} else {
				iuvBD = new IuvBD(bd);
				iuvBD.setAtomica(false); 
			}
			it.govpay.model.Iuv iuv = null;

			log.debug("Generazione dello IUV di tipo ["+type+"] per il versamento [Id: "+codVersamentoEnte+", IdA2A: "+applicazione.getCodApplicazione()+", IdDominio: "+dominio.getCodDominio()+"] in corso...");

			log.debug("Prefisso IUV ["+prefix+"]");

			if(type.equals(TipoIUV.NUMERICO)) {
				// il prefisso deve essere numerico
				if(StringUtils.isNotEmpty(prefix)) { 
					try {
						Long.parseLong(prefix);
					} catch (NumberFormatException e) {
						ctx.getApplicationLogger().log("iuv.generazioneIUVPrefixFail", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), "Il prefisso generato non e' numerico", appContext.getPagamentoCtx().getAllIuvPropsString(applicazione));
						throw new GovPayException(EsitoOperazione.VER_028,prefix, applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getCodDominio(), dominio.getIuvPrefix());
					}
				}
				iuv = iuvBD.generaIuv(applicazione, dominio, codVersamentoEnte, TipoIUV.NUMERICO, prefix);
			} else {
				// Il prefisso deve avere solo caratteri ammissibili
				if(prefix.matches("[a-zA-Z0-9]*"))
					iuv = iuvBD.generaIuv(applicazione, dominio, codVersamentoEnte, TipoIUV.ISO11694, prefix);
				else {
					ctx.getApplicationLogger().log("iuv.generazioneIUVPrefixFail", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), "Il prefisso generato non e' alfanumerico", appContext.getPagamentoCtx().getAllIuvPropsString(applicazione));
					throw new GovPayException(EsitoOperazione.VER_029,prefix, applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getCodDominio(), dominio.getIuvPrefix());
				}
			}

			ctx.getApplicationLogger().log("iuv.generazioneIUVOk", applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getCodDominio(), iuv.getIuv());

			log.debug("Generazione dello IUV di tipo ["+type+"] per il versamento [Id: "+codVersamentoEnte+", IdA2A: "+applicazione.getCodApplicazione()+", IdDominio: "+dominio.getCodDominio()+"] completata IUV: ["+iuv.getIuv()+"].");

			return iuv.getIuv();

		} finally {
			if(iuvBD != null && bd == null) {
				iuvBD.closeConnection();
			}
		}
	}	

	public TipoIUV getTipoIUV(String iuvProposto) {
		try {
			Long.parseLong(iuvProposto);
		} catch (NumberFormatException e) {
			return TipoIUV.ISO11694;
		}
		return TipoIUV.NUMERICO;
	}

	public void checkIUV(Dominio dominio, String iuvProposto, TipoIUV tipo) throws GovPayException, ServiceException, UtilsException {
		if(tipo.equals(TipoIUV.NUMERICO) && !IuvUtils.checkIuvNumerico(iuvProposto, dominio.getAuxDigit(), dominio.getStazione().getApplicationCode())) {
			//			throw new GovPayException(EsitoOperazione.VER_017, iuvProposto);
			ContextThreadLocal.get().getApplicationLogger().log("iuv.checkIUVNumericoWarn", dominio.getAuxDigit()+"", dominio.getStazione().getApplicationCode()+"",iuvProposto);
		}
	}

}
