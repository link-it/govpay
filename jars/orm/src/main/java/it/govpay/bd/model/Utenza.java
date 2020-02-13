package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.TipoVersamento;

public class Utenza extends it.govpay.model.Utenza {

	private static final long serialVersionUID = 1L;

	protected transient List<IdUnitaOperativa> dominiUo;
//	protected transient List<Dominio> domini;
	protected transient List<TipoVersamento> tipiVersamento; 
	protected transient List<Acl> aclPrincipal;
	protected transient List<Acl> aclRuoliEsterni;
	protected transient List<Acl> aclRuoliUtenza;
	protected transient Map<String,List<Acl>> ruoliUtenza;
	protected transient Map<String, List<String>> headers;

	public TIPO_UTENZA getTipoUtenza() { 
		return TIPO_UTENZA.ANONIMO;
	}
	public String getIdentificativo() {
		return this.getPrincipal();
	}
	
	public List<Acl> getAclsNoRuoli() {
		List<Acl> collect = new ArrayList<>();
		if(this.aclPrincipal!=null)
			collect.addAll(this.aclPrincipal);
		if(this.aclRuoliEsterni!=null)
			collect.addAll(this.aclRuoliEsterni);
		return collect;
	}

	public List<Acl> getAcls() {
		List<Acl> collect = new ArrayList<>();
		if(this.aclPrincipal!=null)
			collect.addAll(this.aclPrincipal);
		if(this.aclRuoliEsterni!=null)
			collect.addAll(this.aclRuoliEsterni);
		if(this.aclRuoliUtenza!=null)
			collect.addAll(this.aclRuoliUtenza);
		return collect;
	}
	
	public List<Acl> getAclsProfilo() {
		List<Acl> collect = new ArrayList<>();
		
		Map<Servizio, List<Acl>> mapServizio = new HashMap<Acl.Servizio, List<Acl>>();
		
		List<Acl> acls = this.getAcls();
		for (Acl acl : acls) {
			
			List<Acl> remove = mapServizio.remove(acl.getServizio());
			
			if(remove == null)
				remove = new ArrayList<Acl>();
			
			remove.add(acl);
			mapServizio.put(acl.getServizio(), remove);
		}
		
		Map<Servizio,Acl> sorted = mapServizio.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(e -> e.getKey(), e -> Acl.mergeAcls(e.getValue()), (e1, e2) -> e2, LinkedHashMap::new));
		
		collect.addAll(sorted.values());
		
