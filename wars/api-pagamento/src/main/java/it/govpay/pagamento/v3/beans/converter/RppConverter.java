package it.govpay.pagamento.v3.beans.converter;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.pagamento.v3.beans.Rpp;
import it.govpay.pagamento.v3.beans.RppIndex;
import it.govpay.pagamento.v3.beans.Rpps;
import it.govpay.pagamento.v3.api.impl.PendenzeApiServiceImpl;
import it.govpay.pagamento.v3.api.impl.TransazioniApiServiceImpl;

public class RppConverter {

	public static Rpps toRsModel(List<LeggiRptDTOResponse>results, int offset, int limit, long totalResults, UriInfo uriInfo) throws ServiceException {
		Rpps rpps = new Rpps();
		List<RppIndex> items = new ArrayList<>();
		ConverterUtils.popolaLista(rpps, uriInfo.getRequestUriBuilder(), results.size(), offset, limit, totalResults);
		
		for (LeggiRptDTOResponse dto : results) {
			items.add(toRsModelIndex(dto.getRpt(), dto.getVersamento(), dto.getApplicazione()));
		}

		rpps.setItems(items);
		
		return rpps;
	}
	
	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.viste.model.VersamentoIncasso versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toPendenzaIndex(versamento));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));
		
		return rsModel;
	}
	
	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.viste.model.VersamentoIncasso versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		RppIndex rsModel = new RppIndex();

		String idA2A = applicazione.getCodApplicazione();
		String idPendenza = versamento.getCodVersamentoEnte();
		String idDominio = rpt.getCodDominio();
		String iuv = rpt.getIuv();
		String ccp = rpt.getCcp();
		rsModel.setHref(TransazioniApiServiceImpl.basePath.clone().path(idDominio).path(iuv).path(ccp).build().toString());
		rsModel.setPendenza(PendenzeApiServiceImpl.basePath.clone().path(idA2A).path(idPendenza).build().toString());
		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));
		
		return rsModel;
	}
}
