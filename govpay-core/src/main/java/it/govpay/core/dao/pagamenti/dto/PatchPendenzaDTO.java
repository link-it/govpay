package it.govpay.core.dao.pagamenti.dto;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.model.IAutorizzato;

public class PatchPendenzaDTO extends BasicCreateRequestDTO  {
	
	public enum STATO_PENDENZA { ANNULLATO, DA_PAGARE}
	
	private String idA2a;
	private String idPendenza;
	private STATO_PENDENZA stato;
	private String descrizioneStato;

	public PatchPendenzaDTO(IAutorizzato user) {
		super(user);
	}

	public String getIdA2a() {
		return idA2a;
	}

	public void setIdA2a(String idA2a) {
		this.idA2a = idA2a;
	}

	public String getIdPendenza() {
		return idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}


	public String getDescrizioneStato() {
		return descrizioneStato;
	}

	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	public STATO_PENDENZA getStato() {
		return stato;
	}

	public void setStato(STATO_PENDENZA stato) {
		this.stato = stato;
	}

}
