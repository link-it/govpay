package it.govpay.backoffice.v1.beans.converter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ContiAccredito;
import it.govpay.backoffice.v1.beans.ContiAccreditoPost;
import it.govpay.backoffice.v1.beans.Dominio;
import it.govpay.backoffice.v1.beans.DominioIndex;
import it.govpay.backoffice.v1.beans.DominioPost;
import it.govpay.backoffice.v1.beans.Entrata;
import it.govpay.backoffice.v1.beans.EntrataPost;
import it.govpay.backoffice.v1.beans.TipoContabilita;
import it.govpay.backoffice.v1.beans.TipoPendenzaDominio;
import it.govpay.backoffice.v1.beans.TipoPendenzaDominioPost;
import it.govpay.backoffice.v1.beans.TipoPendenzaForm;
import it.govpay.backoffice.v1.beans.TipoPendenzaPromemoria;
import it.govpay.backoffice.v1.beans.TipoPendenzaTrasformazione;
import it.govpay.backoffice.v1.beans.TracciatoCsv;
import it.govpay.backoffice.v1.beans.UnitaOperativa;
import it.govpay.backoffice.v1.beans.UnitaOperativaPost;
import it.govpay.backoffice.v1.beans.TipoPendenzaTrasformazione.TipoEnum;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Tributo;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTO;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTO;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Anagrafica;

public class DominiConverter {
	
	public static PutEntrataDominioDTO getPutEntrataDominioDTO(EntrataPost entrataRequest, String idDominio, String idEntrata, Authentication user) throws ValidationException {
		PutEntrataDominioDTO entrataDTO = new PutEntrataDominioDTO(user);
		
		it.govpay.bd.model.Tributo tributo = new it.govpay.bd.model.Tributo();
		
	    tributo.setAbilitato(entrataRequest.isAbilitato());
		tributo.setCodContabilitaCustom(entrataRequest.getCodiceContabilita());
		tributo.setCodTributo(idEntrata);
		if(entrataRequest.getTipoContabilita() != null) {
			
			// valore tipo contabilita non valido
			if(TipoContabilita.fromValue(entrataRequest.getTipoContabilita()) == null) {
				throw new ValidationException("Codifica inesistente per tipoContabilita. Valore fornito [" + entrataRequest.getTipoContabilita() + "] valori possibili " + ArrayUtils.toString(TipoContabilita.values()));
			}
			
			entrataRequest.setTipoContabilitaEnum(TipoContabilita.fromValue(entrataRequest.getTipoContabilita()));
			
			
			switch (entrataRequest.getTipoContabilitaEnum()) {
			case ALTRO:
				tributo.setTipoContabilitaCustom(it.govpay.bd.model.Tributo.TipoContabilita.ALTRO);
				break;
			case CAPITOLO:
				tributo.setTipoContabilitaCustom(it.govpay.bd.model.Tributo.TipoContabilita.CAPITOLO);
				break;
			case SIOPE:
				tributo.setTipoContabilitaCustom(it.govpay.bd.model.Tributo.TipoContabilita.SIOPE);
				break;
			case SPECIALE:
				tributo.setTipoContabilitaCustom(it.govpay.bd.model.Tributo.TipoContabilita.SPECIALE);
				break;
			}
		}
		
		entrataDTO.setIbanAccredito(entrataRequest.getIbanAccredito());
	    entrataDTO.setIbanAppoggio(entrataRequest.getIbanAppoggio());
		entrataDTO.setTributo(tributo);
		entrataDTO.setIdDominio(idDominio);
		entrataDTO.setIdTributo(idEntrata);
				
		return entrataDTO;		
	}
	public static PutIbanAccreditoDTO getPutIbanAccreditoDTO(ContiAccreditoPost ibanAccreditoPost, String idDominio, String idIbanAccredito, Authentication user) {
		PutIbanAccreditoDTO ibanAccreditoDTO = new PutIbanAccreditoDTO(user);
		
		it.govpay.bd.model.IbanAccredito iban = new it.govpay.bd.model.IbanAccredito();
		
		iban.setAbilitato(ibanAccreditoPost.isAbilitato());
		iban.setAttivatoObep(ibanAccreditoPost.isMybank());
		iban.setCodBic(ibanAccreditoPost.getBic());
		iban.setCodIban(idIbanAccredito);
		iban.setPostale(ibanAccreditoPost.isPostale());
		
		ibanAccreditoDTO.setIban(iban);
		ibanAccreditoDTO.setIdDominio(idDominio);
		ibanAccreditoDTO.setIbanAccredito(idIbanAccredito);
				
		return ibanAccreditoDTO;		
	}
	
