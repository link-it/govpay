package it.govpay.core.dao.reportistica.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.reportistica.statistiche.FiltroRendicontazioni;

public class ListaRendicontazioniDTO extends BasicFindRequestDTO{
	
	public enum GROUP_BY { DIV, DIR, CODFLUSSO}
	
	public ListaRendicontazioniDTO(Authentication user) {
		super(user);
	}
	
	private FiltroRendicontazioni filtro;
	private List<GROUP_BY> groupBy = new ArrayList<ListaRendicontazioniDTO.GROUP_BY>();
	
	public FiltroRendicontazioni getFiltro() {
		return filtro;
	}
	public void setFiltro(FiltroRendicontazioni filtro) {
		this.filtro = filtro;
	}
	public List<GROUP_BY> getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(List<GROUP_BY> groupBy) {
		this.groupBy = groupBy;
	}
	
}
