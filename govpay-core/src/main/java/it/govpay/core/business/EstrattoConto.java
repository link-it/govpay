package it.govpay.core.business;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.rest.Pagamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.core.utils.CSVSerializerProperties;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.orm.Dominio;

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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.csv.Format;
import org.openspcoop2.utils.csv.FormatReader;
import org.openspcoop2.utils.csv.Printer;
import org.openspcoop2.utils.logger.beans.Property;

public class EstrattoConto extends BasicBD {

	private static final String CSV_SEPARATOR = "|";

	private static Logger log = LogManager.getLogger();

	private static final String IDENTIFICATIVO_VERSAMENTO_HEADER = "Identificativo Versamento";
	private static final String IMPORTO_PAGATO_HEADER = "Importo Pagato";
	private static final String DATA_PAGAMENTO_HEADER = "Data Pagamento";
	private static final String CODICE_RIVERSAMENTO_HEADER = "Codice Riversamento";
	private static final String CODICE_RENDICONTAZIONE_HEADER = "Codice Rendicontazione";
	private static final String NOTE_HEADER = "Note";
	private static final String BIC_RIVERSAMENTO_HEADER = "Bic Riversamento";
	private static final String ID_REGOLAMENTO_HEADER = "Id Regolamento";
	private static final String IBAN_ACCREDITO_HEADER = "Iban Accredito";
	private static final String IUV_HEADER = "Iuv";

	private static final int LIMIT = 50;

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

