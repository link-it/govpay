/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.console.pagamenti.gde.exporter;

import it.govpay.ndp.model.EventiInterfacciaModel.Evento;
import it.govpay.ndp.model.EventiInterfacciaModel.Infospcoop;
import it.govpay.web.console.pagamenti.bean.EventoBean;
import it.govpay.web.console.pagamenti.form.EventiSearchForm;
import it.govpay.web.console.pagamenti.iservice.IEventiService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.server.ExportException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.web.form.CostantiForm;

import com.lowagie.text.Phrase;


/*****
 * 
 * Exporter per gli eventi selezionati dall'interfaccia grafica.
 * 
 * 
 * @author pintori
 *
 */
public class SingleFileExporter {



	private static Logger log = LogManager.getLogger(SingleFileExporter.class);

	private ZipOutputStream zip;
	private String fileName=null;

	private boolean enableHeaderInfo = false;
	private boolean mimeThrowExceptionIfNotFound = false;

	private String formatoExport = null;

	private IEventiService eventiService = null;


	public String getFileName(){
		return this.fileName;
	}

	public boolean getEnableHeaderInfo(){
		return this.enableHeaderInfo;
	}

	private SingleFileExporter(ExporterProperties properties, IEventiService eventiService){
		this.enableHeaderInfo = properties.isEnableHeaderInfo();
		this.mimeThrowExceptionIfNotFound = properties.isMimeThrowExceptionIfNotFound();

		this.formatoExport = properties.getFormatoExport();

		this.eventiService = eventiService;

		SingleFileExporter.log.info("Single File Exporter inizializzato:");
		SingleFileExporter.log.info("\t -MimeType handling (mime.throwExceptionIfMappingNotFound):"+this.mimeThrowExceptionIfNotFound);
	}

	public SingleFileExporter(OutputStream outstream,ExporterProperties properties, IEventiService eventiService){
		this(properties, eventiService);

		this.zip = new ZipOutputStream(outstream);
	}

	public SingleFileExporter(File destFile,ExporterProperties properties, IEventiService eventiService) throws Exception{
		this(properties, eventiService);

		FileOutputStream fos = new FileOutputStream(destFile);

		this.zip = new ZipOutputStream(fos);
		this.fileName = destFile.getName();
		SingleFileExporter.log.info("\n\t -Esportazione su file:"+destFile.getAbsolutePath());
	}

	public SingleFileExporter(String pathToFile,ExporterProperties properties, IEventiService eventiService) throws Exception{
		this(new File(pathToFile), properties, eventiService);
	}

