/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.cache.ApplicazioniBDCacheJmx;
import it.govpay.bd.anagrafica.cache.ApplicazioniBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.CanaliBDCacheJmx;
import it.govpay.bd.anagrafica.cache.CanaliBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.DominiBDCacheJmx;
import it.govpay.bd.anagrafica.cache.DominiBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.IbanAccreditoBDCacheJmx;
import it.govpay.bd.anagrafica.cache.IbanAccreditoBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.IntermediariBDCacheJmx;
import it.govpay.bd.anagrafica.cache.IntermediariBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.OperatoriBDCacheJmx;
import it.govpay.bd.anagrafica.cache.OperatoriBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.PortaliBDCacheJmx;
import it.govpay.bd.anagrafica.cache.PortaliBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.PspBDCacheJmx;
import it.govpay.bd.anagrafica.cache.PspBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.StazioniBDCacheJmx;
import it.govpay.bd.anagrafica.cache.StazioniBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.TipiTributoBDCacheJmx;
import it.govpay.bd.anagrafica.cache.TipiTributoBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.TributiBDCacheJmx;
import it.govpay.bd.anagrafica.cache.TributiBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.UoBDCacheJmx;
import it.govpay.bd.anagrafica.cache.UoBDCacheWrapper;
import it.govpay.model.Applicazione;
import it.govpay.model.Canale;
import it.govpay.model.Dominio;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Intermediario;
import it.govpay.model.Operatore;
import it.govpay.model.Portale;
import it.govpay.model.Psp;
import it.govpay.model.Stazione;
import it.govpay.model.TipoTributo;
import it.govpay.model.Tributo;
import it.govpay.model.UnitaOperativa;

import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.cache.CacheJMXUtils;

public class AnagraficaManager {
	private static final String jmxDomain = "it.govpay.cache.anagrafica";
	private static boolean DEBUG = false;
	
	private static DominiBDCacheWrapper dominiBDCacheWrapper;
	private static ApplicazioniBDCacheWrapper applicazioniBDCacheWrapper;
	private static CanaliBDCacheWrapper canaliBDCacheWrapper;
	private static UoBDCacheWrapper uoBDCacheWrapper;
	private static IbanAccreditoBDCacheWrapper ibanAccreditoBDCacheWrapper;
	private static IntermediariBDCacheWrapper intermediariBDCacheWrapper;
	private static OperatoriBDCacheWrapper operatoriBDCacheWrapper;
	private static PortaliBDCacheWrapper portaliBDCacheWrapper;
	private static PspBDCacheWrapper pspBDCacheWrapper;
	private static StazioniBDCacheWrapper stazioniBDCacheWrapper;
	private static TributiBDCacheWrapper tributiBDCacheWrapper;
	private static TipiTributoBDCacheWrapper tipiTributoBDCacheWrapper;

