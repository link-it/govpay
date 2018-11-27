package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.model.StatoTracciatoPendenza;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.orm.Tracciato;
import it.govpay.orm.constants.StatoTracciatoType;

public class ListaTracciatiDTO extends BasicFindRequestDTO{

	
	private List<TIPO_TRACCIATO> tipoTracciato = null;
	private String operatore;
	private STATO_ELABORAZIONE statoTracciato = null;
	private StatoTracciatoPendenza statoTracciatoPendenza = null;
	private String idDominio;
	private String dettaglioStato = null;
	
	public ListaTracciatiDTO(IAutorizzato user) {
		super(user);
		this.addDefaultSort(Tracciato.model().DATA_CARICAMENTO,SortOrder.DESC);
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
		if(this.statoTracciatoPendenza != null) {
			switch (this.statoTracciatoPendenza) {
			case ESEGUITO:
			case ESEGUITO_CON_ERRORI:
				this.statoTracciato = STATO_ELABORAZIONE.COMPLETATO;
				break;
			case IN_ATTESA:
			case IN_ELABORAZIONE:
				this.statoTracciato = STATO_ELABORAZIONE.ELABORAZIONE;
				break;
			case SCARTATO:
			default:
				this.statoTracciato = STATO_ELABORAZIONE.SCARTATO;
				break;
			}
		}
		
		return this.statoTracciato;
	}
	
	public String getDettaglioStato() {
		if(this.statoTracciatoPendenza != null) {
			switch (this.statoTracciatoPendenza) {
			case ESEGUITO:
				this.dettaglioStato = StatoTracciatoType.CARICAMENTO_OK.getValue();
				break;
			case ESEGUITO_CON_ERRORI:
				this.dettaglioStato =  StatoTracciatoType.CARICAMENTO_KO.getValue();
				break;
			case IN_ATTESA:
				this.dettaglioStato = StatoTracciatoType.NUOVO.getValue();
				break;
			case IN_ELABORAZIONE:
				this.dettaglioStato = StatoTracciatoType.IN_CARICAMENTO.getValue();
				break;
			case SCARTATO:
			default:
				this.dettaglioStato = null;
				break;
			}
		}
		
		return dettaglioStato;
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
