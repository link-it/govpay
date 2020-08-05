package it.govpay.core.dao.eventi.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.bd.pagamento.filters.EventiFilter.VISTA;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.EsitoEvento;
import it.govpay.model.Evento.RuoloEvento;
import it.govpay.orm.Evento;

public class ListaEventiDTO extends BasicFindRequestDTO{

	public ListaEventiDTO(Authentication user) {
		super(user);
		this.addDefaultSort(Evento.model().DATA, SortOrder.DESC);
	}

	private String idDominio;
	private String iuv;
	private String idA2A;
	private String idPendenza;
	private String idPagamento;
	private EsitoEvento esito;
	private String componente;
	private RuoloEvento ruolo;
	private CategoriaEvento categoriaEvento;
	private String tipoEvento;
	private String sottotipoEvento;
	private Date dataA;
	private Date dataDa;
	private Boolean messaggi;
	private VISTA vista;
	private String ccp;
	
	public Date getDataA() {
		return dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Date getDataDa() {
		return dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getIdA2A() {
		return this.idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
	public String getIdPendenza() {
		return this.idPendenza;
	}
	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}
	public String getIdPagamento() {
		return this.idPagamento;
	}
	public void setIdPagamento(String idPagamento) {
		this.idPagamento = idPagamento;
	}
	public EsitoEvento getEsito() {
		return esito;
	}
	public void setEsito(EsitoEvento esito) {
		this.esito = esito;
	}
	public String getComponente() {
		return componente;
	}
	public void setComponente(String componente) {
		this.componente = componente;
	}
	public RuoloEvento getRuolo() {
		return ruolo;
	}
	public void setRuolo(RuoloEvento ruolo) {
		this.ruolo = ruolo;
	}
	public CategoriaEvento getCategoriaEvento() {
		return categoriaEvento;
	}
	public void setCategoriaEvento(CategoriaEvento categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}
	public String getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public Boolean getMessaggi() {
		return messaggi;
	}
	public void setMessaggi(Boolean messaggi) {
		this.messaggi = messaggi;
	}
	public String getSottotipoEvento() {
		return sottotipoEvento;
	}
	public void setSottotipoEvento(String sottotipoEvento) {
		this.sottotipoEvento = sottotipoEvento;
	}
	public VISTA getVista() {
		return vista;
	}
	public void setVista(VISTA vista) {
		this.vista = vista;
	}
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
}
