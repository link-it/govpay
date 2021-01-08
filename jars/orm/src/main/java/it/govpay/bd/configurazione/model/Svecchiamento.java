package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class Svecchiamento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer stampeAvvisi = null;
	private Integer stampeRicevute = null;
	
	private Integer tracciatiPendenzeScartati = null;
	private Integer tracciatiPendenzeCompletati = null;
	
	private Integer pendenzeScadute = null;
	private Integer pendenzePagate = null;
	private Integer pendenzeAnnullate = null;
	private Integer pendenzeDaPagare = null;
	
	private Integer pagamentiEseguiti = null;
	private Integer pagamentiNonEseguiti = null;
	private Integer pagamentiFalliti = null;
	
	private Integer rendicontazioni = null;
	
	private Integer eventi = null;
	
	private Integer notificheConsegnate = null;
	private Integer notificheNonConsegnate = null;
	
	
	public Integer getStampeAvvisi() {
		return stampeAvvisi;
	}
	public void setStampeAvvisi(Integer stampeAvvisi) {
		this.stampeAvvisi = stampeAvvisi;
	}
	public Integer getStampeRicevute() {
		return stampeRicevute;
	}
	public void setStampeRicevute(Integer stampeRicevute) {
		this.stampeRicevute = stampeRicevute;
	}
	public Integer getTracciatiPendenzeScartati() {
		return tracciatiPendenzeScartati;
	}
	public void setTracciatiPendenzeScartati(Integer tracciatiScartati) {
		this.tracciatiPendenzeScartati = tracciatiScartati;
	}
	public Integer getTracciatiPendenzeCompletati() {
		return tracciatiPendenzeCompletati;
	}
	public void setTracciatiPendenzeCompletati(Integer tracciatiPendenzeCompletati) {
		this.tracciatiPendenzeCompletati = tracciatiPendenzeCompletati;
	}
	public Integer getPendenzeScadute() {
		return pendenzeScadute;
	}
	public void setPendenzeScadute(Integer pendenzeScadute) {
		this.pendenzeScadute = pendenzeScadute;
	}
	public Integer getPendenzePagate() {
		return pendenzePagate;
	}
	public void setPendenzePagate(Integer pendenzePagate) {
		this.pendenzePagate = pendenzePagate;
	}
	public Integer getPendenzeAnnullate() {
		return pendenzeAnnullate;
	}
	public void setPendenzeAnnullate(Integer pendenzeAnnullate) {
		this.pendenzeAnnullate = pendenzeAnnullate;
	}
	public Integer getPendenzeDaPagare() {
		return pendenzeDaPagare;
	}
	public void setPendenzeDaPagare(Integer pendenzeDaPagare) {
		this.pendenzeDaPagare = pendenzeDaPagare;
	}
	public Integer getPagamentiEseguiti() {
		return pagamentiEseguiti;
	}
	public void setPagamentiEseguiti(Integer pagamentiEseguiti) {
		this.pagamentiEseguiti = pagamentiEseguiti;
	}
	public Integer getPagamentiNonEseguiti() {
		return pagamentiNonEseguiti;
	}
	public void setPagamentiNonEseguiti(Integer pagamentiNonEseguiti) {
		this.pagamentiNonEseguiti = pagamentiNonEseguiti;
	}
	public Integer getPagamentiFalliti() {
		return pagamentiFalliti;
	}
	public void setPagamentiFalliti(Integer pagamentiFalliti) {
		this.pagamentiFalliti = pagamentiFalliti;
	}
	public Integer getRendicontazioni() {
		return rendicontazioni;
	}
	public void setRendicontazioni(Integer rendicontazioni) {
		this.rendicontazioni = rendicontazioni;
	}
	public Integer getEventi() {
		return eventi;
	}
	public void setEventi(Integer eventi) {
		this.eventi = eventi;
	}
	public Integer getNotificheConsegnate() {
		return notificheConsegnate;
	}
	public void setNotificheConsegnate(Integer notificheConsegnate) {
		this.notificheConsegnate = notificheConsegnate;
	}
	public Integer getNotificheNonConsegnate() {
		return notificheNonConsegnate;
	}
	public void setNotificheNonConsegnate(Integer notificheNonConsegnate) {
		this.notificheNonConsegnate = notificheNonConsegnate;
	}
}