	public String estrattoConto() throws Exception {
		List<String> response = new ArrayList<String>();
		GpContext ctx = GpThreadLocal.get();

		FileOutputStream fos;
		Printer printer  = null;
		int numeroMesi = GovpayConfig.getInstance().getNumeroMesiEstrattoConto();
		ctx.getContext().getRequest().addGenericProperty(new Property("numeroMesi", numeroMesi+""));
		ctx.log("estrattoConto.inizioProcedura");

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

			// prelevo la lista dei domini registrati
			DominiBD dominiBD = new DominiBD(this);
			DominioFilter dominiFilter  = dominiBD.newFilter();
			List<FilterSortWrapper> filterSortList = new ArrayList<FilterSortWrapper>();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filterSortList.add(fsw);
			dominiFilter.setFilterSortList(filterSortList );
			List<it.govpay.bd.model.Dominio> domini =	dominiBD.findAll(dominiFilter);

			SimpleDateFormat f2 = new SimpleDateFormat("yyyy_MM");
			SimpleDateFormat f3 = new SimpleDateFormat("yyyy/MM");

			int giornoEsecuzione = GovpayConfig.getInstance().getGiornoEsecuzioneEstrattoConto();
			// Controllo se ho passato il giorno di esecuzione, altrimenti il mese scorso non lo voglio.
			Calendar c = Calendar.getInstance();
			boolean ignoraScorsoMese = c.get(Calendar.DAY_OF_MONTH) < giornoEsecuzione;

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

//					if(!dominioFile.exists()){
//						log.debug("creo il file CSV: "+dominioFile.getAbsolutePath());
//						dominioFile.createNewFile();
//					} else {
//						// se il file e' gia presente non lo creo;
//						creaEstrattoConto = false;
//					}
//					if(creaEstrattoConto){
						
						int offset = 0;

						try{
							
							if(!dominioFile.exists()){
								log.debug("creo il file CSV: "+dominioFile.getAbsolutePath());
								dominioFile.createNewFile();
								// per ogni mese fino al quello indicato  nelle properties calcolo l'estratto conto
								fos = new FileOutputStream(dominioFile);
								printer = new Printer(this.formatW , fos);
								printer.printRecord(getCsvHeader());
							} else {
								creaEstrattoConto = false;
							}

							List<it.govpay.bd.model.EstrattoConto> estrattoConto = this.getEstrattoContoExt(dominio.getCodDominio(), dataInizio, dataFine, offset, LIMIT);
							
							while(estrattoConto != null && !estrattoConto.isEmpty()) {
								List<FileOutputStream> fosList = new ArrayList<FileOutputStream>();
								List<String> fileEsistentiList = new ArrayList<String>();

								for (it.govpay.bd.model.EstrattoConto pagamentoExt : estrattoConto) {
									List<String> csvRow = this.getCsvRow(pagamentoExt);
									if(creaEstrattoConto)
										printer.printRecord(csvRow);
									
									if(pagamentoExt.getIbanAccredito() != null) {
										Printer printerIban;
										if(ecPerIban.containsKey(pagamentoExt.getIbanAccredito())) {
											printerIban = ecPerIban.get(pagamentoExt.getIbanAccredito());
											printerIban.printRecord(csvRow);
										} else {
											String dominioPerIbanCsvFileName = dominio.getCodDominio() + "_" + pagamentoExt.getIbanAccredito() + "_" + f2.format(dataInizio) +".csv";
											log.debug("Nome del file CSV destinazione: "+dominioPerIbanCsvFileName);

											File dominioPerIbanFile = new File(basePath+File.separator + dominio.getCodDominio() + File.separator + dominioPerIbanCsvFileName );
											
											if(!fileEsistentiList.contains(dominioPerIbanFile.getPath())) {
												if(!dominioPerIbanFile.exists()){
													sb.append("Generazione estratto conto per il mese " + f3.format(dataInizio) + " e per l'iban accredito ["+pagamentoExt.getIbanAccredito()+"] eseguita" + CSV_SEPARATOR);
													log.debug("creo il file CSV: "+dominioPerIbanFile.getAbsolutePath());
													dominioPerIbanFile.createNewFile();
													FileOutputStream fosIban = new FileOutputStream(dominioPerIbanFile);
													fosList.add(fosIban);
	
													printerIban = new Printer(this.formatW , fosIban);
													printerIban.printRecord(getCsvHeader());
	
													ecPerIban.put(pagamentoExt.getIbanAccredito(), printerIban);
													printerIban.printRecord(csvRow);
												} else {
													fileEsistentiList.add(dominioPerIbanFile.getPath());
													sb.append("Generazione estratto conto per il mese " + f3.format(dataInizio) + " e per l'iban accredito ["+pagamentoExt.getIbanAccredito()+"] non eseguita in quanto il file ["+dominioPerIbanFile.getPath()+"] esiste gia."+CSV_SEPARATOR);
												}
											}

										}
										
									}
								}
								
								offset += estrattoConto.size();
								estrattoConto = this.getEstrattoContoExt(dominio.getCodDominio(), dataInizio, dataFine, offset, LIMIT);
							}
							
							if(creaEstrattoConto) {
								sb.append("Generazione estratto conto per il mese " + f3.format(dataInizio) + " eseguita correttamente")
								.append("#serializzati "+offset+" pagamenti sul file "+dominioCsvFileName+".");
								ctx.log("estrattoConto.fineDominioMeseOk", denominazioneDominio, f3.format(dataInizio), estrattoConto.size() +"" ,dominioCsvFileName);
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

	public List<it.govpay.bd.model.rest.Pagamento> getEstrattoConto(String codDominio, Date dataInizio, Date dataFine, Integer offset, Integer limit) throws Exception{
		PagamentiBD pagamentiBD = new PagamentiBD(this);
		PagamentoFilter filter = pagamentiBD.newFilter();
		filter.setOffset(offset );
		filter.setLimit(limit); 
		filter.setCodDominio(codDominio);
		FilterSortWrapper fsw = new FilterSortWrapper();
		fsw.setField(it.govpay.orm.Pagamento.model().DATA_ACQUISIZIONE);
		fsw.setSortOrder(SortOrder.DESC);
		filter.getFilterSortList().add(fsw );
		filter.setDataInizio(dataInizio);
		filter.setDataFine(dataFine);

		List<Pagamento> findAll = pagamentiBD.estrattoConto(filter);
				
		return findAll;
	}

	public List<it.govpay.bd.model.EstrattoConto> getEstrattoContoExt(String codDominio, Date dataInizio, Date dataFine, Integer offset, Integer limit) throws Exception{
		PagamentiBD pagamentiBD = new PagamentiBD(this);
		List<it.govpay.bd.model.EstrattoConto> findAll = pagamentiBD.estrattoConto(codDominio, dataInizio, dataFine, offset, limit);
		return findAll;
	}

	private List<String> getCsvHeader(){
		List<String> header = new ArrayList<String>();
		header.add(IDENTIFICATIVO_VERSAMENTO_HEADER);
		header.add(IUV_HEADER);
		header.add(IMPORTO_PAGATO_HEADER);
		header.add(DATA_PAGAMENTO_HEADER);
		header.add(CODICE_RIVERSAMENTO_HEADER);
		header.add(CODICE_RENDICONTAZIONE_HEADER);
		header.add(NOTE_HEADER);
		header.add(BIC_RIVERSAMENTO_HEADER);
		header.add(ID_REGOLAMENTO_HEADER);
		header.add(IBAN_ACCREDITO_HEADER);

		return header;
	}

	private List<String> getCsvRow(it.govpay.bd.model.EstrattoConto pagamento){


		List<String> oneLine = new ArrayList<String>();

		oneLine.add(pagamento.getCodSingoloVersamentoEnte());
		oneLine.add(pagamento.getIuv());
		oneLine.add(pagamento.getImportoPagato()+"");
		if(pagamento.getDataPagamento() != null)
			oneLine.add(this.sdf.format(pagamento.getDataPagamento()));
		else 
			oneLine.add("");
		oneLine.add(pagamento.getIur());
		oneLine.add(pagamento.getCodFlussoRendicontazione());
		oneLine.add(pagamento.getNote());
		oneLine.add(pagamento.getCodBicRiversamento());
		oneLine.add(pagamento.getIdRegolamento());
		oneLine.add(pagamento.getIbanAccredito());

		return oneLine;
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
			it.govpay.bd.model.Dominio dominio  = dominiBD.getDominio(codDominio);

			String denominazioneDominio = dominio.getRagioneSociale()+" ("+dominio.getCodDominio()+")";

			// 1 controllo esistenza directory con nome codDominio
			File dominioDir = new File(basePath+File.separator + dominio.getCodDominio());
			if(!dominioDir.exists()){
				log.debug("creo directory per il dominio ["+denominazioneDominio+"]: "+dominioDir.getAbsolutePath());
				dominioDir.mkdir();
			}

			for (File file : dominioDir.listFiles()) {
				String fileName = FilenameUtils.removeExtension(file.getName()); 
				response.add(fileName);
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
			it.govpay.bd.model.Dominio dominio  = dominiBD.getDominio(codDominio);

			String denominazioneDominio = dominio.getRagioneSociale()+" ("+dominio.getCodDominio()+")";

			// 1 controllo esistenza directory con nome codDominio
			File dominioDir = new File(basePath+File.separator + dominio.getCodDominio());
			if(!dominioDir.exists()){
				throw new Exception("Directory di contenente gli Estratto Conto per il dominio ["+denominazioneDominio+"] non presente.");
			}

			String fileToDownload = basePath+File.separator + dominio.getCodDominio()+ File.separator + nomeFile;

			// per ora si gestisce solo il formato CSV
			File estrattoConto = new File((fileToDownload + ".csv"));

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

}
