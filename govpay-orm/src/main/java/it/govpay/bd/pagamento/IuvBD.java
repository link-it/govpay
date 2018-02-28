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
package it.govpay.bd.pagamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.id.serial.IDSerialGeneratorType;
import org.openspcoop2.utils.id.serial.InfoStatistics;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.converter.IuvConverter;
import it.govpay.bd.pagamento.filters.IuvFilter;
import it.govpay.bd.pagamento.util.IuvUtils;
import it.govpay.bd.model.Applicazione;
import it.govpay.model.Iuv;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.orm.IUV;
import it.govpay.orm.dao.jdbc.JDBCIUVService;
import it.govpay.orm.dao.jdbc.converter.IUVFieldConverter;

public class IuvBD extends BasicBD {

	private static Logger log = LoggerWrapperFactory.getLogger(IuvBD.class);

	public IuvBD(BasicBD basicBD) {
		super(basicBD);
	}

	public Iuv generaIuv(Applicazione applicazione, Dominio dominio, String codVersamentoEnte, TipoIUV type, String prefix) throws ServiceException {
		
		long prg = getNextPrgIuv(dominio.getCodDominio() + prefix, type);
		
		String iuv = null;
		
		switch (type) {
		case ISO11694:
			{
				String reference, check;
				
				switch (dominio.getAuxDigit()) {
				case 0: 
					reference = prefix + String.format("%0" + (21 - prefix.length()) + "d", prg);
					if(reference.length() > 21) 
						throw new ServiceException("Superato il numero massimo di IUV generabili [Dominio:"+dominio.getCodDominio()+" Prefisso:"+prefix+"]" );
					check = IuvUtils.getCheckDigit(reference);
					iuv = "RF" + check + reference;
				break;
				case 3: 
					if(dominio.getSegregationCode() == null)
						throw new ServiceException("Dominio configurato per IUV segregati privo di codice di segregazione [Dominio:"+dominio.getCodDominio()+"]" ); 

					reference = prefix + String.format("%0" + (19 - prefix.length()) + "d", prg);
					if(reference.length() > 19) 
						throw new ServiceException("Superato il numero massimo di IUV generabili [Dominio:"+dominio.getCodDominio()+" Prefisso:"+prefix+"]" );
					
					reference = String.format("%02d", dominio.getSegregationCode()) + reference;
					check = IuvUtils.getCheckDigit(reference);
					
					iuv = "RF" + check + reference;
				break;
				default: throw new ServiceException("Codice AUX non supportato [Dominio:"+dominio.getCodDominio()+" AuxDigit:"+dominio.getAuxDigit()+"]" ); 
				}
			}
		break;
		case NUMERICO:
			{
				String reference = prefix + String.format("%0" + (13 - prefix.length()) + "d", prg);
				
				if(reference.length() > 15) 
					throw new ServiceException("Superato il numero massimo di IUV generabili [Dominio:"+dominio.getCodDominio()+" Prefisso:"+prefix+"]" );
				
				String check = "";
				// Vedo se utilizzare l'application code o il segregation code
				switch (dominio.getAuxDigit()) {
					case 0: 
						check = IuvUtils.getCheckDigit93(reference, dominio.getAuxDigit(), dominio.getStazione().getApplicationCode()); 
						iuv = reference + check;
					break;
					case 3: 
						if(dominio.getSegregationCode() == null)
							throw new ServiceException("Dominio configurato per IUV segregati privo di codice di segregazione [Dominio:"+dominio.getCodDominio()+"]" ); 
						
						check = IuvUtils.getCheckDigit93(reference, dominio.getAuxDigit(), dominio.getSegregationCode()); 
						iuv = String.format("%02d", dominio.getSegregationCode()) + reference + check;
					break;
					default: throw new ServiceException("Codice AUX non supportato [Dominio:"+dominio.getCodDominio()+" AuxDigit:"+dominio.getAuxDigit()+"]" ); 
				}
				
				
				break;
			}
		}

		Iuv iuvDTO = new Iuv();
		iuvDTO.setIdDominio(dominio.getId());
		iuvDTO.setPrg(prg);
		iuvDTO.setIuv(iuv);
		iuvDTO.setDataGenerazione(new Date());
		iuvDTO.setIdApplicazione(applicazione.getId());
		iuvDTO.setTipo(type);
		iuvDTO.setCodVersamentoEnte(codVersamentoEnte);
		iuvDTO.setAuxDigit(dominio.getAuxDigit());
		iuvDTO.setApplicationCode(dominio.getStazione().getApplicationCode());
		
		return insertIuv(iuvDTO);
	}

