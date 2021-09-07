package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.openspcoop2.utils.service.context.IContext;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtDatiSingoliPagamenti;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtIstitutoMittente;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtIstitutoRicevente;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.FlussoRiversamento;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.bd.pagamento.DocumentiBD;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiNotificaPagamentiBD;
import it.govpay.bd.viste.EntratePrevisteBD;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.thread.InviaNotificaPagamentoMaggioliJPPAThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.core.utils.tracciati.TracciatiNotificaPagamentiUtils;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.Contabilita;
import it.govpay.model.QuotaContabilita;
import it.govpay.model.Rpt.Versione;
import it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE;
import it.govpay.model.TracciatoNotificaPagamenti.TIPO_TRACCIATO;

public class TracciatiNotificaPagamenti {

	public static final String SUFFIX_FILE_RPT_XML = "/rpt.xml";
	public static final String SUFFIX_FILE_RT_XML = "/rt.xml";
	public static final String FLUSSI_RENDICONTAZIONE_DIR_PREFIX = "FlussiRendicontazione/";
	public static final String GOVPAY_FLUSSI_RENDICONTAZIONE_CSV_FILE_NAME = "govpay_flussi_rendicontazione.csv";
	public static final String FILE_RT_DIR_PREFIX = "RT/";
	public static final String GOVPAY_RENDICONTAZIONE_CSV_FILE_NAME = "govpay_rendicontazione.csv";
	private static final String [] MYPIVOT_HEADER_FILE_CSV = { "IUD","codIuv","tipoIdentificativoUnivoco","codiceIdentificativoUnivoco","anagraficaPagatore","indirizzoPagatore","civicoPagatore","capPagatore","localitaPagatore","provinciaPagatore","nazionePagatore","mailPagatore","dataEsecuzionePagamento","importoDovutoPagato","commissioneCaricoPa","tipoDovuto","tipoVersamento","causaleVersamento","datiSpecificiRiscossione","bilancio" };
	private static final String [] GOVPAY_HEADER_FILE_CSV = { "idA2A","idPendenza","idDocumento","descrizioneDocumento","codiceRata","dataScadenza","idVocePendenza","descrizioneVocePendenza","idTipoPendenza","descrizione","anno","identificativoDebitore","anagraficaDebitore","identificativoDominio","identificativoUnivocoVersamento","codiceContestoPagamento","indiceDati","identificativoUnivocoRiscossione","modelloPagamento","singoloImportoPagato","dataEsitoSingoloPagamento","causaleVersamento","datiSpecificiRiscossione","datiAllegati","datiAllegatiVoce","denominazioneAttestante","identificativoAttestante", "contabilita" };
	private static final String [] GOVPAY_FLUSSI_HEADER_FILE_CSV = {"identificativoFlusso","dataOraFlusso","identificativoDominio","identificativoUnivocoRegolamento","dataRegolamento","codiceBicBancaDiRiversamento","numeroTotalePagamenti","importoTotalePagamenti","identificativoUnivocoVersamento","identificativoUnivocoRiscossione","indiceDatiSingoloPagamento","singoloImportoPagato","codiceEsitoSingoloPagamento","dataEsitoSingoloPagamento","denominazioneMittente","identificativoMittente","denominazioneRicevente","identificativoRicevente"	};
	private static final String [] HYPERSIC_APKAPPA_HEADER_FILE_CSV = {"CodiceServizio","DescrizioneServizio","CodiceDebitore","CFPIVADebitore","NominativoDebitore","CodiceDebito","DataEmissione","CausaleDebito","ImportoDebito","CodiceRata","CodiceAvviso","CodiceIUV","DataScadenza","DataPagamento","ImportoPagato","IstitutoMittente","ModalitaPagamento","IBANIncasso","CodiceFlussoRiversamento","DataRiversamento","Annotazioni","LivelloContabile1","CodificaContabile1","QuotaContabile1","LivelloContabile2","CodificaContabile2","QuotaContabile2","LivelloContabile3","CodificaContabile3","QuotaContabile3","LivelloContabile4","CodificaContabile4","QuotaContabile4","LivelloContabile5","CodificaContabile5","QuotaContabile5","LivelloContabile6","CodificaContabile6","QuotaContabile6","LivelloContabile7","CodificaContabile7","QuotaContabile7","LivelloContabile8","CodificaContabile8","QuotaContabile8","LivelloContabile9","CodificaContabile9","QuotaContabile9","LivelloContabile10","CodificaContabile10","QuotaContabile10"}; 
	private static final String [] MAGGIOLI_JPPA_HEADER_FILE_CSV = {"idDominio","iuv","cpp","esito","descrizioneEsito"}; 
	
	
	private static final String QUOTA_CONTABILITA_ACCERTAMENTO = "ACC";
	private static final String QUOTA_CONTABILITA_CAPITOLO = "CAP";
	
	private static Logger log = LoggerWrapperFactory.getLogger(TracciatiNotificaPagamenti.class);
	private TIPO_TRACCIATO tipoTracciato = null;

	public TracciatiNotificaPagamenti(TIPO_TRACCIATO tipoTracciato) {
		this.tipoTracciato = tipoTracciato;
	}

	public void elaboraTracciatoNotificaPagamenti(Dominio dominio, ConnettoreNotificaPagamenti connettore,  IContext ctx) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		TracciatiNotificaPagamentiBD tracciatiNotificaPagamentiBD = null;
		String codDominio = dominio.getCodDominio();
		it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati = null;

