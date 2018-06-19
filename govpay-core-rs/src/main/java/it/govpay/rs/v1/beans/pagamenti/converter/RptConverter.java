package it.govpay.rs.v1.beans.pagamenti.converter;

import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.xml2json.Xml2JsonFactory;

import it.govpay.core.rs.v1.beans.pagamenti.Rpp;
import it.govpay.core.rs.v1.beans.pagamenti.RppIndex;
import it.govpay.core.utils.UriBuilderUtils;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(versamento));

		try {
			// Rimuovo il prefisso dagli elementi
			Map<String, String> map = new HashMap<String, String>();
			map.put("http://www.digitpa.gov.it/schemas/2011/Pagamenti/", "");
			
			String s = Xml2JsonFactory.getXml2JsonMapped(map).xml2json(new String(rpt.getXmlRpt()));
			
			// Rimuovo l'elemento radice RPT
			rsModel.setRpt(s.substring(7, s.length() - 1));
			
			if(rpt.getXmlRt() != null) {
				s = Xml2JsonFactory.getXml2JsonMapped(map).xml2json(new String(rpt.getXmlRt()));
				rsModel.setRt(s.substring(6, s.length() - 1));
			}
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		
		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());

		rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte()));
		
		try {
			// Rimuovo il prefisso dagli elementi
			Map<String, String> map = new HashMap<String, String>();
			map.put("http://www.digitpa.gov.it/schemas/2011/Pagamenti/", "");
			
			String s = Xml2JsonFactory.getXml2JsonMapped(map).xml2json(new String(rpt.getXmlRpt()));
			
			// Rimuovo l'elemento radice RPT
			rsModel.setRpt(s.substring(7, s.length() - 1));
			
			if(rpt.getXmlRt() != null) {
				s = Xml2JsonFactory.getXml2JsonMapped(map).xml2json(new String(rpt.getXmlRt()));
				rsModel.setRt(s.substring(6, s.length() - 1));
			}
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		
		return rsModel;
	}
}
