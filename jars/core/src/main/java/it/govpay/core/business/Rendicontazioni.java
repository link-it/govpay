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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.core.Role;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.rpt.NodoChiediElencoFlussiRendicontazione;
import gov.telematici.pagamenti.ws.rpt.NodoChiediElencoFlussiRendicontazioneRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoChiediFlussoRendicontazione;
import gov.telematici.pagamenti.ws.rpt.NodoChiediFlussoRendicontazioneRisposta;
import gov.telematici.pagamenti.ws.rpt.TipoIdRendicontazione;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtDatiSingoliPagamenti;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.FlussoRiversamento;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.EventoContext;
import it.govpay.core.utils.EventoContext.Categoria;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Intermediario;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.Versamento.TipologiaTipoVersamento;


public class Rendicontazioni {

	public class DownloadRendicontazioniResponse {
		
		private List<RendicontazioneScaricata> rndDwn;
		private String descrizioneEsito;
		
		public DownloadRendicontazioniResponse() {
			rndDwn = new ArrayList<RendicontazioneScaricata>();
		}
		
		public List<RendicontazioneScaricata> getRndDwn() {
			return rndDwn;
		}
		public void setRndDwn(List<RendicontazioneScaricata> rndDwn) {
			this.rndDwn = rndDwn;
		}
		public String getDescrizioneEsito() {
			return descrizioneEsito;
		}
		public void setDescrizioneEsito(String descrizioneEsito) {
			this.descrizioneEsito = descrizioneEsito;
		}
	}

	public class RendicontazioneScaricata {
		
		private String idFlusso;
		private List<String> errori;
		
		public RendicontazioneScaricata() {
			setErrori(new ArrayList<String>());
		}

		public String getIdFlusso() {
			return idFlusso;
		}

		public void setIdFlusso(String idFlusso) {
			this.idFlusso = idFlusso;
		}

		public List<String> getErrori() {
			return errori;
		}

		public void setErrori(List<String> errori) {
			this.errori = errori;
		}
		
	}

	private static Logger log = LoggerWrapperFactory.getLogger(Rendicontazioni.class);

	public Rendicontazioni() {
	}

