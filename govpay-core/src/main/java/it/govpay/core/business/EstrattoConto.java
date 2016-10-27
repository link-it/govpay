package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.csv.Format;
import org.openspcoop2.utils.csv.FormatReader;
import org.openspcoop2.utils.csv.Printer;
import org.openspcoop2.utils.logger.beans.Property;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.reportistica.EstrattiContoBD;
import it.govpay.core.utils.CSVSerializerProperties;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Anagrafica;
import it.govpay.orm.Dominio;
import it.govpay.stampe.pdf.Costanti;
import it.govpay.stampe.pdf.estrattoConto.EstrattoContoPdf;

public class EstrattoConto extends BasicBD {

	public static final String FORMATO_CSV = "csv";
	public static final String FORMATO_PDF = "pdf";
	public static final String FORMATO_STAR = "*";

	private static final String CSV_SEPARATOR = "|";

	private static Logger log = LogManager.getLogger();

	private static final int LIMIT = 50;

	public static final String PAGAMENTI_SENZA_RPT = Costanti.PAGAMENTI_SENZA_RPT_KEY;
	public static final String MARCA_DA_BOLLO = Costanti.MARCA_DA_BOLLO_KEY;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

	private Format formatW= null;

	public EstrattoConto(BasicBD bd) {
		super(bd);

		try{
			// Setto le properties di scrittura
			FormatReader formatWriter = new FormatReader(CSVSerializerProperties.getInstance(log).getProperties());
			this.formatW = formatWriter.getFormat();
		}catch(Exception e){
			log.error("Errore durante l'inizializzazione di EstrattoConto: " + e.getMessage(),e);
		}
	}