		log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], ricerca tracciati in stato non terminale...");
		long countTracciatiInStatoNonTerminalePerDominio = 0;
		try {
			tracciatiNotificaPagamentiBD = new TracciatiNotificaPagamentiBD(configWrapper);
			// controllo se ha tracciati in sospeso
			countTracciatiInStatoNonTerminalePerDominio = tracciatiNotificaPagamentiBD.countTracciatiInStatoNonTerminalePerDominio(codDominio, this.tipoTracciato.toString(), connettore);

			log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], ricerca tracciati in stato non terminale, trovati ["+countTracciatiInStatoNonTerminalePerDominio+"] tracciati in sospeso");
		} catch(Throwable e) {
			log.error("Errore la ricerca dei tracciati "+this.tipoTracciato+" in stato non terminale per il dominio ["+codDominio+"]: " + e.getMessage(), e);
			return;
		} finally {
			if(tracciatiNotificaPagamentiBD != null) {
				tracciatiNotificaPagamentiBD.closeConnection();
			}
		}

		if(countTracciatiInStatoNonTerminalePerDominio == 0) {
			try {
				log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], non sono stati trovati tracciati in sospeso, ricerco record da inserire in un nuovo tracciato");
				
				tracciatiNotificaPagamentiBD = new TracciatiNotificaPagamentiBD(configWrapper);

				tracciatiNotificaPagamentiBD.setupConnection(configWrapper.getTransactionID());

				tracciatiNotificaPagamentiBD.setAtomica(false);

				// cerco la data di partenza delle RT da considerare
				Date dataRtDa = tracciatiNotificaPagamentiBD.getDataPartenzaIntervalloRT(codDominio, this.tipoTracciato.toString(), connettore);

				if(dataRtDa != null) { // se questa data esiste e' la dataRtA del tracciato precedente in ordine temporale ci aggiungo 1 millisecondo per portarla alle 00:00
					Calendar c = Calendar.getInstance();
					c.setTime(dataRtDa);
					c.add(Calendar.MILLISECOND, 1);
					dataRtDa = c.getTime();
				} else { // prima esecuzione del batch, imposto una data molto vecchia.
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);
					c.set(Calendar.DAY_OF_YEAR, 1);
					c.set(Calendar.MONTH, 0);
					c.set(Calendar.YEAR, 1970);
					dataRtDa = c.getTime();
				}

				// calcolo estremo superiore 23:59:59.999 di ieri
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				c.add(Calendar.MILLISECOND, -1);
				Date dataRtA = c.getTime();

				// ricerca delle RT per dominio arrivate tra DataRtDa e DataRtA
				RptBD rptBD = new RptBD(tracciatiNotificaPagamentiBD);

				rptBD.setAtomica(false);

				List<String> listaTipiPendenza = connettore.getTipiPendenza();

				long entriesDaInserireNelTracciato = this.calcolaNumeroEntriesDaInserireNelTracciato(codDominio, dataRtDa, dataRtA, rptBD, listaTipiPendenza);

				if(entriesDaInserireNelTracciato > 0) {
					try {
						tracciatiNotificaPagamentiBD.setAutoCommit(false);

						SerializationConfig config = new SerializationConfig();
						config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
						config.setIgnoreNullValues(true);
						ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);

						log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento nuovo tracciato in stato DRAFT per avviare l'elaborazione.");
						// init tracciato
						TracciatoNotificaPagamenti tracciato = new TracciatoNotificaPagamenti();
						tracciato.setDataRtDa(dataRtDa);
						tracciato.setDataRtA(dataRtA);
						tracciato.setIdDominio(dominio.getId());
						tracciato.setStato(STATO_ELABORAZIONE.DRAFT);
						long progressivo = tracciatiNotificaPagamentiBD.generaProgressivoTracciato(dominio, this.tipoTracciato.toString(), "Tracciato_");
						tracciato.setNomeFile("GOVPAY_" + codDominio + "_"+progressivo+".zip");
						tracciato.setDataCreazione(new Date());
						tracciato.setTipo(this.tipoTracciato);
						tracciato.setVersione(connettore.getVersioneCsv());
						beanDati = new it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti();
						beanDati.setStepElaborazione(STATO_ELABORAZIONE.DRAFT.toString());
						beanDati.setDataUltimoAggiornamento(new Date());
						beanDati.setLineaElaborazione(0);
						tracciato.setBeanDati(serializer.getObject(beanDati));
						// identificativo univoco
						tracciato.setIdentificativo(TracciatiNotificaPagamentiUtils.generaIdentificativoTracciato());

						// insert tracciato
						tracciatiNotificaPagamentiBD.insertTracciato(tracciato);
//						Long idTracciato = tracciato.getId();
						
						log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento nuovo tracciato in stato DRAFT completata.");

						OutputStream oututStreamDestinazione = null;
						Long oid = null;
						Blob blobCsv = null;

						TipiDatabase tipoDatabase = ConnectionManager.getJDBCServiceManagerProperties().getDatabase();

						switch (tipoDatabase) {
						case MYSQL:
							try {
								blobCsv = tracciatiNotificaPagamentiBD.getConnection().createBlob();
								oututStreamDestinazione = blobCsv.setBinaryStream(1);
							} catch (SQLException e) {
								log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
								throw new ServiceException(e);
							}
							break;
						case ORACLE:
							try {
								blobCsv = tracciatiNotificaPagamentiBD.getConnection().createBlob();
								oututStreamDestinazione = blobCsv.setBinaryStream(1);
							} catch (SQLException e) {
								log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
								throw new ServiceException(e);
							}
							break;
						case SQLSERVER:
							try {
								blobCsv = tracciatiNotificaPagamentiBD.getConnection().createBlob();
								oututStreamDestinazione = blobCsv.setBinaryStream(1);
							} catch (SQLException e) {
								log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
								throw new ServiceException(e);
							}
							break;
						case POSTGRESQL:
							org.openspcoop2.utils.datasource.Connection wrappedConn = (org.openspcoop2.utils.datasource.Connection) tracciatiNotificaPagamentiBD.getConnection();
							Connection wrappedConnection = wrappedConn.getWrappedConnection();

							Connection underlyingConnection = null;
							try {
								Method method = wrappedConnection.getClass().getMethod("getUnderlyingConnection");

								Object invoke = method.invoke(wrappedConnection);

								underlyingConnection = (Connection) invoke;
							} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								log.error("Errore durante la lettura dell'oggetto connessione: " + e.getMessage(), e);
								throw new ServiceException(e);
							}

							org.postgresql.PGConnection pgConnection = null;
							try {
								if(underlyingConnection.isWrapperFor(org.postgresql.PGConnection.class)) {
									pgConnection = underlyingConnection.unwrap(org.postgresql.PGConnection.class);
								} else {
									pgConnection = (org.postgresql.PGConnection) underlyingConnection;				
								}

								// Get the Large Object Manager to perform operations with
								LargeObjectManager lobj = pgConnection.getLargeObjectAPI();

								// Create a new large object
								oid = lobj.createLO(LargeObjectManager.WRITE);

								// Open the large object for writing
								LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);

								oututStreamDestinazione = obj.getOutputStream();
							} catch (SQLException e) {
								log.error("Errore durante la creazione dell'outputstream: " + e.getMessage(), e);
								throw new ServiceException(e);
							}
							break;
						case DB2:
						case DEFAULT:
						case DERBY:
						case HSQL:
						default:
							throw new ServiceException("TipoDatabase ["+tipoDatabase+"] non gestito.");
						}

						try (ZipOutputStream zos = new ZipOutputStream(oututStreamDestinazione);){

							switch (this.tipoTracciato) {

							case MYPIVOT:
								this.popolaTracciatoMyPivot(connettore, configWrapper, codDominio, beanDati, dataRtDa, dataRtA, rptBD, listaTipiPendenza, progressivo, zos);
								break;
							case SECIM:
								this.popolaTracciatoSecim(connettore, configWrapper, codDominio, beanDati, dataRtDa, dataRtA, rptBD, listaTipiPendenza, progressivo, zos); 
								break;
							case GOVPAY:
								this.popolaTracciatoGovpay(connettore, configWrapper, dominio, beanDati, dataRtDa, dataRtA, rptBD, listaTipiPendenza, progressivo, serializer, zos);
								break;
							case HYPERSIC_APK:
								this.popolaTracciatoHyperSicAPK(tracciato, connettore, configWrapper, codDominio, beanDati, dataRtDa, dataRtA, rptBD, listaTipiPendenza, progressivo, zos); 
								break;
							case MAGGIOLI_JPPA:
								this.popolaTracciatoMaggioliJPPA(ctx, connettore, configWrapper, dominio, beanDati, dataRtDa, dataRtA, rptBD, listaTipiPendenza, progressivo, serializer, zos); 
								break;
							}
							// chiuso stream
							zos.flush();
							zos.close();

							tracciato.setStato(STATO_ELABORAZIONE.FILE_NUOVO);
							beanDati.setStepElaborazione(STATO_ELABORAZIONE.FILE_NUOVO.toString());
							
							try {
								tracciato.setBeanDati(serializer.getObject(beanDati));
							} catch (IOException e1) {}
							// update tracciato
							switch (tipoDatabase) {
							case MYSQL:
							case ORACLE:
							case SQLSERVER:
								tracciatiNotificaPagamentiBD.updateFineElaborazioneCsvBlob(tracciato,blobCsv);
								break;
							case POSTGRESQL:
								tracciatiNotificaPagamentiBD.updateFineElaborazioneCsvOid(tracciato,oid);
								break;
							case DB2:
							case DEFAULT:
							case DERBY:
							case HSQL:
							default:
								throw new ServiceException("TipoDatabase ["+tipoDatabase+"] non gestito.");
							}
							
							log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], salvataggio contenuto completato");

							if(!tracciatiNotificaPagamentiBD.isAutoCommit()) tracciatiNotificaPagamentiBD.commit();
						} catch (java.io.IOException e) { // gestione errori scrittura zip
							log.error(e.getMessage(), e);
							throw e;
						} 
					} catch(Throwable e) {
						log.error("Errore durante l'elaborazione del tracciato "+this.tipoTracciato+": " + e.getMessage(), e);
						if(!tracciatiNotificaPagamentiBD.isAutoCommit())  tracciatiNotificaPagamentiBD.rollback();	
					} finally {
						if(!tracciatiNotificaPagamentiBD.isAutoCommit()) tracciatiNotificaPagamentiBD.setAutoCommit(true);
					}
				}
			} catch(Throwable e) {
				log.error("Errore durante l'elaborazione del tracciato "+this.tipoTracciato+": " + e.getMessage(), e);
			} finally {
				if(tracciatiNotificaPagamentiBD != null) {
					tracciatiNotificaPagamentiBD.closeConnection();
				}
			}
		}
	}

	private long calcolaNumeroEntriesDaInserireNelTracciato(String codDominio, Date dataRtDa, Date dataRtA, RptBD rptBD, List<String> listaTipiPendenza) throws ServiceException {
		
		long entriesDaInserireNelTracciato = 0;
		
		switch (this.tipoTracciato) {
		case GOVPAY:
		case MYPIVOT:
		case SECIM:
		case MAGGIOLI_JPPA:
			log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], verranno ricercate RT da inserire in un nuovo tracciato da ["
					+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtDa)+"] a ["+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtA)+"]");
			entriesDaInserireNelTracciato = rptBD.countRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza);
			log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+ codDominio +"], trovate ["+ entriesDaInserireNelTracciato +"] RT da inserire in un nuovo tracciato");
			break;
		case HYPERSIC_APK:
			log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], verranno ricercate Riscossioni da inserire in un nuovo tracciato da ["
					+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtDa)+"] a ["+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtA)+"]");
			EntratePrevisteBD pagamentiBD = new EntratePrevisteBD(rptBD);
			pagamentiBD.setAtomica(false);
			entriesDaInserireNelTracciato = pagamentiBD.countRiscossioniDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, true);
			log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+ codDominio +"], trovate ["+ entriesDaInserireNelTracciato +"] Riscossioni da inserire in un nuovo tracciato");
			if(entriesDaInserireNelTracciato == 0) { // se non ci sono nuove rendicontazioni da inserire controllo se ci sono pagamenti non rendicontati relativi a 5 giorni fa
				// allinea gli intervalli di date
				Calendar cDa = Calendar.getInstance();
				cDa.setTime(dataRtDa);
				cDa.add(Calendar.DAY_OF_YEAR, -5);
				Date dateDa = cDa.getTime();
				
				Calendar cA = Calendar.getInstance();
				cA.setTime(dataRtA);
				cA.add(Calendar.DAY_OF_YEAR, -5);
				Date dateA = cA.getTime(); 
				
				log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], verranno ricercate Riscossioni non rendicontate da inserire in un nuovo tracciato da ["
						+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dateDa)+"] a ["+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dateA)+"]");
				entriesDaInserireNelTracciato = pagamentiBD.countRiscossioniDominio(codDominio, dateDa, dateA, listaTipiPendenza, false);
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+ codDominio +"], trovate ["+ entriesDaInserireNelTracciato +"] Riscossioni non rendicontate da inserire in un nuovo tracciato");
			}
			
			
			break;
		}
		
		return entriesDaInserireNelTracciato;
	}
	
	private void popolaTracciatoMyPivot(ConnettoreNotificaPagamenti connettore, BDConfigWrapper configWrapper, String codDominio,
			it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, Date dataRtDa, Date dataRtA,
			RptBD rptBD, List<String> listaTipiPendenza, long progressivo, ZipOutputStream zos)
			throws java.io.IOException, ServiceException, JAXBException, SAXException, ValidationException {
		
		CSVUtils csvUtils = CSVUtils.getInstance(CSVFormat.DEFAULT.withDelimiter(';'));
		
		ZipEntry tracciatoOutputEntry = new ZipEntry("GOVPAY_" + codDominio + "_"+progressivo+".csv");
		zos.putNextEntry(tracciatoOutputEntry);
		
		zos.write(csvUtils.toCsv(MYPIVOT_HEADER_FILE_CSV).getBytes());
		
		int lineaElaborazione = 0;
		int offset = 0;
		int limit = 100; 
		List<Rpt> rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+rtList.size()+"] RT da inserire nel tracciato");
		int totaleRt = rtList.size();
		do {
			if(rtList.size() > 0) {
				for (Rpt rpt : rtList) {
					lineaElaborazione ++;
					beanDati.setLineaElaborazione(lineaElaborazione);
					zos.write(csvUtils.toCsv(this.creaLineaCsvMyPivot(rpt, configWrapper)).getBytes());
				}
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento ["+rtList.size()+"] RT nel tracciato completato");
			}

			offset += limit;
			rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
			log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+rtList.size()+"] RT da inserire nel tracciato");
			totaleRt += rtList.size();
		}while(rtList.size() > 0);
		
		log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserite ["+totaleRt+"] RT nel tracciato");

		// chiusa entry
		zos.flush();
		zos.closeEntry();
		
		beanDati.setNumRtTotali(totaleRt);
	}
	
	private void popolaTracciatoSecim(ConnettoreNotificaPagamenti connettore, BDConfigWrapper configWrapper, String codDominio,
			it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, Date dataRtDa, Date dataRtA,
			RptBD rptBD, List<String> listaTipiPendenza, long progressivo, ZipOutputStream zos)
			throws java.io.IOException, ServiceException, JAXBException, SAXException, ValidationException {
		
		ZipEntry tracciatoOutputEntry = new ZipEntry("GOVPAY_" + codDominio + "_"+progressivo+".txt");
		zos.putNextEntry(tracciatoOutputEntry);
		
		int lineaElaborazione = 0;
		int offset = 0;
		int limit = 100; 
		List<Rpt> rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+rtList.size()+"] RT da inserire nel tracciato");
		int totaleRt = rtList.size();
		
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			do {
				if(rtList.size() > 0) {
					for (Rpt rpt : rtList) {
						lineaElaborazione ++;
						beanDati.setLineaElaborazione(lineaElaborazione);
						this.creaLineaCsvSecim(rpt, configWrapper, lineaElaborazione, connettore, zos, baos);
					}
					log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento ["+rtList.size()+"] RT nel tracciato completato");
				}
	
				offset += limit;
				rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+rtList.size()+"] RT da inserire nel tracciato");
				totaleRt += rtList.size();
			}while(rtList.size() > 0);
			
			log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserite ["+totaleRt+"] RT nel tracciato");
	
			// chiusa entry
			zos.flush();
			zos.closeEntry(); 
			
			if(baos.size() > 0) {
				
				ZipEntry tracciatoNoSecimOutputEntry = new ZipEntry("GOVPAY_" + codDominio + "_"+progressivo+"_NOSECIM.txt");
				zos.putNextEntry(tracciatoNoSecimOutputEntry);
				
				zos.write(baos.toByteArray());
				
				// chiusa entry
				zos.flush();
				zos.closeEntry(); 
			}
		
		} finally {
			
		}
		
		beanDati.setNumRtTotali(totaleRt);
	}
	
	private void popolaTracciatoGovpay(ConnettoreNotificaPagamenti connettore, BDConfigWrapper configWrapper, Dominio dominio,
			it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, Date dataRtDa, Date dataRtA,
			RptBD rptBD, List<String> listaTipiPendenza, long progressivo, ISerializer serializer, ZipOutputStream zos)
			throws java.io.IOException, ServiceException, JAXBException, SAXException, ValidationException, IOException { 
		String codDominio = dominio.getCodDominio();
		
		log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"] in corso...");
		
		// Entry 1. metadati dell'estrazione
		
		ZipEntry metadataEntry = new ZipEntry("metadata.json");
		zos.putNextEntry(metadataEntry);
		
		zos.write(this.creaFileMetadatiTracciatoGovPay(connettore, configWrapper, dominio, beanDati, dataRtDa, dataRtA, rptBD, listaTipiPendenza, serializer).getBytes());
		
		// chiusa entry
		zos.flush();
		zos.closeEntry();
		
		
		// Entry 2. sintesi pagamenti
		
		log.debug("Creazione file di sintesi pagamenti in corso...");
		
		CSVUtils csvUtils = CSVUtils.getInstance(CSVFormat.DEFAULT);
		
		ZipEntry tracciatoOutputEntry = new ZipEntry(GOVPAY_RENDICONTAZIONE_CSV_FILE_NAME);
		zos.putNextEntry(tracciatoOutputEntry);
		
		zos.write(csvUtils.toCsv(GOVPAY_HEADER_FILE_CSV).getBytes());
		
		int lineaElaborazione = 0;
		int offset = 0;
		int limit = 100; 
		List<Rpt> rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		log.trace("Creazione file di sintesi pagamenti, trovate ["+rtList.size()+"] RT da inserire...");
		int totaleRt = 0;
		do {
			if(rtList.size() > 0) {
				for (Rpt rpt : rtList) {
					lineaElaborazione ++;
					totaleRt ++;
					beanDati.setLineaElaborazione(lineaElaborazione);
					
					List<List<String>> linee = this.creaLineaCsvGovPay(rpt, configWrapper);
					for (List<String> linea : linee) {
						String [] lineaArray  = linea.toArray(new String[linea.size()]);
						zos.write(csvUtils.toCsv(lineaArray).getBytes());
					}
				}
				log.trace("Completato inserimento ["+rtList.size()+"] RT nel file di sintesi pagamenti");
			}

			offset += limit;
			rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
			log.trace("Creazione file di sintesi pagamenti, trovate ["+rtList.size()+"] RT da inserire...");
		}while(rtList.size() > 0);
		
		beanDati.setNumRtTotali(totaleRt);
		
		log.debug("Creazione file di sintesi pagamenti completato, inserite ["+totaleRt+"] RT.");

		// chiusa entry
		zos.flush();
		zos.closeEntry();
		
		// Entry 3. ricevute 
		log.debug("Creazione ricevute in corso...");
		
		offset = 0;
		rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		
		do {
			if(rtList.size() > 0) {
				for (Rpt rpt : rtList) {
					String idDominio = rpt.getCodDominio();
					String iuv = rpt.getIuv();
					String ccp = rpt.getCcp();
					if(ccp == null || ccp.equals("n/a"))
						ccp = "na";
					
					if(rpt.getXmlRt() != null) {
						ZipEntry rtEntry = new ZipEntry(TracciatiNotificaPagamentiUtils.creaNomeEntryRT(idDominio, iuv, ccp));
						zos.putNextEntry(rtEntry);
						
						byte[] b = rpt.getXmlRt();
						
						zos.write(b);
						
						// chiusa entry
						zos.flush();
						zos.closeEntry();
					}
					
					if(rpt.getXmlRpt() != null) {
						ZipEntry rtEntry = new ZipEntry(TracciatiNotificaPagamentiUtils.creaNomeEntryRPT(idDominio, iuv, ccp));
						zos.putNextEntry(rtEntry);
						
						byte[] b = rpt.getXmlRpt();
						
						zos.write(b);
						
						// chiusa entry
						zos.flush();
						zos.closeEntry();
					}
					
				}
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento ["+rtList.size()+"] RT nel tracciato completato");
			}

			offset += limit;
			rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
			log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+rtList.size()+"] RT da inserire nel tracciato");
		}while(rtList.size() > 0);
		
		
		log.debug("Creazione ricevute completata, inserite ["+totaleRt+"] ricevute.");
		
		// Entry 4. sintesi flussi 
		
		log.debug("Creazione file di sintesi flussi in corso...");
		
		ZipEntry frOutputEntry = new ZipEntry(GOVPAY_FLUSSI_RENDICONTAZIONE_CSV_FILE_NAME);
		zos.putNextEntry(frOutputEntry);
		
		zos.write(csvUtils.toCsv(GOVPAY_FLUSSI_HEADER_FILE_CSV).getBytes());
		
		FrBD frBD = new FrBD(rptBD);
		
		frBD.setAtomica(false); 
		
		offset = 0;
		int totaleFr= 0;
		List<Fr> frList = frBD.ricercaFrDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		
		do {
			if(frList.size() > 0) {
				for (Fr fr : frList) {
					
					List<List<String>> linee = this.creaLineaCsvFrGovPay(fr, configWrapper);
					for (List<String> linea : linee) {
						String [] lineaArray  = linea.toArray(new String[linea.size()]);
						zos.write(csvUtils.toCsv(lineaArray).getBytes());
					}
					
					totaleFr ++;
				}
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento ["+frList.size()+"] Flussi Rendicontazione nel tracciato completato");
			}

			offset += limit;
			frList = frBD.ricercaFrDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		}while(rtList.size() > 0);
		
		log.debug("Creazione file di sintesi flussi rendicontazione completato, inseriti ["+totaleFr+"] Flussi.");

		// chiusa entry
		zos.flush();
		zos.closeEntry();
		
		
		// Entry 5. flussi 
		log.debug("Creazione flussi in corso...");
		
		offset = 0;
		totaleFr= 0;
		frList = frBD.ricercaFrDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		
		do {
			if(frList.size() > 0) {
				for (Fr fr : frList) {
					String idFlusso = fr.getCodFlusso();
					Date dataFlusso = fr.getDataFlusso();
					String dataFlussoS = SimpleDateFormatUtils.newSimpleDateFormat().format(dataFlusso);
					
					ZipEntry rtEntry = new ZipEntry(TracciatiNotificaPagamentiUtils.creaNomeEntryFlussoRendicontazione(idFlusso, dataFlussoS));
					zos.putNextEntry(rtEntry);
					
					byte[] b = fr.getXml();
					
					zos.write(b);
					
					// chiusa entry
					zos.flush();
					zos.closeEntry();
					
					totaleFr ++;
				}
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento ["+frList.size()+"] Flussi Rendicontazione nel tracciato completato");
			}

			offset += limit;
			frList = frBD.ricercaFrDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		}while(rtList.size() > 0);
		
		log.debug("Creazione flussi rendicontazione completata, inseriti ["+totaleFr+"] flussi.");
		
		
		log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"] completato.");
	}
	
	
	private void popolaTracciatoHyperSicAPK(TracciatoNotificaPagamenti tracciato, ConnettoreNotificaPagamenti connettore, BDConfigWrapper configWrapper, String codDominio,
			it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, Date dataRtDa, Date dataRtA,
			RptBD rptBD, List<String> listaTipiPendenza, long progressivo, ZipOutputStream zos)
			throws java.io.IOException, ServiceException, JAXBException, SAXException, ValidationException {
		
		/*
		 Il naming del file deve essere composto dai seguenti elementi, separati da “_”:
• “RENDICONTAZIONE”, stringa fissa per identificativo tipologia di flusso;
• “AV20”, stringa fissa per identificativo versione del flusso;
• “Codice Fiscale Ente Creditore”, nel formato numerico di 11 caratteri;
• “Data Creazione Flusso”, nel formato AAAAMMGG;
• “Progressivo Flusso”, per la data di creazione flusso nel formato NNN.
		 * */
		
		CSVUtils csvUtils = CSVUtils.getInstance(CSVFormat.DEFAULT.withDelimiter(';'));
		
		String dataCreazioneFlusso = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(tracciato.getDataCreazione());
		String progressivoS = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, "", "progressivoFile",progressivo +"", 3, true, true);
		
		int lineaElaborazione = 0;
		int offset = 0;
		int limit = 100; 
		EntratePrevisteBD entratePrevisteBD = new EntratePrevisteBD(rptBD);
		entratePrevisteBD.setAtomica(false);
		
		List<EntrataPrevista> riscossioniList = entratePrevisteBD.ricercaRiscossioniDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, true, offset, limit);
		log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+riscossioniList.size()+"] Riscossioni da inserire nel tracciato");
		int totaleRt = riscossioniList.size();
		
		// file rendicontazioni
		if(totaleRt > 0) {
			ZipEntry tracciatoOutputEntry = new ZipEntry("RENDICONTAZIONE_AV20_" + codDominio + "_"+ dataCreazioneFlusso + "_" + progressivoS+ ".csv");
			zos.putNextEntry(tracciatoOutputEntry);
			
			zos.write(csvUtils.toCsv(HYPERSIC_APKAPPA_HEADER_FILE_CSV).getBytes());
			
			do {
				if(riscossioniList.size() > 0) {
					for (EntrataPrevista pagamento : riscossioniList) {
						lineaElaborazione ++;
						beanDati.setLineaElaborazione(lineaElaborazione);
						zos.write(csvUtils.toCsv(this.creaLineaCsvHyperSicAPKappa(entratePrevisteBD, pagamento, configWrapper, totaleRt, connettore)).getBytes());
					}
					log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento ["+riscossioniList.size()+"] Riscossioni nel tracciato completato");
				}
	
				offset += limit;
				riscossioniList = entratePrevisteBD.ricercaRiscossioniDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, true, offset, limit);
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+riscossioniList.size()+"] Riscossioni da inserire nel tracciato");
				totaleRt += riscossioniList.size();
			}while(riscossioniList.size() > 0);
			
			log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserite ["+totaleRt+"] Riscossioni nel tracciato");
	
			// chiusa entry
			zos.flush();
			zos.closeEntry(); 
			
			beanDati.setNumRtTotali(totaleRt);
		}
		
		/*
		 Le riscossioni non rendicontate per piu' di 5 gg vengono incluse in un csv separato avente la medesima sintassi, ma con i campi relativi al flusso di rendicontazione vuoti e con progressivo flusso = 999.
		 */
		
		// allinea gli intervalli di date
		Calendar cDa = Calendar.getInstance();
		cDa.setTime(dataRtDa);
		cDa.add(Calendar.DAY_OF_YEAR, -5);
		Date dateDa = cDa.getTime();
		
		Calendar cA = Calendar.getInstance();
		cA.setTime(dataRtA);
		cA.add(Calendar.DAY_OF_YEAR, -5);
		Date dateA = cA.getTime(); 
		
		lineaElaborazione = 0;
		offset = 0;
		riscossioniList = entratePrevisteBD.ricercaRiscossioniDominio(codDominio, dateDa, dateA, listaTipiPendenza, false, offset, limit);
		log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+riscossioniList.size()+"] Riscossioni non rendicontate da inserire nel tracciato");
		totaleRt = riscossioniList.size();
		
		// file rendicontazioni
		if(totaleRt > 0) {
			ZipEntry tracciatoNoSecimOutputEntry = new ZipEntry("RENDICONTAZIONE_AV20_" + codDominio + "_"+ dataCreazioneFlusso + "_" + "999"+ ".csv");
			zos.putNextEntry(tracciatoNoSecimOutputEntry);
			
			zos.write(csvUtils.toCsv(HYPERSIC_APKAPPA_HEADER_FILE_CSV).getBytes());
			
			do {
				if(riscossioniList.size() > 0) {
					for (EntrataPrevista pagamento : riscossioniList) {
						lineaElaborazione ++;
						beanDati.setLineaElaborazione(lineaElaborazione);
						zos.write(csvUtils.toCsv(this.creaLineaCsvHyperSicAPKappa(entratePrevisteBD, pagamento, configWrapper, totaleRt, connettore)).getBytes());
					}
					log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento ["+riscossioniList.size()+"] Riscossioni non rendicontate nel tracciato completato");
				}
	
				offset += limit;
				riscossioniList = entratePrevisteBD.ricercaRiscossioniDominio(codDominio, dateDa, dateA, listaTipiPendenza, false, offset, limit);
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+riscossioniList.size()+"] Riscossioni non rendicontate da inserire nel tracciato");
				totaleRt += riscossioniList.size();
			}while(riscossioniList.size() > 0);
			
			log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserite ["+totaleRt+"] Riscossioni non rendicontate nel tracciato");
	
			// chiusa entry
			zos.flush();
			zos.closeEntry(); 
		}
		
		
	}


	
	private List<List<String>> creaLineaCsvFrGovPay(Fr fr, BDConfigWrapper configWrapper) throws JAXBException, SAXException {
		List<List<String>> linee = new ArrayList<List<String>>();
		
		FlussoRiversamento flussoRiversamento = JaxbUtils.toFR(fr.getXml());
		CtIstitutoMittente istitutoMittente = flussoRiversamento.getIstitutoMittente();
		CtIstitutoRicevente istitutoRicevente = flussoRiversamento.getIstitutoRicevente();
		String identificativoFlusso = flussoRiversamento.getIdentificativoFlusso();
		Date dataOraFlusso = flussoRiversamento.getDataOraFlusso();
		String identificativoUnivocoRegolamento = flussoRiversamento.getIdentificativoUnivocoRegolamento();
		BigDecimal importoTotalePagamenti = flussoRiversamento.getImportoTotalePagamenti();
		BigDecimal numeroTotalePagamenti = flussoRiversamento.getNumeroTotalePagamenti();
		Date dataRegolamento = flussoRiversamento.getDataRegolamento();
		String codiceBicBancaDiRiversamento = flussoRiversamento.getCodiceBicBancaDiRiversamento();
		String idDominio = fr.getCodDominio();
		
		List<CtDatiSingoliPagamenti> datiSingoliPagamentis = flussoRiversamento.getDatiSingoliPagamentis();
		
		for (CtDatiSingoliPagamenti ctDatiSingoliPagamenti : datiSingoliPagamentis) {
			List<String> linea = new ArrayList<String>();
			
			String codiceEsitoSingoloPagamento = ctDatiSingoliPagamenti.getCodiceEsitoSingoloPagamento();
			Date dataEsitoSingoloPagamento = ctDatiSingoliPagamenti.getDataEsitoSingoloPagamento();
			String identificativoUnivocoRiscossione = ctDatiSingoliPagamenti.getIdentificativoUnivocoRiscossione();
			String identificativoUnivocoVersamento = ctDatiSingoliPagamenti.getIdentificativoUnivocoVersamento();
			Integer indiceDatiSingoloPagamento = ctDatiSingoliPagamenti.getIndiceDatiSingoloPagamento();
			BigDecimal singoloImportoPagato = ctDatiSingoliPagamenti.getSingoloImportoPagato();
			
			// identificativoFlusso: fr.identificativoFlusso
			linea.add(identificativoFlusso);
			// dataOraFlusso: fr.dataOraFlusso
			linea.add(SimpleDateFormatUtils.newSimpleDateFormat().format(dataOraFlusso));
			// identificativoDominio: fr.coddominio
			linea.add(idDominio);
			// identificativoUnivocoRegolamento: fr.identificativoUnivocoRegolamento
			linea.add(identificativoUnivocoRegolamento);
			// dataRegolamento: fr.dataRegolamento
			linea.add(SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(dataRegolamento));
			// codiceBicBancaDiRiversamento: fr.codiceBicBancaDiRiversamento
			linea.add(codiceBicBancaDiRiversamento);
			// numeroTotalePagamenti: fr.numeroTotalePagamenti
			linea.add(numeroTotalePagamenti.intValue()+"");
			// importoTotalePagamenti: fr.importoTotalePagamenti
			linea.add(TracciatiNotificaPagamentiUtils.printImporto(importoTotalePagamenti, false));
			// identificativoUnivocoVersamento: fr.ctDatiSingoliPagamenti[i].identificativoUnivocoVersamento
			linea.add(identificativoUnivocoVersamento);
			// identificativoUnivocoRiscossione: fr.ctDatiSingoliPagamenti[i].identificativoUnivocoRiscossione
			linea.add(identificativoUnivocoRiscossione);
			// indiceDatiSingoloPagamento: fr.ctDatiSingoliPagamenti[i].indiceDatiSingoloPagamento
			linea.add(indiceDatiSingoloPagamento != null ? indiceDatiSingoloPagamento.intValue() + "" : "");
			// singoloImportoPagato: fr.ctDatiSingoliPagamenti[i].singoloImportoPagato
			linea.add(TracciatiNotificaPagamentiUtils.printImporto(singoloImportoPagato, false));
			// codiceEsitoSingoloPagamento: fr.ctDatiSingoliPagamenti[i].codiceEsitoSingoloPagamento
			linea.add(codiceEsitoSingoloPagamento);
			// dataEsitoSingoloPagamento: fr.ctDatiSingoliPagamenti[i].dataEsitoSingoloPagamento
			linea.add(SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(dataEsitoSingoloPagamento));
			// denominazioneMittente fr.istitutoMittente.denominazioneMittente
			linea.add(istitutoMittente.getDenominazioneMittente());
			// identificativoMittente fr.istitutoMittente.identificativoUnivocoMittente.codiceIdentificativoUnivoco
			linea.add(istitutoMittente.getIdentificativoUnivocoMittente().getCodiceIdentificativoUnivoco());
			// denominazioneRicevente fr.istitutoRicevente.denominazioneRicevente
			linea.add(istitutoRicevente.getDenominazioneRicevente());
			// identificativoRicevente fr.istitutoRicevente.identificativoUnivocoRicevente.codiceIdentificativoUnivoco
			linea.add(istitutoRicevente.getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco());
			
			linee.add(linea);
		}
		return linee;
	}

	private String creaFileMetadatiTracciatoGovPay(ConnettoreNotificaPagamenti connettore, BDConfigWrapper configWrapper, Dominio dominio,
			it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, Date dataRtDa, Date dataRtA,
			RptBD rptBD, List<String> listaTipiPendenza, ISerializer serializer) throws IOException { 
		String metadati = "{}";
		
		Map<String, Object> json  = new HashMap<String, Object>();
		
		// Dominio
		json.put("enteCreditore", dominio.getRagioneSociale());
		json.put("idDominio", dominio.getCodDominio());
		
		// date
		String dataInizio = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(dataRtDa);
		json.put("dataInizio", dataInizio);
		String dataFine = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(dataRtA);
		json.put("dataFine", dataFine);

		// info tracciato
		json.put("destinatari", StringUtils.join(connettore.getEmailIndirizzi(), ","));
		json.put("numeroPagamenti", beanDati.getNumRtTotali()+"");
		json.put("versioneTracciato", connettore.getVersioneCsv());
		
		// tipi pendenza
		if(listaTipiPendenza != null && listaTipiPendenza.size() > 0) {
			json.put("tipiPendenza", StringUtils.join(listaTipiPendenza, ","));
		}
		
		metadati = serializer.getObject(json);
		
		return metadati;
	}
	
	private List<List<String>> creaLineaCsvGovPay(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException, ValidationException { 
		Versione versione = rpt.getVersione();
		
		switch (versione) {
		case SANP_240:
			return TracciatiNotificaPagamentiUtils.creaLineaCsvGovPayRpt_SANP24(rpt,configWrapper);
		case SANP_230:
		default:
			return TracciatiNotificaPagamentiUtils.creaLineaCsvGovPayRpt_SANP23(rpt,configWrapper);
		}
	}

	
	public List<TracciatoNotificaPagamenti> findTracciatiInStatoNonTerminalePerDominio(String codDominio, int offset, int limit, ConnettoreNotificaPagamenti connettore, IContext ctx) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		TracciatiNotificaPagamentiBD tracciatiNotificaPagamentiBD = null;
		try {
			tracciatiNotificaPagamentiBD = new TracciatiNotificaPagamentiBD(configWrapper);
			// lista tracciati da spedire
			return tracciatiNotificaPagamentiBD.findTracciatiInStatoNonTerminalePerDominio(codDominio, offset, limit, this.tipoTracciato.toString(), connettore);
		} catch(Throwable e) {
			log.error("Errore la ricerca dei tracciati "+this.tipoTracciato+" in stato non terminale per il dominio ["+codDominio+"]: " + e.getMessage(), e);
			throw new ServiceException(e);
		} finally {
			if(tracciatiNotificaPagamentiBD != null) {
				tracciatiNotificaPagamentiBD.closeConnection();
			}
		}
	}

	private String [] creaLineaCsvMyPivot(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException, ValidationException { 
		Versione versione = rpt.getVersione();
		
		switch (versione) {
		case SANP_240:
			return TracciatiNotificaPagamentiUtils.creaLineaCsvMyPivotRpt_SANP24(rpt,configWrapper);
		case SANP_230:
		default:
			return TracciatiNotificaPagamentiUtils.creaLineaCsvMyPivotRpt_SANP23(rpt,configWrapper);
		}
	}
	
	private void creaLineaCsvSecim(Rpt rpt, BDConfigWrapper configWrapper, int numeroLinea, ConnettoreNotificaPagamenti connettore, OutputStream secimOS, OutputStream noSecimOS) throws ServiceException, JAXBException, SAXException, ValidationException, java.io.IOException { 
		Versione versione = rpt.getVersione();
		
		switch (versione) {
		case SANP_240:
			TracciatiNotificaPagamentiUtils.creaLineaCsvSecimRpt_SANP24(log, rpt, configWrapper, numeroLinea, connettore, secimOS, noSecimOS);
			break;
		case SANP_230:
		default:
			TracciatiNotificaPagamentiUtils.creaLineaCsvSecimRpt_SANP23(log, rpt, configWrapper, numeroLinea, connettore, secimOS, noSecimOS);
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private String[] creaLineaCsvHyperSicAPKappa(EntratePrevisteBD pagamentiBD, EntrataPrevista pagamento, BDConfigWrapper configWrapper, int numeroLinea, ConnettoreNotificaPagamenti connettore) throws ServiceException, JAXBException, SAXException, ValidationException, java.io.IOException { 
		List<String> linea = new ArrayList<String>();

		Documento documento = null;
		if(pagamento.getIdDocumento() != null) {
			DocumentiBD documentiBD = new DocumentiBD(pagamentiBD);
			documentiBD.setAtomica(false);
			try {
				documento = documentiBD.getDocumento(pagamento.getIdDocumento());
			} catch (NotFoundException e) { // documento non trovato 
				
			}
		}
		
		String entryKey = "RISC_" + pagamento.getIuvPagamento();
		String contabilitaString = pagamento.getContabilita();
		String codiceServizio = null;
		String descrizioneServizio = null;
		Contabilita contabilita = null;
		if(contabilitaString != null && contabilitaString.length() > 0) {
			contabilita = JSONSerializable.parse(contabilitaString, Contabilita.class);
			
			Object proprietaCustomObj = contabilita.getProprietaCustom();
						
			if(proprietaCustomObj != null) {
				if(proprietaCustomObj instanceof String) {
					String proprietaCustom = (String) proprietaCustomObj;
					if(proprietaCustom != null && proprietaCustom.length() > 0) {
						Map<String, Object> parse = JSONSerializable.parse(proprietaCustom, Map.class);
						// leggo proprieta
						if(parse.containsKey("codiceServizio")) {
							codiceServizio = (String) parse.get("codiceServizio");
						}
						if(parse.containsKey("descrizioneServizio")) {
							descrizioneServizio = (String) parse.get("descrizioneServizio");
						}
					}
				}  else if(proprietaCustomObj instanceof java.util.LinkedHashMap) {
					java.util.LinkedHashMap<?,?> parse = (LinkedHashMap<?,?>) proprietaCustomObj;
					
					// leggo proprieta
					if(parse.containsKey("codiceServizio")) {
						codiceServizio = (String) parse.get("codiceServizio");
					}
					if(parse.containsKey("descrizioneServizio")) {
						descrizioneServizio = (String) parse.get("descrizioneServizio");
					}
				}
			}
		}
		
//		CodiceServizio 10	$.vocePendenza.contabilita.quoteContabili.proprietaCustom.codiceServizio o versamento.tipoPendenza.codTipoPendenza
		String codiceServizioValue = codiceServizio != null ? codiceServizio : pagamento.getCodTipoVersamento();
		codiceServizioValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "CodiceServizio", codiceServizioValue, 10);
		linea.add(codiceServizioValue);

//		DescrizioneServizio 123 $.vocePendenza.contabilita.quoteContabili.proprietaCustom.descrizioneServizio o versamento.tipoPendenza.descrizione		
		String descrizioneServizioValue = descrizioneServizio != null ? descrizioneServizio : (pagamento.getDescrizioneTipoVersamento() != null ? pagamento.getDescrizioneTipoVersamento() : "");
		descrizioneServizioValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "DescrizioneServizio", descrizioneServizioValue, 123);
		linea.add(descrizioneServizioValue);
			
//		CodiceDebitore 20 VUOTO 
		linea.add("");
			
//		CFPIVADebitore 16 $.soggettoPagatore.identificativo
		String identificativoDebitoreValue = pagamento.getIdentificativoDebitore();
		identificativoDebitoreValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "CFPIVADebitore", identificativoDebitoreValue, 16);
		linea.add(identificativoDebitoreValue);
		
//		NominativoDebitore 70 $.soggettoPagatore.anagrafica
		String anagraficaDebitoreValue = pagamento.getAnagraficaDebitore();
		anagraficaDebitoreValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "NominativoDebitore", anagraficaDebitoreValue, 70);
		linea.add(anagraficaDebitoreValue);
		
