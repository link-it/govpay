/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.bd.model;

import it.govpay.bd.model.Rpt.TipoVersamento;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openspcoop2.generic_project.exception.ServiceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modello di un PSP GovPay
 */
public class Psp extends BasicModel {	
	
	private static final long serialVersionUID = 1L;
	private Long id; 	
	private String codPsp;
	private String codFlusso;
	private String ragioneSociale;
	private String urlInfo;
	private boolean bolloGestito;
	private boolean stornoGestito;
	private boolean attivo;
	private List<Canale> canali = new ArrayList<Canale>();
	
	public enum ModelloPagamento {
		IMMEDIATO(0), 
		IMMEDIATO_MULTIBENEFICIARIO(1), 
		DIFFERITO(2), 
		ATTIVATO_PRESSO_PSP(4);
		
		private int codifica;

		ModelloPagamento(int codifica) {
			this.codifica = codifica;
		}
		
		public int getCodifica() {
			return codifica;
		}
		
		public static ModelloPagamento toEnum(int codifica) throws ServiceException {
			for(ModelloPagamento p : ModelloPagamento.values()){
				if(p.getCodifica() == codifica)
					return p;
			}
			throw new ServiceException("Codifica inesistente per ModelloPagamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(ModelloPagamento.values()));
		}
	}
	
	public Psp() {
		canali = new ArrayList<Canale>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodPsp() {
		return codPsp;
	}

	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getUrlInfo() {
		return urlInfo;
	}

	public void setUrlInfo(String urlInfo) {
		this.urlInfo = urlInfo;
	}

	public boolean isBolloGestito() {
		return bolloGestito;
	}

	public void setBolloGestito(boolean bolloGestito) {
		this.bolloGestito = bolloGestito;
	}

	public boolean isStornoGestito() {
		return stornoGestito;
	}

	public void setStornoGestito(boolean stornoGestito) {
		this.stornoGestito = stornoGestito;
	}

	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
	
	public List<Canale> getCanali() {
		return canali;
	}

	public void setCanali(List<Canale> canali) {
		this.canali = canali;
	}
	
	public String getCodFlusso() {
		return codFlusso;
	}

	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}


    /**
     * Verifica l'equivalenza tra due PSP
     * Nel controllo non verifica la chiave fisica!!
     */
	@Override
	public boolean equals(Object obj) {
		Psp psp = null;
		if(obj instanceof Psp) {
			psp = (Psp) obj;
		} else {
			return false;
		}
		
		boolean equal =
			equals(codPsp, psp.getCodPsp()) &&
			equals(codFlusso, psp.getCodFlusso()) &&
			equals(ragioneSociale, psp.getRagioneSociale()) &&
			equals(urlInfo, psp.getUrlInfo()) &&
			equals(canali, psp.getCanali()) &&
			bolloGestito==psp.isBolloGestito() &&
			stornoGestito==psp.isStornoGestito() &&
			attivo==psp.isAttivo();
		
		return equal;
	}
	
	@Override
	public String toString() {
		String txt =  "\n[Id: " + id + "]"
			 + "\n[CodPsp: " + codPsp + "]"
		     + "\n[RagioneSociale: " + ragioneSociale.toString() + "]"
			 + "\n[UrlInfo: " + urlInfo + "]"
			 + "\n[BolloGestito: " + bolloGestito + "]"
			 + "\n[StornoGestito: " + stornoGestito + "]"
			 + "\n[Attivo: " + attivo + "]";
		
		Collections.sort(canali);
		for(Canale c : canali){
			txt = txt + c.toString();
		}
		return txt;
	}

	/**
	 * Restituisce il Canale attivo in base al {@link TipoVersamento} e al {@link ModelloPagamento} se indicato
	 *
	 * @param tipoVersamento
	 * @param modelloPagamento
	 * @return {@link Canale} attivo
	 */
	public Canale getCanaleAttivo(TipoVersamento tipoVersamento, ModelloPagamento modelloPagamento) {
		for (Canale c : canali) {
			if (c.getTipoVersamento().equals(tipoVersamento)
					&& (modelloPagamento == null || c.getModelloPagamento().equals(modelloPagamento))
					&& c.isAbilitato())
				return c;
		}
		return null;
	}
	
