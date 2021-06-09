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

import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.cache.CacheJMXUtils;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.cache.ApplicazioniBDCacheJmx;
import it.govpay.bd.anagrafica.cache.ApplicazioniBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.ConfigurazioneBDCacheJmx;
import it.govpay.bd.anagrafica.cache.ConfigurazioneBDCacheWrapper;
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
import it.govpay.bd.anagrafica.cache.TipiVersamentoBDCacheJmx;
import it.govpay.bd.anagrafica.cache.TipiVersamentoBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.TipiVersamentoDominiBDCacheJmx;
import it.govpay.bd.anagrafica.cache.TipiVersamentoDominiBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.TributiBDCacheJmx;
import it.govpay.bd.anagrafica.cache.TributiBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.UoBDCacheJmx;
import it.govpay.bd.anagrafica.cache.UoBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.UtenzeBDCacheJmx;
import it.govpay.bd.anagrafica.cache.UtenzeBDCacheWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Configurazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Utenza;
import it.govpay.model.Intermediario;
import it.govpay.model.TipoTributo;
import it.govpay.model.TipoVersamento;

public class AnagraficaManager {
	
	private static Date dataReset;
	
	private static final String CACHE_KEY_GET_TIPO_TRIBUTO = "getTipoTributo";
	private static final String CACHE_KEY_GET_TRIBUTO = "getTributo";
	private static final String CACHE_KEY_GET_STAZIONE = "getStazione";
	private static final String CACHE_KEY_GET_UTENZA = "getUtenza";
	private static final String CACHE_KEY_GET_UTENZA_BY_SUBJECT = "getUtenzaBySubject";
	private static final String CACHE_KEY_GET_OPERATORE_BY_SUBJECT = "getOperatoreBySubject";
	private static final String CACHE_KEY_GET_OPERATORE_BY_PRINCIPAL = "getOperatoreByPrincipal";
	private static final String CACHE_KEY_GET_OPERATORE = "getOperatore";
	private static final String CACHE_KEY_GET_INTERMEDIARIO = "getIntermediario";
	private static final String CACHE_KEY_GET_IBAN_ACCREDITO = "getIbanAccredito";
	private static final String CACHE_KEY_GET_UNITA_OPERATIVA = "getUnitaOperativa";
	private static final String CACHE_KEY_GET_UNITA_OPERATIVA_BY_UNIQUE = "getUnitaOperativaByCodUnivocoUo";
	private static final String CACHE_KEY_GET_DOMINIO = "getDominio";
	private static final String CACHE_KEY_GET_COD_DOMINI = "getCodDomini";
	private static final String CACHE_KEY_GET_APPLICAZIONE_BY_SUBJECT = "getApplicazioneBySubject";
	private static final String CACHE_KEY_GET_APPLICAZIONE_BY_PRINCIPAL = "getApplicazioneByPrincipal";
	private static final String CACHE_KEY_GET_APPLICAZIONE = "getApplicazione";
	private static final String CACHE_KEY_GET_COD_APPLICAZIONI = "getCodApplicazioni";
	private static final String CACHE_KEY_GET_TIPO_VERSAMENTO = "getTipoVersamento";
	private static final String CACHE_KEY_GET_TIPO_VERSAMENTO_DOMINIO = "getTipoVersamentoDominio";
	private static final String CACHE_KEY_GET_TIPI_VERSAMENTO_DOMINIO_PORTALE_PAGAMENTO_FORM = "getTipiVersamentoDominioPortalePagamentoForm";
	private static final String CACHE_KEY_GET_CONFIGURAZIONE = "getConfigurazione";
	
	private static final String keyPrefID = "ID";
	private static final String keyPrefCODICE = "CODICE";
	
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
	private static TipiVersamentoBDCacheWrapper tipiVersamentoBDCacheWrapper, tipiVersamentoBDNoCacheWrapper;
	private static TipiVersamentoDominiBDCacheWrapper tipiVersamentoDominiBDCacheWrapper, tipiVersamentoDominiBDNoCacheWrapper;
	private static ConfigurazioneBDCacheWrapper configurazioneBDCacheWrapper, configurazioneBDNoCacheWrapper;