//		CodiceDebito 30 $.documento.identificativo o $.idPendenza
		String codiceDebitoValue = documento != null ? documento.getCodDocumento() : pagamento.getCodVersamentoEnte();
		codiceDebitoValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "CodiceDebito", codiceDebitoValue, 30);
		linea.add(codiceDebitoValue);
		
//		DataEmissione $.dataCaricamento 
		linea.add(SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(pagamento.getDataCreazione()));
		
//		CausaleDebito 100 $.causale
		String causaleValue = pagamento.getCausaleVersamento().getSimple();
		causaleValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "CausaleDebito", causaleValue, 100);
		linea.add(causaleValue);
		
//		ImportoDebito importo (in centesimi)
		linea.add(TracciatiNotificaPagamentiUtils.printImporto(pagamento.getImportoVersamento(), true));
		
//		CodiceRata 30 $.documento.numeroRata
		String codiceRataValue = pagamento.getNumeroRata() != null ? pagamento.getNumeroRata() +"" : "";
		codiceRataValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "CodiceRata", codiceRataValue, 30);
		linea.add(codiceRataValue);
		
//		CodiceAvviso 35 $.numeroAvviso
		String codiceAvvisoValue = pagamento.getNumeroAvviso();
		codiceAvvisoValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "CodiceAvviso", codiceAvvisoValue, 35);
		linea.add(codiceAvvisoValue);
		
