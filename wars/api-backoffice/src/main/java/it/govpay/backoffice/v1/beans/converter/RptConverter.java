package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.backoffice.v1.beans.Rpp;
import it.govpay.backoffice.v1.beans.RppIndex;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.rs.v1.ConverterUtils;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt) throws ServiceException {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(rpt.getVersamento()));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));
		rsModel.setBloccante(rpt.isBloccante());
		
		if(rpt.getPagamentoPortale() != null) {
			if(rpt.getPagamentoPortale().getTipo() == 1) {
				rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.ENTE);	
			} else if(rpt.getPagamentoPortale().getTipo() == 3) {
				rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.PSP);
			}
		}

		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt) throws ServiceException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(rpt.getVersamento()));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt));
		rsModel.setRt(ConverterUtils.getRtJson(rpt));
		rsModel.setBloccante(rpt.isBloccante());
		
		if(rpt.getIdPagamentoPortale() != null) {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			if(rpt.getPagamentoPortale(configWrapper).getTipo() == 1) {
				rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.ENTE);	
			} else if(rpt.getPagamentoPortale(configWrapper).getTipo() == 3) {
				rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.PSP);
			}
		}
		
		return rsModel;
	}
}
