package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtDatiSingoliPagamenti;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtIstitutoMittente;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtIstitutoRicevente;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.FlussoRiversamento;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.DocumentiBD;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiNotificaPagamentiBD;
import it.govpay.bd.viste.EntratePrevisteBD;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.tracciati.TracciatiNotificaPagamentiUtils;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.Contabilita;
import it.govpay.model.QuotaContabilita;
import it.govpay.model.TipoVersamento;
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
			log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], verranno ricercate RT da inserire in un nuovo tracciato da ["
					+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtDa)+"] a ["+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtA)+"]");
			entriesDaInserireNelTracciato = rptBD.countRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza);
			log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+ codDominio +"], trovate ["+ entriesDaInserireNelTracciato +"] RT da inserire in un nuovo tracciato");
			break;
		case HYPERSIC_APK:
			log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], verranno ricercate Riscossioni da inserire in un nuovo tracciato da ["
					+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtDa)+"] a ["+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtA)+"]");
			PagamentiBD pagamentiBD = new PagamentiBD(rptBD);
			pagamentiBD.setAtomica(false);
			entriesDaInserireNelTracciato = pagamentiBD.countRiscossioniDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza);
			log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+ codDominio +"], trovate ["+ entriesDaInserireNelTracciato +"] Riscossioni da inserire in un nuovo tracciato");
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
		
		CSVUtils csvUtils = CSVUtils.getInstance(CSVFormat.DEFAULT);
		
		String dataCreazioneFlusso = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(tracciato.getDataCreazione());
		String progressivoS = this.completaValoreCampoConFiller(progressivo +"", 3, true, true);
		
		ZipEntry tracciatoOutputEntry = new ZipEntry("RENDICONTAZIONE_AV20_" + codDominio + "_"+ dataCreazioneFlusso + "_" + progressivoS+ ".txt");
		zos.putNextEntry(tracciatoOutputEntry);
		
		
		
		int lineaElaborazione = 0;
		int offset = 0;
		int limit = 100; 
		EntratePrevisteBD entratePrevisteBD = new EntratePrevisteBD(rptBD);
		entratePrevisteBD.setAtomica(false);
		
		List<EntrataPrevista> riscossioniList = entratePrevisteBD.ricercaRiscossioniDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
		log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+riscossioniList.size()+"] Riscossioni da inserire nel tracciato");
		int totaleRt = riscossioniList.size();
		
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			
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
				riscossioniList = entratePrevisteBD.ricercaRiscossioniDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+riscossioniList.size()+"] Riscossioni da inserire nel tracciato");
				totaleRt += riscossioniList.size();
			}while(riscossioniList.size() > 0);
			
			log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserite ["+totaleRt+"] Riscossioni nel tracciato");
	
			// chiusa entry
			zos.flush();
			zos.closeEntry(); 
			
			/*
			 Le riscossioni non rendicontate per piu' di 5 gg vengono incluse in un csv separato avente la medesima sintassi, ma con i campi relativi al flusso di rendicontazione vuoti e con progressivo flusso = 999.
			 */
			
			
			if(baos.size() > 0) {
				
				ZipEntry tracciatoNoSecimOutputEntry = new ZipEntry("RENDICONTAZIONE_AV20_" + codDominio + "_"+ dataCreazioneFlusso + "_" + "999"+ ".txt");
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
			linea.add(this.printImporto(importoTotalePagamenti, false));
			// identificativoUnivocoVersamento: fr.ctDatiSingoliPagamenti[i].identificativoUnivocoVersamento
			linea.add(identificativoUnivocoVersamento);
			// identificativoUnivocoRiscossione: fr.ctDatiSingoliPagamenti[i].identificativoUnivocoRiscossione
			linea.add(identificativoUnivocoRiscossione);
			// indiceDatiSingoloPagamento: fr.ctDatiSingoliPagamenti[i].indiceDatiSingoloPagamento
			linea.add(indiceDatiSingoloPagamento != null ? indiceDatiSingoloPagamento.intValue() + "" : "");
			// singoloImportoPagato: fr.ctDatiSingoliPagamenti[i].singoloImportoPagato
			linea.add(this.printImporto(singoloImportoPagato, false));
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
		List<List<String>> linee = new ArrayList<List<String>>();
		

		Versamento versamento = rpt.getVersamento();
		Applicazione applicazione = versamento.getApplicazione(configWrapper);
		TipoVersamento tipoVersamento = versamento.getTipoVersamento(configWrapper);
		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();
		String datiAllegati = versamento.getDatiAllegati();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti();
		Documento documento = versamento.getDocumento(configWrapper);
		String causaleVersamento = null;
		try {
			causaleVersamento = versamento.getCausaleVersamento().getSimple();
		} catch (UnsupportedEncodingException e) {
			causaleVersamento = "";
		}
		
		for(int indiceDati = 0; indiceDati < datiPagamento.getDatiSingoloPagamento().size(); indiceDati ++) {
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiPagamento.getDatiSingoloPagamento().get(indiceDati);
			
			SingoloVersamento singoloVersamento = singoliVersamenti.get(indiceDati);
			String datiAllegatiSV = singoloVersamento.getDatiAllegati();
			
			List<String> linea = new ArrayList<String>();
			
//			idA2A: da pendenza
			linea.add(applicazione.getCodApplicazione());
//			idPendenza: da pendenza
			linea.add(versamento.getCodVersamentoEnte());
//			idDocumento: da pendenza
			String codDocumento = documento != null ? documento.getCodDocumento() : "";
			linea.add(codDocumento);
			// descrizioneDocumento: da pendenza
			String descrizioneDocumento = documento != null ? documento.getDescrizione() : "";
			linea.add(descrizioneDocumento);
//			codiceRata: da pendenza
			linea.add(versamento.getNumeroRata() != null ? versamento.getNumeroRata() + "" : "");
//			dataScadenza: da pendenza
			String dataScadenzaS = versamento.getDataScadenza() != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(versamento.getDataScadenza()): "";
			linea.add(dataScadenzaS);
//			idVocePendenza: da pendenza
			linea.add(singoloVersamento.getCodSingoloVersamentoEnte());
			// descrizioneVocePendenza
			linea.add(singoloVersamento.getDescrizione());
//			idTipoPendenza: da pendenza
			linea.add(tipoVersamento.getCodTipoVersamento());
			// descrizione  pendenza.causale
			linea.add(causaleVersamento);
//			anno: da pendenza
			linea.add(versamento.getCodAnnoTributario() != null ? versamento.getCodAnnoTributario() + "" : "");
//			identificativoDebitore: da RT rt.datiPagamento.soggettoPagatore.identificativoUnivocoPagatore.codiceIdentificativoUnivoco
			linea.add(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
//			anagraficaDebitore: da RT rt.datiPagamento.soggettoPagatore.anagraficaPagatore
			linea.add(soggettoPagatore.getAnagraficaPagatore());
//			identificativoDominio: da RT
			linea.add(datiPagamento.getIdentificativoUnivocoVersamento());
//			identificativoUnivocoVersamento: da RT rt.datiPagamento.identificativoUnivocoVersamento
			linea.add(datiPagamento.getIdentificativoUnivocoVersamento());
//			codiceContestoPagamento:da RT
			linea.add(datiPagamento.getCodiceContestoPagamento());
//			indiceDati: da RT
			linea.add((indiceDati + 1) + "");
//			identificativoUnivocoRiscossione: da RT
			linea.add(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
			// modelloPagamento da RT
			linea.add(rpt.getModelloPagamento().getCodifica()+"");
//			singoloImportoPagato: da RT
			linea.add(this.printImporto(ctDatiSingoloPagamentoRT.getSingoloImportoPagato(), false));
//			dataEsitoSingoloPagamento: da RTrt.datiPagamento.datiSingoloPagamento[0].dataEsitoSingoloPagamento [YYYY]-[MM]-[DD]
			linea.add(SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento()));
//			causaleVersamento: da RT rt.datiPagamento.datiSingoloPagamento[0].causaleVersamento
			linea.add(ctDatiSingoloPagamentoRT.getCausaleVersamento()); 
//			datiSpecificiRiscossione: da RT rt.datiPagamento.datiSingoloPagamento[0].datiSpecificiRiscossione
			linea.add(ctDatiSingoloPagamentoRT.getDatiSpecificiRiscossione());
//			datiAllegati: da Pendenza
			linea.add(datiAllegati != null ? datiAllegati : "");
//			datiAllegatiVoce: da vocePendenza
			linea.add(datiAllegatiSV != null ? datiAllegatiSV : "");
//			denominazioneAttestante: da RT
			linea.add(istitutoAttestante.getDenominazioneAttestante());
//			identificativoAttestante: da RT
			linea.add(istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
			// contabilita
			if(singoloVersamento.getContabilita() != null && singoloVersamento.getContabilita().length() > 0) {
				linea.add(singoloVersamento.getContabilita());
			} else {
				linea.add("");
			}
			
			linee.add(linea);
		}
		

		return linee;
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

	@SuppressWarnings("unchecked")
	private String [] creaLineaCsvMyPivot(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException, ValidationException { 
		List<String> linea = new ArrayList<String>();

		Versamento versamento = rpt.getVersamento();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		SingoloVersamento singoloVersamento = singoliVersamenti.get(0);
		Applicazione applicazione = versamento.getApplicazione(configWrapper);
		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiPagamento.getDatiSingoloPagamento().get(0);

		String contabilitaString = singoloVersamento.getContabilita();
		String tipoDovuto = null;
		String bilancio = null;
		if(contabilitaString != null && contabilitaString.length() > 0) {
			Contabilita contabilita = JSONSerializable.parse(contabilitaString, Contabilita.class);
			
			Object proprietaCustomObj = contabilita.getProprietaCustom();
			
			if(proprietaCustomObj != null) {
				if(proprietaCustomObj instanceof String) {
					String proprietaCustom = (String) proprietaCustomObj;
					
					if(proprietaCustom != null && proprietaCustom.length() > 0) {
						Map<String, Object> parse = JSONSerializable.parse(proprietaCustom, Map.class);
						// leggo proprieta tipoDovuto
						if(parse.containsKey("tipoDovuto")) {
							tipoDovuto = (String) parse.get("tipoDovuto");
						}
					}
				}  else if(proprietaCustomObj instanceof java.util.LinkedHashMap) {
					java.util.LinkedHashMap<?,?> parse = (LinkedHashMap<?,?>) proprietaCustomObj;
					
					// leggo proprieta tipoDovuto
					if(parse.containsKey("tipoDovuto")) {
						tipoDovuto = (String) parse.get("tipoDovuto");
					}
				}
			}
			
			// bilancio a partire dalle quote ricevute nell'oggetto contabilita'
			if(contabilita.getQuote() != null && contabilita.getQuote().size() > 0) {
				StringBuilder sb = new StringBuilder();
				
				sb.append("<bilancio>");
				for (QuotaContabilita quota : contabilita.getQuote()) {
					sb.append("<capitolo>");
					
					sb.append("<codice>");
					sb.append(quota.getCapitolo());
					sb.append("</codice>");
					
					sb.append("<importo>");
					sb.append(this.printImporto(quota.getImporto(), false));
					sb.append("</importo>");
					
					sb.append("</capitolo>");
				}
				sb.append("</bilancio>");
				
				bilancio = sb.toString();
			}
			
		}

		// IUD cod_applicazione@cod_versamento_ente
		linea.add(applicazione.getCodApplicazione() + "@" + versamento.getCodVersamentoEnte());
		// codIuv: rt.datiPagamento.identificativoUnivocoVersamento
		linea.add(datiPagamento.getIdentificativoUnivocoVersamento());
		// tipoIdentificativoUnivoco: rt.datiPagamento.soggettoPagatore.identificativoUnivocoPagatore.tipoIdentificativoUnivoco
		linea.add(soggettoPagatore.getIdentificativoUnivocoPagatore().getTipoIdentificativoUnivoco().value());
		// codiceIdentificativoUnivoco: rt.datiPagamento.soggettoPagatore.identificativoUnivocoPagatore.codiceIdentificativoUnivoco
		linea.add(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
		// anagraficaPagatore: rt.datiPagamento.soggettoPagatore.anagraficaPagatore
		linea.add(soggettoPagatore.getAnagraficaPagatore());
		// indirizzoPagatore: rt.datiPagamento.soggettoPagatore.indirizzoPagatore
		linea.add(soggettoPagatore.getIndirizzoPagatore());
		// civicoPagatore: rt.datiPagamento.soggettoPagatore.civicoPagatore
		linea.add(soggettoPagatore.getCivicoPagatore());
		// capPagatore: rt.datiPagamento.soggettoPagatore.capPagatore
		linea.add(soggettoPagatore.getCapPagatore());
		// localitaPagatore: rt.datiPagamento.soggettoPagatore.localitaPagatore
		linea.add(soggettoPagatore.getLocalitaPagatore());
		// provinciaPagatore: rt.datiPagamento.soggettoPagatore.provinciaPagatore
		linea.add(soggettoPagatore.getProvinciaPagatore());
		// nazionePagatore: rt.datiPagamento.soggettoPagatore.nazionePagatore
		linea.add(soggettoPagatore.getNazionePagatore());
		// e-mailPagatore: rt.datiPagamento.soggettoPagatore.e-mailPagatore
		linea.add(soggettoPagatore.getEMailPagatore());
		// dataEsecuzionePagamento: rt.datiPagamento.datiSingoloPagamento[0].dataEsitoSingoloPagamento [YYYY]-[MM]-[DD]
		linea.add(SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento()));
		// importoDovutoPagato: rt.datiPagamento.importoTotalePagato
		linea.add(this.printImporto(datiPagamento.getImportoTotalePagato(), false));
		// commissioneCaricoPa: vuoto
		linea.add("");
		// tipoDovuto: versamento.datiAllegati.mypivot.tipoDovuto o versamento.tassonomiaEnte o versamento.codTipoPendenza
		if(tipoDovuto == null) {
			tipoDovuto = StringUtils.isNotBlank(versamento.getTassonomiaAvviso()) 
					? versamento.getTassonomiaAvviso() : versamento.getTipoVersamento(configWrapper).getCodTipoVersamento();
		}
		linea.add(tipoDovuto);
		// tipoVersamento: vuoto
		linea.add("");
		// causaleVersamento: rt.datiPagamento.datiSingoloPagamento[0].causaleVersamento
		linea.add(ctDatiSingoloPagamentoRT.getCausaleVersamento());
		// datiSpecificiRiscossione: rt.datiPagamento.datiSingoloPagamento[0].datiSpecificiRiscossione
		linea.add(ctDatiSingoloPagamentoRT.getDatiSpecificiRiscossione());
		// bilancio: versamento.datiAllegati.mypivot.bilancio o vuoto
		linea.add(bilancio != null ? bilancio : "");

		return linea.toArray(new String[linea.size()]);
	}
	
	@SuppressWarnings("unchecked")
	private void creaLineaCsvSecim(Rpt rpt, BDConfigWrapper configWrapper, int numeroLinea, ConnettoreNotificaPagamenti connettore, OutputStream secimOS, OutputStream noSecimOS) throws ServiceException, JAXBException, SAXException, ValidationException, java.io.IOException { 
		StringBuilder sb = new StringBuilder();
		
		
		// NUOVA LINEA 
		if(numeroLinea > 1)
			sb.append("\n");

		Versamento versamento = rpt.getVersamento();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		SingoloVersamento singoloVersamento = singoliVersamenti.get(0);
		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiPagamento.getDatiSingoloPagamento().get(0);
		CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();

		String contabilitaString = singoloVersamento.getContabilita();
		String riferimentoCreditore = null;
		String tipoflusso = null;
		String tipoRiferimentoCreditore = null;
		if(contabilitaString != null && contabilitaString.length() > 0) {
			Contabilita contabilita = JSONSerializable.parse(contabilitaString, Contabilita.class);
			
			Object proprietaCustomObj = contabilita.getProprietaCustom();
			
			if(proprietaCustomObj != null) {
				if(proprietaCustomObj instanceof String) {
					String proprietaCustom = (String) proprietaCustomObj;
					if(proprietaCustom != null && proprietaCustom.length() > 0) {
						Map<String, Object> parse = JSONSerializable.parse(proprietaCustom, Map.class);
						// leggo proprieta
						if(parse.containsKey("riferimentoCreditore")) {
							riferimentoCreditore = (String) parse.get("riferimentoCreditore");
						}
						if(parse.containsKey("tipoflusso")) {
							tipoflusso = (String) parse.get("tipoflusso");
						}
						if(parse.containsKey("tipoRiferimentoCreditore")) {
							tipoRiferimentoCreditore = (String) parse.get("tipoRiferimentoCreditore");
						}
					}
				}  else if(proprietaCustomObj instanceof java.util.LinkedHashMap) {
					java.util.LinkedHashMap<?,?> parse = (LinkedHashMap<?,?>) proprietaCustomObj;
					
					// leggo proprieta
					if(parse.containsKey("riferimentoCreditore")) {
						riferimentoCreditore = (String) parse.get("riferimentoCreditore");
					}
					if(parse.containsKey("tipoflusso")) {
						tipoflusso = (String) parse.get("tipoflusso");
					}
					if(parse.containsKey("tipoRiferimentoCreditore")) {
						tipoRiferimentoCreditore = (String) parse.get("tipoRiferimentoCreditore");
					}
				}
			}
		}
		
//		CODICE ISTITUTO	1	5	5	Numerico	5	0	SI	Codice in rt.istitutoAttestante.identificativoUnivocoAttestante.codiceIdentificativoUnivoco se rt.istitutoAttestante.identificativoUnivocoAttestante.tipoIdentificativoUnivoco == ‘A’
		String codiceIstituto = istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco();
		StTipoIdentificativoUnivoco tipoIdentificativoUnivocoATtestante = istitutoAttestante.getIdentificativoUnivocoAttestante().getTipoIdentificativoUnivoco();
		if(connettore.getCodiceIstituto() != null) {
			codiceIstituto = connettore.getCodiceIstituto();
		} else {
			switch (tipoIdentificativoUnivocoATtestante) {
			case A:
				codiceIstituto = istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco();
				break;
			case B:
			case G:
				codiceIstituto = "00000";
				break;
			}
		}
		
		this.validaCampo("CODICE ISTITUTO", codiceIstituto, 5);
		sb.append(codiceIstituto);
		
//		CODICE CLIENTE	6	12	7	Numerico	7	0	SI	Codice Ente dal portale Ente Creditore
		String codiceCliente = connettore.getCodiceCliente(); 
		this.validaCampo("CODICE CLIENTE", codiceCliente, 7);
		sb.append(codiceCliente);
		
//		FILLER	13	22	10	
		String filler = this.completaValoreCampoConFiller("", 10, false, true);
		this.validaCampo("FILLER 1", filler, 10);
		sb.append(filler);
		
//		TIPO FLUSSO	23	30	8	Carattere			SI	Dato di configurazione assegnato da Poste alla PA datiallegati.tipoflusso
		if(tipoflusso == null) {
			tipoflusso = "NDP001C0";
		}
		tipoflusso = this.completaValoreCampoConFiller(tipoflusso, 8, false, false);
		this.validaCampo("TIPO FLUSSO", tipoflusso, 8);
		sb.append(tipoflusso);
		
//		DATA CREAZIONE FLUSSO	31	38	8	Numerico	8	0	SI	Data Creazione di questo flusso?
		Date dataCreazioneFlusso = new Date();
		String dataCreazione = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(dataCreazioneFlusso);
		this.validaCampo("DATA CREAZIONE FLUSSO", dataCreazione, 8);
		sb.append(dataCreazione);
		
//		FILLER	39	77	39	
		filler = this.completaValoreCampoConFiller("", 39, false, true);
		this.validaCampo("FILLER 2", filler, 39);
		sb.append(filler);
		
//		PROGRESSIVO RECORD	78	90	13	Numerico	13	0		Numero di record incrementale
		String progressivoRecord = "" + numeroLinea;
		progressivoRecord = this.completaValoreCampoConFiller(progressivoRecord, 13, true, true);
		this.validaCampo("PROGRESSIVO RECORD", progressivoRecord, 13);
		sb.append(progressivoRecord);
		
//		OPERAZIONE	91	93	3	Carattere			SI	nel pdf si parla di RIV nell’esempio c’e’ RIS
		String operazione = "RIS";
		this.validaCampo("OPERAZIONE", operazione, 3);
		sb.append(operazione);
		
//		FILLER	94	163	70	Carattere	
		filler = this.completaValoreCampoConFiller("", 70, false, true);
		this.validaCampo("FILLER 3", filler, 70);
		sb.append(filler);
		
//		TIPO PRESENTAZIONE	164	169	6	Carattere	
		String tipoPresentazione = "BOL_PA";
		this.validaCampo("TIPO PRESENTAZIONE", tipoPresentazione, 6);
		sb.append(tipoPresentazione);
		
//		CODICE PRESENTAZIONE	170	187	18	Carattere				versamento.numero_avviso
		String codicePresentazione = versamento.getNumeroAvviso() != null ? versamento.getNumeroAvviso() : "";
		codicePresentazione = this.completaValoreCampoConFiller(codicePresentazione, 18, false, false);
		this.validaCampo("CODICE PRESENTAZIONE", codicePresentazione, 18);
		sb.append(codicePresentazione);
		
//		FILLER	188	204	17	Carattere	
		filler = this.completaValoreCampoConFiller("", 17, false, true);
		this.validaCampo("FILLER 4", filler, 17);
		sb.append(filler);
		
//		IUV	205	239	35	Carattere				versamento.iuv
		String iuvVersamento = datiPagamento.getIdentificativoUnivocoVersamento();
		iuvVersamento = this.completaValoreCampoConFiller(iuvVersamento, 35, false, false);
		this.validaCampo("IUV", iuvVersamento, 35);
		sb.append(iuvVersamento);
		
//		RATA	240	274	35	Carattere				versamento.cod_rata
		String prefixRata = versamento.getNumeroRata() != null ? "S" : "T";
		Integer numeroRata = versamento.getNumeroRata() != null ? versamento.getNumeroRata() : 1;
		String rata = this.completaValoreCampoConFiller(numeroRata+"", 8, true, true); // Aggiungo zeri a sx fino ad arrivare a 8 caratteri
		rata = prefixRata + rata; // aggiungo prefisso
		rata = this.completaValoreCampoConFiller(rata, 35, false, false); // completo con spazi bianchi a dx fino a 35 caratteri
		this.validaCampo("RATA", rata, 35);
		sb.append(rata);
		
//		FILLER	275	344	70	
		filler = this.completaValoreCampoConFiller("", 70, false, true);
		this.validaCampo("FILLER 5", filler, 70);
		sb.append(filler);
		
//		RIFERIMENTO CREDITORE	345	379	35	Carattere		SECIM +	$pendenza.{datiAllegati}.secim.riferimentoCreditore o, in sua assenza, il campo $pendenza.voce[0].idVoce
		// il prefisso SECIM viene valorizzato cosi se il campo $pendenza.{datiAllegati}.secim.tipoRiferimentoCreditore e' vuoto, altrimenti ci viene messo il valore ricevuto
		if(riferimentoCreditore == null) {
			riferimentoCreditore = singoloVersamento.getCodSingoloVersamentoEnte();
		}
		riferimentoCreditore = (tipoRiferimentoCreditore == null) ? ("SECIM" + riferimentoCreditore) : (tipoRiferimentoCreditore + riferimentoCreditore); 
		riferimentoCreditore = this.completaValoreCampoConFiller(riferimentoCreditore, 35, false, false);
		this.validaCampo("RIFERIMENTO CREDITORE", riferimentoCreditore, 35);
		sb.append(riferimentoCreditore);
		
//		FILLER	380	457	78	
		filler = this.completaValoreCampoConFiller("", 78, false, true);
		this.validaCampo("FILLER 6", filler, 78);
		sb.append(filler);
		
//		IMPORTO VERSAMENTO	458	472	15	Numerico	13	2	SI	singolo_versamento.importo_singolo_versamento o versamento.importo_totale
		String importoTotalePagato = this.printImporto(versamento.getImportoTotale(), true);
		importoTotalePagato = this.completaValoreCampoConFiller(importoTotalePagato, 15, true, true);
		this.validaCampo("IMPORTO VERSAMENTO", importoTotalePagato, 15);
		sb.append(importoTotalePagato);
		
//		FILLER	473	517	45	
		filler = this.completaValoreCampoConFiller("", 45, false, true);
		this.validaCampo("FILLER 7", filler, 45);
		sb.append(filler);
		
//		CAUSALE VERSAMENTO	518	657	140	Carattere			SI	singolo_versamento.descrizione_causale_RPT o versamento.causale
		String causaleVersamento = ctDatiSingoloPagamentoRT.getCausaleVersamento();
		causaleVersamento = this.completaValoreCampoConFiller(causaleVersamento, 140, false, false);
		this.validaCampo("CAUSALE VERSAMENTO", causaleVersamento, 140);
		sb.append(causaleVersamento);
		
//		FILLER	658	713	56	
		filler = this.completaValoreCampoConFiller("", 56, false, true);
		this.validaCampo("FILLER 8", filler, 56);
		sb.append(filler);
		
//		TIPO DEBITORE	714	716	3	Carattere				versamento.debitore_tipo
		StTipoIdentificativoUnivocoPersFG tipoIdentificativoUnivoco = soggettoPagatore.getIdentificativoUnivocoPagatore().getTipoIdentificativoUnivoco();
		String tipoDebitore = tipoIdentificativoUnivoco.equals(StTipoIdentificativoUnivocoPersFG.F) ? "F" : "G";
		tipoDebitore = this.completaValoreCampoConFiller(tipoDebitore, 3, false, false);
		this.validaCampo("TIPO DEBITORE", tipoDebitore, 3);
		sb.append(tipoDebitore);
		
//		TIPO CODICE DEBITORE	717	718	2	Carattere			SI	Tipologia del dato versamento.debitore_identificativo
		String tipoCodiceDebitore = tipoIdentificativoUnivoco.equals(StTipoIdentificativoUnivocoPersFG.F) ? "CF" : "PI";
		this.validaCampo("TIPO CODICE DEBITORE", tipoCodiceDebitore, 2);
		sb.append(tipoCodiceDebitore);
		
//		CODICE DEBITORE	719	753	35	Carattere			SI	versamento.debitore_identificativo
		String codiceDebitore = soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco();
		codiceDebitore = this.completaValoreCampoConFiller(codiceDebitore, 35, false, false);
		this.validaCampo("CODICE DEBITORE", codiceDebitore, 35);
		sb.append(codiceDebitore);
		
//		ANAGRAFICA DEBITORE	754	803	50	Carattere			SI	versamento.debitore_anagrafica
		String anagraficaDebitore = soggettoPagatore.getAnagraficaPagatore();
		anagraficaDebitore = this.completaValoreCampoConFiller(anagraficaDebitore, 50, false, false);
		this.validaCampo("ANAGRAFICA DEBITORE", anagraficaDebitore, 50);
		sb.append(anagraficaDebitore);
		
//		FILLER	804	838	35	
		filler = this.completaValoreCampoConFiller("", 35, false, true);
		this.validaCampo("FILLER 9", filler, 35);
		sb.append(filler);
		
//		INDIRIZZO DEBITORE	839	888	50	Carattere				versamento.debitore_indirizzo
		String indirizzoDebitore = soggettoPagatore.getIndirizzoPagatore();
		indirizzoDebitore = this.completaValoreCampoConFiller(indirizzoDebitore, 50, false, false);
		this.validaCampo("INDIRIZZO DEBITORE", indirizzoDebitore, 50);
		sb.append(indirizzoDebitore);
		
//		NUMERO CIVICO DEBITORE	889	893	5	Carattere				versamento.debitore_civico
		String numeroCivicoDebitore = soggettoPagatore.getCivicoPagatore();
		numeroCivicoDebitore = this.completaValoreCampoConFiller(numeroCivicoDebitore, 5, false, false);
		this.validaCampo("NUMERO CIVICO DEBITORE", numeroCivicoDebitore, 5);
		sb.append(numeroCivicoDebitore);
		
//		CAP DEBITORE	894	898	5	Carattere				versamento.debitore_cap
		String capDebitore = soggettoPagatore.getCapPagatore();
		capDebitore = this.completaValoreCampoConFiller(capDebitore, 5, false, false);
		this.validaCampo("CAP DEBITORE", capDebitore, 5);
		sb.append(capDebitore);
		
//		LOCALITA DEBITORE	899	948	50	Carattere				versamento.debitore_localita
		String localitaDebitore = soggettoPagatore.getLocalitaPagatore();
		localitaDebitore = this.completaValoreCampoConFiller(localitaDebitore, 50, false, false);
		this.validaCampo("LOCALITA DEBITORE", localitaDebitore, 50);
		sb.append(localitaDebitore);
		
//		PROVINCIA DEBITORE	949	950	2	Carattere				versamento.debitore_provincia
		String provinciaDebitore = soggettoPagatore.getProvinciaPagatore();
		provinciaDebitore = this.completaValoreCampoConFiller(provinciaDebitore, 2, false, false);
		this.validaCampo("PROVINCIA DEBITORE", provinciaDebitore, 2);
		sb.append(provinciaDebitore);
		
//		STATO DEBITORE	951	985	35	Carattere				versamento.debitore_nazione
		String nazioneDebitore = soggettoPagatore.getNazionePagatore();
		nazioneDebitore = this.completaValoreCampoConFiller(nazioneDebitore, 35, false, false);
		this.validaCampo("STATO DEBITORE", nazioneDebitore, 35);
		sb.append(nazioneDebitore);
		
//		FILLER	986	1055	70	
		filler = this.completaValoreCampoConFiller("", 70, false, true);
		this.validaCampo("FILLER 10", filler, 70);
		sb.append(filler);
		
//		DATA PAGAMENTO	1056	1063	8	Numerico	8	0		versamento.data_pagamento
		String dataPagamento = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
		this.validaCampo("DATA PAGAMENTO", dataPagamento, 8);
		sb.append(dataPagamento);
		
//		DATA INCASSO	1064	1071	8	Numerico	8	0		fr.data_ora_flusso se disponibile
		String dataIncasso = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
		dataIncasso = this.completaValoreCampoConFiller(dataIncasso, 8, true, true);
		this.validaCampo("DATA INCASSO", dataIncasso, 8);
		sb.append(dataIncasso);
		
//		ESERCIZIO DI RIFERIMENTO	1072	1075	4	Numerico	4	0		??? nel file di esempio vale sempre 0000
		String esercizioRiferimento = "";
		esercizioRiferimento = this.completaValoreCampoConFiller(esercizioRiferimento, 4, true, true);
		this.validaCampo("ESERCIZIO DI RIFERIMENTO", esercizioRiferimento, 4);
		sb.append(esercizioRiferimento);
		
//		NUMERO PROVVISORIO	1076	1082	7	Numerico	7	0		??? nel file di esempio vale sempre 0000000
		String numeroProvvisorio = "";
		numeroProvvisorio = this.completaValoreCampoConFiller(numeroProvvisorio, 7, true, true);
		this.validaCampo("NUMERO PROVVISORIO", numeroProvvisorio, 7);
		sb.append(numeroProvvisorio);
		
//		CODICE RETE INCASSO	1083	1085	3	Carattere				NDP nei casi normali, PST se non si ha la RT ma il pagamento e’ stato solamente rendicontato da un flusso con codice esito = 9
		String codiceReteIncasso = "NDP";
		codiceReteIncasso = this.completaValoreCampoConFiller(codiceReteIncasso, 3, false, true);
		this.validaCampo("CODICE RETE INCASSO", codiceReteIncasso, 3);
		sb.append(codiceReteIncasso);
		
//		CODICE CANALE INCASSO	1086	1088	3	Carattere				Dal PSP che ha e’ stato utilizzato
		String codiceCanaleIncasso = "";
		codiceCanaleIncasso = this.completaValoreCampoConFiller(codiceCanaleIncasso, 3, false, true);
		this.validaCampo("CODICE CANALE INCASSO", codiceCanaleIncasso, 3);
		sb.append(codiceCanaleIncasso);
		
//		CODICE STRUMENTO INCASSO	1089	1091	3	Carattere				NDP se il campo precedente e’ di tipo PSP, altrimenti bisogna chiedere il codice bollettino
		String codiceStrumentoIncasso = "NDP";
		codiceStrumentoIncasso = this.completaValoreCampoConFiller(codiceStrumentoIncasso, 3, false, true);
		this.validaCampo("CODICE STRUMENTO INCASSO", codiceStrumentoIncasso, 3);
		sb.append(codiceStrumentoIncasso);

//		NUMERO BOLLETTA	1092	1104	13	Numerico	13	0		fr.trn se disponibile
		String numeroBolletta = "";
		numeroBolletta = this.completaValoreCampoConFiller(numeroBolletta, 13, true, true);
		this.validaCampo("NUMERO BOLLETTA", numeroBolletta, 13);
		sb.append(numeroBolletta);
		
//		IMPORTO PAGATO	1105	1119	15	Numerico	13	2		versamento.importo_pagato o rendicontazione.importo_pagato
		String importoPagato = this.printImporto(datiPagamento.getImportoTotalePagato(), true);
		importoPagato = this.completaValoreCampoConFiller(importoPagato, 15, true, true);
		this.validaCampo("IMPORTO PAGATO", importoPagato, 15);
		sb.append(importoPagato);
		
//		FILLER	1120	1172	53		
		filler = this.completaValoreCampoConFiller("", 53, false, true);
		this.validaCampo("FILLER 11", filler, 53);
		sb.append(filler);
		
//		IMPORTO COMMISSIONE PA	1173	1187	15	Numerico	13	2		0
		String importoCommissionePA = "";
		importoCommissionePA = this.completaValoreCampoConFiller(importoCommissionePA, 15, true, true);
		this.validaCampo("IMPORTO COMMISSIONE PA", importoCommissionePA, 15);
		sb.append(importoCommissionePA);
		
//		IMPORTO COMMISSIONE DEBITORE	1188	1202	15	Numerico	13	2		Da RT? rt.datiPagamento.datiSingoloPagamento[i].commissioniApplicatePSP
		BigDecimal commissioniApplicatePSP = ctDatiSingoloPagamentoRT.getCommissioniApplicatePSP() != null ? ctDatiSingoloPagamentoRT.getCommissioniApplicatePSP() : BigDecimal.ZERO;
		String importoCommissioniDebitore = this.printImporto(commissioniApplicatePSP, true);
		importoCommissioniDebitore = this.completaValoreCampoConFiller(importoCommissioniDebitore, 15, true, true);
		this.validaCampo("IMPORTO COMMISSIONE DEBITORE", importoCommissioniDebitore, 15);
		sb.append(importoCommissioniDebitore);
		
//		FILLER	1203	1589	387	
		filler = this.completaValoreCampoConFiller("", 387, false, true);
		this.validaCampo("FILLER 12", filler, 387);
		sb.append(filler);
		
//		CCP	1590	1601	12	Numerico				numero conto corrente postale?
		String ccp = "";
		ccp = this.completaValoreCampoConFiller(ccp, 12, true, true);
		this.validaCampo("CCP", ccp, 12);
		sb.append(ccp);
		
//		FILLER	1602	2000	399	
		filler = this.completaValoreCampoConFiller("", 399, false, true);
		this.validaCampo("FILLER 13", filler, 399);
		sb.append(filler);
		
		if(tipoRiferimentoCreditore == null) {
			secimOS.write(sb.toString().getBytes());
		} else {
			noSecimOS.write(sb.toString().getBytes());
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
		
//		CodiceServizio	$.vocePendenza.contabilita.quoteContabili.proprietaCustom.codiceServizio o versamento.tipoPendenza.codTipoPendenza	
		linea.add(codiceServizio != null ? codiceServizio : pagamento.getCodTipoVersamento());

//		DescrizioneServizio $.vocePendenza.contabilita.quoteContabili.proprietaCustom.descrizioneServizio o versamento.tipoPendenza.descrizione		
		linea.add(descrizioneServizio != null ? descrizioneServizio : (pagamento.getDescrizioneTipoVersamento() != null ? pagamento.getDescrizioneTipoVersamento() : ""));
			
//		CodiceDebitore VUOTO
		linea.add("");
			
//		CFPIVADebitore $.soggettoPagatore.identificativo
		linea.add(pagamento.getIdentificativoDebitore());
		
//		NominativoDebitore $.soggettoPagatore.anagrafica
		linea.add(pagamento.getAnagraficaDebitore());
		
//		CodiceDebito $.documento.identificativo o $.idPendenza
		linea.add(documento != null ? documento.getCodDocumento() : pagamento.getCodVersamentoEnte());
		
//		DataEmissione $.dataCaricamento 
		linea.add(SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(pagamento.getDataCreazione()));
		
//		CausaleDebito $.causale
		linea.add(pagamento.getCausaleVersamento().getSimple());
		
//		ImportoDebito importo (in centesimi)
		linea.add(this.printImporto(pagamento.getImportoVersamento(), true));
		
//		CodiceRata $.documento.numeroRata
		linea.add(pagamento.getNumeroRata() != null ? pagamento.getNumeroRata() +"" : "");
		
//		CodiceAvviso $.numeroAvviso
		linea.add(pagamento.getNumeroAvviso());
		
//		CodiceIUV $.iuvPagamento
		linea.add(pagamento.getIuvPagamento());
		
//		DataScadenza $.dataScadenza
		linea.add(pagamento.getDataScadenza() != null ? SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(pagamento.getDataScadenza()) : "");
		
//		DataPagamento	rendicontazione.data
		linea.add(pagamento.getDataPagamento() != null ? SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(pagamento.getDataPagamento()) : "");
		
//		ImportoPagato rendicontazione.importo
		linea.add(pagamento.getImportoPagato() != null ? this.printImporto(pagamento.getImportoPagato(), true) : "");
		
//		IstitutoMittente fr.ragioneSocialePsp
		linea.add(pagamento.getRagioneSocialePsp() != null ? pagamento.getRagioneSocialePsp() : "");
		
//		ModalitaPagamento VUOTO
		linea.add("");
		
//		IBANIncasso VUOTO
		linea.add("");
		
//		CodiceFlussoRiversamento fr.codFlusso
		linea.add(pagamento.getCodFlusso() != null ? pagamento.getCodFlusso() : "");
		
//		DataRiversamento fr.dataRegolamento
		linea.add(pagamento.getDataRegolamento() != null ? SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(pagamento.getDataRegolamento()) : "");
		
//		Annotazioni VUOTO
		linea.add("");
		
		// IF sv.contabilita = null, tutto a null.
		if(contabilita == null) {
			linea.addAll(this.aggiungiCampiVuoti(30));
		} else {  
			// conto le quote disponibili
			List<QuotaContabilita> quote = contabilita.getQuote();
			
			int numeroQuote = Math.min(quote.size(), 10);
			
			for (int i = 0; i < numeroQuote; i++) {
				QuotaContabilita quotaContabilita = quote.get(i);
				
//				LivelloContabile1 Se sv.contabilita.quote[0].accertamento = null THEN LivelloContabile1 = CAP ELSE LivelloContabile1 = ACC
//				CodificaContabile1 IF LivelloContabile1 = CAP THEN CodificaContabile1 = {sv.contabilita.quote[0].annoEsercizio}/{sv.contabilita.quote[0].capitolo} ELSE CodificaContabile1 = {sv.contabilita.quote[0].annoEsercizio}/{sv.contabilita.quote[0].accertamento}	

				if(quotaContabilita.getAccertamento() == null) {
					linea.add(QUOTA_CONTABILITA_CAPITOLO);
					linea.add(quotaContabilita.getAnnoEsercizio() + "/" + quotaContabilita.getCapitolo());
				} else {
					linea.add(QUOTA_CONTABILITA_ACCERTAMENTO);
					linea.add(quotaContabilita.getAnnoEsercizio() + "/" + quotaContabilita.getAccertamento());
				}
//				QuotaContabile1	sv.contabilita.quote[0].importo in centesimi	
				linea.add(this.printImporto(quotaContabilita.getImporto(), true));
			}
			
			if(numeroQuote < 10) { // aggiungo campi vuoti per arrivare alla fine del record
				linea.addAll(this.aggiungiCampiVuoti(((10 -numeroQuote) *3) ));
			}
		}
		
		return linea.toArray(new String[linea.size()]);
	}
	
	@SuppressWarnings("unchecked")
	private void creaLineaCsvHyperSicAPKappaNonRendicontato(PagamentiBD pagamentiBD, it.govpay.bd.model.Pagamento pagamento, BDConfigWrapper configWrapper, int numeroLinea, 
			ConnettoreNotificaPagamenti connettore, OutputStream rendicontateOS, ByteArrayOutputStream nonRendicontateOS, CSVUtils csvUtils) throws ServiceException, JAXBException, SAXException, ValidationException, java.io.IOException { 
		List<String> linea = new ArrayList<String>();

		boolean rendicontato = false;
		SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento();
		Versamento versamento = singoloVersamento.getVersamento(pagamentiBD);
		Documento documento = versamento.getDocumento(configWrapper);
		TipoVersamento tipoVersamento = versamento.getTipoVersamento(configWrapper);
		
		List<Rendicontazione> rendicontazioni = pagamento.getRendicontazioni(pagamentiBD);
		Fr fr = null;
		Rendicontazione rendicontazione = null;
		if(rendicontazioni != null && rendicontazioni.size() > 0) {
			rendicontazione = rendicontazioni.get(0);
			fr = rendicontazione.getFr(pagamentiBD);
			rendicontato = true;
		}

		String contabilitaString = singoloVersamento.getContabilita();
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
		
//		CodiceServizio	$.vocePendenza.contabilita.quoteContabili.proprietaCustom.codiceServizio o versamento.tipoPendenza.codTipoPendenza	
		linea.add(codiceServizio != null ? codiceServizio : tipoVersamento.getCodTipoVersamento());

//		DescrizioneServizio $.vocePendenza.contabilita.quoteContabili.proprietaCustom.descrizioneServizio o versamento.tipoPendenza.descrizione		
		linea.add(descrizioneServizio != null ? descrizioneServizio : (tipoVersamento.getDescrizione() != null ? tipoVersamento.getDescrizione() : ""));
			
//		CodiceDebitore VUOTO
		linea.add("");
			
//		CFPIVADebitore $.soggettoPagatore.identificativo
		linea.add(versamento.getAnagraficaDebitore().getCodUnivoco());
		
//		NominativoDebitore $.soggettoPagatore.anagrafica
		linea.add(versamento.getAnagraficaDebitore().getRagioneSociale());
		
//		CodiceDebito $.documento.identificativo o $.idPendenza
		linea.add(documento != null ? documento.getCodDocumento() : versamento.getCodVersamentoEnte());
		
//		DataEmissione $.dataCaricamento
		linea.add(SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(versamento.getDataCreazione()));
		
//		CausaleDebito $.causale
		linea.add(versamento.getCausaleVersamento().getSimple());
		
//		ImportoDebito importo (in centesimi)
		linea.add(this.printImporto(versamento.getImportoTotale(), true));
		
//		CodiceRata $.documento.numeroRata
		linea.add(versamento.getNumeroRata() != null ? versamento.getNumeroRata() +"" : "");
		
//		CodiceAvviso $.numeroAvviso
		linea.add(versamento.getNumeroAvviso());
		
//		CodiceIUV $.iuvPagamento
		linea.add(versamento.getIuvPagamento());
		
//		DataScadenza $.dataScadenza
		linea.add(versamento.getDataScadenza() != null ? SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(versamento.getDataScadenza()) : "");
		
//		DataPagamento	rendicontazione.data
		linea.add(rendicontazione != null ? SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(rendicontazione.getData()) : "");
		
//		ImportoPagato rendicontazione.importo
		linea.add(rendicontazione != null ? this.printImporto(rendicontazione.getImporto(), true) : "");
		
//		IstitutoMittente fr.ragioneSocialePsp
		linea.add(fr != null ? fr.getRagioneSocialePsp() : "");
		
//		ModalitaPagamento VUOTO
		linea.add("");
		
//		IBANIncasso VUOTO
		linea.add("");
		
//		CodiceFlussoRiversamento fr.codFlusso
		linea.add(fr != null ? fr.getCodFlusso() : "");
		
//		DataRiversamento fr.dataRegolamento
		linea.add(fr != null ? SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA().format(fr.getDataRegolamento()) : "");
		
//		Annotazioni VUOTO
		linea.add("");
		
		// IF sv.contabilita = null, tutto a null.
		if(contabilita == null) {
			linea.addAll(this.aggiungiCampiVuoti(30));
		} else {  
			// conto le quote disponibili
			List<QuotaContabilita> quote = contabilita.getQuote();
			
			int numeroQuote = Math.min(quote.size(), 10);
			
			for (int i = 0; i < numeroQuote; i++) {
				QuotaContabilita quotaContabilita = quote.get(i);
				
//				LivelloContabile1 Se sv.contabilita.quote[0].accertamento = null THEN LivelloContabile1 = CAP ELSE LivelloContabile1 = ACC
//				CodificaContabile1 IF LivelloContabile1 = CAP THEN CodificaContabile1 = {sv.contabilita.quote[0].annoEsercizio}/{sv.contabilita.quote[0].capitolo} ELSE CodificaContabile1 = {sv.contabilita.quote[0].annoEsercizio}/{sv.contabilita.quote[0].accertamento}	

				if(quotaContabilita.getAccertamento() == null) {
					linea.add(QUOTA_CONTABILITA_CAPITOLO);
					linea.add(quotaContabilita.getAnnoEsercizio() + "/" + quotaContabilita.getCapitolo());
				} else {
					linea.add(QUOTA_CONTABILITA_ACCERTAMENTO);
					linea.add(quotaContabilita.getAnnoEsercizio() + "/" + quotaContabilita.getAccertamento());
				}
//				QuotaContabile1	sv.contabilita.quote[0].importo in centesimi	
				linea.add(this.printImporto(quotaContabilita.getImporto(), true));
			}
			
			if(numeroQuote < 10) { // aggiungo campi vuoti per arrivare alla fine del record
				linea.addAll(this.aggiungiCampiVuoti(((10 -numeroQuote) *3) ));
			}
		}
		
		if(rendicontato) {
			rendicontateOS.write(csvUtils.toCsv(linea.toArray(new String[linea.size()])).getBytes());
		} else {
			if(nonRendicontateOS.size() == 0) { // la prima volta che entro metto l'intestazione
				nonRendicontateOS.write(csvUtils.toCsv(HYPERSIC_APKAPPA_HEADER_FILE_CSV).getBytes());
			}
			nonRendicontateOS.write(csvUtils.toCsv(linea.toArray(new String[linea.size()])).getBytes());
		}
	}
	
	
	private String completaValoreCampoConFiller(String valoreCampo, int dimensioneTotaleCampo, boolean numerico, boolean left) {
		String filler = " ";
		if(numerico) {
			filler = "0";
		} 
		
		if(valoreCampo == null) {
			valoreCampo = "";
		}
		
		String tmp = left ? StringUtils.leftPad(valoreCampo, dimensioneTotaleCampo, filler) : StringUtils.rightPad(valoreCampo, dimensioneTotaleCampo, filler);
		
		return tmp.length() > dimensioneTotaleCampo ? tmp.substring(0,dimensioneTotaleCampo) : tmp;
	}
	
	private boolean validaCampo(String nomeCampo, String valoreCampo, int dimensioneCampo) throws ValidationException {
		if(valoreCampo.length() != dimensioneCampo) {
			throw new ValidationException("Il valore contenuto nel campo [" + nomeCampo + "] non rispetta la lunghezza previsti [" + dimensioneCampo + "], trovati [" + valoreCampo.length() + "]");
		}
		
		return true;
	}
	
	private String printImporto(BigDecimal value, boolean removeDecimalSeparator) {
		DecimalFormatSymbols custom=new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(custom);
		format.setGroupingUsed(false);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		
		String formatValue = format.format(value);
		
		if(removeDecimalSeparator) {
			formatValue = formatValue.replace(".", "");
		}
		
		return formatValue;
	}
	
	private List<String> aggiungiCampiVuoti(int numero){
		List<String> lst = new ArrayList<>();
		for (int i = 0; i < numero; i++) {
			lst.add("");
		}
		
		return lst;
	}
}