//		CodiceIUV 35 $.iuvPagamento
		String iuvPagamentoValue = pagamento.getIuvPagamento();
		iuvPagamentoValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "CodiceIUV", iuvPagamentoValue, 35);
		linea.add(iuvPagamentoValue);
		
//		DataScadenza $.dataScadenza
		linea.add(pagamento.getDataScadenza() != null ? SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(pagamento.getDataScadenza()) : "");
		
//		DataPagamento	rendicontazione.data
		linea.add(pagamento.getDataPagamento() != null ? SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(pagamento.getDataPagamento()) : "");
		
//		ImportoPagato rendicontazione.importo
		linea.add(pagamento.getImportoPagato() != null ? TracciatiNotificaPagamentiUtils.printImporto(pagamento.getImportoPagato(), true) : "");
		
//		IstitutoMittente 120 fr.ragioneSocialePsp
		String istitutoMittenteValue = pagamento.getRagioneSocialePsp() != null ? pagamento.getRagioneSocialePsp() : "";
		istitutoMittenteValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "IstitutoMittente", istitutoMittenteValue, 120);
		linea.add(istitutoMittenteValue);
		
//		ModalitaPagamento 100 VUOTO
		linea.add("");
		
//		IBANIncasso 27 VUOTO
		linea.add("");
		