	private AnagraficaManager(String domain) throws UtilsException {
		
		dataReset = new Date();
		
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
		tipiVersamentoBDCacheWrapper = new TipiVersamentoBDCacheWrapper(true, LoggerWrapperFactory.getLogger(TipiVersamentoBDCacheWrapper.class));
		tipiVersamentoDominiBDCacheWrapper = new TipiVersamentoDominiBDCacheWrapper(true, LoggerWrapperFactory.getLogger(TipiVersamentoDominiBDCacheWrapper.class));
		configurazioneBDCacheWrapper = new ConfigurazioneBDCacheWrapper(true, LoggerWrapperFactory.getLogger(ConfigurazioneBDCacheWrapper.class));
		
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
		tipiVersamentoBDNoCacheWrapper = new TipiVersamentoBDCacheWrapper(false, LoggerWrapperFactory.getLogger(TipiVersamentoBDCacheWrapper.class));
		tipiVersamentoDominiBDNoCacheWrapper = new TipiVersamentoDominiBDCacheWrapper(false, LoggerWrapperFactory.getLogger(TipiVersamentoDominiBDCacheWrapper.class));
		configurazioneBDNoCacheWrapper = new ConfigurazioneBDCacheWrapper(false, LoggerWrapperFactory.getLogger(ConfigurazioneBDCacheWrapper.class));

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
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new TipiVersamentoBDCacheJmx(), domain, "tipiVersamento");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new TipiVersamentoDominiBDCacheJmx(), domain, "tipiVersamentoDomini");
		CacheJMXUtils.register(LoggerWrapperFactory.getLogger(AnagraficaManager.class), new ConfigurazioneBDCacheJmx(), domain, "configurazione");
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
	
	public static TipiVersamentoBDCacheWrapper getTipiVersamentoBDCacheWrapper() {
		return tipiVersamentoBDCacheWrapper;
	}
	
	public static TipiVersamentoDominiBDCacheWrapper getTipiVersamentoDominiBDCacheWrapper() {
		return tipiVersamentoDominiBDCacheWrapper;
	}
	
	public static ConfigurazioneBDCacheWrapper getConfigurazioneBDCacheWrapper() {
		return configurazioneBDCacheWrapper;
	}
	
	public static DominiBDCacheWrapper getDominiBDWrapper(boolean isUseCache) {
		return isUseCache ? dominiBDCacheWrapper : dominiBDNoCacheWrapper;
	}

	public static ApplicazioniBDCacheWrapper getApplicazioniBDWrapper(boolean isUseCache) {
		return isUseCache ? applicazioniBDCacheWrapper : applicazioniBDNoCacheWrapper;
	}

	public static UoBDCacheWrapper getUoBDWrapper(boolean isUseCache) {
		return isUseCache ? uoBDCacheWrapper : uoBDNoCacheWrapper;
	}

	public static IbanAccreditoBDCacheWrapper getIbanAccreditoBDWrapper(boolean isUseCache) {
		return isUseCache ? ibanAccreditoBDCacheWrapper : ibanAccreditoBDNoCacheWrapper;
	}

	public static IntermediariBDCacheWrapper getIntermediariBDWrapper(boolean isUseCache) {
		return isUseCache ? intermediariBDCacheWrapper : intermediariBDNoCacheWrapper;
	}

	public static OperatoriBDCacheWrapper getOperatoriBDWrapper(boolean isUseCache) {
		return isUseCache ? operatoriBDCacheWrapper : operatoriBDNoCacheWrapper;
	}
	
	public static UtenzeBDCacheWrapper getUtenzeBDWrapper(boolean isUseCache) {
		return isUseCache ? utenzeBDCacheWrapper : utenzeBDNoCacheWrapper;
	}

	public static StazioniBDCacheWrapper getStazioniBDWrapper(boolean isUseCache) {
		return isUseCache ? stazioniBDCacheWrapper : stazioniBDNoCacheWrapper;
	}

	public static TributiBDCacheWrapper getTributiBDWrapper(boolean isUseCache) {
		return isUseCache ? tributiBDCacheWrapper : tributiBDNoCacheWrapper;
	}
	
	public static TipiTributoBDCacheWrapper getTipiTributoBDWrapper(boolean isUseCache) {
		return isUseCache ? tipiTributoBDCacheWrapper : tipiTributoBDNoCacheWrapper;
	}
	
	public static TipiVersamentoBDCacheWrapper getTipiVersamentoBDWrapper(boolean isUseCache) {
		return isUseCache ? tipiVersamentoBDCacheWrapper : tipiVersamentoBDNoCacheWrapper;
	}
	
	public static TipiVersamentoDominiBDCacheWrapper getTipiVersamentoDominiBDCacheWrapper(boolean isUseCache) {
		return isUseCache ? tipiVersamentoDominiBDCacheWrapper : tipiVersamentoDominiBDNoCacheWrapper;
	}
	
	public static ConfigurazioneBDCacheWrapper getConfigurazioneBDCacheWrapper(boolean isUseCache) {
		return isUseCache ? configurazioneBDCacheWrapper : configurazioneBDNoCacheWrapper;
	}
	
	public static void removeFromCache(Applicazione applicazione) {
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_APPLICAZIONE, keyPrefID + applicazione.getId()));} catch (Exception e) {	}
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_APPLICAZIONE, keyPrefCODICE + applicazione.getCodApplicazione()));} catch (Exception e) {	}
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_APPLICAZIONE_BY_PRINCIPAL, applicazione.getPrincipal()));} catch (Exception e) {	}
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_APPLICAZIONE_BY_SUBJECT, applicazione.getPrincipal()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Dominio dominio) {
		try {dominiBDCacheWrapper.removeObjectCache(dominiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_DOMINIO, keyPrefID + dominio.getId()));} catch (Exception e) {	}
		try {dominiBDCacheWrapper.removeObjectCache(dominiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_DOMINIO, keyPrefCODICE + dominio.getCodDominio()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(UnitaOperativa uo) {
		try {uoBDCacheWrapper.removeObjectCache(uoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UNITA_OPERATIVA, keyPrefID + uo.getId()));} catch (Exception e) {	}
		try {uoBDCacheWrapper.removeObjectCache(uoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UNITA_OPERATIVA, keyPrefCODICE + uo.getIdDominio() + "_" + uo.getCodUo()));} catch (Exception e) {	}
		try {uoBDCacheWrapper.removeObjectCache(uoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UNITA_OPERATIVA_BY_UNIQUE, uo.getIdDominio() + "_" + uo.getAnagrafica().getCodUnivoco()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(IbanAccredito iban) {
		try {ibanAccreditoBDCacheWrapper.removeObjectCache(ibanAccreditoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_IBAN_ACCREDITO, keyPrefID + iban.getId()));} catch (Exception e) {	}
		try {ibanAccreditoBDCacheWrapper.removeObjectCache(ibanAccreditoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_IBAN_ACCREDITO, keyPrefCODICE + iban.getCodIban()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Intermediario intermediario) {
		try {intermediariBDCacheWrapper.removeObjectCache(intermediariBDCacheWrapper.getKeyCache(CACHE_KEY_GET_INTERMEDIARIO, keyPrefID + intermediario.getId()));} catch (Exception e) {	}
		try {intermediariBDCacheWrapper.removeObjectCache(intermediariBDCacheWrapper.getKeyCache(CACHE_KEY_GET_INTERMEDIARIO, keyPrefCODICE + intermediario.getCodIntermediario()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Operatore operatore) {
		try {operatoriBDCacheWrapper.removeObjectCache(operatoriBDCacheWrapper.getKeyCache(CACHE_KEY_GET_OPERATORE, String.valueOf(operatore.getId())));} catch (Exception e) {	}
		try {operatoriBDCacheWrapper.removeObjectCache(operatoriBDCacheWrapper.getKeyCache(CACHE_KEY_GET_OPERATORE_BY_PRINCIPAL, operatore.getUtenza().getPrincipal()));} catch (Exception e) {	}
		try {operatoriBDCacheWrapper.removeObjectCache(operatoriBDCacheWrapper.getKeyCache(CACHE_KEY_GET_OPERATORE_BY_SUBJECT, operatore.getUtenza().getPrincipal()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Utenza utenza) {
		try {utenzeBDCacheWrapper.removeObjectCache(utenzeBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UTENZA, keyPrefID + utenza.getId()));} catch (Exception e) {	}
		try {utenzeBDCacheWrapper.removeObjectCache(utenzeBDCacheWrapper.getKeyCache(CACHE_KEY_GET_UTENZA, keyPrefCODICE + utenza.getPrincipal()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Stazione stazione) {
		try {stazioniBDCacheWrapper.removeObjectCache(stazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_STAZIONE, keyPrefID + stazione.getId()));} catch (Exception e) {	}
		try {stazioniBDCacheWrapper.removeObjectCache(stazioniBDCacheWrapper.getKeyCache(CACHE_KEY_GET_STAZIONE, keyPrefCODICE + stazione.getCodStazione()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Tributo tributo) {
		try {tributiBDCacheWrapper.removeObjectCache(tributiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TRIBUTO, keyPrefID + tributo.getId()));} catch (Exception e) {	}
		try {tributiBDCacheWrapper.removeObjectCache(tributiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TRIBUTO, keyPrefCODICE + tributo.getIdDominio() + "_" + tributo.getCodTributo() ));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(TipoTributo tipoTributo) {
		try {tipiTributoBDCacheWrapper.removeObjectCache(tipiTributoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TIPO_TRIBUTO, keyPrefID + tipoTributo.getId()));} catch (Exception e) {	}
		try {tipiTributoBDCacheWrapper.removeObjectCache(tipiTributoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TIPO_TRIBUTO, keyPrefCODICE + tipoTributo.getCodTributo()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(TipoVersamento tipoVersamento) {
		try {tipiVersamentoBDCacheWrapper.removeObjectCache(tipiVersamentoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TIPO_VERSAMENTO, keyPrefID + tipoVersamento.getId()));} catch (Exception e) {	}
		try {tipiVersamentoBDCacheWrapper.removeObjectCache(tipiVersamentoBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TIPO_VERSAMENTO, keyPrefCODICE + tipoVersamento.getCodTipoVersamento()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(TipoVersamentoDominio tipoVersamentoDominio) {
		try {tipiVersamentoBDCacheWrapper.removeObjectCache(tipiVersamentoDominiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TIPO_VERSAMENTO_DOMINIO, keyPrefID + tipoVersamentoDominio.getId()));} catch (Exception e) {	}
		try {tipiVersamentoBDCacheWrapper.removeObjectCache(tipiVersamentoDominiBDCacheWrapper.getKeyCache(CACHE_KEY_GET_TIPO_VERSAMENTO_DOMINIO, keyPrefCODICE + tipoVersamentoDominio.getIdDominio() + "_" + tipoVersamentoDominio.getCodTipoVersamento()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Configurazione configurazione) {
		try {configurazioneBDCacheWrapper.removeObjectCache(configurazioneBDCacheWrapper.getKeyCache(CACHE_KEY_GET_CONFIGURAZIONE, CACHE_KEY_GET_CONFIGURAZIONE));} catch (Exception e) {	}
	}
	
	public static Dominio getDominio(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_DOMINIO;
			
			Object dominio = getDominiBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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

	public static Dominio getDominio(BDConfigWrapper configWrapper, String codDominio) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_DOMINIO;
			Object dominio = getDominiBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + codDominio, method, codDominio);
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
	
	@SuppressWarnings("unchecked")
	public static List<String> getListaCodDomini(BDConfigWrapper configWrapper) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_COD_DOMINI;
			Object codiciDomini = getDominiBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, CACHE_KEY_GET_COD_DOMINI, method);
			return (List<String>) codiciDomini;
		} catch (Throwable t) {
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}  
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getListaCodApplicazioni(BDConfigWrapper configWrapper) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_COD_APPLICAZIONI;
			Object codiciApplicazione = getApplicazioniBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, CACHE_KEY_GET_COD_APPLICAZIONI, method);
			return (List<String>) codiciApplicazione;
		} catch (Throwable t) {
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		}  
	}

	public static Applicazione getApplicazione(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_APPLICAZIONE;
			Object applicazione = getApplicazioniBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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

	public static Applicazione getApplicazione(BDConfigWrapper configWrapper, String codApplicazione) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_APPLICAZIONE;
			Object applicazione = getApplicazioniBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + codApplicazione, method, codApplicazione);
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
	
	public static Applicazione getApplicazioneBySubject(BDConfigWrapper configWrapper, String principal) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_APPLICAZIONE_BY_SUBJECT;
			Object applicazione = getApplicazioniBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, principal, method, principal);
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
	
	public static Applicazione getApplicazioneByPrincipal(BDConfigWrapper configWrapper, String principal) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_APPLICAZIONE_BY_PRINCIPAL;
			Object applicazione = getApplicazioniBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, principal, method, principal);
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


	public static UnitaOperativa getUnitaOperativa(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_UNITA_OPERATIVA;
			Object uo = getUoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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

	public static UnitaOperativa getUnitaOperativa(BDConfigWrapper configWrapper, long idDominio, String codUo) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_UNITA_OPERATIVA;
			Object uo = getUoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + idDominio + "_" + codUo, method, idDominio, codUo);
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
	
	public static UnitaOperativa getUnitaOperativaByCodUnivocoUo(BDConfigWrapper configWrapper, long idDominio, String codUnivocoUo) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_UNITA_OPERATIVA_BY_UNIQUE;
			Object uo = getUoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, idDominio + "_" + codUnivocoUo, method, idDominio, codUnivocoUo);
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


	public static IbanAccredito getIbanAccredito(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_IBAN_ACCREDITO;
			Object ibanAccredito = getIbanAccreditoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
		}
	}

	public static IbanAccredito getIbanAccredito(BDConfigWrapper configWrapper, Long idDominio, String codIbanAccredito) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_IBAN_ACCREDITO;
			Object ibanAccredito = getIbanAccreditoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + idDominio + "_" + codIbanAccredito, method, idDominio, codIbanAccredito);
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
		} finally {
		}
	}
	
	public static IbanAccredito getIbanAccredito(BDConfigWrapper configWrapper, String codIbanAccredito) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = CACHE_KEY_GET_IBAN_ACCREDITO;
			Object ibanAccredito = getIbanAccreditoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + codIbanAccredito, method, codIbanAccredito);
			return (IbanAccredito) ibanAccredito;
		} catch (Throwable t) {
			if(t instanceof NotFoundException) {
				throw (NotFoundException) t;
			}
			if(t instanceof MultipleResultException) {
				throw (MultipleResultException) t;
			}
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		} finally {
		}
	}


	public static Intermediario getIntermediario(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_INTERMEDIARIO;
			Object intermediario = getIntermediariBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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

	public static Intermediario getIntermediario(BDConfigWrapper configWrapper, String codIntermediario) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_INTERMEDIARIO;
			Object intermediario = getIntermediariBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + codIntermediario, method, codIntermediario);
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

	public static Operatore getOperatore(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_OPERATORE;
			Object operatore = getOperatoriBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
		}
	}
	
	
	public static Operatore getOperatoreByPrincipal(BDConfigWrapper configWrapper, String principal) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_OPERATORE_BY_PRINCIPAL;
			Object operatore = getOperatoriBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, principal, method, principal);
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
		} finally {
		}
	}
	
	public static Operatore getOperatoreBySubject(BDConfigWrapper configWrapper, String subject) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_OPERATORE_BY_SUBJECT;
			Object operatore = getOperatoriBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, subject, method, subject);
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
		} finally {
		}
	}
	
	public static Utenza getUtenza(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_UTENZA;
			Object utenza = getUtenzeBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
	
	public static Utenza getUtenza(BDConfigWrapper configWrapper, String principal) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_UTENZA;
			Object utenza = getUtenzeBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + principal, method, principal);
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
	
	
	public static Utenza getUtenzaBySubject(BDConfigWrapper configWrapper, String subject) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_UTENZA_BY_SUBJECT;
			Object utenza = getUtenzeBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, subject, method, subject);
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

	
	
	public static Stazione getStazione(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_STAZIONE;
			Object stazione = getStazioniBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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

	public static Stazione getStazione(BDConfigWrapper configWrapper, String codStazione) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_STAZIONE;
			Object stazione = getStazioniBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + codStazione, method, codStazione);
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


	public static Tributo getTributo(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_TRIBUTO;
			Object tributo = getTributiBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
		}
	}

	public static Tributo getTributo(BDConfigWrapper configWrapper, long idDominio, String codTributo) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_TRIBUTO;
			Object tributo = getTributiBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + idDominio + "_" + codTributo, method, Long.valueOf(idDominio), codTributo);
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
		} finally {
		}
	}

	public static TipoTributo getTipoTributo(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_TIPO_TRIBUTO;
			Object tipoTributo = getTipiTributoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
			return (TipoTributo) tipoTributo;
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
		} finally {
		}
	}

	public static TipoTributo getTipoTributo(BDConfigWrapper configWrapper, String codTributo) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_TIPO_TRIBUTO;
			Object tributo = getTipiTributoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + codTributo, method, codTributo);
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
		} finally {
		}
	}
	
	public static TipoVersamento getTipoVersamento(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_TIPO_VERSAMENTO;
			Object tipoVersamento = getTipiVersamentoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
			return (TipoVersamento) tipoVersamento;
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

	public static TipoVersamento getTipoVersamento(BDConfigWrapper configWrapper, String codTipoVersamento) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_TIPO_VERSAMENTO;
			Object tipoVersamento = getTipiVersamentoBDWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + codTipoVersamento, method, codTipoVersamento);
			return (TipoVersamento) tipoVersamento;
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
	
	public static TipoVersamentoDominio getTipoVersamentoDominio(BDConfigWrapper configWrapper, long id) throws ServiceException, NotFoundException  {
		try {
			String method = CACHE_KEY_GET_TIPO_VERSAMENTO_DOMINIO;
			Object tipoVersamentoDominio = getTipiVersamentoDominiBDCacheWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefID + id, method, Long.valueOf(id));
			return (TipoVersamentoDominio) tipoVersamentoDominio;
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
		} finally {
		}
	}

	public static TipoVersamentoDominio getTipoVersamentoDominio(BDConfigWrapper configWrapper, long idDominio, String codTipoVersamento) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_TIPO_VERSAMENTO_DOMINIO;
			Object tipoVersamentoDominio = getTipiVersamentoDominiBDCacheWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + idDominio + "_" + codTipoVersamento, method, Long.valueOf(idDominio), codTipoVersamento);
			return (TipoVersamentoDominio) tipoVersamentoDominio;
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
		} finally {
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<TipoVersamentoDominio> getListaTipiVersamentoDominioConPagamentiPortaleForm(BDConfigWrapper configWrapper, long idDominio) throws ServiceException {
		try {
			String method = CACHE_KEY_GET_TIPI_VERSAMENTO_DOMINIO_PORTALE_PAGAMENTO_FORM;
			Object tipiVersamentoDominio = getTipiVersamentoDominiBDCacheWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, keyPrefCODICE + idDominio + "_tv_pagamentoPortaleForm", method, Long.valueOf(idDominio));
			return (List<TipoVersamentoDominio>) tipiVersamentoDominio;
		} catch (Throwable t) {
			if(t instanceof ServiceException) {
				throw (ServiceException) t;
			}
			throw new ServiceException(t);
		} finally {
		}
	}
	
	public static Configurazione getConfigurazione(BDConfigWrapper configWrapper) throws ServiceException, NotFoundException {
		try {
			String method = CACHE_KEY_GET_CONFIGURAZIONE;
			Object configurazione = getConfigurazioneBDCacheWrapper(configWrapper.isUseCache()).getObjectCache(configWrapper, DEBUG, CACHE_KEY_GET_CONFIGURAZIONE, method);
			return (Configurazione) configurazione;
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
		} finally {
		}
	}


	public static void unregister() {
		CacheJMXUtils.unregister();
	}
	
	public static void cleanCache() throws UtilsException {
		aggiornaDataReset(new Date());
		dominiBDCacheWrapper.resetCache();
		applicazioniBDCacheWrapper.resetCache();
		uoBDCacheWrapper.resetCache();
		ibanAccreditoBDCacheWrapper.resetCache();
		intermediariBDCacheWrapper.resetCache();
		operatoriBDCacheWrapper.resetCache();
		utenzeBDCacheWrapper.resetCache();
		stazioniBDCacheWrapper.resetCache();
		tributiBDCacheWrapper.resetCache();
		tipiTributoBDCacheWrapper.resetCache();
		tipiVersamentoBDCacheWrapper.resetCache();
		tipiVersamentoDominiBDCacheWrapper.resetCache();
		configurazioneBDCacheWrapper.resetCache();
	}

	public static void aggiornaDataReset(Date nuovaData) {
		AnagraficaManager.dataReset = nuovaData;
	}
	
	public static Date getDataReset() {
		return AnagraficaManager.dataReset;
	}
}
