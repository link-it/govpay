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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.exception.VersamentoException;
import it.govpay.bd.model.Allegato;
import it.govpay.bd.model.NotificaAppIo;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.converter.DocumentoConverter;
import it.govpay.bd.model.converter.NotificaAppIoConverter;
import it.govpay.bd.model.converter.PromemoriaConverter;
import it.govpay.bd.model.converter.SingoloVersamentoConverter;
import it.govpay.bd.model.converter.VersamentoConverter;
import it.govpay.bd.pagamento.filters.AllegatoFilter;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.pagamento.util.CountPerDominio;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.orm.Documento;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdDocumento;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.constants.StatoOperazioneType;
import it.govpay.orm.dao.IDBSingoloVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.JDBCVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.SingoloVersamentoFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;
import it.govpay.orm.model.VersamentoModel;

public class VersamentiBD extends BasicBD {

	public VersamentiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public VersamentiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public VersamentiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public VersamentiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public Versamento getVersamento(long id) throws ServiceException {
		return this.getVersamento(id, false);
	}
	/**
	 * Recupera il versamento identificato dalla chiave fisica
	 */
	public Versamento getVersamento(long id, boolean deep) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Versamento versamento = ((JDBCVersamentoServiceSearch) this.getVersamentoService()).get(id);
			Versamento dto = VersamentoConverter.toDTO(versamento);
			
			if(deep) {
				List<SingoloVersamento> singoliVersamenti = this._getSingoliVersamenti(dto.getId());
				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
					dto.addSingoloVersamento(singoloVersamento);
				}
			}
			
			return dto;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) { 
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public Versamento getVersamentoByDominioIuv(Long idDominio, String iuv) throws NotFoundException, ServiceException {
		return getVersamentoByDominioIuv(idDominio, iuv, false);
	}
	
	
	public Versamento getVersamentoByDominioIuv(Long idDominio, String iuv, boolean deep) throws NotFoundException, ServiceException {
		
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getVersamentoService().newExpression();
			
			VersamentoFieldConverter fieldConverter = new VersamentoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Versamento.model())), idDominio);
			exp.equals(it.govpay.orm.Versamento.model().IUV_VERSAMENTO, iuv);
			it.govpay.orm.Versamento versamento = this.getVersamentoService().find(exp);
			Versamento dto = VersamentoConverter.toDTO(versamento);
			
			if(deep) {
				List<SingoloVersamento> singoliVersamenti = this.getSingoliVersamenti(dto.getId());
				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
					dto.addSingoloVersamento(singoloVersamento);
				}
			}
			
			return dto;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Recupera il versamento identificato dalla chiave logica
	 */
	public Versamento getVersamento(long idApplicazione, String codVersamentoEnte) throws NotFoundException, ServiceException {
		return getVersamento(idApplicazione, codVersamentoEnte, false);	
	}
		
	/**
	 * Recupera il versamento identificato dalla chiave logica
	 */
	public Versamento getVersamento(long idApplicazione, String codVersamentoEnte, boolean deep) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getVersamentoService().newExpression();
			
			VersamentoFieldConverter fieldConverter = new VersamentoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", fieldConverter.toTable(it.govpay.orm.Versamento.model())), idApplicazione);
			exp.and();
			exp.equals(it.govpay.orm.Versamento.model().COD_VERSAMENTO_ENTE, codVersamentoEnte);
			
			it.govpay.orm.Versamento versamento = this.getVersamentoService().find(exp);
			Versamento dto = VersamentoConverter.toDTO(versamento);
			
			if(deep) {
				List<SingoloVersamento> singoliVersamenti = this._getSingoliVersamenti(dto.getId());
				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
					dto.addSingoloVersamento(singoloVersamento);
				}
			}
			
			return dto;
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	/**
	 * Recupera il versamento identificato dalla chiave logica
	 */
	public Versamento getVersamentoByBundlekey(long idApplicazione, String bundleKey, String codDominio, String codUnivocoDebitore) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
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
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Crea un nuovo versamento.
	 */
	public void insertVersamento(Versamento versamento) throws ServiceException {
		_insertVersamento(versamento, null, null);
	}
	
