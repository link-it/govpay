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
package it.govpay.ejb.core.utils;

public final class IdUtils {
	
	private static volatile long counter = 0;
	
	private static final int minTrustedDigits = 14;
	
	private static int TRANSACTION_ID_LENGTH = 15;
	
	/**
	 * Genera un ID univoco di lunghezza fissa il cui numero di cifre e impostato dal parametro di ingresso desiredLength (per cui sono ammessi solo i valori 14, 15 e 16).
	 * Non aggiunge il CRC.
	 * 
	 * Se si sceglie di generare un ID a 16 cifre e garantita l'univocita per 317 anni, con 15 cifre e garantita l'univocita per 31 anni, 
	 * mentre se si sceglie un ID a 14 cifre il codice generato sara univoco solo per 3 anni.
	 * 
	 * @param desiredLength numero di cifre dell'ID univoco da generare (non comprende il CRC)
	 * @return un ID univoco di lunghezza fissa il cui numero di cifre e impostato dal parametro di ingresso desiredLength senza CRC
	 */
	private static final synchronized String generateID(int desiredLength)	{	
		
		if (desiredLength < minTrustedDigits)
			throw new IllegalStateException("impossibile garantire univocita per ID con meno di "+minTrustedDigits+" cifre");
		
		String currentTime = String.valueOf(System.currentTimeMillis());
		
		String key = currentTime.substring(2 + minTrustedDigits - desiredLength) + fillLeftWithDigits(3, counter++ % 1000,'0');
		
		return  key; 		 
		
	}	
	
	public static String generaCodTransazione(){
		
		String key = IdUtils.generateID(TRANSACTION_ID_LENGTH);
		
		String keyWithCRC = key + IdUtils.calculateCRC(key);
		
		return keyWithCRC;
	}
	
	private static String fillLeftWithDigits(long desideredLength, long initialValue, char filler){
		String valueString = String.valueOf(initialValue);
		
		StringBuffer sb = new StringBuffer();
		
		while ( (sb.length() + valueString.length()) < desideredLength)
			sb.append(filler);
		
		sb.append(valueString);
		return sb.toString();		
	}
	
	/**
	 * Calcola il CRC di una stringa di sole cifre in base all'algoritmo Eu-Pay.
	 * 
	 * Secondo le specifiche Eu-Pay, il check-digit di una stringa costituita di sole cifre,
	 * si calcola sommando le cifre pari (pos. 2,4,6,… SP), sommando le cifre dispari (pos 1,3,5,… SD), calcolando S = SP + SD * 3. 
	 * Il CRC è il più piccolo numero che sommato a S consente di ottenere un numero S1 multiplo di 10 (S1 MOD 10 = 0).
	 *
	 * @param digitsToCheck la stringa di sole cifre di cui calcolare il check digit secondo l'algoritmo Eu-Pay.
	 * @return il Check Digit della stringa di sole cifre ricevuta in ingresso.
	 */
	private static String calculateCRC(String digitsToCheck) {
		
		int sumOdd = 0;
		
		int sumEven = 0;
		
		for(int i = digitsToCheck.length()-1; i >= 0; i--){
			
			char c = digitsToCheck.charAt(i);
			
			Integer num = Integer.parseInt(Character.toString(c));
			
			if(isOdd(digitsToCheck.length() - i))
				sumOdd += num;
			else 
				sumEven += num;
		}
		
		int sumTot = sumEven + (sumOdd * 3);
		
		int crcInt;
		
		for(crcInt = 0; ((sumTot + crcInt) % 10) != 0; crcInt++);
		
		return String.valueOf(crcInt);
	}
	
	private static boolean isOdd(int i) {
		
		return (i % 2) != 0;
		
	}
	
}
