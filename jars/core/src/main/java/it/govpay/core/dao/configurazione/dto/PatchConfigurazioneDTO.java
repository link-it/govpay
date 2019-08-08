package it.govpay.core.dao.configurazione.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.pagamenti.dto.AbstractPatchDTO;

public class PatchConfigurazioneDTO  extends AbstractPatchDTO  {

	public PatchConfigurazioneDTO(Authentication user) {
		super(user);
	}
}
