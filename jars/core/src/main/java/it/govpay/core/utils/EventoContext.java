package it.govpay.core.utils;

import java.util.Date;

import org.openspcoop2.utils.logger.beans.context.core.Role;

import it.govpay.bd.model.Evento;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.bd.model.eventi.DettaglioRichiesta;
import it.govpay.bd.model.eventi.DettaglioRisposta;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.EsitoEvento;
import it.govpay.model.Evento.RuoloEvento;

public class EventoContext {
	
	public static final String APIPAGOPA_TIPOEVENTO_PAAVERIFICARPT = "paaVerificaRPT";
	public static final String APIPAGOPA_TIPOEVENTO_PAAATTIVARPT = "paaAttivaRPT";
	public static final String APIPAGOPA_TIPOEVENTO_PAAINVIAESITOSTORNO = "paaInviaEsitoStorno";
	public static final String APIPAGOPA_TIPOEVENTO_PAAINVIARICHIESTAREVOCA = "paaInviaRichiestaRevoca";
	public static final String APIPAGOPA_TIPOEVENTO_PAAINVIART = "paaInviaRT";

	public static final String SOTTOTIPO_EVENTO_NOTA = "nota";
	
	public enum Componente {API_ENTE, API_PAGAMENTO, API_RAGIONERIA, API_BACKOFFICE, API_PAGOPA, API_PENDENZE, API_WC, API_USER};
	public enum Esito {OK, KO, FAIL};
	public enum Categoria { INTERFACCIA, INTERNO, UTENTE };

	private boolean registraEvento = false;
	private Componente componente;
	private Role role;
	private Categoria categoriaEvento;
	private String tipoEvento;
	private String sottotipoEvento;
	private Esito esito;
	private String sottotipoEsito;
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
	private DatiPagoPA datiPagoPA;

	private String codDominio;
	private String iuv;
	private String ccp;
	private String idA2A;
	private String idPendenza;
	private String idPagamento;

	
	private String idTransazione;
	private Long id;
	
	private Long idFr;
	private Long idIncasso;
	private Long idTracciato;
	
	public EventoContext() {
		this.dataRichiesta = new Date();
	}

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

//	public void setTipoEvento(String httpMethod, String nomeOperazione) {
//		StringBuilder sb = new StringBuilder();
//		if(httpMethod != null)
//			sb.append(httpMethod.toUpperCase());
//		if(sb.length() > 0 && nomeOperazione != null)
//			sb.append("-");
//
//		if(nomeOperazione != null)
//			sb.append(nomeOperazione.toUpperCase());
//
//		this.tipoEvento = sb.toString();
//	}

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

	public DatiPagoPA getDatiPagoPA() {
		return datiPagoPA;
	}

	public void setDatiPagoPA(DatiPagoPA datiPagoPA) {
		this.datiPagoPA = datiPagoPA;
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
		dto.setDatiPagoPA(this.getDatiPagoPA());
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
		dto.setCodApplicazione(this.idA2A);
		dto.setCodVersamentoEnte(this.idPendenza);
		dto.setCodDominio(this.codDominio);
		dto.setIuv(this.iuv);
		dto.setCcp(this.ccp);
		dto.setIdSessione(this.idPagamento);
		dto.setIdFr(this.idFr);
		dto.setIdTracciato(this.idTracciato);
		dto.setIdIncasso(this.idIncasso);

		return dto;
	}

	public String getIdTransazione() {
		return idTransazione;
	}

	public void setIdTransazione(String idTransazione) {
		this.idTransazione = idTransazione;
	}

	public boolean isRegistraEvento() {
		return registraEvento;
	}

	public void setRegistraEvento(boolean registraEvento) {
		this.registraEvento = registraEvento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(String idPagamento) {
		this.idPagamento = idPagamento;
	}

	public String getSottotipoEsito() {
		return sottotipoEsito;
	}

	public void setSottotipoEsito(String sottotipoEsito) {
		this.sottotipoEsito = sottotipoEsito;
	}

	public Long getIdFr() {
		return idFr;
	}

	public void setIdFr(Long idFr) {
		this.idFr = idFr;
	}

	public Long getIdIncasso() {
		return idIncasso;
	}

	public void setIdIncasso(Long idIncasso) {
		this.idIncasso = idIncasso;
	}

	public Long getIdTracciato() {
		return idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

}
