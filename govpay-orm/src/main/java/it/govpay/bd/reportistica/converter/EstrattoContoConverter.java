package it.govpay.bd.reportistica.converter;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import it.govpay.model.EstrattoConto;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento;
import it.govpay.model.Versamento.StatoVersamento;

public class EstrattoContoConverter {
	
	
	public static EstrattoConto getEstrattoContoFromResultSet(List<Object> list) throws UnsupportedEncodingException {
		EstrattoConto estrattoConto = new EstrattoConto();

		estrattoConto.setIdPagamento((Long) list.get(0)); //id_pagamento oppure id_rsr
		estrattoConto.setIdSingoloVersamento((Long) list.get(1)); // id singoloVersamento
		estrattoConto.setIdVersamento((Long) list.get(2)); // id_versamento
		estrattoConto.setDataPagamento((Date) list.get(3)); // data_pagamento
		estrattoConto.setImportoDovuto((Double) list.get(4)); // importo dovuto
		estrattoConto.setImportoPagato((Double) list.get(5)); // importo pagato
		estrattoConto.setIuv((String) list.get(6)); // iuv
		estrattoConto.setIur((String) list.get(7)); // iur1
		estrattoConto.setIdRegolamento((String) list.get(8)); // iur 2
		estrattoConto.setCodFlussoRendicontazione((String) list.get(9)); // cod_flusso_rendicontazione
		estrattoConto.setCodBicRiversamento((String) list.get(10)); //  codice_bic_riversamento
		estrattoConto.setCodVersamentoEnte((String) list.get(11)); // cod_versamento_ente
		estrattoConto.setStatoVersamento(StatoVersamento.valueOf((String) list.get(12))); // stato_versamento
		estrattoConto.setCodSingoloVersamentoEnte((String) list.get(13)); // cod_singolo_versamento_ente
		estrattoConto.setStatoSingoloVersamento(StatoSingoloVersamento.valueOf((String) list.get(14))); // stato_singolo_versamento
		estrattoConto.setIbanAccredito((String) list.get(15)); // iban_accredito
		estrattoConto.setDebitoreIdentificativo((String) list.get(16)); // cf_debitore
		estrattoConto.setNote((String) list.get(17)); // note
		estrattoConto.setCausale(Versamento.decode((String) list.get(18)).getSimple()); // causale

		return estrattoConto;
	}

	public static Object getObjectFromMap(Map<String,Object> map,String name){
		if(map==null){
			return null;
		}
		else if(map.containsKey(name)){
			Object o = map.get(name);
			if(o instanceof org.apache.commons.lang.ObjectUtils.Null){
				return null;
			}
			else{
				return o;
			}
		}
		else{
			return null;
		}
	}
 
}
