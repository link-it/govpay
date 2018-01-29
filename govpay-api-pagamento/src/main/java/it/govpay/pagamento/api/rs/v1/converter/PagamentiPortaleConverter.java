package it.govpay.pagamento.api.rs.v1.converter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.pagamento.api.rs.v1.model.IdPendenza;
import it.govpay.pagamento.api.rs.v1.model.IdVersamento;
import it.govpay.pagamento.api.rs.v1.model.PagamentiPortaleRequest;
import it.govpay.pagamento.api.rs.v1.model.PagamentiPortaleResponseOk;
import it.govpay.pagamento.api.rs.v1.model.PagamentoPortale;
import it.govpay.pagamento.api.rs.v1.model.Pendenza;
import it.govpay.pagamento.api.rs.v1.model.VocePendenza;
import it.govpay.rs.v1.beans.ListaPagamentiPortale;
import it.govpay.servizi.commons.Anagrafica;
import it.govpay.servizi.commons.TipoContabilita;
import it.govpay.servizi.commons.Versamento.SingoloVersamento;
import it.govpay.servizi.commons.Versamento.SingoloVersamento.BolloTelematico;
import it.govpay.servizi.commons.Versamento.SingoloVersamento.Tributo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class PagamentiPortaleConverter {

	public static final String PENDENZE_KEY = "pendenze";
	public static final String VOCI_PENDENZE_KEY = "voci";
	public static final String ID_A2A_KEY = "idA2A";
	public static final String ID_PENDENZA_KEY = "idPendenza";
	public static final String ID_DOMINIO_KEY = "idDominio";
	public static final String IUV_KEY = "iuv";

	public static PagamentiPortaleRequest readFromJson(ByteArrayOutputStream baos) {
		return readFromJson(baos.toString());
	}
	
	public static PagamentiPortaleRequest readFromJson(String jsonContent) {
		PagamentiPortaleRequest pagamentiPortaleRequest = null;
		JsonConfig jsonConfig = new JsonConfig();

		JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonContent );  

		JSONArray jsonArrayPendenze = jsonObjectPagamentiPortaleRequest.getJSONArray(PagamentiPortaleConverter.PENDENZE_KEY);

		List<Object> listPendenzeGeneriche = new ArrayList<Object>();

		JsonConfig jsonConfigPendenza = new JsonConfig();
		Map<String,Class<?>> classMapPendenza = new HashMap<String, Class<?>>();
		classMapPendenza.put(VOCI_PENDENZE_KEY, VocePendenza.class);
		jsonConfigPendenza.setRootClass(Pendenza.class);
		jsonConfigPendenza.setClassMap(classMapPendenza); 

		JsonConfig jsonConfigIdPendenza = new JsonConfig();
		jsonConfigIdPendenza.setRootClass(IdPendenza.class);

		JsonConfig jsonConfigIdVersamento = new JsonConfig();
		jsonConfigIdVersamento.setRootClass(IdVersamento.class);

		if(jsonArrayPendenze != null && jsonArrayPendenze.size() > 0) {
			for (int i = 0; i < jsonArrayPendenze.size(); i++) {
				JSONObject jsonObjectGenericPendenza = jsonArrayPendenze.getJSONObject(i);
				// se il numero di properties dell'oggetto e' maggiora di 2 sono in una pendenza altrimenti e' un id
				if(jsonObjectGenericPendenza.size() > 2) {
					Pendenza pendenza = (Pendenza) JSONObject.toBean( jsonObjectGenericPendenza, jsonConfigPendenza );
					listPendenzeGeneriche.add(pendenza);
				} else {
					IdPendenza idPendenza = (IdPendenza) JSONObject.toBean( jsonObjectGenericPendenza, jsonConfigIdPendenza );
					IdVersamento idVersamento = (IdVersamento) JSONObject.toBean( jsonObjectGenericPendenza, jsonConfigIdVersamento );

					if(idPendenza.getIdA2A() != null && idPendenza.getIdPendenza() != null) {
						listPendenzeGeneriche.add(idPendenza);
					} else if(idVersamento.getIdDominio() != null && idVersamento.getIuv() != null) {
						listPendenzeGeneriche.add(idVersamento);
					} else {
						// tipo non riconosciuto
					}
				}
			}
		}

		// rimuovo l'oggetto jsonArrayPendenze prima di creare l'oggetto
		jsonObjectPagamentiPortaleRequest.remove(PENDENZE_KEY);

		jsonConfig.setRootClass(PagamentiPortaleRequest.class);
		pagamentiPortaleRequest = (PagamentiPortaleRequest) JSONObject.toBean( jsonObjectPagamentiPortaleRequest, jsonConfig );

		// setto le pendenze lette
		pagamentiPortaleRequest.setPendenze(listPendenzeGeneriche);

		return pagamentiPortaleRequest;
	}


	public static PagamentiPortaleResponseOk getPagamentiPortaleResponseOk(PagamentiPortaleDTOResponse dtoResponse) {
		PagamentiPortaleResponseOk  json = new PagamentiPortaleResponseOk();

		json.setId(dtoResponse.getId());
		json.setLocation("/pagamenti/"+ dtoResponse.getId());
		json.setRedirect(dtoResponse.getRedirectUrl());

		return json;
	}

	public static PagamentiPortaleDTO getPagamentiPortaleDTO (PagamentiPortaleRequest pagamentiPortaleRequest, String jsonRichiesta, String principal, String idSessione, String idSessionePortale) throws Exception {

		PagamentiPortaleDTO pagamentiPortaleDTO = new PagamentiPortaleDTO();

		pagamentiPortaleDTO.setIdSessione(idSessione);
		pagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
		pagamentiPortaleDTO.setPrincipal(principal);
		pagamentiPortaleDTO.setJsonRichiesta(jsonRichiesta);
		pagamentiPortaleDTO.setAutenticazioneSoggetto(pagamentiPortaleRequest.getAutenticazioneSoggetto());

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

		pagamentiPortaleDTO.setLingua(pagamentiPortaleRequest.getLingua());
		pagamentiPortaleDTO.setUrlRitorno(pagamentiPortaleRequest.getUrlRitorno());

		it.govpay.pagamento.api.rs.v1.model.Anagrafica soggettoVersante = pagamentiPortaleRequest.getSoggettoVersante();
		Anagrafica versante = toAnagraficaCommons(soggettoVersante);
		pagamentiPortaleDTO.setVersante(versante);

		if(pagamentiPortaleRequest.getPendenze() != null && pagamentiPortaleRequest.getPendenze().size() > 0 ) {
			List<Object> listRefs = new ArrayList<Object>();
			for (Object obj: pagamentiPortaleRequest.getPendenze()) {
				if(obj instanceof IdVersamento) {
					IdVersamento idVersamento = (IdVersamento) obj;

					it.govpay.servizi.commons.VersamentoKey versamento = new it.govpay.servizi.commons.VersamentoKey();

					it.govpay.servizi.commons.ObjectFactory objectFactory = new it.govpay.servizi.commons.ObjectFactory();
					versamento.getContent().add(objectFactory.createVersamentoKeyCodDominio(idVersamento.getIdDominio()));
					versamento.getContent().add(objectFactory.createVersamentoKeyIuv(idVersamento.getIuv()));

					listRefs.add(versamento);
				} else if(obj instanceof IdPendenza) {
					IdPendenza idPendenza = (IdPendenza) obj;

					it.govpay.servizi.commons.VersamentoKey versamento = new it.govpay.servizi.commons.VersamentoKey();

					it.govpay.servizi.commons.ObjectFactory objectFactory = new it.govpay.servizi.commons.ObjectFactory();
					versamento.getContent().add(objectFactory.createVersamentoKeyCodApplicazione(idPendenza.getIdA2A()));
					versamento.getContent().add(objectFactory.createVersamentoKeyCodVersamentoEnte(idPendenza.getIdPendenza()));

					listRefs.add(versamento);
				} else if(obj instanceof Pendenza) {
					it.govpay.servizi.commons.Versamento versamento = getVersamentoFromPendenza((Pendenza) obj);
					listRefs.add(versamento);
				} else throw new Exception("tipo pendenza non riconosciuto");
			}

			pagamentiPortaleDTO.setPendenzeOrPendenzeRef(listRefs);
		}

		return pagamentiPortaleDTO;
	}


	public static Anagrafica toAnagraficaCommons(it.govpay.pagamento.api.rs.v1.model.Anagrafica anagraficaRest) {
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

	public static it.govpay.servizi.commons.Versamento getVersamentoFromPendenza(Pendenza pendenza) {
		it.govpay.servizi.commons.Versamento versamento = new it.govpay.servizi.commons.Versamento();

		// versamento.setAggiornabile(??);
		// versamento.setBundlekey(??);
		//		pendenza.getDataCaricamento();
		//		pendenza.getDataValidita();
		//		pendenza.getIdCartellaPagamento();
		//		pendenza.getStato();
		//		pendenza.getTassonomia();
		// versamento.setCodDebito(value);

		versamento.setAnnoTributario(pendenza.getAnnoRiferimento());
		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(pendenza.getIdA2A());

		versamento.setCodDominio(pendenza.getIdDominio());
		versamento.setCodUnitaOperativa(pendenza.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getIdPendenza());
		versamento.setDataScadenza(pendenza.getDataScadenza());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		versamento.setImportoTotale(new BigDecimal(pendenza.getImporto()));
		versamento.setIuv(pendenza.getIuv());

		// voci pagamento
		fillSingoliVersamentiFromVociPendenza(versamento, pendenza);

		return versamento;
	}

	public static void fillSingoliVersamentiFromVociPendenza(it.govpay.servizi.commons.Versamento versamento, Pendenza pendenza) {
		List<VocePendenza> voci = pendenza.getVoci();

		if(voci != null && voci.size() > 0) {
			for (VocePendenza vocePendenza : voci) {
				SingoloVersamento sv = new SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				sv.setNote(vocePendenza.getDescrizione());
				sv.setImporto(new BigDecimal(vocePendenza.getImporto()));

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getHashDocumento() != null && vocePendenza.getTipoBollo() != null && vocePendenza.getProvinciaResidenza() != null) {
					BolloTelematico bollo = new BolloTelematico();
					bollo.setHash(vocePendenza.getHashDocumento());
					bollo.setProvincia(vocePendenza.getProvinciaResidenza());
					bollo.setTipo(vocePendenza.getTipoBollo());
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodEntrata() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					// ??	vocePendenza.getCodEntrata();

				} else { // Definisce i dettagli di incasso della singola entrata.
					Tributo tributo = new Tributo();
					tributo.setCodContabilita(vocePendenza.getCodiceContabilita());
					tributo.setIbanAccredito(vocePendenza.getIbanAccredito());
					tributo.setTipoContabilita(TipoContabilita.valueOf(vocePendenza.getTipoContabilita()));
					sv.setTributo(tributo);
				}

				versamento.getSingoloVersamento().add(sv);
			}
		}
	}

	public static PagamentoPortale toJsonPagamentoPortale(it.govpay.bd.model.PagamentoPortale model) {
		PagamentoPortale json = new PagamentoPortale();

//		json.setId(model.getIdSessione());
//		json.setStato(model.getStato().toString());
//		
//		if(psp != null) {
//			json.setIdPsp(psp.getCodPsp());
//			json.setNomePsp(psp.getRagioneSociale());
//		}
//		if(canale != null) {
//			json.setIdCanale(canale.getCodCanale());
//			json.setModelloPagamento(canale.getModelloPagamento().toString());
//			json.setNomeCanale(canale.getDescrizione());
//			json.setTipoVersamento(canale.getTipoVersamento().toString());
//		}

		// [TODO] psp canali rpts

		return json;
	}


	/**
	 * @param lstPagamenti
	 * @return
	 */
	public static ListaPagamentiPortale toJsonPagamentoPortaleList(List<it.govpay.bd.model.PagamentoPortale> lstPagamenti, long totalResults) {
		new ListaPagamentiPortale(domini, requestUri, count, pagina, risultatiPerPagina)
	}
}
