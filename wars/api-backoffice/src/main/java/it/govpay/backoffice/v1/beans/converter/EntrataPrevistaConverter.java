package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.EntrataPrevistaIndex;

public class EntrataPrevistaConverter {

	
	public static EntrataPrevistaIndex toRsModelIndex(it.govpay.bd.viste.model.EntrataPrevista entrataPrevista) throws ServiceException {
		EntrataPrevistaIndex rsModel = new EntrataPrevistaIndex();
		
		rsModel.setDataPagamento(entrataPrevista.getDataPagamento());
		rsModel.setDataRegolamento(entrataPrevista.getDataRegolamento());
		rsModel.setIdA2A(entrataPrevista.getCodApplicazione());
		rsModel.setIdDominio(entrataPrevista.getCodDominio());
		rsModel.setIdFlusso(entrataPrevista.getCodFlusso());
		rsModel.setIdPendenza(entrataPrevista.getCodVersamentoEnte());
		rsModel.setIdVocePendenza(entrataPrevista.getCodSingoloVersamentoEnte());
		rsModel.setImportoPagato(entrataPrevista.getImportoPagato());
		if(entrataPrevista.getImportoTotalePagamenti() != null)
			rsModel.setImportoTotale(entrataPrevista.getImportoTotalePagamenti().doubleValue());
		else 
			rsModel.setImportoTotale(0.0d);
		
		rsModel.setIndice(new BigDecimal(entrataPrevista.getIndiceDati()));
		rsModel.setIur(entrataPrevista.getIur());
		rsModel.setIuv(entrataPrevista.getIuv());
		rsModel.setNumeroPagamenti(new BigDecimal(entrataPrevista.getNumeroPagamenti()));
		rsModel.setTrn(entrataPrevista.getFrIur());
		
		if(entrataPrevista.getAnno() != null)
		rsModel.setAnno(new BigDecimal(entrataPrevista.getAnno()));
		
		rsModel.setIdTipoPendenza(entrataPrevista.getCodTipoVersamento());
		rsModel.setIdEntrata(entrataPrevista.getCodEntrata());
		rsModel.setIdentificativoDebitore(entrataPrevista.getIdentificativoDebitore());
		
		
		return rsModel;
	}
}
