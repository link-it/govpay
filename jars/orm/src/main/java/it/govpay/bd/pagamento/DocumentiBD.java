/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.bd.pagamento;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.converter.DocumentoConverter;
import it.govpay.orm.dao.jdbc.JDBCDocumentoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.DocumentoFieldConverter;

public class DocumentiBD extends BasicBD {

	public DocumentiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public DocumentiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public DocumentiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public DocumentiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public Documento getDocumento(long id) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Documento vo = ((JDBCDocumentoServiceSearch)this.getDocumentoService()).get(id);
			return DocumentoConverter.toDTO(vo);
		} catch (NotImplementedException | MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public Documento getDocumentoByApplicazioneDominioIdentificativo(Long idApplicazione, Long idDominio, String codDocumento) throws NotFoundException, ServiceException {
		
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getDocumentoService().newExpression();
			
			DocumentoFieldConverter fieldConverter = new DocumentoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Documento.model())), idDominio);
			exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", fieldConverter.toTable(it.govpay.orm.Documento.model())), idApplicazione);
			exp.equals(it.govpay.orm.Documento.model().COD_DOCUMENTO, codDocumento);
			it.govpay.orm.Documento docuemnto = this.getDocumentoService().find(exp);
			return DocumentoConverter.toDTO(docuemnto);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
