package it.govpay.rs.v3;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.rs.BaseRsService;

public class BaseRsServiceV3 extends BaseRsService {
	
	public BaseRsServiceV3(String nomeServizio) throws ServiceException {
		super(nomeServizio);
	}
	
	@Override
	public int getVersione() {
		return 3;
	}

}
