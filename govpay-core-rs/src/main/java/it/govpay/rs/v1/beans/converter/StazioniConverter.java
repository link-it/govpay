package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutStazioneDTO;
import it.govpay.core.rs.v1.beans.base.DominioIndex;
import it.govpay.core.rs.v1.beans.base.Stazione;
import it.govpay.core.rs.v1.beans.base.StazioneIndex;
import it.govpay.core.rs.v1.beans.base.StazionePost;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.IAutorizzato;

public class StazioniConverter {

	public static PutStazioneDTO getPutStazioneDTO(StazionePost stazionePost, String idIntermediario, String idStazione, IAutorizzato user) throws ServiceException {
		PutStazioneDTO stazioneDTO = new PutStazioneDTO(user);
		
		it.govpay.bd.model.Stazione stazione = new it.govpay.bd.model.Stazione();

		stazione.setAbilitato(stazionePost.Abilitato());
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

		List<DominioIndex> domini = new ArrayList<>();
		for(it.govpay.bd.model.Dominio dominio: dominiLst) {
			domini.add(DominiConverter.toRsModelIndex(dominio));
		}
			
		rsModel.setDomini(domini);
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