	public String creaEstrattiContoSuFileSystem() throws Exception {
		List<String> response = new ArrayList<String>();
		GpContext ctx = GpThreadLocal.get();

		FileOutputStream fos;
		FileOutputStream fosPdf = null;
		Printer printer  = null;
		int numeroMesi = GovpayConfig.getInstance().getNumeroMesiEstrattoConto();
		ctx.getContext().getRequest().addGenericProperty(new Property("numeroMesi", numeroMesi+""));
		ctx.log("estrattoConto.inizioProcedura");

		String basePdfPath =  null;
		File basePdfDir = null;

		try {
			//controllo directory base
			String basePath = GovpayConfig.getInstance().getPathEstrattoConto();
			if(StringUtils.isEmpty(basePath)){
				throw new Exception("Directory di export non impostata, impossibile eseguire la generazione degli Estratti Conto.");
			}

			File baseDir = new File(basePath);

			if(!baseDir.exists()){
				throw new Exception("Directory di export (" + basePath + ") non presente, impossibile eseguire la generazione degli Estratti Conto.");
			}

			// pdf
			boolean creaFilePdf = GovpayConfig.getInstance().isBatchEstrattoContoPdf();
			String pathLoghi = null;
			if(creaFilePdf){
				//controllo directory base
				basePdfPath = GovpayConfig.getInstance().getPathEstrattoContoPdf();
				if(StringUtils.isEmpty(basePdfPath)){
					throw new Exception("Directory di export PDf non impostata, impossibile eseguire la generazione degli Estratti Conto PDF.");
				}

				basePdfDir = new File(basePdfPath);

				if(!basePdfDir.exists()){
					throw new Exception("Directory di export PDF (" + basePdfPath + ") non presente, impossibile eseguire la generazione degli Estratti Conto in formato PDF.");
				}

				pathLoghi = GovpayConfig.getInstance().getPathEstrattoContoPdfLoghi();
			}

			// prelevo la lista dei domini registrati
			DominiBD dominiBD = new DominiBD(this);
			DominioFilter dominiFilter  = dominiBD.newFilter();
			List<FilterSortWrapper> filterSortList = new ArrayList<FilterSortWrapper>();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filterSortList.add(fsw);
			dominiFilter.setFilterSortList(filterSortList );
			List<it.govpay.bd.model.Dominio> domini = dominiBD.findAll(dominiFilter);

			SimpleDateFormat f2 = new SimpleDateFormat("yyyy_MM");
			SimpleDateFormat f3 = new SimpleDateFormat("yyyy/MM");

			int giornoEsecuzione = GovpayConfig.getInstance().getGiornoEsecuzioneEstrattoConto();
			// Controllo se ho passato il giorno di esecuzione, altrimenti il mese scorso non lo voglio.
			Calendar c = Calendar.getInstance();
			boolean ignoraScorsoMese = c.get(Calendar.DAY_OF_MONTH) < giornoEsecuzione;

			// EstrattiContoBD
			EstrattiContoBD estrattiContoBD = new EstrattiContoBD(this);

			for (it.govpay.bd.model.Dominio dominio : domini) {
				String denominazioneDominio = dominio.getRagioneSociale()+" ("+dominio.getCodDominio()+")";

				ctx.log("estrattoConto.inizioDominio", denominazioneDominio);
				log.debug("Generazione Estratto Conto per il Dominio ["+denominazioneDominio+"] in corso...");
				// 1 controllo esistenza directory con nome codDominio
				File dominioDir = new File(basePath+File.separator + dominio.getCodDominio());

				if(!dominioDir.exists()){
					log.debug("creo directory per il dominio ["+denominazioneDominio+"]: "+dominioDir.getAbsolutePath());
					dominioDir.mkdir();
				}

				Calendar cf = Calendar.getInstance();
				Date df = new Date();
				// inizializzazione del contatore mesi
				cf.setTime(df);
				cf.set(Calendar.MILLISECOND, 0);
				cf.set(Calendar.SECOND, 0);
				cf.set(Calendar.MINUTE, 0);
				cf.set(Calendar.HOUR_OF_DAY, 0);
				cf.set(Calendar.DAY_OF_MONTH, 1);
				cf.add(Calendar.MILLISECOND, -1);

				Calendar ci = Calendar.getInstance();
				Date di = new Date();
				// inizializzazione del contatore mesi
				ci.setTime(di);
				ci.set(Calendar.MILLISECOND, 0);
				ci.set(Calendar.SECOND, 0);
				ci.set(Calendar.MINUTE, 0);
				ci.set(Calendar.HOUR_OF_DAY, 0);
				ci.set(Calendar.DAY_OF_MONTH, 1);

				if(ignoraScorsoMese) {
					ci.add(Calendar.MONTH, -1);
					cf.add(Calendar.MONTH, -1);
				}

				response.add("Dominio "+denominazioneDominio+"#");




				for (int contatoreMese = 0; contatoreMese < numeroMesi; contatoreMese++) {

					Map<String, Printer> ecPerIban = new HashMap<String, Printer>();
					Map<String, List<it.govpay.model.EstrattoConto>> pagamentiPerIban = new HashMap<String, List<it.govpay.model.EstrattoConto>>();
					StringBuilder sb = new StringBuilder();
					// ad ogni iterazione tolgo un mese
					Date dataFine = cf.getTime();

					ci.add(Calendar.MONTH, -1);
					Date dataInizio = ci.getTime();

					log.debug("Generazione Estratto Conto per il Dominio ["+dominio.getCodDominio()+"] Intervallo Temporale dal ["+dataInizio+"] al ["+dataFine+"] ...");
					// creo nome file csv nel formato codDominio_YYYY_MM.csv
					String dominioCsvFileName = dominio.getCodDominio() + "_" + f2.format(dataInizio) +".csv";
					log.debug("Nome del file CSV destinazione: "+dominioCsvFileName);
					File dominioFile = new File(basePath+File.separator + dominio.getCodDominio() + File.separator + dominioCsvFileName );

					boolean creaEstrattoConto = true;

					int offset = 0;

					try{

						if(!dominioFile.exists()){
							log.debug("creo il file CSV: "+dominioFile.getAbsolutePath());
							dominioFile.createNewFile();
							// per ogni mese fino al quello indicato  nelle properties calcolo l'estratto conto
							fos = new FileOutputStream(dominioFile);
							printer = new Printer(this.formatW , fos);
							printer.printRecord(CSVUtils.getEstrattoContoCsvHeader());
						} else {
							creaEstrattoConto = false;
						}

						List<it.govpay.model.EstrattoConto> estrattoConto = estrattiContoBD.estrattoContoFromCodDominioIntervalloDate(dominio.getCodDominio(), dataInizio, dataFine, offset, LIMIT);

						while(estrattoConto != null && !estrattoConto.isEmpty()) {
							List<FileOutputStream> fosList = new ArrayList<FileOutputStream>();
							List<String> fileEsistentiList = new ArrayList<String>();

							for (it.govpay.model.EstrattoConto pagamentoExt : estrattoConto) {
								List<String> csvRow = CSVUtils.getEstrattoContoAsCsvRow(pagamentoExt, this.sdf); 
								if(creaEstrattoConto)
									printer.printRecord(csvRow);

								String ibanAccredito = pagamentoExt.getIbanAccredito();

								// l'iban e' null solo se ho una marca da bollo;
								if(ibanAccredito == null)
									ibanAccredito = MARCA_DA_BOLLO;

								// iban e' vuoto se proviene dalla tabella rendicontazioni senza rpt;
								if(ibanAccredito.isEmpty())
									ibanAccredito = PAGAMENTI_SENZA_RPT;


								if(ibanAccredito != null) {
									if(creaFilePdf){
										List<it.govpay.model.EstrattoConto> estrattoContoPdf = null;
										if(pagamentiPerIban.containsKey(ibanAccredito)) {
											estrattoContoPdf = pagamentiPerIban.get(ibanAccredito);
										} else{
											estrattoContoPdf = new ArrayList<it.govpay.model.EstrattoConto>();
											pagamentiPerIban.put(ibanAccredito, estrattoContoPdf);
										}
										estrattoContoPdf.add(pagamentoExt);
									}

									Printer printerIban;
									if(ecPerIban.containsKey(ibanAccredito)) {
										printerIban = ecPerIban.get(ibanAccredito);
										printerIban.printRecord(csvRow);
									} else {
										String dominioPerIbanCsvFileName = dominio.getCodDominio() + "_" + ibanAccredito + "_" + f2.format(dataInizio) +".csv";
										log.debug("Nome del file CSV destinazione: "+dominioPerIbanCsvFileName);

										File dominioPerIbanFile = new File(basePath+File.separator + dominio.getCodDominio() + File.separator + dominioPerIbanCsvFileName );

										if(!fileEsistentiList.contains(dominioPerIbanFile.getPath())) {
											if(!dominioPerIbanFile.exists()){
												sb.append("Generazione estratto conto per il mese " + f3.format(dataInizio) + " e per l'iban accredito ["+ibanAccredito+"] eseguita" + CSV_SEPARATOR);
												log.debug("creo il file CSV: "+dominioPerIbanFile.getAbsolutePath());
												dominioPerIbanFile.createNewFile();
												FileOutputStream fosIban = new FileOutputStream(dominioPerIbanFile);
												fosList.add(fosIban);

												printerIban = new Printer(this.formatW , fosIban);
												printerIban.printRecord(CSVUtils.getEstrattoContoCsvHeader());

												ecPerIban.put(ibanAccredito, printerIban);
												printerIban.printRecord(csvRow);
											} else {
												fileEsistentiList.add(dominioPerIbanFile.getPath());
												sb.append("Generazione estratto conto per il mese " + f3.format(dataInizio) + " e per l'iban accredito ["+ibanAccredito+"] non eseguita in quanto il file ["+dominioPerIbanFile.getPath()+"] esiste gia."+CSV_SEPARATOR);
											}
										}
									}
								}
							}

							offset += estrattoConto.size();
							estrattoConto = estrattiContoBD.estrattoContoFromCodDominioIntervalloDate(dominio.getCodDominio(), dataInizio, dataFine, offset, LIMIT);
						}


						//creazione pdf
						if(creaFilePdf){
							boolean creaEstrattoContoPdf = true;

							for (String ibanAccredito : pagamentiPerIban.keySet()) {
								String esitoGenerazione = null;
								List<it.govpay.model.EstrattoConto> estrattoContoPdf = pagamentiPerIban.get(ibanAccredito);

								log.debug("Generazione Estratto Conto in formato PDF per il Dominio ["+dominio.getCodDominio()+"] mese " 
										+ f3.format(dataInizio) + " e per l'iban accredito ["+ibanAccredito+"]  ...");
								// creo nome file csv nel formato codDominio_YYYY_MM.csv
								String dominioPdfFileName = dominio.getCodDominio() +  "_"  + ibanAccredito + "_" + f2.format(dataInizio) +".pdf";
								log.debug("Nome del file PDf destinazione: "+dominioPdfFileName);
								File dominioPdfFile = new File(basePdfPath+File.separator + dominio.getCodDominio() + File.separator + dominioPdfFileName );

								if(!dominioPdfFile.exists()){
									log.debug("creo il file PDf: "+dominioPdfFile.getAbsolutePath());
									dominioPdfFile.createNewFile();
									fosPdf  = new FileOutputStream(dominioPdfFile);
									Anagrafica anagraficaDominio = dominio.getAnagrafica(this);
									esitoGenerazione = EstrattoContoPdf.getPdfEstrattoConto(pathLoghi,dominio,anagraficaDominio, dataInizio, dataFine, ibanAccredito, estrattoContoPdf, fosPdf,log); 

								} else {
									creaEstrattoContoPdf = false;
								}

								if(creaEstrattoContoPdf) {
									sb.append("Generazione estratto conto in formato PDF per il mese " + f3.format(dataInizio) + " e per l'iban accredito ["+ibanAccredito+"] eseguita correttamente")
									.append("#generato file "+dominioPdfFileName+"." + CSV_SEPARATOR);
									if(esitoGenerazione != null){
										sb.append("Attenzione")
										.append("# "+esitoGenerazione+"." + CSV_SEPARATOR);	
									}

									ctx.log("estrattoConto.fineDominioMeseOk", denominazioneDominio, f3.format(dataInizio), estrattoContoPdf.size() +"" ,dominioPdfFileName);
								} else {
									sb.append("Generazione estratto conto in formato PDF per il mese " + f3.format(dataInizio) + " e per l'iban accredito ["+ibanAccredito+"] non eseguita")
									.append("#il file "+dominioPdfFileName+" e' gia' presente." + CSV_SEPARATOR);
									ctx.log("estrattoConto.fineDominioMeseFileGiaPresente",denominazioneDominio, f3.format(dataInizio), dominioPdfFileName);
								}
							}
						}


						if(creaEstrattoConto) {
							sb.append("Generazione estratto conto per il mese " + f3.format(dataInizio) + " eseguita correttamente")
							.append("#serializzati "+offset+" pagamenti sul file "+dominioCsvFileName+".");
							ctx.log("estrattoConto.fineDominioMeseOk", denominazioneDominio, f3.format(dataInizio), offset +"" ,dominioCsvFileName);
						} else {
							sb.append("Generazione estratto conto per il mese " + f3.format(dataInizio) + " non eseguita")
							.append("#il file "+dominioCsvFileName+" e' gia' presente.");
							ctx.log("estrattoConto.fineDominioMeseFileGiaPresente",denominazioneDominio, f3.format(dataInizio), dominioCsvFileName);
						}
					} finally{
						try{
							if(printer!=null){
								printer.close();
							}
						}catch (Exception e) {
							throw new Exception("Errore durante la chiusura dello stream ",e);
						}
						for(Printer printerIban: ecPerIban.values())
							try{
								if(printerIban!=null){
									printerIban.close();
								}
							}catch (Exception e) {
								throw new Exception("Errore durante la chiusura dello stream ",e);
							}

						try{
							if(fosPdf!=null){
								fosPdf.close();
							}
						}catch (Exception e) {
							throw new Exception("Errore durante la chiusura dello stream ",e);
						}
					}

					log.debug("Estratto Conto per il Dominio ["+denominazioneDominio+"] completato con esito: "+sb.toString());
					response.add(sb.toString());

					cf.add(Calendar.MONTH, -1);
				}

				ctx.log("estrattoConto.fineDominio", denominazioneDominio);
			}

			if(response.isEmpty()) {
				ctx.log("estrattoConto.fineProceduraNoDomini");
				return "Estratto Conto completato#Nessun Dominio Registrato.";
			} else {
				ctx.log("estrattoConto.fineProceduraOk");
				return StringUtils.join(response,CSV_SEPARATOR);
			}
		} catch(Exception e) {
			ctx.log("estrattoConto.erroreProcedura",e.getMessage());
			log.error(e,e);
			throw e;
		} finally {
		}
	}

