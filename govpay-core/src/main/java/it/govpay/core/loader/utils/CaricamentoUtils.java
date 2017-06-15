package it.govpay.core.loader.utils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.exception.VersamentoException;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.loader.timers.model.AnnullamentoRequest;
import it.govpay.core.loader.timers.model.AnnullamentoResponse;
import it.govpay.core.loader.timers.model.CaricamentoRequest;
import it.govpay.core.loader.timers.model.CaricamentoResponse;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.orm.constants.StatoOperazioneType;

import org.openspcoop2.generic_project.exception.NotFoundException;

public class CaricamentoUtils {

	public CaricamentoResponse caricaVersamento(CaricamentoRequest request, BasicBD basicBD) throws Exception {

		CaricamentoResponse caricamentoResponse = new CaricamentoResponse();

		if(!request.isValid()) {
			caricamentoResponse.setStato(StatoOperazioneType.NON_VALIDO);
			caricamentoResponse.setDescrizioneEsito("VAL_902 : ");
			return caricamentoResponse;
		}

		VersamentiBD versamentiBD = new VersamentiBD(basicBD);
		
		try {
			Versamento versamento = VersamentoUtils.toVersamentoModel(request, basicBD);
			versamentiBD.insertVersamento(versamento);
		} catch(GovPayException e) {
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setDescrizioneEsito(e.getCodEsito() + " : " + e.getMessage());
		}
		
		return caricamentoResponse;
	}
	
	public AnnullamentoResponse annullaVersamento(AnnullamentoRequest request, BasicBD basicBD) throws Exception {

		AnnullamentoResponse annullamentoResponse = new AnnullamentoResponse();

		if(!request.isValid()) {
			annullamentoResponse.setStato(StatoOperazioneType.NON_VALIDO);
			annullamentoResponse.setDescrizioneEsito("VAL_902 : ");
			return annullamentoResponse;
		}

		VersamentiBD versamentiBD = new VersamentiBD(basicBD);

		try {
			Versamento versamento = null;
			try {
				versamento = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(basicBD, request.getCodApplicazione()).getId(), request.getCodVersamentoEnte());
			} catch(NotFoundException e) {
				throw new VersamentoException(request.getCodVersamentoEnte(), VersamentoException.VER_008, "Versamento inesistente", e);
			}
			versamentiBD.annullaVersamento(versamento, request.getMotivoAnnullamento());
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
		} catch(VersamentoException e) {
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setDescrizioneEsito(e.getCodEsito() + " : " + e.getMessage());
		}
		return annullamentoResponse;
	
	}
}
