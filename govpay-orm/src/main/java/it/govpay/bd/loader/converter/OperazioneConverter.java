package it.govpay.bd.loader.converter;

import it.govpay.bd.loader.model.Operazione;
import it.govpay.model.loader.Operazione.StatoOperazioneType;
import it.govpay.model.loader.Operazione.TipoOperazioneType;
import it.govpay.orm.IdApplicazione;
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
		dto.setTipoOperazione(TipoOperazioneType.valueOf(vo.getTipoOperazione().name()));
		dto.setLineaElaborazione(vo.getLineaElaborazione());
		if(vo.getStato() != null)
		dto.setStato(StatoOperazioneType.valueOf(vo.getStato().name()));

		
		dto.setDatiRichiesta(vo.getDatiRichiesta());
		dto.setDatiRisposta(vo.getDatiRisposta());
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
		vo.setTipoOperazione(it.govpay.orm.constants.TipoOperazioneType.valueOf(dto.getTipoOperazione().name()));
		vo.setLineaElaborazione(dto.getLineaElaborazione());
		if(dto.getStato() != null)
			vo.setStato(it.govpay.orm.constants.StatoOperazioneType.valueOf(dto.getStato().name()));

		
		vo.setDatiRichiesta(dto.getDatiRichiesta());
		vo.setDatiRisposta(dto.getDatiRisposta());
		vo.setDettaglioEsito(dto.getDettaglioEsito());

		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(dto.getIdTracciato());
		vo.setIdTracciato(idTracciato);

		if(dto.getIdApplicazione() != null) {
			IdApplicazione idApp = new IdApplicazione();
			idApp.setId(dto.getIdApplicazione());
			vo.setIdApplicazione(idApp);
		}

		vo.setCodVersamentoEnte(dto.getCodVersamentoEnte());
		
		return vo;
	}
	
	public static Operazione toDettaglioDTO(it.govpay.orm.Operazione vo) throws ServiceException {
		Operazione dto = new Operazione();
		
		dto.setId(vo.getId());
		dto.setTipoOperazione(TipoOperazioneType.valueOf(vo.getTipoOperazione().name()));
		dto.setLineaElaborazione(vo.getLineaElaborazione());
		if(vo.getStato() != null)
		dto.setStato(StatoOperazioneType.valueOf(vo.getStato().name()));

		
		dto.setDatiRichiesta(vo.getDatiRichiesta());
		dto.setDatiRisposta(vo.getDatiRisposta());
		dto.setDettaglioEsito(vo.getDettaglioEsito());

		dto.setIdTracciato(vo.getIdTracciato().getIdTracciato());
		
		if(dto.getIdApplicazione() != null) {
			IdApplicazione idApp = new IdApplicazione();
			idApp.setId(dto.getIdApplicazione());
			vo.setIdApplicazione(idApp);
		}
		
		dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());

		return dto;
	}

}