	public List<String> getListaEstrattoConto(String codDominio, String formatoFile) throws Exception {
		List<String> response = new ArrayList<String>();
		GpContext ctx = GpThreadLocal.get();
		int numeroMesi = GovpayConfig.getInstance().getNumeroMesiEstrattoConto();
		ctx.getContext().getRequest().addGenericProperty(new Property("numeroMesi", numeroMesi+""));
		ctx.log("estrattoConto.listaEstrattoContoDominio");

		try {
			//controllo directory base
			String basePath = GovpayConfig.getInstance().getPathEstrattoConto();
			if(StringUtils.isEmpty(basePath)){
				throw new Exception("Directory di export non impostata, impossibile trovare la lista degli Estratto Conto per il dominio ["+codDominio+"].");
			}

			File baseDir = new File(basePath);

			if(!baseDir.exists()){
				throw new Exception("Directory di export non presente, impossibile trovare la lista degli Estratto Conto per il dominio ["+codDominio+"].");
			}

			// controllo esistenza dominio 
			DominiBD dominiBD = new DominiBD(this);
			it.govpay.model.Dominio dominio  = dominiBD.getDominio(codDominio);

			String denominazioneDominio = dominio.getRagioneSociale()+" ("+dominio.getCodDominio()+")";

			// 1 controllo esistenza directory con nome codDominio
			File dominioDir = new File(basePath+File.separator + dominio.getCodDominio());
			if(!dominioDir.exists()){
				log.debug("creo directory per il dominio ["+denominazioneDominio+"]: "+dominioDir.getAbsolutePath());
				dominioDir.mkdir();
			}

			for (File file : dominioDir.listFiles()) {
				// String fileName = FilenameUtils.removeExtension(file.getName()); 
				//				response.add(fileName);
				response.add(file.getName());
			}

			if(response.isEmpty()) {
				ctx.log("estrattoConto.listaEstrattoContoDominioVuota");
			} else {
				ctx.log("estrattoConto.listaEstrattoContoDominioOk");
			}

			return response;
		} catch(NotFoundException e){
			ctx.log("estrattoConto.listaEstrattoContoDominioKo",e.getMessage());
			throw new Exception("Dominio ["+codDominio+"] non registrato in GovPay.");
		}	catch(Exception e) {

			ctx.log("estrattoConto.listaEstrattoContoDominioKo",e.getMessage());
			log.error(e,e);
			throw e;
		} finally {
		}
	}

