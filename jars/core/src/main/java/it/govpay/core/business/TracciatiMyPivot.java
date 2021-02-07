package it.govpay.core.business;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang.StringUtils;
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
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.TracciatoMyPivot;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiMyPivotBD;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.adapter.DataTypeAdapter;
import it.govpay.model.ConnettoreMyPivot;
import it.govpay.model.TracciatoMyPivot.STATO_ELABORAZIONE;

public class TracciatiMyPivot {

	private static final String HEADER_FILE_CSV = "IUD;codIuv;tipoIdentificativoUnivoco;codiceIdentificativoUnivoco;anagraficaPagatore;indirizzoPagatore;civicoPagatore;capPagatore;localitaPagatore;provinciaPagatore;nazionePagatore;mailPagatore;dataEsecuzionePagamento;importoDovutoPagato;commissioneCaricoPa;tipoDovuto;tipoVersamento;causaleVersamento;datiSpecificiRiscossione;bilancio";
	private static Logger log = LoggerWrapperFactory.getLogger(TracciatiMyPivot.class);

	public TracciatiMyPivot() {
	}

	public void elaboraTracciatoMyPivot(Dominio dominio, IContext ctx) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		TracciatiMyPivotBD tracciatiMyPivotBD = null;
		String codDominio = dominio.getCodDominio();
		it.govpay.core.beans.tracciati.TracciatoMyPivot beanDati = null;

		long countTracciatiInStatoNonTerminalePerDominio = 0;
		try {
			tracciatiMyPivotBD = new TracciatiMyPivotBD(configWrapper);
			// controllo se ha tracciati in sospeso
			countTracciatiInStatoNonTerminalePerDominio = tracciatiMyPivotBD.countTracciatiInStatoNonTerminalePerDominio(codDominio);

		} catch(Throwable e) {
			log.error("Errore la ricerca dei tracciati mypivot in stato non terminale per il dominio ["+codDominio+"]: " + e.getMessage(), e);
			return;
		} finally {
			if(tracciatiMyPivotBD != null) {
				tracciatiMyPivotBD.closeConnection();
			}
		}

		if(countTracciatiInStatoNonTerminalePerDominio == 0) {
			try {
				tracciatiMyPivotBD = new TracciatiMyPivotBD(configWrapper);

				tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());

				tracciatiMyPivotBD.setAtomica(false);

				// cerco la data di partenza delle RT da considerare
				Date dataRtDa = tracciatiMyPivotBD.getDataPartenzaIntervalloRT(codDominio);

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
				RptBD rptBD = new RptBD(tracciatiMyPivotBD);

				rptBD.setAtomica(false);

				int offset = 0;
				int limit = 500; 
				int totaleRt = 0;

				List<Rpt> rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, offset, limit);
				totaleRt = rtList.size();

