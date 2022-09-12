package it.govpay.pagopa.v2.client.verifica;

import it.govpay.core.exception.GovPayException;
import it.govpay.core.exception.VersamentoAnnullatoException;
import it.govpay.core.exception.VersamentoDuplicatoException;
import it.govpay.core.exception.VersamentoNonValidoException;
import it.govpay.core.exception.VersamentoScadutoException;
import it.govpay.core.exception.VersamentoSconosciutoException;
import it.govpay.pagopa.v2.client.exception.ClientException;
import it.govpay.pagopa.v2.entity.VersamentoEntity;

public interface IVerificaClient {

	public VersamentoEntity invoke(String codVersamentoEnte, String bundlekey, String codUnivocoDebitore, String codDominio, String iuv) 
			throws ClientException, 
	VersamentoAnnullatoException, VersamentoDuplicatoException, 
	VersamentoScadutoException, VersamentoSconosciutoException, GovPayException, VersamentoNonValidoException;
}