//		CodiceFlussoRiversamento 60 fr.codFlusso
		String codiceFlussoRiversamentoValue = pagamento.getCodFlusso() != null ? pagamento.getCodFlusso() : "";
		codiceFlussoRiversamentoValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, "CodiceFlussoRiversamento", codiceFlussoRiversamentoValue, 60);
		linea.add(codiceFlussoRiversamentoValue);
		
//		DataRiversamento fr.dataRegolamento
		linea.add(pagamento.getDataRegolamento() != null ? SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(pagamento.getDataRegolamento()) : "");
		
//		Annotazioni 255 VUOTO
		linea.add("");
		
		// IF sv.contabilita = null, tutto a null.
		if(contabilita == null || contabilita.getQuote() == null) {
			linea.addAll(TracciatiNotificaPagamentiUtils.aggiungiCampiVuoti(30));
		} else {  
			// conto le quote disponibili
			List<QuotaContabilita> quote = contabilita.getQuote();
			
			int numeroQuote = Math.min(quote.size(), 10);
			
			for (int i = 0; i < numeroQuote; i++) {
				QuotaContabilita quotaContabilita = quote.get(i);
				
//				LivelloContabile1 3 Se sv.contabilita.quote[0].accertamento = null THEN LivelloContabile1 = CAP ELSE LivelloContabile1 = ACC
//				CodificaContabile1 35 IF LivelloContabile1 = CAP THEN CodificaContabile1 = {sv.contabilita.quote[0].annoEsercizio}/{sv.contabilita.quote[0].capitolo} ELSE CodificaContabile1 = {sv.contabilita.quote[0].annoEsercizio}/{sv.contabilita.quote[0].accertamento}	

				if(quotaContabilita.getAccertamento() == null) {
					linea.add(QUOTA_CONTABILITA_CAPITOLO);
					String codificaContabileValue = quotaContabilita.getAnnoEsercizio() + "/" + quotaContabilita.getCapitolo();
					codificaContabileValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, ("CodificaContabile" + (i+1)), codificaContabileValue, 35);
					linea.add(codificaContabileValue);
				} else {
					linea.add(QUOTA_CONTABILITA_ACCERTAMENTO);
					String codificaContabileValue = quotaContabilita.getAnnoEsercizio() + "/" + quotaContabilita.getAccertamento();
					codificaContabileValue = TracciatiNotificaPagamentiUtils.impostaLunghezzaMassimaCampo(log, entryKey, ("CodificaContabile" + (i+1)), codificaContabileValue, 35);
					linea.add(codificaContabileValue);
				}
//				QuotaContabile1	sv.contabilita.quote[0].importo in centesimi	
				linea.add(TracciatiNotificaPagamentiUtils.printImporto(quotaContabilita.getImporto(), true));
			}
			
			if(numeroQuote < 10) { // aggiungo campi vuoti per arrivare alla fine del record
				linea.addAll(TracciatiNotificaPagamentiUtils.aggiungiCampiVuoti(((10 -numeroQuote) *3) ));
			}
		}
		
		return linea.toArray(new String[linea.size()]);
	}
	
	private void popolaTracciatoMaggioliJPPA(IContext ctx, ConnettoreNotificaPagamenti connettore, BDConfigWrapper configWrapper, Dominio dominio,
			it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, Date dataRtDa, Date dataRtA,
			RptBD rptBD, List<String> listaTipiPendenza, long progressivo, ISerializer serializer, ZipOutputStream zos)
			throws Exception { 
		String codDominio = dominio.getCodDominio();
		
		log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"] in corso...");
		
		// Entry 1. file di esito invio Notifiche di pagamento
		
		ZipEntry tracciatoOutputEntry = new ZipEntry("GOVPAY_" + codDominio + "_"+progressivo+".csv");
		zos.putNextEntry(tracciatoOutputEntry);
		
		CSVUtils csvUtils = CSVUtils.getInstance(CSVFormat.DEFAULT);
		
		zos.write(csvUtils.toCsv(MAGGIOLI_JPPA_HEADER_FILE_CSV).getBytes());
		
		int lineaElaborazione = 0;
		int offset = 0;
		int limit = 100; 
		List<Rpt> rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		log.trace("Invio notifiche pagamento, trovate ["+rtList.size()+"] RT da notificare a Maggioli...");
		int totaleRt = 0;
		do {
			if(rtList.size() > 0) {
				List<InviaNotificaPagamentoMaggioliJPPAThread> threadsSpedizioni = new ArrayList<InviaNotificaPagamentoMaggioliJPPAThread>();
				
				for (Rpt rpt : rtList) {
					
					InviaNotificaPagamentoMaggioliJPPAThread sender = new InviaNotificaPagamentoMaggioliJPPAThread(rpt, dominio, ("ThreadSpedizione_" + (threadsSpedizioni.size() + 1)), ctx);
					ThreadExecutorManager.getExecutorSpedizioneNotificaPagamentoMaggioli().execute(sender);
					threadsSpedizioni.add(sender);
				}
				
				while(true){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {

					}
					boolean completed = true;
					for(InviaNotificaPagamentoMaggioliJPPAThread sender : threadsSpedizioni) {
						if(!sender.isCompleted()) {
							completed = false;
						} else {
							if(!sender.isCommit()) {
								sender.setCommit(true);
								synchronized (this) {
									if(sender.getException() != null) {
										log.error("Si e' verificato un errore durante l'invio della notifica per il " + sender.getNomeThread() + ": "+sender.getException().getMessage());
										throw sender.getException();
									}

									log.debug(sender.getNomeThread() + " ha eseguito l'invio correttamente");
									zos.write(csvUtils.toCsv(this.inviaNotificaMaggioliJppa(sender.getRpt(), sender.getEsito(), sender.getDescrizioneEsito())).getBytes());
								}
							}
						}
					}

					if(completed) { 
						log.debug("Completata Esecuzione dei ["+threadsSpedizioni.size()+"] Threads di spedizione");
						break; // esco
					}
				}
				
				lineaElaborazione += threadsSpedizioni.size();
				totaleRt += threadsSpedizioni.size();
				beanDati.setLineaElaborazione(lineaElaborazione);
				log.trace("Completato invio ["+rtList.size()+"] RT a Maggioli e inserimento esiti nel file csv");
			}

			offset += limit;
			rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
			log.trace("Invio notifiche pagamento, trovate ["+rtList.size()+"] RT da notificare a Maggioli...");
		}while(rtList.size() > 0);
		
		beanDati.setNumRtTotali(totaleRt);
		
		log.debug("Completato invio ["+totaleRt+"] RT a Maggioli e completata generazione del file csv di esito.");

		// chiusa entry
		zos.flush();
		zos.closeEntry();
		
		log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"] completato.");
	}

	private String[] inviaNotificaMaggioliJppa(Rpt rpt, String esito, String descrizioneEsito) {

		List<String> linea = new ArrayList<String>();
		
		// coddominio
		linea.add(rpt.getCodDominio());
		// iuv
		linea.add(rpt.getIuv());
		// ccp
		linea.add(rpt.getCcp());
		// esito invio notifica		
		linea.add(esito);
		// descrizione esito
		linea.add(descrizioneEsito != null ? descrizioneEsito : ""); 
		
		return linea.toArray(new String[linea.size()]);
	}
}
