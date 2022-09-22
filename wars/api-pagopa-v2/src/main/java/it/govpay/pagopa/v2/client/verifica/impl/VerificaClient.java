package it.govpay.pagopa.v2.client.verifica.impl;

import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.pagopa.v2.client.exception.ClientException;
import it.govpay.pagopa.v2.client.verifica.IVerificaClient;
import it.govpay.pagopa.v2.entity.ApplicazioneEntity;
import it.govpay.pagopa.v2.entity.VersamentoEntity;

public class VerificaClient implements IVerificaClient {

	private ApplicazioneEntity applicazione;
	
	public VerificaClient(ApplicazioneEntity applicazione) {
		this.applicazione = applicazione;
	}
	
	@Override
	public VersamentoEntity invoke(String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv)
			throws ClientException, VersamentoAnnullatoException, VersamentoDuplicatoException,
			VersamentoScadutoException, VersamentoSconosciutoException, GovPayException, VersamentoNonValidoException {
		// TODO Auto-generated method stub
		return null;
	}

}
