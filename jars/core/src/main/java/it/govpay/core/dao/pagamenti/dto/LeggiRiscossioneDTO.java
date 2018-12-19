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
public class LeggiRiscossioneDTO extends BasicCreateRequestDTO {


	public LeggiRiscossioneDTO(Authentication user, String idDominio, String iuv, String iur, Integer indice) {
		super(user);
		this.idDominio=idDominio;
		this.iuv=iuv;
		this.iur=iur;
		this.indice=indice;
	}

	public String getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

	private String idDominio;
	private String iuv;
	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getIur() {
		return this.iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public Integer getIndice() {
		return this.indice;
	}

	public void setIndice(Integer indice) {
		this.indice = indice;
	}

	private String iur;
	private Integer indice;
}
