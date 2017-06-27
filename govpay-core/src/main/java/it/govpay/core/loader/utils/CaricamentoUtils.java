package it.govpay.core.loader.utils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.loader.model.Tracciato;
import it.govpay.core.business.PagamentiAttesa;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.loader.timers.model.AnnullamentoRequest;
import it.govpay.core.loader.timers.model.AnnullamentoResponse;
import it.govpay.core.loader.timers.model.CaricamentoRequest;
import it.govpay.core.loader.timers.model.CaricamentoResponse;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.loader.Operazione.StatoOperazioneType;

public class CaricamentoUtils {

	public CaricamentoResponse caricaVersamento(Tracciato tracciato, CaricamentoRequest request, BasicBD basicBD) throws Exception {

		CaricamentoResponse caricamentoResponse = new CaricamentoResponse();
		
		try {
			it.govpay.bd.model.Versamento versamentoModel = VersamentoUtils.toVersamentoModel(request, basicBD);
			CaricaVersamentoDTO caricaVersamentoDTO = null;

			if(tracciato.getApplicazione(basicBD) != null) {
				caricaVersamentoDTO = new CaricaVersamentoDTO(tracciato.getApplicazione(basicBD), versamentoModel);
			} else {
				caricaVersamentoDTO = new CaricaVersamentoDTO(tracciato.getOperatore(basicBD), versamentoModel);
			}
			caricaVersamentoDTO.setAggiornaSeEsiste(true);
			caricaVersamentoDTO.setGeneraIuv(true);

			PagamentiAttesa pagamentiAttesa = new PagamentiAttesa(basicBD);
			CaricaVersamentoDTOResponse caricaVersamento = pagamentiAttesa.caricaVersamento(caricaVersamentoDTO);
			caricamentoResponse.setBarCode(caricaVersamento.getBarCode());
			caricamentoResponse.setQrCode(caricaVersamento.getQrCode());
			caricamentoResponse.setIuv(caricaVersamento.getIuv());

			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
		} catch(GovPayException e) {
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setDescrizioneEsito(e.getCodEsito() + " : " + e.getMessage());
		}
		
		return caricamentoResponse;
	}
	
	public AnnullamentoResponse annullaVersamento(Tracciato tracciato, AnnullamentoRequest request, BasicBD basicBD) throws Exception {

		
		AnnullamentoResponse annullamentoResponse = new AnnullamentoResponse();

		try {
			PagamentiAttesa pagamentiAttesa = new PagamentiAttesa(basicBD);
			AnnullaVersamentoDTO annullaVersamentoDTO = null;
			if(tracciato.getApplicazione(basicBD) != null) {
				annullaVersamentoDTO = new AnnullaVersamentoDTO(tracciato.getApplicazione(basicBD), request.getCodApplicazione(), request.getCodVersamentoEnte());
			} else {
				annullaVersamentoDTO = new AnnullaVersamentoDTO(tracciato.getOperatore(basicBD), request.getCodApplicazione(), request.getCodVersamentoEnte());
			}

			pagamentiAttesa.annullaVersamento(annullaVersamentoDTO);
			
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
		} catch(GovPayException e) {
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setDescrizioneEsito(e.getCodEsito() + " : " + e.getMessage());
		}
		return annullamentoResponse;
	
	}
}
