package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.govpay.core.dao.commons.Anagrafica;
import it.govpay.core.dao.commons.Versamento;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.PagamentiPortaleResponseOk;
import it.govpay.rs.v1.beans.base.PagamentoPost;
import it.govpay.rs.v1.beans.base.PagamentoPost.AutenticazioneSoggettoEnum;
import it.govpay.rs.v1.beans.base.Pendenza;
import it.govpay.rs.v1.beans.base.Soggetto;
import it.govpay.rs.v1.beans.base.VocePendenza;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

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

	public static PagamentiPortaleDTO getPagamentiPortaleDTO(PagamentoPost pagamentiPortaleRequest, String jsonRichiesta, String principal, String idSessione, String idSessionePortale) throws Exception {

		PagamentiPortaleDTO pagamentiPortaleDTO = new PagamentiPortaleDTO();

		pagamentiPortaleDTO.setIdSessione(idSessione);
		pagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
		pagamentiPortaleDTO.setPrincipal(principal);
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
			
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(BaseRsService.datePatterns.toArray(new String[1])) , true);

			for (int i = 0; i < jsonArrayPendenze.size(); i++) {

				JSONObject jsonObjectPendenza = jsonArrayPendenze.getJSONObject(i);

				Pendenza pendenza = (Pendenza) JSONObject.toBean( jsonObjectPendenza, jsonConfigPendenza );

				if((pendenza.getIdDominio() != null && pendenza.getNumeroAvviso() != null) && (pendenza.getIdA2A() == null && pendenza.getIdPendenza() == null)) {

					PagamentiPortaleDTO.RefVersamentoAvviso ref = new PagamentiPortaleDTO(). new RefVersamentoAvviso();
					ref.setIdDominio(pendenza.getIdDominio());
					ref.setNumeroAvviso(pendenza.getNumeroAvviso());
					listRefs.add(ref);

				} else	if((pendenza.getIdDominio() == null) && (pendenza.getIdA2A() != null && pendenza.getIdPendenza() != null)) {
					
					PagamentiPortaleDTO.RefVersamentoPendenza ref = new PagamentiPortaleDTO(). new RefVersamentoPendenza();
					ref.setIdA2A(pendenza.getIdA2A());
					ref.setIdPendenza(pendenza.getIdPendenza());
					listRefs.add(ref);

				}else if(pendenza.getIdA2A() != null && pendenza.getIdPendenza() != null && pendenza.getIdDominio() != null) {
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

	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(Pendenza pendenza) {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(pendenza.getIdA2A());

		versamento.setCodDominio(pendenza.getIdDominio());
		versamento.setCodUnitaOperativa(pendenza.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getIdPendenza());
		versamento.setDataScadenza(pendenza.getDataScadenza());
		versamento.setDataValidita(pendenza.getDataValidita());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		versamento.setImportoTotale(pendenza.getImporto());
		versamento.setIuv(pendenza.getNumeroAvviso());
		versamento.setNome(pendenza.getNome());

		// voci pagamento
		fillSingoliVersamentiFromVociPendenza(versamento, pendenza);

		return versamento;
	}

	public static void fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, Pendenza pendenza) {
		List<VocePendenza> voci = pendenza.getVoci();

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

}
