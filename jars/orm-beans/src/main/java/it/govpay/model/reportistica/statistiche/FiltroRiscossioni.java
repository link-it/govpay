package it.govpay.model.reportistica.statistiche;

import java.io.Serializable;
import java.util.Date;

public class FiltroRiscossioni implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date dataDa;
	private Date dataA;
	private Long idDominio;
	private String codDominio;
	private Long idUo;
	private String codUo;
	private Long idTipoVersamento;
	private String codTipoVersamento;
	private String direzione;
	private String divisione;
	private String tassonomia;
	private Long idApplicazione;
	private String codApplicazione;
	
	public Date getDataDa() {
		return dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public Date getDataA() {
		return dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public Long getIdUo() {
		return idUo;
	}
	public void setIdUo(Long idUo) {
		this.idUo = idUo;
	}
	public String getCodUo() {
		return codUo;
	}
	public void setCodUo(String codUo) {
		this.codUo = codUo;
	}
	public Long getIdTipoVersamento() {
		return idTipoVersamento;
	}
	public void setIdTipoVersamento(Long idTipoVersamento) {
		this.idTipoVersamento = idTipoVersamento;
	}
	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}
	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}
	public String getDirezione() {
		return direzione;
	}
	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}
	public String getDivisione() {
		return divisione;
	}
	public void setDivisione(String divisione) {
		this.divisione = divisione;
	}
	public Long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public String getCodApplicazione() {
		return codApplicazione;
	}
	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	public String getTassonomia() {
		return tassonomia;
	}
	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}
}
