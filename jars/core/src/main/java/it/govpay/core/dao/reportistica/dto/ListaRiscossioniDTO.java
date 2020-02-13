package it.govpay.core.dao.reportistica.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.reportistica.statistiche.FiltroRiscossioni;

public class ListaRiscossioniDTO extends BasicFindRequestDTO{
	
	public enum GROUP_BY { DIV, DIR, DOMINIO, TIPO_PENDENZA, UO, APPLICAZIONE, TASSONOMIA}
	
	public ListaRiscossioniDTO(Authentication user) {
		super(user);
	}
	
	private FiltroRiscossioni filtro;
	private List<GROUP_BY> groupBy = new ArrayList<ListaRiscossioniDTO.GROUP_BY>();
	
	public FiltroRiscossioni getFiltro() {
		return filtro;
	}
	public void setFiltro(FiltroRiscossioni filtro) {
		this.filtro = filtro;
	}
	public List<GROUP_BY> getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(List<GROUP_BY> groupBy) {
		this.groupBy = groupBy;
	}
	
}
