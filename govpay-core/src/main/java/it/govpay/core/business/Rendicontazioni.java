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

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoliPagamenti;
import it.gov.digitpa.schemas._2011.pagamenti.CtFlussoRiversamento;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediElencoFlussiRendicontazione;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediElencoFlussiRendicontazioneRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediFlussoRendicontazione;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediFlussoRendicontazioneRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.TipoIdRendicontazione;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.model.Intermediario;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Stazione;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Fr.StatoFr;
import it.govpay.servizi.commons.EsitoOperazione;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;


public class Rendicontazioni extends BasicBD {

	private static Logger log = LogManager.getLogger();

	public Rendicontazioni(BasicBD basicBD) {
		super(basicBD);
	}

	public String downloadRendicontazioni(boolean deep) throws GovPayException {
		boolean errori = false;
		List<String> response = new ArrayList<String>();
		try {
			GpThreadLocal.get().log("rendicontazioni.acquisizione");
			DominiBD dominiBD = new DominiBD(this);

			StazioniBD stazioniBD = new StazioniBD(this);
			List<Stazione> lstStazioni = stazioniBD.getStazioni();

			PspBD pspBD = new PspBD(this);
			List<Psp> lstPsp = pspBD.getPsp();
			closeConnection();

			for(Stazione stazione : lstStazioni) {


				List<TipoIdRendicontazione> flussiDaAcquisire = new ArrayList<TipoIdRendicontazione>();

				setupConnection(GpThreadLocal.get().getTransactionId());
				Intermediario intermediario = stazione.getIntermediario(this);
				NodoClient client = new NodoClient(intermediario, this);
				closeConnection();

				if(deep) {
					DominioFilter filter = dominiBD.newFilter();
					filter.setCodStazione(stazione.getCodStazione());
					List<Dominio> lstDomini = dominiBD.findAll(filter);

					for(Dominio dominio : lstDomini) { 
						List<String> sids = new ArrayList<String>();
						for(Psp psp : lstPsp) {
							if(sids.contains(psp.getCodPsp())) continue;
							sids.add(psp.getCodPsp());
							log.debug("Acquisizione dei flussi di rendicontazione dal psp [" + psp.getCodPsp() + "] per il dominio [" + dominio.getCodDominio() + "] in corso.");
							flussiDaAcquisire.addAll(chiediListaFr(client, psp, stazione, dominio));
						}
					}
				} else {
					log.debug("Acquisizione dei flussi di rendicontazione per la stazione [" + stazione.getCodStazione() + "] in corso.");
					flussiDaAcquisire.addAll(chiediListaFr(client, null, stazione, null));
				}

				// Scarto i flussi gia acquisiti
				setupConnection(GpThreadLocal.get().getTransactionId());
				
				FrBD frBD = new FrBD(this);
				for(TipoIdRendicontazione idRendicontazione : flussiDaAcquisire) {
					if(frBD.exists(idRendicontazione.getIdentificativoFlusso()))
						flussiDaAcquisire.remove(idRendicontazione);
				}
				closeConnection();

				for(TipoIdRendicontazione idRendicontazione : flussiDaAcquisire) {
					log.debug("Acquisizione flusso di rendicontazione " + idRendicontazione.getIdentificativoFlusso());
					boolean hasFrAnomalia = false;
					String idTransaction2 = null;
					try {
						idTransaction2 = GpThreadLocal.get().openTransaction();
						GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codStazione", stazione.getCodStazione()));
						GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("idFlusso", idRendicontazione.getIdentificativoFlusso()));
						GpThreadLocal.get().setupNodoClient(stazione.getCodStazione(), null, Azione.nodoChiediFlussoRendicontazione);
						NodoChiediFlussoRendicontazione richiestaFlusso = new NodoChiediFlussoRendicontazione();
						richiestaFlusso.setIdentificativoIntermediarioPA(stazione.getIntermediario(this).getCodIntermediario());
						richiestaFlusso.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
						richiestaFlusso.setPassword(stazione.getPassword());
						richiestaFlusso.setIdentificativoFlusso(idRendicontazione.getIdentificativoFlusso());

						NodoChiediFlussoRendicontazioneRisposta risposta;
						try {
							risposta = client.nodoChiediFlussoRendicontazione(richiestaFlusso, stazione.getIntermediario(this).getDenominazione());
						} catch (Exception e) {
							// Errore nella richiesta. Loggo e continuo con il prossimo flusso
							response.add(idRendicontazione.getIdentificativoFlusso() + "#Richiesta al nodo fallita: " + e + ".");
							log.error("Richiesta flusso  rendicontazione [" + idRendicontazione.getIdentificativoFlusso() + "] fallita: " + e);
							GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoFail", e.getMessage());
							errori = true;
							continue;
						} 

						if(risposta.getFault() != null) {
							// Errore nella richiesta. Loggo e continuo con il prossimo flusso
							response.add(idRendicontazione.getIdentificativoFlusso() + "#Richiesta al nodo fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString() + ".");
							log.error("Richiesta flusso rendicontazione [" + idRendicontazione.getIdentificativoFlusso() + "] fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
							GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoKo", risposta.getFault().getFaultCode(), risposta.getFault().getFaultString(), risposta.getFault().getDescription());
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
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoFail", "Lettura del flusso fallita: " + e);
								errori = true;
								continue;
							}

							CtFlussoRiversamento flussoRendicontazione = null;
							try {
								flussoRendicontazione = JaxbUtils.toFR(tracciato);
							} catch (Exception e) {
								response.add(idRendicontazione.getIdentificativoFlusso() + "#Parsing del flusso fallita: " + e + ".");
								log.error("Errore durante il parsing del flusso di rendicontazione", e);
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoFail", "Errore durante il parsing del flusso di rendicontazione: " + e);
								errori = true;
								continue;
							}

							log.info("Ricevuto flusso rendicontazione per " + flussoRendicontazione.getDatiSingoliPagamenti().size() + " singoli pagamenti");

							setupConnection(GpThreadLocal.get().getTransactionId());

							GpThreadLocal.get().log("rendicontazioni.acquisizioneFlusso");
							GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("trn", flussoRendicontazione.getIdentificativoUnivocoRegolamento()));
							
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
							try {
								codPsp = idRendicontazione.getIdentificativoFlusso().substring(10, idRendicontazione.getIdentificativoFlusso().indexOf("-", 10));
								fr.setCodPsp(codPsp);
								log.debug("Identificativo PSP estratto dall'identificativo flusso: " + codPsp);
								AnagraficaManager.getPsp(this, codPsp);
								GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codPsp", codPsp));
							} catch (Exception e) {
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoPspNonCensito", codPsp == null ? "null" : codPsp);
								GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codPsp", codPsp == null ? "null" : codPsp));
								fr.addAnomalia("007108", "L'identificativo PSP [" + codPsp + "] ricavato dal codice flusso non riferisce un PSP censito");
							}

							Dominio dominio = null;
							try {
								codDominio = flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco();
								fr.setCodDominio(codDominio);
								GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio));
								dominio = AnagraficaManager.getDominio(this, codDominio);	
							} catch (Exception e) {
								if(codDominio == null) {
									codDominio = "????";
									GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codDominio", "null"));
								}
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoDominioNonCensito");
								fr.addAnomalia("007109", "L'indentificativo ricevente [" + codDominio + "] del flusso non riferisce un Dominio censito");
							}

							BigDecimal totaleImportiRendicontati = BigDecimal.ZERO;

							PagamentiBD pagamentiBD = new PagamentiBD(this);
							VersamentiBD versamentiBD = new VersamentiBD(this);
							IuvBD iuvBD = new IuvBD(this);
							for(CtDatiSingoliPagamenti dsp : flussoRendicontazione.getDatiSingoliPagamenti()) {

								String iur = dsp.getIdentificativoUnivocoRiscossione();
								String iuv = dsp.getIdentificativoUnivocoVersamento();
								BigDecimal importoRendicontato = dsp.getSingoloImportoPagato();

								log.debug("Rendicontato (Esito " + dsp.getCodiceEsitoSingoloPagamento() + ") per un importo di (" + dsp.getSingoloImportoPagato() + ") [CodDominio: " + codDominio + "] [Iuv: "+ dsp.getIdentificativoUnivocoVersamento() + "][Iur: " + iur + "]");

								it.govpay.bd.model.Rendicontazione rendicontazione = new it.govpay.bd.model.Rendicontazione();

								// Gestisco un codice esito non supportato
								try {
									rendicontazione.setEsito(EsitoRendicontazione.toEnum(dsp.getCodiceEsitoSingoloPagamento()));
								} catch (Exception e) {
									GpThreadLocal.get().log("rendicontazioni.esitoSconosciuto", iuv, iur, dsp.getCodiceEsitoSingoloPagamento() == null ? "null" : dsp.getCodiceEsitoSingoloPagamento());
									rendicontazione.addAnomalia("007110", "Codice esito [" + dsp.getCodiceEsitoSingoloPagamento() + "] sconosciuto");
								}

								rendicontazione.setData(dsp.getDataEsitoSingoloPagamento());
								rendicontazione.setIur(dsp.getIdentificativoUnivocoRiscossione());
								rendicontazione.setIuv(dsp.getIdentificativoUnivocoVersamento());
								rendicontazione.setImporto(dsp.getSingoloImportoPagato());

								totaleImportiRendicontati = totaleImportiRendicontati.add(importoRendicontato);

								// Cerco il pagamento riferito
								it.govpay.bd.model.Pagamento pagamento = null;
								try {
									pagamento = pagamentiBD.getPagamento(codDominio, iuv, iur); 

									// Pagamento trovato. Faccio i controlli semantici
									rendicontazione.setIdPagamento(pagamento.getId());

									// Verifico l'importo
									if(rendicontazione.getEsito().equals(EsitoRendicontazione.REVOCATO)) {
										if(pagamento.getImportoRevocato().compareTo(importoRendicontato.abs()) != 0) {
											GpThreadLocal.get().log("rendicontazioni.importoStornoErrato", iuv, iur);
											log.info("Revoca [Dominio:" + codDominio + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: l'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto stornato [" + pagamento.getImportoRevocato().doubleValue() + "]");
											rendicontazione.addAnomalia("007112", "L'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto stornato [" + pagamento.getImportoRevocato().doubleValue() + "]");
										}

										// Verifico che il pagamento non sia gia' rendicontato
										if(pagamento.isPagamentoRendicontato(this)) {
											GpThreadLocal.get().log("rendicontazioni.giaStornato", iuv, iur);
											log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: storno gia' rendicontato da altri flussi");
											rendicontazione.addAnomalia("007113", "Lo storno riferito dalla rendicontazione risulta gia' rendicontato da altri flussi");
										}

									} else {
										if(pagamento.getImportoPagato().compareTo(importoRendicontato) != 0) {
											GpThreadLocal.get().log("rendicontazioni.importoErrato", iuv, iur);
											log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: l'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto pagato [" + pagamento.getImportoPagato().doubleValue() + "]");
											rendicontazione.addAnomalia("007104", "L'importo rendicontato ["+importoRendicontato.doubleValue()+"] non corrisponde a quanto pagato [" + pagamento.getImportoPagato().doubleValue() + "]");
										}

										// Verifico che il pagamento non sia gia' rendicontato
										if(pagamento.isPagamentoRendicontato(this)) {
											GpThreadLocal.get().log("rendicontazioni.giaRendicontato", iuv, iur);
											log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: pagamento gia' rendicontato da altri flussi");
											rendicontazione.addAnomalia("007103", "Il pagamento riferito dalla rendicontazione risulta gia' rendicontato da altri flussi");
										}
									}

								} catch (NotFoundException e) {
									// Pagamento non trovato. Devo capire se ce' un errore.

									// Controllo che sia per uno IUV generato da noi
									if(!isInterno(dominio, iuv)) {
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
												codApplicazione = it.govpay.bd.GovpayConfig.getInstance().getDefaultCustomIuvGenerator().getCodApplicazione(dominio, iuv, dominio.getApplicazioneDefault(this));
												if(codApplicazione == null) {
													response.add(idRendicontazione.getIdentificativoFlusso() + "#Acquisizione flusso fallita. Impossibile individuare l'applicativo gestore del versamento per acquisirne i dati [Dominio:" + codDominio+ " Iuv:" + iuv + "].");
													log.error("Errore durante il processamento del flusso di Rendicontazione [Flusso:" + idRendicontazione.getIdentificativoFlusso() + "]: Impossibile individuare l'applicativo gestore del versamento per acquisirne i dati [Dominio:" + codDominio+ " Iuv:" + iuv + "]. Flusso non acquisito.");
													GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoKo", idRendicontazione.getIdentificativoFlusso(), "Impossibile individuare l'applicativo gestore del versamento per acquisirne i dati [Dominio:" + codDominio+ " Iuv:" + iuv + "].  Flusso non acquisito.");
													throw new GovPayException(EsitoOperazione.INTERNAL, "Impossibile individuare l'applicativo gestore del versamento per acquisirne i dati [Dominio:" + codDominio+ " Iuv:" + iuv + "].  Flusso non acquisito.");
												}
												versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(this, codApplicazione), null, null, null, codDominio, iuv, this);
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
											response.add(idRendicontazione.getIdentificativoFlusso() + "#Acquisizione flusso fallita. Riscontrato errore nell'acquisizione del versamento dall'applicazione gestrice [Transazione: " + idTransaction2 + "].");
											log.error("Errore durante il processamento del flusso di Rendicontazione [Flusso:" + idRendicontazione.getIdentificativoFlusso() + "]: impossibile acquisire i dati del versamento [Dominio:" + codDominio+ " Iuv:" + iuv + "]. Flusso non acquisito.");
											GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoKo", idRendicontazione.getIdentificativoFlusso(), "Impossibile acquisire i dati di un versamento dall'applicativo gestore [Applicazione:" + codApplicazione + " Dominio:" + codDominio+ " Iuv:" + iuv + "].  Flusso non acquisito.");
											throw new GovPayException(ce);
										}

										if(versamento == null) {
											// non ho trovato il versamento 
											GpThreadLocal.get().log("rendicontazioni.senzaRptNoVersamento", iuv, iur);
											log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.");
											rendicontazione.addAnomalia("007111", "Il versamento risulta sconosciuto: " + erroreVerifica);
											continue;
										} else {
											
											if(versamento.getSingoliVersamenti(this).size() != 1) {
												// Un pagamento senza rpt DEVE riferire un pagamento tipo 3 con un solo singolo versamento
												GpThreadLocal.get().log("rendicontazioni.senzaRptVersamentoMalformato", iuv, iur);
												log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.");
												rendicontazione.addAnomalia("007114", "Il versamento presenta piu' singoli versamenti");
												continue;
											}
											
											// Trovato versamento. Creo il pagamento senza rpt 
											pagamento = new it.govpay.bd.model.Pagamento();
											pagamento.setCodDominio(codDominio);
											pagamento.setDataAcquisizione(rendicontazione.getData());
											pagamento.setDataPagamento(rendicontazione.getData());
											pagamento.setImportoPagato(rendicontazione.getImporto());
											pagamento.setIur(rendicontazione.getIur());
											pagamento.setIuv(rendicontazione.getIuv());
											pagamento.setCodDominio(fr.getCodDominio());
											pagamento.setSingoloVersamento(versamento.getSingoliVersamenti(this).get(0));
											
											rendicontazione.setPagamento(pagamento);
											rendicontazione.setPagamentoDaCreare(true);
											continue;
										}
									}

									GpThreadLocal.get().log("rendicontazioni.noPagamento", iuv, iur);
									log.info("Pagamento [Dominio:" + codDominio + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: il pagamento non risulta presente in base dati.");
									rendicontazione.addAnomalia("007101", "Il pagamento riferito dalla rendicontazione non risulta presente in base dati.");
									continue;
								} catch (MultipleResultException e) {
									// Individuati piu' pagamenti riferiti dalla rendicontazione
									GpThreadLocal.get().log("rendicontazioni.poliPagamento", iuv, iur);
									log.info("Pagamento rendicontato duplicato: [CodDominio: " + codDominio + "] [Iuv: "+ iuv + "] [Iur: " + iur + "]");
									rendicontazione.addAnomalia("007102", "La rendicontazione riferisce piu di un pagamento gestito.");
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
								GpThreadLocal.get().log("rendicontazioni.importoTotaleErrato");
								log.info("La somma degli importi rendicontati ["+totaleImportiRendicontati+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getImportoTotalePagamenti()  + "]");
								fr.addAnomalia("007106", "La somma degli importi rendicontati ["+totaleImportiRendicontati+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getImportoTotalePagamenti()  + "]");
							}

							try {
								if(flussoRendicontazione.getDatiSingoliPagamenti().size() != flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()) {
									GpThreadLocal.get().log("rendicontazioni.numeroRendicontazioniErrato");
									log.info("Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamenti().size()+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()  + "]");
									fr.addAnomalia("007107", "Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamenti().size()+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()  + "]");
								}	
							} catch (Exception e) {
								GpThreadLocal.get().log("rendicontazioni.numeroRendicontazioniErrato");
								log.info("Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamenti().size()+"] non corrisponde al totale indicato nella testata del flusso [????]");
								fr.addAnomalia("007107", "Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamenti().size()+"] non corrisponde al totale indicato nella testata del flusso [????]");
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
							setAutoCommit(false);
							frBD.insertFr(fr);
							for(Rendicontazione r : fr.getRendicontazioni(this)) {
								r.setIdFr(fr.getId());
								
								// controllo se c'e' un pagamento da creare relativo alla rendicontazione
								// deve anche essere creato il pagamento.
								if(r.isPagamentoDaCreare()) {
									pagamentiBD.insertPagamento(r.getPagamento(this));
									r.setIdPagamento(r.getPagamento(this).getId());
								}
								rendicontazioniBD.insert(r);
							}
							this.commit();
							if(!hasFrAnomalia) {
								log.info("Flusso di rendicontazione acquisito senza anomalie.");
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoOk");
							} else {
								log.info("Flusso di rendicontazione acquisito con anomalie.");
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoOkAnomalia");
							}
						}
					} catch (GovPayException ce) {
						log.error("Flusso di rendicontazione non acquisito", ce);
						errori = true;
					} finally {
						GpThreadLocal.get().closeTransaction(idTransaction2);
					}
				}
			}
		} catch(Exception e) {
			GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussiFail", e.getMessage());
			throw new GovPayException(e);
		} finally {
			try {
				if(isClosed()) setupConnection(GpThreadLocal.get().getTransactionId());
			} catch (Exception e) {
				log.error("Errore nel ripristino della connessione", e);
			}
		}

