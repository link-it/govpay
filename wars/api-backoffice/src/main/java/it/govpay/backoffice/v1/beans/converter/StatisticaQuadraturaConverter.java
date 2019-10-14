package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.StatisticaQuadratura;

public class StatisticaQuadraturaConverter {

	
	public static StatisticaQuadratura toRsModelIndex(it.govpay.bd.reportistica.statistiche.model.StatisticaRiscossione statistica) throws ServiceException {
		StatisticaQuadratura rsModel = new StatisticaQuadratura();
		
		if(statistica.getApplicazione(null) != null)
			rsModel.setApplicazione(ApplicazioniConverter.toRsModelIndex(statistica.getApplicazione(null)));
		rsModel.setData(statistica.getData());
		rsModel.setDataA(statistica.getDataA());
		rsModel.setDataDa(statistica.getDataDa());
		rsModel.setDettaglio(null);
		rsModel.setDirezione(statistica.getDirezione());
		rsModel.setDivisione(statistica.getDivisione());
		if(statistica.getDominio(null) != null)
			rsModel.setDominio(DominiConverter.toRsModelIndex(statistica.getDominio(null)));
		if(statistica.getImporto() != null)
			rsModel.setImporto(statistica.getImporto());
		if(statistica.getNumeroPagamenti() != null)
			rsModel.setNumeroPagamenti(new BigDecimal(statistica.getNumeroPagamenti()));
		if(statistica.getTipoVersamento(null) != null)
			rsModel.setTipoPendenza(TipiPendenzaConverter.toTipoPendenzaRsModelIndex(statistica.getTipoVersamento(null)));
		if(statistica.getUo(null) != null)
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModelIndex(statistica.getUo(null)));
		
		return rsModel;
	}
}

