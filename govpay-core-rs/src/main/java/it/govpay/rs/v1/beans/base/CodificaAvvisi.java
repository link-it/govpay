/**
 * 
 */
package it.govpay.rs.v1.beans.base;

import it.govpay.rs.v1.beans.JSONSerializable;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 24 feb 2018 $
 * 
 */
public class CodificaAvvisi extends JSONSerializable {

	private String codificaIUV;
	private String regExp;
	
	@Override
	public String getJsonIdFilter() {
		return "codificaAvvisi";
	}

	public String getCodificaIUV() {
		return codificaIUV;
	}

	public void setCodificaIUV(String codificaIUV) {
		this.codificaIUV = codificaIUV;
	}

	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}

}
