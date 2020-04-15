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
import it.govpay.backoffice.v1.beans.DominioProfiloIndex;
import it.govpay.backoffice.v1.beans.DominioProfiloPost;
import it.govpay.backoffice.v1.beans.Entrata;
import it.govpay.backoffice.v1.beans.EntrataPost;
import it.govpay.backoffice.v1.beans.TipoContabilita;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaAppIO;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaMail;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaMailPromemoriaAvviso;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaMailPromemoriaRicevuta;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaPromemoriaAvvisoBase;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaPromemoriaRicevutaBase;
import it.govpay.backoffice.v1.beans.TipoPendenzaAvvisaturaPromemoriaScadenza;
import it.govpay.backoffice.v1.beans.TipoPendenzaDominio;
import it.govpay.backoffice.v1.beans.TipoPendenzaDominioAvvisaturaAppIO;
import it.govpay.backoffice.v1.beans.TipoPendenzaDominioPost;
import it.govpay.backoffice.v1.beans.TipoPendenzaFormPortaleBackoffice;
import it.govpay.backoffice.v1.beans.TipoPendenzaFormPortalePagamenti;
import it.govpay.backoffice.v1.beans.TipoPendenzaPortaleBackofficeCaricamentoPendenze;
import it.govpay.backoffice.v1.beans.TipoPendenzaPortalePagamentiCaricamentoPendenze;
import it.govpay.backoffice.v1.beans.TipoPendenzaTrasformazione;
import it.govpay.backoffice.v1.beans.TipoTemplateTrasformazione;
import it.govpay.backoffice.v1.beans.TracciatoCsv;
import it.govpay.backoffice.v1.beans.UnitaOperativa;
import it.govpay.backoffice.v1.beans.UnitaOperativaIndex;
import it.govpay.backoffice.v1.beans.UnitaOperativaPost;
import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Tributo;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTO;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTO;
import it.govpay.core.dao.commons.Dominio.Uo;
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

	public static DominioProfiloIndex toRsModelProfiloIndex(it.govpay.core.dao.commons.Dominio dominio) throws ServiceException {
		return toRsModelProfiloIndex(dominio, dominio.getUo());

	}
	public static DominioProfiloIndex toRsModelProfiloIndex(it.govpay.core.dao.commons.Dominio dominio, List<it.govpay.core.dao.commons.Dominio.Uo> uoLst) throws ServiceException {
		DominioProfiloIndex rsModel = new DominioProfiloIndex();
		rsModel.setIdDominio(dominio.getCodDominio()); 
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		if(uoLst != null && !uoLst.isEmpty()) {
			List<UnitaOperativaIndex> unitaOperative = new ArrayList<>();

			for(it.govpay.core.dao.commons.Dominio.Uo uo: uoLst) {
				if(uo.getCodUo() != null)
					unitaOperative.add(toUnitaOperativaRsModelIndex(uo));
			}
			rsModel.setUnitaOperative(unitaOperative);
		}

		return rsModel;
	}

	public static DominioProfiloIndex toRsModelProfiloIndex(it.govpay.bd.model.Dominio dominio) throws ServiceException {
		return toRsModelProfiloIndex(dominio, dominio.getUnitaOperative());

	}
	public static DominioProfiloIndex toRsModelProfiloIndex(it.govpay.bd.model.Dominio dominio, List<it.govpay.bd.model.UnitaOperativa> uoLst) throws ServiceException {
		DominioProfiloIndex rsModel = new DominioProfiloIndex();
		rsModel.setIdDominio(dominio.getCodDominio()); 
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		if(uoLst != null) {
			List<UnitaOperativaIndex> unitaOperative = new ArrayList<>();

			for(it.govpay.bd.model.UnitaOperativa uo: uoLst) {
				unitaOperative.add(toUnitaOperativaRsModelIndex(uo));
			}
			rsModel.setUnitaOperative(unitaOperative);
		}

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

	public static UnitaOperativaIndex toUnitaOperativaRsModelIndex(it.govpay.core.dao.commons.Dominio.Uo uo) throws IllegalArgumentException, ServiceException {
		UnitaOperativaIndex rsModel = new UnitaOperativaIndex();

		rsModel.setIdUnita(uo.getCodUo());
		rsModel.setRagioneSociale(uo.getRagioneSociale());

		return rsModel;
	}

	public static UnitaOperativaIndex toUnitaOperativaRsModelIndex(it.govpay.bd.model.UnitaOperativa uo) throws IllegalArgumentException, ServiceException {
		UnitaOperativaIndex rsModel = new UnitaOperativaIndex();

		rsModel.setIdUnita(uo.getCodUo());
		rsModel.setRagioneSociale(uo.getAnagrafica().getRagioneSociale());

		return rsModel;
	}


	public static UnitaOperativa toUnitaOperativaRsModel(it.govpay.bd.model.UnitaOperativa uo) throws IllegalArgumentException, ServiceException {
		UnitaOperativa rsModel = new UnitaOperativa();

		rsModel.setCap(uo.getAnagrafica().getCap());
		rsModel.setCivico(uo.getAnagrafica().getCivico());
		rsModel.setIdUnita(uo.getCodUo());
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
		
		// Caricamento Pendenze Portale Backoffice
		TipoPendenzaPortaleBackofficeCaricamentoPendenze portaleBackoffice = new TipoPendenzaPortaleBackofficeCaricamentoPendenze();
		portaleBackoffice.setAbilitato(tipoVersamentoDominio.isCaricamentoPendenzePortaleBackofficeAbilitatoDefault());
		
		if(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault() != null && tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeFormTipoDefault() != null) {
			TipoPendenzaFormPortaleBackoffice form = new TipoPendenzaFormPortaleBackoffice();
			form.setTipo(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault())); 
			portaleBackoffice.setForm(form);
		}
		
		if(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault() != null && tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault());
			trasformazione.setDefinizione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault())); 
			portaleBackoffice.setTrasformazione(trasformazione);
		}
		if(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault() != null)
			portaleBackoffice.setValidazione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault()));
		
		portaleBackoffice.setInoltro(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault());
		
		rsModel.setPortaleBackoffice(portaleBackoffice);
		
		// Caricamento Pendenze Portale Pagamento
		TipoPendenzaPortalePagamentiCaricamentoPendenze portalePagamento = new TipoPendenzaPortalePagamentiCaricamentoPendenze();
		portalePagamento.setAbilitato(tipoVersamentoDominio.isCaricamentoPendenzePortalePagamentoAbilitatoDefault());
		
		if(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault() != null && tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormTipoDefault() != null) {
			TipoPendenzaFormPortalePagamenti form = new TipoPendenzaFormPortalePagamenti();
			form.setTipo(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault())); 
			if(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault() !=null)
				form.setImpaginazione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault()));
			portalePagamento.setForm(form);
		}
		
		if(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault() != null && tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault());
			trasformazione.setDefinizione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault())); 
			portalePagamento.setTrasformazione(trasformazione);
		}
		if(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault() != null)
			portalePagamento.setValidazione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault()));
		
		portalePagamento.setInoltro(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoCodApplicazioneDefault());
		
		rsModel.setPortalePagamento(portalePagamento);
		
		// Avvisatura via mail 
		
		TipoPendenzaAvvisaturaMail avvisaturaMail = new TipoPendenzaAvvisaturaMail();
		
		TipoPendenzaAvvisaturaMailPromemoriaAvviso avvisaturaMailPromemoriaAvviso = new TipoPendenzaAvvisaturaMailPromemoriaAvviso();
		avvisaturaMailPromemoriaAvviso.setAbilitato(tipoVersamentoDominio.isAvvisaturaMailPromemoriaAvvisoAbilitatoDefault());
		
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoOggettoDefault() != null)
			avvisaturaMailPromemoriaAvviso.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoOggettoDefault()));
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoMessaggioDefault() != null)
			avvisaturaMailPromemoriaAvviso.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoMessaggioDefault()));
		avvisaturaMailPromemoriaAvviso.setAllegaPdf(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoPdfDefault());
		avvisaturaMailPromemoriaAvviso.setTipo(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoTipoDefault());
		
		avvisaturaMail.setPromemoriaAvviso(avvisaturaMailPromemoriaAvviso);
		
		TipoPendenzaAvvisaturaMailPromemoriaRicevuta avvisaturaMailPromemoriaRicevuta = new TipoPendenzaAvvisaturaMailPromemoriaRicevuta();
		avvisaturaMailPromemoriaRicevuta.setAbilitato(tipoVersamentoDominio.isAvvisaturaMailPromemoriaRicevutaAbilitatoDefault());
		
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaOggettoDefault() != null)
			avvisaturaMailPromemoriaRicevuta.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaOggettoDefault()));
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaMessaggioDefault() != null)
			avvisaturaMailPromemoriaRicevuta.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaMessaggioDefault()));
		avvisaturaMailPromemoriaRicevuta.setAllegaPdf(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaPdfDefault());
		avvisaturaMailPromemoriaRicevuta.setSoloEseguiti(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault());
		avvisaturaMailPromemoriaRicevuta.setTipo(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipoDefault());
		
		avvisaturaMail.setPromemoriaRicevuta(avvisaturaMailPromemoriaRicevuta);

		TipoPendenzaAvvisaturaPromemoriaScadenza avvisaturaMailPromemoriaScadenza = new TipoPendenzaAvvisaturaPromemoriaScadenza();
		avvisaturaMailPromemoriaScadenza.setAbilitato(tipoVersamentoDominio.isAvvisaturaMailPromemoriaScadenzaAbilitatoDefault());
		
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaOggettoDefault() != null)
			avvisaturaMailPromemoriaScadenza.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaOggettoDefault()));
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaMessaggioDefault() != null)
			avvisaturaMailPromemoriaScadenza.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaMessaggioDefault()));
		avvisaturaMailPromemoriaScadenza.setPreavviso(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaPreavvisoDefault());
		avvisaturaMailPromemoriaScadenza.setTipo(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaTipoDefault());
		
		avvisaturaMail.setPromemoriaScadenza(avvisaturaMailPromemoriaScadenza);
		
		rsModel.setAvvisaturaMail(avvisaturaMail);
		
		// Visualizzazione Custom pendenza
		if(tipoVersamentoDominio.getVisualizzazioneDefinizioneDefault() != null)
			rsModel.setVisualizzazione(new RawObject(tipoVersamentoDominio.getVisualizzazioneDefinizioneDefault()));

		if(tipoVersamentoDominio.getTracciatoCsvTipoDefault() != null &&  
				tipoVersamentoDominio.getTracciatoCsvIntestazioneDefault() != null && 
				tipoVersamentoDominio.getTracciatoCsvRichiestaDefault() != null && 
				tipoVersamentoDominio.getTracciatoCsvRispostaDefault() != null) {
			TracciatoCsv tracciatoCsv = new TracciatoCsv();
			tracciatoCsv.setTipo(tipoVersamentoDominio.getTracciatoCsvTipoDefault());
			tracciatoCsv.setIntestazione(tipoVersamentoDominio.getTracciatoCsvIntestazioneDefault());
			tracciatoCsv.setRichiesta(new RawObject(tipoVersamentoDominio.getTracciatoCsvRichiestaDefault()));
			tracciatoCsv.setRisposta(new RawObject(tipoVersamentoDominio.getTracciatoCsvRispostaDefault()));
			rsModel.setTracciatoCsv(tracciatoCsv);
		}

		// Avvisatura via AppIO 
		
		TipoPendenzaAvvisaturaAppIO avvisaturaAppIO = new TipoPendenzaAvvisaturaAppIO();
		
		TipoPendenzaAvvisaturaPromemoriaAvvisoBase avvisaturaAppIOPromemoriaAvviso = new TipoPendenzaAvvisaturaPromemoriaAvvisoBase();
		avvisaturaAppIOPromemoriaAvviso.setAbilitato(tipoVersamentoDominio.isAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault());
		
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault() != null)
			avvisaturaAppIOPromemoriaAvviso.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault()));
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault() != null)
			avvisaturaAppIOPromemoriaAvviso.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault()));
		avvisaturaAppIOPromemoriaAvviso.setTipo(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoTipoDefault());
		
		avvisaturaAppIO.setPromemoriaAvviso(avvisaturaAppIOPromemoriaAvviso);
		
		TipoPendenzaAvvisaturaPromemoriaRicevutaBase avvisaturaAppIOPromemoriaRicevuta = new TipoPendenzaAvvisaturaPromemoriaRicevutaBase();
		avvisaturaAppIOPromemoriaRicevuta.setAbilitato(tipoVersamentoDominio.isAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault());
		
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaOggettoDefault() != null)
			avvisaturaAppIOPromemoriaRicevuta.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaOggettoDefault()));
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault() != null)
			avvisaturaAppIOPromemoriaRicevuta.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault()));
		avvisaturaAppIOPromemoriaRicevuta.setSoloEseguiti(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault());
		avvisaturaAppIOPromemoriaRicevuta.setTipo(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaTipoDefault());
		
		avvisaturaAppIO.setPromemoriaRicevuta(avvisaturaAppIOPromemoriaRicevuta);

		TipoPendenzaAvvisaturaPromemoriaScadenza avvisaturaAppIOPromemoriaScadenza = new TipoPendenzaAvvisaturaPromemoriaScadenza();
		avvisaturaAppIOPromemoriaScadenza.setAbilitato(tipoVersamentoDominio.isAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault());
		
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaOggettoDefault() != null)
			avvisaturaAppIOPromemoriaScadenza.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaOggettoDefault()));
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault() != null)
			avvisaturaAppIOPromemoriaScadenza.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault()));
		avvisaturaAppIOPromemoriaScadenza.setPreavviso(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault());
		avvisaturaAppIOPromemoriaScadenza.setTipo(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaTipoDefault());
		
		avvisaturaAppIO.setPromemoriaScadenza(avvisaturaAppIOPromemoriaScadenza);
		
		rsModel.setAvvisaturaAppIO(avvisaturaAppIO);
		
		TipoPendenzaDominioPost valori = new TipoPendenzaDominioPost();

		valori.codificaIUV(tipoVersamentoDominio.getCodificaIuvCustom())
		.pagaTerzi(tipoVersamentoDominio.getPagaTerziCustom())
		.abilitato(tipoVersamentoDominio.getAbilitatoCustom());
		
		// Caricamento Pendenze Portale Backoffice
		TipoPendenzaPortaleBackofficeCaricamentoPendenze valoriPortaleBackoffice = new TipoPendenzaPortaleBackofficeCaricamentoPendenze();
		valoriPortaleBackoffice.setAbilitato(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeAbilitatoCustom());
		
		if(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeFormDefinizioneCustom() != null && tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeFormTipoCustom() != null) {
			TipoPendenzaFormPortaleBackoffice form = new TipoPendenzaFormPortaleBackoffice();
			form.setTipo(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeFormTipoCustom());
			form.setDefinizione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeFormDefinizioneCustom())); 
			valoriPortaleBackoffice.setForm(form);
		}
		
		if(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom() != null && tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom());
			trasformazione.setDefinizione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom())); 
			valoriPortaleBackoffice.setTrasformazione(trasformazione);
		}
		if(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom() != null)
			valoriPortaleBackoffice.setValidazione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom()));
		
		valoriPortaleBackoffice.setInoltro(tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeCodApplicazioneCustom());
		
		valori.setPortaleBackoffice(valoriPortaleBackoffice);
		
		// Caricamento Pendenze Portale Pagamento
		TipoPendenzaPortalePagamentiCaricamentoPendenze valoriPortalePagamento = new TipoPendenzaPortalePagamentiCaricamentoPendenze();
		valoriPortalePagamento.setAbilitato(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoAbilitatoCustom());
		
		if(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormDefinizioneCustom() != null && tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormTipoCustom() != null) {
			TipoPendenzaFormPortalePagamenti form = new TipoPendenzaFormPortalePagamenti();
			form.setTipo(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormTipoCustom());
			form.setDefinizione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormDefinizioneCustom())); 
			if(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormImpaginazioneCustom() !=null)
			form.setImpaginazione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoFormImpaginazioneCustom()));
			valoriPortalePagamento.setForm(form);
		}
		
		if(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoCustom() != null && tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom() != null) {
			TipoPendenzaTrasformazione trasformazione  = new TipoPendenzaTrasformazione();
			trasformazione.setTipo(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoCustom());
			trasformazione.setDefinizione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom())); 
			valoriPortalePagamento.setTrasformazione(trasformazione);
		}
		if(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom() != null)
			valoriPortalePagamento.setValidazione(new RawObject(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom()));
		
		valoriPortalePagamento.setInoltro(tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoCodApplicazioneCustom());
		
		valori.setPortalePagamento(valoriPortalePagamento);
		
		// Avvisatura via mail 
		
		TipoPendenzaAvvisaturaMail valoriAvvisaturaMail = new TipoPendenzaAvvisaturaMail();
		
		TipoPendenzaAvvisaturaMailPromemoriaAvviso valoriAvvisaturaMailPromemoriaAvviso = new TipoPendenzaAvvisaturaMailPromemoriaAvviso();
		valoriAvvisaturaMailPromemoriaAvviso.setAbilitato(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoAbilitatoCustom());
		
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoOggettoCustom() != null)
			valoriAvvisaturaMailPromemoriaAvviso.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoOggettoCustom()));
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoMessaggioCustom() != null)
			valoriAvvisaturaMailPromemoriaAvviso.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoMessaggioCustom()));
		valoriAvvisaturaMailPromemoriaAvviso.setAllegaPdf(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoPdfCustom());
		valoriAvvisaturaMailPromemoriaAvviso.setTipo(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoTipoCustom());
		
		valoriAvvisaturaMail.setPromemoriaAvviso(valoriAvvisaturaMailPromemoriaAvviso);
		
		TipoPendenzaAvvisaturaMailPromemoriaRicevuta valoriaAvvisaturaMailPromemoriaRicevuta = new TipoPendenzaAvvisaturaMailPromemoriaRicevuta();
		valoriaAvvisaturaMailPromemoriaRicevuta.setAbilitato(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaAbilitatoCustom());
		
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaOggettoCustom() != null)
			valoriaAvvisaturaMailPromemoriaRicevuta.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaOggettoCustom()));
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaMessaggioCustom() != null)
			valoriaAvvisaturaMailPromemoriaRicevuta.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaMessaggioCustom()));
		valoriaAvvisaturaMailPromemoriaRicevuta.setAllegaPdf(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaPdfCustom());
		valoriaAvvisaturaMailPromemoriaRicevuta.setSoloEseguiti(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom());
		valoriaAvvisaturaMailPromemoriaRicevuta.setTipo(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipoCustom());
		
		valoriAvvisaturaMail.setPromemoriaRicevuta(valoriaAvvisaturaMailPromemoriaRicevuta);

		TipoPendenzaAvvisaturaPromemoriaScadenza valoriAvvisaturaMailPromemoriaScadenza = new TipoPendenzaAvvisaturaPromemoriaScadenza();
		valoriAvvisaturaMailPromemoriaScadenza.setAbilitato(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaAbilitatoCustom());
		
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaOggettoCustom() != null)
			valoriAvvisaturaMailPromemoriaScadenza.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaOggettoCustom()));
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaMessaggioCustom() != null)
			valoriAvvisaturaMailPromemoriaScadenza.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaMessaggioCustom()));
		valoriAvvisaturaMailPromemoriaScadenza.setPreavviso(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaPreavvisoCustom());
		valoriAvvisaturaMailPromemoriaScadenza.setTipo(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaTipoCustom());
		
		valoriAvvisaturaMail.setPromemoriaScadenza(valoriAvvisaturaMailPromemoriaScadenza);
		
		valori.setAvvisaturaMail(valoriAvvisaturaMail);
		
		// Visualizzazione Custom pendenza
		if(tipoVersamentoDominio.getVisualizzazioneDefinizioneCustom() != null)
			valori.setVisualizzazione(new RawObject(tipoVersamentoDominio.getVisualizzazioneDefinizioneCustom()));

		if(tipoVersamentoDominio.getTracciatoCsvTipoCustom() != null &&  
				tipoVersamentoDominio.getTracciatoCsvIntestazioneCustom() != null && 
				tipoVersamentoDominio.getTracciatoCsvRichiestaCustom() != null && 
				tipoVersamentoDominio.getTracciatoCsvRispostaCustom() != null) {
			TracciatoCsv tracciatoCsv = new TracciatoCsv();
			tracciatoCsv.setTipo(tipoVersamentoDominio.getTracciatoCsvTipoCustom());
			tracciatoCsv.setIntestazione(tipoVersamentoDominio.getTracciatoCsvIntestazioneCustom());
			tracciatoCsv.setRichiesta(new RawObject(tipoVersamentoDominio.getTracciatoCsvRichiestaCustom()));
			tracciatoCsv.setRisposta(new RawObject(tipoVersamentoDominio.getTracciatoCsvRispostaCustom()));
			valori.setTracciatoCsv(tracciatoCsv);
		}

		// Avvisatura via AppIO 
		
		TipoPendenzaDominioAvvisaturaAppIO valoriAvvisaturaAppIO = new TipoPendenzaDominioAvvisaturaAppIO();
		valoriAvvisaturaAppIO.setApiKey(tipoVersamentoDominio.getAppIOAPIKey());
		
		TipoPendenzaAvvisaturaPromemoriaAvvisoBase valoriAvvisaturaAppIOPromemoriaAvviso = new TipoPendenzaAvvisaturaPromemoriaAvvisoBase();
		valoriAvvisaturaAppIOPromemoriaAvviso.setAbilitato(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom());
		
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoOggettoCustom() != null)
			valoriAvvisaturaAppIOPromemoriaAvviso.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoOggettoCustom()));
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom() != null)
			valoriAvvisaturaAppIOPromemoriaAvviso.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom()));
		valoriAvvisaturaAppIOPromemoriaAvviso.setTipo(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoTipoCustom());
		
		valoriAvvisaturaAppIO.setPromemoriaAvviso(valoriAvvisaturaAppIOPromemoriaAvviso);
		
		TipoPendenzaAvvisaturaPromemoriaRicevutaBase valoriAvvisaturaAppIOPromemoriaRicevuta = new TipoPendenzaAvvisaturaPromemoriaRicevutaBase();
		valoriAvvisaturaAppIOPromemoriaRicevuta.setAbilitato(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom());
		
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaOggettoCustom() != null)
			valoriAvvisaturaAppIOPromemoriaRicevuta.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaOggettoCustom()));
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom() != null)
			valoriAvvisaturaAppIOPromemoriaRicevuta.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom()));
		valoriAvvisaturaAppIOPromemoriaRicevuta.setSoloEseguiti(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom());
		valoriAvvisaturaAppIOPromemoriaRicevuta.setTipo(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaRicevutaTipoCustom());
		
		valoriAvvisaturaAppIO.setPromemoriaRicevuta(valoriAvvisaturaAppIOPromemoriaRicevuta);

		TipoPendenzaAvvisaturaPromemoriaScadenza valoriAvvisaturaAppIOPromemoriaScadenza = new TipoPendenzaAvvisaturaPromemoriaScadenza();
		valoriAvvisaturaAppIOPromemoriaScadenza.setAbilitato(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom());
		
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaOggettoCustom() != null)
			valoriAvvisaturaAppIOPromemoriaScadenza.setOggetto(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaOggettoCustom()));
		if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom() != null)
			valoriAvvisaturaAppIOPromemoriaScadenza.setMessaggio(new RawObject(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom()));
		valoriAvvisaturaAppIOPromemoriaScadenza.setPreavviso(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom());
		valoriAvvisaturaAppIOPromemoriaScadenza.setTipo(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaTipoCustom());
		
		valoriAvvisaturaAppIO.setPromemoriaScadenza(valoriAvvisaturaAppIOPromemoriaScadenza);
		
		valori.setAvvisaturaAppIO(valoriAvvisaturaAppIO);
		
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

		// Configurazione Caricamento Pendenze Portale Backoffice
		tipoVersamentoDominio.setCaricamentoPendenzePortaleBackofficeAbilitatoCustom(false);
		if(tipoPendenzaRequest.getPortaleBackoffice() != null) {
			if(tipoPendenzaRequest.getPortaleBackoffice().Abilitato() != null)
				tipoVersamentoDominio.setCaricamentoPendenzePortaleBackofficeAbilitatoCustom(tipoPendenzaRequest.getPortaleBackoffice().Abilitato());
			else 
				tipoVersamentoDominio.setCaricamentoPendenzePortaleBackofficeAbilitatoCustom(null);
			
			if(tipoPendenzaRequest.getPortaleBackoffice().getForm() != null && tipoPendenzaRequest.getPortaleBackoffice().getForm().getDefinizione() != null && tipoPendenzaRequest.getPortaleBackoffice().getForm().getTipo() != null) {
				Object definizione = tipoPendenzaRequest.getPortaleBackoffice().getForm().getDefinizione();
				tipoVersamentoDominio.setCaricamentoPendenzePortaleBackofficeFormDefinizioneCustom(ConverterUtils.toJSON(definizione,null));
				tipoVersamentoDominio.setCaricamentoPendenzePortaleBackofficeFormTipoCustom(tipoPendenzaRequest.getPortaleBackoffice().getForm().getTipo());
			}
			
			if(tipoPendenzaRequest.getPortaleBackoffice().getTrasformazione() != null  && tipoPendenzaRequest.getPortaleBackoffice().getTrasformazione().getDefinizione() != null && tipoPendenzaRequest.getPortaleBackoffice().getTrasformazione().getTipo() != null) {
				if(tipoPendenzaRequest.getPortaleBackoffice().getTrasformazione().getTipo() != null) {
					// valore tipo template trasformazione non valido
					if(TipoTemplateTrasformazione.fromValue(tipoPendenzaRequest.getPortaleBackoffice().getTrasformazione().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + tipoPendenzaRequest.getPortaleBackoffice().getTrasformazione().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
				
				Object definizione = tipoPendenzaRequest.getPortaleBackoffice().getTrasformazione().getDefinizione();
				tipoVersamentoDominio.setCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom(ConverterUtils.toJSON(definizione,null));
				tipoVersamentoDominio.setCaricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom(tipoPendenzaRequest.getPortaleBackoffice().getTrasformazione().getTipo());
			}
			if(tipoPendenzaRequest.getPortaleBackoffice().getValidazione() != null)
				tipoVersamentoDominio.setCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getPortaleBackoffice().getValidazione(),null));
			
			if(tipoPendenzaRequest.getPortaleBackoffice().getInoltro() != null)
				tipoVersamentoDominio.setCaricamentoPendenzePortaleBackofficeCodApplicazioneCustom(tipoPendenzaRequest.getPortaleBackoffice().getInoltro());
		}
		
		// Configurazione Caricamento Pendenze Portale Backoffice
		tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoAbilitatoCustom(false);
		if(tipoPendenzaRequest.getPortalePagamento() != null) {
			if(tipoPendenzaRequest.getPortalePagamento().Abilitato() != null)
				tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoAbilitatoCustom(tipoPendenzaRequest.getPortalePagamento().Abilitato());
			else 
				tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoAbilitatoCustom(null);
				
			if(tipoPendenzaRequest.getPortalePagamento().getForm() != null && tipoPendenzaRequest.getPortalePagamento().getForm().getDefinizione() != null && tipoPendenzaRequest.getPortalePagamento().getForm().getTipo() != null) {
				Object definizione = tipoPendenzaRequest.getPortalePagamento().getForm().getDefinizione();
				tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoFormDefinizioneCustom(ConverterUtils.toJSON(definizione,null));
				tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoFormTipoCustom(tipoPendenzaRequest.getPortalePagamento().getForm().getTipo());
				Object impaginazione = tipoPendenzaRequest.getPortalePagamento().getForm().getImpaginazione();
				tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoFormImpaginazioneCustom(ConverterUtils.toJSON(impaginazione,null));
			}
			
			if(tipoPendenzaRequest.getPortalePagamento().getTrasformazione() != null  && tipoPendenzaRequest.getPortalePagamento().getTrasformazione().getDefinizione() != null && tipoPendenzaRequest.getPortalePagamento().getTrasformazione().getTipo() != null) {
				if(tipoPendenzaRequest.getPortalePagamento().getTrasformazione().getTipo() != null) {
					// valore tipo template trasformazione non valido
					if(TipoTemplateTrasformazione.fromValue(tipoPendenzaRequest.getPortalePagamento().getTrasformazione().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" + tipoPendenzaRequest.getPortalePagamento().getTrasformazione().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
				
				Object definizione = tipoPendenzaRequest.getPortalePagamento().getTrasformazione().getDefinizione();
				tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom(ConverterUtils.toJSON(definizione,null));
				tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoTrasformazioneTipoCustom(tipoPendenzaRequest.getPortalePagamento().getTrasformazione().getTipo());
			}
			if(tipoPendenzaRequest.getPortalePagamento().getValidazione() != null)
				tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getPortalePagamento().getValidazione(),null));
			
			if(tipoPendenzaRequest.getPortalePagamento().getInoltro() != null)
				tipoVersamentoDominio.setCaricamentoPendenzePortalePagamentoCodApplicazioneCustom(tipoPendenzaRequest.getPortalePagamento().getInoltro());
		}
		
		// Avvisatura Via Mail
		tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoAbilitatoCustom(false);
		tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaAbilitatoCustom(false);
		tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaAbilitatoCustom(false);
		if(tipoPendenzaRequest.getAvvisaturaMail() != null) {
			if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso() != null) {
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().Abilitato() != null) {
					
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoAbilitatoCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().Abilitato());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoAbilitatoCustom(false);
				}

				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().getMessaggio() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoMessaggioCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().getMessaggio(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoMessaggioCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().getOggetto() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoOggettoCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().getOggetto(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoOggettoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().AllegaPdf() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoPdfCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().AllegaPdf());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoPdfCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().getTipo() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoTipoCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().getTipo());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaAvvisoTipoCustom(null);
				}
				
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaAvviso().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
			
			if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta() != null) {
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().Abilitato() != null) {
					
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaAbilitatoCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().Abilitato());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaAbilitatoCustom(false);
				}

				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().getMessaggio() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaMessaggioCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().getMessaggio(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaMessaggioCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().getOggetto() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaOggettoCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().getOggetto(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaOggettoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().AllegaPdf() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaPdfCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().AllegaPdf());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaPdfCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().getTipo() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaTipoCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().getTipo());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaTipoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().SoloEseguiti() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().SoloEseguiti());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom(null);
				}
				
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaRicevuta().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
			
			if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza() != null) {
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().Abilitato() != null) {
					
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaAbilitatoCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().Abilitato());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaAbilitatoCustom(false);
				}

				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getMessaggio() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaMessaggioCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getMessaggio(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaMessaggioCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getOggetto() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaOggettoCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getOggetto(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaOggettoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getPreavviso() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaPreavvisoCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getPreavviso());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaPreavvisoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getTipo() != null) {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaTipoCustom(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getTipo());
				}else {
					tipoVersamentoDominio.setAvvisaturaMailPromemoriaScadenzaTipoCustom(null);
				}
				
				
				if(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								tipoPendenzaRequest.getAvvisaturaMail().getPromemoriaScadenza().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
		}
		
		if(tipoPendenzaRequest.getVisualizzazione() != null)
			tipoVersamentoDominio.setVisualizzazioneDefinizioneCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getVisualizzazione(),null));

		if(tipoPendenzaRequest.getTracciatoCsv() != null
				&& tipoPendenzaRequest.getTracciatoCsv().getTipo() != null
				&& tipoPendenzaRequest.getTracciatoCsv().getIntestazione() != null
				&& tipoPendenzaRequest.getTracciatoCsv().getRichiesta() != null
				&& tipoPendenzaRequest.getTracciatoCsv().getRisposta() != null) {
			tipoVersamentoDominio.setTracciatoCsvTipoCustom(tipoPendenzaRequest.getTracciatoCsv().getTipo());
			if(tipoPendenzaRequest.getTracciatoCsv().getTipo() != null) {
				// valore tipo contabilita non valido
				if(TipoTemplateTrasformazione.fromValue(tipoPendenzaRequest.getTracciatoCsv().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
							tipoPendenzaRequest.getTracciatoCsv().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
				}
			}

			tipoVersamentoDominio.setTracciatoCsvIntestazioneCustom(tipoPendenzaRequest.getTracciatoCsv().getIntestazione());
			tipoVersamentoDominio.setTracciatoCsvRichiestaCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getTracciatoCsv().getRichiesta(),null));
			tipoVersamentoDominio.setTracciatoCsvRispostaCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getTracciatoCsv().getRisposta(),null));
		}

		tipoPendenzaDTO.setTipoVersamentoDominio(tipoVersamentoDominio);
		tipoPendenzaDTO.setIdDominio(idDominio);
		tipoPendenzaDTO.setCodTipoVersamento(idTipoPendenza);
		
		// Avvisatura Via AppIO
		tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom(false);
		tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom(false);
		tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom(false);
		if(tipoPendenzaRequest.getAvvisaturaAppIO() != null) {
			if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso() != null) {
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().Abilitato() != null) {
					
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().Abilitato());
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom(false);
				}

				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().getMessaggio() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().getMessaggio(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().getOggetto() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaAvvisoOggettoCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().getOggetto(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaAvvisoOggettoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaAvvisoTipoCustom(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo());
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaAvvisoTipoCustom(null);
				}
				
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaAvviso().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
			
			if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta() != null) {
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().Abilitato() != null) {
					
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().Abilitato());
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom(false);
				}

				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().getMessaggio() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().getMessaggio(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().getOggetto() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaOggettoCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().getOggetto(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaOggettoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaTipoCustom(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo());
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaTipoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().SoloEseguiti() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().SoloEseguiti());
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom(null);
				}
				
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaRicevuta().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
			
			if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza() != null) {
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().Abilitato() != null) {
					
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().Abilitato());
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom(false);
				}

				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getMessaggio() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getMessaggio(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getOggetto() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaOggettoCustom(ConverterUtils.toJSON(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getOggetto(),null));
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaOggettoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getPreavviso() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getPreavviso());
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom(null);
				}
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo() != null) {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaTipoCustom(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo());
				}else {
					tipoVersamentoDominio.setAvvisaturaAppIoPromemoriaScadenzaTipoCustom(null);
				}
				
				
				if(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo() != null) {
					// valore tipo contabilita non valido
					if(TipoTemplateTrasformazione.fromValue(tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo()) == null) {
						throw new ValidationException("Codifica inesistente per tipo trasformazione. Valore fornito [" +
								tipoPendenzaRequest.getAvvisaturaAppIO().getPromemoriaScadenza().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoTemplateTrasformazione.values()));
					}
				}
			}
		}

		return tipoPendenzaDTO;		
	}
	public static it.govpay.core.dao.commons.Dominio getDominioCommons(DominioProfiloPost dominio) { 
		it.govpay.core.dao.commons.Dominio dominioCommons = new it.govpay.core.dao.commons.Dominio();

		dominioCommons.setCodDominio(dominio.getIdDominio());
		if(dominio.getUnitaOperative() != null) {
			List<Uo> uoList = new ArrayList<>();

			for (String uo : dominio.getUnitaOperative()) {
				if(uo.equals(ApplicazioniController.AUTORIZZA_UO_STAR)) {
					uoList.clear();
					break;
				}

				Uo uoCommons = new Uo();
				uoCommons.setCodUo(uo);
				uoList.add(uoCommons);
			}

			dominioCommons.setUo(uoList );
		}

		return dominioCommons;
	}

	public static it.govpay.core.dao.commons.Dominio getDominioCommons(String codDominio) { 
		it.govpay.core.dao.commons.Dominio dominioCommons = new it.govpay.core.dao.commons.Dominio();

		dominioCommons.setCodDominio(codDominio);
		return dominioCommons;
	}
}
