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
		return getFromPsp().buildFromEncoded(codPsp).toString();
	}

	public static String getStazione(String codIntermediario, String codStazione) {
		return getFromIntermediari().path("stazioni").path("{idStazione}").buildFromEncoded(codIntermediario,codStazione).toString();
	}
	
	public static String getStazioni(String codIntermediario) {
		return getFromIntermediari().path("stazioni").buildFromEncoded(codIntermediario).toString();
	}

	public static String getDominio(String codCominio) {
		return getFromDomini().buildFromEncoded(codCominio).toString();
	}

	public static String getLogoDominio(String codCominio) {
		return getFromDomini().path("logo").buildFromEncoded(codCominio).toString();
	}
	
	public static String getEntrata(String codTipoTributo) {
		return getFromEntrate().buildFromEncoded(codTipoTributo).toString();
	}
	
	public static String getUoByDominio(String codCominio, String codUo) {
		return getFromDomini().path("unitaOperative").path("{idUo}").buildFromEncoded(codCominio,codUo).toString();
	}
	
	public static String getCanali(String codPsp) {
		return getFromPsp().path("canali").buildFromEncoded(codPsp).toString();
	}
	
	public static String getCanale(String codPsp, String codCanale, String tipoVersamento) {
		return getFromPsp().path("canali").path("{codCanale}").path("{tipoVersamento}").buildFromEncoded(codPsp,codCanale,tipoVersamento).toString();
	}
	
	public static String getPendenzaByIdA2AIdPendenza(String idA2A, String idPendenza) {
		return getListPendenze().path("{idA2A}").path("{idPendenza}").buildFromEncoded(idA2A,idPendenza).toString();
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
		return getFromDomini().path("unitaOperative").buildFromEncoded(idDominio).toString();
	}

	public static String getEntrateByDominio(String idDominio) {
		return getFromDomini().path("entrate").buildFromEncoded(idDominio).toString();
	}
	
	public static String getTipiPendenzaByDominio(String idDominio) {
		return getFromDomini().path("tipiPendenza").buildFromEncoded(idDominio).toString();
	}

	public static String getContiAccreditoByDominio(String idDominio) {
		return getFromDomini().path("contiAccredito").buildFromEncoded(idDominio).toString();
	}

	public static String getIbanAccreditoByDominio(String idDominio) {
		return getFromDomini().path("ibanAccredito").buildFromEncoded(idDominio).toString();
	}

	public static String getByPagamento(UriBuilder base, String idPagamento) {
		return base.queryParam("idPagamento", idPagamento).build().toString();
	}

	public static String getByPendenza(UriBuilder base, String idPendenza) {
		return base.queryParam("idPendenza", idPendenza).build().toString();
	}

	private static UriBuilder getFromPsp() {
		return getListPsp().path("{idPsp}");
	}
	
	public static UriBuilder getFromIntermediari() {
		return getListIntermediari().path("{idIntermediario}");
	}
	
	private static UriBuilder getFromDomini() {
		return getListDomini().path("{idDominio}");
	}
	
	private static UriBuilder getFromEntrate() {
		return getListEntrate().path("{idEntrata}");
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
	
	private static UriBuilder getListRiconciliazioni() {
		return getBaseList("riconciliazioni");
	}
	
	private static UriBuilder getListPendenze() {
		return getBaseList("pendenze");
	}
	
	private static UriBuilder getListOperazioni() {
		return getBaseList("operazioni");
	}
	
	public static UriBuilder getListRendicontazioni() {
		return getBaseList("rendicontazioni");
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

	public static String getIncassiByIdDominioIdIncasso(String idDominio, String idIncasso) {
		return getListIncassi().path("{idDominio}").path("{idIncasso}").buildFromEncoded(idDominio,idIncasso).toString();
	}
	
	public static String getRiconciliazioniByIdDominioIdIncasso(String idDominio, String idIncasso) {
		return getListRiconciliazioni().path("{idDominio}").path("{idIncasso}").buildFromEncoded(idDominio,idIncasso).toString();
	}
	
}
