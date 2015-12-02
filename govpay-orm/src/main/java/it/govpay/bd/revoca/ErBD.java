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
package it.govpay.bd.revoca;

import java.util.Date;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Er;
import it.govpay.bd.model.converter.ErConverter;
import it.govpay.bd.pagamento.TracciatiBD.TipoTracciato;
import it.govpay.orm.ER;
import it.govpay.orm.IdEr;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.TracciatoXML;
import it.govpay.orm.dao.jdbc.JDBCERServiceSearch;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class ErBD extends BasicBD {

	public ErBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera l'Er identificato dalla chiave fisica
	 * 
	 * @param idEr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Er getEr(long idEr) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			ER vo = ((JDBCERServiceSearch)this.getServiceManager().getERServiceSearch()).get(idEr);
			
			return getDTO(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera l'Er identificato dalla chiave logica
	 * 
	 * @param idEr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Er getEr(String codMsgEsito) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			
			IdEr id = new IdEr();
			id.setCodMsgEsito(codMsgEsito);
			ER vo = this.getServiceManager().getERServiceSearch().get(id);
			return getDTO(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	private Er getDTO(ER vo) throws ServiceException, ExpressionNotImplementedException, ExpressionException, NotImplementedException {
		Er dto = ErConverter.toDTO(vo);
		return dto;
	}
	
	/**
	 * Inserisce un nuovo er
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertEr(Er er, byte[] documento) throws ServiceException, NotFoundException {
		try {
			
			it.govpay.orm.ER vo = ErConverter.toVO(er);
			insertTracciato(vo, documento);
			er.setIdTracciatoXML(vo.getIdTracciatoXML().getId());
			this.getServiceManager().getERService().create(vo);
			er.setId(vo.getId());

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	private void insertTracciato(it.govpay.orm.ER er, byte[] xml) throws ServiceException, NotImplementedException {
		TracciatoXML tracciatoXML = new TracciatoXML();
		tracciatoXML.setTipoTracciato(TipoTracciato.RR.name());
		tracciatoXML.setCodMessaggio(er.getCodMsgEsito());
		tracciatoXML.setDataOraCreazione(new Date());
		tracciatoXML.setXml(xml);

		this.getServiceManager().getTracciatoXMLService().create(tracciatoXML);
		
		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(tracciatoXML.getId());
		
		er.setIdTracciatoXML(idTracciato);
	}

	
}
