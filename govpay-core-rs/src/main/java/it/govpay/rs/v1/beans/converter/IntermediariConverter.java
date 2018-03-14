package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTO;
import it.govpay.core.rs.v1.beans.Intermediario;
import it.govpay.core.rs.v1.beans.base.IntermediarioPost;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.IAutorizzato;

public class IntermediariConverter {

	public static PutIntermediarioDTO getPutIntermediarioDTO(IntermediarioPost intermediarioPost, String idIntermediario, IAutorizzato user) throws ServiceException {
		PutIntermediarioDTO dominioDTO = new PutIntermediarioDTO(user);
		
		it.govpay.model.Intermediario intermediario = new it.govpay.model.Intermediario();
		intermediario.setAbilitato(intermediarioPost.isAbilitato());
		intermediario.setCodIntermediario(idIntermediario);
		if(intermediarioPost.getServizioPagoPa() != null) {
			intermediario.setConnettorePdd(ConnettoriConverter.getConnettore(intermediarioPost.getServizioPagoPa()));
			intermediario.getConnettorePdd().setPrincipal(intermediarioPost.getPrincipalPagoPa());

		}
		intermediario.setDenominazione(intermediarioPost.getDenominazione());
		
		
		dominioDTO.setIntermediario(intermediario);
		dominioDTO.setIdIntermediario(idIntermediario);
		return dominioDTO;		
	}
	
	
	public static Intermediario toRsModel(it.govpay.model.Intermediario i) throws ServiceException {
		Intermediario rsModel = new Intermediario();
		rsModel.abilitato(i.isAbilitato())
		.denominazione(i.getDenominazione())
		.idIntermediario(i.getCodIntermediario())
		.principalPagoPa(i.getConnettorePdd().getPrincipal())
		.servizioPagoPa(ConnettoriConverter.toRsModel(i.getConnettorePdd()))
		.stazioni(UriBuilderUtils.getStazioni(i.getCodIntermediario()));
		
		return rsModel;
	}
}
