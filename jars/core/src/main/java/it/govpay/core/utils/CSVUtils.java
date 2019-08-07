package it.govpay.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
	
	private static CSVUtils instance;
	
	public static CSVUtils getInstance () {
		if(CSVUtils.instance == null)
			CSVUtils.instance = new CSVUtils();
		
		return CSVUtils.instance;
	}
	
	public static long countLines(byte[] tracciato) throws IOException {
		InputStream is = new BufferedInputStream(new ByteArrayInputStream(tracciato));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
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
	
	public static CSVRecord getCSVRecord(String csvEntry) {
		try {
			CSVParser p = CSVParser.parse(csvEntry, CSVFormat.RFC4180);
			return p.getRecords().get(0);
		} catch (IOException ioe) {
			return null;
		}
	}
	
	public static boolean isEmpty(CSVRecord record, int position) {
		try {
			return record.get(position).isEmpty(); 
		} catch (Throwable t) {
			return true;
		}
	}
	
	public static String toJsonValue(CSVRecord record, int position) {
		if(isEmpty(record, position)) 
			return "null";
		else
			return "\"" + record.get(position) +  "\"";
	}
	
	public static String toCsv(String ...strings) throws IOException {
		StringWriter writer = new StringWriter();
		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.RFC4180);
		printer.printRecord(Arrays.asList(strings));
		printer.flush();
		printer.close();
		return writer.toString();
	}
}
