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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.Function;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.id.serial.IDSerialGeneratorType;
import org.openspcoop2.utils.id.serial.InfoStatistics;

import it.govpay.bd.BasicBD;
import it.govpay.bd.exception.VersamentoException;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.converter.SingoloVersamentoConverter;
import it.govpay.bd.model.converter.VersamentoConverter;
import it.govpay.bd.nativequeries.NativeQueries;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.pagamento.util.CountPerDominio;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.ModoAvvisatura;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.dao.IDBSingoloVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.SingoloVersamentoFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;

public class VersamentiBD extends BasicBD {

	public VersamentiBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera il versamento identificato dalla chiave fisica
	 */
	public Versamento getVersamento(long id) throws ServiceException {
		try {
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(id);
			it.govpay.orm.Versamento versamento = this.getVersamentoService().get(idVersamento);
			return VersamentoConverter.toDTO(versamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera il versamento identificato dalla chiave logica
	 */
	public Versamento getVersamento(String codDominio, String iuv) throws NotFoundException, ServiceException {
		try {
			IExpression exp = this.getVersamentoService().newExpression();
			exp.equals(it.govpay.orm.Versamento.model().ID_UO.ID_DOMINIO.COD_DOMINIO, codDominio);
			exp.and().isNotNull(it.govpay.orm.Versamento.model().ID_UO.COD_UO);	
			exp.equals(it.govpay.orm.Versamento.model().IUV_VERSAMENTO,iuv);
			it.govpay.orm.Versamento versamento = this.getVersamentoService().find(exp);
			return VersamentoConverter.toDTO(versamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera il versamento identificato dalla coppia dominio/numeroavviso
	 */
	public Versamento getVersamentoFromDominioNumeroAvviso(String codDominio, String numeroAvviso) throws NotFoundException, ServiceException {
		try {
			IExpression exp = this.getVersamentoService().newExpression();
			exp.equals(it.govpay.orm.Versamento.model().ID_UO.ID_DOMINIO.COD_DOMINIO, codDominio);
			exp.and().isNotNull(it.govpay.orm.Versamento.model().ID_UO.COD_UO);
			exp.equals(it.govpay.orm.Versamento.model().NUMERO_AVVISO,numeroAvviso);
			it.govpay.orm.Versamento versamento = this.getVersamentoService().find(exp);
			return VersamentoConverter.toDTO(versamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera il versamento identificato dalla chiave logica
	 */
	public Versamento getVersamento(long idApplicazione, String codVersamentoEnte) throws NotFoundException, ServiceException {
		try {
			IdVersamento id = new IdVersamento();
			IdApplicazione idApplicazioneOrm = new IdApplicazione();
			idApplicazioneOrm.setId(idApplicazione);
			id.setIdApplicazione(idApplicazioneOrm);
			id.setCodVersamentoEnte(codVersamentoEnte);
			it.govpay.orm.Versamento versamento = this.getVersamentoService().get(id);
			return VersamentoConverter.toDTO(versamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}


	/**
	 * Recupera il versamento identificato dalla chiave logica
	 */
	public Versamento getVersamentoByBundlekey(long idApplicazione, String bundleKey, String codDominio, String codUnivocoDebitore) throws NotFoundException, ServiceException {
		try {
			IExpression exp = this.getVersamentoService().newExpression();
			exp.equals(it.govpay.orm.Versamento.model().COD_BUNDLEKEY, bundleKey);

			if(codUnivocoDebitore != null)
				exp.equals(it.govpay.orm.Versamento.model().DEBITORE_IDENTIFICATIVO, codUnivocoDebitore);

			if(codDominio != null) {
				exp.equals(it.govpay.orm.Versamento.model().ID_UO.ID_DOMINIO.COD_DOMINIO, codDominio);
				exp.and().isNotNull(it.govpay.orm.Versamento.model().ID_UO.COD_UO);
			}

			VersamentoFieldConverter fieldConverter = new VersamentoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", fieldConverter.toTable(it.govpay.orm.Versamento.model())), idApplicazione);

			it.govpay.orm.Versamento versamento =  this.getVersamentoService().find(exp);
			return VersamentoConverter.toDTO(versamento);
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
	 * Crea un nuovo versamento.
	 */
	public void insertVersamento(Versamento versamento) throws ServiceException{
		try {
			if(this.isAutoCommit())
				throw new ServiceException("L'operazione insertVersamento deve essere completata in transazione singola");

			String nextAvvisatura = this.getNextAvvisatura(versamento.getUo(this).getDominio(this).getCodDominio());
			log.info("CodAvvisatura:" + nextAvvisatura);
			versamento.setAvvisaturaCodAvvisatura(nextAvvisatura);

			it.govpay.orm.Versamento vo = VersamentoConverter.toVO(versamento);
			this.getVersamentoService().create(vo);
			versamento.setId(vo.getId());

			for(SingoloVersamento singoloVersamento: versamento.getSingoliVersamenti(this)) {
				singoloVersamento.setIdVersamento(vo.getId());
				it.govpay.orm.SingoloVersamento singoloVersamentoVo = SingoloVersamentoConverter.toVO(singoloVersamento);
				this.getSingoloVersamentoService().create(singoloVersamentoVo);
				singoloVersamento.setId(singoloVersamentoVo.getId());
			}
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}




	/**
	 * Aggiorna il versamento
	 */

	public void updateVersamento(Versamento versamento) throws ServiceException, NotFoundException {
		try {
			it.govpay.orm.Versamento vo = VersamentoConverter.toVO(versamento);
			IdVersamento idVersamento = this.getVersamentoService().convertToId(vo);
			this.getVersamentoService().update(idVersamento, vo);
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void updateVersamento(Versamento versamento, boolean deep) throws NotFoundException, ServiceException {
		try {
			if(deep && this.isAutoCommit())
				throw new ServiceException("L'operazione updateVersamento deve essere completata in transazione singola");

			this.updateVersamento(versamento);

			if(deep) {
				for(SingoloVersamento singolo: versamento.getSingoliVersamenti(this)) {
					it.govpay.orm.SingoloVersamento singoloVO = SingoloVersamentoConverter.toVO(singolo);
					IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
					idSingoloVersamento.setId(singolo.getId());
					this.getSingoloVersamentoService().update(idSingoloVersamento, singoloVO);
				}
			}
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} 
	}

	public void updateStatoVersamento(long idVersamento, StatoVersamento statoVersamento, String descrizioneStato) throws ServiceException {
		try {
			IdVersamento idVO = new IdVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().STATO_VERSAMENTO, statoVersamento.toString()));
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().DESCRIZIONE_STATO, descrizioneStato));

			this.getVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public VersamentoFilter newFilter() throws ServiceException {
		return new VersamentoFilter(this.getVersamentoService());
	}

	public VersamentoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new VersamentoFilter(this.getVersamentoService(),simpleSearch);
	}

	public long count(VersamentoFilter filter) throws ServiceException {
		try {
			return this.getVersamentoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<CountPerDominio> countGroupByIdDominio(VersamentoFilter filter) throws ServiceException {
		try {
			VersamentoFieldConverter converter = new VersamentoFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.Versamento.model()));
			FunctionField field = new FunctionField(cf, Function.COUNT, "cnt");
			List<CountPerDominio> countPerDominioLst = new ArrayList<>();
			IExpression expression = filter.toExpression();
			expression.addGroupBy(cf);
			try {
				List<Map<String,Object>> groupBy = this.getVersamentoService().groupBy(expression, field);
				for(Map<String,Object> cnt: groupBy) {
					CountPerDominio countPerDominio = new CountPerDominio();
					countPerDominio.setCount((Long) cnt.get("cnt")); 
					countPerDominio.setIdDominio((Long) cnt.get("id_dominio")); 
					countPerDominioLst.add(countPerDominio);
				}
			}catch(NotFoundException e) {}
			
			return countPerDominioLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public int updateConLimit(long idDominio, long idTracciato, long limit) throws ServiceException {
		try {
			List<Class<?>> lstReturnType = new ArrayList<>();
			lstReturnType.add(Long.class);
			String nativeUpdate = NativeQueries.getInstance().getUpdateVersamentiPerDominioConLimit();
			log.info("NATIVE: "+ nativeUpdate);
			
			Object[] fields = Arrays.asList(idTracciato, idDominio, true, ModoAvvisatura.ASICNRONA.getValue(), limit).toArray();
			return this.getVersamentoService().nativeUpdate(nativeUpdate, fields);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public List<Versamento> findAll(VersamentoFilter filter) throws ServiceException {
		try {
			List<Versamento> versamentoLst = new ArrayList<>();

//			if(filter.getIdDomini() != null && filter.getIdDomini().isEmpty()) return versamentoLst;

			List<it.govpay.orm.Versamento> versamentoVOLst = this.getVersamentoService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Versamento versamentoVO: versamentoVOLst) {
				versamentoLst.add(VersamentoConverter.toDTO(versamentoVO));
			}
			return versamentoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public SingoloVersamento getSingoloVersamento(long idSingoloVersamento) throws ServiceException {
		try {
			it.govpay.orm.SingoloVersamento singoloVersamentoVO = ((IDBSingoloVersamentoServiceSearch)this.getSingoloVersamentoService()).get(idSingoloVersamento);
			return SingoloVersamentoConverter.toDTO(singoloVersamentoVO);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<SingoloVersamento> getSingoliVersamenti(long idVersamento) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getSingoloVersamentoService().newPaginatedExpression();
			SingoloVersamentoFieldConverter fieldConverter = new SingoloVersamentoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_versamento", Long.class, "id_versamento", fieldConverter.toTable(it.govpay.orm.SingoloVersamento.model())), idVersamento);
			List<it.govpay.orm.SingoloVersamento> singoliVersamenti =  this.getSingoloVersamentoService().findAll(exp);
			return SingoloVersamentoConverter.toDTO(singoliVersamenti);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public void updateStatoSingoloVersamento(long idVersamento, StatoSingoloVersamento statoSingoloVersamento) throws ServiceException {
		try {
			IdSingoloVersamento idVO = new IdSingoloVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO, statoSingoloVersamento.toString()));

			this.getSingoloVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateVersamentoStatoAvvisatura(long idVersamento, boolean daAvvisare) throws ServiceException {
		try {
			IdVersamento idVO = new IdVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().AVVISATURA_DA_INVIARE, daAvvisare));

			this.getVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateVersamentoModalitaAvvisatura(long idVersamento, String modalitaAvvisatura) throws ServiceException {
		try {
			IdVersamento idVO = new IdVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().AVVISATURA_MODALITA, modalitaAvvisatura));

			this.getVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateVersamentoOperazioneAvvisatura(long idVersamento, String operazioneAvvisatura) throws ServiceException {
		try {
			IdVersamento idVO = new IdVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().AVVISATURA_OPERAZIONE, operazioneAvvisatura));

			this.getVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public void annullaVersamento(Versamento versamento, String descrizioneStato) throws VersamentoException,ServiceException{

		try {
			// Se è già annullato non devo far nulla.
			if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
				return;
			}

			// Se è in stato NON_ESEGUITO lo annullo
			if(versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
				versamento.setStatoVersamento(StatoVersamento.ANNULLATO);
				versamento.setDescrizioneStato(descrizioneStato); 
				this.updateVersamento(versamento);
				this.emitAudit(versamento);
				return;
			}
			// Se non è ne ANNULLATO ne NON_ESEGUITO non lo posso annullare
			throw new VersamentoException(versamento.getCodVersamentoEnte(),VersamentoException.VER_009);
		} catch (NotFoundException e) {
			// Versamento inesistente
			throw new VersamentoException(versamento.getCodVersamentoEnte(),VersamentoException.VER_008);
		} catch (Exception e) {
			this.rollback();
			if(e instanceof ServiceException)
				throw (ServiceException) e;
			else if(e instanceof VersamentoException)
				throw (VersamentoException) e;
			else 
				throw new ServiceException(e);
		}
	}
	
	
	public String getNextAvvisatura(String codDominio) throws ServiceException {
		InfoStatistics infoStat = null;
		BasicBD bd = null;
		try {
			infoStat = new InfoStatistics();
			org.openspcoop2.utils.id.serial.IDSerialGenerator serialGenerator = new org.openspcoop2.utils.id.serial.IDSerialGenerator(infoStat);
			org.openspcoop2.utils.id.serial.IDSerialGeneratorParameter params = new org.openspcoop2.utils.id.serial.IDSerialGeneratorParameter("GovPay");
			params.setTipo(IDSerialGeneratorType.NUMERIC);
			params.setWrap(false);
			params.setInformazioneAssociataAlProgressivo(codDominio+"_AVV"); // il progressivo sarà relativo a questa informazione

			java.sql.Connection con = null; 

			// Se sono in transazione aperta, utilizzo una connessione diversa perche' l'utility di generazione non supporta le transazioni.
			if(!this.isAutoCommit()) {
				bd = BasicBD.newInstance(this.getIdTransaction());
				con = bd.getConnection();
			} else {
				con = this.getConnection();
			}

			return ""+serialGenerator.buildIDAsNumber(params, con, this.getJdbcProperties().getDatabase(), log);
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
}
