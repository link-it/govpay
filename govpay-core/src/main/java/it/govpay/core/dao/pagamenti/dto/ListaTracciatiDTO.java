package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.core.rs.v1.beans.base.StatoTracciatoPendenza;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.orm.Tracciato;

public class ListaTracciatiDTO extends BasicFindRequestDTO{

	
	private List<TIPO_TRACCIATO> tipoTracciato = null;
	private String operatore;
	private STATO_ELABORAZIONE statoTracciato = null;
	private StatoTracciatoPendenza statoTracciatoPendenza = null;
	private String idDominio;
	
	public ListaTracciatiDTO(IAutorizzato user) {
		super(user);
		this.setDefaultSort(Tracciato.model().DATA_CARICAMENTO,SortOrder.DESC);
	}

	public List<TIPO_TRACCIATO> getTipoTracciato() {
		return this.tipoTracciato;
	}

	public void setTipoTracciato(List<TIPO_TRACCIATO> tipoTracciato) {
		this.tipoTracciato = tipoTracciato;
	}

	public String getOperatore() {
		return this.operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public STATO_ELABORAZIONE getStatoTracciato() {
		return this.statoTracciato;
	}

	public void setStatoTracciato(STATO_ELABORAZIONE statoTracciato) {
		this.statoTracciato = statoTracciato;
	}

	public StatoTracciatoPendenza getStatoTracciatoPendenza() {
		return this.statoTracciatoPendenza;
	}

	public void setStatoTracciatoPendenza(StatoTracciatoPendenza statoTracciatoPendenza) {
		this.statoTracciatoPendenza = statoTracciatoPendenza;
	}

	public String getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
}
