package it.govpay.bd.model;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.orm.IdUtenza;

public class Acl extends it.govpay.model.Acl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	public Acl() {
		super();
	}

	// business
	private transient Utenza utenza;
	private transient IdUtenza idUtenzaObj;

	public Utenza getUtenza(BasicBD bd) throws ServiceException {
		if(this.getIdUtenza() != null && this.utenza == null)
			try {
				this.setUtenza(AnagraficaManager.getUtenza(bd, this.getIdUtenza()));
			} catch (NotFoundException e) {
			}

		return this.utenza;
	}


	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	public String getUtenzaPrincipal() {
		return this.utenza != null ? this.utenza.getPrincipal() : null;
	}

	public String getUtenzaPrincipalOriginale() {
		return this.utenza != null ? this.utenza.getPrincipalOriginale() : null;
	}

	public boolean isUtenzaAbilitato() {
		return this.utenza != null ? this.utenza.isAbilitato() : false;
	}


	public IdUtenza getIdUtenza(BasicBD bd) throws ServiceException {
		Utenza u = this.getUtenza(bd);
		if(u != null && this.idUtenzaObj == null) {
			this.idUtenzaObj = new IdUtenza();
			this.idUtenzaObj.setId(u.getId());
			this.idUtenzaObj.setPrincipal(u.getPrincipal());
			this.idUtenzaObj.setPrincipalOriginale(u.getPrincipalOriginale());
			this.idUtenzaObj.setAbilitato(u.isAbilitato());
		}

		return this.idUtenzaObj;
	}

	public static Acl mergeAcls(List<Acl> listaServizio) {
		Acl aclR = null, aclW = null;

		for (Acl acl : listaServizio) {
			if(acl.getListaDiritti().contains(Diritti.SCRITTURA) && acl.getListaDiritti().contains(Diritti.LETTURA)) {
				return acl;
			} else if(acl.getListaDiritti().contains(Diritti.SCRITTURA)) {
				aclW = acl;
			} else if(acl.getListaDiritti().contains(Diritti.LETTURA)) {
				aclR = acl;
			} else { // donothing
			}
		}

		if(aclW != null)
			return aclW;

		if(aclR != null)
			return aclR;

		return null;
	}
}
