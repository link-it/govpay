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
package it.govpay.bd.anagrafica;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.cache.CacheJMXUtils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.cache.ApplicazioniBDCacheJmx;
import it.govpay.bd.anagrafica.cache.ApplicazioniBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.DominiBDCacheJmx;
import it.govpay.bd.anagrafica.cache.DominiBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.IbanAccreditoBDCacheJmx;
import it.govpay.bd.anagrafica.cache.IbanAccreditoBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.IntermediariBDCacheJmx;
import it.govpay.bd.anagrafica.cache.IntermediariBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.OperatoriBDCacheJmx;
import it.govpay.bd.anagrafica.cache.OperatoriBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.StazioniBDCacheJmx;
import it.govpay.bd.anagrafica.cache.StazioniBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.TipiTributoBDCacheJmx;
import it.govpay.bd.anagrafica.cache.TipiTributoBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.TributiBDCacheJmx;
import it.govpay.bd.anagrafica.cache.TributiBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.UoBDCacheJmx;
import it.govpay.bd.anagrafica.cache.UoBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.UtenzeBDCacheJmx;
import it.govpay.bd.anagrafica.cache.UtenzeBDCacheWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Applicazione;
import it.govpay.model.Intermediario;
import it.govpay.model.TipoTributo;
import it.govpay.bd.model.Utenza;

public class AnagraficaManager {
	private static final String CACHE_KEY_GET_TIPO_TRIBUTO = "getTipoTributo";
	private static final String CACHE_KEY_GET_TRIBUTO = "getTributo";
	private static final String CACHE_KEY_GET_STAZIONE = "getStazione";
	private static final String CACHE_KEY_GET_UTENZA = "getUtenza";
	private static final String CACHE_KEY_GET_OPERATORE_BY_SUBJECT = "getOperatoreBySubject";
	private static final String CACHE_KEY_GET_OPERATORE_BY_PRINCIPAL = "getOperatoreByPrincipal";
	private static final String CACHE_KEY_GET_OPERATORE = "getOperatore";
	private static final String CACHE_KEY_GET_INTERMEDIARIO = "getIntermediario";
	private static final String CACHE_KEY_GET_IBAN_ACCREDITO = "getIbanAccredito";
	private static final String CACHE_KEY_GET_UNITA_OPERATIVA = "getUnitaOperativa";
	private static final String CACHE_KEY_GET_DOMINIO = "getDominio";
	private static final String CACHE_KEY_GET_APPLICAZIONE_BY_SUBJECT = "getApplicazioneBySubject";
	private static final String CACHE_KEY_GET_APPLICAZIONE_BY_PRINCIPAL = "getApplicazioneByPrincipal";
	private static final String CACHE_KEY_GET_APPLICAZIONE = "getApplicazione";

	private static final boolean DEBUG = false;
	
	private static DominiBDCacheWrapper dominiBDCacheWrapper, dominiBDNoCacheWrapper;
	private static ApplicazioniBDCacheWrapper applicazioniBDCacheWrapper, applicazioniBDNoCacheWrapper;
	private static UoBDCacheWrapper uoBDCacheWrapper, uoBDNoCacheWrapper;
	private static IbanAccreditoBDCacheWrapper ibanAccreditoBDCacheWrapper, ibanAccreditoBDNoCacheWrapper;
	private static IntermediariBDCacheWrapper intermediariBDCacheWrapper, intermediariBDNoCacheWrapper;
	private static OperatoriBDCacheWrapper operatoriBDCacheWrapper, operatoriBDNoCacheWrapper;
	private static StazioniBDCacheWrapper stazioniBDCacheWrapper, stazioniBDNoCacheWrapper;
	private static TributiBDCacheWrapper tributiBDCacheWrapper, tributiBDNoCacheWrapper;
	private static TipiTributoBDCacheWrapper tipiTributoBDCacheWrapper, tipiTributoBDNoCacheWrapper;
	private static UtenzeBDCacheWrapper utenzeBDCacheWrapper, utenzeBDNoCacheWrapper;

