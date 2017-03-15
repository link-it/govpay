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
package it.govpay.core.business.model;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.govpay.bd.model.Dominio;

public class EstrattoConto {
	
	public enum TipoEstrattoConto {
		CSV , PDF 
	}

	private Dominio dominio;
	private List<Long> idVersamenti;
	private List<Long> idSingoliVersamenti;
	private Map<String, ByteArrayOutputStream> output;
	private TipoEstrattoConto tipoEstrattoConto;
	
	public EstrattoConto(){
		
	}
 
	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public List<Long> getIdVersamenti() {
		return idVersamenti;
	}
	public void setIdVersamenti(List<Long> idVersamenti) {
		this.idVersamenti = idVersamenti;
	}
	public TipoEstrattoConto getTipoEstrattoConto() {
		return tipoEstrattoConto;
	}
	public void setTipoEstrattoConto(TipoEstrattoConto tipoEstrattoConto) {
		this.tipoEstrattoConto = tipoEstrattoConto;
	}
	public List<Long> getIdSingoliVersamenti() {
		return idSingoliVersamenti;
	}
	public void setIdSingoliVersamenti(List<Long> idSingoliVersamenti) {
		this.idSingoliVersamenti = idSingoliVersamenti;
	}
	public Map<String, ByteArrayOutputStream> getOutput() {
		if(output == null)
			output = new HashMap<String, ByteArrayOutputStream>();
		return output;
	}

	public static EstrattoConto creaEstrattoContoPDF(){
		EstrattoConto ec = new EstrattoConto();
		ec.setTipoEstrattoConto(TipoEstrattoConto.PDF);
		return ec;
	}
	
	public static EstrattoConto creaEstrattoContoVersamentiPDF(Dominio dominio,List<Long> idVersamenti){
		EstrattoConto ec = new EstrattoConto();
		ec.setDominio(dominio);
		ec.setIdVersamenti(idVersamenti);
		ec.setTipoEstrattoConto(TipoEstrattoConto.PDF);
		return ec;
	}
	
	public static EstrattoConto creaEstrattoContoPagamentiPDF(Dominio dominio,List<Long> idPagamenti){
		EstrattoConto ec = new EstrattoConto();
		ec.setDominio(dominio);
		ec.setIdSingoliVersamenti(idPagamenti);
		ec.setTipoEstrattoConto(TipoEstrattoConto.PDF);
		return ec;
	}
}
