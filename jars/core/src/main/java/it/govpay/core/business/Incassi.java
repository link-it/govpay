/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.NotificaAppIo;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.NotificheAppIoBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.exceptions.EcException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.IncassiException.FaultType;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.NotificaException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IncassoUtils;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.EsitoEvento;
import it.govpay.model.Evento.RuoloEvento;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Incasso.StatoIncasso;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;
import it.govpay.model.eventi.DatiPagoPA;


public class Incassi {

	private static final String ERROR_MSG_IL_DOMINIO_0_INDICATO_NELLA_RICHIESTA_NON_RISULTA_CENSITO_IN_ANAGRAFICA_GOV_PAY = "Il dominio {0} indicato nella richiesta non risulta censito in anagrafica GovPay.";
	private static Logger log = LoggerWrapperFactory.getLogger(Incassi.class);

	public Incassi() {
		super();
	}
	
	
	public void verificaRiconciliazione(RichiestaIncassoDTO richiestaIncasso) throws NotAuthorizedException, GovPayException, IncassiException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		IncassiBD incassiBD = null;
		
		try {
			IContext ctx = ContextThreadLocal.get();
			ctx.getApplicationLogger().log("incasso.richiesta");
			
			// Validazione dati obbligatori
			boolean iuvIdFlussoSet = richiestaIncasso.getIuv() != null || richiestaIncasso.getIdFlusso() != null;
			
			if(!iuvIdFlussoSet && richiestaIncasso.getCausale() == null) {
				ctx.getApplicationLogger().log("incasso.sintassi", "causale mancante");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso non e' stato specificato il campo obbligatorio causale");
			}
			
			if(!iuvIdFlussoSet && richiestaIncasso.getCausale().length() > 512) {
				ctx.getApplicationLogger().log("incasso.sintassi", "causale troppo lunga");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso e' stato specificata una causale che eccede il massimo numero di caratteri consentiti (512)");
			}
			
			if(richiestaIncasso.getCodDominio() == null) {
				ctx.getApplicationLogger().log("incasso.sintassi", "dominio mancante");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso non e' stato specificato il campo obbligatorio cod_dominio");
			}
			
			if(richiestaIncasso.getImporto() == null) {
				ctx.getApplicationLogger().log("incasso.sintassi", "importo mancante");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso non e' stato specificato il campo obbligatorio importo");
			}
			
			// Verifica Dominio
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, richiestaIncasso.getCodDominio());
			} catch (NotFoundException e) {
				ctx.getApplicationLogger().log("incasso.dominioInesistente", richiestaIncasso.getCodDominio());
				throw new IncassiException(FaultType.DOMINIO_INESISTENTE, MessageFormat.format(ERROR_MSG_IL_DOMINIO_0_INDICATO_NELLA_RICHIESTA_NON_RISULTA_CENSITO_IN_ANAGRAFICA_GOV_PAY, richiestaIncasso.getCodDominio()));
			}
			
			// Verifica IbanAccredito, se indicato
			if(richiestaIncasso.getIbanAccredito() != null)
			try {
				AnagraficaManager.getIbanAccredito(configWrapper, dominio.getId(), richiestaIncasso.getIbanAccredito());
			} catch (NotFoundException e) {
				ctx.getApplicationLogger().log("incasso.ibanInesistente", richiestaIncasso.getIbanAccredito());
				throw new IncassiException(FaultType.IBAN_INESISTENTE, MessageFormat.format(ERROR_MSG_IL_DOMINIO_0_INDICATO_NELLA_RICHIESTA_NON_RISULTA_CENSITO_IN_ANAGRAFICA_GOV_PAY, richiestaIncasso.getCodDominio()));
			}
			
			if(richiestaIncasso.getApplicazione() == null && richiestaIncasso.getOperatore() == null) {
				throw new NotAuthorizedException("Utente non autorizzato al servizio di Incassi");
			} 
			
			// Validazione della causale
			String causale = richiestaIncasso.getCausale();
			String iuv = null;
			String idf = null;
			
			// Se non mi e' stato passato uno IUV o un idFlusso, lo cerco nella causale
			if(!iuvIdFlussoSet) {
				try {
					if(causale != null) {
						iuv = IncassoUtils.getRiferimentoIncassoSingolo(causale);
						idf = IncassoUtils.getRiferimentoIncassoCumulativo(causale);
					} 
				} catch (Throwable e) {
					log.error("Riscontrato errore durante il parsing della causale",e);
				}
				
				if(iuv == null && idf==null) {
					ctx.getApplicationLogger().log("incasso.causaleNonValida", causale);
					throw new IncassiException(FaultType.CAUSALE_NON_VALIDA, "La causale dell'operazione di incasso non e' conforme alle specifiche AgID (SACIV 1.2.1): " + causale);
				}
			} 
