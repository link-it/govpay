package it.govpay.core.dao.pagamenti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;

public class RicevuteDAO extends BasicBD{
	
	private static Logger log = LogManager.getLogger();
	
	public RicevuteDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public LeggiRicevutaDTOResponse leggiRpt(LeggiRicevutaDTO leggiRicevutaDTO) throws ServiceException,RicevutaNonTrovataException{
		
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();
		
		RptBD rptBD = new RptBD(this);
		Rpt rpt;
		try {
			rpt = rptBD.getRpt(leggiRicevutaDTO.getIdDominio(), leggiRicevutaDTO.getIuv(), leggiRicevutaDTO.getCcp());
			
			if(rpt.getXmlRt() == null)
				throw new RicevutaNonTrovataException();
			
			response.setRpt(rpt);
			response.setDominio(rpt.getDominio(this));
			response.setVersamento(rpt.getVersamento(this));
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		}
		return response;
	}
}
