package it.govpay.rs.v1.beans.converter;

import it.govpay.bd.model.Dominio;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTO;
import it.govpay.model.Anagrafica;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.base.DominioPost;

public class DominiConverter {

	public static PutDominioDTO getPutDominioDTO(DominioPost dominioPost, String idDominio, IAutorizzato user) {
		PutDominioDTO dominioDTO = new PutDominioDTO(user);
		
		Dominio dominio = new Dominio();
		dominio.setAbilitato(dominioPost.isAbilitato());
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(dominioPost.getCap());
		anagrafica.setCivico(dominioPost.getCivico());
		anagrafica.setCodUnivoco(idDominio);
		anagrafica.setIndirizzo(dominioPost.getIndirizzo());
		anagrafica.setLocalita(dominioPost.getLocalita());
		// anagrafica.setNazione(dominioPost.getNazione());
		// anagrafica.setProvincia(dominioPost.getProvincia());
		anagrafica.setRagioneSociale(dominioPost.getRagioneSociale());
		// anagrafica.setTipo(dominioPost.);
		// anagrafica.setUrlSitoWeb(dominioPost.);
		
		dominio.setAnagrafica(anagrafica );
		dominio.setApplicazioneDefault(null);
		if(dominioPost.getAuxDigit() != null)
			dominio.setAuxDigit(Integer.parseInt(dominioPost.getAuxDigit()));
		//dominio.setCbill(dominioPost.getcbill());
		dominio.setCodDominio(idDominio);
		//dominio.setContiAccredito(contiAccredito);
		//dominio.setCustomIuv(dominioPost.getc);
		dominio.setGln(dominioPost.getGln());
		dominio.setIdApplicazioneDefault(null);
		
		dominio.setIuvPrefix(dominioPost.getIuvPrefix());
		//dominio.setIuvPrefixStrict(iuvPrefixStrict);
		dominio.setLogo(dominioPost.getLogo().getBytes());
//		dominio.setNdpData(null);
//		dominio.setNdpDescrizione(null);
//		dominio.setNdpOperazione(null);
//		dominio.setNdpStato(null);
		dominio.setRagioneSociale(dominioPost.getRagioneSociale());
		// dominio.setRiusoIuv(dominioPost.riuso);
		if(dominioPost.getSegregationCode() != null)
			dominio.setSegregationCode(Integer.parseInt(dominioPost.getSegregationCode()));
		
		//dominio.setTabellaControparti(tabellaControparti);
		
		dominioDTO.setDominio(dominio);
		dominioDTO.setIdDominio(idDominio);
		dominioDTO.setCodStazione(dominioPost.getStazione());
		return dominioDTO;		
	}
}
