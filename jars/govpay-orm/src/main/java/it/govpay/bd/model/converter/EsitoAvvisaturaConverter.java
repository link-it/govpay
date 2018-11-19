package it.govpay.bd.model.converter;

import it.govpay.bd.model.EsitoAvvisatura;
import it.govpay.orm.IdTracciato;

public class EsitoAvvisaturaConverter {

	public static EsitoAvvisatura toDTO(it.govpay.orm.EsitoAvvisatura vo) {
		EsitoAvvisatura dto = new EsitoAvvisatura();
		dto.setCodDominio(vo.getCodDominio());
		dto.setIdentificativoAvvisatura(vo.getIdentificativoAvvisatura());
		dto.setTipoCanale(vo.getTipoCanale());
		dto.setCodCanale(vo.getCodCanale());
		dto.setData(vo.getData());
		dto.setCodEsito(vo.getCodEsito());
		dto.setDescrizioneEsito(vo.getDescrizioneEsito());
		dto.setIdTracciato(vo.getIdTracciato().getId());

		return dto;
	}

	public static it.govpay.orm.EsitoAvvisatura toVO(it.govpay.model.EsitoAvvisatura dto)  {
		it.govpay.orm.EsitoAvvisatura vo = new it.govpay.orm.EsitoAvvisatura();
		vo.setId(dto.getId());
		vo.setCodDominio(dto.getCodDominio());
		vo.setIdentificativoAvvisatura(dto.getIdentificativoAvvisatura());
		vo.setTipoCanale(dto.getTipoCanale());
		vo.setCodCanale(dto.getCodCanale());
		vo.setData(dto.getData());
		vo.setCodEsito(dto.getCodEsito());
		vo.setDescrizioneEsito(dto.getDescrizioneEsito());
		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(dto.getIdTracciato());
		idTracciato.setIdTracciato(dto.getIdTracciato());
		
		vo.setIdTracciato(idTracciato);

		return vo;
	}

}
