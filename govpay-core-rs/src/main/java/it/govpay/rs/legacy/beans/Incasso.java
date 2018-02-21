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
package it.govpay.rs.legacy.beans;

import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

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
	
	static {
		jsonConfig.setRootClass(Incasso.class);
	}
	
	public Incasso() {

	}
	
	public Incasso(it.govpay.bd.model.Incasso i) throws ServiceException {
		this.dominio = i.getCodDominio();
		this.causale = i.getCausale();
		this.data_contabile = i.getDataContabile();
		this.data_valuta = i.getDataValuta();
		this.dispositivo = i.getDispositivo();
		this.importo = i.getImporto();
		this.trn = i.getTrn();
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
