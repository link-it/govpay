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

	public static String getPsp(String codPsp) {
		return getFromPsp(codPsp).build().toString();
	}
	
	public static String getDominio(String codCominio) {
		return getFromDomini(codCominio).build().toString();
	}
	
	public static String getUoByDominio(String codCominio, String codUo) {
		return getFromDomini(codCominio).path("unitaOperative").path(codUo).build().toString();
	}
	
	public static String getCanale(String codPsp, String codCanale) {
		return getList(getFromPsp(codPsp), "canali").path(codCanale).build().toString();
	}
	
	public static String getPendenzaByIdA2AIdPendenza(String idA2A, String idPendenza) {
		return getListPendenze().path(idA2A).path(idPendenza).build().toString();
	}
	
	public static String getPendenzeByPagamento(String idPagamento) {
		return getByPagamento(getListPendenze(), idPagamento);
	}

	public static String getPagamentiByPendenza(String idPendenza) {
		return getByPendenza(getListPagamenti(), idPendenza);
	}

	public static String getRptsByPagamento(String idPagamento) {
		return getByPagamento(getListRpt(), idPagamento);
	}

	public static String getRptsByPendenza(String idPendenza) {
		return getByPendenza(getListRpt(), idPendenza);
	}

	public static String getListUoByDominio(String idDominio) {
		return getFromDomini(idDominio).path("unitaOperative").build().toString();
	}

	public static String getEntrateByDominio(String idDominio) {
		return getFromDomini(idDominio).path("entrate").build().toString();
	}

	public static String getIbanAccreditoByDominio(String idDominio) {
		return getFromDomini(idDominio).path("ibanAccredito").build().toString();
	}

	public static String getByPagamento(UriBuilder base, String idPagamento) {
		return base.queryParam("idPagamento", idPagamento).build().toString();
	}

	public static String getByPendenza(UriBuilder base, String idPendenza) {
		return base.queryParam("idPendenza", idPendenza).build().toString();
	}

	private static UriBuilder getFromPsp(String codPsp) {
		return getListPsp().path(codPsp);
	}
	
	private static UriBuilder getFromDomini(String codDominio) {
		return getListDomini().path(codDominio);
	}

	private static UriBuilder getListPsp() {
		return getBaseList("psp");
	}
	
	private static UriBuilder getListDomini() {
		return getBaseList("domini");
	}

	private static UriBuilder getListPagamenti() {
		return getBaseList("pagamenti");
	}

	private static UriBuilder getListRpt() {
		return getBaseList("rpt");
	}
	
	private static UriBuilder getListPendenze() {
		return getBaseList("pendenze");
	}
	
	private static UriBuilder getBaseList(String type) {
		return getList(getBasePath(), type);
	}
	
	private static UriBuilder getBasePath() {
		return UriBuilder.fromPath("/");
	}

	private static UriBuilder getList(UriBuilder base, String type) {
		return base.path(type);
	}
	

}
