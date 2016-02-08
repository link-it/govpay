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
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.converter.AnagraficaConverter;
import it.govpay.bd.model.converter.RptConverter;
import it.govpay.bd.pagamento.TracciatiBD.TipoTracciato;
import it.govpay.orm.Carrello;
import it.govpay.orm.IdAnagrafica;
import it.govpay.orm.IdRpt;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.RPT;
import it.govpay.orm.TracciatoXML;
import it.govpay.orm.dao.jdbc.JDBCRPTServiceSearch;
import it.govpay.orm.dao.jdbc.converter.RPTFieldConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class RptBD extends BasicBD {

	public RptBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'RPT identificato dalla chiave fisica
	 * 
	 * @param idTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rpt getRpt(long idRpt) throws NotFoundException, ServiceException {
		try {
			RPT rptVO = ((JDBCRPTServiceSearch)this.getRptService()).get(idRpt);
			return toRpt(rptVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera l'RPT identificato dal msg id
	 * 
	 * @param codMsgRichiesta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rpt getRpt(String codMsgRichiesta) throws NotFoundException, ServiceException {
		try {
			IdRpt id = new IdRpt();
			id.setCodMsgRichiesta(codMsgRichiesta);
			RPT rptVO = this.getRptService().get(id);
			return toRpt(rptVO);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	

	/**
	 * Recupera l'RPT afferente ad un Tributo con indice maggiore.
	 * @param idVersamento
	 * @return
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public Rpt getLastRpt(long idVersamento) throws NotFoundException, ServiceException {
		try {
			IPaginatedExpression exp = this.getRptService().newPaginatedExpression();
			RPTFieldConverter fieldConverter = new RPTFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_versamento", Long.class, "id_versamento", fieldConverter.toTable(it.govpay.orm.RPT.model())), idVersamento);
			exp.sortOrder(SortOrder.DESC);
			exp.addOrder(RPT.model().DATA_ORA_CREAZIONE);
			exp.offset(0);
			exp.limit(1);
			List<RPT> rptLst = this.getRptService().findAll(exp);

			if(rptLst.size() <= 0) {
				throw new NotFoundException("Impossibile trovate un RPT con id versamento["+idVersamento+"]");	
			}

			return toRpt(rptLst.get(0));

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} 

	}

	public Rpt getRptByCodSessione(String codSessione) throws NotFoundException, ServiceException {
		try {
			IPaginatedExpression exp = this.getRptService().newPaginatedExpression();
			exp.equals(it.govpay.orm.RPT.model().COD_SESSIONE, codSessione);
			exp.sortOrder(SortOrder.DESC);
			exp.addOrder(RPT.model().DATA_ORA_CREAZIONE);
			exp.offset(0);
			exp.limit(1);
			List<RPT> rptLst = this.getRptService().findAll(exp);

			if(rptLst.size() <= 0) {
				throw new NotFoundException("Impossibile trovate un RPT con cod sessione["+codSessione+"]");	
			}

			return toRpt(rptLst.get(0));

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} 
	}

	/**
	 * Inserisce l'RPT.
	 * 
	 * @param rpt
	 * @param documentoXml
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void insertRpt(Rpt rpt, byte[] documento) throws NotFoundException, ServiceException {
		try {
			RPT rptVo = RptConverter.toVO(rpt);
			rptVo.setIdTracciatoXML(insertTracciato(rptVo.getCodMsgRichiesta(), documento));
			if(rpt.getAnagraficaVersante() != null) {
				rptVo.setIdAnagraficaVersante(insertAnagrafica(rpt.getAnagraficaVersante()));
			}
			this.getRptService().create(rptVo);
			rpt.setId(rptVo.getId());
			rpt.setIdTracciatoXML(rptVo.getIdTracciatoXML().getId());

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	
	/**
	 * Inserisce un carrello di RPT
	 * 
	 * @param codCarrello
	 * @param idsRpt
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void insertCarrelloRpt(String codCarrello, List<Long> idsRpt) throws NotFoundException, ServiceException {
		try {

			for(Long rpt: idsRpt) {
				Carrello carrello = new Carrello();
				carrello.setCodCarrello(codCarrello);
				IdRpt idRpt = new IdRpt();
				idRpt.setId(rpt);
				carrello.setIdRpt(idRpt);
				this.getCarrelloService().create(carrello);
			}

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Aggiorna lo stato di una RPT identificata dall'id
	 * @param idRpt
	 * @param stato
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void updateStatoRpt(long idRpt, Rpt.StatoRpt stato, Rpt.FaultNodo fault, String descrizione) throws NotFoundException, ServiceException{
		try {
			if(!((JDBCRPTServiceSearch)this.getRptService()).exists(idRpt)) {
				throw new NotFoundException("RPT con id ["+idRpt+"] non trovato.");
			}
			IdRpt idVO = ((JDBCRPTServiceSearch)this.getRptService()).findId(idRpt, true);

			List<UpdateField> lstUpdateFields = new ArrayList<UpdateField>();

			if(stato != null) {
				lstUpdateFields.add(new UpdateField(RPT.model().STATO, stato.toString()));
			}

			if(fault != null) {
				lstUpdateFields.add(new UpdateField(RPT.model().COD_FAULT, fault.toString()));
			}

			if(descrizione != null) {
				lstUpdateFields.add(new UpdateField(RPT.model().DESCRIZIONE_STATO, descrizione));
			}

			this.getRptService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}

	public void updateRpt(long idRpt, String codSessione, String pspRedirectUrl) throws ServiceException, NotFoundException {
		try {
			if(!((JDBCRPTServiceSearch)this.getRptService()).exists(idRpt)) {
				throw new NotFoundException("RPT con id ["+idRpt+"] non trovato.");
			}
			IdRpt idVO = ((JDBCRPTServiceSearch)this.getRptService()).findId(idRpt, true);

			UpdateField sessioneField = new UpdateField(RPT.model().COD_SESSIONE, codSessione);
			UpdateField pspRedirectUrlField = new UpdateField(RPT.model().PSP_REDIRECT_URL, pspRedirectUrl);
			this.getRptService().updateFields(idVO, sessioneField, pspRedirectUrlField);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	
	
	private IdTracciato insertTracciato(String codMsgRichiesta, byte[] xml) throws ServiceException, NotImplementedException {
		TracciatoXML tracciatoXML = new TracciatoXML();
		tracciatoXML.setTipoTracciato(TipoTracciato.RPT.name());
		tracciatoXML.setCodMessaggio(codMsgRichiesta);
		tracciatoXML.setDataOraCreazione(new Date());
		tracciatoXML.setXml(xml);

		this.getTracciatoXMLService().create(tracciatoXML);

		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(tracciatoXML.getId());

		return idTracciato;
	}
	
	private IdAnagrafica insertAnagrafica(Anagrafica anagraficaVersante)  throws ServiceException, NotImplementedException{
		it.govpay.orm.Anagrafica voAnagrafica = AnagraficaConverter.toVO(anagraficaVersante);
		this.getAnagraficaService().create(voAnagrafica);
		IdAnagrafica idAnagrafica = new IdAnagrafica();
		idAnagrafica.setId(voAnagrafica.getId());
		return idAnagrafica;
	}
	
	private Rpt toRpt(RPT rptVO) throws ServiceException, NotImplementedException {
		return toRpt(rptVO, null);
	}

	private Rpt toRpt(RPT rptVO, it.govpay.orm.Anagrafica anagraficaVersante) throws ServiceException, NotImplementedException {
		try {
			Rpt rpt = RptConverter.toDTO(rptVO);
			if(rptVO.getIdAnagraficaVersante() != null && anagraficaVersante == null) {
				IdAnagrafica idAnagrafica = new IdAnagrafica();
				idAnagrafica.setId(rptVO.getIdAnagraficaVersante().getId());
				anagraficaVersante = this.getAnagraficaService().get(idAnagrafica);
			}
			if(anagraficaVersante != null)
				rpt.setAnagraficaVersante(AnagraficaConverter.toDTO(anagraficaVersante));
			return rpt;
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public List<Rpt> getRptPendenti(String codDominio) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getRptService().newPaginatedExpression();
			
			exp.equals(RPT.model().COD_DOMINIO, codDominio);
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RPT_INVIO_A_NODO_FALLITO.toString());
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RPT_RIFIUTATA_NODO.toString());
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RPT_RIFIUTATA_PSP.toString());
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RPT_ERRORE_INVIO_A_PSP.toString());
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RT_ACCETTATA_PA.toString());
			
			List<RPT> findAll = this.getRptService().findAll(exp);
			List<Rpt> findAllDTO = new ArrayList<Rpt>();
			if(findAll != null) {
				for(RPT rpt: findAll) {
					findAllDTO.add(toRpt(rpt));
				}
			}
			return findAllDTO;
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

}
