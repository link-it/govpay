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
	private Long idApplicazione;
	private String codApplicazione;
	private String codVersamentoEnte;
	private String idSessione;
	private Date datainizio;
	private Date dataFine;
	private List<Long> idEventi= null;
	
	public EventiFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public EventiFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Evento.model().IUV);
		this.listaFieldSimpleSearch.add(Evento.model().CCP);
		this.listaFieldSimpleSearch.add(Evento.model().COD_DOMINIO);
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
			}
			
			if(this.idApplicazione != null) {
				if(addAnd)
					newExpression.and();
				
				CustomField idApplicazioneField = new CustomField("id_applicazione",  Long.class, "id_applicazione",  eventoFieldConverter.toTable(Evento.model().ID_VERSAMENTO));
				newExpression.equals(idApplicazioneField, this.idApplicazione);
				addAnd = true;
			}
			
			if(this.codVersamentoEnte!= null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE, this.codVersamentoEnte);
				addAnd = true;
			}
			
			if(this.codApplicazione!= null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.idSessione!= null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Evento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE, this.idSessione);
				addAnd = true;
			}
			
			
			
			if(this.idEventi != null && !this.idEventi.isEmpty()){
				CustomField idEventoField = new CustomField("id",  Long.class, "id",  eventoFieldConverter.toTable(Evento.model()));
				newExpression.in(idEventoField, this.idEventi);
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

	public Long getIdApplicazione() {
		return this.idApplicazione;
	}

	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
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
	
}