		GpThreadLocal.get().log("rendicontazioni.acquisizioneOk");

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

	private List<TipoIdRendicontazione> chiediListaFr(NodoClient client, Psp psp, Stazione stazione, Dominio dominio){
		String idTransaction = null;
		List<TipoIdRendicontazione> flussiDaAcquisire = new ArrayList<TipoIdRendicontazione>();
		try {
			idTransaction = GpThreadLocal.get().openTransaction();
			GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codDominio", dominio != null ? dominio.getCodDominio() : "-"));
			GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codPsp", psp != null ? psp.getCodPsp() : "-"));
			GpThreadLocal.get().setupNodoClient(stazione.getCodStazione(), dominio != null ? dominio.getCodDominio() : null, Azione.nodoChiediElencoFlussiRendicontazione);
			GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussi");

			NodoChiediElencoFlussiRendicontazione richiesta = new NodoChiediElencoFlussiRendicontazione();
			if(dominio != null) richiesta.setIdentificativoDominio(dominio.getCodDominio());
			richiesta.setIdentificativoIntermediarioPA(stazione.getIntermediario(this).getCodIntermediario());
			richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione()); 
			richiesta.setPassword(stazione.getPassword());
			if(psp != null) richiesta.setIdentificativoPSP(psp.getCodPsp());

			NodoChiediElencoFlussiRendicontazioneRisposta risposta;
			try {
				risposta = client.nodoChiediElencoFlussiRendicontazione(richiesta, stazione.getIntermediario(this).getDenominazione());
			} catch (Exception e) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				log.error("Richiesta elenco flussi rendicontazione fallita", e);
				GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussiFail", e.getMessage());
				return flussiDaAcquisire;
			}

