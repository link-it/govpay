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

import java.util.Date;
import java.util.List;


public class Mail extends BasicModel {

	private static final long serialVersionUID = 1L;
	
	public enum StatoSpedizione {DA_SPEDIRE, ERRORE_SPEDIZIONE, IN_RISPEDIZIONE, SPEDITA}
	public enum TipoMail {NOTIFICA_RPT, NOTIFICA_RT}
	
	private Long id;
	private Long idVersamento;
	private long bundleKey;
	private String mittente;
	private String destinatario;
	private List<String> cc;
	private String oggetto;
	private String messaggio;
	private TipoMail tipoMail;
	private StatoSpedizione statoSpedizione;
	private String dettaglioErroreSpedizione;
	private Date dataOraUltimaSpedizione;
	private Long tentativiRispedizione;

	private Long idTracciatoRPT;
	private Long idTracciatoRT;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StatoSpedizione getStatoSpedizione() {
		return statoSpedizione;
	}
	public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
		this.statoSpedizione = statoSpedizione;
	}
	public String getDettaglioErroreSpedizione() {
		return dettaglioErroreSpedizione;
	}
	public void setDettaglioErroreSpedizione(String dettaglioErroreSpedizione) {
		this.dettaglioErroreSpedizione = dettaglioErroreSpedizione;
	}
	public Date getDataOraUltimaSpedizione() {
		return dataOraUltimaSpedizione;
	}
	public void setDataOraUltimaSpedizione(Date dataOraUltimaSpedizione) {
		this.dataOraUltimaSpedizione = dataOraUltimaSpedizione;
	}
	public Long getTentativiRispedizione() {
		return tentativiRispedizione;
	}
	public void setTentativiRispedizione(Long tentativiRispedizione) {
		this.tentativiRispedizione = tentativiRispedizione;
	}
	
	@Override
	public boolean equals(Object obj) {
		Mail mail = null;
		if(obj instanceof Mail) {
			mail = (Mail) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(mittente, mail.getMittente()) &&
				equals(destinatario, mail.getDestinatario()) &&
				equals(cc, mail.getCc()) &&
				equals(oggetto, mail.getOggetto()) &&
				equals(messaggio, mail.getMessaggio()) &&
				equals(statoSpedizione, mail.getStatoSpedizione()) &&
				equals(dataOraUltimaSpedizione, mail.getDataOraUltimaSpedizione()) &&
				equals(tentativiRispedizione, mail.getTentativiRispedizione()) &&
				equals(tipoMail, mail.getTipoMail()) &&
				equals(idTracciatoRPT, mail.getIdTracciatoRPT()) &&
				equals(idVersamento, mail.getIdVersamento()) &&
				equals(bundleKey, mail.getBundleKey()) &&
				equals(idTracciatoRT, mail.getIdTracciatoRT());
		
		return equal;
	}
	public List<String> getCc() {
		return cc;
	}
	public void setCc(List<String> cc) {
		this.cc = cc;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	public String getMittente() {
		return mittente;
	}
	public void setMittente(String mittente) {
		this.mittente = mittente;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public Long getIdTracciatoRT() {
		return idTracciatoRT;
	}
	public void setIdTracciatoRT(Long idTracciatoRT) {
		this.idTracciatoRT = idTracciatoRT;
	}
	public Long getIdTracciatoRPT() {
		return idTracciatoRPT;
	}
	public void setIdTracciatoRPT(Long idTracciatoRPT) {
		this.idTracciatoRPT = idTracciatoRPT;
	}
	public TipoMail getTipoMail() {
		return tipoMail;
	}
	public void setTipoMail(TipoMail tipoMail) {
		this.tipoMail = tipoMail;
	}
	public Long getIdVersamento() {
		return idVersamento;
	}
	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}
	public long getBundleKey() {
		return bundleKey;
	}
	public void setBundleKey(long bundleKey) {
		this.bundleKey = bundleKey;
	}

}
