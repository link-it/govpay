package it.govpay.rs.v3;

import it.govpay.rs.BaseRsService;

public class BaseRsServiceV3 extends BaseRsService {
	
	public BaseRsServiceV3(String nomeServizio) {
		super(nomeServizio);
	}
	
	@Override
	public int getVersione() {
		return 3;
	}

}