//	public void insertVersamento(Versamento versamento, Promemoria promemoria, NotificaAppIo notificaAppIo) throws ServiceException{
//		_insertVersamento(versamento, promemoria, notificaAppIo);
//	}

	private void _insertVersamento(Versamento versamento, Promemoria promemoria, NotificaAppIo notificaAppIo) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			if(this.isAutoCommit())
				throw new ServiceException("L'operazione insertVersamento deve essere completata in transazione singola");

			Long idDocumentoLong = null;
			Documento documento = null;
			if(versamento.getDocumento(this) != null) {
				IdDocumento idDocumento = new IdDocumento();
				idDocumento.setCodDocumento(versamento.getDocumento(this).getCodDocumento());
				IdApplicazione idApplicazione = new IdApplicazione();
				BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
				idApplicazione.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
				idApplicazione.setId(versamento.getApplicazione(configWrapper).getId());
				idDocumento.setIdApplicazione(idApplicazione);
				IdDominio idDominio = new IdDominio();
				idDominio.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
				idDominio.setId(versamento.getDominio(configWrapper).getId());
				idDocumento.setIdDominio(idDominio);
					
				
				try {
					boolean existsDocumento = this.getDocumentoService().exists(idDocumento);
					Documento documentoVo = DocumentoConverter.toVO(versamento.getDocumento(this));
					
					if(existsDocumento) { // update
						try {
							this.enableSelectForUpdate();
							documento = this.getDocumentoService().get(idDocumento);
							idDocumentoLong = documento.getId();
							
							this.getDocumentoService().update(idDocumento, documentoVo);
						} catch (NotFoundException e) {
							throw new ServiceException(e); // non dovrei entrare mai in questo punto perche' ho fatto la exists prima 
						} finally {
							this.disableSelectForUpdate();
						}
					} else { // create
						// Provo ad inserirlo, ma un thread concorrente potrebbe anticiparmi
						try {
							this.getDocumentoService().create(documentoVo);
							idDocumentoLong = documentoVo.getId();
						} catch (ServiceException se) {
							try {
								// Verifico che un concorrente non abbia inserito il documento
								this.enableSelectForUpdate();
								documento = this.getDocumentoService().get(idDocumento);
								this.getDocumentoService().update(idDocumento, documentoVo);
							} catch (NotFoundException nfe) {
								throw new ServiceException(se);
							} catch (Exception ie){
								throw new ServiceException(ie);
							} finally {
								this.disableSelectForUpdate();
							}
							
							idDocumentoLong = documento.getId();
						}
					}
					
				} catch (MultipleResultException e) {
					throw new ServiceException(e); // in realta' nell'implementazione non viene lanciata... 
				}
				
				versamento.setIdDocumento(idDocumentoLong);
				versamento.getDocumento(this).setId(idDocumentoLong);
			}
			

			it.govpay.orm.Versamento vo = VersamentoConverter.toVO(versamento);
			this.getVersamentoService().create(vo);
			versamento.setId(vo.getId());

			for(SingoloVersamento singoloVersamento: versamento.getSingoliVersamenti(this)) {
				singoloVersamento.setIdVersamento(vo.getId());
				it.govpay.orm.SingoloVersamento singoloVersamentoVo = SingoloVersamentoConverter.toVO(singoloVersamento);
				this.getSingoloVersamentoService().create(singoloVersamentoVo);
				singoloVersamento.setId(singoloVersamentoVo.getId());
			}
			
			// promemoria mail
			if(promemoria != null) {
				if(idDocumentoLong == null)
					promemoria.setIdVersamento(vo.getId());
				else 
					promemoria.setIdDocumento(idDocumentoLong);
				it.govpay.orm.Promemoria promemoriaVo = PromemoriaConverter.toVO(promemoria);
				this.getPromemoriaService().create(promemoriaVo);
				promemoria.setId(promemoriaVo.getId());
			}
			
			// notifica AppIO
			if(notificaAppIo != null) {
				notificaAppIo.setIdVersamento(vo.getId());
				it.govpay.orm.NotificaAppIO notificaAppIOVo = NotificaAppIoConverter.toVO(notificaAppIo);
				this.getNotificaAppIOService().create(notificaAppIOVo);
				notificaAppIo.setId(notificaAppIOVo.getId());
			}
			
			// allegati
			if(versamento.getAllegati() != null && versamento.getAllegati().size() > 0) {
				AllegatiBD allegatiBD  = new AllegatiBD(this);
				allegatiBD.setAtomica(false);
				
				for (Allegato allegato : versamento.getAllegati()) {
					allegato.setIdVersamento(vo.getId());
					allegatiBD.insertAllegato(allegato);
				}
			}
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}




	/**
	 * Aggiorna il versamento
	 */

	public void updateVersamento(Versamento versamento) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			this._updateVersamento(versamento);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updateVersamento(Versamento versamento, boolean deep) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			if(deep && this.isAutoCommit())
				throw new ServiceException("L'operazione updateVersamento deve essere completata in transazione singola");

			if(deep) {
				for(SingoloVersamento singolo: versamento.getSingoliVersamenti(this)) {
					it.govpay.orm.SingoloVersamento singoloVO = SingoloVersamentoConverter.toVO(singolo);
					IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
					idSingoloVersamento.setId(singolo.getId());
					this.getSingoloVersamentoService().update(idSingoloVersamento, singoloVO);
				}
				
				// aggiornamento documento
				Long idDocumentoLong = null;
				Documento documento = null;
				if(versamento.getDocumento(this) != null) {
					
					IdDocumento idDocumento = new IdDocumento();
					idDocumento.setCodDocumento(versamento.getDocumento(this).getCodDocumento());
					IdApplicazione idApplicazione = new IdApplicazione();
					BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
					idApplicazione.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
					idApplicazione.setId(versamento.getApplicazione(configWrapper).getId());
					idDocumento.setIdApplicazione(idApplicazione);
					IdDominio idDominio = new IdDominio();
					idDominio.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
					idDominio.setId(versamento.getDominio(configWrapper).getId());
					idDocumento.setIdDominio(idDominio );
					
					try {
						boolean existsDocumento = this.getDocumentoService().exists(idDocumento);
						Documento documentoVo = DocumentoConverter.toVO(versamento.getDocumento(this));
						
						if(existsDocumento) { // update
							try {
								this.enableSelectForUpdate();
								documento = this.getDocumentoService().get(idDocumento);
								idDocumentoLong = documento.getId();
								
								this.getDocumentoService().update(idDocumento, documentoVo);
							} catch (NotFoundException e) {
								throw new ServiceException(e); // non dovrei entrare mai in questo punto perche' ho fatto la exists prima 
							} finally {
								this.disableSelectForUpdate();
							}
						} else { // create
							// Provo ad inserirlo, ma un thread concorrente potrebbe anticiparmi
							try {
								this.getDocumentoService().create(documentoVo);
								idDocumentoLong = documentoVo.getId();
							} catch (ServiceException se) {
								try {
									// Verifico che un concorrente non abbia inserito il documento
									this.enableSelectForUpdate();
									documento = this.getDocumentoService().get(idDocumento);
									this.getDocumentoService().update(idDocumento, documentoVo);
								} catch (NotFoundException nfe) {
									throw new ServiceException(se);
								} catch (Exception ie){
									throw new ServiceException(ie);
								} finally {
									this.disableSelectForUpdate();
								}
								
								idDocumentoLong = documento.getId();
							}
						}
						
					} catch (MultipleResultException e) {
						throw new ServiceException(e); // in realta' nell'implementazione non viene lanciata... 
					}
					
					versamento.setIdDocumento(idDocumentoLong);
					versamento.getDocumento(this).setId(idDocumentoLong);
				}
				
				// aggiornamento allegati
				AllegatiBD allegatiBD = new AllegatiBD(this);
				allegatiBD.setAtomica(false);
				AllegatoFilter allegatoFilter = allegatiBD.newFilter();
				allegatoFilter.setIdVersamento(versamento.getId());
				
				long count = allegatiBD.count(allegatoFilter);
				
				if(count > 0) {
					// cancello le vecchi allegati
					List<Allegato> lst = allegatiBD.findAll(allegatoFilter); 
					for(Allegato allegato: lst) {
						allegatiBD.deleteAllegato(allegato);
					}
				}
				
				// allegati
				if(versamento.getAllegati() != null && versamento.getAllegati().size() > 0) {
					for (Allegato allegato : versamento.getAllegati()) {
						allegato.setIdVersamento(versamento.getId());
						allegatiBD.insertAllegato(allegato);
					}
				}
			}
			// spostato sotto perche' posso sostituire il documento
			this._updateVersamento(versamento);
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	
	public void _updateVersamento(Versamento versamento) throws ServiceException, NotFoundException {
		try {
			it.govpay.orm.Versamento vo = VersamentoConverter.toVO(versamento);
			IdVersamento idVersamento = this.getVersamentoService().convertToId(vo);
			this.getVersamentoService().update(idVersamento, vo);
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	public void updateStatoVersamento(Versamento versamento) throws ServiceException, NotFoundException {
		this._updateStatoVersamento(versamento.getId(), versamento.getStatoVersamento(), versamento.getDescrizioneStato());
	}
	

	public void updateStatoVersamento(long idVersamento, StatoVersamento statoVersamento, String descrizioneStato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			this._updateStatoVersamento(idVersamento, statoVersamento, descrizioneStato);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	private void _updateStatoVersamento(long idVersamento, StatoVersamento statoVersamento, String descrizioneStato) throws ServiceException, NotFoundException {
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
			throw e;
		}
	}
	
	public void updateStatoPromemoriaAvvisoVersamento(long idVersamento, boolean updateAvvisoNotificato, Boolean avvisoNotificato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdVersamento idVO = new IdVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(updateAvvisoNotificato)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().AVVISO_NOTIFICATO, avvisoNotificato));

			this.getVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateStatoPromemoriaScadenzaMailVersamento(long idVersamento, boolean updatePromemoriaScadenzaNotificato, Boolean promemoriaScadenzaNotificato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdVersamento idVO = new IdVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(updatePromemoriaScadenzaNotificato)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().AVV_MAIL_PROM_SCAD_NOTIFICATO, promemoriaScadenzaNotificato));

			this.getVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateStatoPromemoriaScadenzaAppIOVersamento(long idVersamento, boolean updatePromemoriaScadenzaNotificato, Boolean promemoriaScadenzaNotificato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdVersamento idVO = new IdVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(updatePromemoriaScadenzaNotificato)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().AVV_APP_IO_PROM_SCAD_NOTIFICATO, promemoriaScadenzaNotificato));

			this.getVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}


	public VersamentoFilter newFilter() throws ServiceException {
		return new VersamentoFilter(this.getVersamentoService());
	}

	public VersamentoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new VersamentoFilter(this.getVersamentoService(),simpleSearch);
	}
	
	public long count(VersamentoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(VersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVersamentoService());
			}
			
			return this.getVersamentoService().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(VersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			VersamentoFieldConverter converter = new VersamentoFieldConverter(this.getJdbcProperties().getDatabase());
			VersamentoModel model = it.govpay.orm.Versamento.model();
			/*
			SELECT count(distinct id) 
				FROM
				  (
				  SELECT versamenti.id
				  FROM versamenti
				  WHERE ...restrizioni di autorizzazione o ricerca...
				  ORDER BY data_richiesta 
				  LIMIT K
				  ) a
				);
			*/
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_VERSAMENTO_ENTE));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_VERSAMENTO_ENTE), "id");
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.DATA_CREAZIONE), "data_creazione");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_CREAZIONE, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getVersamentoService().nativeQuery(sql, returnTypes, parameters);
			
			Long count = 0L;
			for (List<Object> row : nativeQuery) {
				int pos = 0;
				count = BasicBD.getValueOrNull(row.get(pos++), Long.class);
			}
			
			return count.longValue();
		} catch (NotImplementedException | SQLQueryObjectException | ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<CountPerDominio> countGroupByIdDominio(VersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
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
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Versamento> findAll(VersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVersamentoService());
			}
			
			List<Versamento> versamentoLst = new ArrayList<>();

//			if(filter.getIdDomini() != null && filter.getIdDomini().isEmpty()) return versamentoLst;

			List<it.govpay.orm.Versamento> versamentoVOLst = this.getVersamentoService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Versamento versamentoVO: versamentoVOLst) {
				versamentoLst.add(VersamentoConverter.toDTO(versamentoVO));
			}
			return versamentoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public SingoloVersamento getSingoloVersamento(long idSingoloVersamento) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.SingoloVersamento singoloVersamentoVO = ((IDBSingoloVersamentoServiceSearch)this.getSingoloVersamentoService()).get(idSingoloVersamento);
			return SingoloVersamentoConverter.toDTO(singoloVersamentoVO);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<SingoloVersamento> getSingoliVersamenti(long idVersamento) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			return this._getSingoliVersamenti(idVersamento);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<SingoloVersamento> _getSingoliVersamenti(long idVersamento) throws ServiceException {
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
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdSingoloVersamento idVO = new IdSingoloVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO, statoSingoloVersamento.toString()));

			this.getSingoloVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateVersamentoInformazioniPagamento(Long idVersamento, Date dataPagamento, BigDecimal totalePagato, BigDecimal totaleIncassato, String iuvPagamento, StatoPagamento statoPagamento) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdVersamento idVO = new IdVersamento();
			idVO.setId(idVersamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(dataPagamento != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().DATA_PAGAMENTO, dataPagamento));
			if(totalePagato != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().IMPORTO_PAGATO, totalePagato.doubleValue()));
			if(totaleIncassato != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().IMPORTO_INCASSATO, totaleIncassato.doubleValue()));
			if(iuvPagamento != null) {
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().IUV_PAGAMENTO, iuvPagamento));
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().SRC_IUV, iuvPagamento.toUpperCase()));
			}
			if(statoPagamento != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().STATO_PAGAMENTO, statoPagamento.toString()));

			this.getVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void aggiornaIncassoVersamento(Pagamento pagamento) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			Versamento versamento = pagamento.getSingoloVersamento(this).getVersamentoBD(this);
			BigDecimal importoIncassato = versamento.getImportoIncassato() != null ? versamento.getImportoIncassato() : BigDecimal.ZERO;
			if(pagamento.getImportoPagato() != null)
				importoIncassato = importoIncassato.add(pagamento.getImportoPagato());
			
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			int countTotali = 0;
			int countIncassati = 0;
			//controllo se sono tutti incassati per aggiornare lo stato
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(this);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				List<Pagamento> pagamenti = singoloVersamento.getPagamenti(this);
				for (Pagamento pagamento2 : pagamenti) {
					if(pagamento2.getStato().equals(Stato.INCASSATO))
						countIncassati ++;
				}
				countTotali ++;
			}

			if(countIncassati == countTotali)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().STATO_PAGAMENTO, StatoPagamento.INCASSATO.toString()));
			
			IdVersamento idVO = new IdVersamento();
			idVO.setId(versamento.getId());
			
			
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Versamento.model().IMPORTO_INCASSATO, importoIncassato.doubleValue()));
			this.getVersamentoService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
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
				this.updateStatoVersamento(versamento);
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

	public List<Versamento> findVersamentiConAvvisoDiPagamentoDaSpedire(Integer offset, Integer limit) throws ServiceException {
		List<Versamento> ret = new ArrayList<Versamento>();
		
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			//Notifica Avviso: tutte le pendenze con dataNotificaAvviso != null, dataNotificaAvviso <= oggi, isAvvisoNotificato == false, stato = DA_PAGARE
			IExpression exp = this.getVersamentoService().newExpression();
			exp.equals(it.govpay.orm.Versamento.model().AVVISO_NOTIFICATO, false);
			exp.and().isNotNull(it.govpay.orm.Versamento.model().DATA_NOTIFICA_AVVISO).and()
				.lessEquals(it.govpay.orm.Versamento.model().DATA_NOTIFICA_AVVISO, new Date());
			exp.and().equals(it.govpay.orm.Versamento.model().STATO_VERSAMENTO, StatoVersamento.NON_ESEGUITO.toString());
			
			IPaginatedExpression pagExpr = this.getVersamentoService().toPaginatedExpression(exp);
			if(offset != null)
				pagExpr.offset(offset);
			
			if(limit != null)
				pagExpr.limit(limit);
			
			pagExpr.addOrder(it.govpay.orm.Versamento.model().DATA_CREAZIONE, SortOrder.DESC);

			List<it.govpay.orm.Versamento> versamentiVO = this.getVersamentoService().findAll(pagExpr);
			ret = VersamentoConverter.toDTOList(versamentiVO);
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		return ret;
	}

	public List<Versamento> findVersamentiConAvvisoDiScadenzaDaSpedireViaMail(Integer offset, Integer limit) throws ServiceException{
		List<Versamento> ret = new ArrayList<Versamento>();
		
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			//Notifica Promemoria: tutte le pendenze con dataNotificaPromemoria != null, dataNotificaPromemoria <= oggi, isPromemoriaNotificato == false, stato = DA_PAGARE
			IExpression exp = this.getVersamentoService().newExpression();
			exp.equals(it.govpay.orm.Versamento.model().AVV_MAIL_PROM_SCAD_NOTIFICATO, false);
			exp.and().isNotNull(it.govpay.orm.Versamento.model().AVV_MAIL_DATA_PROM_SCADENZA).and()
				.lessEquals(it.govpay.orm.Versamento.model().AVV_MAIL_DATA_PROM_SCADENZA, new Date());
			exp.and().equals(it.govpay.orm.Versamento.model().STATO_VERSAMENTO, StatoVersamento.NON_ESEGUITO.toString());
			
			IPaginatedExpression pagExpr = this.getVersamentoService().toPaginatedExpression(exp);
			if(offset != null)
				pagExpr.offset(offset);
			
			if(limit != null)
				pagExpr.limit(limit);
			
			pagExpr.addOrder(it.govpay.orm.Versamento.model().DATA_CREAZIONE, SortOrder.DESC);

			List<it.govpay.orm.Versamento> versamentiVO = this.getVersamentoService().findAll(pagExpr);
			ret = VersamentoConverter.toDTOList(versamentiVO);
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		return ret;
	}

	public List<Versamento> findVersamentiConAvvisoDiScadenzaDaSpedireViaAppIO(Integer offset, Integer limit) throws ServiceException{
		List<Versamento> ret = new ArrayList<Versamento>();
		
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			//Notifica Promemoria: tutte le pendenze con dataNotificaPromemoria != null, dataNotificaPromemoria <= oggi, isPromemoriaNotificato == false, stato = DA_PAGARE
			IExpression exp = this.getVersamentoService().newExpression();
			exp.equals(it.govpay.orm.Versamento.model().AVV_APP_IO_PROM_SCAD_NOTIFICATO, false);
			exp.and().isNotNull(it.govpay.orm.Versamento.model().AVV_APP_IO_DATA_PROM_SCADENZA).and()
				.lessEquals(it.govpay.orm.Versamento.model().AVV_APP_IO_DATA_PROM_SCADENZA, new Date());
			exp.and().equals(it.govpay.orm.Versamento.model().STATO_VERSAMENTO, StatoVersamento.NON_ESEGUITO.toString());
			
			IPaginatedExpression pagExpr = this.getVersamentoService().toPaginatedExpression(exp);
			if(offset != null)
				pagExpr.offset(offset);
			
			if(limit != null)
				pagExpr.limit(limit);
			
			pagExpr.addOrder(it.govpay.orm.Versamento.model().DATA_CREAZIONE, SortOrder.DESC);

			List<it.govpay.orm.Versamento> versamentiVO = this.getVersamentoService().findAll(pagExpr);
			ret = VersamentoConverter.toDTOList(versamentiVO);
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		return ret;
	}
	
	public List<Versamento> findVersamentiDiUnTracciato(Long idTracciato, Integer offset, Integer limit) throws ServiceException{
		List<Versamento> ret = new ArrayList<Versamento>();
		
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			VersamentoFieldConverter converter = new VersamentoFieldConverter(this.getJdbcProperties().getDatabase());
			VersamentoModel model = it.govpay.orm.Versamento.model();
			IExpression exp = this.getVersamentoService().newExpression();
//			exp.and().isNotNull(model.NUMERO_AVVISO);
//			exp.and().equals(model.STATO_VERSAMENTO, StatoVersamento.NON_ESEGUITO.toString());
			exp.and().equals(model.ID_OPERAZIONE.STATO, StatoOperazioneType.ESEGUITO_OK.toString());
			exp.and().equals(new CustomField("id_tracciato", Long.class, "id_tracciato", converter.toTable(model.ID_OPERAZIONE)), idTracciato);
			
			IPaginatedExpression pagExpr = this.getVersamentoService().toPaginatedExpression(exp);
			if(offset != null)
				pagExpr.offset(offset);
			
			if(limit != null)
				pagExpr.limit(limit);
			
			pagExpr.addOrder(it.govpay.orm.Versamento.model().NUMERO_AVVISO, SortOrder.ASC);

			List<it.govpay.orm.Versamento> versamentiVO = this.getVersamentoService().findAll(pagExpr);
			ret = VersamentoConverter.toDTOList(versamentiVO);
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		return ret;
	}
}