		return collect;
	}
	
	
	public boolean isIdDominioAutorizzato(Long idDominio) {
		if(this.dominiUo != null) {
			for(IdUnitaOperativa id: this.dominiUo) {
				if(id.getIdDominio() != null) {
					if(id.getIdDominio().longValue() == idDominio.longValue())
						return true;
				}
			}
		}
		
		return false;
	}
	
	public List<Long> getIdDomini() { // TODO
		if(this.dominiUo != null) {
			List<Long> idDomini = new ArrayList<Long>();
			
			for(IdUnitaOperativa id: this.dominiUo) {
				if(id.getIdDominio() != null) {
					if(!idDomini.contains(id.getIdDominio()))
						idDomini.add(id.getIdDominio());
				}
			}
			
			return idDomini;
		}
		
		return null;
		
//		return this.domini != null ? this.domini.stream().map(d -> d.getCodDominio()).collect(Collectors.toList()) : null;
	}

	public List<String> getIdDominio() { // TODO
		if(this.dominiUo != null) {
			List<String> codDomini = new ArrayList<String>();
			
			for(IdUnitaOperativa id: this.dominiUo) {
				if(id.getCodDominio() != null) {
					if(!codDomini.contains(id.getCodDominio()))
						codDomini.add(id.getCodDominio());
				}
			}
			
			return codDomini;
		}
		
		return null;
		
//		return this.domini != null ? this.domini.stream().map(d -> d.getCodDominio()).collect(Collectors.toList()) : null;
	}

	public List<String> getIdTipoVersamento() {
		return this.tipiVersamento != null ? this.tipiVersamento.stream().map(d -> d.getCodTipoVersamento()).collect(Collectors.toList()) : null;
	}
	
	public List<IdUnitaOperativa> getDominiUo(String codDominio) {
		if(this.dominiUo != null) {
			List<IdUnitaOperativa> listaUO = new ArrayList<IdUnitaOperativa>();
			for (IdUnitaOperativa idUnitaOperativa : dominiUo) {
				if(idUnitaOperativa.getCodDominio() != null && idUnitaOperativa.getCodDominio().equals(codDominio))
					listaUO.add(idUnitaOperativa);
			}
			return listaUO;
		}
		return null;
	}
	
	public List<IdUnitaOperativa> getDominiUo() {
		return dominiUo;
	}
	
	public List<IdUnitaOperativa> getDominiUo(BasicBD bd) throws ServiceException {
		if(this.dominiUo == null) {
			this.dominiUo = new ArrayList<>();
			if(this.getIdDominiUo() != null) {
				for(it.govpay.model.IdUnitaOperativa id: this.getIdDominiUo()) {
					IdUnitaOperativa idUo = new IdUnitaOperativa(id);
					try {
						
						if(idUo.getIdDominio() != null) {
							Dominio dominio = AnagraficaManager.getDominio(bd, idUo.getIdDominio());
							idUo.setCodDominio(dominio.getCodDominio());
							idUo.setRagioneSociale(dominio.getRagioneSociale());
						}
					} catch (NotFoundException e) {
					}
					try {
						if(idUo.getIdUnita() != null) {
							UnitaOperativa uo = AnagraficaManager.getUnitaOperativa(bd, idUo.getIdUnita());
							idUo.setCodUO(uo.getCodUo());
							idUo.setRagioneSocialeUO(uo.getAnagrafica().getRagioneSociale());
						}
						this.dominiUo.add(idUo);
					} catch (NotFoundException e) {
					}
				}
			}
		}
		return this.dominiUo;
	}

	public List<TipoVersamento> getTipiVersamento(BasicBD bd) throws ServiceException {
		if(this.tipiVersamento == null) {
			this.tipiVersamento = new ArrayList<>();
			if(this.getIdTipiVersamento() != null) {
				for(Long id: this.getIdTipiVersamento()) {
					try {
						this.tipiVersamento.add(AnagraficaManager.getTipoVersamento(bd, id));
					} catch (NotFoundException e) {
					}
				}
			}
		}
		return this.tipiVersamento;
	}
	
	public Map<String,List<Acl>> getRuoliUtenza() throws ServiceException {
		if(this.ruoliUtenza == null && this.getRuoli() != null) {
			this.ruoliUtenza = new HashMap<>();
			for(String idRuolo : this.getRuoli()) {
				for (Acl aclUtenza: this.aclRuoliUtenza) {
					if(aclUtenza.getRuolo() != null && aclUtenza.getRuolo().equals(idRuolo)) {
						List<Acl> remove = this.ruoliUtenza.remove(idRuolo);
						
						if(remove == null) {
							remove = new ArrayList<>();
						} 
						remove.add(aclUtenza);
						this.ruoliUtenza.put(idRuolo, remove);
					}
				}
			}
		}
		return this.ruoliUtenza;
	}


	public void setDominiUo(List<IdUnitaOperativa> dominiUo) {
		this.dominiUo = dominiUo;
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
	public List<Acl> getAclRuoliEsterni() {
		return aclRuoliEsterni;
	}
	public void setAclRuoliEsterni(List<Acl> aclRuoliEsterni) {
		this.aclRuoliEsterni = aclRuoliEsterni;
	}
	public List<Acl> getAclRuoliUtenza() {
		return aclRuoliUtenza;
	}
	public void setAclRuoliUtenza(List<Acl> aclRuoliUtenza) {
		this.aclRuoliUtenza = aclRuoliUtenza;
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
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}
}
