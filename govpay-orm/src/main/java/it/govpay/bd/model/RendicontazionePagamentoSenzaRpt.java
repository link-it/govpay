package it.govpay.bd.model;

import it.govpay.model.BasicModel;
import it.govpay.model.Fr;
import it.govpay.model.FrApplicazione;
import it.govpay.model.RendicontazioneSenzaRpt;
import it.govpay.model.SingoloVersamento;
import it.govpay.model.Versamento;

public class RendicontazionePagamentoSenzaRpt extends BasicModel {

	private static final long serialVersionUID = 1L;
	
	private Fr fr;
	private FrApplicazione frApplicazione;
	private RendicontazioneSenzaRpt rendicontazioneSenzaRpt;
	private Versamento versamento;
	private SingoloVersamento singoloVersamento;
	
	public Fr getFr() {
		return fr;
	}
	public void setFr(Fr fr) {
		this.fr = fr;
	}
	public FrApplicazione getFrApplicazione() {
		return frApplicazione;
	}
	public void setFrApplicazione(FrApplicazione frApplicazione) {
		this.frApplicazione = frApplicazione;
	}
	public Versamento getVersamento() {
		return versamento;
	}
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	public SingoloVersamento getSingoloVersamento() {
		return singoloVersamento;
	}
	public void setSingoloVersamento(SingoloVersamento singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
	}
	public RendicontazioneSenzaRpt getRendicontazioneSenzaRpt() {
		return rendicontazioneSenzaRpt;
	}
	public void setRendicontazioneSenzaRpt(RendicontazioneSenzaRpt rendicontazioneSenzaRpt) {
		this.rendicontazioneSenzaRpt = rendicontazioneSenzaRpt;
	}
	

}
