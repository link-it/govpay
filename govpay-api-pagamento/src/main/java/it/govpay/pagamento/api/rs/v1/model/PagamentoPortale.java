package it.govpay.pagamento.api.rs.v1.model;

import java.util.List;

public class PagamentoPortale {
	
	private String id;
	private String idSessionePortale;
	private String idSessionePsp;
	private String nome;
	private String stato;
	private String pspRedirectUrl;
	private String dataRichiestaPagamento;
	private DatiAddebito datiAddebito;
	private String dataEsecuzionePagamento;
	private String credenzialiPagatore;
	private Anagrafica soggettoVersante;
	
	private Canale canale;
	private List<VocePendenza> pendenze;

	private List<Rpt> rpts;

}
