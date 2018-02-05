package it.govpay.core.dao.pagamenti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;

public class PendenzeDAO extends BasicBD{
	
	private static Logger log = LogManager.getLogger();
	
	public PendenzeDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public LeggiPendenzaDTOResponse leggiPendenza(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException{
		
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();
		
		VersamentiBD versamentiBD = new VersamentiBD(this);
		Versamento versamento;
		try {
			
			versamento = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, leggiPendenzaDTO.getCodA2A()).getId(), leggiPendenzaDTO.getCodPendenza());
			
			response.setVersamento(versamento);
			response.setApplicazione(versamento.getApplicazione(versamentiBD));
			response.setDominio(versamento.getDominio(versamentiBD));
			response.setUnitaOperativa(versamento.getUo(versamentiBD));
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		}
		return response;
	}
}
