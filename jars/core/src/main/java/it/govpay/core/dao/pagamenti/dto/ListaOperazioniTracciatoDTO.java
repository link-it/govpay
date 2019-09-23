package it.govpay.core.dao.pagamenti.dto;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.orm.Operazione;

public class ListaOperazioniTracciatoDTO extends BasicFindRequestDTO{

	private Long idTracciato;
	private StatoOperazioneType stato = null;
	private TipoOperazioneType tipo = null;
	
	public ListaOperazioniTracciatoDTO(Authentication user) {
		super(user);
		this.addDefaultSort(Operazione.model().LINEA_ELABORAZIONE,SortOrder.ASC);
	}

	public Long getIdTracciato() {
		return this.idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public StatoOperazioneType getStato() {
		return stato;
	}

	public void setStato(StatoOperazioneType stato) {
		this.stato = stato;
	}

	public TipoOperazioneType getTipo() {
		return tipo;
	}

	public void setTipo(TipoOperazioneType tipo) {
		this.tipo = tipo;
	}

	
}
