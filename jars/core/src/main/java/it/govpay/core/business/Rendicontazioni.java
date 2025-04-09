/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import jakarta.activation.DataHandler;
import jakarta.xml.bind.JAXBException;

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
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.DateUtils;
import it.govpay.core.utils.EventoUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Intermediario;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.Versamento.TipologiaTipoVersamento;
import it.govpay.model.configurazione.Giornale;
import it.govpay.model.eventi.DatiPagoPA;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.pagopa.beans.utils.JaxbUtils;


public class Rendicontazioni {

	public class DownloadRendicontazioniResponse {
		
		private List<RendicontazioneScaricata> rndDwn;
		private String descrizioneEsito;
		
		public DownloadRendicontazioniResponse() {
			rndDwn = new ArrayList<>();
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
			setErrori(new ArrayList<>());
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
		// do nothing
	}

	public DownloadRendicontazioniResponse downloadRendicontazioni(IContext ctx) throws GovPayException, UtilsException { 
		int frAcquisiti = 0;
		int frNonAcquisiti = 0;
		
		boolean verificaPendenzaMandatoria = GovpayConfig.getInstance().isVerificaPendenzaMandatoriaInAcquisizioneRendicontazioni();
		DownloadRendicontazioniResponse response = new DownloadRendicontazioniResponse();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		LogUtils.logInfo(log, "Acquisizione dei flussi di rendicontazione in corso...");
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
					LogUtils.logInfo(log, "Richiesta lista dei flussi di rendicontazione per il dominio [{}] ...",	dominio.getCodDominio());
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
						LogUtils.logDebug(log, "Cartella di acquisizione FR vuota");
					for (File xmlfile : files) {
						LogUtils.logInfo(log, "Trovato Flusso di Rendicontazione da acquisire su FileSystem: {}", xmlfile.getAbsolutePath());
						FlussoRiversamento flussoRendicontazione = null;
						try {
							flussoRendicontazione = JaxbUtils.toFR(FileUtils.readFileToByteArray(xmlfile));
							TipoIdRendicontazione idRendicontazione = new TipoIdRendicontazione();
							idRendicontazione.setDataOraFlusso(flussoRendicontazione.getDataOraFlusso());
							idRendicontazione.setIdentificativoFlusso(flussoRendicontazione.getIdentificativoFlusso());
							flussiDaPagoPA.add(new RendicontazioneScaricata(idRendicontazione, flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco(), xmlfile));
						} catch (Exception e) {
							LogUtils.logError(log, MessageFormat.format("Impossibile acquisire il flusso di rendicontazione da file: {0}", xmlfile.getAbsolutePath()), e);
						}
					}
				} else {
					LogUtils.logDebug(log, "Cartella di acquisizione FR non presente: {}{}input{}fr", GovpayConfig.getInstance().getResourceDir(), File.separatorChar, File.separatorChar);
				}

				FrBD frBD = new FrBD(configWrapper);
				
				// Lista per i flussi che dovremo acquisire
				List<RendicontazioneScaricata> flussiDaAcquisire = new ArrayList<>();
				// Elenco dei riferimenti ai flussi per verificare che la lista da acquisire non abbia duplicati				
				Set<String> keys = new HashSet<>();
				LogUtils.logInfo(log, "Verifica esistenza sul db dei flussi da acquisire...");
				for(RendicontazioneScaricata rnd : flussiDaPagoPA) {
					TipoIdRendicontazione idRendicontazione = rnd.getIdFlussoRendicontazione();
					// Controllo che il flusso non sia su db
					try {
						LogUtils.logDebug(log, "Verifico presenza del flusso [{}, {}, {}]", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso());
						// Uso la GET perche' la exists risulta buggata con la data nella tupla di identificazione
						frBD.getFr(rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), DateUtils.toJavaDate(idRendicontazione.getDataOraFlusso()));
						LogUtils.logDebug(log, "Flusso di rendicontazione [{}, {}, {}] gia'' acquisito", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso());
						// C'e' gia. Se viene da file, lo elimino
						if(rnd.getFrFile() != null) {
							try { 
								boolean delete = rnd.getFrFile().delete();
								if(!delete) {
									log.warn("Il file di flusso {} non e'' stato eliminato.", rnd.getFrFile().getName());
								}
							} catch (Exception e) { 
								LogUtils.logError(log, MessageFormat.format("Impossibile eliminare il file di flusso gia'' presente: {0}", rnd.getFrFile().getName()), e);
							}
						}
					} catch (NotFoundException e) {
						// Flusso originale, lo aggiungo ma controllo che non sia gia' nella lista di quelli da aggiungere
						long timeMillis = DateUtils.toJavaDate( idRendicontazione.getDataOraFlusso() ).getTime();
						if(!keys.contains(rnd.getCodDominio() + idRendicontazione.getIdentificativoFlusso() + timeMillis)) {
							LogUtils.logInfo(log, "Flusso di rendicontazione [{}, {}, {}] da acquisire", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso());
							flussiDaAcquisire.add(rnd);
							keys.add(rnd.getCodDominio() + idRendicontazione.getIdentificativoFlusso() + timeMillis);
						}
					}
				}
				
