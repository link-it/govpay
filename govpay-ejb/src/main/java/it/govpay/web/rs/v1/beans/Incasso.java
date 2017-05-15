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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.govpay.core.business.model.RichiestaIncassoDTO;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class Incasso {
	
	private static JsonConfig jsonConfig = new JsonConfig();
	
	private String dominio;
	private String trn;
	private String causale;
	private BigDecimal importo;
	private Date data_valuta;
	private Date data_contabile;
	private String dispositivo;
	private List<Pagamento> pagamenti;
	
	static {
		jsonConfig.setRootClass(Incasso.class);
	}
	
	public static Incasso parse(String json) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		return (Incasso) JSONObject.toBean( jsonObject, jsonConfig );
	}
	
	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Date getData_valuta() {
		return data_valuta;
	}
	public void setData_valuta(Date data_valuta) {
		this.data_valuta = data_valuta;
	}
	public Date getData_contabile() {
		return data_contabile;
	}
	public void setData_contabile(Date data_contabile) {
		this.data_contabile = data_contabile;
	}
	public String getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	public List<Pagamento> getPagamenti() {
		return pagamenti;
	}
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	
	public RichiestaIncassoDTO toRichiestaIncassoDTO() {
		RichiestaIncassoDTO dto = new RichiestaIncassoDTO();
		dto.setCausale(causale);
		dto.setCodDominio(dominio);
		dto.setDataValuta(data_valuta);
		dto.setDataContabile(data_contabile);
		dto.setDispositivo(dispositivo);
		dto.setImporto(importo);
		dto.setTrn(trn);
		return dto;
	}

	
}
