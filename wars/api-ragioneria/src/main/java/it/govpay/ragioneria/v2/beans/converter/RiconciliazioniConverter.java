package it.govpay.ragioneria.v2.beans.converter;

<<<<<<< HEAD
import java.io.IOException;
=======
import java.math.BigDecimal;
>>>>>>> feature/326_pagamenti_altri_intermediari_nelle_riconciliazioni
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Rendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.ragioneria.v2.beans.NuovaRiconciliazione;
import it.govpay.ragioneria.v2.beans.Riconciliazione;
import it.govpay.ragioneria.v2.beans.RiconciliazioneIndex;
import it.govpay.ragioneria.v2.beans.RiscossioneIndex;
import it.govpay.ragioneria.v2.beans.TipoRiscossione;

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
	
	
	public static Riconciliazione toRsModel(it.govpay.bd.model.Incasso i, Fr fr, List<TipoPagamento> riscossioniTipo) throws ServiceException, NotFoundException {
		Riconciliazione rsModel = new Riconciliazione();
		
		rsModel.setCausale(i.getCausale());
		rsModel.setDataContabile(i.getDataContabile());
		rsModel.setDataValuta(i.getDataValuta());
		rsModel.setImporto(i.getImporto());
		rsModel.setIdRiconciliazione(i.getTrn());
		rsModel.setIdDominio(i.getCodDominio());
		rsModel.setSct(i.getSct());
		rsModel.setContoAccredito(i.getIbanAccredito());
		List<RiscossioneIndex> riscossioni = new ArrayList<>();
		if(i.getPagamenti()!= null && riscossioniTipo.contains(TipoPagamento.ENTRATA)) {
			for (Pagamento pagamento : i.getPagamenti()) {
				riscossioni.add(RiscossioniConverter.toRsModelIndexOld(pagamento));
			} 
		}
		
		if(fr != null && riscossioniTipo.contains(TipoPagamento.ALTRO_INTERMEDIARIO)) {
			for(Rendicontazione r : fr.getRendicontazioni()) {
				if(r.getStato().equals(StatoRendicontazione.ALTRO_INTERMEDIARIO)) {
					RiscossioneIndex riscossioneIdx = new RiscossioneIndex();
					riscossioneIdx.setData(r.getData());
					riscossioneIdx.setImporto(r.getImporto());
					riscossioneIdx.setIndice(BigDecimal.valueOf(r.getIndiceDati()));
					riscossioneIdx.setIur(r.getIur());
					riscossioneIdx.setIuv(r.getIuv());
					riscossioneIdx.setTipo(TipoRiscossione.ALTRO_INTERMEDIARIO);
					riscossioni.add(riscossioneIdx);
				}
			}
		}
		rsModel.setRiscossioni(riscossioni);
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
