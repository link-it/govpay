package it.govpay.pagamento.api.rs.pagamenti.v1.model;

public class VocePendenza {
	private String idVocePendenza = null;
	private double importo;
	private String descrizione = null;
	private String datiAllegati = null;
	
	/*Choice */
	
	//	Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
	private String codEntrata = null;
	
	// 	Definisce i dettagli di incasso della singola entrata.
	private String ibanAccredito = null;
	private String bicAccredito = null;
	private String ibanAppoggio = null;
	private String bicAppoggio = null;
	private String tipoContabilita = null;
	private String codiceContabilita = null;
	
	// Definisce i dati di un bollo telematico
	private String tipoBollo = null;
	private String hashDocumento = null;
	private String provinciaResidenza = null;
	
	public String getIdVocePendenza() {
		return idVocePendenza;
	}
	public void setIdVocePendenza(String idVocePendenza) {
		this.idVocePendenza = idVocePendenza;
	}
	public double getImporto() {
		return importo;
	}
	public void setImporto(double importo) {
		this.importo = importo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getDatiAllegati() {
		return datiAllegati;
	}
	public void setDatiAllegati(String datiAllegati) {
		this.datiAllegati = datiAllegati;
	}
	public String getCodEntrata() {
		return codEntrata;
	}
	public void setCodEntrata(String codEntrata) {
		this.codEntrata = codEntrata;
	}
	public String getIbanAccredito() {
		return ibanAccredito;
	}
	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	public String getBicAccredito() {
		return bicAccredito;
	}
	public void setBicAccredito(String bicAccredito) {
		this.bicAccredito = bicAccredito;
	}
	public String getIbanAppoggio() {
		return ibanAppoggio;
	}
	public void setIbanAppoggio(String ibanAppoggio) {
		this.ibanAppoggio = ibanAppoggio;
	}
	public String getBicAppoggio() {
		return bicAppoggio;
	}
	public void setBicAppoggio(String bicAppoggio) {
		this.bicAppoggio = bicAppoggio;
	}
	public String getTipoContabilita() {
		return tipoContabilita;
	}
	public void setTipoContabilita(String tipoContabilita) {
		this.tipoContabilita = tipoContabilita;
	}
	public String getCodiceContabilita() {
		return codiceContabilita;
	}
	public void setCodiceContabilita(String codiceContabilita) {
		this.codiceContabilita = codiceContabilita;
	}
	public String getTipoBollo() {
		return tipoBollo;
	}
	public void setTipoBollo(String tipoBollo) {
		this.tipoBollo = tipoBollo;
	}
	public String getHashDocumento() {
		return hashDocumento;
	}
	public void setHashDocumento(String hashDocumento) {
		this.hashDocumento = hashDocumento;
	}
	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}
	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}
	
}
