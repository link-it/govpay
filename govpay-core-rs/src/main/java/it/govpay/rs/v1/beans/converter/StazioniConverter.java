package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutStazioneDTO;
import it.govpay.core.rs.v1.beans.Stazione;
import it.govpay.core.rs.v1.beans.base.StazionePost;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.IAutorizzato;

public class StazioniConverter {

	public static PutStazioneDTO getPutStazioneDTO(StazionePost stazionePost, String idIntermediario, String idStazione, IAutorizzato user) throws ServiceException {
		PutStazioneDTO stazioneDTO = new PutStazioneDTO(user);
		
		it.govpay.bd.model.Stazione stazione = new it.govpay.bd.model.Stazione();

		stazione.setAbilitato(stazionePost.isAbilitato());
		stazione.setApplicationCode(stazione.getApplicationCode());
		stazione.setCodStazione(idStazione);
		stazione.setPassword(stazionePost.getPassword());
		
		stazioneDTO.setStazione(stazione);
		stazioneDTO.setIdIntermediario(idIntermediario);
		stazioneDTO.setIdStazione(idStazione);
		return stazioneDTO;		
	}
	
	public static Stazione toRsModel(it.govpay.bd.model.Stazione stazione) throws ServiceException {
		Stazione rsModel = new Stazione();
		rsModel.abilitato(stazione.isAbilitato())
		.domini(UriBuilderUtils.getListDomini(stazione.getCodStazione()))
		.idStazione(stazione.getCodStazione())
		.password(stazione.getPassword());
		
		return rsModel;
	}
}
