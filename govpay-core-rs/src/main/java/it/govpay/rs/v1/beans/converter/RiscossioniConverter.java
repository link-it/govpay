package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Pagamento;
import it.govpay.core.rs.v1.beans.Riscossione;
import it.govpay.core.utils.UriBuilderUtils;

public class RiscossioniConverter {
	
	public static Riscossione toRsModel(Pagamento input) {
		Riscossione rsModel = new Riscossione();
		try {
			rsModel.setIdDominio(input.getCodDominio());
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			
			rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(input.getSingoloVersamento(null).getVersamento(null).getApplicazione(null).getCodApplicazione(), input.getSingoloVersamento(null).getVersamento(null).getCodVersamentoEnte()));
			rsModel.setIdVocePendenza(input.getSingoloVersamento(null).getCodSingoloVersamentoEnte());
			rsModel.setRpp(UriBuilderUtils.getRppByDominioIuvCcp(input.getRpt(null).getCodDominio(), input.getRpt(null).getIuv(), input.getRpt(null).getCcp()));
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setIbanAccredito(input.getIbanAccredito());
			rsModel.setData(input.getDataPagamento());
			rsModel.setCommissioni(input.getCommissioniPsp());
//			rsModel.setAllegato(input.getAllegato()); //TODO
		} catch(ServiceException e) {}

		return rsModel;
	}
	
}