	public static PutUnitaOperativaDTO getPutUnitaOperativaDTO(UnitaOperativaPost uoPost, String idDominio, String idUo, Authentication user) {
		PutUnitaOperativaDTO uoDTO = new PutUnitaOperativaDTO(user);
		
		it.govpay.bd.model.UnitaOperativa uo = new it.govpay.bd.model.UnitaOperativa();
		uo.setAbilitato(uoPost.isAbilitato());
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(uoPost.getCap());
		anagrafica.setCivico(uoPost.getCivico());
		anagrafica.setCodUnivoco(idUo);
		anagrafica.setEmail(uoPost.getEmail());
		anagrafica.setPec(uoPost.getPec());
		anagrafica.setFax(uoPost.getFax());
		anagrafica.setIndirizzo(uoPost.getIndirizzo());
		anagrafica.setLocalita(uoPost.getLocalita());
		anagrafica.setNazione(uoPost.getNazione());
		anagrafica.setProvincia(uoPost.getProvincia());
		anagrafica.setRagioneSociale(uoPost.getRagioneSociale());
		anagrafica.setTelefono(uoPost.getTel());
		anagrafica.setUrlSitoWeb(uoPost.getWeb());
		anagrafica.setArea(uoPost.getArea());
		
		uo.setAnagrafica(anagrafica);
		uo.setCodUo(idUo);
		
		uoDTO.setUo(uo );
		uoDTO.setIdDominio(idDominio);
		uoDTO.setIdUo(idUo);
				
		return uoDTO;		
	}

	public static PutDominioDTO getPutDominioDTO(DominioPost dominioPost, String idDominio, Authentication user) {
		PutDominioDTO dominioDTO = new PutDominioDTO(user);
		
		it.govpay.bd.model.Dominio dominio = new it.govpay.bd.model.Dominio();
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
		anagrafica.setPec(dominioPost.getPec());
		anagrafica.setArea(dominioPost.getArea()); 
		
		dominio.setAnagrafica(anagrafica );
		if(dominioPost.getAuxDigit() != null)
			dominio.setAuxDigit(Integer.parseInt(dominioPost.getAuxDigit()));
		dominio.setCbill(dominioPost.getCbill());
		dominio.setCodDominio(idDominio);
		dominio.setGln(dominioPost.getGln());
		dominio.setIdApplicazioneDefault(null);
		
		dominio.setIuvPrefix(dominioPost.getIuvPrefix());
		if(dominioPost.getLogo() != null) {
			dominio.setLogo(dominioPost.getLogo().getBytes(StandardCharsets.UTF_8));
		}
		dominio.setNdpData(null);
		dominio.setNdpDescrizione(null);
		dominio.setNdpOperazione(null);
		dominio.setNdpStato(null);
		dominio.setRagioneSociale(dominioPost.getRagioneSociale());
		if(dominioPost.getSegregationCode() != null)
			dominio.setSegregationCode(Integer.parseInt(dominioPost.getSegregationCode()));

		dominio.setAutStampaPoste(dominioPost.getAutStampaPosteItaliane());
		
		dominioDTO.setDominio(dominio);
		dominioDTO.setIdDominio(idDominio);
		dominioDTO.setCodStazione(dominioPost.getStazione());
		
		return dominioDTO;		
	}
	
	
	