				if(rtList.size() > 0) {
					try {
						tracciatiMyPivotBD.setAutoCommit(false);
						
						SerializationConfig config = new SerializationConfig();
						config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
						config.setIgnoreNullValues(true);
						ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
						
						// init tracciato
						TracciatoMyPivot tracciato = new TracciatoMyPivot();
						tracciato.setDataRtDa(dataRtDa);
						tracciato.setDataRtA(dataRtA);
						tracciato.setIdDominio(dominio.getId());
						tracciato.setStato(STATO_ELABORAZIONE.DRAFT);
						long progressivo = tracciatiMyPivotBD.generaProgressivoTracciato(dominio, ConnettoreMyPivot.Tipo.MYPIVOT.toString(), "Tracciato_");
						tracciato.setNomeFile("GOVPAY_" + codDominio + "_"+progressivo+".zip");
						tracciato.setRawContenuto("TMP".getBytes()); // inserisco un contenuto finto provvisorio
						tracciato.setDataCreazione(new Date());
						beanDati = new it.govpay.core.beans.tracciati.TracciatoMyPivot();
						beanDati.setStepElaborazione(STATO_ELABORAZIONE.DRAFT.toString());
						beanDati.setDataUltimoAggiornamento(new Date());
						beanDati.setLineaElaborazione(offset);
						tracciato.setBeanDati(serializer.getObject(beanDati));
						
						// insert tracciato
						tracciatiMyPivotBD.insertTracciato(tracciato);
						Long idTracciato = tracciato.getId();
	
						CSVUtils csvUtils = CSVUtils.getInstance(CSVFormat.DEFAULT.withDelimiter(';'));
						
						OutputStream oututStreamDestinazione = null;
						Long oid = null;
						Blob blobCsv = null;
						
						TipiDatabase tipoDatabase = ConnectionManager.getJDBCServiceManagerProperties().getDatabase();
						
						switch (tipoDatabase) {
						case MYSQL:
							try {
								blobCsv = tracciatiMyPivotBD.getConnection().createBlob();
								oututStreamDestinazione = blobCsv.setBinaryStream(1);
							} catch (SQLException e) {
								log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
								throw new ServiceException(e);
							}
							break;
						case ORACLE:
							try {
								blobCsv = tracciatiMyPivotBD.getConnection().createBlob();
								oututStreamDestinazione = blobCsv.setBinaryStream(1);
							} catch (SQLException e) {
								log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
								throw new ServiceException(e);
							}
							break;
						case SQLSERVER:
							try {
								blobCsv = tracciatiMyPivotBD.getConnection().createBlob();
								oututStreamDestinazione = blobCsv.setBinaryStream(1);
							} catch (SQLException e) {
								log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
								throw new ServiceException(e);
							}
							break;
						case POSTGRESQL:
							org.openspcoop2.utils.datasource.Connection wrappedConn = (org.openspcoop2.utils.datasource.Connection) tracciatiMyPivotBD.getConnection();
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
	
							ZipEntry tracciatoOutputEntry = new ZipEntry("GOVPAY_" + codDominio + "_"+progressivo+".csv");
							zos.putNextEntry(tracciatoOutputEntry);
							zos.write(csvUtils.toCsv(this.creaLineaHeader()).getBytes());
							do {
								if(rtList.size() > 0) {
									for (Rpt rpt : rtList) {
										zos.write(csvUtils.toCsv(this.creaLineaCsv(rpt, configWrapper)).getBytes());
									}
								}
								
								offset += limit;
								beanDati.setLineaElaborazione(offset);
								rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, offset, limit);
								totaleRt = rtList.size();
							}while(rtList.size() > 0);
	
							// chiusa entry
							zos.flush();
							zos.closeEntry();
							// chiuso stream
							zos.flush();
							zos.close();
							
							tracciato.setStato(STATO_ELABORAZIONE.FILE_NUOVO);
							beanDati.setStepElaborazione(STATO_ELABORAZIONE.FILE_NUOVO.toString());
							beanDati.setNumRtTotali(totaleRt);
							try {
								tracciato.setBeanDati(serializer.getObject(beanDati));
							} catch (IOException e1) {}
							// update tracciato
							switch (tipoDatabase) {
							case MYSQL:
							case ORACLE:
							case SQLSERVER:
								tracciatiMyPivotBD.updateFineElaborazioneCsvBlob(tracciato,blobCsv);
								break;
							case POSTGRESQL:
								tracciatiMyPivotBD.updateFineElaborazioneCsvOid(tracciato,oid);
								break;
							case DB2:
							case DEFAULT:
							case DERBY:
							case HSQL:
							default:
								throw new ServiceException("TipoDatabase ["+tipoDatabase+"] non gestito.");
							}
							
							// update rpt
							rptBD.updateIdTracciatoMyPivotRtDominio(codDominio, dataRtDa, dataRtA, idTracciato);
	
							if(!tracciatiMyPivotBD.isAutoCommit()) tracciatiMyPivotBD.commit();
						} catch (java.io.IOException e) { // gestione errori scrittura zip
							log.error(e.getMessage(), e);
							throw e;
						} 
					} catch(Throwable e) {
						log.error("Errore durante l'elaborazione del tracciato mypivot: " + e.getMessage(), e);
						if(!tracciatiMyPivotBD.isAutoCommit())  tracciatiMyPivotBD.rollback();	
					} finally {
						if(!tracciatiMyPivotBD.isAutoCommit()) tracciatiMyPivotBD.setAutoCommit(true);
					}
				}
			} catch(Throwable e) {
				log.error("Errore durante l'elaborazione del tracciato mypivot: " + e.getMessage(), e);
			} finally {
				if(tracciatiMyPivotBD != null) {
					tracciatiMyPivotBD.closeConnection();
				}
			}
		}
	}

	private  String [] creaLineaCsv(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException, ValidationException { 
		List<String> linea = new ArrayList<String>();

		Versamento versamento = rpt.getVersamento();
		Applicazione applicazione = versamento.getApplicazione(configWrapper);
		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiPagamento.getDatiSingoloPagamento().get(0);
		
		String datiAllegati = versamento.getDatiAllegati();
		String tipoDovuto = null;
		String bilancio = null;
		if(datiAllegati != null && datiAllegati.length() > 0) {
			Map<String, Object> parse = JSONSerializable.parse(datiAllegati, Map.class);
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
		linea.add(DataTypeAdapter.printImporto(datiPagamento.getImportoTotalePagato()));
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

	private String [] creaLineaHeader(){
		String [] header = HEADER_FILE_CSV.split(";");

		return header;
	}
}
