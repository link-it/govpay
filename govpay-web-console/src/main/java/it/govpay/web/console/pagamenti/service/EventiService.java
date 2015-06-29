/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.web.console.pagamenti.service;

import it.govpay.ejb.ndp.ejb.EventoEJB;
import it.govpay.ejb.ndp.ejb.filter.EventoFilter;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Evento;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Infospcoop;
import it.govpay.web.console.pagamenti.bean.EventoBean;
import it.govpay.web.console.pagamenti.bean.InfospcoopBean;
import it.govpay.web.console.pagamenti.form.EventiSearchForm;
import it.govpay.web.console.pagamenti.iservice.IEventiService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.service.BaseService;

@Named("eventiService") @Singleton
public class EventiService extends BaseService<EventiSearchForm> implements IEventiService,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject  
	private transient Logger log;

	@Inject
	EventoEJB eventoEjb; 

	@PostConstruct
	private void init(){
		if(log != null)
			log.debug("Init EventiService completato."); 
	}

	@Override @Inject @Named("eventiSearchForm")
	public void setForm(EventiSearchForm form) {
		super.setForm(form);
		//		log.debug("Set Form...");
	}

	public List<EventoBean> findAll(int start, int limit)
			throws ServiceException {
		log.debug("Find All Eventi Start["+start+"] Limit["+limit+"]");
		List<EventoBean> list = new ArrayList<EventoBean>();

		try{

			List<Evento> eventi = this.eventoEjb.findAll(getFiltro(start, limit));
			if(eventi != null && eventi.size() > 0){
				for (Evento evento : eventi) {
					EventoBean bean = new EventoBean();
					bean.setDTO(evento);
					//					bean.setInfospcoop(evt.getInfospcoop());
					list.add(bean);
				}
			}

		}catch(Exception e){
			log.error(e, e);
		}

		return list;
	}

	public int totalCount() throws ServiceException {
		log.debug("Count numero eventi presenti");
		try{
			return this.eventoEjb.count(getFiltro(null, null));
		}catch(Exception e){
			log.error(e, e);
		}

		return 0;
	}

	public EventoBean findById(Long key) throws ServiceException {
		log.debug("Find Eventi by id long["+key+"]");



		try {

			EventiInterfacciaModel.Evento evento = this.eventoEjb.findById(key);

			EventoBean eBean = new EventoBean();

			if(evento != null){
				eBean.setDTO(evento);
			}

			return eBean;
		} catch (Exception e) {
			log.error(e, e);
			throw new ServiceException(e);
		}  
	}

	public List<EventoBean> findAll() throws ServiceException {
		log.debug("Find All Eventi!");
		List<EventoBean> list = new ArrayList<EventoBean>();

		try{
			List<Evento> eventi = this.eventoEjb.findAll(getFiltro(null,null));
			if(eventi != null && eventi.size() > 0){
				for (Evento evento : eventi) {
					EventoBean bean = new EventoBean();
					bean.setDTO(evento);

					list.add(bean);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}

		return list;
	}


	public List<String> getElencoSottoTipiEvento() {
		log.debug("Carico lista dei sotto tipi evento registrati.");
		List<String> list = new ArrayList<String>();

		try{
			//			IPaginatedExpression pagExpr = this.eventoDAO.newPaginatedExpression();
			//
			//			pagExpr.sortOrder(SortOrder.ASC);
			//			pagExpr.addGroupBy(Evento.model().SOTTO_TIPO_EVENTO).addOrder(Evento.model().SOTTO_TIPO_EVENTO);
			//			List<Object> lstRes = this.eventoDAO.select(pagExpr,true, Evento.model().SOTTO_TIPO_EVENTO);
			//
			//			if(lstRes != null){
			//				for (Object res : lstRes) {
			//					list.add((String) res);
			//				}
			//			}
		}catch(Exception e){
			log.error(e,e);
		}

		return list;
	}

	public List<String> getElencoTipiEvento() {
		log.debug("Carico lista dei tipi evento registrati.");
		List<String> list = new ArrayList<String>();

		try{
			//			IPaginatedExpression pagExpr = this.eventoDAO.newPaginatedExpression();
			//
			//			pagExpr.sortOrder(SortOrder.ASC).addOrder(Evento.model().TIPO_EVENTO);
			//			pagExpr.addGroupBy(Evento.model().TIPO_EVENTO);
			//			List<Object> lstRes = this.eventoDAO.select(pagExpr, true, Evento.model().TIPO_EVENTO);
			//
			//			if(lstRes != null){
			//				for (Object res : lstRes) {
			//					list.add((String) res);
			//				}
			//			}
		}catch(Exception e){
			log.error(e,e);
		}

		return list;
	}


	public InfospcoopBean getInfospcoopByIdEgov(String idEgov) {
		log.debug("Ricerca infospcoop idEgov["+idEgov+"]");
		if(idEgov == null)return null;

		try{

			Infospcoop dto = this.eventoEjb.findInfospcoopById(idEgov);

			if(dto!=null){
				InfospcoopBean bean = new InfospcoopBean();
				bean.setDTO(dto);
				return bean;
			}

		}catch(Exception e){
			log.error(e, e);
		}

		return null;
	}

	public void store(EventoBean obj) throws ServiceException {
		//donothing
	}

	public void deleteById(Long key) throws ServiceException {
		// donothing
	}

	public void delete(EventoBean obj) throws ServiceException {
		// donothing
	}

	/****
	 * 
	 * Converte l'input dell'utente nell'espressione da 
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws NotImplementedException
	 * @throws ExpressionNotImplementedException
	 * @throws ExpressionException
	 */
	private EventoFilter getFiltro(Integer start, Integer limit){ 

		EventoFilter filtro = new EventoFilter();

		filtro.setOffset(start);
		filtro.setLimit(limit);

		return filtro;
	}

	@Override
	public List<EventoBean> findAll(EventiSearchForm arg0)
			throws ServiceException {
		return null;
	}

	@Override
	public boolean exists(EventoBean arg0) throws ServiceException {
		return false;
	}
}
