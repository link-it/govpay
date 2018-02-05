
package it.govpay.rs.v1.beans;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.rs.v1.beans.base.StatoPendenza;

public class Pendenza extends it.govpay.rs.v1.beans.base.Pendenza {

	public Pendenza() {}
	
	@Override
	public String getJsonIdFilter() {
		return "pendenze";
	}
	
	public static Pendenza parse(String json) {
		return (Pendenza) parse(json, Pendenza.class);
	}
	
	public Pendenza(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.model.Applicazione applicazione, it.govpay.bd.model.Dominio dominio) throws ServiceException {
		
		if(versamento.getCodAnnoTributario()!= null)
			this.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));
		
		if(versamento.getCausaleVersamento()!= null)
			try {
				this.setCausale(versamento.getCausaleVersamento().encode());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		
		this.setDataCaricamento(versamento.getDataCreazione());
		this.setDataScadenza(versamento.getDataScadenza());
		this.setDataValidita(versamento.getDataValidita());
		this.setDominio(UriBuilderUtils.getDominio(dominio.getCodDominio()));
		this.setIdA2A(applicazione.getCodApplicazione());
		this.setIdPendenza(versamento.getCodVersamentoEnte());
		this.setImporto(versamento.getImportoTotale());
		this.setNome(versamento.getNome());
		this.setNumeroAvviso(versamento.getTassonomiaAvviso()); //TODO verifica
//		this.setSoggettoPagatore(new Soggetto); TODO
		
		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
			break;
		case ANOMALO: statoPendenza = StatoPendenza.ANOMALIA;
			break;
		case ESEGUITO: statoPendenza = StatoPendenza.PAGATA;
			break;
		case ESEGUITO_SENZA_RPT:  statoPendenza = StatoPendenza.PAGATA;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_PAGATA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.PAGATA_PARZIALMENTE;
			break;
		default:
			break;
		
		}

		this.setStato(statoPendenza);
		this.setTassonomia(versamento.getTassonomia());
		this.setTassonomiaAvviso(versamento.getTassonomiaAvviso());
		if(unitaOperativa != null)
			this.setUnitaOperativa(UriBuilderUtils.getUoByDominio(dominio.getCodDominio(), unitaOperativa.getCodUo()));
		
		this.setPagamenti(UriBuilderUtils.getPagamentiByPendenza(versamento.getCodVersamentoEnte()));
		this.setRpts(UriBuilderUtils.getRptsByPendenza(versamento.getCodVersamentoEnte()));

//		this.setId(versamento.getIdSessione());
//		this.setIdSessionePortale(versamento.getIdSessionePortale());
//		this.setIdSessionePsp(versamento.getIdSessionePsp());
//		this.setNome(versamento.getNome());
//		this.setStato(StatoPagamento.valueOf(versamento.getStato().toString()));
//		this.setPspRedirectUrl(versamento.getPspRedirectUrl());
//		
//		if(versamento.getDataRichiesta() != null)
//			this.setDataRichiestaPagamento(versamento.getDataRichiesta());
//		
//		this.setDatiAddebito(DatiAddebito.parse(jsonObjectPagamentiPortaleRequest.getString("datiAddebito")));
//
//		try {
//			this.setDataEsecuzionePagamento(SimpleDateFormatUtils.newSimpleDateFormatSoloData().parse(jsonObjectPagamentiPortaleRequest.getString("dataEsecuzionePagamento")));
//		} catch (ParseException e) {
//			throw new ServiceException(e);
//		}
//
//		this.setCredenzialiPagatore(jsonObjectPagamentiPortaleRequest.getString("credenzialiPagatore"));
//		this.setSoggettoVersante(Soggetto.parse(jsonObjectPagamentiPortaleRequest.getString("soggettoVersante")));
//		this.setAutenticazioneSoggetto(AutenticazioneSoggettoEnum.fromValue(jsonObjectPagamentiPortaleRequest.getString("autenticazioneSoggetto")));
//		
//		if(versamento.getCodPsp() != null &&  versamento.getCodCanale() != null)
//			this.setCanale(UriBuilderUtils.getCanale(versamento.getCodPsp(), versamento.getCodCanale()));
//		
//		this.setPendenze(UriBuilderUtils.getPendenze(versamento.getIdSessione()));
//		this.setRpts(UriBuilderUtils.getRpts(versamento.getIdSessione()));

	}


}