	private AnagraficaManager(String domain) throws UtilsException {
		
		dominiBDCacheWrapper = new DominiBDCacheWrapper(true, LoggerWrapperFactory.getLogger(DominiBDCacheWrapper.class));
		applicazioniBDCacheWrapper = new ApplicazioniBDCacheWrapper(true, LoggerWrapperFactory.getLogger(ApplicazioniBDCacheWrapper.class));
		uoBDCacheWrapper = new UoBDCacheWrapper(true, LoggerWrapperFactory.getLogger(UoBDCacheWrapper.class));
		ibanAccreditoBDCacheWrapper = new IbanAccreditoBDCacheWrapper(true, LoggerWrapperFactory.getLogger(IbanAccreditoBDCacheWrapper.class));
		intermediariBDCacheWrapper = new IntermediariBDCacheWrapper(true, LoggerWrapperFactory.getLogger(IntermediariBDCacheWrapper.class));
		operatoriBDCacheWrapper = new OperatoriBDCacheWrapper(true, LoggerWrapperFactory.getLogger(OperatoriBDCacheWrapper.class));
		stazioniBDCacheWrapper = new StazioniBDCacheWrapper(true, LoggerWrapperFactory.getLogger(StazioniBDCacheWrapper.class));
		tributiBDCacheWrapper = new TributiBDCacheWrapper(true, LoggerWrapperFactory.getLogger(TributiBDCacheWrapper.class));
		tipiTributoBDCacheWrapper = new TipiTributoBDCacheWrapper(true, LoggerWrapperFactory.getLogger(TipiTributoBDCacheWrapper.class));
		utenzeBDCacheWrapper = new UtenzeBDCacheWrapper(true, LoggerWrapperFactory.getLogger(UtenzeBDCacheWrapper.class));
		
		dominiBDNoCacheWrapper = new DominiBDCacheWrapper(false, LoggerWrapperFactory.getLogger(DominiBDCacheWrapper.class));
		applicazioniBDNoCacheWrapper = new ApplicazioniBDCacheWrapper(false, LoggerWrapperFactory.getLogger(ApplicazioniBDCacheWrapper.class));
		uoBDNoCacheWrapper = new UoBDCacheWrapper(false, LoggerWrapperFactory.getLogger(UoBDCacheWrapper.class));
		ibanAccreditoBDNoCacheWrapper = new IbanAccreditoBDCacheWrapper(false, LoggerWrapperFactory.getLogger(IbanAccreditoBDCacheWrapper.class));
		intermediariBDNoCacheWrapper = new IntermediariBDCacheWrapper(false, LoggerWrapperFactory.getLogger(IntermediariBDCacheWrapper.class));
		operatoriBDNoCacheWrapper = new OperatoriBDCacheWrapper(false, LoggerWrapperFactory.getLogger(OperatoriBDCacheWrapper.class));
		stazioniBDNoCacheWrapper = new StazioniBDCacheWrapper(false, LoggerWrapperFactory.getLogger(StazioniBDCacheWrapper.class));
		tributiBDNoCacheWrapper = new TributiBDCacheWrapper(false, LoggerWrapperFactory.getLogger(TributiBDCacheWrapper.class));
		tipiTributoBDNoCacheWrapper = new TipiTributoBDCacheWrapper(false, LoggerWrapperFactory.getLogger(TipiTributoBDCacheWrapper.class));
		utenzeBDNoCacheWrapper = new UtenzeBDCacheWrapper(false, LoggerWrapperFactory.getLogger(UtenzeBDCacheWrapper.class));

		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new DominiBDCacheJmx(), domain, "domini");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new ApplicazioniBDCacheJmx(), domain, "applicazioni");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new UoBDCacheJmx(), domain, "uo");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new IbanAccreditoBDCacheJmx(), domain, "ibanAccredito");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new IntermediariBDCacheJmx(), domain, "intermediari");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new OperatoriBDCacheJmx(), domain, "operatori");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new StazioniBDCacheJmx(), domain, "stazioni");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new TributiBDCacheJmx(), domain, "tributi");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new TipiTributoBDCacheJmx(), domain, "tipiTributo");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new UtenzeBDCacheJmx(), domain, "utenze");
	}

	public static AnagraficaManager newInstance(String dominio) throws UtilsException {
		return new AnagraficaManager(dominio);
	}
	
	public static DominiBDCacheWrapper getDominiBDCacheWrapper() {
		return dominiBDCacheWrapper;
	}

	public static ApplicazioniBDCacheWrapper getApplicazioniBDCacheWrapper() {
		return applicazioniBDCacheWrapper;
	}

	public static UoBDCacheWrapper getUoBDCacheWrapper() {
		return uoBDCacheWrapper;
	}

	public static IbanAccreditoBDCacheWrapper getIbanAccreditoBDCacheWrapper() {
		return ibanAccreditoBDCacheWrapper;
	}

	public static IntermediariBDCacheWrapper getIntermediariBDCacheWrapper() {
		return intermediariBDCacheWrapper;
	}

	public static OperatoriBDCacheWrapper getOperatoriBDCacheWrapper() {
		return operatoriBDCacheWrapper;
	}
	
	public static UtenzeBDCacheWrapper getUtenzeBDCacheWrapper() {
		return utenzeBDCacheWrapper;
	}

	public static StazioniBDCacheWrapper getStazioniBDCacheWrapper() {
		return stazioniBDCacheWrapper;
	}

	public static TributiBDCacheWrapper getTributiBDCacheWrapper() {
		return tributiBDCacheWrapper;
	}
	
	public static TipiTributoBDCacheWrapper getTipiTributoBDCacheWrapper() {
		return tipiTributoBDCacheWrapper;
	}
	
	public static DominiBDCacheWrapper getDominiBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? dominiBDCacheWrapper : dominiBDNoCacheWrapper;
	}

	public static ApplicazioniBDCacheWrapper getApplicazioniBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? applicazioniBDCacheWrapper : applicazioniBDNoCacheWrapper;
	}

	public static UoBDCacheWrapper getUoBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? uoBDCacheWrapper : uoBDNoCacheWrapper;
	}

	public static IbanAccreditoBDCacheWrapper getIbanAccreditoBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? ibanAccreditoBDCacheWrapper : ibanAccreditoBDNoCacheWrapper;
	}

	public static IntermediariBDCacheWrapper getIntermediariBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? intermediariBDCacheWrapper : intermediariBDNoCacheWrapper;
	}

	public static OperatoriBDCacheWrapper getOperatoriBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? operatoriBDCacheWrapper : operatoriBDNoCacheWrapper;
	}
	
	public static UtenzeBDCacheWrapper getUtenzeBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? utenzeBDCacheWrapper : utenzeBDNoCacheWrapper;
	}

	public static StazioniBDCacheWrapper getStazioniBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? stazioniBDCacheWrapper : stazioniBDNoCacheWrapper;
	}

	public static TributiBDCacheWrapper getTributiBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? tributiBDCacheWrapper : tributiBDNoCacheWrapper;
	}
	
	public static TipiTributoBDCacheWrapper getTipiTributoBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? tipiTributoBDCacheWrapper : tipiTributoBDNoCacheWrapper;
	}
	
	public static void removeFromCache(Applicazione applicazione) {
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_APPLICAZIONE, String.valueOf(applicazione.getId())));} catch (Exception e) {	}
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_APPLICAZIONE, applicazione.getCodApplicazione()));} catch (Exception e) {	}
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_APPLICAZIONE_BY_PRINCIPAL, applicazione.getPrincipal()));} catch (Exception e) {	}
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_APPLICAZIONE_BY_SUBJECT, applicazione.getPrincipal()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Dominio dominio) {
		try {dominiBDCacheWrapper.removeObjectCache(dominiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_DOMINIO, String.valueOf(dominio.getId())));} catch (Exception e) {	}
		try {dominiBDCacheWrapper.removeObjectCache(dominiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_DOMINIO, dominio.getCodDominio()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(UnitaOperativa uo) {
		try {uoBDCacheWrapper.removeObjectCache(uoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UNITA_OPERATIVA, String.valueOf(uo.getId())));} catch (Exception e) {	}
		try {uoBDCacheWrapper.removeObjectCache(uoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UNITA_OPERATIVA, String.valueOf(uo.getCodUo() + "@" + uo.getIdDominio())));} catch (Exception e) {	}
		try {uoBDCacheWrapper.removeObjectCache(uoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UNITA_OPERATIVA, String.valueOf("ByCodUnivocoUo#" + uo.getCodUo() + "@" + uo.getIdDominio())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(IbanAccredito iban) {
		try {ibanAccreditoBDCacheWrapper.removeObjectCache(ibanAccreditoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_IBAN_ACCREDITO, String.valueOf(iban.getId())));} catch (Exception e) {	}
		try {ibanAccreditoBDCacheWrapper.removeObjectCache(ibanAccreditoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_IBAN_ACCREDITO, String.valueOf(iban.getCodIban())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Intermediario intermediario) {
		try {intermediariBDCacheWrapper.removeObjectCache(intermediariBDCacheWrapper.getKeyCache(CACHE_KEY_GET_INTERMEDIARIO, String.valueOf(intermediario.getId())));} catch (Exception e) {	}
		try {intermediariBDCacheWrapper.removeObjectCache(intermediariBDCacheWrapper.getKeyCache(CACHE_KEY_GET_INTERMEDIARIO, String.valueOf(intermediario.getCodIntermediario())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Operatore operatore) {
		try {operatoriBDCacheWrapper.removeObjectCache(operatoriBDCacheWrapper.getKeyCache(CACHE_KEY_GET_OPERATORE, String.valueOf(operatore.getId())));} catch (Exception e) {	}
		try {operatoriBDCacheWrapper.removeObjectCache(operatoriBDCacheWrapper.getKeyCache(CACHE_KEY_GET_OPERATORE_BY_PRINCIPAL, String.valueOf(operatore.getUtenza().getPrincipal())));} catch (Exception e) {	}
		try {operatoriBDCacheWrapper.removeObjectCache(operatoriBDCacheWrapper.getKeyCache(CACHE_KEY_GET_OPERATORE_BY_SUBJECT, String.valueOf(operatore.getUtenza().getPrincipal())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Utenza utenza) {
		try {utenzeBDCacheWrapper.removeObjectCache(utenzeBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UTENZA, String.valueOf(utenza.getId())));} catch (Exception e) {	}
		try {utenzeBDCacheWrapper.removeObjectCache(utenzeBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UTENZA, String.valueOf(utenza.getPrincipal())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Stazione stazione) {
		try {stazioniBDCacheWrapper.removeObjectCache(stazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_STAZIONE, String.valueOf(stazione.getId())));} catch (Exception e) {	}
		try {stazioniBDCacheWrapper.removeObjectCache(stazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_STAZIONE, String.valueOf(stazione.getCodStazione())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Tributo tributo) {
		try {tributiBDCacheWrapper.removeObjectCache(tributiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TRIBUTO, String.valueOf(tributo.getId())));} catch (Exception e) {	}
		try {tributiBDCacheWrapper.removeObjectCache(tributiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TRIBUTO, String.valueOf(tributo.getCodTributo() + "@" + tributo.getIdDominio())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(TipoTributo tipoTributo) {
		try {tipiTributoBDCacheWrapper.removeObjectCache(tipiTributoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TIPO_TRIBUTO, String.valueOf(tipoTributo.getId())));} catch (Exception e) {	}
		try {tipiTributoBDCacheWrapper.removeObjectCache(tipiTributoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TIPO_TRIBUTO, String.valueOf(tipoTributo.getCodTributo())));} catch (Exception e) {	}
	}
	
	public static Dominio getDominio(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_DOMINIO;
			Object dominio = getDominiBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Dominio) dominio;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static Dominio getDominio(BasicBD basicBD, String codDominio) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_DOMINIO;
			Object dominio = getDominiBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, codDominio, method, codDominio);
			return (Dominio) dominio;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static Applicazione getApplicazione(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_APPLICAZIONE;
			Object applicazione = getApplicazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Applicazione) applicazione;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static Applicazione getApplicazione(BasicBD basicBD, String codApplicazione) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_APPLICAZIONE;
			Object applicazione = getApplicazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, codApplicazione, method, codApplicazione);
			return (Applicazione) applicazione;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}
	
	public static Applicazione getApplicazioneBySubject(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_APPLICAZIONE_BY_SUBJECT;
			Object applicazione = getApplicazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, principal, method, principal);
			return (Applicazione) applicazione;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}
	
	public static Applicazione getApplicazioneByPrincipal(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_APPLICAZIONE_BY_PRINCIPAL;
			Object applicazione = getApplicazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, principal, method, principal);
			return (Applicazione) applicazione;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}


	public static UnitaOperativa getUnitaOperativa(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_UNITA_OPERATIVA;
			Object uo = getUoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (UnitaOperativa) uo;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static UnitaOperativa getUnitaOperativa(BasicBD basicBD, long idDominio, String codUo) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_UNITA_OPERATIVA;
			Object uo = getUoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, codUo + "@" + idDominio, method, idDominio, codUo);
			return (UnitaOperativa) uo;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}
	
	public static UnitaOperativa getUnitaOperativaByCodUnivocoUo(BasicBD basicBD, long idDominio, String codUnivocoUo) throws ServiceException, NotFoundException {
		try {
			String method = "getUnitaOperativaByCodUnivocoUo";
			Object uo = getUoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, "ByCodUnivocoUo#" + codUnivocoUo + "@" + idDominio, method, idDominio, codUnivocoUo);
			return (UnitaOperativa) uo;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}


	public static IbanAccredito getIbanAccredito(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_IBAN_ACCREDITO;
			Object ibanAccredito = getIbanAccreditoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (IbanAccredito) ibanAccredito;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static IbanAccredito getIbanAccredito(BasicBD basicBD, Long idDominio, String codIbanAccredito) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_IBAN_ACCREDITO;
			Object ibanAccredito = getIbanAccreditoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, codIbanAccredito + "@" + idDominio, method, idDominio, codIbanAccredito);
			return (IbanAccredito) ibanAccredito;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}


	public static Intermediario getIntermediario(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_INTERMEDIARIO;
			Object intermediario = getIntermediariBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Intermediario) intermediario;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static Intermediario getIntermediario(BasicBD basicBD, String codIntermediario) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_INTERMEDIARIO;
			Object intermediario = getIntermediariBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, codIntermediario, method, codIntermediario);
			return (Intermediario) intermediario;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static Operatore getOperatore(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_OPERATORE;
			Object operatore = getOperatoriBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Operatore) operatore;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}
	
	
	public static Operatore getOperatoreByPrincipal(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_OPERATORE_BY_PRINCIPAL;
			Object operatore = getOperatoriBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, principal, method, principal);
			return (Operatore) operatore;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}
	
	public static Operatore getOperatoreBySubject(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_OPERATORE_BY_SUBJECT;
			Object operatore = getOperatoriBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, principal, method, principal);
			return (Operatore) operatore;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}
	
	public static Utenza getUtenza(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_UTENZA;
			Object utenza = getUtenzeBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Utenza) utenza;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}
	
	public static Utenza getUtenza(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_UTENZA;
			Object utenza = getUtenzeBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, principal, method, principal);
			return (Utenza) utenza;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	
	
	public static Stazione getStazione(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_STAZIONE;
			Object stazione = getStazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Stazione) stazione;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static Stazione getStazione(BasicBD basicBD, String codStazione) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_STAZIONE;
			Object stazione = getStazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, codStazione, method, codStazione);
			return (Stazione) stazione;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}


	public static Tributo getTributo(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_TRIBUTO;
			Object tributo = getTributiBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Tributo) tributo;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static Tributo getTributo(BasicBD basicBD, long idDominio, String codTributo) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_TRIBUTO;
			Object tributo = getTributiBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, codTributo + "@" +  String.valueOf(idDominio), method, Long.valueOf(idDominio), codTributo);
			return (Tributo) tributo;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static TipoTributo getTipoTributo(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_TIPO_TRIBUTO;
			Object tipoTributo = getTipiTributoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (TipoTributo) tipoTributo;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw new ServiceException(t);
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static TipoTributo getTipoTributo(BasicBD basicBD, String codTributo) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_TIPO_TRIBUTO;
			Object tributo = getTipiTributoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, codTributo, method, codTributo);
			return (TipoTributo) tributo;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw new ServiceException(t);
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}
	}

	public static void unregister() {
		CacheJMXUtils.unregister();
	}
	
	public static void cleanCache() throws UtilsException {
		dominiBDCacheWrapper.resetCache();
		applicazioniBDCacheWrapper.resetCache();
		uoBDCacheWrapper.resetCache();
		ibanAccreditoBDCacheWrapper.resetCache();
		intermediariBDCacheWrapper.resetCache();
		operatoriBDCacheWrapper.resetCache();
		stazioniBDCacheWrapper.resetCache();
		tributiBDCacheWrapper.resetCache();
		tipiTributoBDCacheWrapper.resetCache();
	}

}
