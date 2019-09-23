/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.bd.pagamento.filters;

import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;

import it.govpay.bd.AbstractFilter;
import it.govpay.orm.Tracciato;

public class TracciatoFilter extends AbstractFilter {

	private String filenameRichiestaLike;
	private String filenameRichiesta;
	private List<it.govpay.model.Tracciato.TIPO_TRACCIATO> tipo;
	private it.govpay.model.Tracciato.STATO_ELABORAZIONE stato;
	private it.govpay.model.Tracciato.FORMATO_TRACCIATO formato;
	private String dettaglioStato;
	private List<String> domini;
	private String codDominio;
	private String codTipoVersamento;
	private String operatore;

	public String getFilenameRichiestaLike() {
		return this.filenameRichiestaLike;
	}

	public void setFilenameRichiestaLike(String filenameRichiestaLike) {
		this.filenameRichiestaLike = filenameRichiestaLike;
	}

	public TracciatoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public TracciatoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression exp = this.newExpression();
			boolean addAnd = false;
			
			if(this.filenameRichiestaLike != null){
				exp.like(Tracciato.model().FILE_NAME_RICHIESTA, this.filenameRichiestaLike,LikeMode.END); 
				addAnd = true;
			}
			
			if(this.filenameRichiesta != null){
				exp.equals(Tracciato.model().FILE_NAME_RICHIESTA, this.filenameRichiesta); 
				addAnd = true;
			}
			
			if(this.tipo != null && !this.tipo.isEmpty()){
				if(addAnd)
					exp.and();
				exp.in(Tracciato.model().TIPO, this.tipo.stream().map(t -> t.toString()).collect(Collectors.toList()));
				addAnd = true;
			}
			
			if(this.stato != null){
				if(addAnd)
					exp.and();
				
				exp.equals(Tracciato.model().STATO, this.stato.toString());
				
				if(this.getDettaglioStato() != null) {
					exp.like(Tracciato.model().BEAN_DATI, this.dettaglioStato,LikeMode.ANYWHERE);
				}
				
				addAnd = true;
			}
			
			if(this.formato != null){
				if(addAnd)
					exp.and();
				
				exp.equals(Tracciato.model().FORMATO, this.formato.toString());
				addAnd = true;
			}

			if(this.domini != null && !this.domini.isEmpty()){
				if(addAnd)
					exp.and();
				
				exp.in(Tracciato.model().COD_DOMINIO, this.domini); 
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					exp.and();
				
				exp.equals(Tracciato.model().COD_DOMINIO, this.codDominio); 
				addAnd = true;
			}
			
			if(this.codTipoVersamento != null){
				if(addAnd)
					exp.and();
				
				exp.equals(Tracciato.model().COD_TIPO_VERSAMENTO, this.codTipoVersamento); 
				addAnd = true;
			}

			return exp;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public it.govpay.model.Tracciato.STATO_ELABORAZIONE getStato() {
		return this.stato;
	}

	public void setStato(it.govpay.model.Tracciato.STATO_ELABORAZIONE stato) {
		this.stato = stato;
	}

	public List<String> getDomini() {
		return this.domini;
	}

	public void setDomini(List<String> domini) {
		this.domini = domini;
	}

	public List<it.govpay.model.Tracciato.TIPO_TRACCIATO> getTipo() {
		return this.tipo;
	}

	public void setTipo(List<it.govpay.model.Tracciato.TIPO_TRACCIATO> tipo) {
		this.tipo = tipo;
	}

	public String getFilenameRichiesta() {
		return this.filenameRichiesta;
	}

	public void setFilenameRichiesta(String filenameRichiesta) {
		this.filenameRichiesta = filenameRichiesta;
	}

	public String getOperatore() {
		return this.operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public String getDettaglioStato() {
		return dettaglioStato;
	}

	public void setDettaglioStato(String dettaglioStato) {
		this.dettaglioStato = dettaglioStato;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public it.govpay.model.Tracciato.FORMATO_TRACCIATO getFormato() {
		return formato;
	}

	public void setFormato(it.govpay.model.Tracciato.FORMATO_TRACCIATO formato) {
		this.formato = formato;
	}

	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}

	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}

}
