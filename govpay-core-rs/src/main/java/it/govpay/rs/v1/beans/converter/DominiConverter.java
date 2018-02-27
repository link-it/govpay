package it.govpay.rs.v1.beans.converter;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTO;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTO;
import it.govpay.model.Anagrafica;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.base.DominioPost;
import it.govpay.rs.v1.beans.base.IbanAccreditoPost;
import it.govpay.rs.v1.beans.base.UnitaOperativaPost;

public class DominiConverter {
	
	public static PutIbanAccreditoDTO getPutIbanAccreditoDTO(IbanAccreditoPost ibanAccreditoPost, String idDominio, String idIbanAccredito, IAutorizzato user) {
		PutIbanAccreditoDTO ibanAccreditoDTO = new PutIbanAccreditoDTO(user);
		
		IbanAccredito iban = new IbanAccredito();
		
		iban.setAbilitato(ibanAccreditoPost.isAbilitato());
		iban.setAttivatoObep(ibanAccreditoPost.isMybank());
		iban.setCodBicAccredito(ibanAccreditoPost.getBicAccredito());
		iban.setCodBicAppoggio(ibanAccreditoPost.getBicAppoggio());
		iban.setCodIban(idIbanAccredito);
		iban.setCodIbanAppoggio(ibanAccreditoPost.getIbanAppoggio());
//		iban.setIdNegozio(idNegozio);
//		iban.setIdSellerBank(idSellerBank);
		iban.setPostale(ibanAccreditoPost.isPostale());
		
		ibanAccreditoDTO.setIban(iban);
		ibanAccreditoDTO.setIdDominio(idDominio);
		ibanAccreditoDTO.setIbanAccredito(idIbanAccredito);
				
		return ibanAccreditoDTO;		
	}
	
	public static PutUnitaOperativaDTO getPutUnitaOperativaDTO(UnitaOperativaPost uoPost, String idDominio, String idUo, IAutorizzato user) {
		PutUnitaOperativaDTO uoDTO = new PutUnitaOperativaDTO(user);
		
		UnitaOperativa uo = new UnitaOperativa();
		//uo.setAbilitato(uoPost.isAbilitato());
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(uoPost.getCap());
		//anagrafica.setCellulare(uoPost.getcell);
		anagrafica.setCivico(uoPost.getCivico());
		anagrafica.setCodUnivoco(idUo);
		anagrafica.setIndirizzo(uoPost.getIndirizzo());
		anagrafica.setLocalita(uoPost.getLocalita());
		// anagrafica.setNazione(uoPost.getNazione());
		// anagrafica.setProvincia(uoPost.getProvincia());
		anagrafica.setRagioneSociale(uoPost.getRagioneSociale());
		// anagrafica.setUrlSitoWeb(uoPost.);
		
		uo.setAnagrafica(anagrafica);
		uo.setCodUo(idUo);
		
		uoDTO.setUo(uo );
		uoDTO.setIdDominio(idDominio);
		uoDTO.setIdUo(idUo);
				
		return uoDTO;		
	}

	public static PutDominioDTO getPutDominioDTO(DominioPost dominioPost, String idDominio, IAutorizzato user) {
		PutDominioDTO dominioDTO = new PutDominioDTO(user);
		
		Dominio dominio = new Dominio();
		dominio.setAbilitato(dominioPost.isAbilitato());
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(dominioPost.getCap());
		//anagrafica.setCellulare(dominioPost.getcell);
		anagrafica.setCivico(dominioPost.getCivico());
		anagrafica.setCodUnivoco(idDominio);
		anagrafica.setIndirizzo(dominioPost.getIndirizzo());
		anagrafica.setLocalita(dominioPost.getLocalita());
		// anagrafica.setNazione(dominioPost.getNazione());
		// anagrafica.setProvincia(dominioPost.getProvincia());
		anagrafica.setRagioneSociale(dominioPost.getRagioneSociale());
		// anagrafica.setUrlSitoWeb(dominioPost.);
		
		dominio.setAnagrafica(anagrafica );
		if(dominioPost.getAuxDigit() != null)
			dominio.setAuxDigit(Integer.parseInt(dominioPost.getAuxDigit()));
		//dominio.setCbill(dominioPost.getcbill());
		dominio.setCodDominio(idDominio);
		//dominio.setCustomIuv(dominioPost.getc);
		dominio.setGln(dominioPost.getGln());
		dominio.setIdApplicazioneDefault(null);
		
		dominio.setIuvPrefix(dominioPost.getIuvPrefix());
		//dominio.setIuvPrefixStrict(iuvPrefixStrict);
		if(dominioPost.getLogo() != null)
			dominio.setLogo(dominioPost.getLogo().getBytes());
//		dominio.setNdpData(null);
//		dominio.setNdpDescrizione(null);
//		dominio.setNdpOperazione(null);
//		dominio.setNdpStato(null);
		dominio.setRagioneSociale(dominioPost.getRagioneSociale());
		// dominio.setRiusoIuv(dominioPost.riuso);
		if(dominioPost.getSegregationCode() != null)
			dominio.setSegregationCode(Integer.parseInt(dominioPost.getSegregationCode()));
		
		
		dominioDTO.setDominio(dominio);
		dominioDTO.setIdDominio(idDominio);
		dominioDTO.setCodStazione(dominioPost.getStazione());
		return dominioDTO;		
	}
}
