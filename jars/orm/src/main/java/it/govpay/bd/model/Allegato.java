package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.VersamentiBD;

public class Allegato extends it.govpay.model.Allegato {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient Versamento versamento;
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
		if(versamento.getId() != null)
			this.setIdVersamento(versamento.getId());
	}
	
	public Versamento getVersamentoBD(BasicBD bd) throws ServiceException {
		if(this.versamento == null && bd != null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamentiBD.setAtomica(false); // connessione condivisa
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento());
		}
		return this.versamento;
	}
	
	public Versamento getVersamento(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.versamento == null) {
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento());
		}
		return this.versamento;
	}
}
