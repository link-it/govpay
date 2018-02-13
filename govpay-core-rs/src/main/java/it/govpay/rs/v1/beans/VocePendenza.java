
package it.govpay.rs.v1.beans;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.SingoloVersamento;

public class VocePendenza extends it.govpay.rs.v1.beans.base.VocePendenza {

	public VocePendenza() {}
	
	@Override
	public String getJsonIdFilter() {
		return "vociPendenze";
	}
	
	public VocePendenza(SingoloVersamento singoloVersamento, int indice) throws ServiceException {
		
		this.setDatiAllegati(singoloVersamento.getNote());
//		this.setDescrizione(singoloVersamento.getDescrizione()); //aggiungere
		this.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		this.setImporto(singoloVersamento.getImportoSingoloVersamento());
		this.setIndice(new BigDecimal(indice));
	}


}
