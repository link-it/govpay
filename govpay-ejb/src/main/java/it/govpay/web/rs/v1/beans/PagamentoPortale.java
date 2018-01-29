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
package it.govpay.web.rs.v1.beans;

import java.util.Date;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@JsonFilter(value="pagamentiPortale")  
public class PagamentoPortale extends JSONSerializable {

	private String id;
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
		
	}

}