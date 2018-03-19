package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;

public class Utenza extends it.govpay.model.Utenza implements IAutorizzato {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient List<Dominio> domini;
	private transient List<Tributo> tributi;
	private transient List<Acl> aclPrincipal;
	private transient List<Acl> aclRuoli;
	private List<String> ruoli;
	
	@Override
	public List<Acl> getAcls() {
		List<Acl> collect = new ArrayList<>();
		if(this.aclPrincipal!=null)
			collect.addAll(this.aclPrincipal);
		if(this.aclRuoli!=null)
			collect.addAll(this.aclRuoli);
		return collect;
	}

	@Override
	public List<String> getIdDominio() {
		return this.domini != null ? this.domini.stream().map(d -> d.getCodDominio()).collect(Collectors.toList()) : null;
	}

	@Override
	public List<String> getIdTributo() {
		return this.tributi != null ? this.tributi.stream().map(d -> d.getCodTributo()).collect(Collectors.toList()) : null;
	}

	@Override
	public List<String> getRuoli() {
		return this.ruoli;
	}

	@Override
	public void setRuoli(List<String> ruoli) {
		this.ruoli = ruoli;
	}

	public List<Dominio> getDomini(BasicBD bd) throws ServiceException {
		if(this.domini == null) {
			this.domini = new ArrayList<>();
			if(this.getIdDomini() != null) {
				for(Long id: this.getIdDomini()) {
					this.domini.add(AnagraficaManager.getDominio(bd, id));
				}
			}
		}
		return domini;
	}

	public List<Tributo> getTributi(BasicBD bd) throws ServiceException {
		if(this.tributi == null) {
			this.tributi = new ArrayList<>();
			if(this.getIdTributi() != null) {
				for(Long id: this.getIdTributi()) {
					this.tributi.add(AnagraficaManager.getTributo(bd, id));
				}
			}
		}
		return tributi;
	}


	public void setDomini(List<Dominio> domini) {
		this.domini = domini;
	}

	public void setTributi(List<Tributo> tributi) {
		this.tributi = tributi;
	}

	public void setAclPrincipal(List<Acl> aclPrincipal) {
		this.aclPrincipal = aclPrincipal;
	}

	public void setAclRuoli(List<Acl> aclRuoli) {
		this.aclRuoli = aclRuoli;
	}
	
	@Override
	public void merge(IAutorizzato src) throws ServiceException  {
		if(src instanceof Utenza) {
			Utenza srcUtenza = (Utenza) src;
			
			this.setAbilitato(srcUtenza.isAbilitato());
			this.setAclPrincipal(srcUtenza.getAcls());
			this.setIdDomini(srcUtenza.getIdDomini());
			this.setIdTributi(srcUtenza.getIdTributi());
			this.setId(srcUtenza.getId());
			this.setRuoli(srcUtenza.getRuoli());
			this.setDomini(srcUtenza.getDomini(null));
			this.setTributi(srcUtenza.getTributi(null));
		}
	}
}