	public void export() throws ExportException {

		Date startTime = Calendar.getInstance().getTime();

		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

			int start = 0;
			int limit = 100;

			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
			SingleFileExporter.log.debug("Avvio esportazione ...");
			SingleFileExporter.log.debug("Inizio esportazione alle:"+time.format(startTime));



			List<EventoBean> listEventi = this.eventiService.findAll(start, limit);

			String rootDir = "Eventi"+File.separatorChar;

			// inserisco una unica entry
			if(this.formatoExport.equalsIgnoreCase("pdf"))
				this.zip.putNextEntry(new ZipEntry(rootDir+"Eventi.pdf"));
			else  if(this.formatoExport.equalsIgnoreCase("csv"))
				this.zip.putNextEntry(new ZipEntry(rootDir+"Eventi.csv"));

			try{
				//				this.zip.putNextEntry(new ZipEntry(rootDir+"SearchFilter.xml"));
				//				writeSearchFilterXml((EventiSearchForm)this.eventiService.getSearch(),this.zip);
				//				this.zip.flush();
				//				this.zip.closeEntry();

				while(listEventi.size()>0){

					export(rootDir,listEventi);

					start+=limit;

					listEventi = this.eventiService.findAll(start, limit);
				}

				// chiusura entry
				this.zip.flush();
				this.zip.closeEntry();

				//chiusura zip

				this.zip.flush();
				this.zip.close();
			}catch(IOException ioe){
				String msg = "Si e' verificato un errore durante l'esportazione degli eventi";
				msg+=" Non sono riuscito a creare il file SearchFilter.xml ("+ioe.getMessage()+")";
				SingleFileExporter.log.error(msg,ioe);
				throw new ExportException(msg, ioe);
			}

			Date dataFine = Calendar.getInstance().getTime();

			SingleFileExporter.log.debug("Fine esportazione alle:"+formatter.format(dataFine));
			SingleFileExporter.log.debug("Esportazione completata.");

		}catch(ExportException e){
			SingleFileExporter.log.error("Errore durante esportazione su file",e);
			throw e;
		}catch(Exception e){
			SingleFileExporter.log.error("Errore durante esportazione su file",e);
			throw new ExportException("Errore durante esportazione su file", e);
		}

	}

	public void export(List<String> idEventi)  throws ExportException{

		Date startTime = Calendar.getInstance().getTime();
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
			SingleFileExporter.log.debug("Avvio esportazione ...");
			SingleFileExporter.log.debug("Inizio esportazione alle:"+time.format(startTime));
			List<EventoBean> transazioni = new ArrayList<EventoBean>();

			for (String id : idEventi) {
				transazioni.add(this.eventiService.findById(Long.parseLong(id)));
			}

			String rootDir = "Eventi"+File.separatorChar;

			if(this.formatoExport.equalsIgnoreCase("pdf"))
				this.zip.putNextEntry(new ZipEntry(rootDir+"Eventi.pdf"));
			else  if(this.formatoExport.equalsIgnoreCase("csv"))
				this.zip.putNextEntry(new ZipEntry(rootDir+"Eventi.csv"));

			export(rootDir,transazioni);

			// chiusura entry
			this.zip.flush();
			this.zip.closeEntry();

			Date dataFine = Calendar.getInstance().getTime();

			// chiusura zip

			this.zip.flush();
			this.zip.close();

			SingleFileExporter.log.debug("Fine esportazione alle:"+formatter.format(dataFine));
			SingleFileExporter.log.debug("Esportazione completata.");

		}catch(ExportException e){
			SingleFileExporter.log.error("Errore durante esportazione su file",e);
			throw e;
		}catch(Exception e){
			SingleFileExporter.log.error("Errore durante esportazione su file",e);
			throw new ExportException("Errore durante esportazione su file", e);
		}

	}

	private void export(String rootDir, List<EventoBean> eventi) throws ExportException{

		byte[] buf = new byte[1024];

		InputStream in = null;

		if(this.formatoExport.equalsIgnoreCase("csv")){

			for(EventoBean evtBean: eventi){

				try{

					StringBuffer sb = new StringBuffer();
					String newLine =  "\n"  ;

					Evento evento = evtBean.getDTO();
					sb.append("Evento Id["+evento.getId()+"]:").append(newLine);

					if(evento.getData()  != null){
						sb.append(newLine).append("Data registrazione").append(",").append(""+evento.getData());
					}
					//riga 1
					if(evento.getDominio()  != null){
						sb.append(newLine).append("Id Dominio").append(",").append(evento.getDominio());
					}
					//riga 1
					if(evento.getIuv()  != null){
						sb.append(newLine).append("IUV").append(",").append(evento.getIuv());
					}
					//riga 1
					if(evento.getCcp()  != null){
						sb.append(newLine).append("CCP").append(",").append(evento.getCcp());
					}
					//riga 1
					if(evento.getPsp()  != null){
						sb.append(newLine).append("Id PSP").append(",").append(evento.getPsp());
					}
					//riga 1
					if(evento.getTipoVersamento()  != null){
						sb.append(newLine).append("Tipo Versamento").append(",").append(evento.getTipoVersamento());
					}
					//riga 1
					if(evento.getComponente()  != null){
						sb.append(newLine).append("Componente").append(",").append(evento.getComponente());
					}
					//riga 1
					if(evento.getCategoria()  != null){
						sb.append(newLine).append("Categoria Evento").append(",").append(evento.getCategoria().toString());
					}
					//riga 1
					if(evento.getTipo() != null){
						sb.append(newLine).append("Tipo Evento").append(",").append(evento.getTipo());
					}
					//riga 1
					if(evento.getSottoTipo()  != null){
						sb.append(newLine).append("Sottotipo Evento").append(",").append(evento.getSottoTipo());
					}
					//riga 1
					if(evento.getFruitore()  != null){
						sb.append(newLine).append("Id Fruitore").append(",").append(evento.getFruitore());
					}
					//riga 1
					if(evento.getErogatore()  != null){
						sb.append(newLine).append("Id Erogatore").append(",").append(evento.getErogatore());
					}
					//riga 1
					if(evento.getStazioneIntermediarioPA() != null){
						sb.append(newLine).append("Id Stazione Intermediario PA").append(",").append(evento.getStazioneIntermediarioPA());
					}
					//riga 1
					if(evento.getCanalePagamento()  != null){
						sb.append(newLine).append("Canale Pagamento").append(",").append(evento.getCanalePagamento());
					}
					//riga 1
					if(evento.getParametri()  != null){
						sb.append(newLine).append("Parametri Specifici Interfaccia").append(",").append(evento.getParametri());
					}

					if(evento.getEsito() != null){
						sb.append(newLine).append("Esito").append(",").append(evento.getEsito());
					}


					Infospcoop infospcoop = evtBean.getInfospcoop() != null ? evtBean.getInfospcoop().getDTO() : null;
					if(infospcoop!= null){
						//			if(evento.getCategoria().equals(Categoria.INTERFACCIA) && evento.getIdEgov()!= null){


						//						Infospcoop infospcoop = this.eventiService.getInfospcoopByIdEgov(evento.getIdEgov());

						sb.append(newLine).append(newLine).append("Infospcoop: ");
						sb.append(newLine).append("IdEgov").append(",").append(infospcoop.getIdEgov());
						// riga 2
						sb.append(newLine).append("Soggetto Erogatore").append(",").append (infospcoop.getTipoSoggettoErogatore() + "/" + infospcoop.getSoggettoErogatore());
						// riga 3
						sb.append(newLine).append("Soggetto Fruitore").append(",").append (infospcoop.getTipoSoggettoFruitore() + "/" + infospcoop.getSoggettoFruitore());
						// riga 4
						sb.append(newLine).append("Servizio").append(",").append (infospcoop.getTipoServizio() + "/" + infospcoop.getServizio());
						// riga 5
						sb.append(newLine).append("Azione").append(",").append (new Phrase(infospcoop.getAzione()));

						sb.append(newLine).append(newLine);
						//						.append(newLine).append("Infospcoop: ").append(newLine).append(infospcoop.toJson());

					}
					//					}

					// aggiungo lo spazio tra le entry
					sb.append(newLine).append(newLine);

					in = new ByteArrayInputStream(sb.toString().getBytes());
					int len;
					while ((len = in.read(buf)) > 0) {
						this.zip.write(buf, 0, len);
					}

					//				this.zip.flush();
					//				this.zip.closeEntry();
					if(in!=null)
						in.close();
				}catch(IOException ioe){
					String msg = "Si e' verificato un errore durante l'esportazione dell'evento con id:"+evtBean.getDTO().getId();
					msg+=" Non sono riuscito a creare il file .csv ("+ioe.getMessage()+")";
					throw new ExportException(msg, ioe);
				}catch(Exception e){
					String msg = "Si e' verificato un errore durante l'esportazione dell'evento con id:"+evtBean.getDTO().getId();
					msg+=" Non sono riuscito a creare il file .csv ("+e.getMessage()+")";
					throw new ExportException(msg, e);
				}

			}
		} else 	if(this.formatoExport.equalsIgnoreCase("pdf")){
			try{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				PdfExporter.exportAsPdf(eventi, baos, this.eventiService);

				in = new ByteArrayInputStream(baos.toByteArray());
				int len;
				while ((len = in.read(buf)) > 0) {
					this.zip.write(buf, 0, len);
				}

				if(in!=null)
					in.close();

			}catch(Exception e){
				String msg = "Si e' verificato un errore durante l'esportazione degli eventi";
				msg+=" Non sono riuscito a creare il file .pdf ("+e.getMessage()+")";
				throw new ExportException(msg, e);
			}
		}
	}

	public static void writeSearchFilterXml(EventiSearchForm searchForm,
			List<String> idEventi, Integer totEventi, OutputStream out) throws IOException{

		StringBuffer sb = new StringBuffer();
		//		out.w

		sb.append("Parametri di ricerca utilizzati: \n");

		sb.append("\nData Inizio: ");
		if(searchForm.getData().getValue() != null)
			sb.append(searchForm.getData().getValue());
		else
			sb.append("-");

		sb.append(";");

		sb.append("\nData Fine: ");
		if(searchForm.getData().getValue2() != null)
			sb.append(searchForm.getData().getValue2()) ;
		else
			sb.append("-");

		sb.append(";");

		sb.append("\nId Dominio: ");
		if(searchForm.getDominio().getValue() != null)
			sb.append(searchForm.getDominio().getValue()) ;
		else
			sb.append("-");

		sb.append(";");

		sb.append("\nIUV: ");
		if(searchForm.getIuv().getValue() != null)
			sb.append(searchForm.getIuv().getValue()) ;
		else
			sb.append("-");

		sb.append(";");
		
		sb.append("\nCCP: ");
		if(searchForm.getCcp().getValue() != null)
			sb.append(searchForm.getCcp().getValue()) ;
		else
			sb.append("-");

		sb.append(";");

		sb.append("\nCategoria Evento: ");
		String v = searchForm.getCategoria().getValue() != null ? searchForm.getCategoria().getValue().getLabel() : CostantiForm.NON_SELEZIONATO;
		
		if(v != null && !v.equals(CostantiForm.ALL))
			sb.append(v) ;
		else
			sb.append("Qualsiasi");

		sb.append(";");

		sb.append("\nTipo Evento: ");
		v = searchForm.getTipo().getValue() != null ? searchForm.getTipo().getValue().getLabel() : CostantiForm.NON_SELEZIONATO;
		
		if(v != null && !v.equals(CostantiForm.ALL))
			sb.append(v) ;
		else
			sb.append("Qualsiasi");

		sb.append(";");

		sb.append("\nSottotipo Evento: ");
		v = searchForm.getSottoTipo().getValue() != null ? searchForm.getSottoTipo().getValue().getLabel() : CostantiForm.NON_SELEZIONATO;
		
		if(v != null && !v.equals(CostantiForm.ALL))
			sb.append(v) ;
		else
			sb.append("Qualsiasi");

		sb.append(";");

		if(idEventi != null && totEventi != null){
			sb.append("\n\nSono stati selezionati ").append(idEventi.size()).append(" eventi su ").append(totEventi).append(" trovati tramite questa ricerca.");
		}

		out.write(sb.toString().getBytes());
	}


	public static void writeSearchFilterXml(EventiSearchForm searchForm,
			OutputStream out) throws IOException{
		writeSearchFilterXml(searchForm, null, null, out);
	}
}
