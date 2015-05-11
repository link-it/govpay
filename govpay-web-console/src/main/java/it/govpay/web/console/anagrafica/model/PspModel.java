/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.web.console.anagrafica.model;

import it.govpay.ejb.model.GatewayPagamentoModel.EnumModalitaPagamento;
import it.govpay.ejb.model.GatewayPagamentoModel.EnumModelloVersamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PspModel {
	
	public PspModel() {
		this.canali = new ArrayList<CanaleModel>();
		this.tipiVersamentoGestiti = new HashSet<EnumModalitaPagamento>();
	}
	
	public class CanaleModel {
		private long idPsp;
		private String descrizione;
		private String disponibilita;
		private String informazioni;
		private String commissioni;
		private EnumModalitaPagamento tipoVersamento;
		private EnumModelloVersamento modelloVersamento;
		private boolean stornoGestito;
		private boolean isAbilitato;
		
		public long getIdPsp() {
			return idPsp;
		}
		public void setIdPsp(long idPsp) {
			this.idPsp = idPsp;
		}
		public String getDescrizione() {
			return descrizione;
		}
		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}
		public String getDisponibilita() {
			return disponibilita;
		}
		public void setDisponibilita(String disponibilita) {
			this.disponibilita = disponibilita;
		}
		public boolean isStornoGestito() {
			return stornoGestito;
		}
		public void setStornoGestito(boolean stornoGestito) {
			this.stornoGestito = stornoGestito;
		}
		public boolean isAbilitato() {
			return isAbilitato;
		}
		public void setAbilitato(boolean isAbilitato) {
			this.isAbilitato = isAbilitato;
		}
		public EnumModalitaPagamento getTipoVersamento() {
			return tipoVersamento;
		}
		public void setTipoVersamento(EnumModalitaPagamento tipoVersamento) {
			this.tipoVersamento = tipoVersamento;
		}
		public EnumModelloVersamento getModelloVersamento() {
			return modelloVersamento;
		}
		public void setModelloVersamento(EnumModelloVersamento modelloVersamento) {
			this.modelloVersamento = modelloVersamento;
		}
		public String getInformazioni() {
			return informazioni;
		}
		public void setInformazioni(String informazioni) {
			this.informazioni = informazioni;
		}
		public String getCommissioni() {
			return commissioni;
		}
		public void setCommissioni(String commissioni) {
			this.commissioni = commissioni;
		}
	}
	
	private String ragioneSociale;
	private String informazioni;
	private Date inizioValidita, fineValidita;
	private List<CanaleModel> canali;
	private Set<EnumModalitaPagamento> tipiVersamentoGestiti;
	
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	
	public List<CanaleModel> getCanali() {
		return canali;
	}
	
	public void setCanali(List<CanaleModel> canali) {
		this.canali = canali;
	}
	
	public Set<EnumModalitaPagamento> getTipiVersamentoGestiti() {
		return tipiVersamentoGestiti;
	}
	
	public void setTipiVersamentoGestiti(Set<EnumModalitaPagamento> tipiVersamentoGestiti) {
		this.tipiVersamentoGestiti = tipiVersamentoGestiti;
	}
	
	public CanaleModel getCanale(EnumModalitaPagamento tipoVersamento, boolean multibeneficiario) {
		for(CanaleModel canaleModel : canali) {
			if(canaleModel.getTipoVersamento().equals(tipoVersamento)) {
				if(multibeneficiario && canaleModel.getModelloVersamento().equals(EnumModelloVersamento.IMMEDIATO_MULTIBENEFICIARIO))
					return canaleModel;
				if(!multibeneficiario && !canaleModel.getModelloVersamento().equals(EnumModelloVersamento.IMMEDIATO_MULTIBENEFICIARIO))
					return canaleModel;
			}
		}
		return null;
	}
	
	public boolean isMultibeneficiarioSupported(EnumModalitaPagamento tipoVersamento) {
		for(CanaleModel canaleModel : canali) {
			if(canaleModel.getTipoVersamento().equals(tipoVersamento) && canaleModel.getModelloVersamento().equals(EnumModelloVersamento.IMMEDIATO_MULTIBENEFICIARIO)) {
				return true;
			}
		}
		return false;
	}

	public String getInformazioni() {
		return informazioni;
	}

	public void setInformazioni(String informazioni) {
		this.informazioni = informazioni;
	}

	public Date getInizioValidita() {
		return inizioValidita;
	}

	public void setInizioValidita(Date inizioValidita) {
		this.inizioValidita = inizioValidita;
	}

	public Date getFineValidita() {
		return fineValidita;
	}

	public void setFineValidita(Date fineValidita) {
		this.fineValidita = fineValidita;
	}
}
