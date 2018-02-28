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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Rpt.FirmaRichiesta;

public class Applicazione extends Versionabile {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String codApplicazione;
    private Connettore connettoreNotifica;
    private Connettore connettoreVerifica;
    private FirmaRichiesta firmaRichiesta;
    private boolean trusted;
    private String codApplicazioneIuv;
    private String regExp;
	private boolean autoIuv;
	private long idUtenza;

    
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
	public String getCodApplicazioneIuv() {
		return codApplicazioneIuv;
	}
	public void setCodApplicazioneIuv(String codApplicazioneIuv) {
		this.codApplicazioneIuv = codApplicazioneIuv;
	}
	public String getRegExp() {
		return regExp;
	}
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	public boolean isAutoIuv() {
		return autoIuv;
	}
	public void setAutoIuv(boolean autoIuv) {
		this.autoIuv = autoIuv;
	}
	public long getIdUtenza() {
		return idUtenza;
	}
	public void setIdUtenza(long idUtenza) {
		this.idUtenza = idUtenza;
	}
}
