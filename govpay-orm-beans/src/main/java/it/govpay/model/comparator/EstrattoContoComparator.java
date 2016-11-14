package it.govpay.model.comparator;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import it.govpay.model.EstrattoConto;

public class EstrattoContoComparator implements Comparator<EstrattoConto>{

	@Override
	public int compare(EstrattoConto o1, EstrattoConto o2) {
		//  cod_flusso_rendicontazione, iuv, data_pagamento
		String codFlussoRendicontazione1 = o1.getCodFlussoRendicontazione() != null ? o1.getCodFlussoRendicontazione() : "";
		String codFlussoRendicontazione2 = o2.getCodFlussoRendicontazione() != null ? o2.getCodFlussoRendicontazione() : "";
		
		
		if(StringUtils.equals(codFlussoRendicontazione1, codFlussoRendicontazione2)){
			String iuv1 = o1.getIuv() != null ? o1.getIuv() : "";
			String iuv2 = o2.getIuv() != null ? o2.getIuv() : "";
			
			if(StringUtils.equals(iuv1, iuv2)){
				Date dataPagamento1 = o1.getDataPagamento();
				
				if(dataPagamento1 == null)
					return -1;
				
				Date dataPagamento2 = o2.getDataPagamento();
				
				if(dataPagamento2 == null)
					return -1;
				
				dataPagamento1.compareTo(dataPagamento2);
			}
			
			return iuv1.compareTo(iuv2);
		}
				
		return codFlussoRendicontazione1.compareTo(codFlussoRendicontazione2); 
	}

}
