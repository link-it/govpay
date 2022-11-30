package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.DominioIndex;
import it.govpay.backoffice.v1.beans.Stazione;
import it.govpay.backoffice.v1.beans.StazioneIndex;
import it.govpay.backoffice.v1.beans.StazionePost;
import it.govpay.backoffice.v1.beans.VersioneStazione;
import it.govpay.core.dao.anagrafica.dto.PutStazioneDTO;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Stazione.Versione;

public class StazioniConverter {

	public static PutStazioneDTO getPutStazioneDTO(StazionePost stazionePost, String idIntermediario, String idStazione, Authentication user) throws UnprocessableEntityException, ValidationException {
		PutStazioneDTO stazioneDTO = new PutStazioneDTO(user);

		it.govpay.bd.model.Stazione stazione = new it.govpay.bd.model.Stazione();

		stazione.setAbilitato(stazionePost.isAbilitato());
		int indexOfIdStazione = idStazione.indexOf("_");

		if(indexOfIdStazione == -1) {
			throw new UnprocessableEntityException("Il formato dell'idStazione non e' valido, previsto idIntermediario_applicationCode.");
		}

		String baseIdStazione = idStazione.substring(0, indexOfIdStazione);

		if(!baseIdStazione.equals(idIntermediario)) {
			throw new UnprocessableEntityException("Il formato dell'idStazione non e' valido, idIntermediario non presente all'interno dell'idStazione.");
		}

		String applicationCodeS = idStazione.substring(indexOfIdStazione+1);
		int applicationCode = Integer.parseInt(applicationCodeS);

		if(applicationCode < 1 || applicationCode > 99)
			throw new UnprocessableEntityException("idStazione deve avere un applicationCode compreso tra 01 e 99.");

		stazione.setApplicationCode(applicationCode);
		stazione.setCodStazione(idStazione);
		stazione.setPassword(stazionePost.getPassword());
		
		if(stazionePost.getVersione() != null) {
			// valore versione non valido
			VersioneStazione versioneStazione = VersioneStazione.fromValue(stazionePost.getVersione());
			if(versioneStazione == null) {
				throw new ValidationException("Codifica inesistente per versione. Valore fornito [" + stazionePost.getVersione() + "] valori possibili " + ArrayUtils.toString(VersioneStazione.values()));
			}

			switch (versioneStazione) {
			case V1:
				stazione.setVersione(Versione.V1);
				break;
			case V2:
				stazione.setVersione(Versione.V2);
				break;
			}
		}
		
		stazioneDTO.setStazione(stazione);
		stazioneDTO.setIdIntermediario(idIntermediario);
		stazioneDTO.setIdStazione(idStazione);
		return stazioneDTO;
	}

	public static Stazione toRsModel(it.govpay.bd.model.Stazione stazione, List<it.govpay.bd.model.Dominio> dominiLst) {
		Stazione rsModel = new Stazione();
		rsModel.abilitato(stazione.isAbilitato())
		.idStazione(stazione.getCodStazione())
		.password(stazione.getPassword());

		if(stazione.getVersione() != null) {
			switch (stazione.getVersione()) {
			case V1:
				rsModel.setVersione(VersioneStazione.V1);
				break;
			case V2:
				rsModel.setVersione(VersioneStazione.V2);
				break;
			}
		}

		if(dominiLst!=null) {
			List<DominioIndex> domini = new ArrayList<>();
			for(it.govpay.bd.model.Dominio dominio: dominiLst) {
				domini.add(DominiConverter.toRsModelIndex(dominio));
			}
			rsModel.setDomini(domini);
		}
		return rsModel;
	}

	public static StazioneIndex toRsModelIndex(it.govpay.bd.model.Stazione stazione) {
		StazioneIndex rsModel = new StazioneIndex();
		rsModel.abilitato(stazione.isAbilitato())
		.idStazione(stazione.getCodStazione())
		.domini(UriBuilderUtils.getListDomini(stazione.getCodStazione()))
		.password(stazione.getPassword());
		
		if(stazione.getVersione() != null) {
			switch (stazione.getVersione()) {
			case V1:
				rsModel.setVersione(VersioneStazione.V1);
				break;
			case V2:
				rsModel.setVersione(VersioneStazione.V2);
				break;
			}
		}

		return rsModel;
	}
}
