/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.model.comparator;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.govpay.model.EstrattoConto;

public class EstrattoContoComparator implements Comparator<EstrattoConto>{

	@Override
	public int compare(EstrattoConto o1, EstrattoConto o2) {
		//  cod_flusso_rendicontazione, iuv, data_pagamento
		String codFlussoRendicontazione1 = EstrattoContoComparator.getCodFlussoAsString(o1.getCodFlussoRendicontazione());
		String codFlussoRendicontazione2 = EstrattoContoComparator.getCodFlussoAsString(o2.getCodFlussoRendicontazione());
		
		
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

	public static String getCodFlussoAsString(List<String> codFlussoRendicontazione) {
		if(codFlussoRendicontazione != null && codFlussoRendicontazione.size() > 0) {
			StringBuilder sb = new StringBuilder(); 
			for (String string : codFlussoRendicontazione) {
				if(sb.length() > 0)
					sb.append(", ");
				
				sb.append(string);
			}
		}
		
		return "";
	}
}
