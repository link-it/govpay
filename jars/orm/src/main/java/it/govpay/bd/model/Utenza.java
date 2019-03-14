package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Acl;
import it.govpay.model.TipoTributo;

public class Utenza extends it.govpay.model.Utenza {
	
	

	private static final long serialVersionUID = 1L;

	protected transient List<Dominio> domini;
	protected transient List<TipoTributo> tipiTributo;
	protected transient List<Acl> aclPrincipal;
	protected transient List<Acl> aclRuoli;
	protected List<String> ruoli;

	public TIPO_UTENZA getTipoUtenza() { 
		return TIPO_UTENZA.ANONIMO;
	}
	public String getIdentificativo() {
		return this.getPrincipal();
	}

	public List<Acl> getAcls() {
		List<Acl> collect = new ArrayList<>();
		if(this.aclPrincipal!=null)
			collect.addAll(this.aclPrincipal);
		if(this.aclRuoli!=null)
			collect.addAll(this.aclRuoli);
		return collect;
	}

	public List<String> getIdDominio() {
		return this.domini != null ? this.domini.stream().map(d -> d.getCodDominio()).collect(Collectors.toList()) : null;
	}

	public List<String> getIdTipoTributo() {
		return this.tipiTributo != null ? this.tipiTributo.stream().map(d -> d.getCodTributo()).collect(Collectors.toList()) : null;
	}

	public List<String> getRuoli() {
		return this.ruoli;
	}

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
		return this.domini;
	}

	public List<TipoTributo> getTipiTributo(BasicBD bd) throws ServiceException {
		if(this.tipiTributo == null) {
			this.tipiTributo = new ArrayList<>();
			if(this.getIdTipiTributo() != null) {
				for(Long id: this.getIdTipiTributo()) {
					this.tipiTributo.add(AnagraficaManager.getTipoTributo(bd, id));
				}
			}
		}
		return this.tipiTributo;
	}


	public void setDomini(List<Dominio> domini) {
		this.domini = domini;
	}

	public void setTipiributo(List<TipoTributo> tributi) {
		this.tipiTributo = tributi;
	}

	public void setAclPrincipal(List<Acl> aclPrincipal) {
		this.aclPrincipal = aclPrincipal;
	}

	public List<Acl> getAclPrincipal() {
		return aclPrincipal;
	}
	
	public void setAclRuoli(List<Acl> aclRuoli) {
		this.aclRuoli = aclRuoli;
	}
	
	public List<Acl> getAclRuoli() {
		return aclRuoli;
	}

//	@Override
//	public void merge(IAutorizzato src) throws ServiceException  {
//		if(src instanceof Utenza) {
//			Utenza srcUtenza = (Utenza) src;
//
//			this.setAbilitato(srcUtenza.isAbilitato());
//			this.setAclPrincipal(srcUtenza.getAcls());
//			this.setIdDomini(srcUtenza.getIdDomini());
//			this.setIdTributi(srcUtenza.getIdTributi());
//			this.setId(srcUtenza.getId());
//			this.setRuoli(srcUtenza.getRuoli());
//			this.setDomini(srcUtenza.getDomini(null));
//			this.setTributi(srcUtenza.getTributi(null));
//		}
//	}
}
