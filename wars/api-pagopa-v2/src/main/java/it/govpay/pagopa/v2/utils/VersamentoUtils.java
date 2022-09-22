package it.govpay.pagopa.v2.utils;

import it.govpay.core.beans.commons.Versamento;
import it.govpay.core.exceptions.GovPayException;
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
	
	public static boolean generaIUV(VersamentoEntity versamento) {
		
		boolean hasBollo = false;
		for(SingoloVersamentoEntity singoloVersamento : versamento.getSingoliVersamenti()) {
			if(!hasBollo) {
				if(singoloVersamento.getTipoBollo() != null && singoloVersamento.getHashDocumento() != null && singoloVersamento.getProvinciaResidenza() != null) {
					hasBollo = true;
				}
			}
		}
		
		// se non c'e' una voce con il bollo devo semplicemente controllare che non me lo passino
		if(!hasBollo) {
			return versamento.getNumeroAvviso() == null;
		} else {
		// altrimenti non si genera
			return false;
		}
	}

	public static VersamentoEntity toVersamentoModel(Versamento versamentoCommons, boolean b) throws GovPayException{ 
		// TODO Auto-generated method stub
		return null;
	}
}
