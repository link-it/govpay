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
package it.govpay.model;


import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Evento extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idVersamento;
	private Long idPagamentoPortale;
	private Long idRpt;
	private String componente;
	private CategoriaEvento categoriaEvento;
	private RuoloEvento ruoloEvento;
	private String tipoEvento;
	private String sottotipoEvento;
	private Long intervallo;
	private Date data;
	private EsitoEvento esitoEvento;
	private Integer sottotipoEsito;
	private String dettaglioEsito;
	private String parametriRichiesta;
	private String parametriRisposta;
	private String datiControparte;
	
	public enum CategoriaEvento {
		INTERNO ("B"), INTERFACCIA ("I"), UTENTE ("U");
		
		private String codifica;

		CategoriaEvento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return this.codifica;
		}
		
		public static CategoriaEvento toEnum(String codifica) throws ServiceException {
			
			for(CategoriaEvento p : CategoriaEvento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			
			throw new ServiceException("Codifica inesistente per CategoriaEvento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(CategoriaEvento.values()));
		}
		
		@Override
		public String toString() {
			return String.valueOf(this.codifica);
		}
	}
	
	public enum RuoloEvento {
		CLIENT ("C"), SERVER ("S");
		
		private String codifica;

		RuoloEvento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return this.codifica;
		}
		
		public static RuoloEvento toEnum(String codifica) throws ServiceException {
			
			for(RuoloEvento p : RuoloEvento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			
			throw new ServiceException("Codifica inesistente per RuoloEvento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(RuoloEvento.values()));
		}
		
		@Override
		public String toString() {
			return String.valueOf(this.codifica);
		}
	}
	
	public enum EsitoEvento {
		OK ("OK"), KO("KO"), FAIL("FAIL");
		
		private String codifica;

		EsitoEvento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return this.codifica;
		}
		
		public static EsitoEvento toEnum(String codifica) throws ServiceException {
			
			for(EsitoEvento p : EsitoEvento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			
			throw new ServiceException("Codifica inesistente per EsitoEvento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(EsitoEvento.values()));
		}
		
		@Override
		public String toString() {
			return String.valueOf(this.codifica);
		}
	}
	
	public Evento() {
		this.categoriaEvento = CategoriaEvento.INTERFACCIA;
		this.ruoloEvento = RuoloEvento.SERVER;
		this.data  = new Date();
	}
	
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CategoriaEvento getCategoriaEvento() {
		return this.categoriaEvento;
	}
	public void setCategoriaEvento(CategoriaEvento categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}
	public String getSottotipoEvento() {
		return this.sottotipoEvento;
	}
	public void setSottotipoEvento(String sottotipoEvento) {
		this.sottotipoEvento = sottotipoEvento;
	}

	public Long getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}

	public Long getIdPagamentoPortale() {
		return idPagamentoPortale;
	}

	public void setIdPagamentoPortale(Long idPagamentoPortale) {
		this.idPagamentoPortale = idPagamentoPortale;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Long getIntervallo() {
		return intervallo;
	}

	public void setIntervallo(Long intervallo) {
		this.intervallo = intervallo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	public void setIntervalloFromData(Date data) {
		if(data != null) {
			if(this.getData() != null) {
				this.setIntervallo(data.getTime() - this.getData().getTime());
			} else {
				this.setIntervallo(0l);
			}
		} else {
			this.setIntervallo(0l);
		}
	}

	public Long getIdRpt() {
		return idRpt;
	}

	public void setIdRpt(Long idRpt) {
		this.idRpt = idRpt;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public RuoloEvento getRuoloEvento() {
		return ruoloEvento;
	}

	public void setRuoloEvento(RuoloEvento ruoloEvento) {
		this.ruoloEvento = ruoloEvento;
	}

	public EsitoEvento getEsitoEvento() {
		return esitoEvento;
	}

	public void setEsitoEvento(EsitoEvento esitoEvento) {
		this.esitoEvento = esitoEvento;
	}

	public Integer getSottotipoEsito() {
		return sottotipoEsito;
	}

	public void setSottotipoEsito(Integer sottotipoEsito) {
		this.sottotipoEsito = sottotipoEsito;
	}

	public String getDettaglioEsito() {
		return dettaglioEsito;
	}

	public void setDettaglioEsito(String dettaglioEsito) {
		this.dettaglioEsito = dettaglioEsito;
	}

	public String getParametriRichiesta() {
		return parametriRichiesta;
	}

	public void setParametriRichiesta(String parametriRichiesta) {
		this.parametriRichiesta = parametriRichiesta;
	}

	public String getParametriRisposta() {
		return parametriRisposta;
	}

	public void setParametriRisposta(String parametriRisposta) {
		this.parametriRisposta = parametriRisposta;
	}

	public String getDatiControparte() {
		return datiControparte;
	}

	public void setDatiControparte(String datiControparte) {
		this.datiControparte = datiControparte;
	}

	 
}