	public static DominioIndex toRsModelIndex(it.govpay.bd.model.Dominio dominio) throws ServiceException {
		DominioIndex rsModel = new DominioIndex();
		rsModel.setWeb(dominio.getAnagrafica().getUrlSitoWeb());
		rsModel.setIdDominio(dominio.getCodDominio()); 
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		rsModel.setIndirizzo(dominio.getAnagrafica().getIndirizzo());
		rsModel.setCivico(dominio.getAnagrafica().getCivico());
		rsModel.setCap(dominio.getAnagrafica().getCap());
		rsModel.setLocalita(dominio.getAnagrafica().getLocalita());
		rsModel.setProvincia(dominio.getAnagrafica().getProvincia());
		rsModel.setNazione(dominio.getAnagrafica().getNazione());
		rsModel.setEmail(dominio.getAnagrafica().getEmail());
		rsModel.setPec(dominio.getAnagrafica().getPec()); 
		rsModel.setTel(dominio.getAnagrafica().getTelefono());
		rsModel.setFax(dominio.getAnagrafica().getFax());
		rsModel.setArea(dominio.getAnagrafica().getArea());
		rsModel.setGln(dominio.getGln());
		rsModel.setCbill(dominio.getCbill());
		rsModel.setAuxDigit("" + dominio.getAuxDigit());
		if(dominio.getSegregationCode() != null)
			rsModel.setSegregationCode(String.format("%02d", dominio.getSegregationCode()));
		
		if(dominio.getLogo() != null) {
			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
		}
		rsModel.setIuvPrefix(dominio.getIuvPrefix());
		rsModel.setStazione(dominio.getStazione().getCodStazione());
		rsModel.setContiAccredito(UriBuilderUtils.getContiAccreditoByDominio(dominio.getCodDominio()));
		rsModel.setUnitaOperative(UriBuilderUtils.getListUoByDominio(dominio.getCodDominio()));
		rsModel.setEntrate(UriBuilderUtils.getEntrateByDominio(dominio.getCodDominio()));
		rsModel.setTipiPendenza(UriBuilderUtils.getTipiPendenzaByDominio(dominio.getCodDominio()));
		rsModel.setAbilitato(dominio.isAbilitato());
		rsModel.setAutStampaPosteItaliane(dominio.getAutStampaPoste());
		
		return rsModel;
	}
	
	public static Dominio toRsModel(it.govpay.bd.model.Dominio dominio, List<it.govpay.bd.model.UnitaOperativa> uoLst, List<it.govpay.bd.model.Tributo> tributoLst, List<it.govpay.bd.model.IbanAccredito> ibanAccreditoLst,
			List<TipoVersamentoDominio> tipoVersamentoDominioLst) throws ServiceException {
		Dominio rsModel = new Dominio();
		rsModel.setWeb(dominio.getAnagrafica().getUrlSitoWeb()); 
		rsModel.setIdDominio(dominio.getCodDominio()); 
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		rsModel.setIndirizzo(dominio.getAnagrafica().getIndirizzo());
		rsModel.setCivico(dominio.getAnagrafica().getCivico());
		rsModel.setCap(dominio.getAnagrafica().getCap());
		rsModel.setLocalita(dominio.getAnagrafica().getLocalita());
		rsModel.setProvincia(dominio.getAnagrafica().getProvincia());
		rsModel.setNazione(dominio.getAnagrafica().getNazione());
		rsModel.setEmail(dominio.getAnagrafica().getEmail());
		rsModel.setPec(dominio.getAnagrafica().getPec()); 
		rsModel.setTel(dominio.getAnagrafica().getTelefono());
		rsModel.setFax(dominio.getAnagrafica().getFax());
		rsModel.setArea(dominio.getAnagrafica().getArea());
		rsModel.setGln(dominio.getGln());
		rsModel.setCbill(dominio.getCbill());
		rsModel.setAuxDigit("" + dominio.getAuxDigit());
		if(dominio.getSegregationCode() != null)
			rsModel.setSegregationCode(String.format("%02d", dominio.getSegregationCode()));
		
		if(dominio.getLogo() != null) {
			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
		}
		rsModel.setIuvPrefix(dominio.getIuvPrefix());
		rsModel.setStazione(dominio.getStazione().getCodStazione());
		
		if(uoLst != null) {
			List<UnitaOperativa> unitaOperative = new ArrayList<>();
			
			for(it.govpay.bd.model.UnitaOperativa uo: uoLst) {
				unitaOperative.add(toUnitaOperativaRsModel(uo));
			}
			rsModel.setUnitaOperative(unitaOperative);
		}

		if(ibanAccreditoLst != null) {
			List<ContiAccredito> contiAccredito = new ArrayList<>();
			
			for(it.govpay.bd.model.IbanAccredito iban: ibanAccreditoLst) {
				contiAccredito.add(toIbanRsModel(iban));
			}
			rsModel.setContiAccredito(contiAccredito);
		}

		if(tributoLst != null) {
			List<Entrata> entrate = new ArrayList<>();
			
			for(Tributo tributo: tributoLst) {
				entrate.add(toEntrataRsModel(tributo, tributo.getIbanAccredito(), tributo.getIbanAppoggio()));
			}
			rsModel.setEntrate(entrate);
		}
		
		if(tipoVersamentoDominioLst != null) {
			List<TipoPendenzaDominio> tipiPendenzaDominio = new ArrayList<>();
			
			for(TipoVersamentoDominio tvd: tipoVersamentoDominioLst) {
				tipiPendenzaDominio.add(toTipoPendenzaRsModel(tvd));
			}
			rsModel.setTipiPendenza(tipiPendenzaDominio);
		}
		
		rsModel.setAbilitato(dominio.isAbilitato());
		rsModel.setAutStampaPosteItaliane(dominio.getAutStampaPoste());
		
		if(dominio.getLogo() != null) {
			rsModel.setLogo(new String(dominio.getLogo(), StandardCharsets.UTF_8));  
		}
		
		return rsModel;
	}
	
