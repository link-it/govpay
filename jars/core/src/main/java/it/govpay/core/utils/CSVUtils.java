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

public class CSVUtils {

	private CSVFormat csvFormat = CSVFormat.RFC4180;

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


	public CSVRecord getCSVRecord(String csvEntry) {
		try {
			CSVParser p = CSVParser.parse(csvEntry, csvFormat);
			return p.getRecords().get(0);
		} catch (IOException ioe) {
			return null;
		}
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

}
