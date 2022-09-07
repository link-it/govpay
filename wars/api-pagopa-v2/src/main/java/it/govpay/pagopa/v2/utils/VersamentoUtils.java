package it.govpay.pagopa.v2.utils;

import it.govpay.pagopa.v2.entity.DominioEntity;
import it.govpay.pagopa.v2.entity.IbanAccreditoEntity;
import it.govpay.pagopa.v2.entity.SingoloVersamentoEntity;
import it.govpay.pagopa.v2.entity.TributoEntity;
import it.govpay.pagopa.v2.entity.VersamentoEntity;

public class VersamentoUtils {

	public static boolean isPendenzaMultibeneficiario(VersamentoEntity versamento) {
		DominioEntity dominioV = versamento.getDominio();
		for(SingoloVersamentoEntity singoloVersamento : versamento.getSingoliVersamenti()) {
			// appena trovo un singolo versamento con un id dominio definito sono in modalita' multibeneficiario
			DominioEntity dominioSV = singoloVersamento.getDominio();
			if(dominioSV != null) {
				if(!dominioSV.getCodDominio().equals(dominioV.getCodDominio()))
					return true;
			}
		}
		return false;
	}

	public static boolean isAllIBANPostali(VersamentoEntity versamento) {
		for(SingoloVersamentoEntity singoloVersamento : versamento.getSingoliVersamenti()) {
			// sv con tributo definito
			TributoEntity tributo = singoloVersamento.getTributo();
			IbanAccreditoEntity ibanAccredito = singoloVersamento.getIbanAccredito();
			IbanAccreditoEntity ibanAppoggio = singoloVersamento.getIbanAppoggio();

			if(tributo != null) {
				IbanAccreditoEntity ibanAccreditoTributo = tributo.getIbanAccredito();
				IbanAccreditoEntity ibanAppoggioTributo = tributo.getIbanAppoggio();
				if(ibanAccreditoTributo != null) {
					if(!ibanAccreditoTributo.getPostale())
						return false;
				} else if(ibanAppoggioTributo != null) {
					if(!ibanAppoggioTributo.getPostale())
						return false;
				} else {
					// MBT
					return false;
				} 
			} else if(ibanAccredito != null) {
				if(!ibanAccredito.getPostale())
					return false;
			} else if(ibanAppoggio != null) {
				if(!ibanAppoggio.getPostale())
					return false;
			} else { // iban non definito per la voce 

			}
		}
		return true;
	}

	public static DominioEntity getDominioSingoloVersamento(SingoloVersamentoEntity singoloVersamento, DominioEntity dominio) {

		// appena trovo un singolo versamento con un id dominio definito sono in modalita' multibeneficiario
		DominioEntity dominioSV = singoloVersamento.getDominio(); 
		if(dominioSV != null) {
			if(!dominioSV.getCodDominio().equals(dominio.getCodDominio()))
				return dominioSV;
		}
		return dominio;
	}
}