	public static ContiAccredito toIbanRsModel(it.govpay.bd.model.IbanAccredito iban) throws ServiceException {
		ContiAccredito rsModel = new ContiAccredito();
		rsModel.abilitato(iban.isAbilitato())
		.bic(iban.getCodBic())
		.iban(iban.getCodIban())
		.mybank(iban.isAttivatoObep())
		.postale(iban.isPostale());
		
		return rsModel;
	}
	
	
	public static UnitaOperativa toUnitaOperativaRsModel(it.govpay.bd.model.UnitaOperativa uo) throws IllegalArgumentException, ServiceException {
		UnitaOperativa rsModel = new UnitaOperativa();
		
		rsModel.setCap(uo.getAnagrafica().getCap());
		rsModel.setCivico(uo.getAnagrafica().getCivico());
		rsModel.setIdUnita(uo.getAnagrafica().getCodUnivoco());
		rsModel.setIndirizzo(uo.getAnagrafica().getIndirizzo());
		rsModel.setLocalita(uo.getAnagrafica().getLocalita());
		rsModel.setRagioneSociale(uo.getAnagrafica().getRagioneSociale());
		rsModel.setWeb(uo.getAnagrafica().getUrlSitoWeb());
		rsModel.setProvincia(uo.getAnagrafica().getProvincia());
		rsModel.setNazione(uo.getAnagrafica().getNazione());
		rsModel.setEmail(uo.getAnagrafica().getEmail());
		rsModel.setPec(uo.getAnagrafica().getPec());
		rsModel.setTel(uo.getAnagrafica().getTelefono());
		rsModel.setFax(uo.getAnagrafica().getFax());
		rsModel.setArea(uo.getAnagrafica().getArea());
		rsModel.setAbilitato(uo.isAbilitato());
		
		return rsModel;
	}
	
	public static Entrata toEntrataRsModel(GetTributoDTOResponse response) throws ServiceException {
		return toEntrataRsModel(response.getTributo(), response.getIbanAccredito(), response.getIbanAppoggio());
	}
	
