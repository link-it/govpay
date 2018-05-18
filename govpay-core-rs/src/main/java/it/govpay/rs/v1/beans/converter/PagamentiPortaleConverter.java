package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.PagamentoPortale.VersioneInterfacciaWISP;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.rs.v1.beans.DatiAddebito;
import it.govpay.core.rs.v1.beans.PagamentiPortaleResponseOk;
import it.govpay.core.rs.v1.beans.PagamentoPortale;
import it.govpay.core.rs.v1.beans.base.PagamentoPost;
import it.govpay.core.rs.v1.beans.base.PagamentoPost.AutenticazioneSoggettoEnum;
import it.govpay.core.rs.v1.beans.base.PendenzaPost;
import it.govpay.core.rs.v1.beans.base.Soggetto;
import it.govpay.core.rs.v1.beans.base.StatoPagamento;
import it.govpay.core.rs.v1.beans.base.VocePendenza;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.VersamentoUtils;
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
		json.setLocation(UriBuilderUtils.getFromPagamenti(dtoResponse.getId()));
		json.setRedirect(dtoResponse.getRedirectUrl());
		json.setIdSession(dtoResponse.getIdSessione()); 

		return json;
	}

	public static PagamentiPortaleDTO getPagamentiPortaleDTO(PagamentoPost pagamentiPortaleRequest, String jsonRichiesta, IAutorizzato user, String idSessione, String idSessionePortale, String versioneInterfacciaWISP) throws Exception {

		PagamentiPortaleDTO pagamentiPortaleDTO = new PagamentiPortaleDTO(user);

		try {
			VersioneInterfacciaWISP interfacciaWISP = VersioneInterfacciaWISP.toEnum(versioneInterfacciaWISP);
			pagamentiPortaleDTO.setVersioneInterfacciaWISP(interfacciaWISP != null ? interfacciaWISP : VersioneInterfacciaWISP.WISP_1_3);
		}catch(Exception e ) {
			pagamentiPortaleDTO.setVersioneInterfacciaWISP(VersioneInterfacciaWISP.WISP_1_3);
		}
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
		pagamentiPortaleDTO.setVersante(VersamentoUtils.toAnagraficaCommons(pagamentiPortaleRequest.getSoggettoVersante()));

		JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( jsonRichiesta );  
		JSONArray jsonArrayPendenze = jsonObjectPagamentiPortaleRequest.getJSONArray(PagamentiPortaleConverter.PENDENZE_KEY);

		if(pagamentiPortaleRequest.getPendenze() != null && pagamentiPortaleRequest.getPendenze().size() > 0 ) {
			List<Object> listRefs = new ArrayList<Object>();

			JsonConfig jsonConfigPendenza = new JsonConfig();
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("voci", VocePendenza.class);
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
					throw new Exception("tipo pendenza non riconosciuto");
				}
			}

			pagamentiPortaleDTO.setPendenzeOrPendenzeRef(listRefs);
		}

		return pagamentiPortaleDTO;
	}

	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(PendenzaPost pendenza, String ida2a, String idPendenza) {
		return VersamentoUtils.getVersamentoFromPendenza(pendenza, ida2a, idPendenza);
	}

	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(PendenzaPost pendenza) {
		return VersamentoUtils.getVersamentoFromPendenza(pendenza);
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

		if(jsonObjectPagamentiPortaleRequest.containsKey("contoAddebito")) {
			rsModel.setContoAddebito(DatiAddebito.parse(jsonObjectPagamentiPortaleRequest.getString("contoAddebito"), DatiAddebito.class));
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
