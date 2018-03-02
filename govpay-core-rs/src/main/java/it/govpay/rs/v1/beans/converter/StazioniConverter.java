package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Stazione;
import it.govpay.core.dao.anagrafica.dto.PutStazioneDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.base.StazionePost;

public class StazioniConverter {

	public static PutStazioneDTO getPutStazioneDTO(StazionePost stazionePost, String idIntermediario, String idStazione, IAutorizzato user) throws ServiceException {
		PutStazioneDTO stazioneDTO = new PutStazioneDTO(user);
		
		Stazione stazione = new Stazione();

		stazione.setAbilitato(stazionePost.isAbilitato());
		stazione.setApplicationCode(stazione.getApplicationCode());
		stazione.setCodStazione(idStazione);
		stazione.setPassword(stazionePost.getPassword());
		
		stazioneDTO.setStazione(stazione);
		stazioneDTO.setIdIntermediario(idIntermediario);
		stazioneDTO.setIdStazione(idStazione);
		return stazioneDTO;		
	}
}
