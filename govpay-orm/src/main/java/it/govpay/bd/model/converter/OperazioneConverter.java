package it.govpay.bd.model.converter;

import it.govpay.bd.model.Operazione;
import it.govpay.orm.IdTracciato;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class OperazioneConverter {


	public static List<Operazione> toDTOList(List<it.govpay.orm.Operazione> anagraficaLst) throws ServiceException {
		List<Operazione> lstDTO = new ArrayList<Operazione>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(it.govpay.orm.Operazione anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Operazione toDTO(it.govpay.orm.Operazione vo) throws ServiceException {
		Operazione dto = new Operazione();
		
		dto.setId(vo.getId());
		dto.setTipoOperazione(vo.getTipoOperazione());
		dto.setLineaElaborazione(vo.getLineaElaborazione());
		dto.setStato(vo.getStato());

		
		dto.setDatiRichiesta(vo.getDatiRichiesta());
		dto.setDatiRisposta(vo.getDatiRisposta());
		dto.setEsito(vo.getEsito());
		dto.setDettaglioEsito(vo.getDettaglioEsito());

		dto.setIdTracciato(vo.getIdTracciato().getIdTracciato());
		
		if(vo.getIdApplicazione() != null)
			dto.setIdApplicazione(vo.getIdApplicazione().getId());
		
		dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());

		return dto;
	}

	public static it.govpay.orm.Operazione toVO(Operazione dto) throws ServiceException {
		it.govpay.orm.Operazione vo = new it.govpay.orm.Operazione();
		vo.setId(dto.getId());
		vo.setTipoOperazione(dto.getTipoOperazione());
		vo.setLineaElaborazione(dto.getLineaElaborazione());
		vo.setStato(dto.getStato());

		
		vo.setDatiRichiesta(dto.getDatiRichiesta());
		vo.setDatiRisposta(dto.getDatiRisposta());
		vo.setDettaglioEsito(dto.getDettaglioEsito());
		vo.setEsito(dto.getEsito());

		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(dto.getIdTracciato());
		vo.setIdTracciato(idTracciato);

		if(vo.getIdApplicazione() != null)
			dto.setIdApplicazione(vo.getIdApplicazione().getId());

		vo.setCodVersamentoEnte(dto.getCodVersamentoEnte());
		
		return vo;
	}

}
