/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
import it.govpay.bd.anagrafica.cache.EntiBDCacheJmx;
import it.govpay.bd.anagrafica.cache.EntiBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.IbanAccreditoBDCacheJmx;
import it.govpay.bd.anagrafica.cache.IbanAccreditoBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.IntermediariBDCacheJmx;
import it.govpay.bd.anagrafica.cache.IntermediariBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.MailTemplateBDCacheJmx;
import it.govpay.bd.anagrafica.cache.MailTemplateBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.OperatoriBDCacheJmx;
import it.govpay.bd.anagrafica.cache.OperatoriBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.PortaliBDCacheJmx;
import it.govpay.bd.anagrafica.cache.PortaliBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.PspBDCacheJmx;
import it.govpay.bd.anagrafica.cache.PspBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.StazioniBDCacheJmx;
import it.govpay.bd.anagrafica.cache.StazioniBDCacheWrapper;
import it.govpay.bd.anagrafica.cache.TributiBDCacheJmx;
import it.govpay.bd.anagrafica.cache.TributiBDCacheWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.MailTemplate;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Tributo;

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
	private static EntiBDCacheWrapper entiBDCacheWrapper;
	private static IbanAccreditoBDCacheWrapper ibanAccreditoBDCacheWrapper;
	private static IntermediariBDCacheWrapper intermediariBDCacheWrapper;
	private static MailTemplateBDCacheWrapper mailTemplateBDCacheWrapper;
	private static OperatoriBDCacheWrapper operatoriBDCacheWrapper;
	private static PortaliBDCacheWrapper portaliBDCacheWrapper;
	private static PspBDCacheWrapper pspBDCacheWrapper;
	private static StazioniBDCacheWrapper stazioniBDCacheWrapper;
	private static TributiBDCacheWrapper tributiBDCacheWrapper;

	private AnagraficaManager(boolean enableCaching) throws UtilsException {
		dominiBDCacheWrapper = new DominiBDCacheWrapper(enableCaching, Logger.getLogger(DominiBDCacheWrapper.class));
		applicazioniBDCacheWrapper = new ApplicazioniBDCacheWrapper(enableCaching, Logger.getLogger(ApplicazioniBDCacheWrapper.class));
		canaliBDCacheWrapper = new CanaliBDCacheWrapper(enableCaching, Logger.getLogger(CanaliBDCacheWrapper.class));
		entiBDCacheWrapper = new EntiBDCacheWrapper(enableCaching, Logger.getLogger(EntiBDCacheWrapper.class));
		ibanAccreditoBDCacheWrapper = new IbanAccreditoBDCacheWrapper(enableCaching, Logger.getLogger(IbanAccreditoBDCacheWrapper.class));
		intermediariBDCacheWrapper = new IntermediariBDCacheWrapper(enableCaching, Logger.getLogger(IntermediariBDCacheWrapper.class));
		mailTemplateBDCacheWrapper = new MailTemplateBDCacheWrapper(enableCaching, Logger.getLogger(MailTemplateBDCacheWrapper.class));
		operatoriBDCacheWrapper = new OperatoriBDCacheWrapper(enableCaching, Logger.getLogger(OperatoriBDCacheWrapper.class));
		portaliBDCacheWrapper = new PortaliBDCacheWrapper(enableCaching, Logger.getLogger(PortaliBDCacheWrapper.class));
		pspBDCacheWrapper = new PspBDCacheWrapper(enableCaching, Logger.getLogger(PspBDCacheWrapper.class));
		stazioniBDCacheWrapper = new StazioniBDCacheWrapper(enableCaching, Logger.getLogger(StazioniBDCacheWrapper.class));
		tributiBDCacheWrapper = new TributiBDCacheWrapper(enableCaching, Logger.getLogger(TributiBDCacheWrapper.class));

		if(enableCaching) {
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new DominiBDCacheJmx(), jmxDomain, "domini");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new ApplicazioniBDCacheJmx(), jmxDomain, "applicazioni");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new CanaliBDCacheJmx(), jmxDomain, "canali");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new EntiBDCacheJmx(), jmxDomain, "enti");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new IbanAccreditoBDCacheJmx(), jmxDomain, "ibanAccredito");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new IntermediariBDCacheJmx(), jmxDomain, "intermediari");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new MailTemplateBDCacheJmx(), jmxDomain, "mailTemplate");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new OperatoriBDCacheJmx(), jmxDomain, "operatori");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new PortaliBDCacheJmx(), jmxDomain, "portali");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new PspBDCacheJmx(), jmxDomain, "psp");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new StazioniBDCacheJmx(), jmxDomain, "stazioni");
			CacheJMXUtils.register(Logger.getLogger(AnagraficaManager.class), new TributiBDCacheJmx(), jmxDomain, "tributi");
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

	public static EntiBDCacheWrapper getEntiBDCacheWrapper() {
		return entiBDCacheWrapper;
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

	public static MailTemplateBDCacheWrapper getMailTemplateBDCacheWrapper() {
		return mailTemplateBDCacheWrapper;
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

	public static Dominio getDominio(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getDominio";
			Object dominio = dominiBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Dominio) dominio;
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
		}
	}

	public static Dominio getDominio(BasicBD basicBD, String codDominio) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getDominio";
			Object dominio = dominiBDCacheWrapper.getObjectCache(basicBD, DEBUG, codDominio, method, codDominio);
			return (Dominio) dominio;
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
		}
	}

	public static Canale getCanale(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getCanale";
			Object canale = canaliBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Canale) canale;
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
		}
	}

	public static Applicazione getApplicazione(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getApplicazione";
			Object applicazione = applicazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Applicazione) applicazione;
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
		}
	}

	public static Applicazione getApplicazione(BasicBD basicBD, String codApplicazione) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getApplicazione";
			Object applicazione = applicazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, codApplicazione, method, codApplicazione);
			return (Applicazione) applicazione;
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
		}
	}
	
	public static Applicazione getApplicazioneByPrincipal(BasicBD basicBD, String principal) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getApplicazioneByPrincipal";
			Object applicazione = applicazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, principal, method, principal);
			return (Applicazione) applicazione;
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
		}
	}


	public static Ente getEnte(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getEnte";
			Object ente = entiBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Ente) ente;
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
		}
	}

	public static Ente getEnte(BasicBD basicBD, String codEnte) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getEnte";
			Object ente = entiBDCacheWrapper.getObjectCache(basicBD, DEBUG, codEnte, method, codEnte);
			return (Ente) ente;
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
		}
	}


	public static IbanAccredito getIbanAccredito(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getIbanAccredito";
			Object ibanAccredito = ibanAccreditoBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
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
		}
	}

	public static IbanAccredito getIbanAccredito(BasicBD basicBD, String codIbanAccredito) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getIbanAccredito";
			Object ibanAccredito = ibanAccreditoBDCacheWrapper.getObjectCache(basicBD, DEBUG, codIbanAccredito, method, codIbanAccredito);
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
		}
	}


	public static Intermediario getIntermediario(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getIntermediario";
			Object intermediario = intermediariBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Intermediario) intermediario;
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
		}
	}

	public static Intermediario getIntermediario(BasicBD basicBD, String codIntermediario) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getIntermediario";
			Object intermediario = intermediariBDCacheWrapper.getObjectCache(basicBD, DEBUG, codIntermediario, method, codIntermediario);
			return (Intermediario) intermediario;
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
		}
	}


	public static MailTemplate getMailTemplate(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getMailTemplate";
			Object mailTemplate = mailTemplateBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (MailTemplate) mailTemplate;
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
		}
	}

	public static Operatore getOperatore(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getOperatore";
			Object operatore = operatoriBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Operatore) operatore;
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
		}
	}

	public static Operatore getOperatore(BasicBD basicBD, String principal) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getOperatore";
			Object operatore = operatoriBDCacheWrapper.getObjectCache(basicBD, DEBUG, principal, method, principal);
			return (Operatore) operatore;
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
		}
	}


	public static Portale getPortale(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getPortale";
			Object portale = portaliBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Portale) portale;
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
		}
	}

	public static Portale getPortale(BasicBD basicBD, String codPortale) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getPortale";
			Object portale = portaliBDCacheWrapper.getObjectCache(basicBD, DEBUG, codPortale, method, codPortale);
			return (Portale) portale;
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
		}
	}
	
	public static Portale getPortaleByPrincipal(BasicBD basicBD, String principal) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getPortaleByPrincipal";
			Object portale = portaliBDCacheWrapper.getObjectCache(basicBD, DEBUG, principal, method, principal);
			return (Portale) portale;
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
		}
	}

	public static Psp getPsp(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getPsp";
			Object psp = pspBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Psp) psp;
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
		}
	}

	public static Psp getPsp(BasicBD basicBD, String codPsp) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getPsp";
			Object psp = pspBDCacheWrapper.getObjectCache(basicBD, DEBUG, codPsp, method, codPsp);
			return (Psp) psp;
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
		}
	}



	public static Stazione getStazione(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getStazione";
			Object stazione = stazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Stazione) stazione;
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
		}
	}

	public static Stazione getStazione(BasicBD basicBD, String codStazione) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getStazione";
			Object stazione = stazioniBDCacheWrapper.getObjectCache(basicBD, DEBUG, codStazione, method, codStazione);
			return (Stazione) stazione;
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
		}
	}


	public static Tributo getTributo(BasicBD basicBD, long id) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getTributo";
			Object tributo = tributiBDCacheWrapper.getObjectCache(basicBD, DEBUG, String.valueOf(id), method, Long.valueOf(id));
			return (Tributo) tributo;
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
		}
	}

	public static Tributo getTributo(BasicBD basicBD, long idEnte, String codTributo) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			String method = "getTributo";
			Object tributo = tributiBDCacheWrapper.getObjectCache(basicBD, DEBUG, idEnte + "." + codTributo, method, Long.valueOf(idEnte), codTributo);
			return (Tributo) tributo;
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
		}
	}

	public static void unregister() {
		CacheJMXUtils.unregister();
	}
	
	public static void cleanCache() throws UtilsException {
		dominiBDCacheWrapper.resetCache();
		applicazioniBDCacheWrapper.resetCache();
		entiBDCacheWrapper.resetCache();
		ibanAccreditoBDCacheWrapper.resetCache();
		intermediariBDCacheWrapper.resetCache();
		operatoriBDCacheWrapper.resetCache();
		portaliBDCacheWrapper.resetCache();
		pspBDCacheWrapper.resetCache();
		stazioniBDCacheWrapper.resetCache();
		tributiBDCacheWrapper.resetCache();
		canaliBDCacheWrapper.resetCache();
	}
}
