package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.commons.Anagrafica;
import it.govpay.core.dao.commons.Versamento;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.rs.v1.beans.DatiAddebito;
import it.govpay.core.rs.v1.beans.PagamentiPortaleResponseOk;
import it.govpay.core.rs.v1.beans.PagamentoPortale;
import it.govpay.core.rs.v1.beans.base.PagamentoPost;
import it.govpay.core.rs.v1.beans.base.PagamentoPost.AutenticazioneSoggettoEnum;
import it.govpay.core.rs.v1.beans.base.Pendenza;
import it.govpay.core.rs.v1.beans.base.PendenzaPost;
import it.govpay.core.rs.v1.beans.base.Soggetto;
import it.govpay.core.rs.v1.beans.base.StatoPagamento;
import it.govpay.core.rs.v1.beans.base.VocePendenza;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.IAutorizzato;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class PagamentiPortaleConverter {

	public static final String PENDENZE_KEY = "pendenze";
	public static final String VOCI_PENDENZE_KEY = "voci";
	public static final String ID_A2A_KEY = "idA2A";
	public static final String ID_PENDENZA_KEY = "idPendenza";
	public static final String ID_DOMINIO_KEY = "idDominio";
	public static final String IUV_KEY = "iuv";

	public static PagamentiPortaleResponseOk getPagamentiPortaleResponseOk(PagamentiPortaleDTOResponse dtoResponse) {
		PagamentiPortaleResponseOk  json = new PagamentiPortaleResponseOk();

		json.setId(dtoResponse.getId());
		json.setLocation("/pagamenti/"+ dtoResponse.getIdSessione());
		json.setRedirect(dtoResponse.getRedirectUrl());

		return json;
	}

	public static PagamentiPortaleDTO getPagamentiPortaleDTO(PagamentoPost pagamentiPortaleRequest, String jsonRichiesta, IAutorizzato user, String idSessione, String idSessionePortale) throws Exception {

		PagamentiPortaleDTO pagamentiPortaleDTO = new PagamentiPortaleDTO(user);

		pagamentiPortaleDTO.setIdSessione(idSessione);
		pagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
		pagamentiPortaleDTO.setJsonRichiesta(jsonRichiesta);
		if(pagamentiPortaleRequest.getAutenticazioneSoggetto() != null)
			pagamentiPortaleDTO.setAutenticazioneSoggetto(pagamentiPortaleRequest.getAutenticazioneSoggetto().toString());
		else 
			pagamentiPortaleDTO.setAutenticazioneSoggetto(AutenticazioneSoggettoEnum.N_A.toString());
		
		pagamentiPortaleDTO.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
		pagamentiPortaleDTO.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());

		if(pagamentiPortaleRequest.getDatiAddebito() != null) {
			pagamentiPortaleDTO.setBicAddebito(pagamentiPortaleRequest.getDatiAddebito().getBicAddebito());
			pagamentiPortaleDTO.setIbanAddebito(pagamentiPortaleRequest.getDatiAddebito().getIbanAddebito());
		}

		if(pagamentiPortaleRequest.getTokenWISP() != null) {
			pagamentiPortaleDTO.setIdDominio(pagamentiPortaleRequest.getTokenWISP().getIdDominio());
			pagamentiPortaleDTO.setKeyPA(pagamentiPortaleRequest.getTokenWISP().getKeyPA());
			pagamentiPortaleDTO.setKeyWISP(pagamentiPortaleRequest.getTokenWISP().getKeyWISP());
		}
		
		if(pagamentiPortaleRequest.getLingua() != null)
			pagamentiPortaleDTO.setLingua(pagamentiPortaleRequest.getLingua().toString());
		
		pagamentiPortaleDTO.setUrlRitorno(pagamentiPortaleRequest.getUrlRitorno());

		
		if(pagamentiPortaleRequest.getSoggettoVersante() != null);
			pagamentiPortaleDTO.setVersante(toAnagraficaCommons(pagamentiPortaleRequest.getSoggettoVersante()));

		JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRichiesta );  
		JSONArray jsonArrayPendenze = jsonObjectPagamentiPortaleRequest.getJSONArray(PagamentiPortaleConverter.PENDENZE_KEY);

		if(pagamentiPortaleRequest.getPendenze() != null && pagamentiPortaleRequest.getPendenze().size() > 0 ) {
			List<Object> listRefs = new ArrayList<Object>();

			JsonConfig jsonConfigPendenza = new JsonConfig();
			jsonConfigPendenza.setRootClass(Pendenza.class);
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("voci", VocePendenza.class);
			jsonConfigPendenza.setClassMap(classMap);
			
			for (int i = 0; i < jsonArrayPendenze.size(); i++) {

				JSONObject jsonObjectPendenza = jsonArrayPendenze.getJSONObject(i);

				Pendenza pendenza = (Pendenza) JSONObject.toBean( jsonObjectPendenza, jsonConfigPendenza );

				if((pendenza.getDominio() != null && pendenza.getNumeroAvviso() != null) && (pendenza.getIdA2A() == null && pendenza.getIdPendenza() == null)) {

					PagamentiPortaleDTO.RefVersamentoAvviso ref = pagamentiPortaleDTO. new RefVersamentoAvviso();
					ref.setIdDominio(pendenza.getDominio());
					ref.setNumeroAvviso(pendenza.getNumeroAvviso());
					listRefs.add(ref);

				} else	if((pendenza.getDominio() == null) && (pendenza.getIdA2A() != null && pendenza.getIdPendenza() != null)) {
					
					PagamentiPortaleDTO.RefVersamentoPendenza ref = pagamentiPortaleDTO. new RefVersamentoPendenza();
					ref.setIdA2A(pendenza.getIdA2A());
					ref.setIdPendenza(pendenza.getIdPendenza());
					listRefs.add(ref);

				}else if(pendenza.getIdA2A() != null && pendenza.getIdPendenza() != null && pendenza.getDominio() != null) {
					it.govpay.core.dao.commons.Versamento versamento = getVersamentoFromPendenza(pendenza);
					listRefs.add(versamento);
				} else {
					throw new Exception("tipo pendenza non riconosciuto");
				}
			}

			pagamentiPortaleDTO.setPendenzeOrPendenzeRef(listRefs);
		}

		return pagamentiPortaleDTO;
	}

	private static Anagrafica toAnagraficaCommons(Soggetto anagraficaRest) {
		Anagrafica anagraficaCommons = null;
		if(anagraficaRest != null) {
			anagraficaCommons = new Anagrafica();
			anagraficaCommons.setCap(anagraficaRest.getCap());
			anagraficaCommons.setCellulare(anagraficaRest.getCellulare());
			anagraficaCommons.setCivico(anagraficaRest.getCivico());
			anagraficaCommons.setCodUnivoco(anagraficaRest.getIdentificativo());
			anagraficaCommons.setEmail(anagraficaRest.getEmail());
			anagraficaCommons.setIndirizzo(anagraficaRest.getIndirizzo());
			anagraficaCommons.setLocalita(anagraficaRest.getLocalita());
			anagraficaCommons.setNazione(anagraficaRest.getNazione());
			anagraficaCommons.setProvincia(anagraficaRest.getProvincia());
			anagraficaCommons.setRagioneSociale(anagraficaRest.getAnagrafica());
		}

		return anagraficaCommons;
	}

	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(PendenzaPost pendenza, String ida2a, String idPendenza) {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(ida2a);

		versamento.setCodDominio(pendenza.getIdDominio());
		versamento.setCodUnitaOperativa(pendenza.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(idPendenza);
		versamento.setDataScadenza(pendenza.getDataScadenza());
		versamento.setDataValidita(pendenza.getDataValidita());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		versamento.setImportoTotale(pendenza.getImporto());
		versamento.setTassonomia(pendenza.getTassonomia());
		versamento.setTassonomiaAvviso(pendenza.getTassonomiaAvviso().toString());

		// voci pagamento
		fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getVoci());

		return versamento;
	}
	
	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(Pendenza pendenza) {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(pendenza.getIdA2A());

		versamento.setCodDominio(pendenza.getDominio());
		versamento.setCodUnitaOperativa(pendenza.getUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getIdPendenza());
		versamento.setDataScadenza(pendenza.getDataScadenza());
		versamento.setDataValidita(pendenza.getDataValidita());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		versamento.setImportoTotale(pendenza.getImporto());
		if(pendenza.getNumeroAvviso()!=null)
			versamento.setIuv(pendenza.getNumeroAvviso());
		versamento.setNome(pendenza.getNome());

		// voci pagamento
		fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getVoci());

		return versamento;
	}

	public static void fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, List<VocePendenza> voci) {

		if(voci != null && voci.size() > 0) {
			for (VocePendenza vocePendenza : voci) {
				Versamento.SingoloVersamento sv = new Versamento.SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				sv.setNote(vocePendenza.getDescrizione());
				sv.setImporto(vocePendenza.getImporto());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getHashDocumento() != null && vocePendenza.getTipoBollo() != null && vocePendenza.getProvinciaResidenza() != null) {
					Versamento.SingoloVersamento.BolloTelematico bollo = new Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getHashDocumento());
					bollo.setProvincia(vocePendenza.getProvinciaResidenza());
					bollo.setTipo(vocePendenza.getTipoBollo());
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodEntrata() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodEntrata());

				} else { // Definisce i dettagli di incasso della singola entrata.
					Versamento.SingoloVersamento.Tributo tributo = new Versamento.SingoloVersamento.Tributo();
					tributo.setCodContabilita(vocePendenza.getCodiceContabilita());
					tributo.setIbanAccredito(vocePendenza.getIbanAccredito());
					tributo.setTipoContabilita(Versamento.SingoloVersamento.Tributo.TipoContabilita.valueOf(vocePendenza.getTipoContabilita()));
					sv.setTributo(tributo);
				}

				versamento.getSingoloVersamento().add(sv);
			}
		}
	}

	public static PagamentoPortale toRsModel(it.govpay.bd.model.PagamentoPortale pagamentoPortale) throws ServiceException {
		PagamentoPortale rsModel = new PagamentoPortale();
		
		JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( pagamentoPortale.getJsonRequest() );  

		rsModel.setId(pagamentoPortale.getIdSessione());
		rsModel.setIdSessionePortale(pagamentoPortale.getIdSessionePortale());
		rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		rsModel.setNome(pagamentoPortale.getNome());
		rsModel.setStato(StatoPagamento.valueOf(pagamentoPortale.getStato().toString()));
		rsModel.setPspRedirectUrl(pagamentoPortale.getPspRedirectUrl());
		
		rsModel.setDataRichiestaPagamento(pagamentoPortale.getDataRichiesta());
		
		if(jsonObjectPagamentiPortaleRequest.containsKey("datiAddebito")) {
			rsModel.setDatiAddebito(DatiAddebito.parse(jsonObjectPagamentiPortaleRequest.getString("datiAddebito")));
		}

		try {
			if(jsonObjectPagamentiPortaleRequest.containsKey("dataEsecuzionePagamento")) {
				Object object = jsonObjectPagamentiPortaleRequest.get("dataEsecuzionePagamento");
				if(object instanceof JSONNull) {
					
				} else {
					String dataEsecuzionePagamentoString = jsonObjectPagamentiPortaleRequest.getString("dataEsecuzionePagamento");
					rsModel.setDataEsecuzionePagamento(SimpleDateFormatUtils.newSimpleDateFormatSoloData().parse(dataEsecuzionePagamentoString));
				}
			}
		} catch (ParseException e) {
			throw new ServiceException(e);
		}
		if(jsonObjectPagamentiPortaleRequest.containsKey("credenzialiPagatore")) {
			rsModel.setCredenzialiPagatore(jsonObjectPagamentiPortaleRequest.getString("credenzialiPagatore"));
		}
		if(jsonObjectPagamentiPortaleRequest.containsKey("soggettoVersante")) {
			rsModel.setSoggettoVersante(Soggetto.parse(jsonObjectPagamentiPortaleRequest.getString("soggettoVersante")));
		}
		if(jsonObjectPagamentiPortaleRequest.containsKey("autenticazioneSoggetto")) {
			rsModel.setAutenticazioneSoggetto(it.govpay.core.rs.v1.beans.base.Pagamento.AutenticazioneSoggettoEnum.fromValue(jsonObjectPagamentiPortaleRequest.getString("autenticazioneSoggetto")));
		}
		
		if(pagamentoPortale.getCodPsp() != null &&  pagamentoPortale.getCodCanale() != null && pagamentoPortale.getTipoVersamento() != null)
			rsModel.setCanale(UriBuilderUtils.getCanale(pagamentoPortale.getCodPsp(), pagamentoPortale.getCodCanale(), pagamentoPortale.getTipoVersamento()));
		
		rsModel.setPendenze(UriBuilderUtils.getPendenzeByPagamento(pagamentoPortale.getIdSessione()));
		rsModel.setRpp(UriBuilderUtils.getRptsByPagamento(pagamentoPortale.getIdSessione()));
		if(pagamentoPortale.getImporto() != null) 
			rsModel.setImporto(new BigDecimal(pagamentoPortale.getImporto())); 

		return rsModel;
	}
}
