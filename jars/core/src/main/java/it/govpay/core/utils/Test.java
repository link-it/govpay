package it.govpay.core.utils;

import java.nio.charset.Charset;
import java.text.Normalizer;

public class Test {

	public static void main(String[] args) {
//		System.out.println("RESULT: " + IncassoUtils.getRiferimentoIncasso(" YYY17032021                SisalPay S.p.A. VIA A.DI TOCQUEVILLE 13 MILANO MI ID11101210760133349                   606260015422752021 RI1/PUR/LGPE-RIVERSAMENTO/URI/2021-03-17SIGPITM1XXX-S005147952 BONIFICO A VOSTRO FAVORE TRN UNCRITMM 1101210760133349 DA SisalPay S.p.A. PER /PUR/LGPE-RIVERSAMENTO/URI/20 21-03-17SIGPITM1XXX-S005147952"));
//		System.out.println("RESULT: " + IncassoUtils.getRiferimentoIncasso(" YYY17032021                SisalPay S.p.A. VIA A.DI TOCQUEVILLE 13 MILANO MI ID11101210760133349                   606260015422752021 RI1/PUR/LGPE-RIVERSAMENTO/URI/2021-0 3-17SIGPITM1XXX-S005147952 BONIFICO A VOSTRO FAVORE TRN UNCRITMM 1101210760133349 DA SisalPay S.p.A. PER /PUR/LGPE-RIVERSAMENTO/URI/2021-03-17SIGPITM1XXX-S005147952"));
//		System.out.println("RESULT: " + IncassoUtils.getRiferimentoIncasso("/PUR/LGPE-RIVERSAMENTO/URI/2021-03-17SIGPITM1XXX-S005147952"));
//		System.out.println("RESULT: " + IncassoUtils.getRiferimentoIncasso("/PUR/LGPE-RIVERSAMENTO/URI/2021-04-07GovPAYPsp1-153107788"));
	
		String causale = "                                   ZZ1000000000000000,00   000000000012931,00EUR000000000000000,00   0000000000000000000000,000000000000,00 ZZ2SATISPAY EUROPE SA ZZ3COMUNE DI BRESCIA                                 /PUR/LGPE-RIVERSAMENTO/URI/2021-07-21SATYLUL1-67394095 /ZZ4/45322700/ZZ4/ ID1FEE73BE0E8ED11EB98F602F53F9A9825   4218D73FDC07422CA829C8F49154D27E";
		System.out.println(IncassoUtils.getRiferimentoIncasso(causale));
		System.out.println(IncassoUtils.getRiferimentoIncasso(causale.replaceAll("(\\s?\\/ZZ4\\/)", "")));
		
	}
}
