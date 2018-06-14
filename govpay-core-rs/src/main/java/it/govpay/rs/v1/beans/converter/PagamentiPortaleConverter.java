package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.beans.base.ContoAddebito;
import it.govpay.core.rs.v1.beans.base.Pagamento;
import it.govpay.core.rs.v1.beans.base.PagamentoIndex;
import it.govpay.core.rs.v1.beans.base.PendenzaPost;
import it.govpay.core.rs.v1.beans.base.StatoPagamento;
import it.govpay.core.rs.v1.beans.pagamenti.PagamentoPost;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.VersamentoUtils;
import net.sf.json.JsonConfig;

public class PagamentiPortaleConverter {

	public static final String PENDENZE_KEY = "pendenze";
	public static final String VOCI_PENDENZE_KEY = "voci";
	public static final String ID_A2A_KEY = "idA2A";
	public static final String ID_PENDENZA_KEY = "idPendenza";
	public static final String ID_DOMINIO_KEY = "idDominio";
	public static final String IUV_KEY = "iuv";



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

		rsModel.setSoggettoVersante(AnagraficaConverter.toSoggettoRsModel(AnagraficaConverter.toAnagrafica(pagamentiPortaleRequest.getSoggettoVersante())));
		rsModel.setAutenticazioneSoggetto(it.govpay.core.rs.v1.beans.base.Pagamento.AutenticazioneSoggettoEnum.fromValue(pagamentiPortaleRequest.getAutenticazioneSoggetto()));

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

		rsModel.setSoggettoVersante(AnagraficaConverter.toSoggettoRsModel(AnagraficaConverter.toAnagrafica(pagamentiPortaleRequest.getSoggettoVersante())));
		rsModel.setAutenticazioneSoggetto(it.govpay.core.rs.v1.beans.base.PagamentoIndex.AutenticazioneSoggettoEnum.fromValue(pagamentiPortaleRequest.getAutenticazioneSoggetto()));

		rsModel.setRpp(UriBuilderUtils.getRptsByPagamento(pagamentoPortale.getIdSessione()));

		if(pagamentoPortale.getImporto() != null) 
			rsModel.setImporto(new BigDecimal(pagamentoPortale.getImporto())); 

		return rsModel;

	}
}
