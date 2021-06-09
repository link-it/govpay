package it.govpay.ragioneria.v2.beans.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Pagamento;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.ragioneria.v2.beans.NuovaRiconciliazione;
import it.govpay.ragioneria.v2.beans.Riconciliazione;
import it.govpay.ragioneria.v2.beans.RiconciliazioneIndex;
import it.govpay.ragioneria.v2.beans.RiscossioneIndex;

public class RiconciliazioniConverter {

	
	public static RichiestaIncassoDTO toRichiestaIncassoDTO(NuovaRiconciliazione incassoPost, String idDominio, Authentication user) {
		RichiestaIncassoDTO dto = new RichiestaIncassoDTO(user);
		dto.setCausale(incassoPost.getCausale());
		dto.setDataValuta(incassoPost.getDataValuta());
		dto.setDataContabile(incassoPost.getDataContabile());
		dto.setImporto(incassoPost.getImporto());
		dto.setCodDominio(idDominio);
		GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
		dto.setApplicazione(authenticationDetails.getApplicazione());
		dto.setOperatore(authenticationDetails.getOperatore());
		dto.setSct(incassoPost.getSct());
		dto.setIuv(incassoPost.getIuv());
		dto.setIdFlusso(incassoPost.getIdFlusso());
		return dto;
	}
	
	
	public static Riconciliazione toRsModel(it.govpay.bd.model.Incasso i) throws ServiceException, NotFoundException, IOException, ValidationException {
		Riconciliazione rsModel = new Riconciliazione();
		
		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setImporto(i.getImporto());
		rsModel.setIdRiconciliazione(i.getTrn());
		rsModel.setIdDominio(i.getCodDominio());
		rsModel.setSct(i.getSct());
		rsModel.setContoAccredito(i.getIbanAccredito());
		if(i.getPagamenti()!= null) {
			List<RiscossioneIndex> riscossioni = new ArrayList<>();
			for (Pagamento pagamento : i.getPagamenti()) {
				riscossioni.add(RiscossioniConverter.toRsModelIndexOld(pagamento));
			} 
			
			rsModel.setRiscossioni(riscossioni);
		}
		
		return rsModel;
	}
	
	public static RiconciliazioneIndex toRsIndexModel(it.govpay.bd.model.Incasso i) throws ServiceException {
		RiconciliazioneIndex rsModel = new RiconciliazioneIndex();
		
		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setImporto(i.getImporto());
		rsModel.setIdRiconciliazione(i.getTrn());
		rsModel.setIdDominio(i.getCodDominio());
		rsModel.setSct(i.getSct());
		rsModel.setContoAccredito(i.getIbanAccredito());
		
		return rsModel;
	}
}
