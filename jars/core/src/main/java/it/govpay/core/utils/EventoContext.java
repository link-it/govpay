package it.govpay.core.utils;

import java.util.Date;

import org.openspcoop2.utils.logger.beans.context.core.Role;

import it.govpay.bd.model.Evento;
import it.govpay.bd.model.eventi.Controparte;
import it.govpay.bd.model.eventi.DettaglioRichiesta;
import it.govpay.bd.model.eventi.DettaglioRisposta;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.EsitoEvento;
import it.govpay.model.Evento.RuoloEvento;

public class EventoContext {

	public enum Componente {API_ENTE, API_PAGAMENTO, API_RAGIONERIA, API_BACKOFFICE, API_PAGOPA, API_PENDENZE};
	public enum Esito {OK, KO, FAIL};
	public enum Categoria { INTERFACCIA, INTERNO, UTENTE };

	private Componente componente;
	private Role role;
	private Categoria categoriaEvento;
	private String tipoEvento;
	private String sottotipoEvento;
	private Esito esito;
	private Integer sottotipoEsito;
	private String descrizioneEsito;

	private String principal;
	private String utente;
	private Date dataRichiesta;
	private Date dataRisposta;

	private String method;
	private String url;

	private Integer status;

	private DettaglioRichiesta dettaglioRichiesta;
	private DettaglioRisposta dettaglioRisposta;
	private Controparte controparte;

	private String codDominio;
	private String iuv;
	private String ccp;
	private String idA2A;
	private String idPendenza;

	private Long idVersamento;
	private Long idPagamentoPortale;
	private Long idRpt;

	public Componente getComponente() {
		return componente;
	}

	public void setComponente(Componente componente) {
		this.componente = componente;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Categoria getCategoriaEvento() {
		return categoriaEvento;
	}

	public void setCategoriaEvento(Categoria categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String httpMethod, String nomeOperazione) {
		StringBuilder sb = new StringBuilder();
		if(httpMethod != null)
			sb.append(httpMethod.toUpperCase());
		if(sb.length() > 0 && nomeOperazione != null)
			sb.append("-");

		if(nomeOperazione != null)
			sb.append(nomeOperazione.toUpperCase());

		this.tipoEvento = sb.toString();
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getSottotipoEvento() {
		return sottotipoEvento;
	}

	public void setSottotipoEvento(String sottotipoEvento) {
		this.sottotipoEvento = sottotipoEvento;
	}

	public Esito getEsito() {
		return esito;
	}

	public void setEsito(Esito esito) {
		this.esito = esito;
	}

	public Integer getSottotipoEsito() {
		return sottotipoEsito;
	}

	public void setSottotipoEsito(Integer sottotipoEsito) {
		this.sottotipoEsito = sottotipoEsito;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public Date getDataRisposta() {
		return dataRisposta;
	}

	public void setDataRisposta(Date dataRisposta) {
		this.dataRisposta = dataRisposta;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public DettaglioRichiesta getDettaglioRichiesta() {
		return dettaglioRichiesta;
	}

	public void setDettaglioRichiesta(DettaglioRichiesta dettaglioRichiesta) {
		this.dettaglioRichiesta = dettaglioRichiesta;
	}

	public DettaglioRisposta getDettaglioRisposta() {
		return dettaglioRisposta;
	}

	public void setDettaglioRisposta(DettaglioRisposta dettaglioRisposta) {
		this.dettaglioRisposta = dettaglioRisposta;
	}
	public String getDescrizioneEsito() {
		return descrizioneEsito;
	}

	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCcp() {
		return ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public String getIdA2A() {
		return idA2A;
	}

	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}

	public String getIdPendenza() {
		return idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	public Long getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}

	public Long getIdPagamentoPortale() {
		return idPagamentoPortale;
	}

	public void setIdPagamentoPortale(Long idPagamentoPortale) {
		this.idPagamentoPortale = idPagamentoPortale;
	}

	public Long getIdRpt() {
		return idRpt;
	}

	public void setIdRpt(Long idRpt) {
		this.idRpt = idRpt;
	}

	public Controparte getControparte() {
		return controparte;
	}

	public void setControparte(Controparte controparte) {
		this.controparte = controparte;
	}

	public Evento toEventoDTO() {
		Evento dto = new Evento();

		if(this.getCategoriaEvento() != null) {
			switch (this.getCategoriaEvento()) {
			case INTERFACCIA:
				dto.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
				break;
			case INTERNO:
				dto.setCategoriaEvento(CategoriaEvento.INTERNO);
				break;
			case UTENTE:
				dto.setCategoriaEvento(CategoriaEvento.UTENTE);
				break;
			}
		}
		if(this.getComponente() != null)
			dto.setComponente(this.getComponente().toString());
		dto.setData(this.getDataRichiesta());
		dto.setControparte(this.getControparte());
		dto.setDettaglioEsito(this.getDescrizioneEsito());
		if(this.getEsito() != null) {
			switch(this.getEsito()) {
			case FAIL:
				dto.setEsitoEvento(EsitoEvento.FAIL);
				break;
			case KO:
				dto.setEsitoEvento(EsitoEvento.KO);
				break;
			case OK:
				dto.setEsitoEvento(EsitoEvento.OK);
				break;
			}
		}
		//dto.setId(this.getId());
		if(this.getDataRisposta() != null) {
			if(this.getDataRichiesta() != null) {
				dto.setIntervallo(this.getDataRisposta().getTime() - this.getDataRichiesta().getTime());
			} else {
				dto.setIntervallo(0l);
			}
		} else {
			dto.setIntervallo(0l);
		}
		dto.setDettaglioRichiesta(this.getDettaglioRichiesta());
		dto.setDettaglioRisposta(this.getDettaglioRisposta());
		if(this.getRole() != null) {
			switch(this.getRole()) {
			case CLIENT:
				dto.setRuoloEvento(RuoloEvento.CLIENT);
				break;
			case SERVER:
				dto.setRuoloEvento(RuoloEvento.SERVER);
				break;
			}
		}
		dto.setSottotipoEsito(this.getSottotipoEsito());
		dto.setSottotipoEvento(this.getSottotipoEvento());
		dto.setTipoEvento(this.getTipoEvento());
		dto.setIdVersamento(this.getIdVersamento());
		dto.setIdPagamentoPortale(this.getIdPagamentoPortale());
		dto.setIdRpt(this.getIdRpt());

		return dto;
	}


}
