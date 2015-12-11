package it.govpay.dars.model;

import java.io.Serializable;
import java.util.List;

import it.govpay.bd.model.Operatore;

public class OperatoreExt implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Operatore operatore;
	private List<ListaEntiEntry> enti;
	private List<ListaApplicazioniEntry> applicazioni;
	
	public OperatoreExt(){}
	
	public OperatoreExt(Operatore operatore, List<ListaEntiEntry> enti, List<ListaApplicazioniEntry> applicazioni){
		this.setApplicazioni(applicazioni);
		this.setEnti(enti);
		this.setOperatore(operatore);
		
	}
	
	public Operatore getOperatore() {
		return operatore;
	}
	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}
	public List<ListaEntiEntry> getEnti() {
		return enti;
	}
	public void setEnti(List<ListaEntiEntry> enti) {
		this.enti = enti;
	}
	public List<ListaApplicazioniEntry> getApplicazioni() {
		return applicazioni;
	}
	public void setApplicazioni(List<ListaApplicazioniEntry> applicazioni) {
		this.applicazioni = applicazioni;
	}

}
