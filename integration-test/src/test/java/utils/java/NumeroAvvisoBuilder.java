package utils.java;

import java.util.concurrent.atomic.AtomicLong;

public class NumeroAvvisoBuilder {

	private static final AtomicLong COUNTER = new AtomicLong();

	public static long successivo() {
		long ms = System.currentTimeMillis() % 10_000_000L;
		long seq = COUNTER.incrementAndGet() % 1000L;
		return ms * 1000L + seq;
	}
	
	public static String newNumeroAvviso(String idStazione, String auxDigit, Integer segregationCode, String prefix) throws Exception {
		return getNumeroAvviso(idStazione, auxDigit, segregationCode, prefix, successivo());
	}
	
	public static String getNumeroAvviso(String idStazione, String auxDigit, Integer segregationCode, String prefix, long reference) throws Exception {
		
		if(!prefix.isEmpty()) Integer.parseInt(prefix);
		
		String applicationCodeS = idStazione.substring(12);
		
		if(auxDigit.equals("0")) {
			return new NumeroAvvisoFactory().build_aux0(Integer.parseInt(applicationCodeS), prefix, reference);
		}
		
		if(auxDigit.equals("1")) {
			return new NumeroAvvisoFactory().build_aux1(prefix, reference);
		}
		
		if(auxDigit.equals("2")) {
			return new NumeroAvvisoFactory().build_aux2(prefix, reference);
		}
		
		if(auxDigit.equals("3")) {
			return new NumeroAvvisoFactory().build_aux3(segregationCode, prefix, reference);
		}
		
		throw new Exception("Aux digit "+auxDigit+" non supportato");
		
	}
	
	public static String extractIuv(String numeroAvviso) throws Exception {
		return new NumeroAvvisoFactory().getIuvFromNumeroAvviso(numeroAvviso);
	}

}