	public static Entrata toEntrataRsModel(it.govpay.bd.model.Tributo tributo, it.govpay.model.IbanAccredito ibanAccredito,
			it.govpay.model.IbanAccredito ibanAppoggio) throws ServiceException {
		Entrata rsModel = new Entrata();
		rsModel.codiceContabilita(tributo.getCodContabilitaCustom())
		.abilitato(tributo.isAbilitato())
		.idEntrata(tributo.getCodTributo())
		.tipoEntrata(EntrateConverter.toTipoEntrataRsModel(tributo));
		
		if(tributo.getTipoContabilitaCustom() != null) {
			switch (tributo.getTipoContabilitaCustom()) {
			case ALTRO:
				rsModel.tipoContabilita(TipoContabilita.ALTRO);
				break;
			case CAPITOLO:
				rsModel.tipoContabilita(TipoContabilita.CAPITOLO);
				break;
			case SIOPE:
				rsModel.tipoContabilita(TipoContabilita.SIOPE);
				break;
			case SPECIALE:
				rsModel.tipoContabilita(TipoContabilita.SPECIALE);
				break;
			}
		}
		
		if(ibanAccredito != null)
			rsModel.setIbanAccredito(ibanAccredito.getCodIban());
		
		if(ibanAppoggio != null)
			rsModel.setIbanAppoggio(ibanAppoggio.getCodIban());
		
		return rsModel;
	}
	
	public static TipoPendenzaDominio toTipoPendenzaRsModel(GetTipoPendenzaDominioDTOResponse response) throws ServiceException {
		return toTipoPendenzaRsModel(response.getTipoVersamento());
	}
	
