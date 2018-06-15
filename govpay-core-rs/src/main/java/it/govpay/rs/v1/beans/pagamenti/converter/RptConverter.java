package it.govpay.rs.v1.beans.pagamenti.converter;

import javax.xml.bind.JAXBException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.xml.sax.SAXException;

import it.govpay.core.rs.v1.beans.pagamenti.Rpp;
import it.govpay.core.rs.v1.beans.pagamenti.RppIndex;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.UriBuilderUtils;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(versamento));

		try {
			rsModel.setRpt(JaxbUtils.toRPT(rpt.getXmlRpt()));
			
			if(rpt.getXmlRt() != null) {
				rsModel.setRt(JaxbUtils.toRT(rpt.getXmlRt()));
			}
		} catch(SAXException e) {
			throw new ServiceException(e);
		} catch (JAXBException e) {
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
			rsModel.setRpt(JaxbUtils.toRPT(rpt.getXmlRpt()));
			
			if(rpt.getXmlRt() != null) {
				rsModel.setRt(JaxbUtils.toRT(rpt.getXmlRt()));
			}
		} catch(SAXException e) {
			throw new ServiceException(e);
		} catch (JAXBException e) {
			throw new ServiceException(e);
		}
		
		return rsModel;
	}
}
