package it.govpay.web.rs.utils;

import javax.ws.rs.WebApplicationException;

import org.apache.cxf.common.util.StringUtils;

import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.model.EstrattoContoRequest;
import it.govpay.web.rs.model.Versamento;

public class ValidationUtils {

	public static void validaRichiestaCaricaVersamento(Versamento request) throws WebApplicationException {
		if(StringUtils.isEmpty(request.getCodiceCreditore()))
			throw new WebApplicationException(BaseRsService.getBadRequestResponse("Codice Creditore Obbligatorio")); 
		
		if(StringUtils.isEmpty(request.getCodiceTributo()))
			throw new WebApplicationException(BaseRsService.getBadRequestResponse("Codice Tributo Obbligatorio"));
		
		if(StringUtils.isEmpty(request.getCodiceFiscaleContribuente()))
			throw new WebApplicationException(BaseRsService.getBadRequestResponse("Codice Fiscale Contribuente Obbligatorio"));
		
		if(StringUtils.isEmpty(request.getAnagraficaContribuente()))
			throw new WebApplicationException(BaseRsService.getBadRequestResponse("Anagrafica Contribuente Obbligatoria"));
		
		if(StringUtils.isEmpty(request.getIdentificativoVersamento()))
			throw new WebApplicationException(BaseRsService.getBadRequestResponse("Identificativo Versamento Obbligatorio"));
		
		if(request.getImporto() == null)
			throw new WebApplicationException(BaseRsService.getBadRequestResponse("Importo Obbligatorio"));
	}
	
	public static void validaRichiestaEstrattoConto(EstrattoContoRequest request) throws WebApplicationException {
		if(StringUtils.isEmpty(request.getCodiceCreditore()))
			throw new WebApplicationException(BaseRsService.getBadRequestResponse("Codice Creditore Obbligatorio")); 
		if(request.getDataInizio() == null)
			throw new WebApplicationException(BaseRsService.getBadRequestResponse("Data Inizio Obbligatoria"));
	}
}
