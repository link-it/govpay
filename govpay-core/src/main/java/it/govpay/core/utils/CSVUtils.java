package it.govpay.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {

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
	
	public static List<byte[]> splitCSV(byte[] tracciato, long skip) throws Exception {

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
}
