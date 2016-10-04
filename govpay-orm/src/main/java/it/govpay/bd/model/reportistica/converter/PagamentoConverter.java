package it.govpay.bd.model.reportistica.converter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.reportistica.PagamentiBD;
import it.govpay.model.Versamento;
import it.govpay.model.Versamento.StatoVersamento;

public class PagamentoConverter  extends it.govpay.bd.model.rest.converter.PagamentoConverter{


	public static it.govpay.model.reportistica.Pagamento toRestDTO(Map<String, Object> map) throws ServiceException {
		it.govpay.model.reportistica.Pagamento dto = new it.govpay.model.reportistica.Pagamento();

		try{

			Long id = (Long) getObjectFromMap(map, PagamentiBD.ALIAS_ID);
			dto.setId(id);

			String codVersamentoEnte = (String) getObjectFromMap(map, PagamentiBD.ALIAS_COD_VERSAMENTO_ENTE);
			dto.setCodSingoloVersamentoEnte(codVersamentoEnte);

			String iuv = (String) getObjectFromMap(map, PagamentiBD.ALIAS_IUV);
			dto.setIuv(iuv);

			String codiceFiscaleDebitore = (String) getObjectFromMap(map, PagamentiBD.ALIAS_CODICE_FISCALE_DEBITORE);
			dto.setDebitoreIdentificativo(codiceFiscaleDebitore);

			String causale = (String) getObjectFromMap(map, PagamentiBD.ALIAS_CAUSALE);
			dto.setCausale(Versamento.decode(causale).getSimple()); 

			String statoVersamento = (String) getObjectFromMap(map, PagamentiBD.ALIAS_STATO_VERSAMENTO);
			dto.setStatoVersamento(StatoVersamento.valueOf(statoVersamento));

			Date data= null;
			Object objectFromMap = getObjectFromMap(map, PagamentiBD.ALIAS_DATA_PAGAMENTO);
			if(objectFromMap != null){
				if(objectFromMap  instanceof Date)
					data= (Date) objectFromMap;
				if(objectFromMap  instanceof Long)
					data= new Date ((Long) objectFromMap);
			}
			dto.setDataPagamento(data);

			Double importoDovuto = (Double) getObjectFromMap(map, PagamentiBD.ALIAS_IMPORTO_DOVUTO);
			if(importoDovuto != null)
				dto.setImportoDovuto(BigDecimal.valueOf(importoDovuto));

			Double importoPagato = (Double) getObjectFromMap(map, PagamentiBD.ALIAS_IMPORTO_PAGATO);
			if(importoPagato != null)
				dto.setImportoPagato(BigDecimal.valueOf(importoPagato));

		}catch(Exception e){
			throw new ServiceException(e);
		}

		return dto;
	}
}
