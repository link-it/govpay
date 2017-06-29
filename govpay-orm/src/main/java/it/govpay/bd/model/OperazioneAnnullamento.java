package it.govpay.bd.model;

public class OperazioneAnnullamento extends Operazione{
	
	public OperazioneAnnullamento() {
	}
	
	public OperazioneAnnullamento(Operazione operazione){
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
	}

	private String motivoAnnullamento;
	
	public String getMotivoAnnullamento() {
		return motivoAnnullamento;
	}

	public void setMotivoAnnullamento(String motivoAnnullamento) {
		this.motivoAnnullamento = motivoAnnullamento;
	}
}
