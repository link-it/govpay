/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.rs.v1.beans;

import java.util.Date;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@JsonFilter(value="pagamentiPortale")  
public class PagamentoPortale extends JSONSerializable {

	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdSessionePortale() {
		return idSessionePortale;
	}

	public void setIdSessionePortale(String idSessionePortale) {
		this.idSessionePortale = idSessionePortale;
	}

	public String getIdSessionePsp() {
		return idSessionePsp;
	}

	public void setIdSessionePsp(String idSessionePsp) {
		this.idSessionePsp = idSessionePsp;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getPspRedirectUrl() {
		return pspRedirectUrl;
	}

	public void setPspRedirectUrl(String pspRedirectUrl) {
		this.pspRedirectUrl = pspRedirectUrl;
	}

	public Date getDataRichiestaPagamento() {
		return dataRichiestaPagamento;
	}

	public void setDataRichiestaPagamento(Date dataRichiestaPagamento) {
		this.dataRichiestaPagamento = dataRichiestaPagamento;
	}

	public DatiAddebito getDatiAddebito() {
		return datiAddebito;
	}

	public void setDatiAddebito(DatiAddebito datiAddebito) {
		this.datiAddebito = datiAddebito;
	}

	public Date getDataEsecuzionePagamento() {
		return dataEsecuzionePagamento;
	}

	public void setDataEsecuzionePagamento(Date dataEsecuzionePagamento) {
		this.dataEsecuzionePagamento = dataEsecuzionePagamento;
	}

	public String getCredenzialiPagatore() {
		return credenzialiPagatore;
	}

	public void setCredenzialiPagatore(String credenzialiPagatore) {
		this.credenzialiPagatore = credenzialiPagatore;
	}

	public Anagrafica getSoggettoVersante() {
		return soggettoVersante;
	}

	public void setSoggettoVersante(Anagrafica soggettoVersante) {
		this.soggettoVersante = soggettoVersante;
	}

	public String getAutenticazioneSoggetto() {
		return autenticazioneSoggetto;
	}

	public void setAutenticazioneSoggetto(String autenticazioneSoggetto) {
		this.autenticazioneSoggetto = autenticazioneSoggetto;
	}

	public String getCanale() {
		return canale;
	}

	public void setCanale(String canale) {
		this.canale = canale;
	}

	public String getPendenze() {
		return pendenze;
	}

	public void setPendenze(String pendenze) {
		this.pendenze = pendenze;
	}

	public String getRpts() {
		return rpts;
	}

	public void setRpts(String rpts) {
		this.rpts = rpts;
	}

	private String idSessionePortale;
	private String idSessionePsp;
	private String nome;
	private String stato;
	private String pspRedirectUrl;
	private Date dataRichiestaPagamento;
	private DatiAddebito datiAddebito;
	private Date dataEsecuzionePagamento;

	private String credenzialiPagatore;
	private Anagrafica soggettoVersante;
	private String autenticazioneSoggetto;
	private String canale;
	private String pendenze;
	private String rpts;

	
	private static JsonConfig jsonConfig = new JsonConfig();

	static {
		jsonConfig.setRootClass(PagamentoPortale.class);
	}
	public PagamentoPortale() {}
	
	@Override
	public String getJsonIdFilter() {
		return "pagamentiPortale";
	}
	
	public static PagamentoPortale parse(String json) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		return (PagamentoPortale) JSONObject.toBean( jsonObject, jsonConfig );
	}
	
	public PagamentoPortale(it.govpay.bd.model.PagamentoPortale pagamentoPortale, UriBuilder uriBuilder) throws ServiceException {
		this.id = pagamentoPortale.getIdSessione();
		this.idSessionePortale = pagamentoPortale.getIdSessionePortale();
		this.idSessionePsp = pagamentoPortale.getIdSessionePsp();
		this.nome = pagamentoPortale.getNome();
		this.stato = pagamentoPortale.getStato().toString();
		this.pspRedirectUrl = pagamentoPortale.getPspRedirectUrl();
		this.dataRichiestaPagamento = pagamentoPortale.getDataRichiesta();
//		this.datiAddebito = new DatiAddebito();
//		this.datiAddebito.setBicAddebito(pagamentoPortale.get);
//		this.datiAddebito.setIbanAddebito(pagamentoPortale.getibanaddebito);
//		this.dataEsecuzionePagamento = pagamentoPortale.getDataRichiesta(); //TODO
//
//		this.credenzialiPagatore = pagamentoPortale.get;
//		this.soggettoVersante = new Anagrafica();
//		this.soggettoVersante.setCap(pagamentoPortale.get);
//		this.autenticazioneSoggetto = pagamentoPortale.get;
		this.canale = uriBuilder.clone().path("psp").path(pagamentoPortale.getCodPsp()).path("canali").path(pagamentoPortale.getCodCanale()).toString();
		this.pendenze = uriBuilder.clone().path("pendenze").queryParam("idPagamento", pagamentoPortale.getIdSessione()).toString();
		this.rpts = uriBuilder.clone().path("rpts").queryParam("idPagamento", pagamentoPortale.getIdSessione()).toString();

	}

}