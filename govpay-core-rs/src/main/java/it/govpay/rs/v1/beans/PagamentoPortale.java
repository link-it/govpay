
package it.govpay.rs.v1.beans;

import java.text.ParseException;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.rs.v1.beans.base.StatoPagamento;
import net.sf.json.JSONObject;

public class PagamentoPortale extends it.govpay.rs.v1.beans.base.Pagamento {

	public PagamentoPortale() {}
	
	@Override
	public String getJsonIdFilter() {
		return "pagamentiPortale";
	}
	
	public static PagamentoPortale parse(String json) {
		return (PagamentoPortale) parse(json, PagamentoPortale.class);
	}
	
	public PagamentoPortale(it.govpay.bd.model.PagamentoPortale pagamentoPortale) throws ServiceException {
		
		JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( pagamentoPortale.getJsonRequest() );  

		this.setId(pagamentoPortale.getIdSessione());
		this.setIdSessionePortale(pagamentoPortale.getIdSessionePortale());
		this.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		this.setNome(pagamentoPortale.getNome());
		this.setStato(StatoPagamento.valueOf(pagamentoPortale.getStato().toString()));
		this.setPspRedirectUrl(pagamentoPortale.getPspRedirectUrl());
		
		if(pagamentoPortale.getDataRichiesta() != null)
			this.setDataRichiestaPagamento(pagamentoPortale.getDataRichiesta());
		
		if(jsonObjectPagamentiPortaleRequest.containsKey("datiAddebito")) {
			this.setDatiAddebito(DatiAddebito.parse(jsonObjectPagamentiPortaleRequest.getString("datiAddebito")));
		}

		try {
			if(jsonObjectPagamentiPortaleRequest.containsKey("dataEsecuzionePagamento")) {
				this.setDataEsecuzionePagamento(SimpleDateFormatUtils.newSimpleDateFormatSoloData().parse(jsonObjectPagamentiPortaleRequest.getString("dataEsecuzionePagamento")));
			}
		} catch (ParseException e) {
			throw new ServiceException(e);
		}
		if(jsonObjectPagamentiPortaleRequest.containsKey("credenzialiPagatore")) {
			this.setCredenzialiPagatore(jsonObjectPagamentiPortaleRequest.getString("credenzialiPagatore"));
		}
		if(jsonObjectPagamentiPortaleRequest.containsKey("soggettoVersante")) {
			this.setSoggettoVersante(Soggetto.parse(jsonObjectPagamentiPortaleRequest.getString("soggettoVersante")));
		}
		if(jsonObjectPagamentiPortaleRequest.containsKey("autenticazioneSoggetto")) {
			this.setAutenticazioneSoggetto(AutenticazioneSoggettoEnum.fromValue(jsonObjectPagamentiPortaleRequest.getString("autenticazioneSoggetto")));
		}
		
		if(pagamentoPortale.getCodPsp() != null &&  pagamentoPortale.getCodCanale() != null)
			this.setCanale(UriBuilderUtils.getCanale(pagamentoPortale.getCodPsp(), pagamentoPortale.getCodCanale()));
		
		this.setPendenze(UriBuilderUtils.getPendenzeByPagamento(pagamentoPortale.getIdSessione()));
		this.setRpts(UriBuilderUtils.getRptsByPagamento(pagamentoPortale.getIdSessione()));

	}


}
