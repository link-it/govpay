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
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.FrApplicazione;
import it.govpay.model.Intermediario;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.RendicontazioneSenzaRpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Pagamento.EsitoRendicontazione;
import it.govpay.servizi.commons.EsitoOperazione;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private static SimpleDateFormat simpleDateFormatAnno = new SimpleDateFormat("yyyy");
	
	public Rendicontazioni(BasicBD basicBD) {
		super(basicBD);
	}

	public String downloadRendicontazioni(boolean deep) throws GovPayException {
		
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
				NodoClient client = new NodoClient(intermediario);
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
					int annoFlusso = Integer.parseInt(simpleDateFormatAnno.format(idRendicontazione.getDataOraFlusso()));
					if(frBD.exists(annoFlusso, idRendicontazione.getIdentificativoFlusso()))
						flussiDaAcquisire.remove(idRendicontazione);
				}
				closeConnection();
				
				for(TipoIdRendicontazione idRendicontazione : flussiDaAcquisire) {
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
							GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoFail", idRendicontazione.getIdentificativoFlusso(), e.getMessage());
							continue;
						} 
	
						if(risposta.getFault() != null) {
							// Errore nella richiesta. Loggo e continuo con il prossimo flusso
							response.add(idRendicontazione.getIdentificativoFlusso() + "#Richiesta al nodo fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString() + ".");
							log.error("Richiesta flusso rendicontazione [" + idRendicontazione.getIdentificativoFlusso() + "] fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
							GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoKo", idRendicontazione.getIdentificativoFlusso(), risposta.getFault().getFaultCode(), risposta.getFault().getFaultString(), risposta.getFault().getDescription());
						} else {
							byte[] tracciato = null;
							try {
								ByteArrayOutputStream output = new ByteArrayOutputStream();
								DataHandler dh = risposta.getXmlRendicontazione();
								dh.writeTo(output);
								tracciato = output.toByteArray();
							} catch (IOException e) {
								response.add(idRendicontazione.getIdentificativoFlusso() + "#Lettura del flusso fallita: " + e + ".");
								log.error("Errore durante la lettura del flusso di rendicontazione: " + e);
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoFail", idRendicontazione.getIdentificativoFlusso(), "Lettura del flusso fallita: " + e);
								continue;
							}
							
							CtFlussoRiversamento flussoRendicontazione = null;
							try {
								flussoRendicontazione = JaxbUtils.toFR(tracciato);
							} catch (Exception e) {
								response.add(idRendicontazione.getIdentificativoFlusso() + "#Parsing del flusso fallita: " + e + ".");
								log.error("Errore durante il parsing del flusso di rendicontazione: " + e);
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoFail", "Errore durante il parsing del flusso di rendicontazione: " + e);
								continue;
							}
							
							log.info("Ricevuto flusso rendicontazione per " + flussoRendicontazione.getDatiSingoliPagamenti().size() + " singoli pagamenti");
							
							setupConnection(GpThreadLocal.get().getTransactionId());
							
							Psp psp = null;
							Dominio dominio = null;
							String identificativoUnivocoMittente = null, identificativoUnivocoRicevente = null;
							try {
								identificativoUnivocoMittente = flussoRendicontazione.getIstitutoMittente().getIdentificativoUnivocoMittente().getCodiceIdentificativoUnivoco();
								psp = AnagraficaManager.getPsp(this, identificativoUnivocoMittente);
								GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codPsp", psp.getCodPsp()));
							} catch (Exception e) {
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoFail", "Impossibile individuare il PSP riferito dal Flusso [IdentificativoUnivocoMittente: " + identificativoUnivocoMittente + "]");
								continue;
							}
							
							try {
								identificativoUnivocoRicevente = flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco();
								dominio = AnagraficaManager.getDominio(this, identificativoUnivocoRicevente);			
								GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codDominio", dominio.getCodDominio()));
							} catch (Exception e) {
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoFail", "Impossibile individuare il Dominio riferito dal Flusso [IdentificativoUnivocoRicevente: " + identificativoUnivocoMittente + "]");
								continue;
							}
							
							
							GpThreadLocal.get().log("rendicontazioni.acquisizioneFlusso", flussoRendicontazione.getIdentificativoUnivocoRegolamento());
							
							Fr fr = new Fr();
							
							int annoFlusso = Integer.parseInt(simpleDateFormatAnno.format(idRendicontazione.getDataOraFlusso()));
							fr.setAnnoRiferimento(annoFlusso);
							fr.setCodBicRiversamento(flussoRendicontazione.getCodiceBicBancaDiRiversamento());
							fr.setCodFlusso(idRendicontazione.getIdentificativoFlusso());
							fr.setIdPsp(psp.getId());
							fr.setIur(flussoRendicontazione.getIdentificativoUnivocoRegolamento());
							fr.setDataAcquisizione(new Date());
							fr.setDataFlusso(flussoRendicontazione.getDataOraFlusso());
							fr.setDataRegolamento(flussoRendicontazione.getDataRegolamento());
							fr.setNumeroPagamenti(flussoRendicontazione.getNumeroTotalePagamenti().longValue());
							fr.setImportoTotalePagamenti(flussoRendicontazione.getImportoTotalePagamenti().doubleValue());
							fr.setIdDominio(dominio.getId());
							fr.setXml(tracciato);
	
							BigDecimal totaleImportiRendicontati = BigDecimal.ZERO;
							List<String> erroriAcquisizione = new ArrayList<String>();
							
							PagamentiBD pagamentiBD = new PagamentiBD(this);
							VersamentiBD versamentiBD = new VersamentiBD(this);
							Map<Long, FrApplicazione> frRendicontazioniMap = new HashMap<Long, FrApplicazione>();
							
							for(CtDatiSingoliPagamenti dsp : flussoRendicontazione.getDatiSingoliPagamenti()) {
								
								String iur = dsp.getIdentificativoUnivocoRiscossione();
								String iuv = dsp.getIdentificativoUnivocoVersamento();
								BigDecimal importoRendicontato = dsp.getSingoloImportoPagato();
								
								log.debug("Rendicontato (Esito " + dsp.getCodiceEsitoSingoloPagamento() + ") per un importo di (" + dsp.getSingoloImportoPagato() + ") [CodDominio: " + dominio.getCodDominio() + "] [Iuv: "+ dsp.getIdentificativoUnivocoVersamento() + "][Iur: " + iur + "]");
	
								totaleImportiRendicontati = totaleImportiRendicontati.add(importoRendicontato);
								
								if(dsp.getCodiceEsitoSingoloPagamento().equals("0")) {
	
									it.govpay.bd.model.Pagamento pagamento = null;
									try {
										pagamento = pagamentiBD.getPagamento(dominio.getCodDominio(), iuv, iur); 
									} catch (NotFoundException e) {
										GpThreadLocal.get().log("rendicontazioni.noPagamento", iuv, iur);
										erroriAcquisizione.add("Rendicontazione [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: nessun pagamento associato alla rendicontazione.");
										log.error("Pagamento [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: il pagamento non risulta presente in base dati.");
										continue;
									} catch (MultipleResultException e) {
										GpThreadLocal.get().log("rendicontazioni.poliPagamento", iuv, iur);
										erroriAcquisizione.add("Rendicontazione [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: nessun pagamento associato alla rendicontazione.");
										log.error("Pagamento rendicontato duplicato: [CodDominio: " + dominio.getCodDominio() + "] [Iuv: "+ iuv + "] [Iur: " + iur + "]");
										continue;
									} 
									
									if(pagamento.getIdFrApplicazione() != null) {
										GpThreadLocal.get().log("rendicontazioni.giaRendicontato", iuv, iur);
										erroriAcquisizione.add("Rendicontazione [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: pagamento gia' rendicontato dal flusso [CodFlusso:" + pagamento.getCodFlussoRendicontazione() + "]");
										log.error("Pagamento [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: pagamento gia' rendicontato dal flusso [CodFlusso:" + pagamento.getCodFlussoRendicontazione() + "]");
										continue;
									}
											
					 				if(pagamento.getImportoPagato().compareTo(importoRendicontato) != 0) {
					 					GpThreadLocal.get().log("rendicontazioni.importoErrato", iuv, iur);
										erroriAcquisizione.add("Rendicontazione [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: l'importo rendicontato ["+importoRendicontato+"] non corrisponde a quanto pagato [" + pagamento.getImportoPagato() + "]");
										log.error("Pagamento [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] rendicontato con errore: l'importo rendicontato ["+importoRendicontato+"] non corrisponde a quanto pagato [" + pagamento.getImportoPagato() + "]");
										continue;
									}
									
									long idApplicazione = pagamento.getSingoloVersamento(this).getVersamento(this).getIdApplicazione();
									
									FrApplicazione frApplicazione = frRendicontazioniMap.get(idApplicazione);
									
									if(frApplicazione == null) {
										frApplicazione = new FrApplicazione();
										frApplicazione.setIdApplicazione(idApplicazione);
										frRendicontazioniMap.put(idApplicazione, frApplicazione);
									}
									
									pagamento.setCodFlussoRendicontazione(fr.getCodFlusso());
									pagamento.setDataRendicontazione(dsp.getDataEsitoSingoloPagamento());
									pagamento.setEsitoRendicontazione(EsitoRendicontazione.toEnum(dsp.getCodiceEsitoSingoloPagamento()));
									
									frApplicazione.addPagamento(pagamento);
								}
								
								
								if(dsp.getCodiceEsitoSingoloPagamento().equals("9")) {
									RendicontazioneSenzaRpt rendicontazioneSenzaRpt = new RendicontazioneSenzaRpt();
									
									IuvBD iuvBD = new IuvBD(this);
									it.govpay.model.Iuv iuvModel = null;
									try {
										iuvModel = iuvBD.getIuv(dominio.getId(), iuv);
									} catch (NotFoundException e) {
										GpThreadLocal.get().log("rendicontazioni.noVersamento", iuv, iur);
										erroriAcquisizione.add("Rendicontazione senza RPT [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: Iuv sconosciuto");
										log.error("Rendicontazione senza RPT [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: Iuv sconosciuto");
										continue;
									}
									
									try {
										it.govpay.bd.model.Versamento versamento = versamentiBD.getVersamento(iuvModel.getIdApplicazione(), iuvModel.getCodVersamentoEnte());
										List<SingoloVersamento> singoli = versamento.getSingoliVersamenti(this);
										if(singoli.size() != 1) {
											GpThreadLocal.get().log("rendicontazioni.noVersamento", iuv);
											erroriAcquisizione.add("Rendicontazione senza RPT [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: il versamento riferito ha piu' voci di pagamento.");
											log.error("Rendicontazione senza RPT [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: il versamento riferito ha piu' voci di pagamento.");
											continue;
										}
										rendicontazioneSenzaRpt.setIdSingoloVersamento(singoli.get(0).getId());
									} catch (NotFoundException e) {
										GpThreadLocal.get().log("rendicontazioni.noVersamento", iuv);
										erroriAcquisizione.add("Rendicontazione senza RPT [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: nessun versamento associato al pagamento.");
										log.error("Rendicontazione senza RPT [Dominio:" + dominio.getCodDominio() + " Iuv:" + iuv + " Iur: " + iur + "] acquisita con errore: nessun versamento associato al pagamento.");
										continue;
									}
									
									rendicontazioneSenzaRpt.setDataRendicontazione(dsp.getDataEsitoSingoloPagamento());
									rendicontazioneSenzaRpt.setIdIuv(iuvModel.getId());
									rendicontazioneSenzaRpt.setImportoPagato(importoRendicontato);
									rendicontazioneSenzaRpt.setIur(iur);
									
									FrApplicazione frApplicazione = frRendicontazioniMap.get(iuvModel.getIdApplicazione());
									
									if(frApplicazione == null) {
										frApplicazione = new FrApplicazione();
										frApplicazione.setIdApplicazione(iuvModel.getIdApplicazione());
										frRendicontazioniMap.put(iuvModel.getIdApplicazione(), frApplicazione);
									}
									
									frApplicazione.addRendicontazioneSenzaRpt(rendicontazioneSenzaRpt);
								}
							}
	
							if(totaleImportiRendicontati.compareTo(flussoRendicontazione.getImportoTotalePagamenti()) != 0){
								GpThreadLocal.get().log("rendicontazioni.importoTotaleErrato");
								erroriAcquisizione.add("La somma degli importi rendicontati ["+totaleImportiRendicontati+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getImportoTotalePagamenti()  + "]");
								log.error("La somma degli importi rendicontati ["+totaleImportiRendicontati+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getImportoTotalePagamenti()  + "]");
							}
							
							try {
								if(flussoRendicontazione.getDatiSingoliPagamenti().size() != flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()) {
									GpThreadLocal.get().log("rendicontazioni.numeroRendicontazioniErrato");
									erroriAcquisizione.add("Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamenti().size()+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()  + "]");
									log.error("Il numero di pagamenti rendicontati ["+flussoRendicontazione.getDatiSingoliPagamenti().size()+"] non corrisponde al totale indicato nella testata del flusso [" + flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()  + "]");
								}	
							} catch (Exception e) {
								log.error("Numero pagamenti non intero: " + flussoRendicontazione.getNumeroTotalePagamenti());
							}
							
							
							// Se c'e' stato un errore rollback e commit del solo flusso errato
							if(erroriAcquisizione.size() == 0){
								setAutoCommit(false);
								fr.setStato(StatoFr.ACCETTATA);
								frBD.insertFr(fr);
								for(FrApplicazione frApplicazione : frRendicontazioniMap.values()) {
									frApplicazione.setIdFr(fr.getId());
									frBD.insertFrApplicazione(frApplicazione);
									
									if(frApplicazione.getPagamenti(this) != null) {
										for(it.govpay.bd.model.Pagamento pagamento : frApplicazione.getPagamenti(this)) {
											pagamento.setIdFrApplicazione(frApplicazione.getId());
											pagamentiBD.updatePagamento(pagamento);
										}
									}
									
									if(frApplicazione.getRendicontazioniSenzaRpt(this) != null) {
										for(RendicontazioneSenzaRpt rendicontazione : frApplicazione.getRendicontazioniSenzaRpt(this)) {
											rendicontazione.setIdFrApplicazioni(frApplicazione.getId());
											frBD.insertRendicontazioneSenzaRpt(rendicontazione);
										}
									}
								}
								this.commit();
								response.add(idRendicontazione.getIdentificativoFlusso() + "#Acquisiti " + flussoRendicontazione.getDatiSingoliPagamenti().size() + " pagamenti per un totale di " + flussoRendicontazione.getImportoTotalePagamenti() + " Euro.");
								log.info("Flusso di rendicontazione acquisito con successo.");
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoOk", idRendicontazione.getIdentificativoFlusso());
							} else {
								fr.setStato(StatoFr.RIFIUTATA);
								fr.setDescrizioneStato(StringUtils.join(erroriAcquisizione,"#"));
								frBD.insertFr(fr);
								log.error("Flusso di rendicontazione acquisito con errori: " + StringUtils.join(erroriAcquisizione,"#"));
								response.add(idRendicontazione.getIdentificativoFlusso() + "#Flusso rifiutato.");
								GpThreadLocal.get().log("rendicontazioni.acquisizioneFlussoKo", idRendicontazione.getIdentificativoFlusso(), StringUtils.join(erroriAcquisizione,"\n"));
							}
						}
					} finally {
						GpThreadLocal.get().closeTransaction(idTransaction2);
					}
				}
			}
			
		} catch(Exception e) {
			throw new GovPayException(e);
		}
		
		GpThreadLocal.get().log("rendicontazioni.acquisizioneOk");
		
		if(response.isEmpty()) {
			return "Acquisizione completata#Nessun flusso acquisito.";
		} else {
			return StringUtils.join(response,"|");
		}
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
					int annoFlusso = Integer.parseInt(simpleDateFormatAnno.format(idRendicontazione.getDataOraFlusso()));
					setupConnection(GpThreadLocal.get().getTransactionId());
					FrBD frBD = new FrBD(this);
					boolean exists = frBD.exists(annoFlusso, idRendicontazione.getIdentificativoFlusso());
					closeConnection();
					if(exists){
						GpThreadLocal.get().log("rendicontazioni.flussoDuplicato",  idRendicontazione.getIdentificativoFlusso(), annoFlusso + "");
						//response.add(idRendicontazione.getIdentificativoFlusso() + "#Flusso gia' acquisito");
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
	
	
	public List<FrApplicazione> chiediListaRendicontazioni(Applicazione applicazione) throws GovPayException, ServiceException {
		FrBD frBD = new FrBD(this);
		return frBD.getFrApplicazioni(applicazione.getId());
	}
	
	public List<Fr> chiediListaRendicontazioni(Applicazione applicazione, String codDominio, String codApplicazione, Date da, Date a) throws GovPayException, ServiceException, NotFoundException {
		AclEngine.isAuthorized(applicazione, Servizio.RENDICONTAZIONE, codDominio, null);
		
		FrBD frBD = new FrBD(this);
		
		long idDominio = 0;
		
		try {
			idDominio = AnagraficaManager.getDominio(this, codDominio).getId();
		} catch (Exception e) {
			throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
		}
		
		long idApplicazione = 0;
		if(codApplicazione != null) {
			try {
				idApplicazione = AnagraficaManager.getApplicazione(this, codApplicazione).getId();
			} catch (Exception e) {
				throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
			}
			return frBD.findAll(idDominio, idApplicazione, da, a);
		} else { 
			return frBD.findAll(idDominio, null, da, a);
		}
	}

	public FrApplicazione chiediRendicontazione(Applicazione applicazione, int anno, String codFlusso) throws GovPayException, ServiceException {
		FrBD frBD = new FrBD(this);
		try {
			return frBD.getFrApplicazione(applicazione.getId(), anno, codFlusso);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.RND_000);
		}
	}
}