				LogUtils.logInfo(log, "Individuati {} flussi di rendicontazione da acquisire", flussiDaAcquisire.size());

				for(RendicontazioneScaricata rnd : flussiDaAcquisire) {
					TipoIdRendicontazione idRendicontazione = rnd.getIdFlussoRendicontazione();
					LogUtils.logInfo(log, "Acquisizione flusso di rendicontazione {} {}", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso());
					
					
					boolean hasFrAnomalia = false;
					boolean isAggiornamento = false;
					
					NodoClient chiediFlussoRendicontazioneClient = null;
					EventoContext eventoCtx = new EventoContext(Componente.API_PAGOPA);
					eventoCtx.setCodDominio(rnd.getCodDominio());
					popolaDatiPagoPAEvento(eventoCtx, intermediario, stazione, null, idRendicontazione.getIdentificativoFlusso());
					
					try {
						byte[] tracciato = null;
						if(rnd.getFrFile() == null) {
							appContext.setupNodoClient(stazione.getCodStazione(), null, EventoContext.Azione.NODOCHIEDIFLUSSORENDICONTAZIONE);
							appContext.getRequest().addGenericProperty(new Property("codStazione", stazione.getCodStazione()));
							appContext.getRequest().addGenericProperty(new Property("idFlusso", idRendicontazione.getIdentificativoFlusso()));
							appContext.getRequest().addGenericProperty(new Property("codDominio", rnd.getCodDominio()));
							
							NodoChiediFlussoRendicontazione richiestaFlusso = new NodoChiediFlussoRendicontazione();
							richiestaFlusso.setIdentificativoIntermediarioPA(stazione.getIntermediario(configWrapper).getCodIntermediario());
							richiestaFlusso.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
							richiestaFlusso.setIdentificativoDominio(rnd.codDominio);
							richiestaFlusso.setPassword(stazione.getPassword());
							richiestaFlusso.setIdentificativoFlusso(idRendicontazione.getIdentificativoFlusso());
	
							NodoChiediFlussoRendicontazioneRisposta risposta;
							
							try {
								chiediFlussoRendicontazioneClient = new NodoClient(intermediario, null, giornale, eventoCtx);
								risposta = chiediFlussoRendicontazioneClient.nodoChiediFlussoRendicontazione(richiestaFlusso, stazione.getIntermediario(configWrapper).getDenominazione());
								eventoCtx.setEsito(Esito.OK);
							} catch (ClientException | UtilsException | ServiceException e) {
								if(eventoCtx != null) {
									if(e instanceof ClientException) {
										eventoCtx.setSottotipoEsito(((ClientException)e).getResponseCode() + "");
									} else {
										eventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
									}
									eventoCtx.setEsito(Esito.FAIL);
									eventoCtx.setDescrizioneEsito(e.getMessage());
									eventoCtx.setException(e);
								}
								// Errore nella richiesta. Loggo e continuo con il prossimo flusso
								rnd.getErrori().add(MessageFormat.format("Richiesta al nodo fallita: {0}.", e.getMessage()));
								LogUtils.logError(log, MessageFormat.format("Richiesta flusso rendicontazione [{0}] fallita: {1}", idRendicontazione.getIdentificativoFlusso(), e.getMessage()), e);
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", e.getMessage());
								continue;
							} catch (ClientInitializeException e) {
								eventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
								eventoCtx.setEsito(Esito.FAIL);
								eventoCtx.setDescrizioneEsito(e.getMessage());
								eventoCtx.setException(e);
								
								// Errore nella richiesta. Loggo e continuo con il prossimo flusso
								rnd.getErrori().add(MessageFormat.format("Creazione client per la richiesta al nodo fallita: {0}.", e.getMessage()));
								LogUtils.logError(log, MessageFormat.format("Creazione client per la richiesta flusso rendicontazione [{0}] fallita: {1}", idRendicontazione.getIdentificativoFlusso(), e.getMessage()), e);
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", e.getMessage());
								continue;
							} 
	
							if(risposta.getFault() != null) {
								// Errore nella richiesta. Loggo e continuo con il prossimo flusso
								rnd.getErrori().add(MessageFormat.format("Richiesta al nodo fallita: {0} {1}.", risposta.getFault().getFaultCode(), risposta.getFault().getFaultString()));
								LogUtils.logError(log, MessageFormat.format("Richiesta flusso rendicontazione [{0}] fallita: {1} {2}", idRendicontazione.getIdentificativoFlusso(), risposta.getFault().getFaultCode(), risposta.getFault().getFaultString()));
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoKo", risposta.getFault().getFaultCode(), risposta.getFault().getFaultString(), risposta.getFault().getDescription());
								if(eventoCtx != null) {
									eventoCtx.setSottotipoEsito(risposta.getFault().getFaultCode());
									eventoCtx.setEsito(Esito.KO);
									eventoCtx.setDescrizioneEsito(risposta.getFault().getFaultString());
								}
							} else {
								try {
									ByteArrayOutputStream output = new ByteArrayOutputStream();
									DataHandler dh = risposta.getXmlRendicontazione();
									dh.writeTo(output);
									tracciato = output.toByteArray();
								} catch (IOException e) {
									rnd.getErrori().add(MessageFormat.format("Lettura del flusso fallita: {0}.", e.getMessage()));
									LogUtils.logError(log, "Errore durante la lettura del flusso di rendicontazione", e);
									ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Lettura del flusso fallita: " + e);
									continue;
								}
							}
						} else {
							try {
								tracciato = FileUtils.readFileToByteArray(rnd.getFrFile());
							} catch (IOException e) {
								rnd.getErrori().add(MessageFormat.format("Lettura del flusso fallita: {0}.", e.getMessage()));
								LogUtils.logError(log, "Errore durante la lettura del flusso di rendicontazione da file: " + rnd.getFrFile().getAbsolutePath(), e);
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Lettura del flusso fallita: " + e);
								continue;
							}
						}

						FlussoRiversamento flussoRendicontazione = null;
						try {
							flussoRendicontazione = JaxbUtils.toFR(tracciato);
						} catch (JAXBException | SAXException e) {
							rnd.getErrori().add(MessageFormat.format("Parsing del flusso fallita: {0}.", e.getMessage()));
							LogUtils.logError(log, "Errore durante il parsing del flusso di rendicontazione", e);
							ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoFail", "Errore durante il parsing del flusso di rendicontazione: " + e);
							continue;
						}

						LogUtils.logInfo(log, "Ricevuto flusso rendicontazione per {} singoli pagamenti", flussoRendicontazione.getDatiSingoliPagamentis().size());

						ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlusso");
						appContext.getRequest().addGenericProperty(new Property("trn", flussoRendicontazione.getIdentificativoUnivocoRegolamento()));
						
						Fr fr = new Fr();
						fr.setObsoleto(false);
						fr.setCodBicRiversamento(flussoRendicontazione.getCodiceBicBancaDiRiversamento());
						fr.setCodFlusso(idRendicontazione.getIdentificativoFlusso());
						fr.setIur(flussoRendicontazione.getIdentificativoUnivocoRegolamento());
						fr.setDataAcquisizione(new Date());
						fr.setDataFlusso(DateUtils.toJavaDate(idRendicontazione.getDataOraFlusso()));
						fr.setDataRegolamento(DateUtils.toJavaDate(flussoRendicontazione.getDataRegolamento()));
						fr.setNumeroPagamenti(flussoRendicontazione.getNumeroTotalePagamenti().longValue());
						fr.setImportoTotalePagamenti(flussoRendicontazione.getImportoTotalePagamenti());

						fr.setXml(tracciato);

						String codPsp = null;
						String codDominio = null;
						String ragioneSocialePsp = flussoRendicontazione.getIstitutoMittente() != null ? flussoRendicontazione.getIstitutoMittente().getDenominazioneMittente() : null;
						String ragioneSocialeDominio = null; 
						codPsp = idRendicontazione.getIdentificativoFlusso().substring(10, idRendicontazione.getIdentificativoFlusso().indexOf("-", 10));
						fr.setCodPsp(codPsp);
						fr.setRagioneSocialePsp(ragioneSocialePsp);
						LogUtils.logDebug(log, "Identificativo PSP estratto dall''identificativo flusso: {}", codPsp);
						appContext.getRequest().addGenericProperty(new Property("codPsp", codPsp));

						Dominio dominio = null;
						try {
							codDominio = flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco();
							ragioneSocialeDominio = flussoRendicontazione.getIstitutoRicevente().getDenominazioneRicevente();
							fr.setCodDominio(codDominio);
							fr.setRagioneSocialeDominio(ragioneSocialeDominio);
							appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
							dominio = AnagraficaManager.getDominio(configWrapper, codDominio);	
						} catch (ServiceException | NotFoundException e) {
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
							BigInteger indiceDati = dsp.getIndiceDatiSingoloPagamento() != null ? BigInteger.valueOf(dsp.getIndiceDatiSingoloPagamento()) : null;
							BigDecimal importoRendicontato = dsp.getSingoloImportoPagato();

							LogUtils.logInfo(log, "Rendicontato (Esito {}) per un importo di ({}) [CodDominio: {}] [Iuv: {}][Iur: {}]",
									dsp.getCodiceEsitoSingoloPagamento(), dsp.getSingoloImportoPagato(), codDominio, dsp.getIdentificativoUnivocoVersamento(), iur);

							it.govpay.bd.model.Rendicontazione rendicontazione = new it.govpay.bd.model.Rendicontazione();

							// Gestisco un codice esito non supportato
							try {
								rendicontazione.setEsito(EsitoRendicontazione.toEnum(dsp.getCodiceEsitoSingoloPagamento()));
							} catch (CodificaInesistenteException e) {
								ctx.getApplicationLogger().log("rendicontazioni.esitoSconosciuto", iuv, iur, dsp.getCodiceEsitoSingoloPagamento() == null ? "null" : dsp.getCodiceEsitoSingoloPagamento());
								rendicontazione.addAnomalia("007110", MessageFormat.format("Codice esito [{0}] sconosciuto", dsp.getCodiceEsitoSingoloPagamento()));
							}

							rendicontazione.setData(DateUtils.toJavaDate(dsp.getDataEsitoSingoloPagamento()));
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
								rendicontazione.setIdSingoloVersamento(pagamento.getIdSingoloVersamento());

								// Verifico l'importo
								if(rendicontazione.getEsito().equals(EsitoRendicontazione.REVOCATO)) {
									if(pagamento.getImportoRevocato().compareTo(importoRendicontato.abs()) != 0) {
										ctx.getApplicationLogger().log("rendicontazioni.importoStornoErrato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
										LogUtils.logInfo(log, "Revoca [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: l''importo rendicontato [{}] non corrisponde a quanto stornato [{}]",
												codDominio, iuv, iur, indiceDati, importoRendicontato.doubleValue(), pagamento.getImportoRevocato().doubleValue());
										rendicontazione.addAnomalia("007112", MessageFormat.format("L''importo rendicontato [{0}] non corrisponde a quanto stornato [{1}]",
												importoRendicontato.doubleValue(), pagamento.getImportoRevocato().doubleValue()));
									}

								} else {
									if(pagamento.getImportoPagato().compareTo(importoRendicontato) != 0) {
										ctx.getApplicationLogger().log("rendicontazioni.importoErrato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
										LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: l''importo rendicontato [{}] non corrisponde a quanto pagato [{}]",
												codDominio, iuv, iur, indiceDati, importoRendicontato.doubleValue(), pagamento.getImportoPagato().doubleValue());
										rendicontazione.addAnomalia("007104", MessageFormat.format("L''importo rendicontato [{0}] non corrisponde a quanto pagato [{1}]",
												importoRendicontato.doubleValue(), pagamento.getImportoPagato().doubleValue()));
									}
								}
							} catch (NotFoundException e) {
								// Pagamento non trovato. Devo capire se ce' un errore.
								
								// 2024-10-31 Nuova procedura, recupero il versamento, se lo trovo poi decido in base al codice esito se si tratta di una ANOMALIA o di un ESEGUITO_SENZA_RPT
								// Se non lo trovo allora si tratta di ALTRO_INTERMEDIARIO
								
								//Recupero il versamento, internamente o dall'applicazione esterna
								it.govpay.bd.model.Versamento versamento = null;
								String erroreVerifica = null;
								String codApplicazione = null;
								try {
									try {
										LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] non trovato, ricerco pendenza a cui assegnare la rendicontazione...",	codDominio, iuv, iur, indiceDati);
										versamento = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv);
									} catch (NotFoundException nfe) {
										// Verifico se lo iuv appartiene al dominio, se lo e' provo ad acquisire la pendenza
										if(IuvUtils.isIuvInterno(dominio, iuv)) {
											// Non e' su sistema. Individuo l'applicativo gestore
											Applicazione applicazioneDominio = new it.govpay.core.business.Applicazione().getApplicazioneDominio(configWrapper, dominio, iuv,false);
											if(applicazioneDominio != null) {
												codApplicazione = applicazioneDominio.getCodApplicazione();
												versamento = VersamentoUtils.acquisisciVersamento(applicazioneDominio, null, null, null, codDominio, iuv, TipologiaTipoVersamento.DOVUTO, log);
											}	
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
									if(verificaPendenzaMandatoria) {
										rnd.getErrori().add(MessageFormat.format("Acquisizione flusso fallita. Riscontrato errore nell''acquisizione del versamento dall''applicazione gestrice [Transazione: {0}].", ctx.getTransactionId()));
										LogUtils.logError(log, MessageFormat.format("Errore durante il processamento del flusso di Rendicontazione [Flusso:{0}]: impossibile acquisire i dati del versamento [Dominio:{1} Iuv:{2}]. Flusso non acquisito.", idRendicontazione.getIdentificativoFlusso(), codDominio, iuv));
										ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoKo", idRendicontazione.getIdentificativoFlusso(), "Impossibile acquisire i dati di un versamento dall'applicativo gestore [Applicazione:" + codApplicazione + " Dominio:" + codDominio+ " Iuv:" + iuv + "].  Flusso non acquisito.");
										throw new GovPayException(ce);	
									} else {
										LogUtils.logWarn(log, MessageFormat.format("Errore durante il processamento del flusso di Rendicontazione [Flusso:{0}]: impossibile acquisire i dati del versamento [Dominio:{1} Iuv:{2}], stato pendenza gestito come SCONOSCIUTO.", idRendicontazione.getIdentificativoFlusso(), codDominio, iuv));
										erroreVerifica = "Versamento non acquisito dall'applicazione gestrice perche' SCONOSCIUTO.";
									}
								} catch (ClientInitializeException ce) {
									if(verificaPendenzaMandatoria) {
										rnd.getErrori().add(MessageFormat.format("Acquisizione flusso fallita. Riscontrato errore nella creazione del client per l''acquisizione del versamento dall''applicazione gestrice [Transazione: {0}].", ctx.getTransactionId()));
										LogUtils.logError(log, MessageFormat.format("Errore durante il processamento del flusso di Rendicontazione [Flusso:{0}]: impossibile acquisire i dati del versamento [Dominio:{1} Iuv:{2}] a causa di un errore durante la creazione del client. Flusso non acquisito.", idRendicontazione.getIdentificativoFlusso(), codDominio, iuv));
										ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoKo", idRendicontazione.getIdentificativoFlusso(), "Impossibile acquisire i dati di un versamento dall'applicativo gestore [Applicazione:" + codApplicazione + " Dominio:" + codDominio+ " Iuv:" + iuv + "].  Flusso non acquisito.");
										throw new GovPayException(ce);	
									} else {
										LogUtils.logWarn(log, MessageFormat.format("Errore durante il processamento del flusso di Rendicontazione [Flusso:{0}]: impossibile acquisire i dati del versamento [Dominio:{1} Iuv:{2}], stato pendenza gestito come SCONOSCIUTO.", idRendicontazione.getIdentificativoFlusso(), codDominio, iuv));
										erroreVerifica = "Versamento non acquisito dall'applicazione gestrice perche' SCONOSCIUTO.";
									}
								} catch (VersamentoNonValidoException e1) {
									erroreVerifica = "Versamento non acquisito dall'applicazione gestrice perche' NON VALIDO: " + e.getMessage();
								}

								// imposto l'id singoloversamento in funzione dell'indice dati
								if(versamento != null) {
									LogUtils.logInfo(log, "Trovata Pendenza [{}, {}], verra' associata alla rendicontazione [Dominio:{} Iuv:{} Iur:{} Indice:{}].", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), codDominio, iuv, iur, indiceDati);
									
									List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
									int idxRiconciliazione = indiceDati != null ? indiceDati.intValue() : 1; // se la rendicontazione non ha l'indice dati assumo che sia 1. 
									
									for (SingoloVersamento singoloVersamento : singoliVersamenti) {
										if(singoloVersamento.getIndiceDati().intValue() == idxRiconciliazione) {
											rendicontazione.setIdSingoloVersamento(singoloVersamento.getId());
											break;
										}
									}
								} else {
									LogUtils.logInfo(log, "Non e' stata trovata nessuna pendenza corrispondente alla rendicontazione [Dominio:{} Iuv:{} Iur:{} Indice:{}]: {}.", codDominio, iuv, iur, indiceDati, erroreVerifica);
								}
								
								// Controllo se e' un pagamento senza RPT o pagamento standin senza rpt
								if(rendicontazione.getEsito().equals(EsitoRendicontazione.ESEGUITO_STANDIN_SENZA_RPT) || 
										rendicontazione.getEsito().equals(EsitoRendicontazione.ESEGUITO_SENZA_RPT)) {
									if(versamento == null) {
										// non ho trovato il versamento 
										ctx.getApplicationLogger().log("rendicontazioni.senzaRptNoVersamento", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
										LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.",	codDominio, iuv, iur, indiceDati);
										rendicontazione.addAnomalia("007111", "Il versamento risulta sconosciuto: " + erroreVerifica);
										// salto la parte di add anomalia
										continue;
									} else {
										List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
										if(singoliVersamenti.size() != 1) {
											// Un pagamento senza rpt DEVE riferire un pagamento tipo 3 con un solo singolo versamento
											ctx.getApplicationLogger().log("rendicontazioni.senzaRptVersamentoMalformato", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
											LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.",	codDominio, iuv, iur, indiceDati);
											rendicontazione.addAnomalia("007114", "Il versamento presenta piu' singoli versamenti");
										}
										// salto la parte di add anomalia
										continue;
									}
								} else {
									// Se il versamento non e' in base dati allora e' di un altro intermediario
									if(versamento == null) {
										rendicontazione.setStato(StatoRendicontazione.ALTRO_INTERMEDIARIO);
										// salto la parte di add anomalia
										continue;
									}
								}

								ctx.getApplicationLogger().log("rendicontazioni.noPagamento", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
								LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: il pagamento non risulta presente in base dati.", codDominio, iuv, iur, indiceDati);
								rendicontazione.addAnomalia("007101", "Il pagamento riferito dalla rendicontazione non risulta presente in base dati.");
							} catch (MultipleResultException e) {
								// Individuati piu' pagamenti riferiti dalla rendicontazione
								ctx.getApplicationLogger().log("rendicontazioni.poliPagamento", iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
								LogUtils.logInfo(log, "Pagamento rendicontato duplicato: [Dominio:{} Iuv:{} Iur:{} Indice:{}]",	codDominio, iuv, iur, indiceDati);
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
											LogUtils.logInfo(log, "Rendicontazione [Dominio:{} Iuv:{} Iur:{} Indice:{}] duplicata all''interno del flusso, in violazione delle specifiche PagoPA. Necessario intervento manuale per la risoluzione del problema.", codDominio, iuv, iur, indiceDati);
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
							LogUtils.logInfo(log, "La somma degli importi rendicontati [{}] non corrisponde al totale indicato nella testata del flusso [{}]", totaleImportiRendicontati, flussoRendicontazione.getImportoTotalePagamenti());
							fr.addAnomalia("007106", MessageFormat.format("La somma degli importi rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [{1}]", totaleImportiRendicontati, flussoRendicontazione.getImportoTotalePagamenti()));
						}

						try {
							if(flussoRendicontazione.getDatiSingoliPagamentis().size() != flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()) {
								ctx.getApplicationLogger().log("rendicontazioni.numeroRendicontazioniErrato");
								LogUtils.logInfo(log, "Il numero di pagamenti rendicontati [{}] non corrisponde al totale indicato nella testata del flusso [{}]", flussoRendicontazione.getDatiSingoliPagamentis().size(), flussoRendicontazione.getNumeroTotalePagamenti().longValueExact());
								fr.addAnomalia("007107", MessageFormat.format("Il numero di pagamenti rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [{1}]", flussoRendicontazione.getDatiSingoliPagamentis().size(), flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()));
							}	
						} catch (UtilsException e) {
							ctx.getApplicationLogger().log("rendicontazioni.numeroRendicontazioniErrato");
							LogUtils.logInfo(log, "Il numero di pagamenti rendicontati [{}] non corrisponde al totale indicato nella testata del flusso [????]", flussoRendicontazione.getDatiSingoliPagamentis().size());
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
									LogUtils.logInfo(log, "Trovata versione precedente [{}] da marcare come obsoleta.", fr.getCodFlusso());
									frBD.updateObsoleto(frEsistente.getId(), true);
									isAggiornamento=true;
								} else {
									// Flusso su DB gia' recente. Lascio tutto fare e inserisco quello nuovo come obsoleto.
									LogUtils.logInfo(log, "Trovata versione successiva [{}]. Il nuovo flusso viene marcato come obsoleto.", fr.getCodFlusso());
									fr.setObsoleto(true);
								}
							} catch (NotFoundException e) {
								LogUtils.logDebug(log, "Nessuna versione alternativa [{}].", fr.getCodFlusso());
							}
							
							frBD.insertFr(fr);
							
							
							for(Rendicontazione r : fr.getRendicontazioni()) {
								r.setIdFr(fr.getId());
								rendicontazioniBD.insert(r);
							}
							rendicontazioniBD.commit();
							frAcquisiti++;
							
							if(eventoCtx != null) {
								eventoCtx.setIdFr(fr.getId());
							}
							if(!hasFrAnomalia) {
								LogUtils.logInfo(log, "Flusso di rendicontazione acquisito senza anomalie.");
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoOk");
							} else {
								LogUtils.logInfo(log, "Flusso di rendicontazione acquisito con anomalie.");
								ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussoOkAnomalia");
							}
							
						}catch (ServiceException e) {
							if(!rendicontazioniBD.isAutoCommit())
								rendicontazioniBD.rollback();
							
							LogUtils.logError(log, MessageFormat.format("Flusso di rendicontazione non acquisito: {0}", e.getMessage()), e);
						} finally {
							// ripristino autocommit
							if(!rendicontazioniBD.isAutoCommit() ) {
								rendicontazioniBD.setAutoCommit(true);
							}
							
							rendicontazioniBD.closeConnection();
						}
					} catch (GovPayException ce) {
						LogUtils.logError(log, "Flusso di rendicontazione non acquisito", ce);
						frNonAcquisiti++;
					} finally {
						if(eventoCtx != null && eventoCtx.isRegistraEvento()) {
							EventiBD eventiBD = new EventiBD(configWrapper);
							Evento eventoDTO = EventoUtils.toEventoDTO(eventoCtx,log);
							if(isAggiornamento)
								eventoDTO.setSottotipoEvento(EventoContext.APIPAGOPA_SOTTOTIPOEVENTO_FLUSSO_RENDICONTAZIONE_DUPLICATO);
							eventiBD.insertEvento(eventoDTO);
							
						}
					}
				}
			}
			
		} catch(ServiceException | it.govpay.core.exceptions.IOException e) {
			ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiFail", e.getMessage());
			response.descrizioneEsito=MessageFormat.format("Impossibile acquisire i flussi: {0}", e.getMessage());
			LogUtils.logError(log, MessageFormat.format("Acquisizione dei flussi di rendicontazione in completata con errore: {0}", e.getMessage()), e);
			throw new GovPayException(e);
		} finally {
			//donothing
		}
		response.descrizioneEsito = MessageFormat.format("Operazione completata: {0} flussi acquisiti", frAcquisiti);
		if(frNonAcquisiti > 0) response.descrizioneEsito += MessageFormat.format(" e {0}non acquisiti per errori", frNonAcquisiti);
		ctx.getApplicationLogger().log("rendicontazioni.acquisizioneOk");
		LogUtils.logInfo(log, "Acquisizione dei flussi di rendicontazione in completata.");
		
