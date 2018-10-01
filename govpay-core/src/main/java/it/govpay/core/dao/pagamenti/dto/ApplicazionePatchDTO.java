/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import it.govpay.model.IAutorizzato;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class ApplicazionePatchDTO extends AbstractPatchDTO {

	private String codApplicazione;
	
	public ApplicazionePatchDTO(IAutorizzato user) {
		super(user);
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

}
