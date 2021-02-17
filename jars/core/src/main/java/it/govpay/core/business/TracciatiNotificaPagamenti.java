package it.govpay.core.business;

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
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiNotificaPagamentiBD;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.adapter.DataTypeAdapter;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE;
import it.govpay.model.TracciatoNotificaPagamenti.TIPO_TRACCIATO;

public class TracciatiNotificaPagamenti {

	private static final String HEADER_FILE_CSV = "IUD;codIuv;tipoIdentificativoUnivoco;codiceIdentificativoUnivoco;anagraficaPagatore;indirizzoPagatore;civicoPagatore;capPagatore;localitaPagatore;provinciaPagatore;nazionePagatore;mailPagatore;dataEsecuzionePagamento;importoDovutoPagato;commissioneCaricoPa;tipoDovuto;tipoVersamento;causaleVersamento;datiSpecificiRiscossione;bilancio";
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
			countTracciatiInStatoNonTerminalePerDominio = tracciatiNotificaPagamentiBD.countTracciatiInStatoNonTerminalePerDominio(codDominio, this.tipoTracciato.toString());

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
				log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], non sono stati trovati tracciati in sospeso, ricerco RT da inserire in un nuovo tracciato");
				
				tracciatiNotificaPagamentiBD = new TracciatiNotificaPagamentiBD(configWrapper);

				tracciatiNotificaPagamentiBD.setupConnection(configWrapper.getTransactionID());

				tracciatiNotificaPagamentiBD.setAtomica(false);

				// cerco la data di partenza delle RT da considerare
				Date dataRtDa = tracciatiNotificaPagamentiBD.getDataPartenzaIntervalloRT(codDominio, this.tipoTracciato.toString());

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

				int offset = 0;
				int limit = 100; 
				int totaleRt = 0;
				int lineaElaborazione = 0;
				
				List<String> listaTipiPendenza = connettore.getTipiPendenza();

				log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], verranno ricercate RT da inserire in un nuovo tracciato da ["
						+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtDa)+"] a ["+SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().format(dataRtA)+"]");
				
				List<Rpt> rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
				totaleRt = rtList.size();
				
				log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+rtList.size()+"] RT da inserire in un nuovo tracciato");

				if(rtList.size() > 0) {
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
						beanDati.setLineaElaborazione(offset);
						tracciato.setBeanDati(serializer.getObject(beanDati));

						// insert tracciato
						tracciatiNotificaPagamentiBD.insertTracciato(tracciato);
//						Long idTracciato = tracciato.getId();
						
						log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento nuovo tracciato in stato DRAFT completata.");

						CSVUtils csvUtils = CSVUtils.getInstance(CSVFormat.DEFAULT.withDelimiter(';'));

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

							ZipEntry tracciatoOutputEntry = new ZipEntry(creaNomeEntryTracciato(codDominio, progressivo));
							zos.putNextEntry(tracciatoOutputEntry);
							
							this.inserisciHeader(csvUtils, zos);
							
							do {
								if(rtList.size() > 0) {
									for (Rpt rpt : rtList) {
										lineaElaborazione ++;
										beanDati.setLineaElaborazione(lineaElaborazione);
										this.inserisciRiga(configWrapper, csvUtils, zos, rpt, lineaElaborazione, connettore);
									}
									log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserimento ["+rtList.size()+"] RT nel tracciato completato");
								}

								offset += limit;
								rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, listaTipiPendenza, offset, limit);
								log.trace("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], trovate ["+rtList.size()+"] RT da inserire nel tracciato");
								totaleRt += rtList.size();
							}while(rtList.size() > 0);
							
							this.inserisciFooter(csvUtils, zos);
							
							log.debug("Elaborazione Tracciato "+this.tipoTracciato+" per il Dominio ["+codDominio+"], inserite ["+totaleRt+"] RT nel tracciato");

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

	private String creaNomeEntryTracciato(String codDominio, long progressivo) {
		switch (this.tipoTracciato) {
		case MYPIVOT:
			return "GOVPAY_" + codDominio + "_"+progressivo+".csv";
		case SECIM:
			return "GOVPAY_" + codDominio + "_"+progressivo+".txt";
		}
		
		return null;
	}
	
	public List<TracciatoNotificaPagamenti> findTracciatiInStatoNonTerminalePerDominio(String codDominio, int offset, int limit, IContext ctx) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		TracciatiNotificaPagamentiBD tracciatiNotificaPagamentiBD = null;
		try {
			tracciatiNotificaPagamentiBD = new TracciatiNotificaPagamentiBD(configWrapper);
			// lista tracciati da spedire
			return tracciatiNotificaPagamentiBD.findTracciatiInStatoNonTerminalePerDominio(codDominio, offset, limit, this.tipoTracciato.toString());
		} catch(Throwable e) {
			log.error("Errore la ricerca dei tracciati "+this.tipoTracciato+" in stato non terminale per il dominio ["+codDominio+"]: " + e.getMessage(), e);
			throw new ServiceException(e);
		} finally {
			if(tracciatiNotificaPagamentiBD != null) {
				tracciatiNotificaPagamentiBD.closeConnection();
			}
		}
	}

	private void inserisciRiga(BDConfigWrapper configWrapper, CSVUtils csvUtils, ZipOutputStream zos, Rpt rpt, int numeroLinea, ConnettoreNotificaPagamenti connettore)
			throws java.io.IOException, ServiceException, JAXBException, SAXException, ValidationException {
		switch (this.tipoTracciato) {
		case MYPIVOT:
			zos.write(csvUtils.toCsv(this.creaLineaCsvMyPivot(rpt, configWrapper)).getBytes());
			break;
		case SECIM:
			zos.write(this.creaLineaCsvSecim(rpt, configWrapper, numeroLinea, connettore).getBytes());
			break;
		}
	}

	private void inserisciHeader(CSVUtils csvUtils, ZipOutputStream zos) throws java.io.IOException {
		switch (this.tipoTracciato) {
		case MYPIVOT:
			zos.write(csvUtils.toCsv(this.creaLineaHeaderMyPivot()).getBytes());
			break;
		case SECIM:
			break;
		}
	}

	private void inserisciFooter(CSVUtils csvUtils, ZipOutputStream zos) throws java.io.IOException {
		//do nothing
	}

	@SuppressWarnings("unchecked")
	private String [] creaLineaCsvMyPivot(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException, ValidationException { 
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
			// leggo oggetto mypivot
			if(parse.containsKey("mypivot")) {
				Object mypivotObj = parse.get("mypivot");
				Map<String, Object> mypivot = (Map<String, Object>) mypivotObj;
				if(mypivot.containsKey("tipoDovuto")) {
					tipoDovuto = (String) mypivot.get("tipoDovuto");
				}
				if(mypivot.containsKey("bilancio")) {
					bilancio = (String) mypivot.get("bilancio");
				}
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

	private String [] creaLineaHeaderMyPivot(){
		String [] header = HEADER_FILE_CSV.split(";");
		return header;
	}
	
	
	/*
	  
	  
	  */
	@SuppressWarnings("unchecked")
	private String creaLineaCsvSecim(Rpt rpt, BDConfigWrapper configWrapper, int numeroLinea, ConnettoreNotificaPagamenti connettore) throws ServiceException, JAXBException, SAXException, ValidationException { 
		StringBuilder sb = new StringBuilder();

		Versamento versamento = rpt.getVersamento();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiPagamento.getDatiSingoloPagamento().get(0);
		CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();

		String datiAllegati = versamento.getDatiAllegati();
		String riferimentoCreditore = null;
		String tipoflusso = null;
		if(datiAllegati != null && datiAllegati.length() > 0) {
			Map<String, Object> parse = JSONSerializable.parse(datiAllegati, Map.class);
			// leggo oggetto secim
			if(parse.containsKey("secim")) {
				Object secimObj = parse.get("secim");
				Map<String, Object> secim = (Map<String, Object>) secimObj;
				if(secim.containsKey("riferimentoCreditore")) {
					riferimentoCreditore = (String) secim.get("riferimentoCreditore");
				}
				if(secim.containsKey("tipoflusso")) {
					tipoflusso = (String) secim.get("tipoflusso");
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
		String rata = prefixRata + numeroRata;
		rata = this.completaValoreCampoConFiller(rata, 35, false, false);
		this.validaCampo("RATA", rata, 35);
		sb.append(rata);
		
//		FILLER	275	344	70	
		filler = this.completaValoreCampoConFiller("", 70, false, true);
		this.validaCampo("FILLER 5", filler, 70);
		sb.append(filler);
		
//		RIFERIMENTO CREDITORE	345	379	35	Carattere		SECIM +	$pendenza.{datiAllegati}.secim.riferimentoCreditore o, in sua assenza, il campo $pendenza.voce[0].idVoce
		if(riferimentoCreditore == null)
			riferimentoCreditore = singoliVersamenti.get(0).getCodSingoloVersamentoEnte();
		riferimentoCreditore = "SECIM" + riferimentoCreditore;
		riferimentoCreditore = this.completaValoreCampoConFiller(riferimentoCreditore, 35, false, false);
		this.validaCampo("RIFERIMENTO CREDITORE", riferimentoCreditore, 35);
		sb.append(riferimentoCreditore);
		
//		FILLER	380	457	78	
		filler = this.completaValoreCampoConFiller("", 78, false, true);
		this.validaCampo("FILLER 6", filler, 78);
		sb.append(filler);
		
//		IMPORTO VERSAMENTO	458	472	15	Numerico	13	2	SI	singolo_versamento.importo_singolo_versamento o versamento.importo_totale
		String importoTotalePagato = DataTypeAdapter.printImporto(versamento.getImportoTotale());
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
		String dataIncasso = "";
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
		String codiceReteIncasso = "";
		codiceReteIncasso = this.completaValoreCampoConFiller("", 3, false, true);
		this.validaCampo("CODICE RETE INCASSO", codiceReteIncasso, 3);
		sb.append(codiceReteIncasso);
		
//		CODICE CANALE INCASSO	1086	1088	3	Carattere				Dal PSP che ha e’ stato utilizzato
		String codiceCanaleIncasso = "";
		codiceCanaleIncasso = this.completaValoreCampoConFiller("", 3, false, true);
		this.validaCampo("CODICE CANALE INCASSO", codiceCanaleIncasso, 3);
		sb.append(codiceCanaleIncasso);
		
//		CODICE STRUMENTO INCASSO	1089	1091	3	Carattere				NDP se il campo precedente e’ di tipo PSP, altrimenti bisogna chiedere il codice bollettino
		String codiceStrumentoIncasso = "";
		codiceStrumentoIncasso = this.completaValoreCampoConFiller("", 3, false, true);
		this.validaCampo("CODICE STRUMENTO INCASSO", codiceStrumentoIncasso, 3);
		sb.append(codiceStrumentoIncasso);

//		NUMERO BOLLETTA	1092	1104	13	Numerico	13	0		fr.trn se disponibile
		String numeroBolletta = "";
		numeroBolletta = this.completaValoreCampoConFiller(numeroBolletta, 13, true, true);
		this.validaCampo("NUMERO BOLLETTA", numeroBolletta, 13);
		sb.append(numeroBolletta);
		
//		IMPORTO PAGATO	1105	1119	15	Numerico	13	2		versamento.importo_pagato o rendicontazione.importo_pagato
		String importoPagato = DataTypeAdapter.printImporto(datiPagamento.getImportoTotalePagato());
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
		String importoCommissioniDebitore = DataTypeAdapter.printImporto(commissioniApplicatePSP);
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

		return sb.toString();
	}
	
	
	private String completaValoreCampoConFiller(String valoreCampo, int dimensioneTotaleCampo, boolean numerico, boolean left) {
		String filler = " ";
		if(numerico) {
			filler = "0";
		} 
		
		if(valoreCampo == null) {
			valoreCampo = "";
		}
		
		return left ? StringUtils.leftPad(valoreCampo, dimensioneTotaleCampo, filler) : StringUtils.rightPad(valoreCampo, dimensioneTotaleCampo, filler);
	}
	
	private boolean validaCampo(String nomeCampo, String valoreCampo, int dimensioneCampo) throws ValidationException {
		if(valoreCampo.length() != dimensioneCampo) {
			throw new ValidationException("Il valore contenuto nel campo [" + nomeCampo + "] non rispetta la lunghezza previsti [" + dimensioneCampo + "], trovati [" + valoreCampo.length() + "]");
		}
		
		return true;
	}
}