	public Canale getCanale(TipoVersamento tipoVersamento, ModelloPagamento modelloPagamento) {
		for(Canale c : canali) {
			if(c.getTipoVersamento().equals(tipoVersamento) && (modelloPagamento == null || c.getModelloPagamento().equals(modelloPagamento)))
				return c;
		}
		return null;
	}
	
	public Canale getCanale(String codCanale) {
		for(Canale c : canali) {
			if(c.getCodCanale().equals(codCanale))
				return c;
		}
		return null;
	}
	
	public Canale getCanale(long idCanale) {
		for(Canale c : canali) {
			if(c.getId() == idCanale)
				return c;
		}
		return null;
	}

	public class Canale extends BasicModel implements Comparable<Canale> {
		
		private static final long serialVersionUID = 1L;
		private Long id; 
		private String codCanale;
		private String codIntermediario;
		private TipoVersamento tipoVersamento;
		private ModelloPagamento modelloPagamento;
		private String disponibilita;
		private String descrizione;
		private String condizioni;
		private String urlInfo;
		private boolean abilitato;
		@JsonIgnore
		private Psp psp;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public TipoVersamento getTipoVersamento() {
			return tipoVersamento;
		}
		public void setTipoVersamento(TipoVersamento tipoVersamento) {
			this.tipoVersamento = tipoVersamento;
		}
		public ModelloPagamento getModelloPagamento() {
			return modelloPagamento;
		}
		public void setModelloPagamento(ModelloPagamento modelloPagamento) {
			this.modelloPagamento = modelloPagamento;
		}
		public String getDisponibilita() {
			return disponibilita;
		}
		public void setDisponibilita(String disponibita) {
			this.disponibilita = disponibita;
		}
		public String getDescrizione() {
			return descrizione;
		}
		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}
		public String getCondizioni() {
			return condizioni;
		}
		public void setCondizioni(String condizioni) {
			this.condizioni = condizioni;
		}
		public String getUrlInfo() {
			return urlInfo;
		}
		public void setUrlInfo(String urlInfo) {
			this.urlInfo = urlInfo;
		}
		public Psp getPsp() {
			return psp;
		}
		public void setPsp(Psp psp) {
			this.psp = psp;
		}
		public String getCodCanale() {
			return codCanale;
		}
		public void setCodCanale(String codCanale) {
			this.codCanale = codCanale;
		}
		public String getCodIntermediario() {
			return codIntermediario;
		}
		public void setCodIntermediario(String codIntermediario) {
			this.codIntermediario = codIntermediario;
		}
		
	    /**
	     * Verifica l'equivalenza tra due Canali
	     * Nel controllo non verifica la chiave fisica!!
	     */
		@Override
		public boolean equals(Object obj) {
			Canale canale = null;
			if(obj instanceof Canale) {
				canale = (Canale) obj;
			} else {
				return false;
			}
			
			boolean equal = 
					equals(codCanale, canale.getCodCanale()) &&
					equals(codIntermediario, canale.getCodIntermediario()) &&
					equals(tipoVersamento, canale.getTipoVersamento()) &&
					equals(modelloPagamento, canale.getModelloPagamento()) &&
					equals(disponibilita, canale.getDisponibilita()) &&
					equals(descrizione, canale.getDescrizione()) &&
					equals(condizioni, canale.getCondizioni()) &&
					equals(urlInfo, canale.getUrlInfo()) &&
					this.abilitato == canale.isAbilitato();
			
			if(psp == null || canale.getPsp() == null)
				return (psp == canale.getPsp());
				
			equal = equal && equals(psp.getCodPsp(), canale.getPsp().getCodPsp());
			
			return equal;
		}
		
		@Override
		public int compareTo(Canale o) {
			return codCanale.compareTo(o.getCodCanale());
		}
		public boolean isAbilitato() {
			return this.abilitato;
		}
		public void setAbilitato(boolean abilitato) {
			this.abilitato = abilitato;
		}
	}
}
