/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

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
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.EventoUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Intermediario;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.Versamento.TipologiaTipoVersamento;
import it.govpay.model.configurazione.Giornale;
import it.govpay.model.eventi.DatiPagoPA;
import it.govpay.pagopa.beans.utils.JaxbUtils;


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
		private TipoIdRendicontazione idFlussoRendicontazione;
		private String codDominio;
		private String idFlusso;
		private List<String> errori;
		private File frFile;
		
		public RendicontazioneScaricata(TipoIdRendicontazione idFlussoRendicontazione, String codDominio) {
			this.setIdFlussoRendicontazione(idFlussoRendicontazione);
			this.codDominio = codDominio;
			this.idFlusso = idFlussoRendicontazione.getIdentificativoFlusso();
			setErrori(new ArrayList<String>());
		}
		
		public RendicontazioneScaricata(TipoIdRendicontazione idFlussoRendicontazione, String codDominio, File file) {
			this(idFlussoRendicontazione, codDominio);
			this.frFile = file;
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

		public File getFrFile() {
			return frFile;
		}

		public void setFrFile(File frFile) {
			this.frFile = frFile;
		}

		public TipoIdRendicontazione getIdFlussoRendicontazione() {
			return idFlussoRendicontazione;
		}

		public void setIdFlussoRendicontazione(TipoIdRendicontazione idFlussoRendicontazione) {
			this.idFlussoRendicontazione = idFlussoRendicontazione;
		}

		public String getCodDominio() {
			return codDominio;
		}

		public void setCodDominio(String codDominio) {
			this.codDominio = codDominio;
		}
		
	}

	private static Logger log = LoggerWrapperFactory.getLogger(Rendicontazioni.class);

	public Rendicontazioni() {
	}

	public DownloadRendicontazioniResponse downloadRendicontazioni(IContext ctx) throws GovPayException, UtilsException { 
		int frAcquisiti = 0;
		int frNonAcquisiti = 0;
		
		DownloadRendicontazioniResponse response = new DownloadRendicontazioniResponse();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		log.info("Acquisizione dei flussi di rendicontazione in corso...");
		try {
			ctx.getApplicationLogger().log("rendicontazioni.acquisizione");
			DominiBD dominiBD = new DominiBD(ctx.getTransactionId());
			Giornale giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();

			StazioniBD stazioniBD = new StazioniBD(configWrapper);
			List<Stazione> lstStazioni = stazioniBD.getStazioni();

			for(Stazione stazione : lstStazioni) {
				List<RendicontazioneScaricata> flussiDaPagoPA = new ArrayList<>();

				Intermediario intermediario = stazione.getIntermediario(configWrapper);

				DominioFilter filter = dominiBD.newFilter();
				filter.setCodStazione(stazione.getCodStazione());
				List<Dominio> lstDomini = dominiBD.findAll(filter);

				// La lista deve essere richiesta per dominio visto che pagoPA non garantisce l'univocita globale per idFlusso
				for(Dominio dominio : lstDomini) { 
					log.info(MessageFormat.format("Richiesta lista dei flussi di rendicontazione per il dominio [{0}] ...",	dominio.getCodDominio()));
					List<TipoIdRendicontazione> chiediListaFrResponse = this.chiediListaFr(stazione, dominio, giornale);
					for(TipoIdRendicontazione i : chiediListaFrResponse)
						flussiDaPagoPA.add(new RendicontazioneScaricata(i, dominio.getCodDominio()));
				}
				
				// Aggiungo alla lista quelli su FileSystem
				File dir = new File(GovpayConfig.getInstance().getResourceDir() + File.separatorChar + "input" + File.separatorChar + "fr");
				if(dir.exists() && dir.isDirectory()) {
					File [] files = dir.listFiles(new FilenameFilter() {
					    @Override
					    public boolean accept(File dir, String name) {
					        return name.endsWith(".xml");
					    }
					});
	
					if(files.length == 0) 
						log.debug("Cartella di acquisizione FR vuota");
					for (File xmlfile : files) {
						log.info("Trovato Flusso di Rendicontazione da acquisire su FileSystem: {}", xmlfile.getAbsolutePath());
						FlussoRiversamento flussoRendicontazione = null;
						try {
							flussoRendicontazione = JaxbUtils.toFR(FileUtils.readFileToByteArray(xmlfile));
							TipoIdRendicontazione idRendicontazione = new TipoIdRendicontazione();
							idRendicontazione.setDataOraFlusso(flussoRendicontazione.getDataOraFlusso());
							idRendicontazione.setIdentificativoFlusso(flussoRendicontazione.getIdentificativoFlusso());
							flussiDaPagoPA.add(new RendicontazioneScaricata(idRendicontazione, flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco(), xmlfile));
						} catch (Exception e) {
							log.error(MessageFormat.format("Impossibile acquisire il flusso di rendicontazione da file: {0}", xmlfile.getAbsolutePath()), e);
						}
					}
				} else {
					log.debug(MessageFormat.format("Cartella di acquisizione FR non presente: {0}{1}input{2}fr", GovpayConfig.getInstance().getResourceDir(), File.separatorChar, File.separatorChar));
				}

				FrBD frBD = new FrBD(configWrapper);
				
				// Lista per i flussi che dovremo acquisire
				List<RendicontazioneScaricata> flussiDaAcquisire = new ArrayList<>();
				// Elenco dei riferimenti ai flussi per verificare che la lista da acquisire non abbia duplicati				
				Set<String> keys = new HashSet<String>();
				log.info("Verifica esistenza sul db dei flussi da acquisire...");
				for(RendicontazioneScaricata rnd : flussiDaPagoPA) {
					TipoIdRendicontazione idRendicontazione = rnd.getIdFlussoRendicontazione();
					// Controllo che il flusso non sia su db
					try {
						log.debug(MessageFormat.format("Verifico presenza del flusso [{0}, {1}, {2}]", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso()) );
						// Uso la GET perche' la exists risulta buggata con la data nella tupla di identificazione
						frBD.getFr(rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso());
						log.debug(MessageFormat.format("Flusso di rendicontazione [{0}, {1}, {2}] gia'' acquisito", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso()) );
						// C'e' gia. Se viene da file, lo elimino
						if(rnd.getFrFile() != null) {
							try { 
								boolean delete = rnd.getFrFile().delete();
								if(!delete) {
									log.warn(MessageFormat.format("Il file di flusso {0} non e'' stato eliminato.", rnd.getFrFile().getName()));
								}
							} catch (Exception e) { 
								log.error(MessageFormat.format("Impossibile eliminare il file di flusso gia'' presente: {0}", rnd.getFrFile().getName()), e);
							}
						}
					} catch (NotFoundException e) {
						// Flusso originale, lo aggiungo ma controllo che non sia gia' nella lista di quelli da aggiungere
						if(!keys.contains(rnd.getCodDominio() + idRendicontazione.getIdentificativoFlusso() + idRendicontazione.getDataOraFlusso().getTime())) {
							log.info(MessageFormat.format("Flusso di rendicontazione [{0}, {1}, {2}] da acquisire", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso()) );
							flussiDaAcquisire.add(rnd);
							keys.add(rnd.getCodDominio() + idRendicontazione.getIdentificativoFlusso() + idRendicontazione.getDataOraFlusso().getTime());
						}
					}
				}
				
				log.info("Individuati {} flussi di rendicontazione da acquisire", flussiDaAcquisire.size());

				for(RendicontazioneScaricata rnd : flussiDaAcquisire) {
					TipoIdRendicontazione idRendicontazione = rnd.getIdFlussoRendicontazione();
					log.info(MessageFormat.format("Acquisizione flusso di rendicontazione {0} {1}", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso()));
					
					
					boolean hasFrAnomalia = false;
					boolean isAggiornamento = false;
					
					NodoClient chiediFlussoRendicontazioneClient = null;
					
					try {
						byte[] tracciato = null;
						if(rnd.getFrFile() == null) {
							appContext.setupNodoClient(stazione.getCodStazione(), null, EventoContext.Azione.NODOCHIEDIFLUSSORENDICONTAZIONE);
							appContext.getRequest().addGenericProperty(new Property("codStazione", stazione.getCodStazione()));
							appContext.getRequest().addGenericProperty(new Property("idFlusso", idRendicontazione.getIdentificativoFlusso()));
							
							NodoChiediFlussoRendicontazione richiestaFlusso = new NodoChiediFlussoRendicontazione();
							richiestaFlusso.setIdentificativoIntermediarioPA(stazione.getIntermediario(configWrapper).getCodIntermediario());
							richiestaFlusso.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
							richiestaFlusso.setIdentificativoDominio(rnd.codDominio);
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
								rnd.getErrori().add(MessageFormat.format("Richiesta al nodo fallita: {0}.", e.getMessage()));
								log.error(MessageFormat.format("Richiesta flusso rendicontazione [{0}] fallita: {1}", idRendicontazione.getIdentificativoFlusso(), e.getMessage()), e);
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", e.getMessage());
								continue;
							} 
	
							if(risposta.getFault() != null) {
								// Errore nella richiesta. Loggo e continuo con il prossimo flusso
								rnd.getErrori().add(MessageFormat.format("Richiesta al nodo fallita: {0} {1}.", risposta.getFault().getFaultCode(), risposta.getFault().getFaultString()));
								log.error(MessageFormat.format("Richiesta flusso rendicontazione [{0}] fallita: {1} {2}", idRendicontazione.getIdentificativoFlusso(), risposta.getFault().getFaultCode(), risposta.getFault().getFaultString()));
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoKo", risposta.getFault().getFaultCode(), risposta.getFault().getFaultString(), risposta.getFault().getDescription());
								if(chiediFlussoRendicontazioneClient != null) {
									chiediFlussoRendicontazioneClient.getEventoCtx().setSottotipoEsito(risposta.getFault().getFaultCode());
									chiediFlussoRendicontazioneClient.getEventoCtx().setEsito(Esito.KO);
									chiediFlussoRendicontazioneClient.getEventoCtx().setDescrizioneEsito(risposta.getFault().getFaultString());
								}
							} else {
								try {
									ByteArrayOutputStream output = new ByteArrayOutputStream();
									DataHandler dh = risposta.getXmlRendicontazione();
									dh.writeTo(output);
									tracciato = output.toByteArray();
								} catch (IOException e) {
									rnd.getErrori().add(MessageFormat.format("Lettura del flusso fallita: {0}.", e.getMessage()));
									log.error("Errore durante la lettura del flusso di rendicontazione", e);
									ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Lettura del flusso fallita: " + e);
									continue;
								}
							}
						} else {
							try {
								tracciato = FileUtils.readFileToByteArray(rnd.getFrFile());
							} catch (IOException e) {
								rnd.getErrori().add(MessageFormat.format("Lettura del flusso fallita: {0}.", e.getMessage()));
								log.error("Errore durante la lettura del flusso di rendicontazione da file: " + rnd.getFrFile().getAbsolutePath(), e);
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Lettura del flusso fallita: " + e);
								continue;
							}
						}

						FlussoRiversamento flussoRendicontazione = null;
						try {
							flussoRendicontazione = JaxbUtils.toFR(tracciato);
						} catch (JAXBException | SAXException e) {
							rnd.getErrori().add(MessageFormat.format("Parsing del flusso fallita: {0}.", e.getMessage()));
							log.error("Errore durante il parsing del flusso di rendicontazione", e);
							ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Errore durante il parsing del flusso di rendicontazione: " + e);
							continue;
						}

						log.info(MessageFormat.format("Ricevuto flusso rendicontazione per {0} singoli pagamenti", flussoRendicontazione.getDatiSingoliPagamentis().size()));

						ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlusso");
						appContext.getRequest().addGenericProperty(new Property("trn", flussoRendicontazione.getIdentificativoUnivocoRegolamento()));
						
						Fr fr = new Fr();
						fr.setObsoleto(false);
						fr.setCodBicRiversamento(flussoRendicontazione.getCodiceBicBancaDiRiversamento());
						fr.setCodFlusso(idRendicontazione.getIdentificativoFlusso());
						fr.setIur(flussoRendicontazione.getIdentificativoUnivocoRegolamento());
						fr.setDataAcquisizione(new Date());
						fr.setDataFlusso(idRendicontazione.getDataOraFlusso());
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
						log.debug(MessageFormat.format("Identificativo PSP estratto dall''identificativo flusso: {0}", codPsp));
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

							log.info(MessageFormat.format("Rendicontato (Esito {0}) per un importo di ({1}) [CodDominio: {2}] [Iuv: {3}][Iur: {4}]",
									dsp.getCodiceEsitoSingoloPagamento(), dsp.getSingoloImportoPagato(), codDominio, dsp.getIdentificativoUnivocoVersamento(), iur));

							it.govpay.bd.model.Rendicontazione rendicontazione = new it.govpay.bd.model.Rendicontazione();

							// Gestisco un codice esito non supportato
							try {
								rendicontazione.setEsito(EsitoRendicontazione.toEnum(dsp.getCodiceEsitoSingoloPagamento()));
							} catch (Exception e) {
								ctx.getApplicationLogger().log("rendicontazioni.esitoSconosciuto", iuv, iur, dsp.getCodiceEsitoSingoloPagamento() == null ? "null" : dsp.getCodiceEsitoSingoloPagamento());
								rendicontazione.addAnomalia("007110", MessageFormat.format("Codice esito [{0}] sconosciuto", dsp.getCodiceEsitoSingoloPagamento()));
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
										log.info(MessageFormat.format("Revoca [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}] rendicontato con errore: l''importo rendicontato [{4}] non corrisponde a quanto stornato [{5}]",
												codDominio, iuv, iur, indiceDati, importoRendicontato.doubleValue(), pagamento.getImportoRevocato().doubleValue()));
										rendicontazione.addAnomalia("007112", MessageFormat.format("L''importo rendicontato [{0}] non corrisponde a quanto stornato [{1}]",
												importoRendicontato.doubleValue(), pagamento.getImportoRevocato().doubleValue()));
									}

								} else {
									if(pagamento.getImportoPagato().compareTo(importoRendicontato) != 0) {
										ctx.getApplicationLogger().log("rendicontazioni.importoErrato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
										log.info(MessageFormat.format("Pagamento [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}] rendicontato con errore: l''importo rendicontato [{4}] non corrisponde a quanto pagato [{5}]",
												codDominio, iuv, iur, indiceDati, importoRendicontato.doubleValue(), pagamento.getImportoPagato().doubleValue()));
										rendicontazione.addAnomalia("007104", MessageFormat.format("L''importo rendicontato [{0}] non corrisponde a quanto pagato [{1}]",
												importoRendicontato.doubleValue(), pagamento.getImportoPagato().doubleValue()));
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
												versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(configWrapper, codApplicazione), null, null, null, codDominio, iuv, TipologiaTipoVersamento.DOVUTO, log);
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
										rnd.getErrori().add(MessageFormat.format("Acquisizione flusso fallita. Riscontrato errore nell''acquisizione del versamento dall''applicazione gestrice [Transazione: {0}].", ctx.getTransactionId()));
										log.error(MessageFormat.format("Errore durante il processamento del flusso di Rendicontazione [Flusso:{0}]: impossibile acquisire i dati del versamento [Dominio:{1} Iuv:{2}]. Flusso non acquisito.", idRendicontazione.getIdentificativoFlusso(), codDominio, iuv));
										ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoKo", idRendicontazione.getIdentificativoFlusso(), "Impossibile acquisire i dati di un versamento dall'applicativo gestore [Applicazione:" + codApplicazione + " Dominio:" + codDominio+ " Iuv:" + iuv + "].  Flusso non acquisito.");
										throw new GovPayException(ce);
									}

									if(versamento == null) {
										// non ho trovato il versamento 
										ctx.getApplicationLogger().log("rendicontazioni.senzaRptNoVersamento", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
										log.info(MessageFormat.format("Pagamento [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.",	codDominio, iuv, iur, indiceDati));
										rendicontazione.addAnomalia("007111", "Il versamento risulta sconosciuto: " + erroreVerifica);
										continue;
									} else {
										
										List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
										if(singoliVersamenti.size() != 1) {
											// Un pagamento senza rpt DEVE riferire un pagamento tipo 3 con un solo singolo versamento
											ctx.getApplicationLogger().log("rendicontazioni.senzaRptVersamentoMalformato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
											log.info(MessageFormat.format("Pagamento [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.",	codDominio, iuv, iur, indiceDati));
											rendicontazione.addAnomalia("007114", "Il versamento presenta piu' singoli versamenti");
											continue;
										}
										
										rendicontazione.setIdSingoloVersamento(singoliVersamenti.get(0).getId());
										continue;
									}
								}

								ctx.getApplicationLogger().log("rendicontazioni.noPagamento", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
								log.info(MessageFormat.format("Pagamento [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}] rendicontato con errore: il pagamento non risulta presente in base dati.", codDominio, iuv, iur, indiceDati));
								rendicontazione.addAnomalia("007101", "Il pagamento riferito dalla rendicontazione non risulta presente in base dati.");
							} catch (MultipleResultException e) {
								// Individuati piu' pagamenti riferiti dalla rendicontazione
								ctx.getApplicationLogger().log("rendicontazioni.poliPagamento", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
								log.info(MessageFormat.format("Pagamento rendicontato duplicato: [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}]",	codDominio, iuv, iur, indiceDati));
								rendicontazione.addAnomalia("007102", "La rendicontazione riferisce piu di un pagamento gestito.");
								fr.addAnomalia("007102", "La rendicontazione riferisce piu di un pagamento gestito.");
							} finally {
								//controllo che non sia gia' stata  acquisita un rendicontazione per la tupla (codDominio,iuv,iur,indiceDati), in questo caso emetto una anomalia
								if(fr.getRendicontazioni() != null) {
									for (Rendicontazione r2 : fr.getRendicontazioni()) {
										if(r2.getIuv().equals(rendicontazione.getIuv()) 
												&& r2.getIur().equals(rendicontazione.getIur()) 
												&& (   (r2.getIndiceDati() == null && rendicontazione.getIndiceDati()==null) 
														|| 
													   (r2.getIndiceDati() != null && rendicontazione.getIndiceDati()!=null && r2.getIndiceDati().compareTo(rendicontazione.getIndiceDati()) == 0)) 
												) {
											log.info(MessageFormat.format("Rendicontazione [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}] duplicata all''interno del flusso, in violazione delle specifiche PagoPA. Necessario intervento manuale per la risoluzione del problema.", codDominio, iuv, iur, indiceDati));
											rendicontazione.addAnomalia("007115",
													MessageFormat.format("Rendicontazione [Dominio:{0} Iuv:{1} Iur:{2} Indice:{3}] duplicata all''interno del flusso, in violazione delle specifiche PagoPA. Necessario intervento manuale per la risoluzione del problema.", codDominio, iuv, iur, indiceDati));	
											rendicontazione.setStato(StatoRendicontazione.ANOMALA);
											hasFrAnomalia = true;
											break;
										}
									}
								}
								
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
							log.info(MessageFormat.format("La somma degli importi rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [{1}]", totaleImportiRendicontati, flussoRendicontazione.getImportoTotalePagamenti()));
							fr.addAnomalia("007106", MessageFormat.format("La somma degli importi rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [{1}]", totaleImportiRendicontati, flussoRendicontazione.getImportoTotalePagamenti()));
						}

						try {
							if(flussoRendicontazione.getDatiSingoliPagamentis().size() != flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()) {
								ctx.getApplicationLogger().log("rendicontazioni.numeroRendicontazioniErrato");
								log.info(MessageFormat.format("Il numero di pagamenti rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [{1}]", flussoRendicontazione.getDatiSingoliPagamentis().size(), flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()));
								fr.addAnomalia("007107", MessageFormat.format("Il numero di pagamenti rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [{1}]", flussoRendicontazione.getDatiSingoliPagamentis().size(), flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()));
							}	
						} catch (Exception e) {
							ctx.getApplicationLogger().log("rendicontazioni.numeroRendicontazioniErrato");
							log.info(MessageFormat.format("Il numero di pagamenti rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [????]", flussoRendicontazione.getDatiSingoliPagamentis().size()));
							fr.addAnomalia("007107", MessageFormat.format("Il numero di pagamenti rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [????]", flussoRendicontazione.getDatiSingoliPagamentis().size()));
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
							
							// Controllo se c'e' gia' un'altra versione
							try {
								Fr frEsistente = frBD.getFr(fr.getCodDominio(), fr.getCodFlusso());
								
								// Ok, c'e' gia' una versione in DB. Vedo e' la data e' precedente o successiva
								if(frEsistente.getDataFlusso().before(fr.getDataFlusso())) {
									
									// Flusso su DB vecchio. Lo aggiorno come obsoleto e aggiungo il nuovo
									log.info(MessageFormat.format("Trovata versione precedente [{0}] da marcare come obsoleta.", fr.getCodFlusso()));
									frBD.updateObsoleto(frEsistente.getId(), true);
									isAggiornamento=true;
								} else {
									// Flusso su DB gia' recente. Lascio tutto fare e inserisco quello nuovo come obsoleto.
									log.info(MessageFormat.format("Trovata versione successiva [{0}]. Il nuovo flusso viene marcato come obsoleto.", fr.getCodFlusso()));
									fr.setObsoleto(true);
								}
							} catch (NotFoundException e) {
								log.debug(MessageFormat.format("Nessuna versione alternativa [{0}].", fr.getCodFlusso()));
							}
							
							frBD.insertFr(fr);
							
							
							for(Rendicontazione r : fr.getRendicontazioni()) {
								r.setIdFr(fr.getId());
								rendicontazioniBD.insert(r);
							}
							rendicontazioniBD.commit();
							frAcquisiti++;
							
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
							
						}catch (ServiceException e) {
							if(!rendicontazioniBD.isAutoCommit())
								rendicontazioniBD.rollback();
							
							log.error(MessageFormat.format("Flusso di rendicontazione non acquisito: {0}", e.getMessage()), e);
							//throw e;
						} finally {
							rendicontazioniBD.closeConnection();
						}
					} catch (GovPayException ce) {
						log.error("Flusso di rendicontazione non acquisito", ce);
						frNonAcquisiti++;
					} finally {
						if(chiediFlussoRendicontazioneClient != null && chiediFlussoRendicontazioneClient.getEventoCtx().isRegistraEvento()) {
							EventiBD eventiBD = new EventiBD(configWrapper);
							Evento eventoDTO = EventoUtils.toEventoDTO(chiediFlussoRendicontazioneClient.getEventoCtx(),log);
							if(isAggiornamento)
								eventoDTO.setSottotipoEvento(EventoContext.APIPAGOPA_SOTTOTIPOEVENTO_FLUSSO_RENDICONTAZIONE_DUPLICATO);
							eventiBD.insertEvento(eventoDTO);
							
						}
					}
				}
			}
			
		} catch(Exception e) {
			ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiFail", e.getMessage());
			response.descrizioneEsito=MessageFormat.format("Impossibile acquisire i flussi: {0}", e.getMessage());
			log.error(MessageFormat.format("Acquisizione dei flussi di rendicontazione in completata con errore: {0}", e.getMessage()), e);
			throw new GovPayException(e);
		} finally {
		}
		response.descrizioneEsito = MessageFormat.format("Operazione completata: {0} flussi acquisiti", frAcquisiti);
		if(frNonAcquisiti > 0) response.descrizioneEsito += MessageFormat.format(" e {0}non acquisiti per errori", frNonAcquisiti);
		ctx.getApplicationLogger().log("rendicontazioni.acquisizioneOk");
		log.info("Acquisizione dei flussi di rendicontazione in completata.");
		
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
		
		if(dominio.getAuxDigit() == 1) {
			// AuxDigit 1: Ente monointermediato. 
			// Per i pagamenti di tipo 1 e 2, se non ho trovato il pagamento e sono arrivato qui, posso assumere che non e' interno.
			// Per i pagamenti di tipo 3, e' mio se e' di 17 cifre.
			// Quindi controllo solo se e' numerico e di 17 cifre.

			if(isNumerico && iuv.length() == 17)
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
			appContext.setupNodoClient(stazione.getCodStazione(), dominio != null ? dominio.getCodDominio() : null, EventoContext.Azione.NODOCHIEDIELENCOFLUSSIRENDICONTAZIONE);
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
				log.debug(MessageFormat.format("Richiesta elenco flussi rendicontazione per il dominio [{0}] al nodo in corso...", dominio.getCodDominio()));
				risposta = chiediFlussoRendicontazioniClient.nodoChiediElencoFlussiRendicontazione(richiesta, intermediario.getDenominazione());
				chiediFlussoRendicontazioniClient.getEventoCtx().setEsito(Esito.OK);
				log.debug(MessageFormat.format("Richiesta elenco flussi rendicontazione per il dominio [{0}] al nodo completata.", dominio.getCodDominio()));
			} catch (Exception e) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				log.error(MessageFormat.format("Richiesta elenco flussi rendicontazione per il dominio [{0}] al nodo completata con errore {1}.", dominio.getCodDominio(), e.getMessage()), e);
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
				log.warn(MessageFormat.format("Richiesta elenco flussi rendicontazione per il dominio [{0}] fallita: {1} {2}", dominio.getCodDominio(), risposta.getFault().getFaultCode(), risposta.getFault().getFaultString()));
				ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiKo", risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
				if(chiediFlussoRendicontazioniClient != null) {
					chiediFlussoRendicontazioniClient.getEventoCtx().setSottotipoEsito(risposta.getFault().getFaultCode());
					chiediFlussoRendicontazioniClient.getEventoCtx().setEsito(Esito.KO);
					chiediFlussoRendicontazioniClient.getEventoCtx().setDescrizioneEsito(risposta.getFault().getFaultString());
				}	
				return flussiDaAcquisire;
			} else {

				if(risposta.getElencoFlussiRendicontazione() == null || risposta.getElencoFlussiRendicontazione().getTotRestituiti() == 0) {
					log.info(MessageFormat.format("Richiesta elenco flussi rendicontazione per il dominio [{0}]: ritornata lista vuota dal psp", dominio.getCodDominio()));
					ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiOk", "0");
					return flussiDaAcquisire;
				}

				ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiOk", risposta.getElencoFlussiRendicontazione().getTotRestituiti() + "");
				log.info(MessageFormat.format("Richiesta elenco flussi rendicontazione per il dominio [{0}]: ritornati {1} flussi.", dominio.getCodDominio(), risposta.getElencoFlussiRendicontazione().getTotRestituiti()));
				
				for(TipoIdRendicontazione idRendicontazione : risposta.getElencoFlussiRendicontazione().getIdRendicontazione()) {
					log.debug(MessageFormat.format("Ricevuto flusso rendicontazione: {0}, {1}", idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso()));
					flussiDaAcquisire.add(idRendicontazione);
				}
			}
		} catch (ServiceException e) {
			log.error("Errore durante l'acquisizione dei flussi di rendicontazione", e);
			return flussiDaAcquisire;
		}  finally {
			if(chiediFlussoRendicontazioniClient != null && chiediFlussoRendicontazioniClient.getEventoCtx().isRegistraEvento()) {
				try {
					EventiBD eventiBD = new EventiBD(configWrapper);
					eventiBD.insertEvento(EventoUtils.toEventoDTO(chiediFlussoRendicontazioniClient.getEventoCtx(),log));
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
		datiPagoPA.setCodStazione(stazione.getCodStazione());
		datiPagoPA.setCodIntermediario(intermediario.getCodIntermediario());
		datiPagoPA.setErogatore(it.govpay.model.Evento.NDP);
		datiPagoPA.setFruitore(intermediario.getCodIntermediario());
		if(codFlusso != null)
			datiPagoPA.setCodFlusso(codFlusso);
		if(dominio != null)
			datiPagoPA.setCodDominio(dominio.getCodDominio());
		eventoCtx.setDatiPagoPA(datiPagoPA);
	}
}
