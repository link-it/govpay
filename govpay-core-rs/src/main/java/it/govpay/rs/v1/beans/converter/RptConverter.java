package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.beans.Rpt;
import it.govpay.core.rs.v1.beans.base.EsitoRpt;
import it.govpay.core.rs.v1.beans.base.ModelloPagamento;
import it.govpay.core.utils.UriBuilderUtils;

public class RptConverter {


	public static Rpt toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione, it.govpay.bd.model.Canale canale, it.govpay.bd.model.Psp psp) throws ServiceException {
		Rpt rsModel = new Rpt();
		
		rsModel.setCanale(UriBuilderUtils.getCanale(psp.getCodPsp(), canale.getCodCanale(), canale.getTipoVersamento().getCodifica()));
		rsModel.setCcp(rpt.getCcp());
		rsModel.setDataRicevuta(rpt.getDataMsgRicevuta());
		rsModel.setDataRichiesta(rpt.getDataMsgRichiesta());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());

		EsitoRpt eRpt  = EsitoRpt.IN_CORSO;
		if(rpt.getEsitoPagamento() != null) {
			switch (rpt.getEsitoPagamento()) {
			case DECORRENZA_TERMINI:
				eRpt = EsitoRpt.DECORRENZA;
				break;
			case DECORRENZA_TERMINI_PARZIALE:
				eRpt = EsitoRpt.DECORENNZA_PARZIALE;
				break;
			case PAGAMENTO_ESEGUITO:
				eRpt = EsitoRpt.ESEGUITO;
				break;
			case PAGAMENTO_NON_ESEGUITO:
				eRpt = EsitoRpt.NON_ESEGUITO;
				break;
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				eRpt = EsitoRpt.ESEGUITO_PARZIALE;
				break;
			}
		}
		rsModel.setEsito(eRpt);

		rsModel.setIdDominio(rpt.getCodDominio());
		rsModel.setIuv(rpt.getIuv());
		if(canale.getModelloPagamento() != null) {
			ModelloPagamento mod = null;
			switch(canale.getModelloPagamento()) {
			case ATTIVATO_PRESSO_PSP:
				mod = ModelloPagamento._4;
				break;
			case DIFFERITO:
				mod = ModelloPagamento._2;
				break;
			case IMMEDIATO:
				mod = ModelloPagamento._0;
				break;
			case IMMEDIATO_MULTIBENEFICIARIO:
				mod = ModelloPagamento._1;
				break;
			default:
				break;

			}

			rsModel.setModelloPagamento(mod);
		}
		rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte()));
		rsModel.setStato(rpt.getStato().toString());
		
		return rsModel;
	}
}
