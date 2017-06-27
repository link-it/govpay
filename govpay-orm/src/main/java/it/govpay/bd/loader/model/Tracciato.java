package it.govpay.bd.loader.model;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.loader.OperazioniBD;
import it.govpay.bd.loader.filters.OperazioneFilter;
import it.govpay.model.Applicazione;
import it.govpay.bd.model.Operatore;

import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Tracciato extends it.govpay.model.loader.Tracciato {

	private transient Operatore operatore = null;
	private transient Applicazione applicazione = null;
	private transient List<Operazione> operazioni = null;
	
	public Operatore getOperatore(BasicBD bd) throws ServiceException {
		if(this.operatore == null) {
			try {
				if(this.getIdOperatore()!=null)
					this.operatore = new OperatoriBD(bd).getOperatore(this.getIdOperatore());
			} catch(NotFoundException e) {
				throw new ServiceException(e);
			} catch(MultipleResultException e) {
				throw new ServiceException(e);
			}
		}
		return this.operatore;
	}
	
	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(this.applicazione == null) {
			try {
				if(this.getIdApplicazione()!=null)
					this.applicazione = new ApplicazioniBD(bd).getApplicazione(this.getIdApplicazione());
			} catch(NotFoundException e) {
				throw new ServiceException(e);
			} catch(MultipleResultException e) {
				throw new ServiceException(e);
			}
		}
		return this.applicazione;
	}
	
	public List<Operazione> getOperazioni(BasicBD bd) throws ServiceException {
		if(this.operazioni == null) {
			OperazioniBD operazioniBD = new OperazioniBD(bd);
			OperazioneFilter filter = operazioniBD.newFilter();
			filter.setIdTracciato(super.getId());
			this.operazioni = new OperazioniBD(bd).findAll(filter);
		}
		return this.operazioni;
	}
}
