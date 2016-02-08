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
package it.govpay.business;

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
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Fr.StatoFr;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.SingolaRendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoRendicontazione;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.rendicontazione.FrBD;
import it.govpay.bd.rendicontazione.FrFilter;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.utils.JaxbUtils;
import it.govpay.web.wsclient.NodoPerPa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;


public class Rendicontazioni extends BasicBD {

	private static Logger log = LogManager.getLogger();

	public Rendicontazioni(BasicBD basicBD) {
		super(basicBD);
	}

	public void downloadRendicontazioni() throws GovPayException {

		try {
			DominiBD dominiBD = new DominiBD(this);
			List<Dominio> lstDomini = dominiBD.getDomini();
			closeConnection();
			for(Dominio dominioEnte : lstDomini) { 
				ThreadContext.put("dom", dominioEnte.getCodDominio());
				setupConnection();
				Stazione stazione = AnagraficaManager.getStazione(this, dominioEnte.getIdStazione());
				Intermediario intermediario = AnagraficaManager.getIntermediario(this, stazione.getIdIntermediario());
				
				NodoPerPa client = new NodoPerPa(intermediario);
				
				PspBD pspBD = new PspBD(this);
				List<Psp> lstPsp = pspBD.getPsp();
				
				closeConnection();
				
				SimpleDateFormat simpleDateFormatAnno = new SimpleDateFormat("yyyy");
				
				List<String> sids = new ArrayList<String>();
				for(Psp psp : lstPsp) {
					if(sids.contains(psp.getCodPsp())) continue;
					sids.add(psp.getCodPsp());
					log.trace("Download flussi rendicontazione dal psp [" + psp.getCodPsp() + "] in corso.");
					try {
						NodoChiediElencoFlussiRendicontazione richiesta = new NodoChiediElencoFlussiRendicontazione();
						richiesta.setIdentificativoDominio(dominioEnte.getCodDominio());
						richiesta.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
						richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione()); 
						richiesta.setPassword(stazione.getPassword());
						richiesta.setIdentificativoPSP(psp.getCodPsp());

						NodoChiediElencoFlussiRendicontazioneRisposta risposta;
						try {
							risposta = client.nodoChiediElencoFlussiRendicontazione(richiesta, intermediario.getDenominazione());
						} catch (Exception e) {
							// Errore nella richiesta. Loggo e continuo con il prossimo psp
							log.error("Richiesta elenco flussi rendicontazione per il psp [" + psp.getCodPsp() + "] fallita", e);
							continue;
						}

						if(risposta.getFault() != null) {
							// Errore nella richiesta. Loggo e continuo con il prossimo psp
							log.error("Richiesta elenco flussi rendicontazione per il psp [" + psp.getCodPsp() + "] fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
							continue;
						} else {
							log.trace("Ritornati " + risposta.getElencoFlussiRendicontazione().getTotRestituiti() + " flussi rendicontazione per il psp [" + psp.getCodPsp() + "]");
							// Per ogni flusso della lista, vedo se ce l'ho gia' in DB ed in caso lo archivio
							
							for(TipoIdRendicontazione idRendicontazione : risposta.getElencoFlussiRendicontazione().getIdRendicontazione()) {
								int annoFlusso = Integer.parseInt(simpleDateFormatAnno.format(idRendicontazione.getDataOraFlusso()));
								setupConnection();
								FrBD frBD = new FrBD(this);
								boolean exists = frBD.exists(annoFlusso, idRendicontazione.getIdentificativoFlusso());
								closeConnection();
								if(!exists){
									log.info("Individuato flusso non presente negli archivi: " + risposta.getElencoFlussiRendicontazione().getTotRestituiti() + " flussi rendicontazione per il psp [" + psp.getCodPsp() + "]");
									// Il documento non e' stato archiviato. Lo richiedo e lo memorizzo
									NodoChiediFlussoRendicontazione richiestaFlusso = new NodoChiediFlussoRendicontazione();
									richiestaFlusso.setIdentificativoDominio(dominioEnte.getCodDominio());
									richiestaFlusso.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
									richiestaFlusso.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
									richiestaFlusso.setPassword(stazione.getPassword());
									richiestaFlusso.setIdentificativoPSP(psp.getCodPsp());
									richiestaFlusso.setIdentificativoFlusso(idRendicontazione.getIdentificativoFlusso());

									NodoChiediFlussoRendicontazioneRisposta rispostaFlusso;
									try {
										rispostaFlusso = client.nodoChiediFlussoRendicontazione(richiestaFlusso, intermediario.getDenominazione());
									} catch (Exception e) {
										// Errore nella richiesta. Loggo e continuo con il prossimo flusso
										log.error("Richiesta flusso  rendicontazione [" + idRendicontazione.getIdentificativoFlusso() + "] per il psp [" + psp.getCodPsp() + "] fallita: " + e);
										continue;
									} 

									if(risposta.getFault() != null) {
										// Errore nella richiesta. Loggo e continuo con il prossimo flusso
										log.error("Richiesta flusso  rendicontazione [" + idRendicontazione.getIdentificativoFlusso() + "] per il psp [" + psp.getCodPsp() + "] fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
									} else {
										byte[] tracciato = null;
										try {
											ByteArrayOutputStream output = new ByteArrayOutputStream();
											DataHandler dh = rispostaFlusso.getXmlRendicontazione();
											dh.writeTo(output);
											tracciato = output.toByteArray();
										} catch (IOException e) {
											log.error("Errore durante la lettura del flusso di rendicontazione: " + e);
											continue;
										}
										
										CtFlussoRiversamento flussoRendicontazione = null;
										try {
											flussoRendicontazione = JaxbUtils.toFR(tracciato);
										} catch (Exception e) {
											log.error("Errore durante il parsing del flusso di rendicontazione: " + e);
											continue;
										}
										
										Fr fr = new Fr();
										fr.setAnnoRiferimento(annoFlusso);
										fr.setCodFlusso(idRendicontazione.getIdentificativoFlusso());
										fr.setIdPsp(psp.getId());
										fr.setIur(flussoRendicontazione.getIdentificativoUnivocoRegolamento());
										fr.setDataOraFlusso(flussoRendicontazione.getDataOraFlusso());
										fr.setDataRegolamento(flussoRendicontazione.getDataRegolamento());
										fr.setNumeroPagamenti(flussoRendicontazione.getNumeroTotalePagamenti().longValue());
										fr.setImportoTotalePagamenti(flussoRendicontazione.getImportoTotalePagamenti().doubleValue());
										fr.setIdDominio(dominioEnte.getId());
										
										log.info("Flusso ricevuto: " + flussoRendicontazione.getDatiSingoliPagamenti().size() + " singoli pagamenti");

										BigDecimal totaleImportiPagati = BigDecimal.ZERO;
										// Acquisizione dati di rendicontazione
										List<String> erroriAcquisizione = new ArrayList<String>();
										
										setupConnection();
										setAutoCommit(false);
										VersamentiBD versamentiBD = new VersamentiBD(this);
										frBD = new FrBD(this);
										
										for(CtDatiSingoliPagamenti dsp : flussoRendicontazione.getDatiSingoliPagamenti()) {
											
											SingolaRendicontazione sr = new SingolaRendicontazione();

											String iur = dsp.getIdentificativoUnivocoRiscossione();
											String iuv = dsp.getIdentificativoUnivocoVersamento();
											BigDecimal importoPagato = dsp.getSingoloImportoPagato();
											
											log.debug("Rendicontazione [CodDominio: " + dominioEnte.getCodDominio() + "] [Iuv: "+ dsp.getIdentificativoUnivocoVersamento() + "][Iur: " + iur + "]");

											totaleImportiPagati = totaleImportiPagati.add(importoPagato);

											sr.setCodiceEsito(Integer.parseInt(dsp.getCodiceEsitoSingoloPagamento()));
											sr.setDataEsito(dsp.getDataEsitoSingoloPagamento());
											sr.setIur(iur);
											sr.setIuv(iuv);
											sr.setSingoloImporto(dsp.getSingoloImportoPagato().longValue());

											Versamento versamento;
											try {
												versamento = versamentiBD.getVersamento(dominioEnte.getCodDominio(), iuv); 
											} catch (NotFoundException e) {
												erroriAcquisizione.add("Versamento rendicontato non trovato: [CodDominio: " + dominioEnte.getCodDominio() + "] [Iuv: "+ iuv + "]");
												continue;
											}

											boolean found = false;
											boolean rendicontazioneIncompleta = false;
											for(SingoloVersamento singoloVersamento : versamento.getSingoliVersamenti()) {
												if(iur.equals(singoloVersamento.getIur())) {
													sr.setIdSingoloVersamento(singoloVersamento.getId());
													singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.RENDICONTATO);
													versamentiBD.updateStatoSingoloVersamento(singoloVersamento.getId(), StatoSingoloVersamento.RENDICONTATO);
													
													// Esitare?
													//EsitiBD esiti = new EsitiBD(this);
													//Applicazione applicazione = AnagraficaManager.getApplicazione(this, versamento.getIdApplicazione());
													//Esito esito = EsitoFactory.newEsito(this, TipoEsito.RENDICONTAZIONE, applicazione, versamento, null, null, null);
													//esiti.insertEsito(esito);
													
													found = true;
													
													if(singoloVersamento.getSingoloImportoPagato().compareTo(importoPagato) != 0) {
														erroriAcquisizione.add("L'importo rendicontato ["+importoPagato+"] non corrisponde con quello versato [CodDominio: " + dominioEnte.getCodDominio() + "][Iuv: "+ iuv + "][Iur: " + iur + "]");
														break;
													}
												}
												// Controllo se ci sono singoli versamenti ancora da rendicontare per questo versamento
												if(singoloVersamento.getStatoSingoloVersamento().equals(StatoSingoloVersamento.PAGATO)) {
													rendicontazioneIncompleta = true;
												}
											}

											if(!found) {
												erroriAcquisizione.add("Non e' stato trovato nessun singolo versamento nel versamento con lo IUR indicato: [CodDominio: " + dominioEnte.getCodDominio() + "][Iuv: "+ iuv + "][Iur: " + iur + "]");
											}
											
											if(rendicontazioneIncompleta) {
												versamentiBD.updateStatoVersamento(versamento.getId(), StatoRendicontazione.RENDICONTATO_PARZIALE);
											} else {
												versamentiBD.updateStatoVersamento(versamento.getId(), StatoRendicontazione.RENDICONTATO);
											}
											
											fr.getSingolaRendicontazioneList().add(sr);
										}

										if(totaleImportiPagati.compareTo(flussoRendicontazione.getImportoTotalePagamenti()) != 0){
											erroriAcquisizione.add("La somma degli importi rendicontati nei singoli pagamenti ["+totaleImportiPagati+"] non corrisponde al totale indicato nel flusso");
										}
										
										// Se c'e' stato un errore rollback e commit del solo flusso errato
										if(erroriAcquisizione.size() == 0){
											fr.setStato(StatoFr.ACQUISITA);
											frBD.insertFr(fr, tracciato);
											this.commit();
										} else {
											this.rollback();
											fr.getSingolaRendicontazioneList().clear();
											fr.setStato(StatoFr.RIFIUTATA);
											fr.setDescrizioneStato(StringUtils.join(erroriAcquisizione,"#"));
											frBD.insertFr(fr, tracciato);
											this.commit();
										}
									}
								}
							}
						}
					} catch (Exception e) {
						log.error("Errore durante l'acquisizione dei flussi di rendicontazione per il PSP [" + psp.getCodPsp() + "]", e);
						continue;
					}
				}
			}
		} catch(Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante l'acquisizione delle rendicontazioni", e);
		}
	}
	
	public Fr getFlusso(long idFr) throws GovPayException {
		try {
			FrBD frBD = new FrBD(this);
			return frBD.getFr(idFr);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
	
	public List<Fr> getFlussi(List<Long> idDomini) throws GovPayException {
		try {
			FrBD frBD = new FrBD(this);
			FrFilter filter = frBD.newFilter();
			filter.setIdDomini(idDomini);
			return frBD.findAll(filter);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
}