	public static TipoPendenzaDominio toTipoPendenzaRsModel(it.govpay.model.TipoVersamentoDominio tipoVersamentoDominio) throws ServiceException {
		TipoPendenzaDominio rsModel = new TipoPendenzaDominio();
		
		rsModel.descrizione(tipoVersamentoDominio.getDescrizione())
		.idTipoPendenza(tipoVersamentoDominio.getCodTipoVersamento())
		.codificaIUV(tipoVersamentoDominio.getCodificaIuvDefault())
		.abilitato(tipoVersamentoDominio.isAbilitatoDefault())
		.pagaTerzi(tipoVersamentoDominio.getPagaTerziDefault());
		
		if(tipoVersamentoDominio.getTipoDefault() != null) {
			switch (tipoVersamentoDominio.getTipoDefault()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.backoffice.v1.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.backoffice.v1.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		if(tipoVersamentoDominio.getFormDefinizioneDefault() != null && tipoVersamentoDominio.getFormTipoDefault() != null) {
			TipoPendenzaForm form = new TipoPendenzaForm();
			form.setTipo(tipoVersamentoDominio.getFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamentoDominio.getFormDefinizioneDefault())); 
			rsModel.setForm(form);
		}
		
		if(tipoVersamentoDominio.getPromemoriaAvvisoMessaggioDefault() != null && tipoVersamentoDominio.getPromemoriaAvvisoOggettoDefault() != null
				&& tipoVersamentoDominio.getPromemoriaAvvisoTipoDefault() != null) {
			TipoPendenzaPromemoria promemoria = new TipoPendenzaPromemoria();
			promemoria.setOggetto(new RawObject(tipoVersamentoDominio.getPromemoriaAvvisoOggettoDefault()));
			promemoria.setMessaggio(new RawObject(tipoVersamentoDominio.getPromemoriaAvvisoMessaggioDefault()));
			promemoria.setAllegaPdf(tipoVersamentoDominio.getPromemoriaAvvisoPdfDefault());
			promemoria.setTipo(tipoVersamentoDominio.getPromemoriaAvvisoTipoDefault());
			rsModel.setPromemoriaAvviso(promemoria);
		}
		
		if(tipoVersamentoDominio.getPromemoriaRicevutaMessaggioDefault() != null && tipoVersamentoDominio.getPromemoriaRicevutaOggettoDefault() != null
				&& tipoVersamentoDominio.getPromemoriaRicevutaTipoDefault() != null) {
			TipoPendenzaPromemoria promemoria = new TipoPendenzaPromemoria();
			promemoria.setOggetto(new RawObject(tipoVersamentoDominio.getPromemoriaRicevutaOggettoDefault()));
			promemoria.setMessaggio(new RawObject(tipoVersamentoDominio.getPromemoriaRicevutaMessaggioDefault()));
			promemoria.setAllegaPdf(tipoVersamentoDominio.getPromemoriaRicevutaPdfDefault());
			promemoria.setTipo(tipoVersamentoDominio.getPromemoriaRicevutaTipoDefault());
			rsModel.setPromemoriaRicevuta(promemoria);
		}
		
		if(tipoVersamentoDominio.getTrasformazioneTipoDefault() != null && tipoVersamentoDominio.getTrasformazioneDefinizioneDefault() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamentoDominio.getTrasformazioneTipoDefault());
			trasformazione.setDefinizione(new RawObject(tipoVersamentoDominio.getTrasformazioneDefinizioneDefault())); 
			rsModel.setTrasformazione(trasformazione);
		}
		if(tipoVersamentoDominio.getValidazioneDefinizioneDefault() != null)
			rsModel.setValidazione(new RawObject(tipoVersamentoDominio.getValidazioneDefinizioneDefault()));
		
		if(tipoVersamentoDominio.getTracciatoCsvHeaderRispostaDefault() != null && tipoVersamentoDominio.getTracciatoCsvFreemarkerRichiestaDefault() != null
				&& tipoVersamentoDominio.getTracciatoCsvFreemarkerRispostaDefault() != null) {
			TracciatoCsv tracciatoCsv = new TracciatoCsv();
			tracciatoCsv.setResponseHeader(tipoVersamentoDominio.getTracciatoCsvHeaderRispostaDefault());
			tracciatoCsv.setFreemarkerRequest(new RawObject(tipoVersamentoDominio.getTracciatoCsvFreemarkerRichiestaDefault()));
			tracciatoCsv.setFreemarkerResponse(new RawObject(tipoVersamentoDominio.getTracciatoCsvFreemarkerRispostaDefault()));
			rsModel.setTracciatoCsv(tracciatoCsv);
		}
		
		rsModel.setInoltro(tipoVersamentoDominio.getCodApplicazioneDefault());
		
		if(tipoVersamentoDominio.getVisualizzazioneDefinizioneDefault() != null)
			rsModel.setVisualizzazione(new RawObject(tipoVersamentoDominio.getVisualizzazioneDefinizioneDefault()));
		
		TipoPendenzaDominioPost valori = new TipoPendenzaDominioPost();
		
		valori.codificaIUV(tipoVersamentoDominio.getCodificaIuvCustom())
		.pagaTerzi(tipoVersamentoDominio.getPagaTerziCustom())
		.abilitato(tipoVersamentoDominio.getAbilitatoCustom());
		
		if(tipoVersamentoDominio.getFormDefinizioneCustom() != null && tipoVersamentoDominio.getFormTipoCustom() != null) {
			TipoPendenzaForm form = new TipoPendenzaForm();
			form.setTipo(tipoVersamentoDominio.getFormTipoCustom());
			form.setDefinizione(new RawObject(tipoVersamentoDominio.getFormDefinizioneCustom())); 
			valori.setForm(form);
		}
		
		if(tipoVersamentoDominio.getPromemoriaAvvisoMessaggioCustom() != null && tipoVersamentoDominio.getPromemoriaAvvisoOggettoCustom() != null
				&& tipoVersamentoDominio.getPromemoriaAvvisoTipoCustom() != null) {
			TipoPendenzaPromemoria promemoria = new TipoPendenzaPromemoria();
			promemoria.setOggetto(new RawObject(tipoVersamentoDominio.getPromemoriaAvvisoOggettoCustom()));
			promemoria.setMessaggio(new RawObject(tipoVersamentoDominio.getPromemoriaAvvisoMessaggioCustom()));
			promemoria.setAllegaPdf(tipoVersamentoDominio.getPromemoriaAvvisoPdfCustom());
			promemoria.setTipo(tipoVersamentoDominio.getPromemoriaAvvisoTipoCustom());
			valori.setPromemoriaAvviso(promemoria);
		}
		
		if(tipoVersamentoDominio.getPromemoriaRicevutaMessaggioCustom() != null && tipoVersamentoDominio.getPromemoriaRicevutaOggettoCustom() != null
				&& tipoVersamentoDominio.getPromemoriaRicevutaTipoCustom() != null) {
			TipoPendenzaPromemoria promemoria = new TipoPendenzaPromemoria();
			promemoria.setOggetto(new RawObject(tipoVersamentoDominio.getPromemoriaRicevutaOggettoCustom()));
			promemoria.setMessaggio(new RawObject(tipoVersamentoDominio.getPromemoriaRicevutaMessaggioCustom()));
			promemoria.setAllegaPdf(tipoVersamentoDominio.getPromemoriaRicevutaPdfCustom());
			promemoria.setTipo(tipoVersamentoDominio.getPromemoriaRicevutaTipoCustom());
			valori.setPromemoriaRicevuta(promemoria);
		}
		
		if(tipoVersamentoDominio.getTrasformazioneTipoCustom() != null && tipoVersamentoDominio.getTrasformazioneDefinizioneCustom() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamentoDominio.getTrasformazioneTipoCustom());
			trasformazione.setDefinizione(new RawObject(tipoVersamentoDominio.getTrasformazioneDefinizioneCustom())); 
			valori.setTrasformazione(trasformazione);
		}
		if(tipoVersamentoDominio.getValidazioneDefinizioneCustom() != null)
			valori.setValidazione(new RawObject(tipoVersamentoDominio.getValidazioneDefinizioneCustom()));
		
