package it.govpay.core.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.govpay.bd.reportistica.EstrattoContoCostanti;
import it.govpay.model.EstrattoConto;

public class CSVUtils {
	
	public static List<String> getEstrattoContoCsvHeader(){
		List<String> header = new ArrayList<String>();
		
		header.add(EstrattoContoCostanti.COD_VERSAMENTO_HEADER);
		header.add(EstrattoContoCostanti.COD_SINGOLO_VERSAMENTO_HEADER);
		header.add(EstrattoContoCostanti.IUV_HEADER);
		header.add(EstrattoContoCostanti.CODICE_FISCALE_DEBITORE_HEADER);
		header.add(EstrattoContoCostanti.IMPORTO_DOVUTO_HEADER);
		header.add(EstrattoContoCostanti.IMPORTO_PAGATO_HEADER);
		header.add(EstrattoContoCostanti.DATA_PAGAMENTO_HEADER);
		header.add(EstrattoContoCostanti.STATO_SINGOLO_VERSAMENTO_HEADER);
		header.add(EstrattoContoCostanti.IUR_HEADER);
		header.add(EstrattoContoCostanti.CODICE_RENDICONTAZIONE_HEADER);
		header.add(EstrattoContoCostanti.BIC_RIVERSAMENTO_HEADER);
		header.add(EstrattoContoCostanti.ID_REGOLAMENTO_HEADER);
		header.add(EstrattoContoCostanti.IBAN_ACCREDITO_HEADER);
		header.add(EstrattoContoCostanti.CAUSALE_HEADER);
		header.add(EstrattoContoCostanti.NOTE_HEADER);

		return header;
	}
	 
	public static List<String> getEstrattoContoAsCsvRow(EstrattoConto pagamento, SimpleDateFormat sdf) throws Exception{
		List<String> oneLine = new ArrayList<String>();
		// CodVersamentoEnte
		if(StringUtils.isNotEmpty(pagamento.getCodVersamentoEnte()))
			oneLine.add(pagamento.getCodVersamentoEnte());
		else 
			oneLine.add("");
		// CodSingoloVersamentoEnte
		if(StringUtils.isNotEmpty(pagamento.getCodSingoloVersamentoEnte()))
			oneLine.add(pagamento.getCodSingoloVersamentoEnte());
		else 
			oneLine.add("");
		// Iuv
		if(StringUtils.isNotEmpty(pagamento.getIuv()))
			oneLine.add(pagamento.getIuv());
		else 
			oneLine.add("");
		// CF Debitore
		if(StringUtils.isNotEmpty(pagamento.getDebitoreIdentificativo()))
			oneLine.add(pagamento.getDebitoreIdentificativo());
		else 
			oneLine.add("");
		//Importo Dovuto
		if(pagamento.getImportoDovuto() != null)
			oneLine.add(pagamento.getImportoDovuto().doubleValue()+"");
		else 
			oneLine.add("");
		// Importo Pagato
		if(pagamento.getImportoPagato() != null)
			oneLine.add(pagamento.getImportoPagato().doubleValue()+"");
		else 
			oneLine.add("");
		// Data Pagamento 
		if(pagamento.getDataPagamento() != null)
			oneLine.add(sdf.format(pagamento.getDataPagamento()));
		else 
			oneLine.add("");
		// Stato Singolo Versamento
		if(pagamento.getStatoSingoloVersamento() != null)
			oneLine.add(pagamento.getStatoSingoloVersamento().toString());
		else 
			oneLine.add("");
		// p.IUR
		if(StringUtils.isNotEmpty(pagamento.getIur()))
			oneLine.add(pagamento.getIur());
		else 
			oneLine.add("");
		// Codice Rendicontazione
		if(StringUtils.isNotEmpty(pagamento.getCodFlussoRendicontazione()))
			oneLine.add(pagamento.getCodFlussoRendicontazione());
		else 
			oneLine.add("");
		//BicRiversamento
		if(StringUtils.isNotEmpty(pagamento.getCodBicRiversamento()))
			oneLine.add(pagamento.getCodBicRiversamento());
		else 
			oneLine.add("");
		// Id Regolamento
		if(StringUtils.isNotEmpty(pagamento.getIdRegolamento()))
			oneLine.add(pagamento.getIdRegolamento());
		else 
			oneLine.add("");
		// IBAN
		if(StringUtils.isNotEmpty(pagamento.getIbanAccredito()))
			oneLine.add(pagamento.getIbanAccredito());
		else 
			oneLine.add("");		
		// Causale
		if(StringUtils.isNotEmpty(pagamento.getCausale()))
			oneLine.add(pagamento.getCausale());
		else 
			oneLine.add("");
		// Note
		if(StringUtils.isNotEmpty(pagamento.getNote()))
			oneLine.add(pagamento.getNote());
		else 
			oneLine.add("");

		return oneLine;
	}
}
