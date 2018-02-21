package it.govpay.rs.v1.beans;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.UriBuilderUtils;
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
	
	
	public Rpt(it.govpay.bd.model.Rpt rpt) throws ServiceException {
		Canale can = rpt.getCanale(null);
		Psp psp = rpt.getPsp(null);
		
		this.setCanale(UriBuilderUtils.getCanale(psp.getCodPsp(), can.getCodCanale()));
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
		if(can.getModelloPagamento() != null) {
			ModelloPagamento mod = null;
			switch(can.getModelloPagamento()) {
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
		Versamento versamento = rpt.getVersamento(null);
		this.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(versamento.getApplicazione(null).getCodApplicazione(), versamento.getCodVersamentoEnte()));
		this.setStato(rpt.getStato().toString());
	}
}
