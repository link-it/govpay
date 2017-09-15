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

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@JsonFilter(value="domini")  
public class Dominio extends JSONSerializable {
	
	private String href;
	private String idDominio;
	private String ragioneSociale;
	private String indirizzo;
	private String civico;
	private String cap;
	private String localita;
	private String provincia;
	private String nazione;
	private String gln;
	private String iuvPrefix;
	private String stazione;
	private int auxDigit;
	private Integer segregationCode;
	private String logo;
	private String unitaOperative;
	private String ibanAccredito;
	private String entrate;
	private boolean abilitato;
	
	private static JsonConfig jsonConfig = new JsonConfig();

	static {
		jsonConfig.setRootClass(Dominio.class);
	}
	
	public Dominio() {

	}
	
	@Override
	public String getJsonIdFilter() {
		return "domini";
	}
	
	public static Dominio parse(String json) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		return (Dominio) JSONObject.toBean( jsonObject, jsonConfig );
	}
	
	public Dominio(it.govpay.bd.model.Dominio dominio, UriBuilder uriBuilder) throws ServiceException {
		uriBuilder = uriBuilder.clone().path("domini").path(dominio.getCodDominio());
		this.setHref(uriBuilder.build().toString());
		this.setIdDominio(dominio.getCodDominio()); 
		this.ragioneSociale = dominio.getRagioneSociale();
		this.indirizzo = dominio.getAnagrafica().getIndirizzo();
		this.civico = dominio.getAnagrafica().getCivico();
		this.cap = dominio.getAnagrafica().getCap();
		this.localita = dominio.getAnagrafica().getLocalita();
		this.provincia = dominio.getAnagrafica().getProvincia();
		this.nazione = dominio.getAnagrafica().getNazione();
		this.gln = dominio.getGln();
		this.auxDigit = dominio.getAuxDigit();
		this.segregationCode = dominio.getSegregationCode();
		if(dominio.getLogo() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(dominio.getLogo(), false)));
			this.logo = sb.toString();
		}
		this.iuvPrefix = dominio.getIuvPrefix();
		this.stazione = dominio.getStazione().getCodStazione();
		this.unitaOperative = uriBuilder.clone().path("unita").build().toString();
		this.ibanAccredito = uriBuilder.clone().path("iban").build().toString();
		this.entrate = uriBuilder.clone().path("entrate").build().toString();
		this.abilitato = dominio.isAbilitato();
	}

	public String getGln() {
		return gln;
	}

	public void setGln(String gln) {
		this.gln = gln;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	public int getAuxDigit() {
		return auxDigit;
	}

	public void setAuxDigit(int auxDigit) {
		this.auxDigit = auxDigit;
	}

	public Integer getSegregationCode() {
		return segregationCode;
	}

	public void setSegregationCode(Integer segregationCode) {
		this.segregationCode = segregationCode;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	
	public String getIuvPrefix() {
		return iuvPrefix;
	}

	public void setIuvPrefix(String iuvPrefix) {
		this.iuvPrefix = iuvPrefix;
	}

	public String getStazione() {
		return stazione;
	}

	public void setStazione(String stazione) {
		this.stazione = stazione;
	}

	public String getUnitaOperative() {
		return unitaOperative;
	}

	public void setUnitaOperative(String unitaOperative) {
		this.unitaOperative = unitaOperative;
	}

	public String getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	public String getEntrate() {
		return entrate;
	}

	public void setEntrate(String entrate) {
		this.entrate = entrate;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
}