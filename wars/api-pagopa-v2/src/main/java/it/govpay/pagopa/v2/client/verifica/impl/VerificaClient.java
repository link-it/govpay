package it.govpay.pagopa.v2.client.verifica.impl;

import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.commons.Versamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.client.IVerificaClient;
import it.govpay.pagopa.v2.entity.ApplicazioneEntity;

public class VerificaClient implements IVerificaClient {

	private ApplicazioneEntity applicazione;
	
	public VerificaClient(ApplicazioneEntity applicazione) {
		this.applicazione = applicazione;
	}
	
	@Override
	public Versamento verificaPendenza(String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv) throws it.govpay.core.utils.client.exception.ClientException,
			VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoScadutoException,
			VersamentoSconosciutoException, VersamentoNonValidoException, GovPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Versamento inoltroPendenza(String codDominio, String codTipoVersamento, String codUnitaOperativa,
			String jsonBody) throws it.govpay.core.utils.client.exception.ClientException, VersamentoAnnullatoException,
			VersamentoDuplicatoException, VersamentoScadutoException, VersamentoSconosciutoException,
			VersamentoNonValidoException, GovPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventoContext getEventoCtx() {
		// TODO Auto-generated method stub
		return null;
	}

}