	public InputStream scaricaEstrattoConto(String codDominio, String nomeFile, String formatoFile) throws Exception {
		InputStream response = null;
		GpContext ctx = GpThreadLocal.get();
		int numeroMesi = GovpayConfig.getInstance().getNumeroMesiEstrattoConto();
		ctx.getContext().getRequest().addGenericProperty(new Property("numeroMesi", numeroMesi+""));
		ctx.log("estrattoConto.scaricaEstrattoContoDominio");

		try {
			//controllo directory base
			String basePath = GovpayConfig.getInstance().getPathEstrattoConto();
			if(StringUtils.isEmpty(basePath)){
				throw new Exception("Directory di export non impostata, impossibile scaricare l'Estratto Conto ["+nomeFile+"] per il dominio ["+codDominio+"].");
			}

			File baseDir = new File(basePath);

			if(!baseDir.exists()){
				throw new Exception("Directory di export non impostata, impossibile scaricare l'Estratto Conto ["+nomeFile+"] per il dominio ["+codDominio+"].");
			}

			// controllo esistenza dominio 
			DominiBD dominiBD = new DominiBD(this);
			it.govpay.model.Dominio dominio  = dominiBD.getDominio(codDominio);

			String denominazioneDominio = dominio.getRagioneSociale()+" ("+dominio.getCodDominio()+")";

			// 1 controllo esistenza directory con nome codDominio
			File dominioDir = new File(basePath+File.separator + dominio.getCodDominio());
			if(!dominioDir.exists()){
				throw new Exception("Directory di contenente gli Estratto Conto per il dominio ["+denominazioneDominio+"] non presente.");
			}

			String fileToDownload = basePath+File.separator + dominio.getCodDominio()+ File.separator + nomeFile;

			// per ora si gestisce solo il formato CSV
			//			String extension = ".csv";

			//			if(formatoFile != null && formatoFile.equals(FORMATO_PDF))
			//				extension = ".pdf";
			//+ extension
			File estrattoConto = new File((fileToDownload ));

			if(!estrattoConto.exists() || estrattoConto.isDirectory()){
				throw new Exception("Il file richiesto ["+nomeFile+"] per il Dominio ["+codDominio+"] non e' disponibile in formato ["+formatoFile+"].");
			}

			response = new FileInputStream(estrattoConto);

			ctx.log("estrattoConto.scaricaEstrattoContoDominioOk");

			return response;
		}catch(NotFoundException e){
			ctx.log("estrattoConto.listaEstrattoContoDominioKo",e.getMessage());
			throw new Exception("Dominio ["+codDominio+"] non registrato in GovPay.");
		}	 catch(Exception e) {
			ctx.log("estrattoConto.scaricaEstrattoContoDominioKo",e.getMessage());
			log.error(e,e);
			throw e;
		} finally {
		}
	}