	public DownloadRendicontazioniResponse downloadRendicontazioni(IContext ctx, boolean deep) throws GovPayException, UtilsException { 
		int frAcquisiti = 0;
		int frNonAcquisiti = 0;
		DownloadRendicontazioniResponse response = new DownloadRendicontazioniResponse();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			ctx.getApplicationLogger().log("rendicontazioni.acquisizione");
			DominiBD dominiBD = new DominiBD(ctx.getTransactionId());
			Giornale giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();

			StazioniBD stazioniBD = new StazioniBD(configWrapper);
			List<Stazione> lstStazioni = stazioniBD.getStazioni();

			for(Stazione stazione : lstStazioni) {
				List<TipoIdRendicontazione> flussiDaAcquisire = new ArrayList<>();

				Intermediario intermediario = stazione.getIntermediario(configWrapper);

				if(deep) {
					DominioFilter filter = dominiBD.newFilter();
					filter.setCodStazione(stazione.getCodStazione());
					List<Dominio> lstDomini = dominiBD.findAll(filter);

					for(Dominio dominio : lstDomini) { 
						log.debug("Acquisizione dei flussi di rendicontazione per il dominio [" + dominio.getCodDominio() + "] in corso.");
						flussiDaAcquisire.addAll(this.chiediListaFr(stazione, dominio, giornale));
					}
				} else {
					log.debug("Acquisizione dei flussi di rendicontazione per la stazione [" + stazione.getCodStazione() + "] in corso.");
					flussiDaAcquisire.addAll(this.chiediListaFr(stazione, null, giornale));
				}

				// In questo momento la connessione al db e' chiusa
				
				// Scarto i flussi gia acquisiti ed eventuali doppioni scaricati
				FrBD frBD = new FrBD(configWrapper);
				Set<String> idfs = new HashSet<>();
				Set<String> idFsDaSvecchiare = new HashSet<>();
				for(TipoIdRendicontazione idRendicontazione : flussiDaAcquisire) {
					if(frBD.exists(idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso()) 
							|| idfs.contains(idRendicontazione.getIdentificativoFlusso())) {
						
					//	flussiDaAcquisire.remove(idRendicontazione);
						// aggiungo l'id da svecchiare
						if(!idFsDaSvecchiare.contains(idRendicontazione.getIdentificativoFlusso())) {
							idFsDaSvecchiare.add(idRendicontazione.getIdentificativoFlusso());
						}
						
						ctx.getApplicationLogger().log("rendicontazioni.flussoDuplicato",  idRendicontazione.getIdentificativoFlusso());
						log.trace("Flusso rendicontazione gia' presente negli archivi: " + idRendicontazione.getIdentificativoFlusso() + "");
						
						// Emissione Evento Flusso duplicato viene fatta durante l'aggiornamento dello stato obsoleto, cosi da collegare l'id long del flusso all'evento.
					}
					
					// aggiungo gli id non ancora acquisiti
					if(!idfs.contains(idRendicontazione.getIdentificativoFlusso())) {
						idfs.add(idRendicontazione.getIdentificativoFlusso());
					}
				}
				
				for(int i =0; i < flussiDaAcquisire.size(); i++) {
					TipoIdRendicontazione idRendicontazione = flussiDaAcquisire.get(i);
					if(idFsDaSvecchiare.contains(idRendicontazione.getIdentificativoFlusso())) {
						flussiDaAcquisire.remove(i);
					}
				}

				List<EventoContext> eventiFlussiDuplicati = new ArrayList<EventoContext>();
				boolean acquisizioneOk = true;
				for(TipoIdRendicontazione idRendicontazione : flussiDaAcquisire) {
					log.debug("Acquisizione flusso di rendicontazione " + idRendicontazione.getIdentificativoFlusso());
					acquisizioneOk = true;
					
					RendicontazioneScaricata rnd = new RendicontazioneScaricata();
					response.rndDwn.add(rnd);
					
					rnd.setIdFlusso(idRendicontazione.getIdentificativoFlusso());
					
					boolean hasFrAnomalia = false;
					NodoClient chiediFlussoRendicontazioneClient = null;
					try {
						appContext.setupNodoClient(stazione.getCodStazione(), null, Azione.nodoChiediFlussoRendicontazione);
						appContext.getRequest().addGenericProperty(new Property("codStazione", stazione.getCodStazione()));
						appContext.getRequest().addGenericProperty(new Property("idFlusso", idRendicontazione.getIdentificativoFlusso()));
						
						NodoChiediFlussoRendicontazione richiestaFlusso = new NodoChiediFlussoRendicontazione();
						richiestaFlusso.setIdentificativoIntermediarioPA(stazione.getIntermediario(configWrapper).getCodIntermediario());
						richiestaFlusso.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
						richiestaFlusso.setPassword(stazione.getPassword());
						richiestaFlusso.setIdentificativoFlusso(idRendicontazione.getIdentificativoFlusso());

						NodoChiediFlussoRendicontazioneRisposta risposta;
						
						try {
							chiediFlussoRendicontazioneClient = new NodoClient(intermediario, null, giornale);
							popolaDatiPagoPAEvento(chiediFlussoRendicontazioneClient.getEventoCtx(), intermediario, stazione, null, idRendicontazione.getIdentificativoFlusso());
							risposta = chiediFlussoRendicontazioneClient.nodoChiediFlussoRendicontazione(richiestaFlusso, stazione.getIntermediario(configWrapper).getDenominazione());
							chiediFlussoRendicontazioneClient.getEventoCtx().setEsito(Esito.OK);
						} catch (Exception e) {
							if(chiediFlussoRendicontazioneClient != null) {
								if(e instanceof GovPayException) {
									chiediFlussoRendicontazioneClient.getEventoCtx().setSottotipoEsito(((GovPayException)e).getCodEsito().toString());
								} else if(e instanceof ClientException) {
									chiediFlussoRendicontazioneClient.getEventoCtx().setSottotipoEsito(((ClientException)e).getResponseCode() + "");
								} else {
									chiediFlussoRendicontazioneClient.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
								}
								chiediFlussoRendicontazioneClient.getEventoCtx().setEsito(Esito.FAIL);
								chiediFlussoRendicontazioneClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
								chiediFlussoRendicontazioneClient.getEventoCtx().setException(e);
							}
							// Errore nella richiesta. Loggo e continuo con il prossimo flusso
							rnd.getErrori().add("Richiesta al nodo fallita: " + e + ".");
							log.error("Richiesta flusso rendicontazione [" + idRendicontazione.getIdentificativoFlusso() + "] fallita: " + e);
							ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", e.getMessage());
							continue;
						} 

						if(risposta.getFault() != null) {
							// Errore nella richiesta. Loggo e continuo con il prossimo flusso
							rnd.getErrori().add("Richiesta al nodo fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString() + ".");
							log.error("Richiesta flusso rendicontazione [" + idRendicontazione.getIdentificativoFlusso() + "] fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
							ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoKo", risposta.getFault().getFaultCode(), risposta.getFault().getFaultString(), risposta.getFault().getDescription());
							if(chiediFlussoRendicontazioneClient != null) {
								chiediFlussoRendicontazioneClient.getEventoCtx().setSottotipoEsito(risposta.getFault().getFaultCode());
								chiediFlussoRendicontazioneClient.getEventoCtx().setEsito(Esito.KO);
								chiediFlussoRendicontazioneClient.getEventoCtx().setDescrizioneEsito(risposta.getFault().getFaultString());
							}
						} else {
							byte[] tracciato = null;
							try {
								ByteArrayOutputStream output = new ByteArrayOutputStream();
								DataHandler dh = risposta.getXmlRendicontazione();
								dh.writeTo(output);
								tracciato = output.toByteArray();
							} catch (IOException e) {
								rnd.getErrori().add("Lettura del flusso fallita: " + e + ".");
								log.error("Errore durante la lettura del flusso di rendicontazione", e);
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Lettura del flusso fallita: " + e);
								continue;
							}

							FlussoRiversamento flussoRendicontazione = null;
							try {
								flussoRendicontazione = JaxbUtils.toFR(tracciato);
							} catch (Exception e) {
								rnd.getErrori().add("Parsing del flusso fallita: " + e + ".");
								log.error("Errore durante il parsing del flusso di rendicontazione", e);
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Errore durante il parsing del flusso di rendicontazione: " + e);
								continue;
							}

							log.info("Ricevuto flusso rendicontazione per " + flussoRendicontazione.getDatiSingoliPagamentis().size() + " singoli pagamenti");

							ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlusso");
							appContext.getRequest().addGenericProperty(new Property("trn", flussoRendicontazione.getIdentificativoUnivocoRegolamento()));
							
							Fr fr = new Fr();
							fr.setObsoleto(false);
							fr.setCodBicRiversamento(flussoRendicontazione.getCodiceBicBancaDiRiversamento());
							fr.setCodFlusso(idRendicontazione.getIdentificativoFlusso());
							fr.setIur(flussoRendicontazione.getIdentificativoUnivocoRegolamento());
							fr.setDataAcquisizione(new Date());
							fr.setDataFlusso(flussoRendicontazione.getDataOraFlusso());
							fr.setDataRegolamento(flussoRendicontazione.getDataRegolamento());
							fr.setNumeroPagamenti(flussoRendicontazione.getNumeroTotalePagamenti().longValue());
							fr.setImportoTotalePagamenti(flussoRendicontazione.getImportoTotalePagamenti());

							fr.setXml(tracciato);

							String codPsp = null, codDominio = null;
							String ragioneSocialePsp = null, ragioneSocialeDominio = null; 
							codPsp = idRendicontazione.getIdentificativoFlusso().substring(10, idRendicontazione.getIdentificativoFlusso().indexOf("-", 10));
							try {
								ragioneSocialePsp = flussoRendicontazione.getIstitutoMittente().getDenominazioneMittente();
							}catch (Exception e) {
							}
							fr.setCodPsp(codPsp);
							fr.setRagioneSocialePsp(ragioneSocialePsp);
							log.debug("Identificativo PSP estratto dall'identificativo flusso: " + codPsp);
							appContext.getRequest().addGenericProperty(new Property("codPsp", codPsp));

							Dominio dominio = null;
							try {
								codDominio = flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco();
								ragioneSocialeDominio = flussoRendicontazione.getIstitutoRicevente().getDenominazioneRicevente();
								fr.setCodDominio(codDominio);
								fr.setRagioneSocialeDominio(ragioneSocialeDominio);
								appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
								dominio = AnagraficaManager.getDominio(configWrapper, codDominio);	
							} catch (Exception e) {
								if(codDominio == null) {
									codDominio = "????";
									appContext.getRequest().addGenericProperty(new Property("codDominio", "null"));
								}
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoDominioNonCensito");
							}

							BigDecimal totaleImportiRendicontati = BigDecimal.ZERO;

							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
							for(CtDatiSingoliPagamenti dsp : flussoRendicontazione.getDatiSingoliPagamentis()) {

								String iur = dsp.getIdentificativoUnivocoRiscossione();
								String iuv = dsp.getIdentificativoUnivocoVersamento();
								Integer indiceDati = dsp.getIndiceDatiSingoloPagamento();
								BigDecimal importoRendicontato = dsp.getSingoloImportoPagato();

								log.debug("Rendicontato (Esito " + dsp.getCodiceEsitoSingoloPagamento() + ") per un importo di (" + dsp.getSingoloImportoPagato() + ") [CodDominio: " + codDominio + "] [Iuv: "+ dsp.getIdentificativoUnivocoVersamento() + "][Iur: " + iur + "]");

								it.govpay.bd.model.Rendicontazione rendicontazione = new it.govpay.bd.model.Rendicontazione();

								// Gestisco un codice esito non supportato
								try {
									rendicontazione.setEsito(EsitoRendicontazione.toEnum(dsp.getCodiceEsitoSingoloPagamento()));
								} catch (Exception e) {
									ctx.getApplicationLogger().log("rendicontazioni.esitoSconosciuto", iuv, iur, dsp.getCodiceEsitoSingoloPagamento() == null ? "null" : dsp.getCodiceEsitoSingoloPagamento());
									rendicontazione.addAnomalia("007110", "Codice esito [" + dsp.getCodiceEsitoSingoloPagamento() + "] sconosciuto");
								}

								rendicontazione.setData(dsp.getDataEsitoSingoloPagamento());
								rendicontazione.setIur(dsp.getIdentificativoUnivocoRiscossione());
								rendicontazione.setIuv(dsp.getIdentificativoUnivocoVersamento());
								rendicontazione.setImporto(dsp.getSingoloImportoPagato());
								rendicontazione.setIndiceDati(indiceDati);
								
								totaleImportiRendicontati = totaleImportiRendicontati.add(importoRendicontato);	

								// Cerco il pagamento riferito
								it.govpay.bd.model.Pagamento pagamento = null;
								try {
									pagamento = pagamentiBD.getPagamento(codDominio, iuv, iur, indiceDati); 

									// Pagamento trovato. Faccio i controlli semantici
									rendicontazione.setIdPagamento(pagamento.getId());
									
									// imposto l'id singolo versamento
//									SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(pagamentiBD);
									rendicontazione.setIdSingoloVersamento(pagamento.getIdSingoloVersamento());

									// Verifico l'importo
									if(rendicontazione.getEsito().equals(EsitoRendicontazione.REVOCATO)) {
										if(pagamento.getImportoRevocato().compareTo(importoRendicontato.abs()) != 0) {
											ctx.getApplicationLogger().log("rendicontazioni.importoStornoErrato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
											log.info("Revoca [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: l'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto stornato [" + pagamento.getImportoRevocato().doubleValue() + "]");
											rendicontazione.addAnomalia("007112", "L'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto stornato [" + pagamento.getImportoRevocato().doubleValue() + "]");
										}

									} else {
										if(pagamento.getImportoPagato().compareTo(importoRendicontato) != 0) {
											ctx.getApplicationLogger().log("rendicontazioni.importoErrato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
											log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: l'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto pagato [" + pagamento.getImportoPagato().doubleValue() + "]");
											rendicontazione.addAnomalia("007104", "L'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto pagato [" + pagamento.getImportoPagato().doubleValue() + "]");
										}
									}
								} catch (NotFoundException e) {
									// Pagamento non trovato. Devo capire se ce' un errore.

									// Controllo che sia per uno IUV generato da noi
									if(!this.isInterno(dominio, iuv)) {
										rendicontazione.setStato(StatoRendicontazione.ALTRO_INTERMEDIARIO);
										continue;
									}

									// Controllo se e' un pagamento senza RPT
									if(rendicontazione.getEsito().equals(EsitoRendicontazione.ESEGUITO_SENZA_RPT)) {

										//Recupero il versamento, internamente o dall'applicazione esterna
										it.govpay.bd.model.Versamento versamento = null;
										String erroreVerifica = null;
										String codApplicazione = null;
										try {
											try {
												versamento = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv);
											} catch (NotFoundException nfe) {
												// Non e' su sistema. Individuo l'applicativo gestore
												Applicazione applicazioneDominio = new it.govpay.core.business.Applicazione().getApplicazioneDominio(configWrapper, dominio, iuv,false);
												if(applicazioneDominio != null) {
													codApplicazione = applicazioneDominio.getCodApplicazione();
													versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(configWrapper, codApplicazione), null, null, null, codDominio, iuv, TipologiaTipoVersamento.DOVUTO);
												}
											}
										} catch (VersamentoScadutoException e1) {
											erroreVerifica = "Versamento non acquisito dall'applicazione gestrice perche' SCADUTO.";
										} catch (VersamentoAnnullatoException e1) {
											erroreVerifica = "Versamento non acquisito dall'applicazione gestrice perche' ANNULLATO.";
										} catch (VersamentoDuplicatoException e1) {
											erroreVerifica = "Versamento non acquisito dall'applicazione gestrice perche' DUPLICATO.";
										} catch (VersamentoSconosciutoException e1) {
											erroreVerifica = "Versamento non acquisito dall'applicazione gestrice perche' SCONOSCIUTO.";
										} catch (ClientException ce) {
											rnd.getErrori().add("Acquisizione flusso fallita. Riscontrato errore nell'acquisizione del versamento dall'applicazione gestrice [Transazione: " + ctx.getTransactionId() + "].");
											log.error("Errore durante il processamento del flusso di Rendicontazione [Flusso:" + idRendicontazione.getIdentificativoFlusso() + "]: impossibile acquisire i dati del versamento [Dominio:" + codDominio+ " Iuv:" + iuv + "]. Flusso non acquisito.");
											ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoKo", idRendicontazione.getIdentificativoFlusso(), "Impossibile acquisire i dati di un versamento dall'applicativo gestore [Applicazione:" + codApplicazione + " Dominio:" + codDominio+ " Iuv:" + iuv + "].  Flusso non acquisito.");
											throw new GovPayException(ce);
										}

										if(versamento == null) {
											// non ho trovato il versamento 
											ctx.getApplicationLogger().log("rendicontazioni.senzaRptNoVersamento", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
											log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.");
											rendicontazione.addAnomalia("007111", "Il versamento risulta sconosciuto: " + erroreVerifica);
											continue;
										} else {
											
											List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
											if(singoliVersamenti.size() != 1) {
												// Un pagamento senza rpt DEVE riferire un pagamento tipo 3 con un solo singolo versamento
												ctx.getApplicationLogger().log("rendicontazioni.senzaRptVersamentoMalformato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
												log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.");
												rendicontazione.addAnomalia("007114", "Il versamento presenta piu' singoli versamenti");
												continue;
											}
											
											rendicontazione.setIdSingoloVersamento(singoliVersamenti.get(0).getId());
											
											continue;
										}
									}

									ctx.getApplicationLogger().log("rendicontazioni.noPagamento", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
									log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: il pagamento non risulta presente in base dati.");
									rendicontazione.addAnomalia("007101", "Il pagamento riferito dalla rendicontazione non risulta presente in base dati.");
									fr.addAnomalia("007101", "Il pagamento riferito dalla rendicontazione non risulta presente in base dati.");
									continue;
								} catch (MultipleResultException e) {
									// Individuati piu' pagamenti riferiti dalla rendicontazione
									ctx.getApplicationLogger().log("rendicontazioni.poliPagamento", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
									log.info("Pagamento rendicontato duplicato: [Dominio:" + codDominio + " Iuv:"+ iuv + " Iur:" + iur + " Indice:" + indiceDati + "]");
									rendicontazione.addAnomalia("007102", "La rendicontazione riferisce piu di un pagamento gestito.");
									fr.addAnomalia("007102", "La rendicontazione riferisce piu di un pagamento gestito.");
								} finally {
									if(!StatoRendicontazione.ALTRO_INTERMEDIARIO.equals(rendicontazione.getStato()) && rendicontazione.getAnomalie().isEmpty()) {
										rendicontazione.setStato(StatoRendicontazione.OK);
									} else if(!StatoRendicontazione.ALTRO_INTERMEDIARIO.equals(rendicontazione.getStato()) && !rendicontazione.getAnomalie().isEmpty()) {
										rendicontazione.setStato(StatoRendicontazione.ANOMALA);
										hasFrAnomalia = true;
									}
									fr.addRendicontazione(rendicontazione);
								}
							}
							
							
							// Singole rendicontazioni elaborate.
							// Controlli di quadratura generali

							if(totaleImportiRendicontati.compareTo(flussoRendicontazione.getImportoTotalePagamenti()) != 0){
								ctx.getApplicationLogger().log("rendicontazioni.importoTotaleErrato");
								log.info("La somma degli importi rendicontati ["+totaleImportiRendicontati+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getImportoTotalePagamenti()  + "]");
								fr.addAnomalia("007106", "La somma degli importi rendicontati ["+totaleImportiRendicontati+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getImportoTotalePagamenti()  + "]");
							}

							try {
								if(flussoRendicontazione.getDatiSingoliPagamentis().size() != flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()) {
									ctx.getApplicationLogger().log("rendicontazioni.numeroRendicontazioniErrato");
									log.info("Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamentis().size()+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()  + "]");
									fr.addAnomalia("007107", "Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamentis().size()+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()  + "]");
								}	
							} catch (Exception e) {
								ctx.getApplicationLogger().log("rendicontazioni.numeroRendicontazioniErrato");
								log.info("Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamentis().size()+"] non corrisponde al totale indicato nella testata del flusso [????]");
								fr.addAnomalia("007107", "Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamentis().size()+"] non corrisponde al totale indicato nella testata del flusso [????]");
							}

							// Decido lo stato del FR
							if(fr.getAnomalie().isEmpty()) {
								fr.setStato(StatoFr.ACCETTATA);
							} else {
								fr.setStato(StatoFr.ANOMALA);
								hasFrAnomalia = true;
							}
								
							
							// Procedo al salvataggio
							RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(configWrapper);
							
							// Tutte le operazioni di salvataggio devono essere in transazione.
							try {
								rendicontazioniBD.setupConnection(configWrapper.getTransactionID());
								
								rendicontazioniBD.setAutoCommit(false);
								
								rendicontazioniBD.setAtomica(false);
								
								frBD = new FrBD(rendicontazioniBD);
								
								frBD.setAtomica(false);
								
								// aggiorno lo stato di eventuali flussi precedenti
								if(idFsDaSvecchiare.contains(fr.getCodFlusso())) {
									List<Long> idsFlussi = frBD.getIdsFlusso(fr.getCodFlusso());
									
									for (Long idFlussoLong : idsFlussi) {
										
										frBD.updateObsoleto(idFlussoLong, true);
										
										EventoContext eventoContext = GiornaleEventi.creaEventoContext(Categoria.INTERFACCIA, Role.CLIENT);
										eventoContext.setComponente(Componente.API_PAGOPA);
										popolaDatiPagoPAEvento(eventoContext, intermediario, stazione, null, idRendicontazione.getIdentificativoFlusso());
										eventoContext.setEsito(Esito.OK);
										eventoContext.setIdFr(idFlussoLong);
										eventoContext.setTipoEvento(Azione.nodoChiediFlussoRendicontazione.toString());
										eventoContext.setSottotipoEvento(EventoContext.APIPAGOPA_SOTTOTIPOEVENTO_FLUSSO_RENDICONTAZIONE_DUPLICATO);
										eventiFlussiDuplicati.add(eventoContext);
									}
								}
								
								frBD.insertFr(fr);
								
								frAcquisiti++;
								for(Rendicontazione r : fr.getRendicontazioni()) {
									r.setIdFr(fr.getId());
									rendicontazioniBD.insert(r);
								}
								rendicontazioniBD.commit();
							}catch (ServiceException | NotFoundException e) {
								if(!rendicontazioniBD.isAutoCommit())
									rendicontazioniBD.rollback();
								
								throw e;
							} finally {
								rendicontazioniBD.closeConnection();
							}
							
							if(chiediFlussoRendicontazioneClient != null) {
								chiediFlussoRendicontazioneClient.getEventoCtx().setIdFr(fr.getId());
							}
							if(!hasFrAnomalia) {
								log.info("Flusso di rendicontazione acquisito senza anomalie.");
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoOk");
							} else {
								log.info("Flusso di rendicontazione acquisito con anomalie.");
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoOkAnomalia");
							}
						}
					} catch (GovPayException ce) {
						log.error("Flusso di rendicontazione non acquisito", ce);
						frNonAcquisiti++;
						acquisizioneOk = false;
					} finally {
						if(chiediFlussoRendicontazioneClient != null && chiediFlussoRendicontazioneClient.getEventoCtx().isRegistraEvento()) {
							EventiBD eventiBD = new EventiBD(configWrapper);
							eventiBD.insertEvento(chiediFlussoRendicontazioneClient.getEventoCtx().toEventoDTO());
						}
						
						if(acquisizioneOk) {
							if(eventiFlussiDuplicati != null && eventiFlussiDuplicati.size() > 0) {
								EventiBD eventiBD = new EventiBD(configWrapper);
								for (EventoContext context : eventiFlussiDuplicati) {
									eventiBD.insertEvento(context.toEventoDTO());
								}
							}
						}
					}
				}
			}
		} catch(Exception e) {
			ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiFail", e.getMessage());
			response.descrizioneEsito="Impossibile acquisire i flussi: " + e.getMessage();
			throw new GovPayException(e);
		} finally {
		}
		response.descrizioneEsito = "Operazione completata: " + frAcquisiti + " flussi acquisiti";
		if(frNonAcquisiti > 0) response.descrizioneEsito += " e " + frNonAcquisiti + "non acquisiti per errori";
		ctx.getApplicationLogger().log("rendicontazioni.acquisizioneOk");
		
		return response;
	}


	private boolean isInterno(Dominio dominio, String iuv) {

		if(dominio == null) {
			// Se il dominio non e' censito, allora sicuramente non e' interno
			return false;
		}

		boolean isNumerico;

		try {
			new BigInteger(iuv);
			isNumerico = true;
		} catch (Exception e) {
			isNumerico = false;
		}

		if(dominio.getAuxDigit() == 0) {
			// AuxDigit 0: Ente monointermediato. 
			// Per i pagamenti di tipo 1 e 2, se non ho trovato il pagamento e sono arrivato qui, posso assumere che non e' interno.
			// Per i pagamenti di tipo 3, e' mio se e' di 15 cifre.
			// Quindi controllo solo se e' numerico e di 15 cifre.

			if(isNumerico && iuv.length() == 15)
				return true;
		}

		if(dominio.getAuxDigit() == 3) {
			// AuxDigit 3: Ente plurintermediato.
			// 
			// Gli IUV generati da GovPay sono nelle forme:
			// RF <check digit (2n)><codice segregazione (2n)><codice alfanumerico (max 19)>
			// <codice segregazione (2n)><IUV base (max 13n)><IUV check digit (2n)>

			// Pagamenti tipo 1 e 2 operati da GovPay
			if(iuv.startsWith("RF") && iuv.substring(4, 6).equals(String.format("%02d", dominio.getSegregationCode())))
				return true;

			// Pagamenti tipo 3
			if(isNumerico && iuv.length() == 17 && iuv.startsWith(String.format("%02d", dominio.getSegregationCode())))
				return true;
		}
		return false;
	}

	private List<TipoIdRendicontazione> chiediListaFr(Stazione stazione, Dominio dominio, Giornale giornale) throws UtilsException{ 
		List<TipoIdRendicontazione> flussiDaAcquisire = new ArrayList<>();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		NodoClient chiediFlussoRendicontazioniClient = null;
		try {
			appContext.setupNodoClient(stazione.getCodStazione(), dominio != null ? dominio.getCodDominio() : null, Azione.nodoChiediElencoFlussiRendicontazione);
			appContext.getRequest().addGenericProperty(new Property("codDominio", dominio != null ? dominio.getCodDominio() : "-"));
			ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussi");

			NodoChiediElencoFlussiRendicontazione richiesta = new NodoChiediElencoFlussiRendicontazione();
			if(dominio != null) richiesta.setIdentificativoDominio(dominio.getCodDominio());
			richiesta.setIdentificativoIntermediarioPA(stazione.getIntermediario(configWrapper).getCodIntermediario());
			richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione()); 
			richiesta.setPassword(stazione.getPassword());

			NodoChiediElencoFlussiRendicontazioneRisposta risposta;
			try {
				Intermediario intermediario = stazione.getIntermediario(configWrapper);
				chiediFlussoRendicontazioniClient = new NodoClient(intermediario, null, giornale);
				popolaDatiPagoPAEvento(chiediFlussoRendicontazioniClient.getEventoCtx(), intermediario, stazione, dominio, null);
				risposta = chiediFlussoRendicontazioniClient.nodoChiediElencoFlussiRendicontazione(richiesta, intermediario.getDenominazione());
				chiediFlussoRendicontazioniClient.getEventoCtx().setEsito(Esito.OK);
			} catch (Exception e) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				log.error("Richiesta elenco flussi rendicontazione fallita", e);
				ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiFail", e.getMessage());
				if(chiediFlussoRendicontazioniClient != null) {
					if(e instanceof GovPayException) {
						chiediFlussoRendicontazioniClient.getEventoCtx().setSottotipoEsito(((GovPayException)e).getCodEsito().toString());
					} else if(e instanceof ClientException) {
						chiediFlussoRendicontazioniClient.getEventoCtx().setSottotipoEsito(((ClientException)e).getResponseCode() + "");
					} else {
						chiediFlussoRendicontazioniClient.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
					}
					chiediFlussoRendicontazioniClient.getEventoCtx().setEsito(Esito.FAIL);
					chiediFlussoRendicontazioniClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
					chiediFlussoRendicontazioniClient.getEventoCtx().setException(e);
				}	
				return flussiDaAcquisire;
			}

			if(risposta.getFault() != null) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				log.error("Richiesta elenco flussi rendicontazione fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
				ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiKo", risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
				if(chiediFlussoRendicontazioniClient != null) {
					chiediFlussoRendicontazioniClient.getEventoCtx().setSottotipoEsito(risposta.getFault().getFaultCode());
					chiediFlussoRendicontazioniClient.getEventoCtx().setEsito(Esito.KO);
					chiediFlussoRendicontazioniClient.getEventoCtx().setDescrizioneEsito(risposta.getFault().getFaultString());
				}	
				return flussiDaAcquisire;
			} else {

				if(risposta.getElencoFlussiRendicontazione() == null || risposta.getElencoFlussiRendicontazione().getTotRestituiti() == 0) {
					log.debug("Ritornata lista vuota dal psp");
					ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiOk", "0");
					return flussiDaAcquisire;
				}

				ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiOk", risposta.getElencoFlussiRendicontazione().getTotRestituiti() + "");
				log.debug("Ritornati " + risposta.getElencoFlussiRendicontazione().getTotRestituiti() + " flussi rendicontazione");

				for(TipoIdRendicontazione idRendicontazione : risposta.getElencoFlussiRendicontazione().getIdRendicontazione()) {
					
//					FrBD frBD = new FrBD(configWrapper);
//					boolean exists = frBD.exists(idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso());
//					
//					if(exists){
//						ctx.getApplicationLogger().log("rendicontazioni.flussoDuplicato",  idRendicontazione.getIdentificativoFlusso());
//						log.trace("Flusso rendicontazione gia' presente negli archivi: " + idRendicontazione.getIdentificativoFlusso() + "");
//					} else {
//						log.debug("Ricevuto flusso rendicontazione non presente negli archivi: " + idRendicontazione.getIdentificativoFlusso() + "");
						log.debug("Ricevuto flusso rendicontazione: " + idRendicontazione.getIdentificativoFlusso() + ", " + idRendicontazione.getDataOraFlusso());
						flussiDaAcquisire.add(idRendicontazione);
//					}
				}
			}
		} catch (ServiceException e) {
			log.error("Errore durante l'acquisizione dei flussi di rendicontazione", e);
			return flussiDaAcquisire;
		}  finally {
			if(chiediFlussoRendicontazioniClient != null && chiediFlussoRendicontazioniClient.getEventoCtx().isRegistraEvento()) {
				try {
					EventiBD eventiBD = new EventiBD(configWrapper);
					eventiBD.insertEvento(chiediFlussoRendicontazioniClient.getEventoCtx().toEventoDTO());
				}catch (ServiceException e) {
					log.error("Errore durante l'acquisizione dei flussi di rendicontazione", e);
				}finally {
				}
			}
		}

		return flussiDaAcquisire;
	}
	
	public static void popolaDatiPagoPAEvento(EventoContext eventoCtx, Intermediario intermediario, Stazione stazione, Dominio dominio, String codFlusso) throws ServiceException {

		DatiPagoPA datiPagoPA = new DatiPagoPA();
//		datiPagoPA.setCodCanale(rpt.getCodCanale());
//		datiPagoPA.setCodPsp(rpt.getCodPsp());
		datiPagoPA.setCodStazione(stazione.getCodStazione());
		datiPagoPA.setCodIntermediario(intermediario.getCodIntermediario());
		datiPagoPA.setErogatore(Evento.NDP);
		datiPagoPA.setFruitore(intermediario.getCodIntermediario());
		if(codFlusso != null)
			datiPagoPA.setCodFlusso(codFlusso);
//		datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());
//		datiPagoPA.setModelloPagamento(rpt.getModelloPagamento());
//		datiPagoPA.setCodIntermediarioPsp(rpt.getCodIntermediarioPsp());
		if(dominio != null)
			datiPagoPA.setCodDominio(dominio.getCodDominio());
		eventoCtx.setDatiPagoPA(datiPagoPA);
	}
}
