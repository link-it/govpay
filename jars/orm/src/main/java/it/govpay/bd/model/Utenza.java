package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.TipoVersamento;

public class Utenza extends it.govpay.model.Utenza {

	private static final long serialVersionUID = 1L;

	protected transient List<Dominio> domini;
	protected transient List<TipoVersamento> tipiVersamento;
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

	public List<String> getIdTipoVersamento() {
		return this.tipiVersamento != null ? this.tipiVersamento.stream().map(d -> d.getCodTipoVersamento()).collect(Collectors.toList()) : null;
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

	public List<TipoVersamento> getTipiVersamento(BasicBD bd) throws ServiceException {
		if(this.tipiVersamento == null) {
			this.tipiVersamento = new ArrayList<>();
			if(this.getIdTipiVersamento() != null) {
				for(Long id: this.getIdTipiVersamento()) {
					this.tipiVersamento.add(AnagraficaManager.getTipoVersamento(bd, id));
				}
			}
		}
		return this.tipiVersamento;
	}


	public void setDomini(List<Dominio> domini) {
		this.domini = domini;
	}

	public void setTipiVersamento(List<TipoVersamento> tributi) {
		this.tipiVersamento = tributi;
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
	
	public String getMessaggioUtenzaDisabilitata() {
		StringBuilder sb = new StringBuilder();
		sb.append("Utenza [").append(this.getIdentificativo()).append("] disabilitata");
		return sb.toString();
	}
	
	public String getMessaggioUtenzaNonAutorizzata() {
		StringBuilder sb = new StringBuilder();
		sb.append("Utenza [").append(this.getIdentificativo()).append("] non autorizzata ad accedere alla risorsa richiesta");
		return sb.toString();
	}
}