	private AnagraficaManager(boolean enableCaching) throws UtilsException {
		dominiBDCacheWrapper = new DominiBDCacheWrapper(enableCaching, Logger.getLogger(DominiBDCacheWrapper.class));
		applicazioniBDCacheWrapper = new ApplicazioniBDCacheWrapper(enableCaching, Logger.getLogger(ApplicazioniBDCacheWrapper.class));
		canaliBDCacheWrapper = new CanaliBDCacheWrapper(enableCaching, Logger.getLogger(CanaliBDCacheWrapper.class));
		uoBDCacheWrapper = new UoBDCacheWrapper(enableCaching, Logger.getLogger(UoBDCacheWrapper.class));
		ibanAccreditoBDCacheWrapper = new IbanAccreditoBDCacheWrapper(enableCaching, Logger.getLogger(IbanAccreditoBDCacheWrapper.class));
		intermediariBDCacheWrapper = new IntermediariBDCacheWrapper(enableCaching, Logger.getLogger(IntermediariBDCacheWrapper.class));
		operatoriBDCacheWrapper = new OperatoriBDCacheWrapper(enableCaching, Logger.getLogger(OperatoriBDCacheWrapper.class));
		portaliBDCacheWrapper = new PortaliBDCacheWrapper(enableCaching, Logger.getLogger(PortaliBDCacheWrapper.class));
		pspBDCacheWrapper = new PspBDCacheWrapper(enableCaching, Logger.getLogger(PspBDCacheWrapper.class));
		stazioniBDCacheWrapper = new StazioniBDCacheWrapper(enableCaching, Logger.getLogger(StazioniBDCacheWrapper.class));
		tributiBDCacheWrapper = new TributiBDCacheWrapper(enableCaching, Logger.getLogger(TributiBDCacheWrapper.class));
		tipiTributoBDCacheWrapper = new TipiTributoBDCacheWrapper(enableCaching, Logger.getLogger(TipiTributoBDCacheWrapper.class));

		if(enableCaching) {
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new DominiBDCacheJmx(), jmxDomain, "domini");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new ApplicazioniBDCacheJmx(), jmxDomain, "applicazioni");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new CanaliBDCacheJmx(), jmxDomain, "canali");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new UoBDCacheJmx(), jmxDomain, "uo");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new IbanAccreditoBDCacheJmx(), jmxDomain, "ibanAccredito");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new IntermediariBDCacheJmx(), jmxDomain, "intermediari");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new OperatoriBDCacheJmx(), jmxDomain, "operatori");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new PortaliBDCacheJmx(), jmxDomain, "portali");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new PspBDCacheJmx(), jmxDomain, "psp");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new StazioniBDCacheJmx(), jmxDomain, "stazioni");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new TributiBDCacheJmx(), jmxDomain, "tributi");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new TipiTributoBDCacheJmx(), jmxDomain, "tipiTributo");
		}
	}

	public static AnagraficaManager newInstance() throws UtilsException {
		return new AnagraficaManager(true);
	}
	
	public static AnagraficaManager newInstance(boolean enableCaching) throws UtilsException {
		return new AnagraficaManager(enableCaching);
	}

	public static DominiBDCacheWrapper getDominiBDCacheWrapper() {
		return dominiBDCacheWrapper;
	}

	public static ApplicazioniBDCacheWrapper getApplicazioniBDCacheWrapper() {
		return applicazioniBDCacheWrapper;
	}

	public static CanaliBDCacheWrapper getCanaliBDCacheWrapper() {
		return canaliBDCacheWrapper;
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

	public static PortaliBDCacheWrapper getPortaliBDCacheWrapper() {
		return portaliBDCacheWrapper;
	}

	public static PspBDCacheWrapper getPspBDCacheWrapper() {
		return pspBDCacheWrapper;
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
	
	public static void removeFromCache(Applicazione applicazione) {
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache("getApplicazione", String.valueOf(applicazione.getId())));} catch (Exception e) {	}
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache("getApplicazione", applicazione.getCodApplicazione()));} catch (Exception e) {	}
		try {applicazioniBDCacheWrapper.removeObjectCache(applicazioniBDCacheWrapper.getKeyCache("getApplicazioneByPrincipal", applicazione.getPrincipal()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Portale portale) {
		try {portaliBDCacheWrapper.removeObjectCache(portaliBDCacheWrapper.getKeyCache("getPortale", String.valueOf(portale.getId())));} catch (Exception e) {	}
		try {portaliBDCacheWrapper.removeObjectCache(portaliBDCacheWrapper.getKeyCache("getPortale", portale.getCodPortale()));} catch (Exception e) {	}
		try {portaliBDCacheWrapper.removeObjectCache(portaliBDCacheWrapper.getKeyCache("getPortaleByPrincipal", portale.getPrincipal()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Dominio dominio) {
		try {dominiBDCacheWrapper.removeObjectCache(dominiBDCacheWrapper.getKeyCache("getDominio", String.valueOf(dominio.getId())));} catch (Exception e) {	}
		try {dominiBDCacheWrapper.removeObjectCache(dominiBDCacheWrapper.getKeyCache("getDominio", dominio.getCodDominio()));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(UnitaOperativa uo) {
		try {uoBDCacheWrapper.removeObjectCache(uoBDCacheWrapper.getKeyCache("getUnitaOperativa", String.valueOf(uo.getId())));} catch (Exception e) {	}
		try {uoBDCacheWrapper.removeObjectCache(uoBDCacheWrapper.getKeyCache("getUnitaOperativa", String.valueOf(uo.getCodUo() + "@" + uo.getIdDominio())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(IbanAccredito iban) {
		try {ibanAccreditoBDCacheWrapper.removeObjectCache(ibanAccreditoBDCacheWrapper.getKeyCache("getIbanAccredito", String.valueOf(iban.getId())));} catch (Exception e) {	}
		try {ibanAccreditoBDCacheWrapper.removeObjectCache(ibanAccreditoBDCacheWrapper.getKeyCache("getIbanAccredito", String.valueOf(iban.getCodIban())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Intermediario intermediario) {
		try {intermediariBDCacheWrapper.removeObjectCache(intermediariBDCacheWrapper.getKeyCache("getIntermediario", String.valueOf(intermediario.getId())));} catch (Exception e) {	}
		try {intermediariBDCacheWrapper.removeObjectCache(intermediariBDCacheWrapper.getKeyCache("getIntermediario", String.valueOf(intermediario.getCodIntermediario())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Operatore operatore) {
		try {operatoriBDCacheWrapper.removeObjectCache(operatoriBDCacheWrapper.getKeyCache("getOperatore", String.valueOf(operatore.getId())));} catch (Exception e) {	}
		try {operatoriBDCacheWrapper.removeObjectCache(operatoriBDCacheWrapper.getKeyCache("getOperatore", String.valueOf(operatore.getPrincipal())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Stazione stazione) {
		try {stazioniBDCacheWrapper.removeObjectCache(stazioniBDCacheWrapper.getKeyCache("getStazione", String.valueOf(stazione.getId())));} catch (Exception e) {	}
		try {stazioniBDCacheWrapper.removeObjectCache(stazioniBDCacheWrapper.getKeyCache("getStazione", String.valueOf(stazione.getCodStazione())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Tributo tributo) {
		try {tributiBDCacheWrapper.removeObjectCache(tributiBDCacheWrapper.getKeyCache("getTributo", String.valueOf(tributo.getId())));} catch (Exception e) {	}
		try {tributiBDCacheWrapper.removeObjectCache(tributiBDCacheWrapper.getKeyCache("getTributo", String.valueOf(tributo.getCodTributo() + "@" + tributo.getIdDominio())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(TipoTributo tipoTributo) {
		try {tipiTributoBDCacheWrapper.removeObjectCache(tipiTributoBDCacheWrapper.getKeyCache("getTipoTributo", String.valueOf(tipoTributo.getId())));} catch (Exception e) {	}
		try {tipiTributoBDCacheWrapper.removeObjectCache(tipiTributoBDCacheWrapper.getKeyCache("getTipoTributo", String.valueOf(tipoTributo.getCodTributo())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Psp psp) {
		try {pspBDCacheWrapper.removeObjectCache(pspBDCacheWrapper.getKeyCache("getPsp", String.valueOf(psp.getId())));} catch (Exception e) {	}
		try {pspBDCacheWrapper.removeObjectCache(pspBDCacheWrapper.getKeyCache("getPsp", String.valueOf(psp.getCodPsp())));} catch (Exception e) {	}
	}
	
	public static void removeFromCache(Canale canale) {
		try {canaliBDCacheWrapper.removeObjectCache(canaliBDCacheWrapper.getKeyCache("getCanale", String.valueOf(canale.getId())));} catch (Exception e) {	}
	}

	public static Dominio getDominio(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = "getDominio";
			Object dominio = dominiBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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
			String method = "getDominio";
			Object dominio = dominiBDCacheWrapper.getObjectCache(basicBD, DEBUG, codDominio, method, codDominio);
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

	public static Canale getCanale(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = "getCanale";
			Object canale = canaliBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Canale) canale;
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
	
	public static Canale getCanale(BasicBD basicBD, String codPsp, String codCanale, it.govpay.model.Canale.TipoVersamento tipoVersamento) throws ServiceException, NotFoundException {
		try {
			String method = "getCanale";
			Object canale = canaliBDCacheWrapper.getObjectCache(basicBD, DEBUG, codPsp + "@" + codCanale + "@" + tipoVersamento.getCodifica(), method, codPsp, codCanale, tipoVersamento);
			return (Canale) canale;
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
			String method = "getApplicazione";
			Object applicazione = applicazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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
			String method = "getApplicazione";
			Object applicazione = applicazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, codApplicazione, method, codApplicazione);
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
			String method = "getApplicazioneByPrincipal";
			Object applicazione = applicazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, principal, method, principal);
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
			String method = "getUnitaOperativa";
			Object uo = uoBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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
			String method = "getUnitaOperativa";
			Object uo = uoBDCacheWrapper.getObjectCache(basicBD, DEBUG, codUo + "@" + idDominio, method, idDominio, codUo);
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
			String method = "getIbanAccredito";
			Object ibanAccredito = ibanAccreditoBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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
			String method = "getIbanAccredito";
			Object ibanAccredito = ibanAccreditoBDCacheWrapper.getObjectCache(basicBD, DEBUG, codIbanAccredito + "@" + idDominio, method, idDominio, codIbanAccredito);
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
			String method = "getIntermediario";
			Object intermediario = intermediariBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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
			String method = "getIntermediario";
			Object intermediario = intermediariBDCacheWrapper.getObjectCache(basicBD, DEBUG, codIntermediario, method, codIntermediario);
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
			String method = "getOperatore";
			Object operatore = operatoriBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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

	public static Operatore getOperatore(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		try {
			String method = "getOperatore";
			Object operatore = operatoriBDCacheWrapper.getObjectCache(basicBD, DEBUG, principal, method, principal);
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


	public static Portale getPortale(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = "getPortale";
			Object portale = portaliBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Portale) portale;
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

	public static Portale getPortale(BasicBD basicBD, String codPortale) throws ServiceException, NotFoundException {
		try {
			String method = "getPortale";
			Object portale = portaliBDCacheWrapper.getObjectCache(basicBD, DEBUG, codPortale, method, codPortale);
			return (Portale) portale;
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
	
	public static Portale getPortaleByPrincipal(BasicBD basicBD, String principal) throws ServiceException, NotFoundException {
		try {
			String method = "getPortaleByPrincipal";
			Object portale = portaliBDCacheWrapper.getObjectCache(basicBD, DEBUG, principal, method, principal);
			return (Portale) portale;
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

	public static Psp getPsp(BasicBD basicBD, long id) throws ServiceException {
		try {
			String method = "getPsp";
			Object psp = pspBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Psp) psp;
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

	public static Psp getPsp(BasicBD basicBD, String codPsp) throws ServiceException, NotFoundException {
		try {
			String method = "getPsp";
			Object psp = pspBDCacheWrapper.getObjectCache(basicBD, DEBUG, codPsp, method, codPsp);
			return (Psp) psp;
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
			String method = "getStazione";
			Object stazione = stazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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
			String method = "getStazione";
			Object stazione = stazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, codStazione, method, codStazione);
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
			String method = "getTributo";
			Object tributo = tributiBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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
			String method = "getTributo";
			Object tributo = tributiBDCacheWrapper.getObjectCache(basicBD, DEBUG, codTributo + "@" +  String.valueOf(idDominio), method, Long.valueOf(idDominio), codTributo);
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
			String method = "getTipoTributo";
			Object tipoTributo = tipiTributoBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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
			String method = "getTipoTributo";
			Object tributo = tipiTributoBDCacheWrapper.getObjectCache(basicBD, DEBUG, codTributo, method, codTributo);
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
		portaliBDCacheWrapper.resetCache();
		pspBDCacheWrapper.resetCache();
		stazioniBDCacheWrapper.resetCache();
		tributiBDCacheWrapper.resetCache();
		tipiTributoBDCacheWrapper.resetCache();
		canaliBDCacheWrapper.resetCache();
	}
}
