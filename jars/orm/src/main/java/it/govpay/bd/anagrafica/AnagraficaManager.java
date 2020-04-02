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

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.cache.CacheJMXUtils;

import it.govpay.bd.BasicBD;
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
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Configurazione;
import it.govpay.model.Intermediario;
import it.govpay.model.TipoTributo;
import it.govpay.model.TipoVersamento;
import it.govpay.bd.model.Utenza;

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
	private static final String CACHE_KEY_GET_APPLICAZIONE_BY_SUBJECT = "getApplicazioneBySubject";
	private static final String CACHE_KEY_GET_APPLICAZIONE_BY_PRINCIPAL = "getApplicazioneByPrincipal";
	private static final String CACHE_KEY_GET_APPLICAZIONE = "getApplicazione";
	private static final String CACHE_KEY_GET_TIPO_VERSAMENTO = "getTipoVersamento";
	private static final String CACHE_KEY_GET_TIPO_VERSAMENTO_DOMINIO = "getTipoVersamentoDominio";
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
	
	public static TipiVersamentoBDCacheWrapper getTipiVersamentoBDWrapper(BasicBD bd) {
		return bd.isUseCache() ? tipiVersamentoBDCacheWrapper : tipiVersamentoBDNoCacheWrapper;
	}
	
	public static TipiVersamentoDominiBDCacheWrapper getTipiVersamentoDominiBDCacheWrapper(BasicBD bd) {
		return bd.isUseCache() ? tipiVersamentoDominiBDCacheWrapper : tipiVersamentoDominiBDNoCacheWrapper;
	}
	
	public static ConfigurazioneBDCacheWrapper getConfigurazioneBDCacheWrapper(BasicBD bd) {
		return bd.isUseCache() ? configurazioneBDCacheWrapper : configurazioneBDNoCacheWrapper;
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
	
	public static Dominio getDominio(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_DOMINIO;
			
			Object dominio = getDominiBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static Dominio getDominio(BasicBD basicBD, String codDominio) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_DOMINIO;
			Object dominio = getDominiBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + codDominio, method, codDominio);
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static Applicazione getApplicazione(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_APPLICAZIONE;
			Object applicazione = getApplicazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static Applicazione getApplicazione(BasicBD basicBD, String codApplicazione) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_APPLICAZIONE;
			Object applicazione = getApplicazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + codApplicazione, method, codApplicazione);
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	public static Applicazione getApplicazioneBySubject(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	public static Applicazione getApplicazioneByPrincipal(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}


	public static UnitaOperativa getUnitaOperativa(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_UNITA_OPERATIVA;
			Object uo = getUoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static UnitaOperativa getUnitaOperativa(BasicBD basicBD, long idDominio, String codUo) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_UNITA_OPERATIVA;
			Object uo = getUoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + idDominio + "_" + codUo, method, idDominio, codUo);
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	public static UnitaOperativa getUnitaOperativaByCodUnivocoUo(BasicBD basicBD, long idDominio, String codUnivocoUo) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_UNITA_OPERATIVA_BY_UNIQUE;
			Object uo = getUoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, idDominio + "_" + codUnivocoUo, method, idDominio, codUnivocoUo);
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}


	public static IbanAccredito getIbanAccredito(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_IBAN_ACCREDITO;
			Object ibanAccredito = getIbanAccreditoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static IbanAccredito getIbanAccredito(BasicBD basicBD, Long idDominio, String codIbanAccredito) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_IBAN_ACCREDITO;
			Object ibanAccredito = getIbanAccreditoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + idDominio + "_" + codIbanAccredito, method, idDominio, codIbanAccredito);
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}


	public static Intermediario getIntermediario(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_INTERMEDIARIO;
			Object intermediario = getIntermediariBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static Intermediario getIntermediario(BasicBD basicBD, String codIntermediario) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_INTERMEDIARIO;
			Object intermediario = getIntermediariBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + codIntermediario, method, codIntermediario);
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static Operatore getOperatore(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_OPERATORE;
			Object operatore = getOperatoriBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	
	public static Operatore getOperatoreByPrincipal(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	public static Operatore getOperatoreBySubject(BasicBD basicBD, String subject) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_OPERATORE_BY_SUBJECT;
			Object operatore = getOperatoriBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, subject, method, subject);
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	public static Utenza getUtenza(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_UTENZA;
			Object utenza = getUtenzeBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	public static Utenza getUtenza(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_UTENZA;
			Object utenza = getUtenzeBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + principal, method, principal);
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	
	public static Utenza getUtenzaBySubject(BasicBD basicBD, String subject) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_UTENZA_BY_SUBJECT;
			Object utenza = getUtenzeBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, subject, method, subject);
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	
	
	public static Stazione getStazione(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_STAZIONE;
			Object stazione = getStazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static Stazione getStazione(BasicBD basicBD, String codStazione) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_STAZIONE;
			Object stazione = getStazioniBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + codStazione, method, codStazione);
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}


	public static Tributo getTributo(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_TRIBUTO;
			Object tributo = getTributiBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static Tributo getTributo(BasicBD basicBD, long idDominio, String codTributo) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_TRIBUTO;
			Object tributo = getTributiBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + idDominio + "_" + codTributo, method, Long.valueOf(idDominio), codTributo);
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static TipoTributo getTipoTributo(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_TIPO_TRIBUTO;
			Object tipoTributo = getTipiTributoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static TipoTributo getTipoTributo(BasicBD basicBD, String codTributo) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_TIPO_TRIBUTO;
			Object tributo = getTipiTributoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + codTributo, method, codTributo);
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	public static TipoVersamento getTipoVersamento(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_TIPO_VERSAMENTO;
			Object tipoVersamento = getTipiVersamentoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static TipoVersamento getTipoVersamento(BasicBD basicBD, String codTipoVersamento) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_TIPO_VERSAMENTO;
			Object tipoVersamento = getTipiVersamentoBDWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + codTipoVersamento, method, codTipoVersamento);
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
		} finally {
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	public static TipoVersamentoDominio getTipoVersamentoDominio(BasicBD basicBD, long id) throws ServiceException, NotFoundException  {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_TIPO_VERSAMENTO_DOMINIO;
			Object tipoVersamentoDominio = getTipiVersamentoDominiBDCacheWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefID + id, method, Long.valueOf(id));
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}

	public static TipoVersamentoDominio getTipoVersamentoDominio(BasicBD basicBD, long idDominio, String codTipoVersamento) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_TIPO_VERSAMENTO_DOMINIO;
			Object tipoVersamentoDominio = getTipiVersamentoDominiBDCacheWrapper(basicBD).getObjectCache(basicBD, DEBUG, keyPrefCODICE + idDominio + "_" + codTipoVersamento, method, Long.valueOf(idDominio), codTipoVersamento);
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
		}
	}
	
	public static Configurazione getConfigurazione(BasicBD basicBD) throws ServiceException, NotFoundException {
		boolean wasSelectForUpdate = false;
		try {
			wasSelectForUpdate = basicBD.isSelectForUpdate();
			if(wasSelectForUpdate)
				basicBD.disableSelectForUpdate();
			
			String method = CACHE_KEY_GET_CONFIGURAZIONE;
			Object configurazione = getConfigurazioneBDCacheWrapper(basicBD).getObjectCache(basicBD, DEBUG, CACHE_KEY_GET_CONFIGURAZIONE, method);
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
			if(wasSelectForUpdate)
				basicBD.enableSelectForUpdate();
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
