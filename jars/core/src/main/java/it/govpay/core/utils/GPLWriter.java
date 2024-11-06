package it.govpay.core.utils;

import java.io.*;

public class GPLWriter {

	private static int profonditaCheck = 1000;

	public static void main(String[] args) {
		try {

			String dir = System.getProperty("sourcedir");
			String file = System.getProperty("sourcefile");

			if((dir == null && file == null) || (dir != null && file != null)) {
				System.out.println("Usage: java -D[sourcedir=directory | -Dsourcefile=file] [-Dgpl=LICENSE_FILE] [-Ddepth=PROFONDITA] "+GPLWriter.class.getName());
				return;
			}

			String realFile = (dir != null) ? dir : file;
			String gplFile = System.getProperty( "gpl", "LICENSE" );
			profonditaCheck = Integer.parseInt(System.getProperty( "depth", "1000" ));
			writeGPLDichiarazione(new File(realFile),1, getGPL(gplFile));
		} catch(Exception ex) {
			ex.printStackTrace();
			System.err.println("Errore generale: " + ex);
		}

	}


	public static void writeGPLDichiarazione(File f,int profondita, byte[] GPL) {
		try {
			if(f.isFile()){
				if(f.getName().endsWith(".java")){

					// Get Bytes Originali
					try (FileInputStream fis =new FileInputStream(f);
							ByteArrayOutputStream byteInputBuffer = new ByteArrayOutputStream();){
						byte [] readB = new byte[8192];
						int readByte = 0;
						while((readByte = fis.read(readB))!= -1){
							String data = new String(readB, 0, readByte);

							// Verifica se la stringa particolare è presente
							if (data.contains("GovPay - Porta di Accesso al Nodo dei Pagamenti SPC")) {
								return; // Interrompe l'esecuzione del metodo
							}

							byteInputBuffer.write(readB,0,readByte);
						}

						try(FileOutputStream fos =new FileOutputStream(f);){
							fos.write(GPL);
							fos.write(byteInputBuffer.toByteArray());
							fos.flush();
						}
					}
				}
			}else{
				if( (profondita++) <= profonditaCheck){
					File [] fChilds = f.listFiles();
					for (int i = 0; i < fChilds.length; i++) {
						writeGPLDichiarazione(fChilds[i],(profondita++), GPL);
					}
				}
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			System.err.println("Errore writeGPLDichiarazione: " + ex);
		}

	}


	private static byte[] getGPL(String file) throws Exception {
		File f = new File(file);
		if (!f.exists()) {
			throw new Exception("Il file ["+file+"]da usare come licenza GPL non esiste");
		}
		try(FileInputStream fis =new FileInputStream(f);
				ByteArrayOutputStream byteInputBuffer = new ByteArrayOutputStream();){
			byte [] readB = new byte[8192];
			int readByte = 0;
			while((readByte = fis.read(readB))!= -1){
				byteInputBuffer.write(readB,0,readByte);
			}
			return byteInputBuffer.toByteArray();
		}
	}
}