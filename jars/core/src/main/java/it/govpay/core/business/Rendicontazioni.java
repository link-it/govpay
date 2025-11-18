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
import java.time.LocalDateTime;
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
import it.govpay.core.utils.logger.MessaggioDiagnosticoCostanti;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
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

	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_OK = "rendicontazioni.acquisizioneOk";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_OK_ANOMALIA = "rendicontazioni.acquisizioneFlussoOkAnomalia";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_OK = "rendicontazioni.acquisizioneFlussoOk";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_NUMERO_RENDICONTAZIONI_ERRATO = "rendicontazioni.numeroRendicontazioniErrato";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_IMPORTO_TOTALE_ERRATO = "rendicontazioni.importoTotaleErrato";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_POLI_PAGAMENTO = "rendicontazioni.poliPagamento";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_NO_PAGAMENTO = "rendicontazioni.noPagamento";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_SENZA_RPT_VERSAMENTO_MALFORMATO = "rendicontazioni.senzaRptVersamentoMalformato";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_SENZA_RPT_NO_VERSAMENTO = "rendicontazioni.senzaRptNoVersamento";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_IMPORTO_ERRATO = "rendicontazioni.importoErrato";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_IMPORTO_STORNO_ERRATO = "rendicontazioni.importoStornoErrato";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ESITO_SCONOSCIUTO = "rendicontazioni.esitoSconosciuto";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_DOMINIO_NON_CENSITO = "rendicontazioni.acquisizioneFlussoDominioNonCensito";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO = "rendicontazioni.acquisizioneFlusso";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_KO = "rendicontazioni.acquisizioneFlussoKo";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_FAIL = "rendicontazioni.acquisizioneFlussoFail";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE = "rendicontazioni.acquisizione";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_OK = "rendicontazioni.acquisizioneFlussiOk";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_KO = "rendicontazioni.acquisizioneFlussiKo";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_FAIL = "rendicontazioni.acquisizioneFlussiFail";
	private static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI = "rendicontazioni.acquisizioneFlussi";

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
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE);
			DominiBD dominiBD = new DominiBD(ctx.getTransactionId());
			Giornale giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();

			StazioniBD stazioniBD = new StazioniBD(configWrapper);
			List<Stazione> lstStazioni = stazioniBD.getStazioni();

			for(Stazione stazione : lstStazioni) {
				List<RendicontazioneScaricata> flussiDaPagoPA = new ArrayList<>();

				Intermediario intermediario = stazione.getIntermediario(configWrapper);

				DominioFilter filter = dominiBD.newFilter();
				filter.setCodStazione(stazione.getCodStazione());
				filter.setScaricaFr(Boolean.TRUE);
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
							String codDominioFlussoRendicontazioneDaFileSystem = flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco();
							LogUtils.logInfo(log, "Aggiungo Flusso di Rendicontazione da fileSystem: [{}, {}, {}]", codDominioFlussoRendicontazioneDaFileSystem, 
									idRendicontazione.getIdentificativoFlusso(), idRendicontazione.getDataOraFlusso());
							// verifico che il dominio del flusso da file sia censito
							AnagraficaManager.getDominio(configWrapper, codDominioFlussoRendicontazioneDaFileSystem);
							
							// Aggiungo la rendicontazione in testa alla lista per processarla prima di quelle da nodo 
							flussiDaPagoPA.add(0, new RendicontazioneScaricata(idRendicontazione, codDominioFlussoRendicontazioneDaFileSystem, xmlfile));
						} catch (JAXBException| SAXException | IOException e) {
							LogUtils.logError(log, MessageFormat.format("Impossibile acquisire il flusso di rendicontazione da file: {0}", xmlfile.getAbsolutePath()), e);
						} catch (NotFoundException e) {
							String errore = MessageFormat.format("Dominio del flusso di rendicontazione da file non censito: {0}, il file non verra' acquisito", e.getMessage());
							LogUtils.logError(log, errore, e);
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
					LocalDateTime idRendicontazioneDataOraFlusso = idRendicontazione.getDataOraFlusso();
					try {
						LogUtils.logDebug(log, "Verifico presenza del flusso [{}, {}, {}]", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazioneDataOraFlusso);
						// Uso la GET perche' la exists risulta buggata con la data nella tupla di identificazione
						frBD.getFr(rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), DateUtils.toJavaDate(idRendicontazioneDataOraFlusso));
						LogUtils.logDebug(log, "Flusso di rendicontazione [{}, {}, {}] gia' acquisito", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazioneDataOraFlusso);
						// C'e' gia. Se viene da file, lo elimino
						if(rnd.getFrFile() != null) {
							try {
								boolean delete = rnd.getFrFile().delete();
								if(!delete) {
									log.warn("Il file di flusso {} non e' stato eliminato.", rnd.getFrFile().getName());
								}
							} catch (Exception e) {
								LogUtils.logError(log, MessageFormat.format("Impossibile eliminare il file di flusso gia'' presente: {0}", rnd.getFrFile().getName()), e);
							}
						}
					} catch (NotFoundException e) {
						// Flusso originale, lo aggiungo ma controllo che non sia gia' nella lista di quelli da aggiungere
						long dataOraFlussoMillis = idRendicontazioneDataOraFlusso != null ? DateUtils.toJavaDate( idRendicontazioneDataOraFlusso ).getTime() : 0;
						if(!keys.contains(rnd.getCodDominio() + idRendicontazione.getIdentificativoFlusso() + dataOraFlussoMillis)) {
							LogUtils.logInfo(log, "Flusso di rendicontazione [{}, {}, {}] da acquisire", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), idRendicontazioneDataOraFlusso);
							flussiDaAcquisire.add(rnd);
							keys.add(rnd.getCodDominio() + idRendicontazione.getIdentificativoFlusso() + dataOraFlussoMillis);
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
						appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_STAZIONE, stazione.getCodStazione()));
						appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_ID_FLUSSO, idRendicontazione.getIdentificativoFlusso()));
						appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_DOMINIO, rnd.getCodDominio()));

						if(rnd.getFrFile() == null) {
							appContext.setupNodoClient(stazione.getCodStazione(), null, EventoContext.Azione.NODOCHIEDIFLUSSORENDICONTAZIONE);

							NodoChiediFlussoRendicontazione richiestaFlusso = new NodoChiediFlussoRendicontazione();
							richiestaFlusso.setIdentificativoIntermediarioPA(stazione.getIntermediario(configWrapper).getCodIntermediario());
							richiestaFlusso.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
							richiestaFlusso.setIdentificativoDominio(rnd.codDominio);
							richiestaFlusso.setPassword(stazione.getPassword());
							richiestaFlusso.setIdentificativoFlusso(idRendicontazione.getIdentificativoFlusso());

							NodoChiediFlussoRendicontazioneRisposta risposta;

							try {
								chiediFlussoRendicontazioneClient = new NodoClient(intermediario, null, giornale, eventoCtx);
								risposta = chiediFlussoRendicontazioneClient.nodoChiediFlussoRendicontazione(richiestaFlusso);
								eventoCtx.setEsito(Esito.OK);
							} catch (ClientException e) {
								String errore = handleClientException(e, eventoCtx, "Richiesta flusso rendicontazione",
										idRendicontazione.getIdentificativoFlusso(), ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_FAIL);
								rnd.getErrori().add(errore);
								continue;
							} catch (ClientInitializeException e) {
								String errore = handleClientInitializeException(e, eventoCtx, "Richiesta flusso rendicontazione",
										idRendicontazione.getIdentificativoFlusso(), ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_FAIL);
								rnd.getErrori().add(errore);
								continue;
							}

							if(risposta.getFault() != null) {
								// Errore nella richiesta. Loggo e continuo con il prossimo flusso
								rnd.getErrori().add(MessageFormat.format("Richiesta al nodo fallita: {0} {1}.", risposta.getFault().getFaultCode(), risposta.getFault().getFaultString()));
								LogUtils.logError(log, MessageFormat.format("Richiesta flusso rendicontazione [{0}] fallita: {1} {2}", idRendicontazione.getIdentificativoFlusso(), risposta.getFault().getFaultCode(), risposta.getFault().getFaultString()));
								MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_KO, risposta.getFault().getFaultCode(), risposta.getFault().getFaultString(), risposta.getFault().getDescription());
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
									MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_FAIL, "Lettura del flusso fallita: " + e);
									continue;
								}
							}
						} else {
							try {
								tracciato = FileUtils.readFileToByteArray(rnd.getFrFile());
							} catch (IOException e) {
								rnd.getErrori().add(MessageFormat.format("Lettura del flusso fallita: {0}.", e.getMessage()));
								LogUtils.logError(log, "Errore durante la lettura del flusso di rendicontazione da file: " + rnd.getFrFile().getAbsolutePath(), e);
								MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_FAIL, "Lettura del flusso fallita: " + e);
								continue;
							}
						}

						FlussoRiversamento flussoRendicontazione = null;
						try {
							flussoRendicontazione = JaxbUtils.toFR(tracciato);
						} catch (JAXBException | SAXException e) {
							rnd.getErrori().add(MessageFormat.format("Parsing del flusso fallita: {0}.", e.getMessage()));
							LogUtils.logError(log, "Errore durante il parsing del flusso di rendicontazione", e);
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_FAIL, "Errore durante il parsing del flusso di rendicontazione: " + e);
							continue;
						}

						LogUtils.logInfo(log, "Ricevuto flusso rendicontazione per {} singoli pagamenti", flussoRendicontazione.getDatiSingoliPagamentis().size());

						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO);
						appContext.getRequest().addGenericProperty(new Property("trn", flussoRendicontazione.getIdentificativoUnivocoRegolamento()));

						// decide se avviare il recupero RT per i pagamenti rendicontati
						boolean avviaRecuperoRT = false;

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
						LogUtils.logDebug(log, "Identificativo PSP estratto dall'identificativo flusso: {}", codPsp);
						appContext.getRequest().addGenericProperty(new Property("codPsp", codPsp));

						Dominio dominio = null;
						try {
							// prelevo il cod dominio dalla lista dei metadati, in questo modo evito evito problemi di foreign key.
							codDominio = rnd.getCodDominio();
							ragioneSocialeDominio = flussoRendicontazione.getIstitutoRicevente().getDenominazioneRicevente();
							fr.setCodDominio(codDominio);
							fr.setRagioneSocialeDominio(ragioneSocialeDominio);
							appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_DOMINIO, codDominio));
							dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
							fr.setIdDominio(dominio.getId());
						} catch (ServiceException | NotFoundException e) {
							if(codDominio == null) {
								codDominio = "????";
								appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_DOMINIO, "null"));
							}
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_DOMINIO_NON_CENSITO);
							LogUtils.logWarn(log, "Il dominio [{}] del flusso di rendicontazione non e' censito", codDominio);
						}
						
						// se il dominio contenuto nel tracciato xml e' diverso da quello previsto emetto un warning
						if(!codDominio.equals(flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco())){
							String errorMsg = MessageFormat.format("Il dominio indicato nel flusso di rendicontazione [{0}] non corrisponde a quello previsto [{1}]",
									flussoRendicontazione.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco(),	codDominio);
							fr.addAnomalia("007116", errorMsg);
							LogUtils.logWarn(log, errorMsg);
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
								MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ESITO_SCONOSCIUTO, iuv, iur, dsp.getCodiceEsitoSingoloPagamento() == null ? "null" : dsp.getCodiceEsitoSingoloPagamento());
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
								LogUtils.logDebug(log, "Pagamento trovato [Dominio:{} Iuv:{} Iur:{} Indice:{}] - ID:{} ImportoPagato:{} ImportoRevocato:{} IdSingoloVersamento:{}",
								  codDominio, iuv, iur, indiceDati, pagamento.getId(), pagamento.getImportoPagato(), pagamento.getImportoRevocato(), pagamento.getIdSingoloVersamento());

								rendicontazione.setIdPagamento(pagamento.getId());

								// imposto l'id singolo versamento
								rendicontazione.setIdSingoloVersamento(pagamento.getIdSingoloVersamento());

								// Verifico l'importo
								if(rendicontazione.getEsito().equals(EsitoRendicontazione.REVOCATO)) {
									if(pagamento.getImportoRevocato().compareTo(importoRendicontato.abs()) != 0) {
										MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_IMPORTO_STORNO_ERRATO, iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
										LogUtils.logInfo(log, "Revoca [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: l''importo rendicontato [{}] non corrisponde a quanto stornato [{}]",
												codDominio, iuv, iur, indiceDati, importoRendicontato.doubleValue(), pagamento.getImportoRevocato().doubleValue());
										rendicontazione.addAnomalia("007112", MessageFormat.format("L''importo rendicontato [{0}] non corrisponde a quanto stornato [{1}]",
												importoRendicontato.doubleValue(), pagamento.getImportoRevocato().doubleValue()));
									}

								} else {
									if(pagamento.getImportoPagato().compareTo(importoRendicontato) != 0) {
										MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_IMPORTO_ERRATO, iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
										LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: l''importo rendicontato [{}] non corrisponde a quanto pagato [{}]",
												codDominio, iuv, iur, indiceDati, importoRendicontato.doubleValue(), pagamento.getImportoPagato().doubleValue());
										rendicontazione.addAnomalia("007104", MessageFormat.format("L''importo rendicontato [{0}] non corrisponde a quanto pagato [{1}]",
												importoRendicontato.doubleValue(), pagamento.getImportoPagato().doubleValue()));
									}
								}
							} catch (NotFoundException e) {
								// Pagamento non trovato. Devo capire se ce' un errore.
								LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] non trovato: ricerco la causa...", codDominio, iuv, iur, indiceDati);

								// controllo se lo IUV e' interno, se non lo e' salto tutta la parte di acquisizione
								// Se dominio e' null viene considerato non intermediato
								if(!IuvUtils.isIuvInterno(log, dominio, iuv)) {
									LogUtils.logInfo(log, "IUV {} appartenente ad un Dominio {} non intermediato, salto acquisizione.", iuv, codDominio);
									rendicontazione.setStato(StatoRendicontazione.ALTRO_INTERMEDIARIO);
									// passo alla prossima rendicontazione
									continue;
								}

								//Recupero il versamento, internamente o dall'applicazione esterna
								it.govpay.bd.model.Versamento versamento = null;
								String erroreVerifica = null;
								String codApplicazione = null;
								try {
									try {
										LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] non trovato, ricerco pendenza a cui assegnare la rendicontazione...",	codDominio, iuv, iur, indiceDati);
										versamento = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv);
									} catch (NotFoundException nfe) {
										// Non e' su sistema. Individuo l'applicativo gestore
										Applicazione applicazioneDominio = new it.govpay.core.business.Applicazione().getApplicazioneDominio(configWrapper, dominio, iuv,false);
										if(applicazioneDominio != null) {
											codApplicazione = applicazioneDominio.getCodApplicazione();
											versamento = VersamentoUtils.acquisisciVersamento(applicazioneDominio, null, null, null, codDominio, iuv, TipologiaTipoVersamento.DOVUTO, log);
										} else {
											erroreVerifica = "Non e' stata trovata un'Applicazione autorizzata a gestire lo Iuv per il dominio indicato.";
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
										MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_KO, idRendicontazione.getIdentificativoFlusso(), "Impossibile acquisire i dati di un versamento dall'applicativo gestore [Applicazione:" + codApplicazione + " Dominio:" + codDominio+ " Iuv:" + iuv + "].  Flusso non acquisito.");
										throw new GovPayException(ce);
									} else {
										LogUtils.logWarn(log, MessageFormat.format("Errore durante il processamento del flusso di Rendicontazione [Flusso:{0}]: impossibile acquisire i dati del versamento [Dominio:{1} Iuv:{2}], stato pendenza gestito come SCONOSCIUTO.", idRendicontazione.getIdentificativoFlusso(), codDominio, iuv));
										erroreVerifica = "Versamento non acquisito dall'applicazione gestrice perche' SCONOSCIUTO.";
									}
								} catch (ClientInitializeException ce) {
									if(verificaPendenzaMandatoria) {
										rnd.getErrori().add(MessageFormat.format("Acquisizione flusso fallita. Riscontrato errore nella creazione del client per l''acquisizione del versamento dall''applicazione gestrice [Transazione: {0}].", ctx.getTransactionId()));
										LogUtils.logError(log, MessageFormat.format("Errore durante il processamento del flusso di Rendicontazione [Flusso:{0}]: impossibile acquisire i dati del versamento [Dominio:{1} Iuv:{2}] a causa di un errore durante la creazione del client. Flusso non acquisito.", idRendicontazione.getIdentificativoFlusso(), codDominio, iuv));
										MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_KO, idRendicontazione.getIdentificativoFlusso(), "Impossibile acquisire i dati di un versamento dall'applicativo gestore [Applicazione:" + codApplicazione + " Dominio:" + codDominio+ " Iuv:" + iuv + "].  Flusso non acquisito.");
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
									LogUtils.logInfo(log, "Trovata Pendenza [{}, {}] in stato [{}], verra' associata alla rendicontazione [Dominio:{} Iuv:{} Iur:{} Indice:{}].", 
											versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getStatoVersamento(), codDominio, iuv, iur, indiceDati);

									List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
									int idxRiconciliazione = indiceDati != null ? indiceDati.intValue() : 1; // se la rendicontazione non ha l'indice dati assumo che sia 1.

									for (SingoloVersamento singoloVersamento : singoliVersamenti) {
										if(singoloVersamento.getIndiceDati().intValue() == idxRiconciliazione) {
											rendicontazione.setIdSingoloVersamento(singoloVersamento.getId());
											break;
										}
									}

									// c'e' almeno una pendenza pagata nel flusso che non ha la RT associata 
									avviaRecuperoRT = true;
								} else {
									LogUtils.logInfo(log, "Non e' stata trovata nessuna pendenza corrispondente alla rendicontazione [Dominio:{} Iuv:{} Iur:{} Indice:{}]: {}.", codDominio, iuv, iur, indiceDati, erroreVerifica);
								}

								// Controllo se e' un pagamento senza RPT o pagamento standin senza rpt
								if(rendicontazione.getEsito().equals(EsitoRendicontazione.ESEGUITO_STANDIN_SENZA_RPT) ||
										rendicontazione.getEsito().equals(EsitoRendicontazione.ESEGUITO_SENZA_RPT)) {
									if(versamento == null) {
										// non ho trovato il versamento
										MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_SENZA_RPT_NO_VERSAMENTO, iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
										LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: Pagamento senza RPT di versamento sconosciuto.",	codDominio, iuv, iur, indiceDati);
										rendicontazione.addAnomalia("007111", "Il versamento risulta sconosciuto: " + erroreVerifica);
										// salto la parte di add anomalia
										continue;
									} else {
										List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
										if(singoliVersamenti.size() != 1) {
											// Un pagamento senza rpt DEVE riferire un pagamento tipo 3 con un solo singolo versamento
											MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_SENZA_RPT_VERSAMENTO_MALFORMATO, iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
											LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: Pagamento senza RPT di versamento malformato, numero voci maggiore di 1.", codDominio, iuv, iur, indiceDati);
											rendicontazione.addAnomalia("007114", "Il versamento presenta piu' singoli versamenti");
										}
										// salto la parte di add anomalia
										continue;
									}
								}

								MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_NO_PAGAMENTO, iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
								LogUtils.logInfo(log, "Pagamento [Dominio:{} Iuv:{} Iur:{} Indice:{}] rendicontato con errore: il pagamento non risulta presente in base dati.", codDominio, iuv, iur, indiceDati);
								rendicontazione.addAnomalia("007101", "Il pagamento riferito dalla rendicontazione non risulta presente in base dati.");
							} catch (MultipleResultException e) {
								// Individuati piu' pagamenti riferiti dalla rendicontazione
								MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_POLI_PAGAMENTO, iuv, iur, indiceDati!=null ? indiceDati+"" : "null");
								LogUtils.logInfo(log, "Pagamento rendicontato duplicato: [Dominio:{} Iuv:{} Iur:{} Indice:{}]",	codDominio, iuv, iur, indiceDati);
								rendicontazione.addAnomalia("007102", "La rendicontazione riferisce piu di un pagamento gestito.");
								fr.addAnomalia("007102", "La rendicontazione riferisce piu di un pagamento gestito.");
							} finally {
								//controllo che non sia gia' stata  acquisita un rendicontazione per la tupla (codDominio,iuv,iur,indiceDati), in questo caso emetto una anomalia
								LogUtils.logInfo(log, "Controllo presenza rendicontazione duplicata all'interno del flusso: [Dominio:{} Iuv:{} Iur:{} Indice:{}]...",	codDominio, iuv, iur, indiceDati);
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
								LogUtils.logInfo(log, "Controllo presenza rendicontazione duplicata all'interno del flusso: [Dominio:{} Iuv:{} Iur:{} Indice:{}] completato",	codDominio, iuv, iur, indiceDati);

								if(!StatoRendicontazione.ALTRO_INTERMEDIARIO.equals(rendicontazione.getStato()) && rendicontazione.getAnomalie().isEmpty()) {
									rendicontazione.setStato(StatoRendicontazione.OK);
								} else if(!StatoRendicontazione.ALTRO_INTERMEDIARIO.equals(rendicontazione.getStato()) && !rendicontazione.getAnomalie().isEmpty()) {
									rendicontazione.setStato(StatoRendicontazione.ANOMALA);
									hasFrAnomalia = true;
								}
								
								LogUtils.logInfo(log, "Rendicontazione: [Dominio:{} Iuv:{} Iur:{} Indice:{}] stato finale acquisizione: {}", codDominio, iuv, iur, indiceDati, rendicontazione.getStato());

								fr.addRendicontazione(rendicontazione);
							}
						}


						// Singole rendicontazioni elaborate.
						// Controlli di quadratura generali

						if(totaleImportiRendicontati.compareTo(flussoRendicontazione.getImportoTotalePagamenti()) != 0){
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_IMPORTO_TOTALE_ERRATO);
							LogUtils.logInfo(log, "La somma degli importi rendicontati [{}] non corrisponde al totale indicato nella testata del flusso [{}]", totaleImportiRendicontati, flussoRendicontazione.getImportoTotalePagamenti());
							fr.addAnomalia("007106", MessageFormat.format("La somma degli importi rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [{1}]", totaleImportiRendicontati, flussoRendicontazione.getImportoTotalePagamenti()));
						}

						if(flussoRendicontazione.getDatiSingoliPagamentis().size() != flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()) {
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_NUMERO_RENDICONTAZIONI_ERRATO);
							LogUtils.logInfo(log, "Il numero di pagamenti rendicontati [{}] non corrisponde al totale indicato nella testata del flusso [{}]", flussoRendicontazione.getDatiSingoliPagamentis().size(), flussoRendicontazione.getNumeroTotalePagamenti().longValueExact());
							fr.addAnomalia("007107", MessageFormat.format("Il numero di pagamenti rendicontati [{0}] non corrisponde al totale indicato nella testata del flusso [{1}]", flussoRendicontazione.getDatiSingoliPagamentis().size(), flussoRendicontazione.getNumeroTotalePagamenti().longValueExact()));
						}

						// Decido lo stato del FR
						if(fr.getAnomalie().isEmpty()) {
							fr.setStato(StatoFr.ACCETTATA);
						} else {
							fr.setStato(StatoFr.ANOMALA);
							hasFrAnomalia = true;
						}

						LogUtils.logDebug(log, "Acquisizione flusso di rendicontazione {} {}, stato finale: {}", rnd.getCodDominio(), idRendicontazione.getIdentificativoFlusso(), fr.getStato());

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
								LogUtils.logDebug(log, "Verifica esistenza versione precedente del flusso [{}].", fr.getCodFlusso());
								Fr frEsistente = frBD.getFr(fr.getCodDominio(), fr.getCodFlusso());

								// Ok, c'e' gia' una versione in DB. Vedo e' la data e' precedente o successiva
								if(frEsistente.getDataFlusso().before(fr.getDataFlusso())) {

									// Flusso su DB vecchio. Lo aggiorno come obsoleto e aggiungo il nuovo
									LogUtils.logInfo(log, "Trovata versione precedente del flusso [{}] da marcare come obsoleta.", fr.getCodFlusso());
									frBD.updateObsoleto(frEsistente.getId(), true);
									isAggiornamento=true;
								} else {
									// Flusso su DB gia' recente. Lascio tutto fare e inserisco quello nuovo come obsoleto.
									LogUtils.logInfo(log, "Trovata versione successiva del flusso [{}]. Il nuovo flusso viene marcato come obsoleto.", fr.getCodFlusso());
									fr.setObsoleto(true);
								}
							} catch (NotFoundException e) {
								LogUtils.logDebug(log, "Nessuna versione alternativa del flusso [{}].", fr.getCodFlusso());
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
								MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_OK);
							} else {
								LogUtils.logInfo(log, "Flusso di rendicontazione acquisito con anomalie.");
								MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_OK_ANOMALIA);
							}

							// lancia il recupero delle ricevute.
							if(avviaRecuperoRT) {
								LogUtils.logInfo(log, "Sono state acquisite delle rendicontazioni per pendenze a cui manca la ricevuta, avvio recupero RT.");
								Operazioni.setEseguiRecuperoRT();
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
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_FAIL, e.getMessage());
			response.descrizioneEsito=MessageFormat.format("Impossibile acquisire i flussi: {0}", e.getMessage());
			LogUtils.logError(log, MessageFormat.format("Acquisizione dei flussi di rendicontazione in completata con errore: {0}", e.getMessage()), e);
			throw new GovPayException(e);
		} finally {
			//donothing
		}
		response.descrizioneEsito = MessageFormat.format("Operazione completata: {0} flussi acquisiti", frAcquisiti);
		if(frNonAcquisiti > 0) response.descrizioneEsito += MessageFormat.format(" e {0}non acquisiti per errori", frNonAcquisiti);
		MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_OK);
		LogUtils.logInfo(log, "Acquisizione dei flussi di rendicontazione in completata.");

		return response;
	}

	private List<TipoIdRendicontazione> chiediListaFr(Stazione stazione, Dominio dominio, Giornale giornale) {
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
			appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_DOMINIO, codDominio));
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI);

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
				risposta = chiediFlussoRendicontazioniClient.nodoChiediElencoFlussiRendicontazione(richiesta);
				eventoCtx.setEsito(Esito.OK);
				LogUtils.logDebug(log, "Richiesta elenco flussi rendicontazione per il dominio [{}] al nodo completata.", codDominio);
			} catch (ClientException e) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				handleClientException(e, eventoCtx, "Richiesta elenco flussi rendicontazione",
						codDominio, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_FAIL);
				return flussiDaAcquisire;
			} catch (ClientInitializeException e) {
				handleClientInitializeException(e, eventoCtx, "Richiesta elenco flussi rendicontazione",
						codDominio,	ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_FAIL);
				return flussiDaAcquisire;
			}

			if(risposta.getFault() != null) {
				// Errore nella richiesta. Loggo e continuo con il prossimo psp
				log.warn("Richiesta elenco flussi rendicontazione per il dominio [{}] fallita: {} {}", codDominio, risposta.getFault().getFaultCode(), risposta.getFault().getFaultString());
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_KO, risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
				eventoCtx.setSottotipoEsito(risposta.getFault().getFaultCode());
				eventoCtx.setEsito(Esito.KO);
				eventoCtx.setDescrizioneEsito(risposta.getFault().getFaultString());
				return flussiDaAcquisire;
			} else {

				if(risposta.getElencoFlussiRendicontazione() == null || risposta.getElencoFlussiRendicontazione().getTotRestituiti() == 0) {
					LogUtils.logInfo(log, "Richiesta elenco flussi rendicontazione per il dominio [{}]: ritornata lista vuota dal psp", codDominio);
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_OK, "0");
					return flussiDaAcquisire;
				}

				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_OK, risposta.getElencoFlussiRendicontazione().getTotRestituiti() + "");
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
			if(eventoCtx.isRegistraEvento()) {
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

	/**
	 * Gestisce un errore ClientException configurando l'evento e il log appropriato.
	 *
	 * @param e L'eccezione da gestire
	 * @param eventoCtx Il contesto dell'evento da aggiornare
	 * @param operazione Descrizione dell'operazione fallita (es. "Richiesta flusso rendicontazione")
	 * @param parametri Parametri specifici dell'operazione per il log
	 * @param ctx Context per diagnostici
	 * @param codiceDiagnostico Codice del messaggio diagnostico
	 * @return Messaggio di errore formattato
	 */
	private String handleClientException(ClientException e, EventoContext eventoCtx, String operazione, String parametri, IContext ctx, String codiceDiagnostico) {

		// Aggiorna evento
		if(eventoCtx != null) {
			eventoCtx.setSottotipoEsito(e.getResponseCode() + "");
			eventoCtx.setEsito(Esito.FAIL);
			eventoCtx.setDescrizioneEsito(e.getMessage());
			eventoCtx.setException(e);
		}

		// Log
		String messaggioErrore = MessageFormat.format("{0} [{1}] fallita: {2}", operazione, parametri, e.getMessage());
		LogUtils.logError(log, messaggioErrore, e);

		// Diagnostico
		MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, codiceDiagnostico, e.getMessage());

		return MessageFormat.format("{0} fallita: {1}.", operazione, e.getMessage());
	}

	/**
	 * Gestisce un errore ClientInitializeException configurando l'evento e il log appropriato.
	 *
	 * @param e L'eccezione da gestire
	 * @param eventoCtx Il contesto dell'evento da aggiornare
	 * @param operazione Descrizione dell'operazione fallita
	 * @param parametri Parametri specifici dell'operazione per il log
	 * @param ctx Context per diagnostici
	 * @param codiceDiagnostico Codice del messaggio diagnostico
	 * @return Messaggio di errore formattato
	 */
	private String handleClientInitializeException(ClientInitializeException e, EventoContext eventoCtx, String operazione, String parametri, IContext ctx, String codiceDiagnostico) {

		// Aggiorna evento
		if(eventoCtx != null) {
			eventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
			eventoCtx.setEsito(Esito.FAIL);
			eventoCtx.setDescrizioneEsito(e.getMessage());
			eventoCtx.setException(e);
		}

		// Log
		String messaggioErrore = MessageFormat.format("Errore nella creazione del client per {0} [{1}]: {2}",
				operazione, parametri, e.getMessage());
		LogUtils.logError(log, messaggioErrore, e);

		// Diagnostico
		MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, codiceDiagnostico, e.getMessage());

		return MessageFormat.format("Creazione client per {0} fallita: {1}.", operazione, e.getMessage());
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
