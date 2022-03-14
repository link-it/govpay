package it.govpay.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.IbanAccredito;

public class CSVUtils {

	private CSVFormat csvFormat = CSVFormat.RFC4180;
	private static org.slf4j.Logger log = LoggerWrapperFactory.getLogger(CSVUtils.class);

	public CSVUtils(CSVFormat csvFormat) {
		this.csvFormat = csvFormat;
	}

	public static CSVUtils getInstance() {
		return new CSVUtils(CSVFormat.DEFAULT);
	}

	public static CSVUtils getInstance(CSVFormat csvFormat) {
		if(csvFormat != null)
			return new CSVUtils(csvFormat);
		else
			return new CSVUtils(CSVFormat.RFC4180);
	}

	public static List<byte[]> splitCSV(byte[] tracciato, long skip) throws IOException {

		ByteArrayInputStream in = new ByteArrayInputStream(tracciato);
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);

		List<byte[]> lst = new ArrayList<byte[]>(); 

		while(br.ready()) {
			if(skip > 0) {
				br.readLine();
				skip--;
			} else {
				lst.add(br.readLine().getBytes());
			}
		}
		return lst;
	}


	public CSVRecord getCSVRecord(String csvEntry) throws IOException {
		log.debug("Parsing del record CSV: [" + csvEntry + "] [Delimiter: " + csvFormat.getDelimiter() +" Escape:"+csvFormat.getEscapeCharacter()+"]");
		CSVParser p = CSVParser.parse(csvEntry, csvFormat);
		CSVRecord r = p.getRecords().get(0);
		if(log.isDebugEnabled()) {
			log.debug("Parsing completed:" );
			for(int i=0; i < r.size(); i++) {
				log.debug(i + ": [" + r.get(i) + "]");
			}
		}
		return r ;
	}

	public boolean isEmpty(CSVRecord record, int position) {
		try {
			return record.get(position).isEmpty(); 
		} catch (Throwable t) {
			return true;
		}
	}

	public String toJsonValue(CSVRecord record, int ... positions) {
		String collage = "";

		for(int position : positions) {
			if(!isEmpty(record, position)) {
				collage += record.get(position) + " ";
			}
		}

		if(collage.trim().isEmpty())
			return "null";
		else
			return "\"" + collage.trim() +  "\"";
	}

	public String toCsv(String ...strings) throws IOException {
		StringWriter writer = new StringWriter();
		CSVPrinter printer = new CSVPrinter(writer, csvFormat);
		printer.printRecord(Arrays.asList(strings));
		printer.flush();
		printer.close();
		
		String string = writer.toString();
		return string;
	}

	public static long countLines(byte[] tracciato) throws IOException {
		try (ByteArrayInputStream in = new ByteArrayInputStream(tracciato); 
				InputStreamReader isr = new InputStreamReader(in); 
				BufferedReader br = new BufferedReader(isr);
				){
			int lines = 0;
			while (br.readLine() != null) {
				lines++;
			}
			return lines;
		} finally {
		}
	}

	public boolean isOperazioneAnnullamento(CSVRecord record, int position) {
		try {
			String importoS = record.get(position);
			
			if(!importoS.isEmpty()) {
				double parseDouble = Double.parseDouble(importoS);
				return parseDouble == 0d;
			}
			
			return false; 
		} catch (Throwable t) {
			return false;
		}
	}
	
	public String getCodDocumento(CSVRecord record, int position) {
		try {
			String codDocumento = record.get(position);
			return codDocumento; 
		} catch (Throwable t) {
			return null;
		}
	}
	
	public String getDelimiter() {
		return String.valueOf(this.csvFormat.getDelimiter());
	}
	
	public String getCodDominioFromIbanAccredito(CSVRecord record, int ... positions) throws ServiceException, NotFoundException {
		String collage = "";

		for(int position : positions) {
			if(!isEmpty(record, position)) {
				collage += record.get(position) + " ";
			}
		}
		
		if(collage.trim().isEmpty())
			return "null";
		else
			return "\"" + getCodDominioFromIbanAccredito(collage.trim()) +  "\"";
	}
	public String getCodDominioFromIbanAccredito(String codIban) throws ServiceException, NotFoundException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		IbanAccredito ibanAccredito = AnagraficaManager.getIbanAccredito(configWrapper, codIban);
		return ibanAccredito.getDominio(configWrapper).getCodDominio();
	}
}
