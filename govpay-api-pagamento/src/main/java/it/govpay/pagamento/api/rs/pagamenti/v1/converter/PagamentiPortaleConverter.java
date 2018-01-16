package it.govpay.pagamento.api.rs.pagamenti.v1.converter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.pagamento.api.rs.pagamenti.v1.model.IdPendenza;
import it.govpay.pagamento.api.rs.pagamenti.v1.model.IdVersamento;
import it.govpay.pagamento.api.rs.pagamenti.v1.model.PagamentiPortaleRequest;
import it.govpay.pagamento.api.rs.pagamenti.v1.model.PagamentiPortaleResponseOk;
import it.govpay.pagamento.api.rs.pagamenti.v1.model.Pendenza;
import it.govpay.servizi.commons.Anagrafica;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class PagamentiPortaleConverter {
	
	public static PagamentiPortaleRequest readFromJson(ByteArrayOutputStream baos) {
		PagamentiPortaleRequest pagamentiPortaleRequest = null;
		JsonConfig jsonConfig = new JsonConfig();
		Map<String,Class<?>> classMap = new HashMap<String, Class<?>>();
		
		JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( baos.toString() );  
		jsonConfig.setRootClass(PagamentiPortaleRequest.class);
		pagamentiPortaleRequest = (PagamentiPortaleRequest) JSONObject.toBean( jsonObjectPagamentiPortaleRequest, jsonConfig );
		
		return pagamentiPortaleRequest;
	}
	

	public static PagamentiPortaleResponseOk getPagamentiPortaleResponseOk(PagamentiPortaleDTOResponse dtoResponse) {
		PagamentiPortaleResponseOk  json = new PagamentiPortaleResponseOk();
		
		json.setId(1);
		json.setLocation("/pagamenti/1");
		json.setRedirect("http://localhost:8080/wisp");
		
		return json;
	}
	
	public static PagamentiPortaleDTO getPagamentiPortaleDTO (PagamentiPortaleRequest pagamentiPortaleRequest, String jsonRichiesta, String principal) throws Exception {
		
		PagamentiPortaleDTO pagamentiPortaleDTO = new PagamentiPortaleDTO();
		
		pagamentiPortaleDTO.setPrincipal(principal);
		pagamentiPortaleDTO.setJsonRichiesta(jsonRichiesta);
		
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
		
		if(pagamentiPortaleRequest.getSoggettoVersante() != null) {
			Anagrafica versante = new Anagrafica();
			versante.setCap(pagamentiPortaleRequest.getSoggettoVersante().getCap());
			versante.setCellulare(pagamentiPortaleRequest.getSoggettoVersante().getCellulare());
			versante.setCivico(pagamentiPortaleRequest.getSoggettoVersante().getCivico());
			versante.setCodUnivoco(pagamentiPortaleRequest.getSoggettoVersante().getIdentificativo());
			versante.setEmail(pagamentiPortaleRequest.getSoggettoVersante().getEmail());
			versante.setIndirizzo(pagamentiPortaleRequest.getSoggettoVersante().getIndirizzo());
			versante.setLocalita(pagamentiPortaleRequest.getSoggettoVersante().getLocalita());
			versante.setNazione(pagamentiPortaleRequest.getSoggettoVersante().getNazione());
			versante.setProvincia(pagamentiPortaleRequest.getSoggettoVersante().getProvincia());
			versante.setRagioneSociale(pagamentiPortaleRequest.getSoggettoVersante().getAnagrafica());
						
			pagamentiPortaleDTO.setVersante(versante);
		}
		
		if(pagamentiPortaleRequest.getPendenze() != null && pagamentiPortaleRequest.getPendenze().size() > 0 ) {
			List<Object> listRefs = new ArrayList<Object>();
			for (Object obj: pagamentiPortaleRequest.getPendenze()) {
				if(obj instanceof IdVersamento) {
					
				} else if(obj instanceof IdPendenza) {
					
				} else if(obj instanceof Pendenza) {
					
				} else throw new Exception("tipo pendenza non riconosciuto");
			}
			
			pagamentiPortaleDTO.setPendenzeOrPendenzeRef(listRefs);
		}
		
		return pagamentiPortaleDTO;
	}
}
