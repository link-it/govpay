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
package it.govpay.bd.rendicontazione;

import it.govpay.bd.BasicBD;
import it.govpay.bd.IFilter;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.SingolaRendicontazione;
import it.govpay.bd.model.converter.FrConverter;
import it.govpay.bd.model.converter.SingolaRendicontazioneConverter;
import it.govpay.bd.pagamento.TracciatiBD.TipoTracciato;
import it.govpay.orm.FR;
import it.govpay.orm.IdFr;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.TracciatoXML;
import it.govpay.orm.dao.jdbc.JDBCFRServiceSearch;
import it.govpay.orm.dao.jdbc.converter.SingolaRendicontazioneFieldConverter;

import java.util.ArrayList;
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

public class FrBD extends BasicBD {

	public FrBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera l'Fr identificato dalla chiave fisica
	 * 
	 * @param idFr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Fr getFr(long idFr) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			FR vo = ((JDBCFRServiceSearch)this.getFrService()).get(idFr);
			
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
	 * Recupera l'Fr identificato dalla chiave logica
	 * 
	 * @param idFr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Fr getFr(int annoRiferimento, String codFlusso) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			
			IdFr id = new IdFr();
			id.setAnnoRiferimento(annoRiferimento);
			id.setCodFlusso(codFlusso);
			FR vo = this.getFrService().get(id);
			return getDTO(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	private Fr getDTO(FR vo) throws ServiceException, ExpressionNotImplementedException, ExpressionException, NotImplementedException {
		Fr dto = FrConverter.toDTO(vo);
		IPaginatedExpression exp = this.getSingolaRendicontazioneService().newPaginatedExpression();
		SingolaRendicontazioneFieldConverter fieldConverter = new SingolaRendicontazioneFieldConverter(this.getJdbcProperties().getDatabaseType());
		exp.equals(new CustomField("id_fr", Long.class, "id_fr", fieldConverter.toTable(it.govpay.orm.SingolaRendicontazione.model())), vo.getId());
		List<it.govpay.orm.SingolaRendicontazione> lstRevoche = this.getSingolaRendicontazioneService().findAll(exp);
		if(lstRevoche != null && !lstRevoche.isEmpty()) {
			dto.setSingolaRendicontazioneList(SingolaRendicontazioneConverter.toDTOList(lstRevoche));
		}
		
		return dto;
	}
	
	/**
	 * Inserisce un nuovo fr
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertFr(Fr fr, byte[] documento) throws ServiceException, NotFoundException {
		try {
			
			it.govpay.orm.FR vo = FrConverter.toVO(fr);
			insertTracciato(vo, documento);
			fr.setIdTracciatoXML(vo.getIdTracciatoXML().getId());
			this.getFrService().create(vo);
			fr.setId(vo.getId());

			if(fr.getSingolaRendicontazioneList() != null && !fr.getSingolaRendicontazioneList().isEmpty()) {
				for(SingolaRendicontazione singolaRendicontazione: fr.getSingolaRendicontazioneList()) {
					it.govpay.orm.SingolaRendicontazione voRendicontazione = SingolaRendicontazioneConverter.toVO(singolaRendicontazione);
					IdFr idFr = new IdFr();
					idFr.setId(vo.getId());
					voRendicontazione.setIdFr(idFr);

					this.getSingolaRendicontazioneService().create(voRendicontazione);
					singolaRendicontazione.setId(voRendicontazione.getId());
				}
			}
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	private void insertTracciato(it.govpay.orm.FR fr, byte[] xml) throws ServiceException, NotImplementedException {
		TracciatoXML tracciatoXML = new TracciatoXML();
		tracciatoXML.setTipoTracciato(TipoTracciato.FR.name());
		tracciatoXML.setCodMessaggio(fr.getCodFlusso());
		tracciatoXML.setDataOraCreazione(new Date());
		tracciatoXML.setXml(xml);

		this.getTracciatoXMLService().create(tracciatoXML);
		
		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(tracciatoXML.getId());
		
		fr.setIdTracciatoXML(idTracciato);
	}

	public boolean exists(int annoRiferimento, String codFlusso) throws ServiceException {
		try {
			IdFr id = new IdFr();
			id.setAnnoRiferimento(annoRiferimento);
			id.setCodFlusso(codFlusso);
			
			return this.getFrService().exists(id);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	public FrFilter newFilter() throws ServiceException {
		return new FrFilter(this.getFrService());
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getFrService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Fr> findAll(IFilter filter) throws ServiceException {
		try {
			List<Fr> frLst = new ArrayList<Fr>();
			List<it.govpay.orm.FR> frVOLst = this.getFrService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.FR frVO: frVOLst) {
				frLst.add(getDTO(frVO));
			}
			return frLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
}
