package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;

public class Tracciato extends it.govpay.model.Tracciato {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Operatore operatore = null;
//	private transient List<Operazione> operazioni = null;
	
	public Operatore getOperatore(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.operatore == null) {
			try {
				if(this.getIdOperatore()!=null)
					this.operatore = AnagraficaManager.getOperatore(configWrapper, this.getIdOperatore());
			} catch(NotFoundException e) {
//				throw new ServiceException(e);
			}
		}
		return this.operatore;
	}
	
//	public List<Operazione> getOperazioni(BasicBD bd) throws ServiceException {
//		if(this.operazioni == null) {
//			OperazioniBD operazioniBD = new OperazioniBD(bd);
//			OperazioneFilter filter = operazioniBD.newFilter();
//			filter.setIdTracciato(super.getId());
//			this.operazioni = new OperazioniBD(bd).findAll(filter);
//		}
//		return this.operazioni;
//	}
}
