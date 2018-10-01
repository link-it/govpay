/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.model.IAutorizzato;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiPendenzaDTO extends BasicCreateRequestDTO {


	public LeggiPendenzaDTO(IAutorizzato user) {
		super(user);
	}

	private String codA2A;
	private String codPendenza;
	private String idDominio;
	private String numeroAvviso;
	private boolean infoIncasso = false;

	public String getCodA2A() {
		return this.codA2A;
	}
	public void setCodA2A(String codA2A) {
		this.codA2A = codA2A;
	}
	public String getCodPendenza() {
		return this.codPendenza;
	}
	public void setCodPendenza(String codPendenza) {
		this.codPendenza = codPendenza;
	}
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getNumeroAvviso() {
		return this.numeroAvviso;
	}
	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}
	public boolean isInfoIncasso() {
		return infoIncasso;
	}
	public void setInfoIncasso(boolean infoIncasso) {
		this.infoIncasso = infoIncasso;
	}
}
