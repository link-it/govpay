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

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.SingolaRevoca;
import it.govpay.bd.model.converter.RrConverter;
import it.govpay.bd.model.converter.SingolaRevocaConverter;
import it.govpay.bd.pagamento.TracciatiBD.TipoTracciato;
import it.govpay.orm.IdRr;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.RR;
import it.govpay.orm.TracciatoXML;
import it.govpay.orm.dao.jdbc.JDBCRRServiceSearch;
import it.govpay.orm.dao.jdbc.converter.SingolaRevocaFieldConverter;

import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

public class RrBD extends BasicBD {

	public RrBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera l'Rr identificato dalla chiave fisica
	 * 
	 * @param idRr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rr getRr(long idRr) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			RR vo = ((JDBCRRServiceSearch)this.getServiceManager().getRRServiceSearch()).get(idRr);
			
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
	 * Recupera l'Rr identificato dalla chiave logica
	 * 
	 * @param idRr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rr getRr(String codMsgRevoca) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			
			IdRr id = new IdRr();
			id.setCodMsgRevoca(codMsgRevoca);
			RR vo = this.getServiceManager().getRRServiceSearch().get(id);
			return getDTO(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	private Rr getDTO(RR vo) throws ServiceException, ExpressionNotImplementedException, ExpressionException, NotImplementedException {
		Rr dto = RrConverter.toDTO(vo);
		IPaginatedExpression exp = this.getServiceManager().getSingolaRevocaServiceSearch().newPaginatedExpression();
		SingolaRevocaFieldConverter fieldConverter = new SingolaRevocaFieldConverter(this.getServiceManager().getJdbcProperties().getDatabaseType());
		exp.equals(new CustomField("id_rr", Long.class, "id_rr", fieldConverter.toTable(it.govpay.orm.SingolaRevoca.model())), vo.getId());
		List<it.govpay.orm.SingolaRevoca> lstRevoche = this.getServiceManager().getSingolaRevocaServiceSearch().findAll(exp);
		if(lstRevoche != null && !lstRevoche.isEmpty()) {
			dto.setSingolaRevocaList(SingolaRevocaConverter.toDTOList(lstRevoche));
		}
		
		return dto;
	}
	
	/**
	 * Inserisce un nuovo rr
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertRr(Rr rr, byte[] documento) throws ServiceException, NotFoundException {
		try {
			
			it.govpay.orm.RR vo = RrConverter.toVO(rr);
			insertTracciato(vo, documento);
			rr.setIdTracciatoXML(vo.getIdTracciatoXML().getId());

			this.getServiceManager().getRRService().create(vo);
			rr.setId(vo.getId());

			if(rr.getSingolaRevocaList() != null && !rr.getSingolaRevocaList().isEmpty()) {
				for(SingolaRevoca singolaRevoca: rr.getSingolaRevocaList()) {
					it.govpay.orm.SingolaRevoca voRevoca = SingolaRevocaConverter.toVO(singolaRevoca);
					IdRr idRR = new IdRr();
					idRR.setId(vo.getId());
					voRevoca.setIdRR(idRR);
					this.getServiceManager().getSingolaRevocaService().create(voRevoca);
					singolaRevoca.setId(voRevoca.getId());
				}
			}
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	private void insertTracciato(it.govpay.orm.RR rr, byte[] xml) throws ServiceException, NotImplementedException {
		TracciatoXML tracciatoXML = new TracciatoXML();
		tracciatoXML.setTipoTracciato(TipoTracciato.RR.name());
		tracciatoXML.setCodMessaggio(rr.getCodMsgRevoca());
		tracciatoXML.setDataOraCreazione(new Date());
		tracciatoXML.setXml(xml);

		this.getServiceManager().getTracciatoXMLService().create(tracciatoXML);
		
		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(tracciatoXML.getId());
		
		rr.setIdTracciatoXML(idTracciato);
	}

	
}
