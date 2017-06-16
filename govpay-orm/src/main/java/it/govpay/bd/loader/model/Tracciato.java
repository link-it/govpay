package it.govpay.bd.loader.model;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.model.Operatore;

public class Tracciato extends it.govpay.model.loader.Tracciato {

	private transient Operatore operatore = null;
	
	public Operatore getOperatore(BasicBD bd) throws ServiceException {
		if(this.operatore == null) {
			try {
				this.operatore = new OperatoriBD(bd).getOperatore(this.getIdOperatore());
			} catch(NotFoundException e) {
				throw new ServiceException(e);
			} catch(MultipleResultException e) {
				throw new ServiceException(e);
			}
		}
		return this.operatore;
	}
}
