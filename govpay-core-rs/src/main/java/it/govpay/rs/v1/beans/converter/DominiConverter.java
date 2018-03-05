package it.govpay.rs.v1.beans.converter;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTO;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTO;
import it.govpay.model.Anagrafica;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Tributo.TipoContabilta;
import it.govpay.rs.v1.beans.base.DominioPost;
import it.govpay.rs.v1.beans.base.EntrataPost;
import it.govpay.rs.v1.beans.base.IbanAccreditoPost;
import it.govpay.rs.v1.beans.base.UnitaOperativaPost;

public class DominiConverter {
	
	public static PutEntrataDominioDTO getPutEntrataDominioDTO(EntrataPost entrataRequest, String idDominio, String idEntrata, IAutorizzato user) {
		PutEntrataDominioDTO entrataDTO = new PutEntrataDominioDTO(user);
		
		Tributo tributo = new Tributo();
		
	    tributo.setAbilitato(entrataRequest.isAbilitato());
		tributo.setCodContabilitaCustom(entrataRequest.getCodiceContabilita());
		tributo.setCodTributo(idEntrata);
		if(entrataRequest.getCodificaIUV()!=null)
			tributo.setCodTributoIuvCustom(entrataRequest.getCodificaIUV()+"");
//		tributo.setDescrizione(entrataRequest.getDescrizione()); //TODO
		if(entrataRequest.getTipoContabilita() != null) {
			switch (entrataRequest.getTipoContabilita()) {
			case ALTRO:
				tributo.setTipoContabilitaCustom(TipoContabilta.ALTRO);
				break;
			case ENTRATA:
				tributo.setTipoContabilitaCustom(TipoContabilta.CAPITOLO);
				break;
			case SIOPE:
				tributo.setTipoContabilitaCustom(TipoContabilta.SIOPE);
				break;
			case SPECIALE:
				tributo.setTipoContabilitaCustom(TipoContabilta.SPECIALE);
				break;
			}
		}
		
		entrataDTO.setIbanAccredito(entrataRequest.getIbanAccreditoBancario());
	    entrataDTO.setIbanAccreditoPostale(entrataRequest.getIbanAccreditoPostale());
		entrataDTO.setTributo(tributo);
		entrataDTO.setIdDominio(idDominio);
		entrataDTO.setIdTributo(idEntrata);
				
		return entrataDTO;		
	}
	
	public static PutIbanAccreditoDTO getPutIbanAccreditoDTO(IbanAccreditoPost ibanAccreditoPost, String idDominio, String idIbanAccredito, IAutorizzato user) {
		PutIbanAccreditoDTO ibanAccreditoDTO = new PutIbanAccreditoDTO(user);
		
		IbanAccredito iban = new IbanAccredito();
		
		iban.setAbilitato(ibanAccreditoPost.isAbilitato());
		iban.setAttivatoObep(ibanAccreditoPost.isMybank());
		iban.setCodBicAccredito(ibanAccreditoPost.getBicAccredito());
		iban.setCodBicAppoggio(ibanAccreditoPost.getBicAppoggio());
		iban.setCodIban(idIbanAccredito);
		iban.setCodIbanAppoggio(ibanAccreditoPost.getIbanAppoggio());
		iban.setPostale(ibanAccreditoPost.isPostale());
		
		ibanAccreditoDTO.setIban(iban);
		ibanAccreditoDTO.setIdDominio(idDominio);
		ibanAccreditoDTO.setIbanAccredito(idIbanAccredito);
				
		return ibanAccreditoDTO;		
	}
	
	public static PutUnitaOperativaDTO getPutUnitaOperativaDTO(UnitaOperativaPost uoPost, String idDominio, String idUo, IAutorizzato user) {
		PutUnitaOperativaDTO uoDTO = new PutUnitaOperativaDTO(user);
		
		UnitaOperativa uo = new UnitaOperativa();
		uo.setAbilitato(uoPost.isAbilitato());
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(uoPost.getCap());
		anagrafica.setCivico(uoPost.getCivico());
		anagrafica.setCodUnivoco(idUo);
		anagrafica.setEmail(uoPost.getEmail());
		anagrafica.setFax(uoPost.getFax());
		anagrafica.setIndirizzo(uoPost.getIndirizzo());
		anagrafica.setLocalita(uoPost.getLocalita());
		anagrafica.setNazione(uoPost.getNazione());
		anagrafica.setProvincia(uoPost.getProvincia());
		anagrafica.setRagioneSociale(uoPost.getRagioneSociale());
		anagrafica.setTelefono(uoPost.getTel());
		anagrafica.setUrlSitoWeb(uoPost.getWeb());
		
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
		anagrafica.setCivico(dominioPost.getCivico());
		anagrafica.setCodUnivoco(idDominio);
		anagrafica.setEmail(dominioPost.getEmail());
		anagrafica.setFax(dominioPost.getFax());
		anagrafica.setIndirizzo(dominioPost.getIndirizzo());
		anagrafica.setLocalita(dominioPost.getLocalita());
		anagrafica.setNazione(dominioPost.getNazione());
		anagrafica.setProvincia(dominioPost.getProvincia());
		anagrafica.setRagioneSociale(dominioPost.getRagioneSociale());
		anagrafica.setTelefono(dominioPost.getTel());
		anagrafica.setUrlSitoWeb(dominioPost.getWeb());
		
		dominio.setAnagrafica(anagrafica );
		if(dominioPost.getAuxDigit() != null)
			dominio.setAuxDigit(Integer.parseInt(dominioPost.getAuxDigit()));
		dominio.setCbill(dominioPost.getCbill());
		dominio.setCodDominio(idDominio);
		dominio.setGln(dominioPost.getGln());
		dominio.setIdApplicazioneDefault(null);
		
		dominio.setIuvPrefix(dominioPost.getIuvPrefix());
		if(dominioPost.getLogo() != null)
			dominio.setLogo(dominioPost.getLogo().getBytes());
		dominio.setNdpData(null);
		dominio.setNdpDescrizione(null);
		dominio.setNdpOperazione(null);
		dominio.setNdpStato(null);
		dominio.setRagioneSociale(dominioPost.getRagioneSociale());
		if(dominioPost.getSegregationCode() != null)
			dominio.setSegregationCode(Integer.parseInt(dominioPost.getSegregationCode()));
		
		
		dominioDTO.setDominio(dominio);
		dominioDTO.setIdDominio(idDominio);
		dominioDTO.setCodStazione(dominioPost.getStazione());
		return dominioDTO;		
	}
}
