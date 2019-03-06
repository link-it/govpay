package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.DominioIndex;
import it.govpay.backoffice.v1.beans.Stazione;
import it.govpay.backoffice.v1.beans.StazioneIndex;
import it.govpay.backoffice.v1.beans.StazionePost;
import it.govpay.core.dao.anagrafica.dto.PutStazioneDTO;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.UriBuilderUtils;

public class StazioniConverter {

	public static PutStazioneDTO getPutStazioneDTO(StazionePost stazionePost, String idIntermediario, String idStazione, Authentication user) throws ServiceException, UnprocessableEntityException {
		PutStazioneDTO stazioneDTO = new PutStazioneDTO(user);
		
		it.govpay.bd.model.Stazione stazione = new it.govpay.bd.model.Stazione();

		stazione.setAbilitato(stazionePost.isAbilitato());
		int indexOfIdStazione = idStazione.indexOf("_");
		
		if(indexOfIdStazione == -1) {
			throw new UnprocessableEntityException("Il formato dell'IdStazione non e' valido, previsto IdIntermediario_ApplicationCode.");
		}
		
		String baseIdStazione = idStazione.substring(0, indexOfIdStazione);
		
		if(!baseIdStazione.equals(idIntermediario)) {
			throw new UnprocessableEntityException("Il formato dell'IdStazione non e' valido, IdIntermediario non presente all'interno dell'IdStazione.");
		}
		
		String applicationCodeS = idStazione.substring(indexOfIdStazione+1);
		int applicationCode = Integer.parseInt(applicationCodeS);
		
		if(applicationCode < 1 || applicationCode > 99)
			throw new UnprocessableEntityException("Identificativo Stazione deve avere un ApplicationCode compreso tra 01 e 99.");
		
		stazione.setApplicationCode(applicationCode); 
		stazione.setCodStazione(idStazione);
		stazione.setPassword(stazionePost.getPassword());
		
		stazioneDTO.setStazione(stazione);
		stazioneDTO.setIdIntermediario(idIntermediario);
		stazioneDTO.setIdStazione(idStazione);
		return stazioneDTO;		
	}
	
	public static Stazione toRsModel(it.govpay.bd.model.Stazione stazione, List<it.govpay.bd.model.Dominio> dominiLst) throws ServiceException {
		Stazione rsModel = new Stazione();
		rsModel.abilitato(stazione.isAbilitato())
		.idStazione(stazione.getCodStazione())
		.password(stazione.getPassword());

		if(dominiLst!=null) {
			List<DominioIndex> domini = new ArrayList<>();
			for(it.govpay.bd.model.Dominio dominio: dominiLst) {
				domini.add(DominiConverter.toRsModelIndex(dominio));
			}
			rsModel.setDomini(domini);
		}
		return rsModel;
	}
	
	public static StazioneIndex toRsModelIndex(it.govpay.bd.model.Stazione stazione) throws ServiceException {
		StazioneIndex rsModel = new StazioneIndex();
		rsModel.abilitato(stazione.isAbilitato())
		.idStazione(stazione.getCodStazione())
		.domini(UriBuilderUtils.getListDomini(stazione.getCodStazione()))
		.password(stazione.getPassword());

		return rsModel;
	}
}
