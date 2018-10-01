package it.govpay.bd.model;

public class OperazioneCaricamento extends Operazione{
	
	public OperazioneCaricamento() {
	}
	
	public OperazioneCaricamento(Operazione operazione){
		this.setCodVersamentoEnte(operazione.getCodVersamentoEnte());
		this.setDatiRichiesta(operazione.getDatiRichiesta());
		this.setDatiRisposta(operazione.getDatiRisposta());
		this.setDettaglioEsito(operazione.getDettaglioEsito());
		this.setId(operazione.getId());
		this.setIdApplicazione(operazione.getIdApplicazione());
		this.setIdOperazione(operazione.getIdOperazione());
		this.setIdTracciato(operazione.getIdTracciato());
		this.setLineaElaborazione(operazione.getLineaElaborazione());
		this.setStato(operazione.getStato());
		this.setTipoOperazione(operazione.getTipoOperazione());
		this.setCodDominio(operazione.getCodDominio());
	}

	// richiesta
	private Versamento versamento;
	


	// risposta
	private String iuv;
	private byte[] qrCode;
	private byte[] barCode;
	
	@Override
	public String getIuv() {
		return this.iuv;
	}
	@Override
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public byte[] getQrCode() {
		return this.qrCode;
	}
	public void setQrCode(byte[] qrCode) {
		this.qrCode = qrCode;
	}
	public byte[] getBarCode() {
		return this.barCode;
	}
	public void setBarCode(byte[] barCode) {
		this.barCode = barCode;
	}
	
	public Versamento getVersamento() {
		return this.versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
}
