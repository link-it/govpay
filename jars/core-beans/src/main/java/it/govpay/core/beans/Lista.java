/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.beans;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.hc.core5.net.URIBuilder;

import com.fasterxml.jackson.annotation.JsonFilter;

import it.govpay.core.exceptions.IOException;

@JsonFilter(value="lista") 
public class Lista<T extends JSONSerializable> extends JSONSerializable {

	private Long numRisultati;
	private Long numPagine;
	private Integer risultatiPerPagina;
	private Integer pagina;
	private String prossimiRisultati;
	private BigDecimal maxRisultati = null;
	
	private List<T> risultati;
	
	public List<T> getRisultati() {
		return this.risultati;
	}
	public void setRisultati(List<T> risultati) {
		this.risultati = risultati;
	}
	public Long getNumRisultati() {
		return this.numRisultati;
	}
	public void setNumRisultati(Long numRisultati) {
		this.numRisultati = numRisultati;
	}
	public Long getNumPagine() {
		return this.numPagine;
	}
	public void setNumPagine(Long numPagine) {
		this.numPagine = numPagine;
	}
	public Integer getRisultatiPerPagina() {
		return this.risultatiPerPagina;
	}
	public void setRisultatiPerPagina(Integer risultatiPerPagina) {
		this.risultatiPerPagina = risultatiPerPagina;
	}
	public Integer getPagina() {
		return this.pagina;
	}
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
	public String getProssimiRisultati() {
		return this.prossimiRisultati;
	}
	public void setProssimiRisultati(String prossimiRisultati) {
		this.prossimiRisultati = prossimiRisultati;
	}
	public BigDecimal getMaxRisultati() {
		return maxRisultati;
	}
	public void setMaxRisultati(BigDecimal maxRisultati) {
		this.maxRisultati = maxRisultati;
	}
	@Override
	public String getJsonIdFilter() {
		return "lista";
	}
	
	public Lista() {
		
	}
	
	public String toJSONArray(String fields) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		for (T t : this.risultati) {
			if(sb.length() > 1)
				sb.append(",");
			
			sb.append(t.toJSON(fields));
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
	public Lista(List<T> risultati, URI requestUri, Long count, Integer pagina, Integer limit) {
		this(risultati, requestUri, count, pagina, limit, null);
	}
	
	public Lista(List<T> risultati, URI requestUri, Long count, Integer pagina, Integer limit, BigDecimal maxRisultati) {

		this.risultati = risultati;
		this.pagina = pagina;
		this.risultatiPerPagina = limit;
		this.numRisultati = count;
		this.maxRisultati = maxRisultati;
		
		boolean generaLinkProssimiRisultati = false;
		
		URIBuilder builder = new URIBuilder(requestUri);
		
		if(this.risultatiPerPagina != null) {
			builder.setParameter(Costanti.PARAMETRO_RISULTATI_PER_PAGINA, Long.toString(this.risultatiPerPagina));
		} else {
			this.risultatiPerPagina = this.numRisultati != null ? this.numRisultati.intValue() : null;
		}
		
		this.numPagine = null;
		
		if(count != null) {
			if(limit != null) {
				if(limit > 0) {
					this.numPagine = (count == 0 ? 1L : (long) Math.ceil(count/(double)limit));
				} else {
					this.numPagine = null;
				}
			} else {
				this.numPagine = 1L;
			}
			
			if(this.pagina != null && this.numPagine != null && this.pagina < this.numPagine) {
				generaLinkProssimiRisultati = true;
			}
			
		} else {
			this.maxRisultati = null;
			if(limit != null) {
				generaLinkProssimiRisultati = true;
			}
		}
				
		if(generaLinkProssimiRisultati) {
			Integer nextPagina = pagina + 1 ;
			builder.setParameter(Costanti.PARAMETRO_PAGINA, Integer.toString(nextPagina));
			try {
				this.prossimiRisultati = builder.build().toString();
			} catch (URISyntaxException e) { }
		}
	}
	
}
