package it.govpay.backoffice.v1.beans.converter;

import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Intermediario;
import it.govpay.backoffice.v1.beans.IntermediarioIndex;
import it.govpay.backoffice.v1.beans.IntermediarioPost;
import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTO;

public class IntermediariConverter {

	public static PutIntermediarioDTO getPutIntermediarioDTO(IntermediarioPost intermediarioPost, String idIntermediario, Authentication user) {
		PutIntermediarioDTO putIntermediarioDTO = new PutIntermediarioDTO(user);

		it.govpay.model.Intermediario intermediario = new it.govpay.model.Intermediario();
		if(intermediarioPost.isAbilitato()!=null)
			intermediario.setAbilitato(intermediarioPost.isAbilitato());

		intermediario.setCodIntermediario(idIntermediario);
		if(intermediarioPost.getServizioPagoPa() != null) {
			intermediario.setConnettorePdd(ConnettorePagopaConverter.getConnettore(intermediarioPost.getServizioPagoPa()));

		}
		intermediario.setDenominazione(intermediarioPost.getDenominazione());

		if(intermediarioPost.getServizioFtp()!=null) {
			intermediario.setConnettoreSftp(ConnettoreSftpConverter.getConnettore(intermediarioPost.getServizioFtp(), idIntermediario));
		}

		intermediario.setPrincipal(intermediarioPost.getPrincipalPagoPa());
		intermediario.setPrincipalOriginale(intermediarioPost.getPrincipalPagoPa());

		putIntermediarioDTO.setIntermediario(intermediario);
		putIntermediarioDTO.setIdIntermediario(idIntermediario);

		return putIntermediarioDTO;
	}


	public static Intermediario toRsModel(it.govpay.model.Intermediario i) {
		Intermediario rsModel = new Intermediario();
		rsModel.abilitato(i.isAbilitato())
		.denominazione(i.getDenominazione())
		.idIntermediario(i.getCodIntermediario())
		.principalPagoPa(i.getPrincipalOriginale())
		.servizioPagoPa(ConnettorePagopaConverter.toRsModel(i.getConnettorePdd()));

		if(i.getConnettoreSftp()!=null)
			rsModel.setServizioFtp(ConnettoreSftpConverter.toRsModel(i.getConnettoreSftp()));

		return rsModel;
	}

	public static IntermediarioIndex toRsModelIndex(it.govpay.model.Intermediario i) {
		IntermediarioIndex rsModel = new IntermediarioIndex();
		rsModel.abilitato(i.isAbilitato())
		.denominazione(i.getDenominazione())
		.idIntermediario(i.getCodIntermediario());

		return rsModel;
	}
}
