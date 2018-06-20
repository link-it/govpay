package it.govpay.rs.v1.beans.pagamenti.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.exceptions.RequestValidationException;
import it.govpay.core.rs.v1.beans.pagamenti.ContoAddebito;
import it.govpay.core.rs.v1.beans.pagamenti.PagamentiPortaleResponseOk;
import it.govpay.core.rs.v1.beans.pagamenti.Pagamento;
import it.govpay.core.rs.v1.beans.pagamenti.PagamentoIndex;
import it.govpay.core.rs.v1.beans.pagamenti.PagamentoPost;
import it.govpay.core.rs.v1.beans.pagamenti.PagamentoPost.AutenticazioneSoggettoEnum;
import it.govpay.core.rs.v1.beans.pagamenti.PendenzaPost;
import it.govpay.core.rs.v1.beans.pagamenti.StatoPagamento;
import it.govpay.core.rs.v1.beans.pagamenti.VocePendenza;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.pagamento.VersamentoUtils;
import it.govpay.model.IAutorizzato;
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

	public static PagamentiPortaleResponseOk getPagamentiPortaleResponseOk(PagamentiPortaleDTOResponse dtoResponse) {
		PagamentiPortaleResponseOk  json = new PagamentiPortaleResponseOk();

		json.setId(dtoResponse.getId());
		json.setLocation(UriBuilderUtils.getFromPagamenti(dtoResponse.getId()));
		json.setRedirect(dtoResponse.getRedirectUrl());
		json.setIdSession(dtoResponse.getIdSessione()); 

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

		if(pagamentiPortaleRequest.getContoAddebito() != null) {
			pagamentiPortaleDTO.setBicAddebito(pagamentiPortaleRequest.getContoAddebito().getBic());
			pagamentiPortaleDTO.setIbanAddebito(pagamentiPortaleRequest.getContoAddebito().getIban());
		}

		if(pagamentiPortaleRequest.getLingua() != null)
			pagamentiPortaleDTO.setLingua(pagamentiPortaleRequest.getLingua().toString());

		pagamentiPortaleDTO.setUrlRitorno(pagamentiPortaleRequest.getUrlRitorno());


		if(pagamentiPortaleRequest.getSoggettoVersante() != null);
		pagamentiPortaleDTO.setVersante(VersamentoUtils.toAnagraficaCommons(pagamentiPortaleRequest.getSoggettoVersante()));

		JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRichiesta );  
		JSONArray jsonArrayPendenze = jsonObjectPagamentiPortaleRequest.getJSONArray(PagamentiPortaleConverter.PENDENZE_KEY);

		if(pagamentiPortaleRequest.getPendenze() != null && pagamentiPortaleRequest.getPendenze().size() > 0 ) {
			List<Object> listRefs = new ArrayList<Object>();

			JsonConfig jsonConfigPendenza = new JsonConfig();
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("voci", VocePendenza.class);
			classMap.put("importo", String.class);
			jsonConfigPendenza.setClassMap(classMap);

			for (int i = 0; i < jsonArrayPendenze.size(); i++) {

				JSONObject jsonObjectPendenza = jsonArrayPendenze.getJSONObject(i);
				
				PendenzaPost pendenza = (PendenzaPost) PendenzaPost.parse(jsonObjectPendenza, PendenzaPost.class, jsonConfigPendenza );

				if((pendenza.getIdDominio() != null && pendenza.getNumeroAvviso() != null) && (pendenza.getIdA2A() == null && pendenza.getIdPendenza() == null)) {

					PagamentiPortaleDTO.RefVersamentoAvviso ref = pagamentiPortaleDTO. new RefVersamentoAvviso();
					ref.setIdDominio(pendenza.getIdDominio());
					ref.setNumeroAvviso(pendenza.getNumeroAvviso());
					listRefs.add(ref);

				} else	if((pendenza.getIdDominio() == null) && (pendenza.getIdA2A() != null && pendenza.getIdPendenza() != null)) {

					PagamentiPortaleDTO.RefVersamentoPendenza ref = pagamentiPortaleDTO. new RefVersamentoPendenza();
					ref.setIdA2A(pendenza.getIdA2A());
					ref.setIdPendenza(pendenza.getIdPendenza());
					listRefs.add(ref);

				}else if(pendenza.getIdA2A() != null && pendenza.getIdPendenza() != null && pendenza.getIdDominio() != null) {
					it.govpay.core.dao.commons.Versamento versamento = getVersamentoFromPendenza(pendenza);
					listRefs.add(versamento);
				} else {
					throw new RequestValidationException("La pendenza "+(i+1)+" e' di un tipo non riconosciuto.");
				}
			}

			pagamentiPortaleDTO.setPendenzeOrPendenzeRef(listRefs);
		}

		return pagamentiPortaleDTO;
	}

	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(PendenzaPost pendenza, String ida2a, String idPendenza) {
		return VersamentoUtils.getVersamentoFromPendenza(pendenza, ida2a, idPendenza);
	}

	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(it.govpay.core.rs.v1.beans.base.PendenzaPost pendenza, String ida2a, String idPendenza) {
		return VersamentoUtils.getVersamentoFromPendenza(pendenza, ida2a, idPendenza);
	}

	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(PendenzaPost pendenza) {
		return VersamentoUtils.getVersamentoFromPendenza(pendenza);
	}

	public static Pagamento toRsModel(it.govpay.bd.model.PagamentoPortale pagamentoPortale) throws ServiceException {
		Pagamento rsModel = new Pagamento();

		JsonConfig jsonConfig = new JsonConfig();
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("autenticazioneSoggetto", String.class);
		jsonConfig.setClassMap(classMap);
		PagamentoPost pagamentiPortaleRequest= (PagamentoPost) PagamentoPost.parse(pagamentoPortale.getJsonRequest(), PagamentoPost.class, jsonConfig);

		rsModel.setId(pagamentoPortale.getIdSessione());
		rsModel.setIdSessionePortale(pagamentoPortale.getIdSessionePortale());
		rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		rsModel.setNome(pagamentoPortale.getNome());
		rsModel.setStato(StatoPagamento.valueOf(pagamentoPortale.getStato().toString()));
		rsModel.setPspRedirectUrl(pagamentoPortale.getPspRedirectUrl());
		rsModel.setUrlRitorno(pagamentoPortale.getUrlRitorno());

		if(pagamentiPortaleRequest.getContoAddebito()!=null) {
			ContoAddebito contoAddebito = new ContoAddebito();
			contoAddebito.setBic(pagamentiPortaleRequest.getContoAddebito().getBic());
			contoAddebito.setIban(pagamentiPortaleRequest.getContoAddebito().getIban());
			rsModel.setContoAddebito(contoAddebito);
		}
		rsModel.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());

		rsModel.setDataRichiestaPagamento(pagamentoPortale.getDataRichiesta());
		rsModel.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
		
		rsModel.setSoggettoVersante(pagamentiPortaleRequest.getSoggettoVersante());
		rsModel.setAutenticazioneSoggetto(it.govpay.core.rs.v1.beans.pagamenti.Pagamento.AutenticazioneSoggettoEnum.fromValue(pagamentiPortaleRequest.getAutenticazioneSoggetto()));

		if(pagamentoPortale.getImporto() != null) 
			rsModel.setImporto(new BigDecimal(pagamentoPortale.getImporto())); 

		return rsModel;
	}
	public static PagamentoIndex toRsModelIndex(it.govpay.bd.model.PagamentoPortale pagamentoPortale) throws ServiceException {
		PagamentoIndex rsModel = new PagamentoIndex();
		
		JsonConfig jsonConfig = new JsonConfig();
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("autenticazioneSoggetto", String.class);
		jsonConfig.setClassMap(classMap);
		PagamentoPost pagamentiPortaleRequest= (PagamentoPost) PagamentoPost.parse(pagamentoPortale.getJsonRequest(), PagamentoPost.class, jsonConfig);

		rsModel.setId(pagamentoPortale.getIdSessione());
		rsModel.setIdSessionePortale(pagamentoPortale.getIdSessionePortale());
		rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		rsModel.setNome(pagamentoPortale.getNome());
		rsModel.setStato(StatoPagamento.valueOf(pagamentoPortale.getStato().toString()));
		rsModel.setPspRedirectUrl(pagamentoPortale.getPspRedirectUrl());
		rsModel.setUrlRitorno(pagamentoPortale.getUrlRitorno());

		if(pagamentiPortaleRequest.getContoAddebito()!=null) {
			ContoAddebito contoAddebito = new ContoAddebito();
			contoAddebito.setBic(pagamentiPortaleRequest.getContoAddebito().getBic());
			contoAddebito.setIban(pagamentiPortaleRequest.getContoAddebito().getIban());
			rsModel.setContoAddebito(contoAddebito);
		}

		rsModel.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());

		rsModel.setDataRichiestaPagamento(pagamentoPortale.getDataRichiesta());
		rsModel.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
		
		rsModel.setSoggettoVersante(pagamentiPortaleRequest.getSoggettoVersante());
		rsModel.setAutenticazioneSoggetto(it.govpay.core.rs.v1.beans.pagamenti.PagamentoIndex.AutenticazioneSoggettoEnum.fromValue(pagamentiPortaleRequest.getAutenticazioneSoggetto()));

		rsModel.setPendenze(UriBuilderUtils.getPendenzeByPagamento(pagamentoPortale.getIdSessione()));
		rsModel.setRpp(UriBuilderUtils.getRptsByPagamento(pagamentoPortale.getIdSessione()));

		if(pagamentoPortale.getImporto() != null) 
			rsModel.setImporto(new BigDecimal(pagamentoPortale.getImporto())); 

		return rsModel;

	}
}
