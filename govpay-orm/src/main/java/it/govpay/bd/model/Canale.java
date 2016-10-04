/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.BasicModel;
import it.govpay.model.Psp;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Canale extends BasicModel {
	
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
	
	public enum TipoVersamento {
		BONIFICO_BANCARIO_TESORERIA("BBT"), 
		BOLLETTINO_POSTALE("BP"), 
		ADDEBITO_DIRETTO("AD"), 
		CARTA_PAGAMENTO("CP"), 
		MYBANK("OBEP"), 
		ATTIVATO_PRESSO_PSP("PO");
		
		private String codifica;

		TipoVersamento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return codifica;
		}
		
		public static TipoVersamento toEnum(String codifica) throws ServiceException {
			for(TipoVersamento p : TipoVersamento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per TipoVersamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoVersamento.values()));
		}
	}
	
	private static final long serialVersionUID = 1L;
	private Long id; 
	private long idPsp; 
	private String codCanale;
	private String codIntermediario;
	private TipoVersamento tipoVersamento;
	private ModelloPagamento modelloPagamento;
	private String disponibilita;
	private String descrizione;
	private String condizioni;
	private String urlInfo;
	private boolean abilitato;
	
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
	public boolean isAbilitato() {
		return this.abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public long getIdPsp() {
		return idPsp;
	}
	public void setIdPsp(long idPsp) {
		this.idPsp = idPsp;
	}
	
	// Business
	
	private Psp psp;
	
	public Psp getPsp(BasicBD bd) throws ServiceException {
		if(psp == null) {
			psp = AnagraficaManager.getPsp(bd, idPsp);
		}
		return psp;
	}
	
	public void setPsp(Psp psp) throws ServiceException {
		this.psp = psp;
	}
}
