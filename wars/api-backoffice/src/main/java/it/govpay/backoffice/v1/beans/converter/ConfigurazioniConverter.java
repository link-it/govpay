package it.govpay.backoffice.v1.beans.converter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Configurazione;
import it.govpay.backoffice.v1.beans.TracciatoCsv;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.configurazione.ConfigurazioneDAO;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.PatchOp;

public class ConfigurazioniConverter {
	
	public static final String PATH_GIORNALE_EVENTI = ConfigurazioneDAO.PATH_GIORNALE_EVENTI;
	public static final String PATH_TRACCIATO_CSV = ConfigurazioneDAO.PATH_TRACCIATO_CSV;

	public static PutConfigurazioneDTO getPutConfigurazioneDTO(Configurazione configurazionePost, Authentication user) throws ServiceException,NotAuthorizedException, ValidationException {
		PutConfigurazioneDTO putConfigurazioneDTO = new PutConfigurazioneDTO(user);

		it.govpay.bd.model.Configurazione configurazione = new it.govpay.bd.model.Configurazione();
		if(configurazionePost.getGiornaleEventi() != null)
			configurazione.setGiornale(GiornaleConverter.getGiornaleDTO(configurazionePost.getGiornaleEventi()));
		if(configurazionePost.getTracciatoCsv() != null)
			configurazione.setTracciatoCsv(getTracciatoCsvDTO(configurazionePost.getTracciatoCsv()));

		putConfigurazioneDTO.setConfigurazione(configurazione );

		return putConfigurazioneDTO;
	}

	public static Configurazione toRsModel(it.govpay.bd.model.Configurazione configurazione) throws ServiceException {
		Configurazione rsModel = new Configurazione();

		if(configurazione.getGiornale() != null)
			rsModel.setGiornaleEventi(GiornaleConverter.toRsModel(configurazione.getGiornale()));
		if(configurazione.getTracciatoCsv() != null)
			rsModel.setTracciatoCsv(toTracciatoRsModel(configurazione.getTracciatoCsv()));

		return rsModel;
	}

	private static it.govpay.bd.configurazione.model.TracciatoCsv getTracciatoCsvDTO(TracciatoCsv tracciatoCsv) throws ServiceException {
		it.govpay.bd.configurazione.model.TracciatoCsv dto = new it.govpay.bd.configurazione.model.TracciatoCsv();

		dto.setHeaderRisposta(tracciatoCsv.getResponseHeader());
		dto.setTrasformazioneRichiesta((ConverterUtils.toJSON(tracciatoCsv.getFreemarkerRequest(),null)));
		dto.setTrasformazioneRisposta(ConverterUtils.toJSON(tracciatoCsv.getFreemarkerResponse(),null));

		return dto;
	}
	
	private static it.govpay.bd.configurazione.model.TracciatoCsv getTracciatoCsvDTOPatch(TracciatoCsv tracciatoCsv) throws ServiceException {
		it.govpay.bd.configurazione.model.TracciatoCsv dto = new it.govpay.bd.configurazione.model.TracciatoCsv();

		dto.setHeaderRisposta(tracciatoCsv.getResponseHeader());
		if(tracciatoCsv.getFreemarkerRequest() != null && tracciatoCsv.getFreemarkerRequest() instanceof String)
			dto.setTrasformazioneRichiesta((String) tracciatoCsv.getFreemarkerRequest());
		else 
			dto.setTrasformazioneRichiesta((ConverterUtils.toJSON(tracciatoCsv.getFreemarkerRequest(),null)));
		
		if(tracciatoCsv.getFreemarkerResponse() != null && tracciatoCsv.getFreemarkerResponse() instanceof String)
			dto.setTrasformazioneRisposta((String) tracciatoCsv.getFreemarkerResponse());
		else 
			dto.setTrasformazioneRisposta(ConverterUtils.toJSON(tracciatoCsv.getFreemarkerResponse(),null));

		return dto;
	}

	private static TracciatoCsv toTracciatoRsModel(it.govpay.bd.configurazione.model.TracciatoCsv tracciatoCsv) {
		TracciatoCsv rsModel = new TracciatoCsv();

		rsModel.setResponseHeader(tracciatoCsv.getHeaderRisposta());
		rsModel.setFreemarkerRequest(new RawObject(tracciatoCsv.getTrasformazioneRichiesta()));
		rsModel.setFreemarkerResponse(new RawObject(tracciatoCsv.getTrasformazioneRisposta()));

		return rsModel;
	}

	public static List<PatchOp> toModel(List<it.govpay.backoffice.v1.beans.PatchOp> lstOp) throws ValidationException, ServiceException {
		List<PatchOp> list = new ArrayList<>();
		for(it.govpay.backoffice.v1.beans.PatchOp op : lstOp) {
			PatchOp e = new PatchOp();
			e.setOp(PatchOp.OpEnum.fromValue(op.getOp().name()));
			e.setPath(op.getPath());
			
			if(PATH_GIORNALE_EVENTI.equals(op.getPath())) {
				it.govpay.backoffice.v1.beans.Giornale giornalePost = it.govpay.backoffice.v1.beans.Giornale.parse(ConverterUtils.toJSON(op.getValue(),null));
				giornalePost.validate();
				e.setValue(GiornaleConverter.getGiornaleDTO(giornalePost ));
			} else if(PATH_TRACCIATO_CSV.equals(op.getPath())) {
				TracciatoCsv tracciatoCsv = TracciatoCsv.parse(ConverterUtils.toJSON(op.getValue(),null));
				tracciatoCsv.validate();
				e.setValue(getTracciatoCsvDTOPatch(tracciatoCsv ));
			} else {
				throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.PATH_XX_NON_VALIDO, op.getPath()));
			}
			
			list.add(e);
		}
		return list;
	}
}
