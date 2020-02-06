package it.govpay.model.reportistica.statistiche;

public class StatisticaRendicontazione extends StatisticaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codFlusso;
	private String direzione;
	private String divisione;
	
	public StatisticaRendicontazione() {
		super();
	}
	
	public String getCodFlusso() {
		return codFlusso;
	}
	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
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
}
