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
	
	public static String getFromOperazioni(String idOperazione) {
		return getListOperazioni().path(idOperazione).build().toString();
	}

	public static String getFromPagamenti(String idPagamento) {
		return getListPagamenti().path(idPagamento).build().toString();
	}
	
	public static String getRppByDominioIuvCcp(String idDominio,String iuv, String ccp) {
		return getListRpp().path(idDominio).path(iuv).path(ccp).build().toString(); 
	}

	public static String getPsp(String codPsp) {
		return getFromPsp(codPsp).build().toString();
	}

	public static String getStazione(String codIntermediari, String codStazione) {
		return getList(getFromIntermediari(codIntermediari), "stazioni").path(codStazione).build().toString();
	}
	
	public static String getStazioni(String codIntermediario) {
		return getList(getFromIntermediari(codIntermediario), "stazioni").build().toString();
	}

	public static String getDominio(String codCominio) {
		return getFromDomini(codCominio).build().toString();
	}

	public static String getLogoDominio(String codCominio) {
		return getFromDomini(codCominio).path("logo").build().toString();
	}
	
	public static String getEntrata(String codTipoTributo) {
		return getFromEntrate(codTipoTributo).build().toString();
	}
	
	public static String getUoByDominio(String codCominio, String codUo) {
		return getFromDomini(codCominio).path("unitaOperative").path(codUo).build().toString();
	}
	
	public static String getCanali(String codPsp) {
		return getList(getFromPsp(codPsp), "canali").build().toString();
	}
	
	public static String getCanale(String codPsp, String codCanale, String tipoVersamento) {
		return getList(getFromPsp(codPsp), "canali").path(codCanale).path(tipoVersamento).build().toString();
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
	
	public static String getPagamentiByIdA2AIdPendenza(String idA2A, String idPendenza) {
		return getListPagamenti().queryParam("idA2A", idA2A).queryParam("idPendenza", idPendenza).build().toString();
	}

	public static String getRptsByPagamento(String idPagamento) {
		return getByPagamento(getListRpp(), idPagamento);
	}

	public static String getRppsByIdA2AIdPendenza(String idA2A, String idPendenza) {
		return getListRpp().queryParam("idA2A", idA2A).queryParam("idPendenza", idPendenza).build().toString();
	}

	public static String getListUoByDominio(String idDominio) {
		return getFromDomini(idDominio).path("unitaOperative").build().toString();
	}

	public static String getEntrateByDominio(String idDominio) {
		return getFromDomini(idDominio).path("entrate").build().toString();
	}

	public static String getContiAccreditoByDominio(String idDominio) {
		return getFromDomini(idDominio).path("contiAccredito").build().toString();
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
	
	public static UriBuilder getFromIntermediari(String codIntermediari) {
		return getListIntermediari().path(codIntermediari);
	}
	
	private static UriBuilder getFromDomini(String codDominio) {
		return getListDomini().path(codDominio);
	}
	
	private static UriBuilder getFromEntrate(String codEntrata) {
		return getListEntrate().path(codEntrata);
	}

	private static UriBuilder getListPsp() {
		return getBaseList("psp");
	}
	
	private static UriBuilder getListIntermediari() {
		return getBaseList("intermediari");
	}
	
	private static UriBuilder getListDomini() {
		return getBaseList("domini");
	}
	
	private static UriBuilder getListEntrate() {
		return getBaseList("entrate");
	}

	private static UriBuilder getListPagamenti() {
		return getBaseList("pagamenti");
	}

	private static UriBuilder getListRpp() {
		return getBaseList("rpp");
	}
	
	private static UriBuilder getListIncassi() {
		return getBaseList("incassi");
	}
	
	private static UriBuilder getListPendenze() {
		return getBaseList("pendenze");
	}
	
	private static UriBuilder getListOperazioni() {
		return getBaseList("operazioni");
	}
	
	private static UriBuilder getBaseList(String type) {
		return getList(getBasePath(), type);
	}
	
	private static UriBuilder getBasePath() {
		UriBuilder fromPath = UriBuilder.fromPath("/");
		return fromPath;
	}

	private static UriBuilder getList(UriBuilder base, String type) {
		return base.path(type);
	}

	/**
	 * @param codStazione
	 * @return
	 */
	public static String getListDomini(String codStazione) {
		return getListDomini().queryParam("idStazione", codStazione).build().toString();
	}

	/**
	 * @param codApplicazione
	 * @param codVersamentoEnte
	 * @return
	 */
	public static String getIncassiByIdA2AIdPendenza(String idA2A, String idPendenza) {
		return getListIncassi().path(idA2A).path(idPendenza).build().toString();
	}
	
}
