package it.govpay.core.dao.pagamenti.dto;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.orm.Operazione;

public class ListaOperazioniTracciatoDTO extends BasicFindRequestDTO{

	private Long idTracciato;
	
	public ListaOperazioniTracciatoDTO(IAutorizzato user) {
		super(user);
		this.addDefaultSort(Operazione.model().LINEA_ELABORAZIONE,SortOrder.ASC);
	}

	public Long getIdTracciato() {
		return this.idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	
}