		valori.setInoltro(tipoVersamentoDominio.getCodApplicazioneCustom());
		
		if(tipoVersamentoDominio.getVisualizzazioneDefinizioneCustom() != null)
			valori.setVisualizzazione(new RawObject(tipoVersamentoDominio.getVisualizzazioneDefinizioneCustom()));
		
		if(tipoVersamentoDominio.getTracciatoCsvHeaderRispostaCustom() != null && tipoVersamentoDominio.getTracciatoCsvFreemarkerRichiestaCustom() != null
				&& tipoVersamentoDominio.getTracciatoCsvFreemarkerRispostaCustom() != null) {
			TracciatoCsv tracciatoCsv = new TracciatoCsv();
			tracciatoCsv.setResponseHeader(tipoVersamentoDominio.getTracciatoCsvHeaderRispostaCustom());
			tracciatoCsv.setFreemarkerRequest(new RawObject(tipoVersamentoDominio.getTracciatoCsvFreemarkerRichiestaCustom()));
			tracciatoCsv.setFreemarkerResponse(new RawObject(tipoVersamentoDominio.getTracciatoCsvFreemarkerRispostaCustom()));
			valori.setTracciatoCsv(tracciatoCsv);
		}
		
		rsModel.setValori(valori);
		
		return rsModel;
	}
	
	public static PutTipoPendenzaDominioDTO getPutTipoPendenzaDominioDTO(TipoPendenzaDominioPost tipoPendenzaRequest, String idDominio, String idTipoPendenza, Authentication user) throws ValidationException, ServiceException {
		PutTipoPendenzaDominioDTO tipoPendenzaDTO = new PutTipoPendenzaDominioDTO(user);
		
		it.govpay.bd.model.TipoVersamentoDominio tipoVersamentoDominio = new it.govpay.bd.model.TipoVersamentoDominio();
		
		tipoVersamentoDominio.setCodTipoVersamento(idTipoPendenza);
		tipoVersamentoDominio.setCodificaIuvCustom(tipoPendenzaRequest.getCodificaIUV());
		tipoVersamentoDominio.setAbilitatoCustom(tipoPendenzaRequest.Abilitato());
		tipoVersamentoDominio.setPagaTerziCustom(tipoPendenzaRequest.PagaTerzi());
		
		if(tipoPendenzaRequest.getForm() != null) {
			Object definizione = tipoPendenzaRequest.getForm().getDefinizione();
//			if(definizione instanceof String)
//				tipoVersamentoDominio.setFormDefinizioneCustom((String) definizione);
//			else
				tipoVersamentoDominio.setFormDefinizioneCustom(ConverterUtils.toJSON(definizione,null));
			tipoVersamentoDominio.setFormTipoCustom(tipoPendenzaRequest.getForm().getTipo());
		}
		
		if(tipoPendenzaRequest.getPromemoriaAvviso() != null) {
			tipoVersamentoDominio.setPromemoriaAvvisoMessaggioCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getPromemoriaAvviso().getMessaggio(),null));
			tipoVersamentoDominio.setPromemoriaAvvisoOggettoCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getPromemoriaAvviso().getOggetto(),null));
			tipoVersamentoDominio.setPromemoriaAvvisoPdfCustom(tipoPendenzaRequest.getPromemoriaAvviso().AllegaPdf());
			tipoVersamentoDominio.setPromemoriaAvvisoTipoCustom(tipoPendenzaRequest.getPromemoriaAvviso().getTipo());
		}
		
		if(tipoPendenzaRequest.getPromemoriaRicevuta() != null) {
			tipoVersamentoDominio.setPromemoriaRicevutaMessaggioCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getPromemoriaRicevuta().getMessaggio(),null));
			tipoVersamentoDominio.setPromemoriaRicevutaOggettoCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getPromemoriaRicevuta().getOggetto(),null));
			tipoVersamentoDominio.setPromemoriaRicevutaPdfCustom(tipoPendenzaRequest.getPromemoriaRicevuta().AllegaPdf());
			tipoVersamentoDominio.setPromemoriaRicevutaTipoCustom(tipoPendenzaRequest.getPromemoriaRicevuta().getTipo());
		}
		
		if(tipoPendenzaRequest.getTrasformazione() != null) {
			if(tipoPendenzaRequest.getTrasformazione().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoEnum.fromValue(tipoPendenzaRequest.getTrasformazione().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + tipoPendenzaRequest.getTrasformazione().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoEnum.values()));
				}
			}
			
			Object definizione = tipoPendenzaRequest.getTrasformazione().getDefinizione();