//			else {
//				iuv = richiestaIncasso.getIuv();
//				idf = richiestaIncasso.getIdFlusso();
//				causale = iuv != null ? iuv : idf;
//				richiestaIncasso.setCausale(causale);
//			}
			
			incassiBD = new IncassiBD(configWrapper);
			
			incassiBD.setupConnection(configWrapper.getTransactionID());
						
			incassiBD.setAtomica(false);
			
			try {
				Incasso incasso = incassiBD.getIncasso(richiestaIncasso.getCodDominio(), richiestaIncasso.getIdRiconciliazione());
				
				// Richiesta presente. Verifico che i dati accessori siano gli stessi
				if(!iuvIdFlussoSet) {
					if(!causale.trim().equalsIgnoreCase(incasso.getCausale())) {
						ctx.getApplicationLogger().log("incasso.duplicato", "causale");
						throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("La causale inviata è diversa dall''originale [{0}]", incasso.getCausale()));
					}
				}else {
					if(iuv != null) {
						if(!iuv.trim().equals(incasso.getIuv())) {
							ctx.getApplicationLogger().log("incasso.duplicato", "iuv");
							throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso già registrato con iuv diverso: {0}", incasso.getIuv()));
						}
					}
					
					if(idf != null) {
						if(!idf.trim().equals(incasso.getIdFlussoRendicontazione())) {
							ctx.getApplicationLogger().log("incasso.duplicato", "idf");
							throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso gia'' registrato con id flusso rendicontazione diverso: {0}",	incasso.getIdFlussoRendicontazione()));
						}
					}
				}
				if(richiestaIncasso.getSct() != null && !richiestaIncasso.getSct().equals(incasso.getSct())) {
					ctx.getApplicationLogger().log("incasso.duplicato", "sct");
					throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso gia'' registrato con sct diverso: {0}", incasso.getSct()));
				}
				if(!richiestaIncasso.getCodDominio().equals(incasso.getCodDominio())) {
					ctx.getApplicationLogger().log("incasso.duplicato", "dominio");
					throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso gia'' registrato con dominio diverso: {0}", incasso.getCodDominio()));
				}
				if(richiestaIncasso.getImporto().compareTo(incasso.getImporto()) != 0) {
					ctx.getApplicationLogger().log("incasso.duplicato", "importo");
					throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso gia'' registrato con importo diverso: {0}", incasso.getImporto()));
				}
				
				// se l'incasso e' gia' acquisito lo restituisco
				return;
			} catch(NotFoundException nfe) {
				// Incasso non registrato. OK
			}  catch (MultipleResultException e) {
				throw new GovPayException(e);
			}
			
			// Validazione Riversamento singolo
			if(iuv != null) {
				PagamentiBD pagamentiBD = new PagamentiBD(incassiBD);
				pagamentiBD.setAtomica(false);
				
				try {
					log.debug(MessageFormat.format("Validazione riconciliazione per riversamento Singolo [Dominio:{0}, Iuv: {1}]...", richiestaIncasso.getCodDominio(), iuv));
					it.govpay.bd.model.Pagamento pagamento = pagamentiBD.getPagamento(richiestaIncasso.getCodDominio(), iuv);
					
					// Controllo se il pagamento e' stato gia' riconciliato
					if(pagamento.getIdIncasso() != null) {
						Incasso incasso = incassiBD.getIncasso(pagamento.getIdIncasso());
						throw new IncassiException(FaultType.PAGAMENTO_GIA_INCASSATO, MessageFormat.format("Il pagamento [Dominio:{0} Iuv:{1} Iur:{2}] risuta gia'' riconciliato [Sct:{3} Trn: {4}].", pagamento.getCodDominio(), pagamento.getIuv(), pagamento.getIur(), incasso.getSct(), incasso.getTrn()));
					}
					
					if(richiestaIncasso.getImporto().doubleValue() != pagamento.getImportoPagato().doubleValue())
						throw new IncassiException(FaultType.IMPORTO_ERRATO, MessageFormat.format("La richiesta di riconciliazione presenta un importo [{0}] non corripondente a quello riscosso [{1}]", richiestaIncasso.getImporto(), pagamento.getImportoPagato().doubleValue()));
					
					log.debug(MessageFormat.format("Validazione riconciliazione per riversamento Singolo [Dominio:{0}, Iuv: {1}] completata con successo.",	richiestaIncasso.getCodDominio(), iuv));
				} catch (NotFoundException nfe) {
					ctx.getApplicationLogger().log("incasso.iuvNonTrovato", iuv);
					throw new IncassiException(FaultType.PAGAMENTO_NON_TROVATO, MessageFormat.format("Lo IUV {0} estratto dalla causale di incasso non identifica alcun pagamento per il creditore {1}", iuv, richiestaIncasso.getCodDominio()));
				} catch (MultipleResultException mre) {
					ctx.getApplicationLogger().log("incasso.iuvPagamentiMultipli", iuv, richiestaIncasso.getCodDominio());
					throw new IncassiException(FaultType.PAGAMENTO_NON_IDENTIFICATO, MessageFormat.format("Lo IUV {0} estratto dalla causale di incasso identifica piu'' di un pagamento per il creditore {1}",	iuv, richiestaIncasso.getCodDominio()));
				}
			}
			
			// Validazione Riversamento cumulativo
			Fr fr = null;
			if(idf != null) {
				FrBD frBD = new FrBD(incassiBD);
				frBD.setAtomica(false);
				
				try {
					log.debug(MessageFormat.format("Validazione riconciliazione per riversamento cumulativo [Dominio:{0}, IdFlusso:{1}]...", richiestaIncasso.getCodDominio(), idf));
					// Cerco l'idf come case insensitive
//								fr = frBD.getFr(idf, richiestaIncasso.isRicercaIdFlussoCaseInsensitive()); TODO come si gestisce ora?
					fr = frBD.getFr(richiestaIncasso.getCodDominio(), idf, null, false);
					
					log.debug(MessageFormat.format("Ricerca flusso per riversamento cumulativo [Dominio:{0}, IdFlusso:{1}] completata.", richiestaIncasso.getCodDominio(), idf));
					
					// Controllo se il flusso e' stato gia' riconciliato
					if(fr.getIdIncasso() != null) {
						Incasso incasso = incassiBD.getIncasso(fr.getIdIncasso());
						throw new IncassiException(FaultType.FLUSSO_GIA_INCASSATO, MessageFormat.format("Il flusso [IdFlusso:{0}] risuta gia'' riconciliato [Sct:{1} Trn:{2}].", idf, incasso.getSct(), incasso.getTrn()));
					}
					
					
					if(!fr.getStato().equals(StatoFr.ACCETTATA)) {
						ctx.getApplicationLogger().log("incasso.frAnomala", idf);
						throw new IncassiException(FaultType.FR_ANOMALA, MessageFormat.format("Il flusso di rendicontazione [IdFlusso:{0}] identificato dalla causale di incasso risulta avere delle anomalie", idf));
					}
					
					// Verifica importo pagato con l'incassato
					if(fr.getImportoTotalePagamenti().doubleValue() != richiestaIncasso.getImporto().doubleValue()) {
						ctx.getApplicationLogger().log("incasso.importoErrato", fr.getImportoTotalePagamenti().doubleValue() + "", richiestaIncasso.getImporto().doubleValue() + "");
						throw new IncassiException(FaultType.IMPORTO_ERRATO, MessageFormat.format("L''importo del flusso di redicontazione [{0}] non corriponde all''importo del riversamento [{1}]", richiestaIncasso.getImporto(), fr.getImportoTotalePagamenti().doubleValue()));
					}
					log.debug(MessageFormat.format("Validazione riconciliazione per riversamento cumulativo [Dominio:{0}, IdFlusso: {1}] completata con successo.",	richiestaIncasso.getCodDominio(), idf));
				} catch (NotFoundException nfe) {
					ctx.getApplicationLogger().log("incasso.idfNonTrovato", idf);
					throw new IncassiException(FaultType.IDF_NON_TROVATO, MessageFormat.format("L''identificativo {0} estratto dalla causale di incasso non identifica alcun flusso di rendicontazione", idf));
				} 
			}	
		}catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (UtilsException e) {
			throw new GovPayException(e);
		} finally {
			if(incassiBD != null)
				incassiBD.closeConnection();
		}
	}
	
	@SuppressWarnings("unused")
	public RichiestaIncassoDTOResponse elaboraRiconciliazione(String codDomino, String idRiconciliazione, IContext ctx) throws ServiceException, GovPayException  {
		RichiestaIncassoDTOResponse richiestaIncassoResponse = new RichiestaIncassoDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		IncassiBD incassiBD = null;
		Incasso incasso = null;
		GpContext gpContext = (GpContext) ctx.getApplicationContext();
		String causale = null;
		String iuv = null;
		String idf = null;
		log.debug(MessageFormat.format("Elaborazione riconciliazione [Dominio:{0}, Id: {1}] ...", codDomino, idRiconciliazione));
		List<Evento> listaEventi = new ArrayList<>();
		
		try {
			if(gpContext.getEventoCtx().getDatiPagoPA() == null) {
				gpContext.getEventoCtx().setDatiPagoPA(new DatiPagoPA());
			}
			
			incassiBD = new IncassiBD(configWrapper);
			incassiBD.setupConnection(configWrapper.getTransactionID());
			incassiBD.setAtomica(false);
			incassiBD.enableSelectForUpdate();

			try {
				log.debug(MessageFormat.format("Lettura riconciliazione [Dominio:{0}, Id: {1}] dal db...", codDomino, idRiconciliazione));
				incasso = incassiBD.getIncasso(codDomino, idRiconciliazione);
				richiestaIncassoResponse.setIncasso(incasso);
			} catch (NotFoundException e) {
				// non dovrebbe succedere perche' la coppia viene letta dal db
				log.warn(MessageFormat.format("La riconciliazione [Dominio:{0}, Id: {1}] non e'' presente sul db: {2}", codDomino, idRiconciliazione, e.getMessage()), e);
				return richiestaIncassoResponse;
			}  catch (MultipleResultException e) {
				throw new GovPayException(e);
			}
			
			log.debug(MessageFormat.format("Lettura riconciliazione [Dominio:{0}, Id: {1}] dal db completata.", codDomino, idRiconciliazione));
			
			// Validazione dati obbligatori
			boolean iuvIdFlussoSet = incasso.getIuv() != null || incasso.getIdFlussoRendicontazione() != null;
			
			gpContext.getEventoCtx().getDatiPagoPA().setTrn(incasso.getTrn());
			gpContext.getEventoCtx().getDatiPagoPA().setSct(incasso.getSct());
			
			incassiBD.setAutoCommit(false);
			
			// Validazione della causale
			causale = incasso.getCausale();
			
			// Se non mi e' stato passato uno IUV o un idFlusso, lo cerco nella causale
			if(!iuvIdFlussoSet) {
				try {
					if(causale != null) {
						iuv = IncassoUtils.getRiferimentoIncassoSingolo(causale);
						idf = IncassoUtils.getRiferimentoIncassoCumulativo(causale);
					} 
				} catch (Throwable e) {
					log.error("Riscontrato errore durante il parsing della causale",e);
				} 
			} else {
				iuv = incasso.getIuv();
				idf = incasso.getIdFlussoRendicontazione();
			}
			
			return eseguiAcquisizioneRiconciliazione(richiestaIncassoResponse, codDomino, idRiconciliazione, ctx, configWrapper, incassiBD, incasso, causale, iuv, idf, false, true, listaEventi);
		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (NotificaException e) {
			throw new GovPayException(e);
		} catch (UtilsException e) {
			throw new GovPayException(e);
		} catch (IncassiException e) {
			if(incasso == null) {
				throw new GovPayException(EsitoOperazione.INTERNAL,"Not found");
			}
			// salvataggio stato in errore
			incasso.setStato(StatoIncasso.ERRORE);
			incasso.setDataIncasso(new Date());
			String descr = e.getMessage() + ": "  + e.getDetails();
			
			log.debug(MessageFormat.format("Riconciliazione [Dominio:{0}, Id: {1}] Iuv [{2}], IdFlusso [{3}], Causale [{4}] completata con errore: {5}", codDomino, idRiconciliazione, iuv, idf, causale, descr));
			
			if(descr.length() > 255) {
				descr = descr.substring(0, 252) + "...";
			}

			incasso.setDescrizioneStato(descr);
			
			try {
				incassiBD.updateIncasso(incasso);
				incassiBD.commit();
				gpContext.getEventoCtx().setIdIncasso(incasso.getId()); 
			} catch(Exception e1) {
				incassiBD.rollback();
				throw new GovPayException(e1);
			} finally {
				incassiBD.setAutoCommit(true);
			}
			return richiestaIncassoResponse;
		} finally {
			try {
				if(incassiBD != null)
					incassiBD.disableSelectForUpdate();
			} catch (ServiceException e) {}
			
			if(incassiBD != null)
				incassiBD.closeConnection();
			
			if(listaEventi.size() >0) {
				EventiBD eventiBD = null;
				
				try {
					eventiBD = new EventiBD(configWrapper);
					eventiBD.setupConnection(configWrapper.getTransactionID());
					eventiBD.setAtomica(false);
					
					for (Evento evento : listaEventi) {
						eventiBD.insertEvento(evento);	
					}
				} catch (ServiceException e) {
					log.error("Riscontrato errore durante il salvataggio degli eventi:" + e.getMessage(),e);
				} finally {
					if(eventiBD != null) {
						eventiBD.closeConnection();
					}
				}
			}
		}
	}


	private RichiestaIncassoDTOResponse eseguiAcquisizioneRiconciliazione(RichiestaIncassoDTOResponse richiestaIncassoResponse, String codDomino, String idRiconciliazione, IContext ctx, BDConfigWrapper configWrapper,
			IncassiBD incassiBD, Incasso incasso, String causale, String iuv, String idf, boolean ricercaIdFlussoCaseInsensitive, boolean salvaConUpdate, List<Evento> listaEventi)
			throws ServiceException, IncassiException, UtilsException, GovPayException, NotificaException {
		GpContext gpContext = (GpContext) ctx.getApplicationContext();
		
		log.debug(MessageFormat.format("Riconciliazione [Dominio:{0}, Id: {1}] Iuv [{2}], IdFlusso [{3}], Causale [{4}] in corso...", codDomino, idRiconciliazione, iuv, idf, causale));
		
		PagamentiBD pagamentiBD = new PagamentiBD(incassiBD);
		pagamentiBD.setAtomica(false);
		
		FrBD frBD = new FrBD(incassiBD);
		frBD.setAtomica(false);
		
		VersamentiBD versamentiBD = new VersamentiBD(incassiBD);
		versamentiBD.setAtomica(false);
		
		RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(incassiBD);
		rendicontazioniBD.setAtomica(false);
		
		List<it.govpay.bd.model.Pagamento> pagamenti = new ArrayList<>();
		
		// Riversamento singolo
		if(iuv != null) {
			try {
				log.debug(MessageFormat.format("Ricerca riconciliazione per riversamento Singolo [Dominio:{0}, Iuv: {1}]...", codDomino, iuv));
				it.govpay.bd.model.Pagamento pagamento = pagamentiBD.getPagamento(incasso.getCodDominio(), iuv);
				
				if(incasso.getImporto().doubleValue() != pagamento.getImportoPagato().doubleValue())
					throw new IncassiException(FaultType.IMPORTO_ERRATO, MessageFormat.format("La richiesta di riconciliazione presenta un importo [{0}] non corripondente a quello riscosso [{1}]", incasso.getImporto(), pagamento.getImportoPagato().doubleValue()));
				
				pagamenti.add(pagamento);
				log.debug(MessageFormat.format("Ricerca riconciliazione per riversamento Singolo [Dominio:{0}, Iuv: {1}] completata.", codDomino, iuv));
			} catch (NotFoundException nfe) {
				ctx.getApplicationLogger().log("incasso.iuvNonTrovato", iuv);
				throw new IncassiException(FaultType.PAGAMENTO_NON_TROVATO, MessageFormat.format("Lo IUV {0} estratto dalla causale di incasso non identifica alcun pagamento per il creditore {1}", iuv, incasso.getCodDominio()));
			} catch (MultipleResultException mre) {
				ctx.getApplicationLogger().log("incasso.iuvPagamentiMultipli", iuv, incasso.getCodDominio());
				throw new IncassiException(FaultType.PAGAMENTO_NON_IDENTIFICATO, MessageFormat.format("Lo IUV {0} estratto dalla causale di incasso identifica piu'' di un pagamento per il creditore {1}",	iuv, incasso.getCodDominio()));
			}
			
			
		}
		
		// Riversamento cumulativo
		Fr fr = null;
		if(idf != null) {
			try {
				log.debug(MessageFormat.format("Ricerca flusso per riversamento cumulativo [Dominio:{0}, IdFlusso: {1}]...", codDomino, idf));
				// Cerco l'idf come case insensitive
				fr = frBD.getFr(codDomino, idf, null, false, ricercaIdFlussoCaseInsensitive);
				richiestaIncassoResponse.setFr(fr);
				log.debug(MessageFormat.format("Ricerca flusso per riversamento cumulativo [Dominio:{0}, IdFlusso: {1}] completata.", codDomino, idf));
				if(!fr.getStato().equals(StatoFr.ACCETTATA)) {
					ctx.getApplicationLogger().log("incasso.frAnomala", idf);
					throw new IncassiException(FaultType.FR_ANOMALA, MessageFormat.format("Il flusso di rendicontazione {0} identificato dalla causale di incasso risulta avere delle anomalie", idf));
				}
				
				// Verifica importo pagato con l'incassato
				if(fr.getImportoTotalePagamenti().doubleValue() != incasso.getImporto().doubleValue()) {
					ctx.getApplicationLogger().log("incasso.importoErrato", fr.getImportoTotalePagamenti().doubleValue() + "", incasso.getImporto().doubleValue() + "");
					throw new IncassiException(FaultType.IMPORTO_ERRATO, MessageFormat.format("L''importo del flusso di redicontazione [{0}] non corriponde all''importo del riversamento [{1}]",	incasso.getImporto(), fr.getImportoTotalePagamenti().doubleValue()));
				}
				
				Versamento versamentoBusiness = new Versamento();
				
				
				for(Rendicontazione rendicontazione : fr.getRendicontazioni(incassiBD)) {
					
					if(rendicontazione.getStato().equals(StatoRendicontazione.ALTRO_INTERMEDIARIO)) continue;
					
					it.govpay.bd.model.Pagamento pagamento = rendicontazione.getPagamento(incassiBD);
					
					if(pagamento == null) {
						// Incasso di un pagamento non presente. Controllo se il pagamento non e' stato creato nel frattempo dall'arrivo di una RT
						try {
							pagamento = pagamentiBD.getPagamento(fr.getCodDominio(), rendicontazione.getIuv(), rendicontazione.getIur(), rendicontazione.getIndiceDati());
						} catch (NotFoundException e) {
							// Pagamento non presente. Lo inserisco 
							it.govpay.bd.model.Versamento versamento = null;
							try {
								// Workaround per le limitazioni in select for update. Da rimuovere quando lo iuv sara nel versamento.
								incassiBD.disableSelectForUpdate();
								versamento = versamentoBusiness.chiediVersamento(null, null, null, null, fr.getCodDominio(), rendicontazione.getIuv(), TipologiaTipoVersamento.DOVUTO);
								incassiBD.enableSelectForUpdate();
								versamento = versamentiBD.getVersamento(versamento.getId(), true);
							} catch (GovPayException gpe) {
								// Non deve accadere... la rendicontazione
								throw new IncassiException(FaultType.FR_ANOMALA, MessageFormat.format("Il versamento rendicontato [Dominio:{0} IUV:{1}] non esiste.", fr.getCodDominio(), rendicontazione.getIuv()));
							} catch (EcException e2) {
								// Non deve accadere... la rendicontazione
								throw new IncassiException(FaultType.FR_ANOMALA, MessageFormat.format("Il versamento rendicontato [Dominio:{0} IUV:{1}] non esiste.", fr.getCodDominio(), rendicontazione.getIuv()));
							}
							
							List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti();
							
							pagamento = new it.govpay.bd.model.Pagamento();
							pagamento.setTipo(TipoPagamento.ENTRATA);
							pagamento.setStato(Stato.PAGATO_SENZA_RPT);
							pagamento.setCodDominio(fr.getCodDominio());
							pagamento.setDataAcquisizione(rendicontazione.getData());
							pagamento.setDataPagamento(rendicontazione.getData());
							pagamento.setImportoPagato(rendicontazione.getImporto());
							pagamento.setIur(rendicontazione.getIur());
							pagamento.setIuv(rendicontazione.getIuv());
							pagamento.setIndiceDati(rendicontazione.getIndiceDati() == null ? 1 : rendicontazione.getIndiceDati());
							
							SingoloVersamento singoloVersamento = singoliVersamenti.get(0); 
							pagamento.setSingoloVersamento(singoloVersamento);
							rendicontazione.setPagamento(pagamento);
							pagamentiBD.insertPagamento(pagamento);
							rendicontazione.setIdPagamento(pagamento.getId());
								
							//Aggiorno lo stato del versamento:
							switch (singoloVersamento.getStatoSingoloVersamento()) {
								case NON_ESEGUITO:
									versamentiBD.updateStatoSingoloVersamento(singoloVersamento.getId(), StatoSingoloVersamento.ESEGUITO);
									versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.ESEGUITO, "Eseguito senza RPT");
									// Aggiornamento stato promemoria
									versamentiBD.updateVersamentoInformazioniAvvisatura(versamento.getId(), true, null, true, null, true, null);
									// Aggiornamento data ultima modifica ACA, per la chiusura di una pendenza pagata fuori PagoPA.
									versamentiBD.updateUltimaModificaAca(versamento.getId());
									
									break;
								case ESEGUITO:
									versamento.setAnomalo(true);
									versamentiBD.updateStatoVersamentoAnomalo(versamento.getId(), StatoVersamento.ESEGUITO, "Pagamento duplicato", versamento.isAnomalo());
									break;
							}
							
							// salvo l'evento in una lista, effettuo l'inserimento di tutti gli eventi insieme al termine della procedura perche' quando veniva impostato l'id_fr il DB andava in deadlock perche' il flusso e' in lock dalla select precedente.
							Evento eventoNota = new Evento();
							eventoNota.setCategoriaEvento(CategoriaEvento.INTERNO);
							eventoNota.setRuoloEvento(RuoloEvento.CLIENT);
							eventoNota.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
							eventoNota.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
							eventoNota.setEsitoEvento(EsitoEvento.OK);
							eventoNota.setDettaglioEsito(MessageFormat.format("Riconciliato flusso {0} con Pagamento senza RPT [IUV: {1} IUR:{2}].", fr.getCodFlusso(), rendicontazione.getIuv(), rendicontazione.getIur()));
							eventoNota.setTipoEvento(EventoContext.GOVPAY_TIPOEVENTO_GOVPAYPAGAMENTOESEGUITOSENZARPT);
							eventoNota.setIuv(rendicontazione.getIuv());
							eventoNota.setCodDominio(fr.getCodDominio());
							eventoNota.setCcp(rendicontazione.getIur());
							eventoNota.setComponente(EventoContext.Componente.GOVPAY.toString());
							eventoNota.setTransactionId(ctx.getTransactionId());
							
							String clusterId = GovpayConfig.getInstance().getClusterId();
							if(clusterId != null)
								eventoNota.setClusterId(clusterId);
							else 
								eventoNota.setClusterId(GovpayConfig.getInstance().getAppName());
							
							listaEventi.add(eventoNota);
							
							TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper);
							// Se e' prevista la spedizione della ricevuta allora procedo a spedirla
							Promemoria promemoria = null;
							if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaAbilitato()) {
								log.debug("Schedulazione invio ricevuta di pagamento senza RPT in corso...");
								it.govpay.core.business.Promemoria promemoriaBD = new it.govpay.core.business.Promemoria();
								promemoria = promemoriaBD.creaPromemoriaRicevutaEseguitoSenzaRPT(versamento, versamento.getTipoVersamentoDominio(configWrapper));
								String msg = "non e' stato trovato un destinatario valido, l'invio non verra' schedulato.";
								if(promemoria.getDestinatarioTo() != null) {
									msg = "e' stato trovato un destinatario valido, l'invio e' stato schedulato con successo.";
									PromemoriaBD promemoriaBD2 = new PromemoriaBD(versamentiBD);
									promemoriaBD2.setAtomica(false); // condivisione della connessione;
									promemoriaBD2.insertPromemoria(promemoria);
									log.debug(MessageFormat.format("Inserimento promemoria ricevuta di pagamento senza RPT per la Pendenza[{0}] effettuato.", versamento.getCodVersamentoEnte()));
								}
								log.debug(MessageFormat.format("Creazione promemoria completata: {0}", msg));
							}
							
							//schedulo l'invio della notifica APPIO
							if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaAbilitato()) {
								log.debug("Creo notifica avvisatura ricevuta di pagamento senza RPT tramite App IO..."); 
								NotificaAppIo notificaAppIo = new NotificaAppIo(versamento, it.govpay.model.NotificaAppIo.TipoNotifica.RICEVUTA_NO_RPT, configWrapper);
								log.debug("Creazione notifica avvisatura ricevuta tramite App IO completata.");
								NotificheAppIoBD notificheAppIoBD = new NotificheAppIoBD(versamentiBD);
								notificheAppIoBD.setAtomica(false); // riuso connessione
								notificheAppIoBD.insertNotifica(notificaAppIo);
								log.debug("Inserimento su DB notifica avvisatura ricevuta di pagamento senza RPT tramite App IO completata.");
							}
						} catch (MultipleResultException e) {
							ctx.getApplicationLogger().log("incasso.frAnomala", idf);
							throw new IncassiException(FaultType.FR_ANOMALA, MessageFormat.format("La rendicontazione [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}] non identifica univocamente un pagamento", fr.getCodDominio(), rendicontazione.getIuv(), rendicontazione.getIur(), rendicontazione.getIndiceDati()));
						}
					} else {
						// Verifica che l'importo rendicontato corrisponda al pagato
						if(rendicontazione.getImporto().doubleValue() != pagamento.getImportoPagato().doubleValue())
							throw new IncassiException(FaultType.IMPORTO_ERRATO, MessageFormat.format("La rendicontazione [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}] presenta un importo [{4}] non corripondente a quello riscosso [{5}]", fr.getCodDominio(), rendicontazione.getIuv(), rendicontazione.getIur(), rendicontazione.getIndiceDati(), rendicontazione.getImporto(), pagamento.getImportoPagato().doubleValue()));
					}
					
					//Aggiorno la FK della rendicontazione
					rendicontazione.setIdPagamento(pagamento.getId());
					rendicontazioniBD.updateRendicontazione(rendicontazione);
					
					pagamenti.add(pagamento);
				}
			} catch (NotFoundException nfe) {
				ctx.getApplicationLogger().log("incasso.idfNonTrovato", idf);
				throw new IncassiException(FaultType.IDF_NON_TROVATO, MessageFormat.format("L''identificativo {0} estratto dalla causale di incasso non identifica alcun flusso di rendicontazione", idf));
			} 
		}
		
		// Verifica stato dei pagamenti da incassare 
		for(it.govpay.bd.model.Pagamento pagamento : pagamenti) {
			if(Stato.INCASSATO.equals(pagamento.getStato())) {
				ctx.getApplicationLogger().log("incasso.pagamentoGiaIncassato", pagamento.getCodDominio(), pagamento.getIuv(), pagamento.getIur());
				throw new IncassiException(FaultType.PAGAMENTO_GIA_INCASSATO, MessageFormat.format("Uno dei pagamenti [Dominio:{0} Iuv:{1} Iur:{2}] risuta gia'' riconciliato.", pagamento.getCodDominio(), pagamento.getIuv(), pagamento.getIur()));
			}
		}
		
		// Inserisco l'incasso e aggiorno lo stato dei pagamenti
		try {
			// salvataggio stato incasso
			incasso.setStato(StatoIncasso.ACQUISITO);
			incasso.setDataIncasso(new Date());
			
			if(salvaConUpdate) {
				incassiBD.updateIncasso(incasso);
			} else {
				incassiBD.insertIncasso(incasso);
			}
			
			for(it.govpay.bd.model.Pagamento pagamento : pagamenti) {
				pagamento.setStato(Stato.INCASSATO);
				pagamento.setIncasso(incasso);
				pagamentiBD.updatePagamento(pagamento);
				versamentiBD.aggiornaIncassoVersamento(pagamento);
			}
			
			// se e' un incasso cumulativo collego il flusso all'incasso
			if(fr != null) {
				frBD.updateIdIncasso(fr.getId(), incasso.getId());
			}
			incassiBD.commit();
			gpContext.getEventoCtx().setIdIncasso(incasso.getId()); 
			
			for (Evento evento : listaEventi) {
				evento.setIdIncasso(incasso.getId());
				evento.setIdFr(fr.getId());
			}
			
			log.debug(MessageFormat.format("Riconciliazione [Dominio:{0}, Id: {1}] Iuv [{2}], IdFlusso [{3}], Causale [{4}] completata con esito OK.", codDomino, idRiconciliazione, iuv, idf, causale));
		} catch(Exception e) {
			incassiBD.rollback();
			throw new GovPayException(e);
		} finally {
			try {
				incassiBD.setAutoCommit(true);
			} catch (ServiceException e) {
				log.error(MessageFormat.format("Riscontrato errore ripristino dell''autocommit:{0}", e.getMessage()),e);
			} 
		}
		
		return richiestaIncassoResponse;
	}

	public RichiestaIncassoDTOResponse richiestaIncasso(RichiestaIncassoDTO richiestaIncasso) throws NotAuthorizedException, GovPayException, IncassiException, EcException {
		
		boolean ricercaIdFlussoCaseInsensitive = richiestaIncasso.isRicercaIdFlussoCaseInsensitive();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		IncassiBD incassiBD = null;
		List<Evento> listaEventi = new ArrayList<>();
		try {
			IContext ctx = ContextThreadLocal.get();
			ctx.getApplicationLogger().log("incasso.richiesta");
			GpContext gpContext = (GpContext) ctx.getApplicationContext();
			
			
			// Validazione dati obbligatori
			boolean iuvIdFlussoSet = richiestaIncasso.getIuv() != null || richiestaIncasso.getIdFlusso() != null;
			
			if(!iuvIdFlussoSet && richiestaIncasso.getCausale() == null) {
				ctx.getApplicationLogger().log("incasso.sintassi", "causale mancante");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso non e' stato specificato il campo obbligatorio causale");
			}
			
			if(!iuvIdFlussoSet && richiestaIncasso.getCausale().length() > 512) {
				ctx.getApplicationLogger().log("incasso.sintassi", "causale troppo lunga");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso e' stato specificata una causale che eccede il massimo numero di caratteri consentiti (512)");
			}
			
			if(richiestaIncasso.getCodDominio() == null) {
				ctx.getApplicationLogger().log("incasso.sintassi", "dominio mancante");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso non e' stato specificato il campo obbligatorio cod_dominio");
			}
			
			if(richiestaIncasso.getImporto() == null) {
				ctx.getApplicationLogger().log("incasso.sintassi", "importo mancante");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso non e' stato specificato il campo obbligatorio importo");
			}
			
			// Verifica Dominio
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, richiestaIncasso.getCodDominio());
			} catch (NotFoundException e) {
				ctx.getApplicationLogger().log("incasso.dominioInesistente", richiestaIncasso.getCodDominio());
				throw new IncassiException(FaultType.DOMINIO_INESISTENTE, MessageFormat.format(ERROR_MSG_IL_DOMINIO_0_INDICATO_NELLA_RICHIESTA_NON_RISULTA_CENSITO_IN_ANAGRAFICA_GOV_PAY, richiestaIncasso.getCodDominio()));
			}
			
			// Verifica IbanAccredito, se indicato
			if(richiestaIncasso.getIbanAccredito() != null)
			try {
				AnagraficaManager.getIbanAccredito(configWrapper, dominio.getId(), richiestaIncasso.getIbanAccredito());
			} catch (NotFoundException e) {
				ctx.getApplicationLogger().log("incasso.ibanInesistente", richiestaIncasso.getIbanAccredito());
				throw new IncassiException(FaultType.IBAN_INESISTENTE, MessageFormat.format("Il conto {0} indicato nella richiesta non risulta censito in anagrafica GovPay.", richiestaIncasso.getIbanAccredito()));
			}
			
			Long idApplicazione = null;
			Long idOperatore = null;
			
			// Verifica autorizzazione all'incasso e acquisizione applicazione chiamante
			if(richiestaIncasso.getApplicazione() != null) {
				idApplicazione = richiestaIncasso.getApplicazione().getId();
			} else if(richiestaIncasso.getOperatore() != null) {
				idOperatore = richiestaIncasso.getOperatore().getId();
			} else {
				throw new NotAuthorizedException("Utente non autorizzato al servizio di Incassi");
			} 
			
			
			// Validazione della causale
			String causale = richiestaIncasso.getCausale();
			String iuv = null;
			String idf = null;
			
			// Se non mi e' stato passato uno IUV o un idFlusso, lo cerco nella causale
			if(!iuvIdFlussoSet) {
				try {
					if(causale != null) {
						iuv = IncassoUtils.getRiferimentoIncassoSingolo(causale);
						idf = IncassoUtils.getRiferimentoIncassoCumulativo(causale);
					} 
				} catch (Throwable e) {
					log.error("Riscontrato errore durante il parsing della causale",e);
				}
				
				if(iuv == null && idf==null) {
					ctx.getApplicationLogger().log("incasso.causaleNonValida", causale);
					throw new IncassiException(FaultType.CAUSALE_NON_VALIDA, MessageFormat.format("La causale dell''operazione di incasso non e'' conforme alle specifiche AgID (SACIV 1.2.1): {0}", causale));
				}
			} else {
				iuv = richiestaIncasso.getIuv();
				idf = richiestaIncasso.getIdFlusso();
				causale = iuv != null ? iuv : idf;
				richiestaIncasso.setCausale(causale);
			}
			
			richiestaIncasso.setIuv(iuv);
			richiestaIncasso.setIdFlusso(idf);
			
			// OVERRIDE TRN NUOVA GESTIONE
			richiestaIncasso.setTrn(iuv != null ? iuv : idf);
			// IDENTIFICATIVO INCASSO
			if(richiestaIncasso.getIdRiconciliazione() == null)
				richiestaIncasso.setIdRiconciliazione(richiestaIncasso.getTrn());
			RichiestaIncassoDTOResponse richiestaIncassoResponse = new RichiestaIncassoDTOResponse();
			
			if(gpContext.getEventoCtx().getDatiPagoPA() == null) {
				gpContext.getEventoCtx().setDatiPagoPA(new DatiPagoPA());
			}
			
			gpContext.getEventoCtx().getDatiPagoPA().setTrn(richiestaIncasso.getTrn());
			gpContext.getEventoCtx().getDatiPagoPA().setSct(richiestaIncasso.getSct());
			// Controllo se il TRN dell'incasso e' gia registrato
			
			incassiBD = new IncassiBD(configWrapper);
			Incasso incasso = null;
			try {
				incasso = incassiBD.getIncasso(dominio.getCodDominio(), richiestaIncasso.getIdRiconciliazione());
				gpContext.getEventoCtx().getDatiPagoPA().setSct(incasso.getSct());
				gpContext.getEventoCtx().setIdIncasso(incasso.getId()); 
				
				// Richiesta presente. Verifico che i dati accessori siano gli stessi
				if(!iuvIdFlussoSet) {
					if(!causale.trim().equalsIgnoreCase(incasso.getCausale())) {
						ctx.getApplicationLogger().log("incasso.duplicato", "causale");
						throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("La causale inviata è diversa dall''originale [{0}]", incasso.getCausale()));
					}
				}else {
					if(iuv != null) {
						if(!iuv.trim().equals(incasso.getIuv())) {
							ctx.getApplicationLogger().log("incasso.duplicato", "iuv");
							throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso già registrato con iuv diverso: {0}", incasso.getIuv()));
						}
					}
					
					if(idf != null) {
						if(!idf.trim().equals(incasso.getIdFlussoRendicontazione())) {
							ctx.getApplicationLogger().log("incasso.duplicato", "idf");
							throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso gia'' registrato con id flusso rendicontazione diverso: {0}",	incasso.getIdFlussoRendicontazione()));
						}
					}
				}
				if(richiestaIncasso.getSct() != null && !richiestaIncasso.getSct().equals(incasso.getSct())) {
					ctx.getApplicationLogger().log("incasso.duplicato", "sct");
					throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso gia'' registrato con sct diverso: {0}", incasso.getSct()));
				}
				if(!richiestaIncasso.getCodDominio().equals(incasso.getCodDominio())) {
					ctx.getApplicationLogger().log("incasso.duplicato", "dominio");
					throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso gia'' registrato con dominio diverso: {0}", incasso.getCodDominio()));
				}
				if(richiestaIncasso.getImporto().compareTo(incasso.getImporto()) != 0) {
					ctx.getApplicationLogger().log("incasso.duplicato", "importo");
					throw new IncassiException(FaultType.DUPLICATO, MessageFormat.format("Incasso gia'' registrato con importo diverso: {0}", incasso.getImporto()));
				}				
				richiestaIncassoResponse.setIncasso(incasso);
				richiestaIncassoResponse.setCreated(false);
				return richiestaIncassoResponse;
			} catch(NotFoundException nfe) {
				// Incasso non registrato.
				richiestaIncassoResponse.setCreated(true);
				
				incasso = new it.govpay.bd.model.Incasso();
				incasso.setCausale(richiestaIncasso.getCausale());
				incasso.setCodDominio(richiestaIncasso.getCodDominio());
				incasso.setDataValuta(richiestaIncasso.getDataValuta());
				incasso.setDataContabile(richiestaIncasso.getDataContabile());
				incasso.setDispositivo(richiestaIncasso.getDispositivo());
				incasso.setImporto(richiestaIncasso.getImporto());
				incasso.setTrn(richiestaIncasso.getTrn());
				incasso.setIbanAccredito(richiestaIncasso.getIbanAccredito());
				incasso.setIdApplicazione(idApplicazione);
				incasso.setIdOperatore(idOperatore); 
				incasso.setSct(richiestaIncasso.getSct());
				incasso.setIuv(richiestaIncasso.getIuv());
				incasso.setIdFlussoRendicontazione(richiestaIncasso.getIdFlusso());
				incasso.setIdRiconciliazione(richiestaIncasso.getIdRiconciliazione());
				richiestaIncassoResponse.setIncasso(incasso);
				
			} catch (MultipleResultException e) {
				throw new GovPayException(e);
			}
			
			// Sto selezionando i pagamenti per impostarli come Incassati.
			incassiBD.setupConnection(configWrapper.getTransactionID());
			
			incassiBD.setAtomica(false);
			
			incassiBD.enableSelectForUpdate();
			
			incassiBD.setAutoCommit(false);
			
			return eseguiAcquisizioneRiconciliazione(richiestaIncassoResponse, incasso.getCodDominio(), incasso.getIdRiconciliazione(), ctx, configWrapper, incassiBD, incasso, causale, iuv, idf, ricercaIdFlussoCaseInsensitive, false, listaEventi);
		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (NotificaException e) {
			throw new GovPayException(e);
		} catch (UtilsException e) {
			throw new GovPayException(e);
		} finally {
			try {
				if(incassiBD != null)
					incassiBD.disableSelectForUpdate();
			} catch (ServiceException e) {}
			
			if(incassiBD != null)
				incassiBD.closeConnection();
			
			if(listaEventi.size() >0) {
				EventiBD eventiBD = null;
				
				try {
					eventiBD = new EventiBD(configWrapper);
					eventiBD.setupConnection(configWrapper.getTransactionID());
					eventiBD.setAtomica(false);
					
					for (Evento evento : listaEventi) {
						eventiBD.insertEvento(evento);	
					}
				} catch (ServiceException e) {
					log.error(MessageFormat.format("Riscontrato errore durante il salvataggio degli eventi:{0}", e.getMessage()),e);
				} finally {
					if(eventiBD != null) {
						eventiBD.closeConnection();
					}
				}
			}
		}
	}
}


