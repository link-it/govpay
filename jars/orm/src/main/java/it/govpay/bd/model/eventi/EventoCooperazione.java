package it.govpay.bd.model.eventi;

import java.util.Date;

import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Rpt;

public class EventoCooperazione extends EventoGenerico {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	public enum TipoEvento {
		nodoInviaRPT,
		nodoInviaCarrelloRPT, 
		nodoChiediStatoRPT, 
		paaInviaRT, 
		nodoChiediCopiaRT, 
		paaVerificaRPT, 
		paaAttivaRPT,
		nodoInviaRichiestaStorno,
		paaInviaEsitoStorno,
		nodoInviaAvvisoDigitale;
	}

	public static final String COMPONENTE = "FESP";
	public static final String NDP = "NodoDeiPagamentiSPC";

	private String codPsp;
	private TipoVersamento tipoVersamento;
	private String fruitore;
	private String erogatore;
	private String codStazione;
	private String codCanale;
	private String altriParametriRichiesta;
	private String altriParametriRisposta;
	
	public EventoCooperazione() {
		super();
		this.setCategoriaEvento(CategoriaEvento.INTERFACCIA_COOPERAZIONE);
		this.setDataRichiesta(new Date());
		this.setComponente(COMPONENTE);
		this.codPsp = Rpt.codPspWISP20;
		this.codCanale = Rpt.codCanaleWISP20;
		this.tipoVersamento = Rpt.tipoVersamentoWISP20;
	}

	public String getCodPsp() {
		return this.codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public TipoVersamento getTipoVersamento() {
		return this.tipoVersamento;
	}
	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	public void setTipoEvento(TipoEvento tipoEvento) {
		if(tipoEvento != null)
		this.setTipoEvento(tipoEvento.toString());
	}
	public String getFruitore() {
		return this.fruitore;
	}
	public void setFruitore(String fruitore) {
		this.fruitore = fruitore;
	}
	public String getErogatore() {
		return this.erogatore;
	}
	public void setErogatore(String erogatore) {
		this.erogatore = erogatore;
	}
	public String getCodStazione() {
		return this.codStazione;
	}
	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}
	public String getCodCanale() {
		return this.codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
	public String getAltriParametriRichiesta() {
		return this.altriParametriRichiesta;
	}
	public void setAltriParametriRichiesta(String altriParametriRichiesta) {
		this.altriParametriRichiesta = altriParametriRichiesta;
	}
	public String getAltriParametriRisposta() {
		return this.altriParametriRisposta;
	}
	public void setAltriParametriRisposta(String altriParametriRisposta) {
		this.altriParametriRisposta = altriParametriRisposta;
	}
}
