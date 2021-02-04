package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.TracciatoMyPivot;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiMyPivotBD;
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

				tracciatiMyPivotBD.setAutoCommit(false);

				// cerco la data di partenza delle RT da considerare
				Date dataRtDa = tracciatiMyPivotBD.getDataPartenzaIntervalloRT(codDominio);

				if(dataRtDa != null) { // se questa data esiste e' la dataRtA del tracciato precedente in ordine temporale ci aggiungo 1 millisecondo per portarla alle 00:00
					Calendar c = Calendar.getInstance();
					c.setTime(dataRtDa);
					c.add(Calendar.MILLISECOND, 1);
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

				List<Rpt> rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, offset, limit);

				if(rtList.size() > 0) {
					// init tracciato
					TracciatoMyPivot tracciato = new TracciatoMyPivot();
					tracciato.setDataRtDa(dataRtDa);
					tracciato.setDataRtA(dataRtA);
					tracciato.setIdDominio(dominio.getId());
					tracciato.setStato(STATO_ELABORAZIONE.FILE_NUOVO);
					long progressivo = tracciatiMyPivotBD.generaProgressivoTracciato(dominio, ConnettoreMyPivot.Tipo.MYPIVOT.toString(), "Tracciato_");
					tracciato.setNomeFile("GOVPAY_" + codDominio + "_"+progressivo+".zip");

					CSVUtils csvUtils = CSVUtils.getInstance(CSVFormat.DEFAULT.withDelimiter(';'));

					try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ZipOutputStream zos = new ZipOutputStream(baos);){

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
							rtList = rptBD.ricercaRtDominio(codDominio, dataRtDa, dataRtA, offset, limit);
						}while(rtList.size() > 0);

						zos.flush();
						zos.closeEntry();
						zos.flush();
						zos.close();
						tracciato.setRawContenuto(baos.toByteArray());

						// insert tracciato
						tracciatiMyPivotBD.insertTracciato(tracciato);

					} catch (java.io.IOException e) { // gestione errori scrittura zip
						log.error(e.getMessage(), e);
					} finally {

					}
				}
			} catch(Throwable e) {
				log.error("Errore durante l'elaborazione del tracciato mypivot: " + e.getMessage(), e);
				tracciatiMyPivotBD.rollback();	

			} finally {
				if(tracciatiMyPivotBD != null) {
					tracciatiMyPivotBD.closeConnection();
				}
			}
		}
	}

	private  String [] creaLineaCsv(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException { 
		List<String> linea = new ArrayList<String>();

		Versamento versamento = rpt.getVersamento();
		Applicazione applicazione = versamento.getApplicazione(configWrapper);
		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiPagamento.getDatiSingoloPagamento().get(0);
		
		String datiAllegati = versamento.getDatiAllegati();
		
		
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
		String tipoDovuto = StringUtils.isNotBlank(versamento.getTassonomiaAvviso()) ? versamento.getTassonomiaAvviso() : versamento.getTipoVersamento(configWrapper).getCodTipoVersamento();
		linea.add(tipoDovuto); // TODO leggi dati allegati
		// tipoVersamento: vuoto
		linea.add("");
		// causaleVersamento: rt.datiPagamento.datiSingoloPagamento[0].causaleVersamento
		linea.add(ctDatiSingoloPagamentoRT.getCausaleVersamento());
		// datiSpecificiRiscossione: rt.datiPagamento.datiSingoloPagamento[0].datiSpecificiRiscossione
		linea.add(ctDatiSingoloPagamentoRT.getDatiSpecificiRiscossione());
		// bilancio: versamento.datiAllegati.mypivot.bilancio o vuoto
		linea.add(""); // TODO leggi dati allegati

		return linea.toArray(new String[linea.size()]);
	}

	private String [] creaLineaHeader(){
		String [] header = HEADER_FILE_CSV.split(";");

		return header;
	}
}
