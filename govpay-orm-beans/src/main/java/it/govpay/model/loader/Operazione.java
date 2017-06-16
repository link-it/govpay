package it.govpay.model.loader;

public class Operazione {
	
	public enum StatoOperazioneType {
		NON_VALIDO, ESEGUITO_OK, ESEGUITO_KO;
	}
	
	private long id;
	private long idTracciato;
	private long lineaElaborazione;
	private byte[] datiRichiesta;
	private byte[] datiRisposta;
	private StatoOperazioneType stato;
	private String dettaglioEsito;
	private String idOperazione;
	private String tipoOperazione;
	private long idApplicazione;
	private String codVersamentoEnte;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdTracciato() {
		return idTracciato;
	}
	public void setIdTracciato(long idTracciato) {
		this.idTracciato = idTracciato;
	}
	public long getLineaElaborazione() {
		return lineaElaborazione;
	}
	public void setLineaElaborazione(long lineaElaborazione) {
		this.lineaElaborazione = lineaElaborazione;
	}
	public byte[] getDatiRichiesta() {
		return datiRichiesta;
	}
	public void setDatiRichiesta(byte[] datiRichiesta) {
		this.datiRichiesta = datiRichiesta;
	}
	public byte[] getDatiRisposta() {
		return datiRisposta;
	}
	public void setDatiRisposta(byte[] datiRisposta) {
		this.datiRisposta = datiRisposta;
	}
	public String getDettaglioEsito() {
		return dettaglioEsito;
	}
	public void setDettaglioEsito(String dettaglioEsito) {
		this.dettaglioEsito = dettaglioEsito;
	}
	public String getIdOperazione() {
		return idOperazione;
	}
	public void setIdOperazione(String idOperazione) {
		this.idOperazione = idOperazione;
	}
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}
	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	public StatoOperazioneType getStato() {
		return stato;
	}
	public void setStato(StatoOperazioneType stato) {
		this.stato = stato;
	}
}
