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
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.converter.AnagraficaConverter;
import it.govpay.bd.model.converter.IuvConverter;
import it.govpay.bd.model.converter.SingoloVersamentoConverter;
import it.govpay.bd.model.converter.VersamentoConverter;
import it.govpay.bd.pagamento.util.IuvUtils;
import it.govpay.orm.IUV;
import it.govpay.orm.IdAnagrafica;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.dao.IDBVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.JDBCSingoloVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.JDBCVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.SingoloVersamentoFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.beans.CustomField;
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

public class VersamentiBD extends BasicBD {


	private Logger log = Logger.getLogger(VersamentiBD.class);

	public enum TipoIUV {
		ISO11694, NUMERICO
	}

	public VersamentiBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera il versamento identificato dalla chiave fisica
	 * 
	 * @param idTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Versamento getVersamento(long idVersamento) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			return getVersamentoDTO(((JDBCVersamentoServiceSearch)this.getServiceManager().getVersamentoServiceSearch()).get(idVersamento));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}

	private Versamento getVersamentoDTO(IdVersamento id)
			throws ServiceException, NotFoundException,
			MultipleResultException, NotImplementedException,
			ExpressionNotImplementedException, ExpressionException {
		return getVersamentoDTO(this.getServiceManager().getVersamentoServiceSearch().get(id));
	}

	private Versamento getVersamentoDTO(it.govpay.orm.Versamento versamentoVO)
			throws ServiceException, NotFoundException,
			MultipleResultException, NotImplementedException,
			ExpressionNotImplementedException, ExpressionException {
		Versamento versamento = VersamentoConverter.toDTO(versamentoVO);
		IdAnagrafica idanag = new IdAnagrafica();
		idanag.setId(versamentoVO.getIdAnagraficaDebitore().getId());
		versamento.setAnagraficaDebitore(AnagraficaConverter.toDTO(this.getServiceManager().getAnagraficaServiceSearch().get(idanag)));

		IPaginatedExpression exp = this.getServiceManager().getSingoloVersamentoServiceSearch().newPaginatedExpression();

		SingoloVersamentoFieldConverter fieldConverter = new SingoloVersamentoFieldConverter(this.getServiceManager().getJdbcProperties().getDatabaseType());
		exp.equals(new CustomField("id_versamento", Long.class, "id_versamento", fieldConverter.toTable(it.govpay.orm.SingoloVersamento.model())), versamento.getId());

		List<it.govpay.orm.SingoloVersamento> singoloLst =  this.getServiceManager().getSingoloVersamentoServiceSearch().findAll(exp);

		List<SingoloVersamento> singoliVersamenti = new ArrayList<SingoloVersamento>();

		for(it.govpay.orm.SingoloVersamento singoloVersamento: singoloLst) {
			SingoloVersamento singoloV = SingoloVersamentoConverter.toDTO(singoloVersamento);
			singoloV.setVersamento(versamento);
			singoliVersamenti.add(singoloV);
		}

		versamento.setSingoliVersamenti(singoliVersamenti);
		return versamento;
	}

	/**
	 * Recupera il versamento identificato dalla chiave logica
	 * 
	 * @param idApplicazione
	 * @param codTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Versamento getVersamento(String codDominio, String iuv) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IdVersamento id = new IdVersamento();
			id.setCodDominio(codDominio);
			id.setCodVersamentoEnte(iuv);

			return getVersamentoDTO(id);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera il versamento identificato da idEnte e IUV
	 * 
	 */
	public Versamento getVersamento(long idApplicazione, String codVersamentoEnte) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression exp = this.getServiceManager().getVersamentoServiceSearch().newExpression();
			exp.equals(it.govpay.orm.Versamento.model().COD_VERSAMENTO_ENTE, codVersamentoEnte);

			VersamentoFieldConverter fieldConverter = new VersamentoFieldConverter(this.getServiceManager().getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", fieldConverter.toTable(it.govpay.orm.Versamento.model())), idApplicazione);
			it.govpay.orm.Versamento versamentoVO = this.getServiceManager().getVersamentoServiceSearch().find(exp);

			return getVersamentoDTO(versamentoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera i versamenti per stato
	 * @param statoVersamento
	 * @return
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public List<Versamento> getVersamenti(int offset, int limit) throws NotFoundException, ServiceException {
		try {
			IPaginatedExpression exp = this.getServiceManager().getVersamentoServiceSearch().newPaginatedExpression();
			exp.offset(offset);
			exp.limit(limit);
			List<it.govpay.orm.Versamento> versamentoVOLst = this.getServiceManager().getVersamentoServiceSearch().findAll(exp);

			List<Versamento> versamentoLst = new ArrayList<Versamento>();
			for(it.govpay.orm.Versamento versamentoVO: versamentoVOLst) {
				versamentoLst.add(getVersamentoDTO(versamentoVO));
			}

			return versamentoLst;
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
	 * Crea un nuovo tributo.
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertVersamento(Versamento versamento) throws ServiceException{
		try {

			it.govpay.orm.Anagrafica voAnagrafica = AnagraficaConverter.toVO(versamento.getAnagraficaDebitore());

			this.getServiceManager().getAnagraficaService().create(voAnagrafica);

			it.govpay.orm.Versamento vo = VersamentoConverter.toVO(versamento);

			IdAnagrafica idAnagrafica = new IdAnagrafica();
			idAnagrafica.setId(voAnagrafica.getId());
			vo.setIdAnagraficaDebitore(idAnagrafica);

			vo.setDataOraUltimoAggiornamento(new Date());
			this.getServiceManager().getVersamentoService().create(vo);
			versamento.setId(vo.getId());


			for(SingoloVersamento singolo: versamento.getSingoliVersamenti()) {

				it.govpay.orm.SingoloVersamento voSingolo = SingoloVersamentoConverter.toVO(singolo);
				IdVersamento idVersamento = new IdVersamento();
				idVersamento.setId(vo.getId());
				voSingolo.setIdVersamento(idVersamento);

				this.getServiceManager().getSingoloVersamentoService().create(voSingolo);
				singolo.setId(voSingolo.getId());

			}

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Effettua la replace del versamento con i nuovi dati forniti.
	 * 
	 * @param versamento
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void replaceVersamento(Versamento versamento) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.Versamento vo = VersamentoConverter.toVO(versamento);
			IdVersamento idVersamento = this.getServiceManager().getVersamentoServiceSearch().convertToId(vo);
			if(!this.getServiceManager().getVersamentoServiceSearch().exists(idVersamento)) {
				throw new NotFoundException("Versamento con id ["+idVersamento.toJson()+"] non trovato");
			}

			it.govpay.orm.Versamento versamentoDB = this.getServiceManager().getVersamentoServiceSearch().get(idVersamento);
			versamento.getAnagraficaDebitore().setId(versamentoDB.getIdAnagraficaDebitore().getId());

			it.govpay.orm.Anagrafica voAnagrafica = AnagraficaConverter.toVO(versamento.getAnagraficaDebitore());
			IdAnagrafica idAnagrafica = this.getServiceManager().getAnagraficaServiceSearch().convertToId(voAnagrafica);

			if(!this.getServiceManager().getAnagraficaServiceSearch().exists(idAnagrafica)) {
				throw new NotFoundException("Anagrafica con id ["+idAnagrafica.toJson()+"] non trovata");
			}

			this.getServiceManager().getAnagraficaService().update(idAnagrafica, voAnagrafica);

			idAnagrafica.setId(voAnagrafica.getId());
			vo.setIdAnagraficaDebitore(idAnagrafica);

			vo.setDataOraUltimoAggiornamento(new Date());
			this.getServiceManager().getVersamentoService().update(idVersamento, vo);
			versamento.setId(vo.getId());


			IExpression exp = this.getServiceManager().getSingoloVersamentoService().newExpression();
			SingoloVersamentoFieldConverter fieldConverter = new SingoloVersamentoFieldConverter(this.getServiceManager().getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_versamento", Long.class, "id_versamento", fieldConverter.toTable(it.govpay.orm.SingoloVersamento.model())), vo.getId());

			this.getServiceManager().getSingoloVersamentoService().deleteAll(exp);

			for(SingoloVersamento singolo: versamento.getSingoliVersamenti()) {
				it.govpay.orm.SingoloVersamento singoloVO = SingoloVersamentoConverter.toVO(singolo);
				singoloVO.setIdVersamento(this.getServiceManager().getVersamentoServiceSearch().convertToId(vo));

				this.getServiceManager().getSingoloVersamentoService().create(singoloVO);
			}

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Effettua l'aggiornamento del versamento con i nuovi dati forniti.
	 * 
	 * @param versamento
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateVersamento(Versamento versamento) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.Versamento vo = VersamentoConverter.toVO(versamento);
			IdVersamento idVersamento = this.getServiceManager().getVersamentoServiceSearch().convertToId(vo);
			if(!this.getServiceManager().getVersamentoServiceSearch().exists(idVersamento)) {
				throw new NotFoundException("Versamento con id ["+idVersamento.toJson()+"] non trovato");
			}

			it.govpay.orm.Versamento versamentoDB = this.getServiceManager().getVersamentoServiceSearch().get(idVersamento);
			versamento.getAnagraficaDebitore().setId(versamentoDB.getIdAnagraficaDebitore().getId());

			it.govpay.orm.Anagrafica voAnagrafica = AnagraficaConverter.toVO(versamento.getAnagraficaDebitore());
			IdAnagrafica idAnagrafica = this.getServiceManager().getAnagraficaServiceSearch().convertToId(voAnagrafica);

			if(!this.getServiceManager().getAnagraficaServiceSearch().exists(idAnagrafica)) {
				throw new NotFoundException("Anagrafica con id ["+idAnagrafica.toJson()+"] non trovata");
			}

			this.getServiceManager().getAnagraficaService().update(idAnagrafica, voAnagrafica);

			idAnagrafica.setId(voAnagrafica.getId());
			vo.setIdAnagraficaDebitore(idAnagrafica);

			vo.setDataOraUltimoAggiornamento(new Date());
			this.getServiceManager().getVersamentoService().update(idVersamento, vo);
			versamento.setId(vo.getId());

			for(SingoloVersamento singolo: versamento.getSingoliVersamenti()) {
				it.govpay.orm.SingoloVersamento singoloVO = SingoloVersamentoConverter.toVO(singolo);
				singoloVO.setIdVersamento(this.getServiceManager().getVersamentoServiceSearch().convertToId(vo));

				IdSingoloVersamento idVO = new IdSingoloVersamento();
				idVO.setIdVersamento(singoloVO.getIdVersamento());
				idVO.setIndice(singoloVO.getIndice());
				this.getServiceManager().getSingoloVersamentoService().update(idVO, singoloVO);
			}

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} 
	}

	/**
	 * Aggiorna lo stato di un versamento
	 * 
	 * @param idVersamento
	 * @param stato
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateStatoVersamento(long idVersamento, Versamento.StatoVersamento stato) throws NotFoundException, ServiceException {
		try {
			if(!((IDBVersamentoServiceSearch)this.getServiceManager().getVersamentoServiceSearch()).exists(idVersamento)) {
				throw new NotFoundException("Versamento con id ["+idVersamento+"] non trovato, impossibile aggiornarne lo stato");
			}

			UpdateField statoUpdateField = new UpdateField(it.govpay.orm.Versamento.model().STATO_VERSAMENTO, stato.toString());
			IdVersamento id = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findId(idVersamento, true);
			this.getServiceManager().getVersamentoService().updateFields(id, statoUpdateField);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateStatoVersamento(long idVersamento, Versamento.StatoRendicontazione stato) throws NotFoundException, ServiceException {
		try {
			if(!((IDBVersamentoServiceSearch)this.getServiceManager().getVersamentoServiceSearch()).exists(idVersamento)) {
				throw new NotFoundException("Versamento con id ["+idVersamento+"] non trovato, impossibile aggiornarne lo stato");
			}

			UpdateField statoUpdateField = new UpdateField(it.govpay.orm.Versamento.model().STATO_RENDICONTAZIONE, stato.toString());
			IdVersamento id = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findId(idVersamento, true);
			this.getServiceManager().getVersamentoService().updateFields(id, statoUpdateField);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	public void updateSingoloVersamentoRPT(long idSingoloVersamento, String ibanAccredito, String causaleVersamento, String datiSpecificiRiscossione) throws ServiceException, NotFoundException {
		try {

			IdSingoloVersamento id = null;
			try{
				id = ((JDBCSingoloVersamentoServiceSearch)this.getServiceManager().getSingoloVersamentoServiceSearch()).findId(idSingoloVersamento, true);
			} catch(NotFoundException e) {
				throw new NotFoundException("SingoloVersamento con id ["+idSingoloVersamento+"] non trovato, impossibile aggiornarlo");
			}

			UpdateField ibanUpdateField = new UpdateField(it.govpay.orm.SingoloVersamento.model().IBAN_ACCREDITO, ibanAccredito);
			UpdateField causaleUpdateField = new UpdateField(it.govpay.orm.SingoloVersamento.model().CAUSALE_VERSAMENTO, causaleVersamento);
			UpdateField datiSpecificiUpdateField = new UpdateField(it.govpay.orm.SingoloVersamento.model().DATI_SPECIFICI_RISCOSSIONE, datiSpecificiRiscossione);


			this.getServiceManager().getSingoloVersamentoService().updateFields(id, ibanUpdateField, causaleUpdateField, datiSpecificiUpdateField);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void updateSingoloVersamentoRT(long idSingoloVersamento, String esito, Date dataEsito, String iur, StatoSingoloVersamento statoSingoloVersamento) throws ServiceException, NotFoundException {
		try {

			IdSingoloVersamento id = null;
			try{
				id = ((JDBCSingoloVersamentoServiceSearch)this.getServiceManager().getSingoloVersamentoServiceSearch()).findId(idSingoloVersamento, true);
			} catch(NotFoundException e) {
				throw new NotFoundException("SingoloVersamento con id ["+idSingoloVersamento+"] non trovato, impossibile aggiornarlo");
			}

			UpdateField ibanUpdateField = new UpdateField(it.govpay.orm.SingoloVersamento.model().ESITO_SINGOLO_PAGAMENTO, esito);
			UpdateField causaleUpdateField = new UpdateField(it.govpay.orm.SingoloVersamento.model().DATA_ESITO_SINGOLO_PAGAMENTO, dataEsito);
			UpdateField datiSpecificiUpdateField = new UpdateField(it.govpay.orm.SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO, statoSingoloVersamento.name());

			this.getServiceManager().getSingoloVersamentoService().updateFields(id, ibanUpdateField, causaleUpdateField, datiSpecificiUpdateField);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateStatoSingoloVersamento(long idSingoloVersamento, StatoSingoloVersamento statoSingoloVersamento) throws ServiceException, NotFoundException {
		try {

			IdSingoloVersamento id = null;
			try{
				id = ((JDBCSingoloVersamentoServiceSearch)this.getServiceManager().getSingoloVersamentoServiceSearch()).findId(idSingoloVersamento, true);
			} catch(NotFoundException e) {
				throw new NotFoundException("SingoloVersamento con id ["+idSingoloVersamento+"] non trovato, impossibile aggiornarlo");
			}

			UpdateField datiSpecificiUpdateField = new UpdateField(it.govpay.orm.SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO, statoSingoloVersamento.name());

			this.getServiceManager().getSingoloVersamentoService().updateFields(id, datiSpecificiUpdateField);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public Iuv generaIuv(long idApplicazione, int auxDigit, int applicationCode, String codDominio, TipoIUV type) throws ServiceException, NotFoundException {
		long prg = getNextPrgIuv(codDominio, type);

		String reference = null;
		String iuv = null;
		switch (type) {
		case ISO11694:
			reference = String.format("%015d", prg);
			String check = IuvUtils.getCheckDigit(reference);
			iuv = "RF" + check + reference;
			break;
		case NUMERICO:
			reference = String.format("%013d", prg);
			long resto93 = (Long.parseLong(String.valueOf(auxDigit) + String.format("%02d", applicationCode) + reference)) % 93;
			iuv = reference + String.valueOf(resto93);
			break;
		}

		return creaIuv(codDominio, prg, iuv, idApplicazione);
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
		try {
			infoStat = new InfoStatistics();
			org.openspcoop2.utils.id.serial.IDSerialGenerator serialGenerator = new org.openspcoop2.utils.id.serial.IDSerialGenerator(infoStat);
			org.openspcoop2.utils.id.serial.IDSerialGeneratorParameter params = new org.openspcoop2.utils.id.serial.IDSerialGeneratorParameter("GovPay");
			params.setTipo(IDSerialGeneratorType.NUMERIC);
			params.setWrap(false);
			params.setInformazioneAssociataAlProgressivo(codDominio+type.toString()); // il progressivo sarà relativo a questa informazione
			java.sql.Connection con = this.getConnection();
			return serialGenerator.buildIDAsNumber(params, con, this.getServiceManager().getJdbcProperties().getDatabase(), log);
		} catch (UtilsException e) {
			log.error("Numero di errori 'access serializable': "+infoStat.getErrorSerializableAccess());
			for (int i=0; i<infoStat.getExceptionOccurs().size(); i++) {
				Throwable t = infoStat.getExceptionOccurs().get(i);
				log.error("Errore-"+(i+1)+" (occurs:"+infoStat.getNumber(t)+"): "+t.getMessage());
			}
			throw new ServiceException(e);
		}
	}

	/**
	 * Aggiorna lo IUV con il codIuv generato
	 * @param iuv
	 * @return
	 * @throws ServiceException
	 */
	private Iuv creaIuv(String codDominio, long prg, String iuv, long idApplicazione) throws ServiceException, NotFoundException {

		try {
			Iuv iuvDTO = new Iuv();
			iuvDTO.setCodDominio(codDominio);
			iuvDTO.setPrg(prg);
			iuvDTO.setIuv(iuv);
			iuvDTO.setDataGenerazione(new Date());
			iuvDTO.setIdApplicazione(idApplicazione);

			IUV iuvVO = IuvConverter.toVO(iuvDTO);
			this.getServiceManager().getIUVService().create(iuvVO);
			iuvDTO.setId(iuvVO.getId());
			return this.getIuv(codDominio, iuv);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Recupera lo IUV con la chiave logi generato
	 * @param iuv
	 * @return
	 * @throws ServiceException
	 */
	public Iuv getIuv(String codDominio, String iuv) throws ServiceException, MultipleResultException, NotFoundException {
		try {
			IExpression exp = this.getServiceManager().getIUVServiceSearch().newExpression();
			exp.equals(it.govpay.orm.IUV.model().IUV, iuv);
			exp.equals(it.govpay.orm.IUV.model().COD_DOMINIO, codDominio);
			it.govpay.orm.IUV iuvVO = this.getServiceManager().getIUVServiceSearch().find(exp);

			Iuv versamento = IuvConverter.toDTO(iuvVO);

			return versamento;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}


	public VersamentoFilter newFilter() throws ServiceException {
		try {
			return new VersamentoFilter(this.getServiceManager().getVersamentoServiceSearch());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(VersamentoFilter filter) throws ServiceException {
		try {
			return this.getServiceManager().getVersamentoServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Versamento> findAll(VersamentoFilter filter) throws ServiceException {
		try {
			List<Versamento> versamentoLst = new ArrayList<Versamento>();
			List<it.govpay.orm.Versamento> versamentoVOLst = this.getServiceManager().getVersamentoServiceSearch().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Versamento versamentoVO: versamentoVOLst) {
				versamentoLst.add(getVersamentoDTO(versamentoVO));
			}
			return versamentoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

}