	public List<it.govpay.core.business.model.EstrattoConto> getEstrattoContoVersamenti(List<it.govpay.core.business.model.EstrattoConto> inputEstrattoConto,String pathLoghi) throws Exception{
		log.debug("Generazione dei PDF estratto Conto in corso...");
		try{
			EstrattiContoBD estrattiContoBD = new EstrattiContoBD(this);

			for (it.govpay.core.business.model.EstrattoConto estrattoConto : inputEstrattoConto) {
				log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()+"], Versamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] in corso...");
				Map<String, List<it.govpay.model.EstrattoConto>> pagamentiPerIban = new HashMap<String, List<it.govpay.model.EstrattoConto>>();
				int offset = 0;

				log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()+"], Versamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] aggregazione dei dati in corso...");
				List<it.govpay.model.EstrattoConto> lstEstrattoConto = estrattiContoBD.estrattoContoFromCodDominioIdVersamenti(estrattoConto.getDominio().getCodDominio(), estrattoConto.getIdVersamenti(), offset, LIMIT); 

				while(lstEstrattoConto != null && !lstEstrattoConto.isEmpty()) {
					for (it.govpay.model.EstrattoConto pagamentoExt : lstEstrattoConto) {
						String ibanAccredito = pagamentoExt.getIbanAccredito();

						// l'iban e' null solo se ho una marca da bollo;
						if(ibanAccredito == null)
							ibanAccredito = MARCA_DA_BOLLO;

						// iban e' vuoto se proviene dalla tabella rendicontazioni senza rpt;
						if(ibanAccredito.isEmpty())
							ibanAccredito = PAGAMENTI_SENZA_RPT;

						if(ibanAccredito != null) {
							List<it.govpay.model.EstrattoConto> estrattoContoPdf = null;
							if(pagamentiPerIban.containsKey(ibanAccredito)) {
								estrattoContoPdf = pagamentiPerIban.get(ibanAccredito);
							} else{
								estrattoContoPdf = new ArrayList<it.govpay.model.EstrattoConto>();
								pagamentiPerIban.put(ibanAccredito, estrattoContoPdf);
							}
							estrattoContoPdf.add(pagamentoExt);
						}
					}

					offset += lstEstrattoConto.size();
					lstEstrattoConto = estrattiContoBD.estrattoContoFromCodDominioIdVersamenti(estrattoConto.getDominio().getCodDominio(), estrattoConto.getIdVersamenti(), offset, LIMIT);
				}

				log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()
						+"], Versamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] aggregazione dei dati completata.");