	public Iuv insertIuv(Iuv iuv) throws ServiceException{
		IUV iuvVO = IuvConverter.toVO(iuv);
		try {
			this.getIuvService().create(iuvVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
		iuv.setId(iuvVO.getId());
		return iuv;
	}


	/**
	 * Crea un nuovo IUV a meno dell'iuv stesso.
	 * Il prg deve essere un progressivo all'interno del DominioEnte fornito
	 * 
	 * @param codDominio
	 * @param idApplicazione
	 * @return prg
	 * @throws ServiceException
	 */
	private long getNextPrgIuv(String codDominio, TipoIUV type) throws ServiceException {
		InfoStatistics infoStat = null;
		BasicBD bd = null;
		try {
			infoStat = new InfoStatistics();
			org.openspcoop2.utils.id.serial.IDSerialGenerator serialGenerator = new org.openspcoop2.utils.id.serial.IDSerialGenerator(infoStat);
			org.openspcoop2.utils.id.serial.IDSerialGeneratorParameter params = new org.openspcoop2.utils.id.serial.IDSerialGeneratorParameter("GovPay");
			params.setTipo(IDSerialGeneratorType.NUMERIC);
			params.setWrap(false);
			params.setInformazioneAssociataAlProgressivo(codDominio+type.toString()); // il progressivo sarà relativo a questa informazione

			java.sql.Connection con = null; 

			// Se sono in transazione aperta, utilizzo una connessione diversa perche' l'utility di generazione non supporta le transazioni.
			if(!isAutoCommit()) {
				bd = BasicBD.newInstance(this.getIdTransaction());
				con = bd.getConnection();
			} else {
				con = getConnection();
			}

			return serialGenerator.buildIDAsNumber(params, con, this.getJdbcProperties().getDatabase(), log);
		} catch (UtilsException e) {
			log.error("Numero di errori 'access serializable': "+infoStat.getErrorSerializableAccess());
			for (int i=0; i<infoStat.getExceptionOccurs().size(); i++) {
				Throwable t = infoStat.getExceptionOccurs().get(i);
				log.error("Errore-"+(i+1)+" (occurs:"+infoStat.getNumber(t)+"): "+t.getMessage());
			}
			throw new ServiceException(e);
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}

	/**
	 * Recupera lo IUV con la chiave logi generato
	 * @param iuv
	 * @return
	 * @throws ServiceException
	 */
	public Iuv getIuv(long idDominio, String iuv) throws ServiceException, NotFoundException {
		try {
			IExpression exp = this.getIuvService().newExpression();
			exp.equals(it.govpay.orm.IUV.model().IUV, iuv);
			IUVFieldConverter converter = new IUVFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField idDominioField = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(IUV.model()));

			exp.equals(idDominioField, idDominio);
			it.govpay.orm.IUV iuvVO = this.getIuvService().find(exp);

			Iuv iuvDTO = IuvConverter.toDTO(iuvVO);

			return iuvDTO;
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

	public Iuv getIuv(long idApplicazione, String codVersamentoEnte, TipoIUV tipo) throws ServiceException, NotFoundException {
		try {
			IPaginatedExpression exp = this.getIuvService().newPaginatedExpression();
			exp.equals(it.govpay.orm.IUV.model().COD_VERSAMENTO_ENTE, codVersamentoEnte);
			exp.equals(it.govpay.orm.IUV.model().TIPO_IUV, tipo.getCodifica());
			IUVFieldConverter converter = new IUVFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField idDominioField = new CustomField("id_applicazione", Long.class, "id_applicazione", converter.toTable(IUV.model()));
			exp.equals(idDominioField, idApplicazione);
			List<it.govpay.orm.IUV> iuvVO = this.getIuvService().findAll(exp);
			List<Iuv> iuvs = IuvConverter.toDTOList(iuvVO);
			if(iuvs.size() > 0)
				return Collections.max(iuvs, new IuvComparator());
			else 
				throw new NotFoundException();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} 
	}

	/**
	 * Recupera lo IUV con la chiave logi generato
	 * @param iuv
	 * @return
	 * @throws ServiceException
	 */
	public Iuv getIuv(long idIuv) throws ServiceException {
		try {
			it.govpay.orm.IUV iuvVO = ((JDBCIUVService)this.getIuvService()).get(idIuv);
			Iuv iuvDTO = IuvConverter.toDTO(iuvVO);

			return iuvDTO;
		}  catch (NotFoundException e) {
			throw new ServiceException(e);
		}  catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	public IuvFilter newFilter() throws ServiceException {
		return new IuvFilter(this.getIuvService());
	}
	
	public IuvFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new IuvFilter(this.getIuvService(),simpleSearch);
	}

	public long count(IuvFilter filter) throws ServiceException {
		try {
			return this.getIuvService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Iuv> findAll(IuvFilter filter) throws ServiceException {
		try {
			List<Iuv> iuvLst = new ArrayList<Iuv>();
			List<it.govpay.orm.IUV> iuvVOLst = this.getIuvService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.IUV iuvVO: iuvVOLst) {
				iuvLst.add(IuvConverter.toDTO(iuvVO));
			}
			return iuvLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public class IuvComparator implements Comparator<Iuv> {
		@Override
		public int compare(Iuv o1, Iuv o2) {
			return o1.getDataGenerazione().compareTo(o2.getDataGenerazione());
		}
	}
}
