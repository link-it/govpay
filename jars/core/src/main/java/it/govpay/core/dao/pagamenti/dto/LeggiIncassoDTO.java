package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.Pagamento.TipoPagamento;

public class LeggiIncassoDTO extends BasicFindRequestDTO {
	
	public LeggiIncassoDTO(Authentication user) {
		super(user);
	}
	
	private String idDominio;
	private String idIncasso;
	private List<TipoPagamento> tipoRiscossioni;
	
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIdIncasso() {
		return this.idIncasso;
	}
	public void setIdIncasso(String idIncasso) {
		this.idIncasso = idIncasso;
	}
	public List<TipoPagamento> getTipoRiscossioni() {
		return tipoRiscossioni;
	}
	public void setTipoRiscossioni(List<TipoPagamento> tipoRiscossioni) {
		this.tipoRiscossioni = tipoRiscossioni;
	}

}
