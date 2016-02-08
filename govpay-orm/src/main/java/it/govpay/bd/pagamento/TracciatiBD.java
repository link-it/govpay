/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.bd.pagamento;

import it.govpay.bd.BasicBD;
import it.govpay.orm.TracciatoXML;
import it.govpay.orm.dao.jdbc.JDBCTracciatoXMLServiceSearch;

import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class TracciatiBD extends BasicBD {

	public enum TipoTracciato {
		PSP, RPT, RT, ER, RR, FR
	}

	public TracciatiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera il tracciatoXML identificato dalla chiave fisica
	 * 
	 * @param idTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public byte[] getTracciato(long idTracciato) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			return ((JDBCTracciatoXMLServiceSearch)this.getTracciatoXMLService()).get(idTracciato).getXml();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera l'RT identificato dal msg id (in caso di multipli, quello con dataOraCreazione maggiore)
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public byte[] getTracciato(TipoTracciato tipoTracciato, String codMessaggio) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IPaginatedExpression exp = this.getTracciatoXMLService().newPaginatedExpression();
			exp.equals(TracciatoXML.model().COD_MESSAGGIO, codMessaggio);
			exp.equals(TracciatoXML.model().TIPO_TRACCIATO, tipoTracciato.name());
			exp.sortOrder(SortOrder.DESC);
			exp.addOrder(TracciatoXML.model().DATA_ORA_CREAZIONE);
			
			exp.offset(0);
			exp.limit(1);
			
			List<TracciatoXML> tracciatoLst = this.getTracciatoXMLService().findAll(exp);
			
			if(tracciatoLst.size() <= 0) {
				throw new NotFoundException("Impossibile trovate un tracciato con codice messaggio ["+codMessaggio+"] e tipo tracciato ["+tipoTracciato.name()+"]");	
			}
			
			if(tracciatoLst.size() != 1) {
			 throw new ServiceException("Impossibile determinare il tracciato piu' recente");
			}
			
			return tracciatoLst.get(0).getXml();
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
}
