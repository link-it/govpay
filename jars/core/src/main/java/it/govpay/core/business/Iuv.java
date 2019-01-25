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

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.IContext;

import it.govpay.bd.BasicBD;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Iuv.TipoIUV;

public class Iuv extends BasicBD {
	
	public Iuv(BasicBD basicBD) {
		super(basicBD);
	}
	
	public it.govpay.model.Iuv generaIUV(Applicazione applicazione, Dominio dominio, String codVersamentoEnte, TipoIUV type) throws GovPayException, ServiceException, UtilsException {
		
		// Build prefix
		IContext ctx = GpThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		String prefix = GovpayConfig.getInstance().getDefaultCustomIuvGenerator().buildPrefix(applicazione, dominio, appContext.getPagamentoCtx().getAllIuvProps(applicazione));
		IuvBD iuvBD = new IuvBD(this);
		it.govpay.model.Iuv iuv = null;
		
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
		
		// Verifico che lo iuv generato rispetti la regular expr definita nell'applicazione
		
		String regexp = (applicazione.getRegExp() != null && !applicazione.getRegExp().isEmpty()) ? applicazione.getRegExp() : ".*";
		Pattern patternIuv = Pattern.compile(regexp);
		Matcher matcher = patternIuv.matcher(iuv.getIuv()); 
		if(!matcher.matches())
			throw new GovPayException(EsitoOperazione.VER_030, prefix ,applicazione.getCodApplicazione()); 
		
		ctx.getApplicationLogger().log("iuv.generazioneIUVOk", applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getCodDominio(), iuv.getIuv());
		
		return iuv;
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
			GpThreadLocal.get().getApplicationLogger().log("iuv.checkIUVNumericoWarn", dominio.getAuxDigit()+"", dominio.getStazione().getApplicationCode()+"",iuvProposto);
		}
	}
	
	public it.govpay.model.Iuv caricaIUV(Applicazione applicazione, Dominio dominio, String iuvProposto, TipoIUV tipo, String codVersamentoEnte) throws GovPayException, ServiceException, UtilsException{
		it.govpay.model.Iuv iuv = null;
		IuvBD iuvBD = new IuvBD(this);
		// Controllo se esiste gia'
		try {
			iuv = iuvBD.getIuv(dominio.getId(), iuvProposto);
			if(!iuv.getCodVersamentoEnte().equals(codVersamentoEnte)) {
				GpThreadLocal.get().getApplicationLogger().log("iuv.caricamentoIUVKo", applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getCodDominio(), iuvProposto);
				throw new GovPayException(EsitoOperazione.VER_018, iuvProposto, iuv.getCodVersamentoEnte());
			}
		} catch (NotFoundException ne) {
			// Non esiste, lo carico 
			iuv = new it.govpay.model.Iuv();
			iuv.setIdDominio(dominio.getId());
			iuv.setPrg(0);
			iuv.setIuv(iuvProposto);
			iuv.setDataGenerazione(new Date());
			iuv.setIdApplicazione(applicazione.getId());
			iuv.setTipo(tipo);
			iuv.setCodVersamentoEnte(codVersamentoEnte);
			iuv.setApplicationCode(dominio.getStazione().getApplicationCode());
			iuvBD.insertIuv(iuv);
			GpThreadLocal.get().getApplicationLogger().log("iuv.caricamentoIUVOk", applicazione.getCodApplicazione(), iuv.getCodVersamentoEnte(), dominio.getCodDominio(), iuv.getIuv());
		}
		return iuv;
	}

	
}
