package it.govpay.bd.model.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Documento;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdDominio;

public class DocumentoConverter {

	public static List<Documento> toDTOList(List<it.govpay.orm.Documento> anagraficaLst) throws ServiceException {
		List<Documento> lstDTO = new ArrayList<>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(it.govpay.orm.Documento anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Documento toDTO(it.govpay.orm.Documento vo) throws ServiceException {
		Documento dto = new Documento();
		dto.setCodDocumento(vo.getCodDocumento());
		dto.setDescrizione(vo.getDescrizione());
		dto.setId(vo.getId());
		dto.setIdApplicazione(vo.getIdApplicazione().getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		return dto;
	}

	public static it.govpay.orm.Documento toVO(Documento dto) throws ServiceException {
		it.govpay.orm.Documento vo = new it.govpay.orm.Documento();
		vo.setId(dto.getId());
	
		IdApplicazione idApplicazione = new IdApplicazione();
		idApplicazione.setId(dto.getIdApplicazione());
		vo.setIdApplicazione(idApplicazione);
		
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		vo.setCodDocumento(dto.getCodDocumento());
		vo.setDescrizione(dto.getDescrizione());
		return vo;
	}
}
