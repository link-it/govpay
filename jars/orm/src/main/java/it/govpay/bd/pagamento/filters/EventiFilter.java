package it.govpay.bd.pagamento.filters;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.Evento;
import it.govpay.orm.dao.jdbc.converter.EventoFieldConverter;

public class EventiFilter extends AbstractFilter{
	
	private String codDominio= null;
	private List<String> codDomini;
	private String iuv;
	private String ccp;
	private String codApplicazione;
	private String codVersamentoEnte;
	private String idSessione;
	private Date datainizio;
	private Date dataFine;
	private List<Long> idEventi= null;
	private String esito;
	private String componente;
	private String tipoEvento;
	private String categoria;
	private String ruolo;
	
	
	public EventiFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public EventiFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Evento.model().COMPONENTE);
		this.listaFieldSimpleSearch.add(Evento.model().TIPO_EVENTO);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			EventoFieldConverter eventoFieldConverter = new EventoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

			boolean addAnd = false;
			
			if(this.codDominio != null){
				newExpression.equals(Evento.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.codDomini != null && !this.codDomini.isEmpty()){
				this.codDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				
				newExpression.in(Evento.model().COD_DOMINIO, this.codDomini);
				addAnd = true;
			}

			
			if(this.iuv != null && StringUtils.isNotEmpty(this.iuv)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(Evento.model().IUV, this.iuv, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.ccp != null && StringUtils.isNotEmpty(this.ccp)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(Evento.model().CCP, this.ccp, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			
			if(this.datainizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Evento.model().DATA, this.datainizio,this.dataFine);
				addAnd = true;
			} else {
				if(this.datainizio != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.greaterEquals(Evento.model().DATA, this.datainizio);
					addAnd = true;
				} 
				
				if(this.dataFine != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.lessEquals(Evento.model().DATA, this.dataFine);
					addAnd = true;
				}
			}
			
			if(this.codVersamentoEnte!= null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().COD_VERSAMENTO_ENTE, this.codVersamentoEnte);
				addAnd = true;
			}
			
			if(this.codApplicazione!= null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.idSessione!= null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().ID_SESSIONE, this.idSessione);
				addAnd = true;
			}
			
			if(this.esito != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().ESITO, this.esito);
				addAnd = true;
			}
			
			if(this.idEventi != null && !this.idEventi.isEmpty()){
				CustomField idEventoField = new CustomField("id",  Long.class, "id",  eventoFieldConverter.toTable(Evento.model()));
				newExpression.in(idEventoField, this.idEventi);
				addAnd = true;
			}
			
			if(this.componente != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().COMPONENTE, this.componente);
				addAnd = true;
			}
			
			if(this.categoria != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().CATEGORIA_EVENTO, this.categoria);
				addAnd = true;
			}
			
			if(this.ruolo != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().RUOLO, this.ruolo);
				addAnd = true;
			}
			
			if(this.tipoEvento != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().TIPO_EVENTO, this.tipoEvento);
				addAnd = true;
			}
			

			return newExpression;
		}  catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCcp() {
		return this.ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public Date getDatainizio() {
		return this.datainizio;
	}

	public void setDatainizio(Date datainizio) {
		this.datainizio = datainizio;
	}

	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public List<Long> getIdEventi() {
		return this.idEventi;
	}

	public void setIdEventi(List<Long> idEventi) {
		this.idEventi = idEventi;
	}

	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public List<String> getCodDomini() {
		return codDomini;
	}

	public void setCodDomini(List<String> codDomini) {
		this.codDomini = codDomini;
	}

	public String getIdSessione() {
		return idSessione;
	}

	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
}
