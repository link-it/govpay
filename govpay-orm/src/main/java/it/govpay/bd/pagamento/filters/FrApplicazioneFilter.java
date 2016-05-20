/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.FrApplicazione;
import it.govpay.orm.dao.jdbc.converter.FrApplicazioneFieldConverter;

public class FrApplicazioneFilter extends AbstractFilter {
	
	private String codFlusso;
	private int annoRiferimento;
	private List<Long> idApplicazioni;
	private List<Long> idFlussi; 
	private Long idFrApplicazione;

	public FrApplicazioneFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = newExpression();
			
			boolean addAnd = false;
			
			if(this.annoRiferimento > 0){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(FrApplicazione.model().ID_FR.ANNO_RIFERIMENTO, this.annoRiferimento);
				addAnd = true;
			}
			
			if(this.codFlusso != null && StringUtils.isNotEmpty(this.codFlusso)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(FrApplicazione.model().ID_FR.COD_FLUSSO, this.codFlusso, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.idApplicazioni!= null && !this.idApplicazioni.isEmpty()) {
				if(addAnd)
					newExpression.and();
				
				FrApplicazioneFieldConverter converter = new FrApplicazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabaseType());
				IField idDominioField = new CustomField("id_applicazione", Long.class, "id_applicazione", converter.toTable(FrApplicazione.model()));
				newExpression.in(idDominioField, this.idApplicazioni);
				addAnd = true;
			}
			
			if(this.idFlussi != null && !this.idFlussi.isEmpty()){
				if(addAnd)
					newExpression.and();
				
				FrApplicazioneFieldConverter converter = new FrApplicazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabaseType());
				IField idField = new CustomField("id_fr", Long.class, "id_fr", converter.toTable(FrApplicazione.model()));
				newExpression.in(idField, this.idFlussi);
				addAnd = true;
			}
			

			if(this.idFrApplicazione != null){
				if(addAnd)
					newExpression.and();
				FrApplicazioneFieldConverter converter = new FrApplicazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(FrApplicazione.model()));
				newExpression.equals(cf, this.idFrApplicazione);
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
 
	public int getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(int annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public String getCodFlusso() {
		return codFlusso;
	}

	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}

	public List<Long> getIdApplicazioni() {
		return idApplicazioni;
	}

	public void setIdApplicazioni(List<Long> idApplicazioni) {
		this.idApplicazioni = idApplicazioni;
	}

	public List<Long> getIdFlussi() {
		return idFlussi;
	}

	public void setIdFlussi(List<Long> idFlussi) {
		this.idFlussi = idFlussi;
	}

	public Long getIdFrApplicazione() {
		return idFrApplicazione;
	}

	public void setIdFrApplicazione(Long idFrApplicazione) {
		this.idFrApplicazione = idFrApplicazione;
	}
	
}
