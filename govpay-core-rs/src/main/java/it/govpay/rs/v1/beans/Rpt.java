package it.govpay.rs.v1.beans;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Applicazione;
import it.govpay.rs.v1.beans.base.EsitoRpt;
import it.govpay.rs.v1.beans.base.ModelloPagamento;

public class Rpt  extends it.govpay.rs.v1.beans.base.Rpt{

public Rpt() {}
	
	@Override
	public String getJsonIdFilter() {
		return "rpt";
	}
	
	public static Rpt parse(String json) {
		return (Rpt) parse(json, Rpt.class);
	}
	
	
	public Rpt(it.govpay.bd.model.Rpt rpt, Versamento versamento, Applicazione applicazione, Canale canale, Psp psp) throws ServiceException {
		
		this.setCanale(UriBuilderUtils.getCanale(psp.getCodPsp(), canale.getCodCanale()));
		this.setCcp(rpt.getCcp());
		this.setDataRicevuta(rpt.getDataMsgRicevuta());
		this.setDataRichiesta(rpt.getDataMsgRichiesta());
		this.setDettaglioStato(rpt.getDescrizioneStato());
		if(rpt.getEsitoPagamento() != null) {
			EsitoRpt eRpt =null;
			
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
			this.setEsito(eRpt);
		}
		this.setIdDominio(rpt.getCodDominio());
		this.setImporto(rpt.getImportoTotalePagato());
		this.setIuv(rpt.getIuv());
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
			
			this.setModelloPagamento(mod);
		}
		this.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte()));
		this.setStato(rpt.getStato().toString());
	}
}