		return response;
	}

	private List<TipoIdRendicontazione> chiediListaFr(Stazione stazione, Dominio dominio, Giornale giornale) throws UtilsException{ 
		List<TipoIdRendicontazione> flussiDaAcquisire = new ArrayList<>();
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		NodoClient chiediFlussoRendicontazioniClient = null;
		EventoContext eventoCtx = new EventoContext(Componente.API_PAGOPA);
		String codDominio = dominio != null ? dominio.getCodDominio() : "-";
		eventoCtx.setCodDominio(codDominio);
		try {
			appContext.setupNodoClient(stazione.getCodStazione(), codDominio, EventoContext.Azione.NODOCHIEDIELENCOFLUSSIRENDICONTAZIONE);
			appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
			ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussi");

			NodoChiediElencoFlussiRendicontazione richiesta = new NodoChiediElencoFlussiRendicontazione();
			if(dominio != null) richiesta.setIdentificativoDominio(codDominio);
			richiesta.setIdentificativoIntermediarioPA(stazione.getIntermediario(configWrapper).getCodIntermediario());
			richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione()); 
			richiesta.setPassword(stazione.getPassword());

			NodoChiediElencoFlussiRendicontazioneRisposta risposta;
			try {
				Intermediario intermediario = stazione.getIntermediario(configWrapper);
				popolaDatiPagoPAEvento(eventoCtx, intermediario, stazione, dominio, null);
				chiediFlussoRendicontazioniClient = new NodoClient(intermediario, null, giornale, eventoCtx);
				LogUtils.logDebug(log, "Richiesta elenco flussi rendicontazione per il dominio [{}] al nodo in corso...", codDominio);
				risposta = chiediFlussoRendicontazioniClient.nodoChiediElencoFlussiRendicontazione(richiesta, intermediario.getDenominazione());
				eventoCtx.setEsito(Esito.OK);
				LogUtils.logDebug(log, "Richiesta elenco flussi rendicontazione per il dominio [{}] al nodo completata.", codDominio);
			} catch (ClientException | UtilsException e) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				LogUtils.logError(log, MessageFormat.format("Richiesta elenco flussi rendicontazione per il dominio [{0}] al nodo completata con errore {1}.", codDominio, e.getMessage()), e);
				ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiFail", e.getMessage());
				if(eventoCtx != null) {
					if(e instanceof ClientException) {
						eventoCtx.setSottotipoEsito(((ClientException)e).getResponseCode() + "");
					} else {
						eventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
					}
					eventoCtx.setEsito(Esito.FAIL);
					eventoCtx.setDescrizioneEsito(e.getMessage());
					eventoCtx.setException(e);
				}	
				return flussiDaAcquisire;
			} catch (ClientInitializeException e) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				LogUtils.logError(log, MessageFormat.format("Errore nella creazione del client per la richiesta elenco flussi rendicontazione per il dominio [{0}] al nodo completata con errore {1}.", codDominio, e.getMessage()), e);
				ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiFail", e.getMessage());
				
				if(eventoCtx != null) {
					eventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
					eventoCtx.setEsito(Esito.FAIL);
					eventoCtx.setDescrizioneEsito(e.getMessage());
					eventoCtx.setException(e);
				}	
				return flussiDaAcquisire;
			}

			if(risposta.getFault() != null) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				log.warn("Richiesta elenco flussi rendicontazione per il dominio [{}] fallita: {} {}", codDominio, risposta.getFault().getFaultCode(), risposta.getFault().getFaultString());
				ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiKo", risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
				if(eventoCtx != null) {
					eventoCtx.setSottotipoEsito(risposta.getFault().getFaultCode());
					eventoCtx.setEsito(Esito.KO);
					eventoCtx.setDescrizioneEsito(risposta.getFault().getFaultString());
				}	
				return flussiDaAcquisire;
			} else {

				if(risposta.getElencoFlussiRendicontazione() == null || risposta.getElencoFlussiRendicontazione().getTotRestituiti() == 0) {
					LogUtils.logInfo(log, "Richiesta elenco flussi rendicontazione per il dominio [{}]: ritornata lista vuota dal psp", codDominio);
					ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiOk", "0");
					return flussiDaAcquisire;
				}

				ctx.getApplicationLogger().log("rendicontazioni.acquisizioneFlussiOk", risposta.getElencoFlussiRendicontazione().getTotRestituiti() + "");
				LogUtils.logInfo(log, "Richiesta elenco flussi rendicontazione per il dominio [{}]: ritornati {} flussi.", codDominio, risposta.getElencoFlussiRendicontazione().getTotRestituiti());
				
				for(TipoIdRendicontazione idRendicontazione : risposta.getElencoFlussiRendicontazione().getIdRendicontazione()) {
					LogUtils.logDebug(log, "Ricevuto flusso rendicontazione: {}, {}", idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso());
					flussiDaAcquisire.add(idRendicontazione);
				}
			}
		} catch (ServiceException e) {
			LogUtils.logError(log, "Errore durante l'acquisizione dei flussi di rendicontazione", e);
			return flussiDaAcquisire;
		}  finally {
			if(eventoCtx != null && eventoCtx.isRegistraEvento()) {
				try {
					EventiBD eventiBD = new EventiBD(configWrapper);
					eventiBD.insertEvento(EventoUtils.toEventoDTO(eventoCtx,log));
				}catch (ServiceException e) {
					LogUtils.logError(log, "Errore durante l'acquisizione dei flussi di rendicontazione", e);
				}finally {
					//donothing
				}
			}
		}

		return flussiDaAcquisire;
	}
	
	public static void popolaDatiPagoPAEvento(EventoContext eventoCtx, Intermediario intermediario, Stazione stazione, Dominio dominio, String codFlusso) {

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
