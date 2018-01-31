/**
 * 
 */
package it.govpay.core.utils;

import javax.ws.rs.core.UriBuilder;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 31 gen 2018 $
 * 
 */
public class UriBuilderUtils {

	public static String getCanale(String codPsp, String codCanale) {
		return UriBuilder.fromPath("/psp").path(codPsp).path("canali").path(codCanale).build().toString();
	}

	public static String getPendenze(String idPagamento) {
		return UriBuilder.fromPath("/pendenze").queryParam("idPagamento", idPagamento).build().toString();
	}

	public static String getRpts(String idPagamento) {
		return UriBuilder.fromPath("/rpts").queryParam("idPagamento", idPagamento).build().toString();
	}
}
