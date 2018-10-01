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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.bd.pagamento.filters.IncassoFilter;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.IncassiException.FaultType;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IncassoUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoVersamento;


public class Incassi extends BasicBD {

	private static Logger log = LoggerWrapperFactory.getLogger(Incassi.class);

	public Incassi(BasicBD basicBD) {
		super(basicBD);
	}

	public RichiestaIncassoDTOResponse richiestaIncasso(RichiestaIncassoDTO richiestaIncasso) throws NotAuthorizedException, InternalException, IncassiException {
		
		try {
			GpThreadLocal.get().log("incasso.richiesta");
			
			// Validazione dati obbligatori
			
			if(richiestaIncasso.getCausale() == null) {
				GpThreadLocal.get().log("incasso.sintassi", "causale mancante");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso non e' stato specificato il campo obbligatorio causale");
			}
			
			if(richiestaIncasso.getCausale().length() > 512) {
				GpThreadLocal.get().log("incasso.sintassi", "causale troppo lunga");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso e' stato specificata una causale che eccede il massimo numero di caratteri consentiti (512)");
			}
			
			if(richiestaIncasso.getCodDominio() == null) {
				GpThreadLocal.get().log("incasso.sintassi", "dominio mancante");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso non e' stato specificato il campo obbligatorio cod_dominio");
			}
			
			if(richiestaIncasso.getImporto() == null) {
				GpThreadLocal.get().log("incasso.sintassi", "importo mancante");
				throw new IncassiException(FaultType.ERRORE_SINTASSI, "Nella richiesta di incasso non e' stato specificato il campo obbligatorio importo");
			}
			
			// Verifica Dominio
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(this, richiestaIncasso.getCodDominio());
			} catch (NotFoundException e) {
				GpThreadLocal.get().log("incasso.dominioInesistente", richiestaIncasso.getCodDominio());
				throw new IncassiException(FaultType.DOMINIO_INESISTENTE, "Il dominio " + richiestaIncasso.getCodDominio() + " indicato nella richiesta non risulta censito in anagrafica GovPay.");
			}
			
			// Verifica IbanAccredito, se indicato
			if(richiestaIncasso.getIbanAccredito() != null)
			try {
				AnagraficaManager.getIbanAccredito(this, dominio.getId(), richiestaIncasso.getIbanAccredito());
			} catch (NotFoundException e) {
				GpThreadLocal.get().log("incasso.ibanInesistente", richiestaIncasso.getIbanAccredito());
				throw new IncassiException(FaultType.IBAN_INESISTENTE, "Il dominio " + richiestaIncasso.getCodDominio() + " indicato nella richiesta non risulta censito in anagrafica GovPay.");
			}
			
			Long idApplicazione = null;
			Long idOperatore = null;
			List<Diritti> diritti = new ArrayList<>(); // TODO controllare quale diritto serve in questa fase
			diritti.add(Diritti.SCRITTURA);
			
			
			// Verifica autorizzazione all'incasso e acquisizione applicazione chiamante
			if(richiestaIncasso.getApplicazione() != null) {
				idApplicazione = richiestaIncasso.getApplicazione().getId();
				if(!AclEngine.isAuthorized(richiestaIncasso.getApplicazione().getUtenza(), Servizio.RENDICONTAZIONI_E_INCASSI, richiestaIncasso.getCodDominio(), null,diritti))
					throw new NotAuthorizedException("Utente non autorizzato al servizio di Incassi");
			} else if(richiestaIncasso.getOperatore() != null) {
				idOperatore = richiestaIncasso.getOperatore().getId();
				if(!AclEngine.isAuthorized(richiestaIncasso.getOperatore().getUtenza(),Servizio.PAGAMENTI_E_PENDENZE, richiestaIncasso.getCodDominio(), null,diritti))
					throw new NotAuthorizedException("Utente non autorizzato al servizio di Incassi");
			} else {
				throw new NotAuthorizedException("Utente non autorizzato al servizio di Incassi");
			} 
			
			
			// Validazione della causale
			String causale = richiestaIncasso.getCausale();
			String iuv = null;
			String idf = null;
			
			try {
				if(causale != null) {
					// Riversamento singolo
					iuv = IncassoUtils.getRiferimentoIncassoSingolo(causale);
					idf = IncassoUtils.getRiferimentoIncassoCumulativo(causale);
				}
			} catch (Throwable e) {
				log.error("Riscontrato errore durante il parsing della causale",e);
			} finally {
				if(iuv == null && idf==null) {
					GpThreadLocal.get().log("incasso.causaleNonValida", causale);
					throw new IncassiException(FaultType.CAUSALE_NON_VALIDA, "La causale dell'operazione di incasso non e' conforme alle specifiche AgID (SACIV 1.2.1): " + causale);
				}
			}
			
			IncassiBD incassiBD = new IncassiBD(this);
			
			// Controllo se l'idf o lo iuv sono gia' stati incassati in precedenti incassi
			IncassoFilter incassoFilter = incassiBD.newFilter();
			List<String> codDomini = new ArrayList<>();
			codDomini.add(richiestaIncasso.getCodDominio());
			incassoFilter.setCodDomini(codDomini);
			if(idf != null)
				incassoFilter.setCausale(idf);
			else
				incassoFilter.setCausale(iuv);
			List<Incasso> findAll = incassiBD.findAll(incassoFilter);
			if(findAll.size() != 0) {
				GpThreadLocal.get().log("incasso.causaleGiaIncassata", causale);
				if(idf != null)
					throw new IncassiException(FaultType.CAUSALE_GIA_INCASSATA, "Il flusso di rendicontazione [" + idf + "] indicato in causale risulta gia' incassato");
				else
					throw new IncassiException(FaultType.CAUSALE_GIA_INCASSATA, "Lo iuv [" + iuv + "] indicato in causale risulta gia' incassato");
			}
			
			// OVERRIDE TRN NUOVA GESTIONE
			richiestaIncasso.setTrn(iuv != null ? iuv : idf);
			RichiestaIncassoDTOResponse richiestaIncassoResponse = new RichiestaIncassoDTOResponse();
			
			// Controllo se il TRN dell'incasso e' gia registrato
			try {
				Incasso incasso = incassiBD.getIncasso(dominio.getCodDominio(), richiestaIncasso.getTrn());
				
				// Richiesta presente. Verifico che i dati accessori siano gli stessi
				if(!richiestaIncasso.getCausale().equals(incasso.getCausale())) {
					GpThreadLocal.get().log("incasso.sintassi", "causale");
					throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con causale diversa");
				}
				if(!richiestaIncasso.getCodDominio().equals(incasso.getCodDominio())) {
					GpThreadLocal.get().log("incasso.sintassi", "dominio");
					throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con dominio diverso");
				}
				if(richiestaIncasso.getImporto().compareTo(incasso.getImporto()) != 0) {
					GpThreadLocal.get().log("incasso.sintassi", "importo");
					throw new IncassiException(FaultType.DUPLICATO, "Incasso gia' registrato con importo diverso");
				}
				
				richiestaIncassoResponse.setIncasso(incasso);
				richiestaIncassoResponse.setCreated(false);
				return richiestaIncassoResponse;
			} catch(NotFoundException nfe) {
				// Incasso non registrato.
				richiestaIncassoResponse.setCreated(true);
			}
			
			// Sto selezionando i pagamenti per impostarli come Incassati.
			this.enableSelectForUpdate();
			this.setAutoCommit(false);
			
			List<it.govpay.bd.model.Pagamento> pagamenti = new ArrayList<>();
			
			// Riversamento singolo
			if(iuv != null) {
				PagamentiBD pagamentiBD = new PagamentiBD(this);
				try {
					it.govpay.bd.model.Pagamento pagamento = pagamentiBD.getPagamento(richiestaIncasso.getCodDominio(), iuv);
					pagamenti.add(pagamento);
				} catch (NotFoundException nfe) {
					GpThreadLocal.get().log("incasso.iuvNonTrovato", iuv);
					throw new IncassiException(FaultType.PAGAMENTO_NON_TROVATO, "Lo IUV " + iuv + " estratto dalla causale di incasso non identifica alcun pagamento per il creditore " + richiestaIncasso.getCodDominio());
				} catch (MultipleResultException mre) {
					GpThreadLocal.get().log("incasso.iuvPagamentiMultipli", iuv, richiestaIncasso.getCodDominio());
					throw new IncassiException(FaultType.PAGAMENTO_NON_IDENTIFICATO, "Lo IUV " + iuv + " estratto dalla causale di incasso identifica piu' di un pagamento per il creditore " + richiestaIncasso.getCodDominio());
				}
			}
			
			// Riversamento cumulativo
			if(idf != null) {
				FrBD frBD = new FrBD(this);
				try {
					// Cerco l'idf come case insensitive
					FrFilter newFilter = frBD.newFilter();
					newFilter.setCodFlusso(idf);
					List<Fr> frs = frBD.findAll(newFilter);
					Fr fr = null;
					for(Fr tmp : frs) {
						if(tmp.getCodFlusso().equalsIgnoreCase(idf))
							fr = tmp;
					}
					if(fr == null) throw new NotFoundException();
					
					if(!fr.getStato().equals(StatoFr.ACCETTATA)) {
						GpThreadLocal.get().log("incasso.frAnomala", idf);
						throw new IncassiException(FaultType.FR_ANOMALA, "Il flusso di rendicontazione " + idf + " identificato dalla causale di incasso risulta avere delle anomalie");
					}
					
					PagamentiBD pagamentiBD = new PagamentiBD(this);
					VersamentiBD versamentiBD = new VersamentiBD(this);
					Versamento versamentoBusiness = new Versamento(this);
					RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(this);
					
					for(Rendicontazione rendicontazione : fr.getRendicontazioni(this)) {
						if(!rendicontazione.getStato().equals(StatoRendicontazione.OK)) {
							GpThreadLocal.get().log("incasso.frAnomala", idf);
							throw new IncassiException(FaultType.FR_ANOMALA, "Il flusso di rendicontazione " + idf + " identificato dalla causale di incasso risulta avere delle anomalie");
						}
						
						it.govpay.bd.model.Pagamento pagamento = rendicontazione.getPagamento(this);
						
						
						if(pagamento == null && rendicontazione.getEsito().equals(EsitoRendicontazione.ESEGUITO_SENZA_RPT)) {
							// Incasso di un pagamento senza RPT. Controllo se il pagamento non e' stato creato nel frattempo dall'arrivo di una RT
							
							try {
								pagamento = pagamentiBD.getPagamento(fr.getCodDominio(), rendicontazione.getIuv(), rendicontazione.getIur(), rendicontazione.getIndiceDati());
								// Pagamento gia presente. 
							} catch (NotFoundException e) {
								// Pagamento non presente. Lo inserisco 
								
								it.govpay.bd.model.Versamento versamento = null;
								try {
									// Workaround per le limitazioni in select for update. Da rimuovere quando lo iuv sara nel versamento.
									this.disableSelectForUpdate();
									versamento = versamentoBusiness.chiediVersamento(null, null, null, null, fr.getCodDominio(), rendicontazione.getIuv());
									this.enableSelectForUpdate();
									versamentiBD.getVersamento(versamento.getId());
								} catch (GovPayException gpe) {
									// Non deve accadere... la rendicontazione e' ok
									throw new IncassiException(FaultType.FR_ANOMALA, "Il versamento rendicontato [Dominio:" + fr.getCodDominio()+ " IUV:"+rendicontazione.getIuv()+"] non esiste");
								}
								
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
								pagamento.setSingoloVersamento(versamento.getSingoliVersamenti(this).get(0));
								rendicontazione.setPagamento(pagamento);
								pagamentiBD.insertPagamento(pagamento);
								rendicontazione.setIdPagamento(pagamento.getId());
									
								//Aggiorno lo stato del versamento:
								switch (versamento.getSingoliVersamenti(this).get(0).getStatoSingoloVersamento()) {
									case NON_ESEGUITO:
										versamentiBD.updateStatoSingoloVersamento(versamento.getSingoliVersamenti(this).get(0).getId(), StatoSingoloVersamento.ESEGUITO);
										versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.ESEGUITO_SENZA_RPT, "Eseguito senza RPT");
										break;
									case ESEGUITO:
										versamentiBD.updateStatoSingoloVersamento(versamento.getSingoliVersamenti(this).get(0).getId(), StatoSingoloVersamento.ANOMALO); 
										versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.ANOMALO, "Pagamento duplicato");
										break;
									case ANOMALO:	
										break;
								}
							} catch (MultipleResultException e) {
								GpThreadLocal.get().log("incasso.frAnomala", idf);
								throw new IncassiException(FaultType.FR_ANOMALA, "La rendicontazione [Dominio:"+fr.getCodDominio()+" Iuv:" + rendicontazione.getIuv()+ " Iur:" + rendicontazione.getIur() + " Indice:" + rendicontazione.getIndiceDati() + "] non identifica univocamente un pagamento");
							}
						}
						
						//Aggiorno la FK della rendicontazione
						rendicontazione.setIdPagamento(pagamento.getId());
						rendicontazioniBD.updateRendicontazione(rendicontazione);
						
						pagamenti.add(pagamento);
					}
				} catch (NotFoundException nfe) {
					GpThreadLocal.get().log("incasso.idfNonTrovato", idf);
					throw new IncassiException(FaultType.IDF_NON_TROVATO, "L'identificativo " + idf + " estratto dalla causale di incasso non identifica alcun flusso di rendicontazione");
				} 
			}
			
			// Verifica stato dei pagamenti da incassare e calcolo dell'importo pagato
			BigDecimal totalePagato = BigDecimal.ZERO;
			for(it.govpay.bd.model.Pagamento pagamento : pagamenti) {
				if(Stato.INCASSATO.equals(pagamento.getStato())) {
					GpThreadLocal.get().log("incasso.pagamentoGiaIncassato", pagamento.getCodDominio(), pagamento.getIuv(), pagamento.getIur());
					throw new IncassiException(FaultType.PAGAMENTO_GIA_INCASSATO, "Uno dei pagamenti incassati [Dominio:" + pagamento.getCodDominio() + " Iuv:" + pagamento.getIuv() + " Iur:" + pagamento.getIur() + "] risuta gia' incassato.");
				}
				totalePagato = totalePagato.add(pagamento.getImportoPagato());
			}
			
			// Verifica importo pagato con l'incassato
			if(totalePagato.doubleValue() != richiestaIncasso.getImporto().doubleValue()) {
				GpThreadLocal.get().log("incasso.importoErrato", totalePagato.doubleValue() + "", richiestaIncasso.getImporto().doubleValue() + "");
				throw new IncassiException(FaultType.IMPORTO_ERRATO, "L'importo incassato [" + richiestaIncasso.getImporto() + "] non corriponde alla somma dei pagamenti [" + totalePagato.doubleValue() + "]");
			}
			
			// Inserisco l'incasso e aggiorno lo stato dei pagamenti
			try {
				it.govpay.bd.model.Incasso incasso = new it.govpay.bd.model.Incasso();
				incasso.setCausale(richiestaIncasso.getCausale());
				incasso.setCodDominio(richiestaIncasso.getCodDominio());
				incasso.setDataIncasso(new Date());
				incasso.setDataValuta(richiestaIncasso.getDataValuta());
				incasso.setDataContabile(richiestaIncasso.getDataContabile());
				incasso.setDispositivo(richiestaIncasso.getDispositivo());
				incasso.setImporto(richiestaIncasso.getImporto());
				incasso.setTrn(richiestaIncasso.getTrn());
				incasso.setIbanAccredito(richiestaIncasso.getIbanAccredito());
				incasso.setIdApplicazione(idApplicazione);
				incasso.setIdOperatore(idOperatore); 
				richiestaIncassoResponse.setIncasso(incasso);
				incassiBD.insertIncasso(incasso);
				
				PagamentiBD pagamentiBD = new PagamentiBD(this);
				for(it.govpay.bd.model.Pagamento pagamento : pagamenti) {
					pagamento.setStato(Stato.INCASSATO);
					pagamento.setIncasso(incasso);
					pagamentiBD.updatePagamento(pagamento);
				}
				this.commit();
			} catch(Exception e) {
				this.rollback();
				throw new InternalException(e);
			} finally {
				this.setAutoCommit(true);
			}
			
			return richiestaIncassoResponse;
		} catch (ServiceException e) {
			throw new InternalException(e);
		} finally {
			try {
				this.disableSelectForUpdate();
			} catch (ServiceException e) {}
		}
	}

	public ListaIncassiDTOResponse listaIncassi(ListaIncassiDTO listaIncassoDTO) throws NotAuthorizedException, ServiceException {
		List<String> domini = null;
		List<Diritti> diritti = new ArrayList<>(); 
		diritti.add(Diritti.LETTURA);
		domini = AclEngine.getDominiAutorizzati((Utenza) listaIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, diritti);
		if(domini == null) {
			throw new NotAuthorizedException("L'utente autenticato non e' autorizzato ai servizi " + Servizio.RENDICONTAZIONI_E_INCASSI + " per alcun dominio");
		}
		
		IncassiBD incassiBD = new IncassiBD(this);
		IncassoFilter newFilter = incassiBD.newFilter();
		if(domini != null)
			newFilter.setCodDomini(new ArrayList<>(domini));
		newFilter.setDataInizio(listaIncassoDTO.getInizio());
		newFilter.setDataFine(listaIncassoDTO.getFine());
		newFilter.setOffset(listaIncassoDTO.getOffset());
		newFilter.setLimit(listaIncassoDTO.getLimit());
		
		FilterSortWrapper fsw = new FilterSortWrapper();
		fsw.setField(it.govpay.orm.Incasso.model().DATA_ORA_INCASSO);
		fsw.setSortOrder(SortOrder.DESC);
		newFilter.getFilterSortList().add(fsw);

		List<Incasso> findAll = incassiBD.findAll(newFilter);
		long count = incassiBD.count(newFilter);
		
		ListaIncassiDTOResponse response = new ListaIncassiDTOResponse(count, findAll);
		return response;
	}
	
	public LeggiIncassoDTOResponse leggiIncasso(LeggiIncassoDTO leggiIncassoDTO) throws NotFoundException,NotAuthorizedException, ServiceException {
		IncassiBD incassiBD = new IncassiBD(this);
		try {
			Incasso incasso = incassiBD.getIncasso(leggiIncassoDTO.getIdDominio(), leggiIncassoDTO.getIdIncasso());
			
			List<Diritti> diritti = new ArrayList<>();
			diritti.add(Diritti.LETTURA);
			List<String> domini = AclEngine.getDominiAutorizzati((Utenza) leggiIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, diritti);
			if(domini == null || (domini.size() > 0 && !domini.contains(incasso.getCodDominio()))) {
				throw new NotAuthorizedException("L'utente autenticato non e' autorizzato ai servizi " + Servizio.RENDICONTAZIONI_E_INCASSI + " per il dominio " + incasso.getCodDominio());
			}
			LeggiIncassoDTOResponse response = new LeggiIncassoDTOResponse();
			response.setIncasso(incasso);
			return response;
		} catch (NotFoundException e) {
			throw e;
		}
	}
}