			if(risposta.getFault() != null) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				log.error("Richiesta elenco flussi rendicontazione fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
				GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussiKo", risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
				return flussiDaAcquisire;
			} else {

				if(risposta.getElencoFlussiRendicontazione() == null || risposta.getElencoFlussiRendicontazione().getTotRestituiti() == 0) {
					log.debug("Ritornata lista vuota dal psp");
					GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussiOk", "0");
					return flussiDaAcquisire;
				}

				GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussiOk", risposta.getElencoFlussiRendicontazione().getTotRestituiti() + "");
				log.debug("Ritornati " + risposta.getElencoFlussiRendicontazione().getTotRestituiti() + " flussi rendicontazione");

				// Per ogni flusso della lista, vedo se ce l'ho gia' in DB ed in caso lo archivio

				for(TipoIdRendicontazione idRendicontazione : risposta.getElencoFlussiRendicontazione().getIdRendicontazione()) {
					setupConnection(GpThreadLocal.get().getTransactionId());
					FrBD frBD = new FrBD(this);
					boolean exists = frBD.exists(idRendicontazione.getIdentificativoFlusso());
					closeConnection();
					if(exists){
						GpThreadLocal.get().log("rendicontazioni.flussoDuplicato",  idRendicontazione.getIdentificativoFlusso());
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
		} finally {
			if(idTransaction != null) GpThreadLocal.get().closeTransaction(idTransaction);
		}

		return flussiDaAcquisire;
	}


	/**
	 * Recupera l'elenco dei flussi di rendicontazione che contengono almeno una redicontazione
	 * afferente all'applicazione fornita.
	 * 
	 * @param applicazione
	 * @return
	 * @throws GovPayException
	 * @throws ServiceException
	 */
	public List<Fr> chiediListaRendicontazioni(Applicazione applicazione) throws GovPayException, ServiceException {
		FrBD frBD = new FrBD(this);
		FrFilter newFilter = frBD.newFilter();
		newFilter.setIdApplicazione(applicazione.getId());
		return frBD.findAll(newFilter);
	}

	/**
	 * Recupera l'elenco dei flussi di rendicontazione per cui l'applicazione richiedente e' abilitata
	 * 
	 * 
	 * TODO rendere opzionale il filtro per dominio
	 * TODO deprecare il filtro per applicazione
	 * 
	 * @param applicazione
	 * @param codDominio
	 * @param codApplicazione
	 * @param da
	 * @param a
	 * @return
	 * @throws GovPayException
	 * @throws ServiceException
	 * @throws NotFoundException
	 */
	public List<Fr> chiediListaRendicontazioni(Applicazione applicazione, String codDominio, String codApplicazione, Date da, Date a) throws GovPayException, ServiceException, NotFoundException {
		
		List<String> domini = new ArrayList<String>();
		if(codDominio != null) {
			AclEngine.isAuthorized(applicazione, Servizio.RENDICONTAZIONE, codDominio, null);
			domini.add(codDominio);
		} else {
			Set<String> authorizedRnd = AclEngine.getAuthorizedRnd(applicazione);
			if(authorizedRnd != null)
				domini.addAll(authorizedRnd);
			else 
				domini = null;
		}
		
		if(domini != null && domini.size() == 0)
			return new ArrayList<Fr>();

		FrBD frBD = new FrBD(this);
		FrFilter newFilter = frBD.newFilter();
		newFilter.setCodDominio(domini);
		newFilter.setDatainizio(da);
		newFilter.setDataFine(a);

		if(codApplicazione != null) {
			long idApplicazione = 0;
			try {
				idApplicazione = AnagraficaManager.getApplicazione(this, codApplicazione).getId();
			} catch (Exception e) {
				throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
			}
			newFilter.setIdApplicazione(idApplicazione);
		}
		return frBD.findAll(newFilter);
	}


	/**
	 * Recupera il flusso di rendicontazione identificato dal codFlusso
	 * 
	 * Se applicazione == null, il flusso contiene tutte le rendicontazioni, 
	 * altrimenti solo quelle riferite a pagamenti dell'applicazione.
	 * 
	 * @param applicazione
	 * @param codFlusso
	 * @return
	 * @throws GovPayException
	 * @throws ServiceException
	 */

	public Fr chiediRendicontazione(String codFlusso) throws GovPayException, ServiceException {
		FrBD frBD = new FrBD(this);
		try {
			Fr flusso = frBD.getFr(codFlusso);
			return flusso;
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.RND_000);
		}
	}
}
