package it.govpay.core.dao.pagamenti;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class PagamentiPortaleDAO {

	public PagamentiPortaleDTOResponse inserisciPagamenti(PagamentiPortaleDTO pagamentiPortaleDTO) throws GovPayException, NotAuthorizedException, ServiceException {
		PagamentiPortaleDTOResponse response  = new PagamentiPortaleDTOResponse();
		return response;
	}
}