//			if(definizione instanceof String)
//				tipoVersamentoDominio.setTrasformazioneDefinizioneCustom((String) definizione);
//			else
				tipoVersamentoDominio.setTrasformazioneDefinizioneCustom(ConverterUtils.toJSON(definizione,null));
			
			tipoVersamentoDominio.setTrasformazioneTipoCustom(tipoPendenzaRequest.getTrasformazione().getTipo());
		}
		if(tipoPendenzaRequest.getValidazione() != null)
			tipoVersamentoDominio.setValidazioneDefinizioneCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getValidazione(),null));
		
		if(tipoPendenzaRequest.getInoltro() != null)
			tipoVersamentoDominio.setCodApplicazioneCustom(tipoPendenzaRequest.getInoltro());
		
		if(tipoPendenzaRequest.getVisualizzazione() != null)
			tipoVersamentoDominio.setVisualizzazioneDefinizioneCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getVisualizzazione(),null));
		
		if(tipoPendenzaRequest.getTracciatoCsv() != null && tipoPendenzaRequest.getTracciatoCsv().getResponseHeader() != null
				&& tipoPendenzaRequest.getTracciatoCsv().getFreemarkerRequest() != null
				&& tipoPendenzaRequest.getTracciatoCsv().getFreemarkerResponse() != null) {
			tipoVersamentoDominio.setTracciatoCsvHeaderRispostaCustom(tipoPendenzaRequest.getTracciatoCsv().getResponseHeader());
			tipoVersamentoDominio.setTracciatoCsvFreemarkerRichiestaCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getTracciatoCsv().getFreemarkerRequest(),null));
			tipoVersamentoDominio.setTracciatoCsvFreemarkerRispostaCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getTracciatoCsv().getFreemarkerResponse(),null));
		}
		
		tipoPendenzaDTO.setTipoVersamentoDominio(tipoVersamentoDominio);
		tipoPendenzaDTO.setIdDominio(idDominio);
		tipoPendenzaDTO.setCodTipoVersamento(idTipoPendenza);
		
		
				
		return tipoPendenzaDTO;		
	}
}