				for (String ibanAccredito : pagamentiPerIban.keySet()) {
					log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()
							+"], Versamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] IBAN ["+ibanAccredito+"] scrittura PDF in corso...");
					String esitoGenerazione = null;
					List<it.govpay.model.EstrattoConto> estrattoContoPdf = pagamentiPerIban.get(ibanAccredito);

					log.debug("Generazione Estratto Conto in formato PDF per il Dominio ["+estrattoConto.getDominio().getCodDominio()+"] Versamenti ID [" 
							+ estrattoConto.getIdVersamenti() + " e per l'iban accredito ["+ibanAccredito+"]  ...");
					// creo nome file csv nel formato codDominio_IbanAccredito.csv
					String dominioPdfFileName = estrattoConto.getDominio().getCodDominio() +  "_"  + ibanAccredito + ".pdf";
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					Anagrafica anagraficaDominio = estrattoConto.getDominio().getAnagrafica(this);
					esitoGenerazione = EstrattoContoPdf.getPdfEstrattoConto(pathLoghi, estrattoConto.getDominio(),anagraficaDominio, null, null, ibanAccredito, estrattoContoPdf, baos,log);

					// salvo il risultato nella base dati
					estrattoConto.getOutput().put(dominioPdfFileName, baos);
					if(esitoGenerazione != null){
						log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()
								+"], Versamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] IBAN ["+ibanAccredito+"] scrittura PDF completata con Warning: "+esitoGenerazione+".");
					} else {
						log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()
								+"], Versamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] IBAN ["+ibanAccredito+"] scrittura PDF completata senza Warning.");
					}
				}

				log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()+"], Versamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] completata.");
			}

			log.debug("Generazione dei PDF estratto Conto in completata.");
			return inputEstrattoConto;
		}catch(Exception e){
			log.error(e,e);
			throw e;
		}
	}
	
	public List<it.govpay.core.business.model.EstrattoConto> getEstrattoContoPagamenti(List<it.govpay.core.business.model.EstrattoConto> inputEstrattoConto,String pathLoghi) throws Exception{
		log.debug("Generazione dei PDF estratto Conto in corso...");
		try{
			EstrattiContoBD estrattiContoBD = new EstrattiContoBD(this);

			for (it.govpay.core.business.model.EstrattoConto estrattoConto : inputEstrattoConto) {
				log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()+"], Pagamenti selezionati ["+ estrattoConto.getIdSingoliVersamenti() +"] in corso...");
				Map<String, List<it.govpay.model.EstrattoConto>> pagamentiPerIban = new HashMap<String, List<it.govpay.model.EstrattoConto>>();
				int offset = 0;

				log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()+"], Pagamenti selezionati ["+ estrattoConto.getIdSingoliVersamenti() +"] aggregazione dei dati in corso...");
				List<it.govpay.model.EstrattoConto> lstEstrattoConto = estrattiContoBD.estrattoContoFromCodDominioIdSingoliVersamenti(estrattoConto.getDominio().getCodDominio(), estrattoConto.getIdSingoliVersamenti(), offset, LIMIT); 

				while(lstEstrattoConto != null && !lstEstrattoConto.isEmpty()) {
					for (it.govpay.model.EstrattoConto pagamentoExt : lstEstrattoConto) {
						String ibanAccredito = pagamentoExt.getIbanAccredito();

						// l'iban e' null solo se ho una marca da bollo;
						if(ibanAccredito == null)
							ibanAccredito = MARCA_DA_BOLLO;

						// iban e' vuoto se proviene dalla tabella rendicontazioni senza rpt;
						if(ibanAccredito.isEmpty())
							ibanAccredito = PAGAMENTI_SENZA_RPT;

						if(ibanAccredito != null) {
							List<it.govpay.model.EstrattoConto> estrattoContoPdf = null;
							if(pagamentiPerIban.containsKey(ibanAccredito)) {
								estrattoContoPdf = pagamentiPerIban.get(ibanAccredito);
							} else{
								estrattoContoPdf = new ArrayList<it.govpay.model.EstrattoConto>();
								pagamentiPerIban.put(ibanAccredito, estrattoContoPdf);
							}
							estrattoContoPdf.add(pagamentoExt);
						}
					}

					offset += lstEstrattoConto.size();
					lstEstrattoConto = estrattiContoBD.estrattoContoFromCodDominioIdSingoliVersamenti(estrattoConto.getDominio().getCodDominio(), estrattoConto.getIdSingoliVersamenti(), offset, LIMIT);
				}

				log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()
						+"], Pagamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] aggregazione dei dati completata.");


				for (String ibanAccredito : pagamentiPerIban.keySet()) {
					log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()
							+"], Pagamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] IBAN ["+ibanAccredito+"] scrittura PDF in corso...");
					String esitoGenerazione = null;
					List<it.govpay.model.EstrattoConto> estrattoContoPdf = pagamentiPerIban.get(ibanAccredito);

					log.debug("Generazione Estratto Conto in formato PDF per il Dominio ["+estrattoConto.getDominio().getCodDominio()+"] Versamenti ID [" 
							+ estrattoConto.getIdVersamenti() + " e per l'iban accredito ["+ibanAccredito+"]  ...");
					// creo nome file csv nel formato codDominio_IbanAccredito.csv
					String dominioPdfFileName = estrattoConto.getDominio().getCodDominio() +  "_"  + ibanAccredito + ".pdf";
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					Anagrafica anagraficaDominio = estrattoConto.getDominio().getAnagrafica(this);
					esitoGenerazione = EstrattoContoPdf.getPdfEstrattoConto(pathLoghi, estrattoConto.getDominio(),anagraficaDominio, null, null, ibanAccredito, estrattoContoPdf, baos,log);

					// salvo il risultato nella base dati
					estrattoConto.getOutput().put(dominioPdfFileName, baos);
					if(esitoGenerazione != null){
						log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()
								+"], Pagamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] IBAN ["+ibanAccredito+"] scrittura PDF completata con Warning: "+esitoGenerazione+".");
					} else {
						log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()
								+"], Pagamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] IBAN ["+ibanAccredito+"] scrittura PDF completata senza Warning.");
					}
				}

				log.debug("Generazione dei PDF estratto Conto per il dominio ["+estrattoConto.getDominio().getCodDominio()+"], Pagamenti selezionati ["+ estrattoConto.getIdVersamenti() +"] completata.");
			}

			log.debug("Generazione dei PDF estratto Conto in completata.");
			return inputEstrattoConto;
		}catch(Exception e){
			log.error(e,e);
			throw e;
		}
	}

}
