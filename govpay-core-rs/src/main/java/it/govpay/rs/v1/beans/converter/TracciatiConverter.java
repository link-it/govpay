package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.beans.base.Tracciato;

public class TracciatiConverter {


	public static Tracciato toRsModel(it.govpay.model.Tracciato tracciato) throws ServiceException {
		Tracciato rsModel = new Tracciato();

		rsModel.setId(BigDecimal.valueOf(tracciato.getId()));
		rsModel.setCodDominio(tracciato.getCodDominio());
		rsModel.setTipo(tracciato.getTipo().toString());
		rsModel.setStato(tracciato.getStato().toString());
		rsModel.setDescrizioneStato(tracciato.getDescrizioneStato());
		rsModel.setDataCaricamento(tracciato.getDataCaricamento());
		rsModel.setDataCompletamento(tracciato.getDataCompletamento());
		rsModel.setBeanDati(tracciato.getBeanDati());
		rsModel.setFilenameRichiesta(tracciato.getFileNameRichiesta());
		rsModel.setFilenameEsito(tracciato.getFileNameEsito());

		return rsModel;
	}
}
