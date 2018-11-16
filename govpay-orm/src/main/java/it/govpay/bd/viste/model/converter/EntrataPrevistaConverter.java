package it.govpay.bd.viste.model.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.viste.model.EntrataPrevista;

public class EntrataPrevistaConverter {

	public static List<EntrataPrevista> toDTOList(List<it.govpay.orm.VistaRiscossioni> listaVistaRiscossioni) throws ServiceException {
		List<EntrataPrevista> lstDTO = new ArrayList<>();
		if(listaVistaRiscossioni != null && !listaVistaRiscossioni.isEmpty()) {
			for(it.govpay.orm.VistaRiscossioni riscossione: listaVistaRiscossioni){
				lstDTO.add(toDTO(riscossione));
			}
		}
		return lstDTO;
	}

	public static EntrataPrevista toDTO(it.govpay.orm.VistaRiscossioni vo) throws ServiceException {
		EntrataPrevista dto = new EntrataPrevista();
		dto.setCodApplicazione(vo.getCodApplicazione());
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodFlusso(vo.getCodFlusso());
		dto.setCodSingoloVersamentoEnte(vo.getCodSingoloVersamentoEnte());
		dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
		dto.setData(vo.getData());
		dto.setDataRegolamento(vo.getDataRegolamento());
		dto.setFrIur(vo.getFrIur());
		if(vo.getImportoPagato()!= null)
			dto.setImportoPagato(new BigDecimal(vo.getImportoPagato().doubleValue()));
		if(vo.getImportoTotalePagamenti()!= null)			
			dto.setImportoTotalePagamenti(new BigDecimal(vo.getImportoTotalePagamenti().doubleValue()));
		dto.setIndiceDati(vo.getIndiceDati());
		dto.setIur(vo.getIur());
		dto.setIuv(vo.getIuv());
		dto.setNumeroPagamenti(vo.getNumeroPagamenti());

		return dto;
	}
}
