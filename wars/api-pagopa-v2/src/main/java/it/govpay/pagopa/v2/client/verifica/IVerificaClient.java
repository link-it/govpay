package it.govpay.pagopa.v2.client.verifica;

import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.pagopa.v2.client.exception.ClientException;
import it.govpay.pagopa.v2.entity.VersamentoEntity;

public interface IVerificaClient {

	public VersamentoEntity invoke(String codVersamentoEnte, String bundlekey, String codUnivocoDebitore, String codDominio, String iuv) 
			throws ClientException, 
	VersamentoAnnullatoException, VersamentoDuplicatoException, 
	VersamentoScadutoException, VersamentoSconosciutoException, GovPayException, VersamentoNonValidoException;
}
