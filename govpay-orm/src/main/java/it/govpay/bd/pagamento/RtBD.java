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
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.converter.AnagraficaConverter;
import it.govpay.bd.model.converter.RtConverter;
import it.govpay.bd.pagamento.TracciatiBD.TipoTracciato;
import it.govpay.orm.Anagrafica;
import it.govpay.orm.IdAnagrafica;
import it.govpay.orm.IdRt;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.RT;
import it.govpay.orm.TracciatoXML;
import it.govpay.orm.dao.jdbc.JDBCRTServiceSearch;
import it.govpay.orm.dao.jdbc.converter.RTFieldConverter;

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
import org.openspcoop2.generic_project.expression.SortOrder;

public class RtBD extends BasicBD {

	public RtBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera l'RT identificato dalla chiave fisica
	 * 
	 * @param idTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rt getRt(long idRt) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			
			RT rtVO = ((JDBCRTServiceSearch)this.getRtService()).get(idRt);
			return getRT(rtVO);
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Recupera l'RT identificato dal msg id
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rt getRt(String codMsgRicevuta) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			
			IdRt id = new IdRt();
			id.setCodMsgRicevuta(codMsgRicevuta);
			
			RT rtVO = this.getRtService().get(id);
			return getRT(rtVO);
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	

	private Rt getRT(RT rtVO) throws ServiceException, NotFoundException,
			MultipleResultException, NotImplementedException {
		Rt rpt = RtConverter.toDTO(rtVO);
		IdAnagrafica idAnagrafica = new IdAnagrafica();
		idAnagrafica.setId(rtVO.getIdAnagraficaAttestante().getId());
		rpt.setAnagraficaAttestante(AnagraficaConverter.toDTO(this.getAnagraficaService().get(idAnagrafica)));
		return rpt;
	}

	private void insertTracciato(it.govpay.orm.RT rt, byte[] xml) throws ServiceException, NotImplementedException {
		TracciatoXML tracciatoXML = new TracciatoXML();
		tracciatoXML.setTipoTracciato(TipoTracciato.RT.name());
		tracciatoXML.setCodMessaggio(rt.getCodMsgRicevuta());
		tracciatoXML.setDataOraCreazione(new Date());
		tracciatoXML.setXml(xml);

		this.getTracciatoXMLService().create(tracciatoXML);
		
		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(tracciatoXML.getId());
		
		rt.setIdTracciato(idTracciato);
	}


	/**
	 * Recupera l'RT afferenti all'RPT indicata con id maggiore.
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rt getLastRt(long idRpt) throws ServiceException, NotFoundException {
		try {
			
			IPaginatedExpression exp = this.getRtService().newPaginatedExpression();
			RTFieldConverter fieldConverter = new RTFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_rpt", Long.class, "id_rpt", fieldConverter.toTable(it.govpay.orm.RT.model())), idRpt);
			exp.sortOrder(SortOrder.DESC);
			exp.addOrder(RT.model().DATA_ORA_MSG_RICEVUTA);
			exp.offset(0);
			exp.limit(1);
			List<RT> rtLst = this.getRtService().findAll(exp);
			
			if(rtLst.size() <= 0) {
				throw new NotFoundException("Impossibile trovate un RT con id rpt["+idRpt+"]");	
			}
			
			if(rtLst.size() != 1) {
			 throw new ServiceException("Impossibile determinare l'RT piu' recente");
			}

			return getRT(rtLst.get(0));
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Inserisce l'RT.
	 * 
	 * @param rt
	 * @param documentoXml
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void insertRt(Rt rt, byte[] documento) throws ServiceException {
		try {
			
			RT rtVo = RtConverter.toVO(rt);
			insertTracciato(rtVo, documento);
			rt.setIdTracciatoXML(rtVo.getIdTracciato().getId());
			Anagrafica anagraficaVo = AnagraficaConverter.toVO(rt.getAnagraficaAttestante());
			this.getAnagraficaService().create(anagraficaVo);
			
			IdAnagrafica idAnagraficaAttestante = new IdAnagrafica();
			idAnagraficaAttestante.setId(anagraficaVo.getId());
			
			rtVo.setIdAnagraficaAttestante(idAnagraficaAttestante);
			
			this.getRtService().create(rtVo);
			
			rt.setId(rtVo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
}
