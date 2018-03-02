package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Intermediario;
import it.govpay.rs.v1.beans.base.IntermediarioPost;

public class IntermediariConverter {

	public static PutIntermediarioDTO getPutIntermediarioDTO(IntermediarioPost intermediarioPost, String idIntermediario, IAutorizzato user) throws ServiceException {
		PutIntermediarioDTO dominioDTO = new PutIntermediarioDTO(user);
		
		Intermediario intermediario = new Intermediario();
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
}
