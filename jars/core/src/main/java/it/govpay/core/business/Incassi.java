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
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.exceptions.EcException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.IncassiException.FaultType;
import it.govpay.core.exceptions.NotAuthorizedException;
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


public class Incassi {

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
//			GpContext gpContext = (GpContext) ctx.getApplicationContext();
			
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
				throw new IncassiException(FaultType.DOMINIO_INESISTENTE, "Il dominio " + richiestaIncasso.getCodDominio() + " indicato nella richiesta non risulta censito in anagrafica GovPay.");
			}
			
			// Verifica IbanAccredito, se indicato
			if(richiestaIncasso.getIbanAccredito() != null)
			try {
				AnagraficaManager.getIbanAccredito(configWrapper, dominio.getId(), richiestaIncasso.getIbanAccredito());
			} catch (NotFoundException e) {
				ctx.getApplicationLogger().log("incasso.ibanInesistente", richiestaIncasso.getIbanAccredito());
				throw new IncassiException(FaultType.IBAN_INESISTENTE, "Il dominio " + richiestaIncasso.getCodDominio() + " indicato nella richiesta non risulta censito in anagrafica GovPay.");
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
				} finally {
					if(iuv == null && idf==null) {
						ctx.getApplicationLogger().log("incasso.causaleNonValida", causale);
						throw new IncassiException(FaultType.CAUSALE_NON_VALIDA, "La causale dell'operazione di incasso non e' conforme alle specifiche AgID (SACIV 1.2.1): " + causale);
					}
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
						throw new IncassiException(FaultType.DUPLICATO, "La causale inviata è diversa dall'originale [" + incasso.getCausale()+ "]");
					}
				}else {
					if(iuv != null) {
						if(!iuv.trim().equals(incasso.getIuv())) {
							ctx.getApplicationLogger().log("incasso.duplicato", "iuv");
							throw new IncassiException(FaultType.DUPLICATO, "Incasso già registrato con iuv diverso: "+incasso.getIuv()+"");
						}
					}
					
					if(idf != null) {
						if(!idf.trim().equals(incasso.getIdFlussoRendicontazione())) {
							ctx.getApplicationLogger().log("incasso.duplicato", "idf");
							throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con id flusso rendicontazione diverso: " + incasso.getIdFlussoRendicontazione());
						}
					}
				}
				if(richiestaIncasso.getSct() != null && !richiestaIncasso.getSct().equals(incasso.getSct())) {
					ctx.getApplicationLogger().log("incasso.duplicato", "sct");
					throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con sct diverso: " + incasso.getSct());
				}
				if(!richiestaIncasso.getCodDominio().equals(incasso.getCodDominio())) {
					ctx.getApplicationLogger().log("incasso.duplicato", "dominio");
					throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con dominio diverso: " + incasso.getCodDominio());
				}
				if(richiestaIncasso.getImporto().compareTo(incasso.getImporto()) != 0) {
					ctx.getApplicationLogger().log("incasso.duplicato", "importo");
					throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con importo diverso: " + incasso.getImporto());
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
					log.debug("Validazione riconciliazione per riversamento Singolo [Dominio:"+richiestaIncasso.getCodDominio()+", Iuv: "+iuv+"]...");
					it.govpay.bd.model.Pagamento pagamento = pagamentiBD.getPagamento(richiestaIncasso.getCodDominio(), iuv);
					
					// Controllo se il pagamento e' stato gia' riconciliato
					if(pagamento.getIdIncasso() != null) {
						Incasso incasso = incassiBD.getIncasso(pagamento.getIdIncasso());
						throw new IncassiException(FaultType.PAGAMENTO_GIA_INCASSATO, "Il pagamento [Dominio:" + pagamento.getCodDominio() + " Iuv:" + pagamento.getIuv() + " Iur:" + pagamento.getIur() + "] risuta gia' riconciliato [Sct:"+incasso.getSct()+" Trn: "+incasso.getTrn()+"].");
					}
					
					if(richiestaIncasso.getImporto().doubleValue() != pagamento.getImportoPagato().doubleValue())
						throw new IncassiException(FaultType.IMPORTO_ERRATO, "La richiesta di riconciliazione presenta un importo [" + richiestaIncasso.getImporto() + "] non corripondente a quello riscosso [" + pagamento.getImportoPagato().doubleValue() + "]");
					
					log.debug("Validazione riconciliazione per riversamento Singolo [Dominio:"+richiestaIncasso.getCodDominio()+", Iuv: "+iuv+"] completata con successo.");
				} catch (NotFoundException nfe) {
					ctx.getApplicationLogger().log("incasso.iuvNonTrovato", iuv);
					throw new IncassiException(FaultType.PAGAMENTO_NON_TROVATO, "Lo IUV " + iuv + " estratto dalla causale di incasso non identifica alcun pagamento per il creditore " + richiestaIncasso.getCodDominio());
				} catch (MultipleResultException mre) {
					ctx.getApplicationLogger().log("incasso.iuvPagamentiMultipli", iuv, richiestaIncasso.getCodDominio());
					throw new IncassiException(FaultType.PAGAMENTO_NON_IDENTIFICATO, "Lo IUV " + iuv + " estratto dalla causale di incasso identifica piu' di un pagamento per il creditore " + richiestaIncasso.getCodDominio());
				}
			}
			
			// Validazione Riversamento cumulativo
			Fr fr = null;
			if(idf != null) {
				FrBD frBD = new FrBD(incassiBD);
				frBD.setAtomica(false);
				
				try {
					log.debug("Validazione riconciliazione per riversamento cumulativo [Dominio:"+richiestaIncasso.getCodDominio()+", IdFlusso:"+idf+"]...");
					// Cerco l'idf come case insensitive
//								fr = frBD.getFr(idf, richiestaIncasso.isRicercaIdFlussoCaseInsensitive()); TODO come si gestisce ora?
					fr = frBD.getFr(idf, false);
					
					log.debug("Ricerca flusso per riversamento cumulativo [Dominio:"+richiestaIncasso.getCodDominio()+", IdFlusso:"+idf+"] completata.");
					
					// Controllo se il flusso e' stato gia' riconciliato
					if(fr.getIdIncasso() != null) {
						Incasso incasso = incassiBD.getIncasso(fr.getIdIncasso());
						throw new IncassiException(FaultType.FLUSSO_GIA_INCASSATO, "Il flusso [IdFlusso:"+idf+"] risuta gia' riconciliato [Sct:"+incasso.getSct()+" Trn:"+incasso.getTrn()+"].");
					}
					
					
					if(!fr.getStato().equals(StatoFr.ACCETTATA)) {
						ctx.getApplicationLogger().log("incasso.frAnomala", idf);
						throw new IncassiException(FaultType.FR_ANOMALA, "Il flusso di rendicontazione [IdFlusso:"+idf+"] identificato dalla causale di incasso risulta avere delle anomalie");
					}
					
					// Verifica importo pagato con l'incassato
					if(fr.getImportoTotalePagamenti().doubleValue() != richiestaIncasso.getImporto().doubleValue()) {
						ctx.getApplicationLogger().log("incasso.importoErrato", fr.getImportoTotalePagamenti().doubleValue() + "", richiestaIncasso.getImporto().doubleValue() + "");
						throw new IncassiException(FaultType.IMPORTO_ERRATO, "L'importo del flusso di redicontazione [" + richiestaIncasso.getImporto() + "] non corriponde all'importo del riversamento [" + fr.getImportoTotalePagamenti().doubleValue() + "]");
					}
					log.debug("Validazione riconciliazione per riversamento cumulativo [Dominio:"+richiestaIncasso.getCodDominio()+", IdFlusso: "+idf+"] completata con successo.");
				} catch (NotFoundException nfe) {
					ctx.getApplicationLogger().log("incasso.idfNonTrovato", idf);
					throw new IncassiException(FaultType.IDF_NON_TROVATO, "L'identificativo " + idf + " estratto dalla causale di incasso non identifica alcun flusso di rendicontazione");
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
	
	public RichiestaIncassoDTOResponse elaboraRiconciliazione(String codDomino, String idRiconciliazione, IContext ctx) throws ServiceException, GovPayException  {
		RichiestaIncassoDTOResponse richiestaIncassoResponse = new RichiestaIncassoDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		IncassiBD incassiBD = null;
		Incasso incasso = null;
		GpContext gpContext = (GpContext) ctx.getApplicationContext();
		String causale = null;
		String iuv = null;
		String idf = null;
		log.debug("Elaborazione riconciliazione [Dominio:"+codDomino+", Id: "+idRiconciliazione+"] ...");
		
		try {
			if(gpContext.getEventoCtx().getDatiPagoPA() == null) {
				gpContext.getEventoCtx().setDatiPagoPA(new DatiPagoPA());
			}
			
			incassiBD = new IncassiBD(configWrapper);
			incassiBD.setupConnection(configWrapper.getTransactionID());
			incassiBD.setAtomica(false);
			incassiBD.enableSelectForUpdate();

			try {
				log.debug("Lettura riconciliazione [Dominio:"+codDomino+", Id: "+idRiconciliazione+"] dal db...");
				incasso = incassiBD.getIncasso(codDomino, idRiconciliazione);
				richiestaIncassoResponse.setIncasso(incasso);
			} catch (NotFoundException e) {
				// non dovrebbe succedere perche' la coppia viene letta dal db
				log.warn("La riconciliazione [Dominio:"+codDomino+", Id: "+idRiconciliazione+"] non e' presente sul db: " + e.getMessage(), e);
				return richiestaIncassoResponse;
			}  catch (MultipleResultException e) {
				throw new GovPayException(e);
			}
			
			log.debug("Lettura riconciliazione [Dominio:"+codDomino+", Id: "+idRiconciliazione+"] dal db completata.");
			
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
			
			return eseguiAcquisizioneRiconciliazione(richiestaIncassoResponse, codDomino, idRiconciliazione, ctx, configWrapper, incassiBD, incasso, causale, iuv, idf, false, true);
		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (UtilsException e) {
			throw new GovPayException(e);
		} catch (IncassiException e) {
			// salvataggio stato in errore
			incasso.setStato(StatoIncasso.ERRORE);
			incasso.setDataIncasso(new Date());
			String descr = e.getMessage() + ": "  + e.getDetails();
			
			log.debug("Riconciliazione [Dominio:"+codDomino+", Id: "+idRiconciliazione+"] Iuv ["+iuv+"], IdFlusso ["+idf+"], Causale ["+causale+"] completata con errore: " + descr);
			
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
		}
	}


	private RichiestaIncassoDTOResponse eseguiAcquisizioneRiconciliazione(RichiestaIncassoDTOResponse richiestaIncassoResponse, String codDomino, String idRiconciliazione, IContext ctx, BDConfigWrapper configWrapper,
			IncassiBD incassiBD, Incasso incasso, String causale, String iuv, String idf, boolean ricercaIdFlussoCaseInsensitive, boolean salvaConUpdate)
			throws ServiceException, IncassiException, UtilsException, GovPayException {
		GpContext gpContext = (GpContext) ctx.getApplicationContext();
		
		log.debug("Riconciliazione [Dominio:"+codDomino+", Id: "+idRiconciliazione+"] Iuv ["+iuv+"], IdFlusso ["+idf+"], Causale ["+causale+"] in corso...");
		
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
				log.debug("Ricerca riconciliazione per riversamento Singolo [Dominio:"+codDomino+", Iuv: "+iuv+"]...");
				it.govpay.bd.model.Pagamento pagamento = pagamentiBD.getPagamento(incasso.getCodDominio(), iuv);
				
				if(incasso.getImporto().doubleValue() != pagamento.getImportoPagato().doubleValue())
					throw new IncassiException(FaultType.IMPORTO_ERRATO, "La richiesta di riconciliazione presenta un importo [" + incasso.getImporto() + "] non corripondente a quello riscosso [" + pagamento.getImportoPagato().doubleValue() + "]");
				
				pagamenti.add(pagamento);
				log.debug("Ricerca riconciliazione per riversamento Singolo [Dominio:"+codDomino+", Iuv: "+iuv+"] completata.");
			} catch (NotFoundException nfe) {
				ctx.getApplicationLogger().log("incasso.iuvNonTrovato", iuv);
				throw new IncassiException(FaultType.PAGAMENTO_NON_TROVATO, "Lo IUV " + iuv + " estratto dalla causale di incasso non identifica alcun pagamento per il creditore " + incasso.getCodDominio());
			} catch (MultipleResultException mre) {
				ctx.getApplicationLogger().log("incasso.iuvPagamentiMultipli", iuv, incasso.getCodDominio());
				throw new IncassiException(FaultType.PAGAMENTO_NON_IDENTIFICATO, "Lo IUV " + iuv + " estratto dalla causale di incasso identifica piu' di un pagamento per il creditore " + incasso.getCodDominio());
			}
			
			
		}
		
		// Riversamento cumulativo
		Fr fr = null;
		if(idf != null) {
			try {
				log.debug("Ricerca flusso per riversamento cumulativo [Dominio:"+codDomino+", IdFlusso: "+idf+"]...");
				// Cerco l'idf come case insensitive
				fr = frBD.getFr(idf, ricercaIdFlussoCaseInsensitive);
				richiestaIncassoResponse.setFr(fr);
				log.debug("Ricerca flusso per riversamento cumulativo [Dominio:"+codDomino+", IdFlusso: "+idf+"] completata.");
				if(!fr.getStato().equals(StatoFr.ACCETTATA)) {
					ctx.getApplicationLogger().log("incasso.frAnomala", idf);
					throw new IncassiException(FaultType.FR_ANOMALA, "Il flusso di rendicontazione " + idf + " identificato dalla causale di incasso risulta avere delle anomalie");
				}
				
				// Verifica importo pagato con l'incassato
				if(fr.getImportoTotalePagamenti().doubleValue() != incasso.getImporto().doubleValue()) {
					ctx.getApplicationLogger().log("incasso.importoErrato", fr.getImportoTotalePagamenti().doubleValue() + "", incasso.getImporto().doubleValue() + "");
					throw new IncassiException(FaultType.IMPORTO_ERRATO, "L'importo del flusso di redicontazione [" + incasso.getImporto() + "] non corriponde all'importo del riversamento [" + fr.getImportoTotalePagamenti().doubleValue() + "]");
				}
				
				Versamento versamentoBusiness = new Versamento();
				EventiBD eventiBD = new EventiBD(configWrapper);
				
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
								throw new IncassiException(FaultType.FR_ANOMALA, "Il versamento rendicontato [Dominio:" + fr.getCodDominio()+ " IUV:"+rendicontazione.getIuv()+"] non esiste.");
							} catch (EcException e2) {
								// Non deve accadere... la rendicontazione
								throw new IncassiException(FaultType.FR_ANOMALA, "Il versamento rendicontato [Dominio:" + fr.getCodDominio()+ " IUV:"+rendicontazione.getIuv()+"] non esiste.");
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
							
							pagamento.setSingoloVersamento(singoliVersamenti.get(0));
							rendicontazione.setPagamento(pagamento);
							pagamentiBD.insertPagamento(pagamento);
							rendicontazione.setIdPagamento(pagamento.getId());
								
							//Aggiorno lo stato del versamento:
							switch (singoliVersamenti.get(0).getStatoSingoloVersamento()) {
								case NON_ESEGUITO:
									versamentiBD.updateStatoSingoloVersamento(singoliVersamenti.get(0).getId(), StatoSingoloVersamento.ESEGUITO);
									versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.ESEGUITO, "Eseguito senza RPT");
									// Aggiornamento stato promemoria
									versamentiBD.updateStatoPromemoriaAvvisoVersamento(versamento.getId(), true, null);
									versamentiBD.updateStatoPromemoriaScadenzaAppIOVersamento(versamento.getId(), true, null);
									versamentiBD.updateStatoPromemoriaScadenzaMailVersamento(versamento.getId(), true, null);
									
									break;
								case ESEGUITO:
									versamento.setAnomalo(true);
									versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.ESEGUITO, "Pagamento duplicato");
									break;
							}
							
							versamentiBD.updateVersamento(versamento);
							
							Evento eventoNota = new Evento();
							eventoNota.setCategoriaEvento(CategoriaEvento.INTERNO);
							eventoNota.setRuoloEvento(RuoloEvento.CLIENT);
							eventoNota.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
							eventoNota.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
							eventoNota.setEsitoEvento(EsitoEvento.OK);
							eventoNota.setDettaglioEsito("Riconciliato flusso " + fr.getCodFlusso() + " con Pagamento senza RPT [IUV: " + rendicontazione.getIuv() + " IUR:" + rendicontazione.getIur() + "].");
							eventoNota.setTipoEvento("Pagamento eseguito senza RPT");
							eventiBD.insertEvento(eventoNota);
						} catch (MultipleResultException e) {
							ctx.getApplicationLogger().log("incasso.frAnomala", idf);
							throw new IncassiException(FaultType.FR_ANOMALA, "La rendicontazione [Dominio:"+fr.getCodDominio()+" Iuv:" + rendicontazione.getIuv()+ " Iur:" + rendicontazione.getIur() + " Indice:" + rendicontazione.getIndiceDati() + "] non identifica univocamente un pagamento");
						}
					} else {
						// Verifica che l'importo rendicontato corrisponda al pagato
						if(rendicontazione.getImporto().doubleValue() != pagamento.getImportoPagato().doubleValue())
							throw new IncassiException(FaultType.IMPORTO_ERRATO, "La rendicontazione [Dominio:"+fr.getCodDominio()+" Iuv:" + rendicontazione.getIuv()+ " Iur:" + rendicontazione.getIur() + " Indice:" + rendicontazione.getIndiceDati() + "] presenta un importo [" + rendicontazione.getImporto() + "] non corripondente a quello riscosso [" + pagamento.getImportoPagato().doubleValue() + "]");
					}
					
					//Aggiorno la FK della rendicontazione
					rendicontazione.setIdPagamento(pagamento.getId());
					rendicontazioniBD.updateRendicontazione(rendicontazione);
					
					pagamenti.add(pagamento);
				}
			} catch (NotFoundException nfe) {
				ctx.getApplicationLogger().log("incasso.idfNonTrovato", idf);
				throw new IncassiException(FaultType.IDF_NON_TROVATO, "L'identificativo " + idf + " estratto dalla causale di incasso non identifica alcun flusso di rendicontazione");
			} 
		}
		
		// Verifica stato dei pagamenti da incassare 
		for(it.govpay.bd.model.Pagamento pagamento : pagamenti) {
			if(Stato.INCASSATO.equals(pagamento.getStato())) {
				ctx.getApplicationLogger().log("incasso.pagamentoGiaIncassato", pagamento.getCodDominio(), pagamento.getIuv(), pagamento.getIur());
				throw new IncassiException(FaultType.PAGAMENTO_GIA_INCASSATO, "Uno dei pagamenti [Dominio:" + pagamento.getCodDominio() + " Iuv:" + pagamento.getIuv() + " Iur:" + pagamento.getIur() + "] risuta gia' riconciliato.");
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
			
			log.debug("Riconciliazione [Dominio:"+codDomino+", Id: "+idRiconciliazione+"] Iuv ["+iuv+"], IdFlusso ["+idf+"], Causale ["+causale+"] completata con esito OK.");
		} catch(Exception e) {
			incassiBD.rollback();
			throw new GovPayException(e);
		} finally {
			try {
				incassiBD.setAutoCommit(true);
			} catch (ServiceException e) {
				throw new GovPayException(e);
			} 
		}
		
		return richiestaIncassoResponse;
	}

	public RichiestaIncassoDTOResponse richiestaIncasso(RichiestaIncassoDTO richiestaIncasso) throws NotAuthorizedException, GovPayException, IncassiException, EcException {
		
		boolean ricercaIdFlussoCaseInsensitive = richiestaIncasso.isRicercaIdFlussoCaseInsensitive();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		IncassiBD incassiBD = null;
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
				throw new IncassiException(FaultType.DOMINIO_INESISTENTE, "Il dominio " + richiestaIncasso.getCodDominio() + " indicato nella richiesta non risulta censito in anagrafica GovPay.");
			}
			
			// Verifica IbanAccredito, se indicato
			if(richiestaIncasso.getIbanAccredito() != null)
			try {
				AnagraficaManager.getIbanAccredito(configWrapper, dominio.getId(), richiestaIncasso.getIbanAccredito());
			} catch (NotFoundException e) {
				ctx.getApplicationLogger().log("incasso.ibanInesistente", richiestaIncasso.getIbanAccredito());
				throw new IncassiException(FaultType.IBAN_INESISTENTE, "Il dominio " + richiestaIncasso.getCodDominio() + " indicato nella richiesta non risulta censito in anagrafica GovPay.");
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
				} finally {
					if(iuv == null && idf==null) {
						ctx.getApplicationLogger().log("incasso.causaleNonValida", causale);
						throw new IncassiException(FaultType.CAUSALE_NON_VALIDA, "La causale dell'operazione di incasso non e' conforme alle specifiche AgID (SACIV 1.2.1): " + causale);
					}
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
						throw new IncassiException(FaultType.DUPLICATO, "La causale inviata è diversa dall'originale [" + incasso.getCausale()+ "]");
					}
				}else {
					if(iuv != null) {
						if(!iuv.trim().equals(incasso.getIuv())) {
							ctx.getApplicationLogger().log("incasso.duplicato", "iuv");
							throw new IncassiException(FaultType.DUPLICATO, "Incasso già registrato con iuv diverso: "+incasso.getIuv()+"");
						}
					}
					
					if(idf != null) {
						if(!idf.trim().equals(incasso.getIdFlussoRendicontazione())) {
							ctx.getApplicationLogger().log("incasso.duplicato", "idf");
							throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con id flusso rendicontazione diverso: " + incasso.getIdFlussoRendicontazione());
						}
					}
				}
				if(richiestaIncasso.getSct() != null && !richiestaIncasso.getSct().equals(incasso.getSct())) {
					ctx.getApplicationLogger().log("incasso.duplicato", "sct");
					throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con sct diverso: " + incasso.getSct());
				}
				if(!richiestaIncasso.getCodDominio().equals(incasso.getCodDominio())) {
					ctx.getApplicationLogger().log("incasso.duplicato", "dominio");
					throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con dominio diverso: " + incasso.getCodDominio());
				}
				if(richiestaIncasso.getImporto().compareTo(incasso.getImporto()) != 0) {
					ctx.getApplicationLogger().log("incasso.duplicato", "importo");
					throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con importo diverso: " + incasso.getImporto());
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
			
			return eseguiAcquisizioneRiconciliazione(richiestaIncassoResponse, incasso.getCodDominio(), incasso.getIdRiconciliazione(), ctx, configWrapper, incassiBD, incasso, causale, iuv, idf, ricercaIdFlussoCaseInsensitive, false);
		} catch (ServiceException e) {
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
		}
	}
}


