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

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Tributo extends TipoTributo {
	private static final long serialVersionUID = 1L;
	
	public static final String BOLLOT = "BOLLOT";
	
	public enum TipoContabilta {
	    CAPITOLO("0"),
	    SPECIALE("1"),
	    SIOPE("2"),
	    ALTRO("9");
	    
		private String codifica;

		TipoContabilta(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return codifica;
		}
		
		public static TipoContabilta toEnum(String codifica) throws ServiceException {
			for(TipoContabilta p : TipoContabilta.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per TipoContabilta. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoContabilta.values()));
		}
	}
	
	private long idTipoTributo;
	private long idDominio;
	private Long idIbanAccredito;
	private Long idIbanAppoggio;
	private boolean abilitato;
	private TipoContabilta tipoContabilitaCustom;
	private String codContabilitaCustom;
	private String codTributoIuvCustom;
	
	public Long getIdIbanAccredito() {
		return idIbanAccredito;
	}
	public void setIdIbanAccredito(Long idIbanAccredito) {
		this.idIbanAccredito = idIbanAccredito;
	}
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(long idDominio) {
		this.idDominio = idDominio;
	}

	public long getIdTipoTributo() {
		return idTipoTributo;
	}
	public void setIdTipoTributo(long idTipoTributo) {
		this.idTipoTributo = idTipoTributo;
	}
	public TipoContabilta getTipoContabilitaCustom() {
		return tipoContabilitaCustom;
	}
	public void setTipoContabilitaCustom(TipoContabilta tipoContabilitaCustom) {
		this.tipoContabilitaCustom = tipoContabilitaCustom;
	}
	public String getCodContabilitaCustom() {
		return codContabilitaCustom;
	}
	public void setCodContabilitaCustom(String codContabilitaCustom) {
		this.codContabilitaCustom = codContabilitaCustom;
	}
	public String getCodTributoIuvCustom() {
		return codTributoIuvCustom;
	}
	public void setCodTributoIuvCustom(String codTributoIuv) {
		this.codTributoIuvCustom = codTributoIuv;
	}
	public Long getIdIbanAppoggio() {
		return idIbanAppoggio;
	}
	public void setIdIbanAppoggio(Long idIbanAppoggio) {
		this.idIbanAppoggio = idIbanAppoggio;
	}
}
