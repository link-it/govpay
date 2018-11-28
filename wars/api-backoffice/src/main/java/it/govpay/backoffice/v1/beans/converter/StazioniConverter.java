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
import it.govpay.core.utils.UriBuilderUtils;

public class StazioniConverter {

	public static PutStazioneDTO getPutStazioneDTO(StazionePost stazionePost, String idIntermediario, String idStazione, Authentication user) throws ServiceException {
		PutStazioneDTO stazioneDTO = new PutStazioneDTO(user);
		
		it.govpay.bd.model.Stazione stazione = new it.govpay.bd.model.Stazione();

		stazione.setAbilitato(stazionePost.isAbilitato());
		String applicationCodeS = idStazione.substring(idStazione.indexOf("_")+1);
		stazione.setApplicationCode(Integer.parseInt(applicationCodeS)); 
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
