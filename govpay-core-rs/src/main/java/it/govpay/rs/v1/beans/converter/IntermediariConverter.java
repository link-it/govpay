package it.govpay.rs.v1.beans.converter;

import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTO;
import it.govpay.model.Connettore;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Intermediario;
import it.govpay.rs.v1.beans.base.Connector;
import it.govpay.rs.v1.beans.base.IntermediarioPost;

public class IntermediariConverter {

	public static PutIntermediarioDTO getPutIntermediarioDTO(IntermediarioPost intermediarioPost, String idIntermediario, IAutorizzato user) {
		PutIntermediarioDTO dominioDTO = new PutIntermediarioDTO(user);
		
		Intermediario intermediario = new Intermediario();
		intermediario.setAbilitato(intermediarioPost.isAbilitato());
		intermediario.setCodIntermediario(idIntermediario);
		Connettore connettorePdd = new Connettore();
		connettorePdd.setPrincipal(intermediarioPost.getPrincipalPagoPa());
		if(intermediarioPost.getServizioPagoPa() != null) {
			Connector servizioPagoPa = intermediarioPost.getServizioPagoPa();
			connettorePdd.setUrl(servizioPagoPa.getUrl());
			// properties
			servizioPagoPa.getAuth(); // TODO ????
			
			intermediario.setConnettorePdd(connettorePdd );
		}
		intermediario.setDenominazione(intermediarioPost.getDenominazione());
		
		
		dominioDTO.setIntermediario(intermediario);
		dominioDTO.setIdIntermediario(idIntermediario);
		return dominioDTO;		
	}
}
