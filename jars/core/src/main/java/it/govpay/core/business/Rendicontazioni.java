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

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
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
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
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


public class Rendicontazioni extends BasicBD {

	private static Logger log = LoggerWrapperFactory.getLogger(Rendicontazioni.class);

	public Rendicontazioni(BasicBD basicBD) {
		super(basicBD);
	}

	public String downloadRendicontazioni(boolean deep) throws GovPayException, UtilsException { 
		boolean errori = false;
		List<String> response = new ArrayList<>();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		try {
			ctx.getApplicationLogger().log("rendicontazioni.acquisizione");
			DominiBD dominiBD = new DominiBD(this);
			Giornale giornale = AnagraficaManager.getConfigurazione(this).getGiornale();

			StazioniBD stazioniBD = new StazioniBD(this);
			List<Stazione> lstStazioni = stazioniBD.getStazioni();

			this.closeConnection();

			for(Stazione stazione : lstStazioni) {


				List<TipoIdRendicontazione> flussiDaAcquisire = new ArrayList<>();

				this.setupConnection(ctx.getTransactionId());
				Intermediario intermediario = stazione.getIntermediario(this);
				this.closeConnection();

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

				this.setupConnection(ctx.getTransactionId());
				// Scarto i flussi gia acquisiti ed eventuali doppioni scaricati
				FrBD frBD = new FrBD(this);
				Set<String> idfs = new HashSet<>();
				for(TipoIdRendicontazione idRendicontazione : flussiDaAcquisire) {
					if(frBD.exists(idRendicontazione.getIdentificativoFlusso()) || idfs.contains(idRendicontazione.getIdentificativoFlusso()))
						flussiDaAcquisire.remove(idRendicontazione);
					idfs.add(idRendicontazione.getIdentificativoFlusso());
				}
				this.closeConnection();

				for(TipoIdRendicontazione idRendicontazione : flussiDaAcquisire) {
					log.debug("Acquisizione flusso di rendicontazione " + idRendicontazione.getIdentificativoFlusso());
					boolean hasFrAnomalia = false;
					NodoClient chiediFlussoRendicontazioneClient = null;
					try {
						appContext.setupNodoClient(stazione.getCodStazione(), null, Azione.nodoChiediFlussoRendicontazione);
						appContext.getRequest().addGenericProperty(new Property("codStazione", stazione.getCodStazione()));
						appContext.getRequest().addGenericProperty(new Property("idFlusso", idRendicontazione.getIdentificativoFlusso()));
						
						NodoChiediFlussoRendicontazione richiestaFlusso = new NodoChiediFlussoRendicontazione();
						richiestaFlusso.setIdentificativoIntermediarioPA(stazione.getIntermediario(this).getCodIntermediario());
						richiestaFlusso.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
						richiestaFlusso.setPassword(stazione.getPassword());
						richiestaFlusso.setIdentificativoFlusso(idRendicontazione.getIdentificativoFlusso());

						NodoChiediFlussoRendicontazioneRisposta risposta;
						
						try {
							chiediFlussoRendicontazioneClient = new NodoClient(intermediario, null, giornale, this);
							risposta = chiediFlussoRendicontazioneClient.nodoChiediFlussoRendicontazione(richiestaFlusso, stazione.getIntermediario(this).getDenominazione());
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
							}
							// Errore nella richiesta. Loggo e continuo con il prossimo flusso
							response.add(idRendicontazione.getIdentificativoFlusso() + "#Richiesta al nodo fallita: " + e + ".");
							log.error("Richiesta flusso rendicontazione [" + idRendicontazione.getIdentificativoFlusso() + "] fallita: " + e);
							ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", e.getMessage());
							errori = true;
							continue;
						} 

						if(risposta.getFault() != null) {
							// Errore nella richiesta. Loggo e continuo con il prossimo flusso
							response.add(idRendicontazione.getIdentificativoFlusso() + "#Richiesta al nodo fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString() + ".");
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
								response.add(idRendicontazione.getIdentificativoFlusso() + "#Lettura del flusso fallita: " + e + ".");
								log.error("Errore durante la lettura del flusso di rendicontazione", e);
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Lettura del flusso fallita: " + e);
								errori = true;
								continue;
							}

							FlussoRiversamento flussoRendicontazione = null;
							try {
								flussoRendicontazione = JaxbUtils.toFR(tracciato);
							} catch (Exception e) {
								response.add(idRendicontazione.getIdentificativoFlusso() + "#Parsing del flusso fallita: " + e + ".");
								log.error("Errore durante il parsing del flusso di rendicontazione", e);
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Errore durante il parsing del flusso di rendicontazione: " + e);
								errori = true;
								continue;
							}

							log.info("Ricevuto flusso rendicontazione per " + flussoRendicontazione.getDatiSingoliPagamentis().size() + " singoli pagamenti");

							this.setupConnection(ctx.getTransactionId());

							ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlusso");
							appContext.getRequest().addGenericProperty(new Property("trn", flussoRendicontazione.getIdentificativoUnivocoRegolamento()));
							
							Fr fr = new Fr();
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
							codPsp = idRendicontazione.getIdentificativoFlusso().substring(10, idRendicontazione.getIdentificativoFlusso().indexOf("-", 10));
							fr.setCodPsp(codPsp);
							log.debug("Identificativo PSP estratto dall'identificativo flusso: " + codPsp);
							appContext.getRequest().addGenericProperty(new Property("codPsp", codPsp));

							Dominio dominio = null;
							try {
								codDominio = flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco();
								fr.setCodDominio(codDominio);
								appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
								dominio = AnagraficaManager.getDominio(this, codDominio);	
							} catch (Exception e) {
								if(codDominio == null) {
									codDominio = "????";
									appContext.getRequest().addGenericProperty(new Property("codDominio", "null"));
								}
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoDominioNonCensito");
								fr.addAnomalia("007109", "L'indentificativo ricevente [" + codDominio + "] del flusso non riferisce un Dominio censito");
							}

							BigDecimal totaleImportiRendicontati = BigDecimal.ZERO;

							PagamentiBD pagamentiBD = new PagamentiBD(this);
							VersamentiBD versamentiBD = new VersamentiBD(this);
							IuvBD iuvBD = new IuvBD(this);
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
									SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(this);
									rendicontazione.setIdSingoloVersamento(singoloVersamento.getId());

									// Verifico l'importo
									if(rendicontazione.getEsito().equals(EsitoRendicontazione.REVOCATO)) {
										if(pagamento.getImportoRevocato().compareTo(importoRendicontato.abs()) != 0) {
											ctx.getApplicationLogger().log("rendicontazioni.importoStornoErrato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
											log.info("Revoca [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: l'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto stornato [" + pagamento.getImportoRevocato().doubleValue() + "]");
											rendicontazione.addAnomalia("007112", "L'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto stornato [" + pagamento.getImportoRevocato().doubleValue() + "]");
										}

										// Verifico che il pagamento non sia gia' rendicontato
										// Controllo RIMOSSO. Non e' un'anomalia una duplice rendicontazione. L'anomalia occorre se due rendicontazioni dello stesso pagamento vengono incassate entrambe.
//										if(pagamento.isPagamentoRendicontato(this)) {
//											ContextThreadLocal.get().getApplicationLogger().log("rendicontazioni.giaStornato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
//											log.info("Revoca [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: storno gia' rendicontato da altri flussi");
//											rendicontazione.addAnomalia("007113", "Lo storno riferito dalla rendicontazione risulta gia' rendicontato da altri flussi");
//										}

									} else {
										if(pagamento.getImportoPagato().compareTo(importoRendicontato) != 0) {
											ctx.getApplicationLogger().log("rendicontazioni.importoErrato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
											log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: l'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto pagato [" + pagamento.getImportoPagato().doubleValue() + "]");
											rendicontazione.addAnomalia("007104", "L'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto pagato [" + pagamento.getImportoPagato().doubleValue() + "]");
										}

										// Verifico che il pagamento non sia gia' rendicontato
										// Controllo RIMOSSO. Non e' un'anomalia una duplice rendicontazione. L'anomalia occorre se due rendicontazioni dello stesso pagamento vengono incassate entrambe.
//										if(pagamento.isPagamentoRendicontato(this)) {
//											ContextThreadLocal.get().getApplicationLogger().log("rendicontazioni.giaRendicontato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
//											log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: pagamento gia' rendicontato da altri flussi");
//											rendicontazione.addAnomalia("007103", "Il pagamento riferito dalla rendicontazione risulta gia' rendicontato da altri flussi");
//										}
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
												it.govpay.model.Iuv iuvModel = iuvBD.getIuv(dominio.getId(), iuv);
												versamento = versamentiBD.getVersamento(iuvModel.getIdApplicazione(), iuvModel.getCodVersamentoEnte());
											} catch (NotFoundException nfe) {
												// Non e' su sistema. Individuo l'applicativo gestore
												Applicazione applicazioneDominio = new it.govpay.core.business.Applicazione(this).getApplicazioneDominio(dominio, iuv,false);
												if(applicazioneDominio != null) {
													codApplicazione = applicazioneDominio.getCodApplicazione();
													versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(this, codApplicazione), null, null, null, codDominio, iuv, this);
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
											response.add(idRendicontazione.getIdentificativoFlusso() + "#Acquisizione flusso fallita. Riscontrato errore nell'acquisizione del versamento dall'applicazione gestrice [Transazione: " + ctx.getTransactionId() + "].");
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
											
											if(versamento.getSingoliVersamenti(this).size() != 1) {
												// Un pagamento senza rpt DEVE riferire un pagamento tipo 3 con un solo singolo versamento
												ctx.getApplicationLogger().log("rendicontazioni.senzaRptVersamentoMalformato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
												log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur:" + iur + " Indice:" + indiceDati + "] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.");
												rendicontazione.addAnomalia("007114", "Il versamento presenta piu' singoli versamenti");
												continue;
											}
											
											// inserisco l'id singolo versamento corrispondente alla posizione											
											for (SingoloVersamento sv : versamento.getSingoliVersamenti(this)) {
												if(sv.getIndiceDati().intValue() == rendicontazione.getIndiceDati().intValue()) {
													rendicontazione.setIdSingoloVersamento(sv.getId());
													break;
												}
												
											}
											
											// Trovato versamento.
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
							RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(this);
							
							// Tutte le operazioni di salvataggio devono essere in transazione.
							this.setAutoCommit(false);
							frBD.insertFr(fr);
							for(Rendicontazione r : fr.getRendicontazioni(this)) {
								r.setIdFr(fr.getId());
								rendicontazioniBD.insert(r);
							}
							this.commit();
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
						errori = true;
					} finally {
						if(chiediFlussoRendicontazioneClient != null && chiediFlussoRendicontazioneClient.getEventoCtx().isRegistraEvento()) {
							GiornaleEventi giornaleEventi = new GiornaleEventi(this);
							giornaleEventi.registraEvento(chiediFlussoRendicontazioneClient.getEventoCtx().toEventoDTO());
						}
					}
				}
			}
		} catch(Exception e) {
			ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiFail", e.getMessage());
			throw new GovPayException(e);
		} finally {
			try {
				if(this.isClosed()) this.setupConnection(ctx.getTransactionId());
			} catch (Exception e) {
				log.error("Errore nel ripristino della connessione", e);
			}
		}

		ctx.getApplicationLogger().log("rendicontazioni.acquisizioneOk");

		String rspTxt = "";
		if(errori)
			rspTxt = "WARNING#Processo di acquisizione completato parzialmente: uno o piu' flussi non sono stati acquisiti.|";
		else 
			rspTxt = "OK#Processo di acquisizione completato con successo|";

		if(response.isEmpty()) {
			return rspTxt + "Processo di acquisizione completato con successo. #Nessun flusso acquisito.";
		} else {
			return rspTxt + StringUtils.join(response,"|");
		}
	}


	private boolean isInterno(Dominio dominio, String iuv) {

		if(dominio == null) {
			// Se il dominio non e' censito, allora sicramente non e' interno
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
		NodoClient chiediFlussoRendicontazioniClient = null;
		try {
			appContext.setupNodoClient(stazione.getCodStazione(), dominio != null ? dominio.getCodDominio() : null, Azione.nodoChiediElencoFlussiRendicontazione);
			appContext.getRequest().addGenericProperty(new Property("codDominio", dominio != null ? dominio.getCodDominio() : "-"));
			ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussi");

			NodoChiediElencoFlussiRendicontazione richiesta = new NodoChiediElencoFlussiRendicontazione();
			if(dominio != null) richiesta.setIdentificativoDominio(dominio.getCodDominio());
			richiesta.setIdentificativoIntermediarioPA(stazione.getIntermediario(this).getCodIntermediario());
			richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione()); 
			richiesta.setPassword(stazione.getPassword());

			NodoChiediElencoFlussiRendicontazioneRisposta risposta;
			try {
				Intermediario intermediario = stazione.getIntermediario(this);
				chiediFlussoRendicontazioniClient = new NodoClient(intermediario, null, giornale, this);
				risposta = chiediFlussoRendicontazioniClient.nodoChiediElencoFlussiRendicontazione(richiesta, stazione.getIntermediario(this).getDenominazione());
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

				// Per ogni flusso della lista, vedo se ce l'ho gia' in DB ed in caso lo archivio

				for(TipoIdRendicontazione idRendicontazione : risposta.getElencoFlussiRendicontazione().getIdRendicontazione()) {
					this.setupConnection(ctx.getTransactionId());
					FrBD frBD = new FrBD(this);
					boolean exists = frBD.exists(idRendicontazione.getIdentificativoFlusso());
					this.closeConnection();
					if(exists){
						ctx.getApplicationLogger().log("rendicontazioni.flussoDuplicato",  idRendicontazione.getIdentificativoFlusso());
						log.trace("Flusso rendicontazione gia' presente negli archivi: " + idRendicontazione.getIdentificativoFlusso() + "");
					} else {
						log.debug("Ricevuto flusso rendicontazione non presente negli archivi: " + idRendicontazione.getIdentificativoFlusso() + "");
						flussiDaAcquisire.add(idRendicontazione);
					}
				}
			}
		} catch (ServiceException e) {
			log.error("Errore durante l'acquisizione dei flussi di rendicontazione", e);
			return flussiDaAcquisire;
		}  finally {
			if(chiediFlussoRendicontazioniClient != null && chiediFlussoRendicontazioniClient.getEventoCtx().isRegistraEvento()) {
				try {
					this.setupConnection(ctx.getTransactionId());
					GiornaleEventi giornaleEventi = new GiornaleEventi(this);
					giornaleEventi.registraEvento(chiediFlussoRendicontazioniClient.getEventoCtx().toEventoDTO());
				}catch (ServiceException e) {
					log.error("Errore durante l'acquisizione dei flussi di rendicontazione", e);
				}finally {
					try {
						if(!this.isClosed())
							this.closeConnection();
					} catch (ServiceException e) {
					}
				}
			}
		}

		return flussiDaAcquisire;
	}
}
