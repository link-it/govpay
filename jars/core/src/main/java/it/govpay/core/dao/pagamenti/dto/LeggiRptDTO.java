/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiRptDTO extends BasicCreateRequestDTO {


	public LeggiRptDTO(Authentication user) {
		super(user);
	}

	private String codA2A;
	private String codPendenza;
	private String idDominio;
	private String iuv;
	private String ccp;
	private boolean deepRiscossioni = false;

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
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return this.ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public boolean isDeepRiscossioni() {
		return deepRiscossioni;
	}
	public void setDeepRiscossioni(boolean deepRiscossioni) {
		this.deepRiscossioni = deepRiscossioni;
	}
	
}
