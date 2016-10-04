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
package it.govpay.model;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Connettore;
import it.govpay.bd.model.Versionabile;
import it.govpay.model.Rpt.FirmaRichiesta;

public class Applicazione extends Versionabile {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String codApplicazione;
	private String principal;
	private boolean abilitato;
    private Connettore connettoreNotifica;
    private Connettore connettoreVerifica;
    private FirmaRichiesta firmaRichiesta;
    private boolean trusted;
    private List<Acl> acls;
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodApplicazione() {
		return codApplicazione;
	}
	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	public Connettore getConnettoreNotifica() {
		return connettoreNotifica;
	}
	public void setConnettoreNotifica(Connettore connettoreNotifica) {
		this.connettoreNotifica = connettoreNotifica;
	}
	public Connettore getConnettoreVerifica() {
		return connettoreVerifica;
	}
	public void setConnettoreVerifica(Connettore connettoreVerifica) {
		this.connettoreVerifica = connettoreVerifica;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public FirmaRichiesta getFirmaRichiesta() {
		return firmaRichiesta;
	}
	public void setFirmaRichiesta(FirmaRichiesta firmaRichiesta) {
		this.firmaRichiesta = firmaRichiesta;
	}
	public void setCodFirmaRichiesta(String codifica) throws ServiceException {
		this.firmaRichiesta = FirmaRichiesta.toEnum(codifica);
	}
	public boolean isTrusted() {
		return trusted;
	}
	public void setTrusted(boolean trusted) {
		this.trusted = trusted;
	}
	public List<Acl> getAcls() {
		return acls;
	}
	public void setAcls(List<Acl> acls) {
		this.acls = acls;
	}
}
