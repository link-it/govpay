package it.govpay.bd.model.eventi;

import java.util.Date;

//import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Evento.CategoriaEvento;
//import it.govpay.model.Rpt;

public class EventoIntegrazione extends EventoGenerico {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	public enum TipoEvento {
		paNotificaTransazione;
	}

	public static final String COMPONENTE = "FESP";

	private Date dataRichiesta;
	private Date dataRisposta;
//	private String codPsp;
//	private TipoVersamento tipoVersamento;
	private String componente;
	private String fruitore;
	private String erogatore;
//	private String codStazione;
//	private String codCanale;
	private String altriParametriRichiesta;
	private String altriParametriRisposta;
	private String idTransazione;
	
	public EventoIntegrazione() {
		super();
		this.setCategoriaEvento(CategoriaEvento.INTERFACCIA_INTEGRAZIONE);
		this.dataRichiesta = new Date();
		this.componente = COMPONENTE;
//		this.codPsp = Rpt.codPspWISP20;
//		this.codCanale = Rpt.codCanaleWISP20;
//		this.tipoVersamento = Rpt.tipoVersamentoWISP20;
	}

	public Date getDataRichiesta() {
		return this.dataRichiesta;
	}
	public void setDataRichiesta(Date data_richiesta) {
		this.dataRichiesta = data_richiesta;
	}
	public Date getDataRisposta() {
		return this.dataRisposta;
	}
	public void setDataRisposta(Date data_risposta) {
		this.dataRisposta = data_risposta;
	}
//	public String getCodPsp() {
//		return this.codPsp;
//	}
//	public void setCodPsp(String codPsp) {
//		this.codPsp = codPsp;
//	}
//	public TipoVersamento getTipoVersamento() {
//		return this.tipoVersamento;
//	}
//	public void setTipoVersamento(TipoVersamento tipoVersamento) {
//		this.tipoVersamento = tipoVersamento;
//	}
	public String getComponente() {
		return this.componente;
	}
	public void setComponente(String componente) {
		this.componente = componente;
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
//	public String getCodStazione() {
//		return this.codStazione;
//	}
//	public void setCodStazione(String codStazione) {
//		this.codStazione = codStazione;
//	}
//	public String getCodCanale() {
//		return this.codCanale;
//	}
//	public void setCodCanale(String codCanale) {
//		this.codCanale = codCanale;
//	}
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
	public String getIdTransazione() {
		return idTransazione;
	}
	public void setIdTransazione(String idTransazione) {
		this.idTransazione = idTransazione;
	}
}
