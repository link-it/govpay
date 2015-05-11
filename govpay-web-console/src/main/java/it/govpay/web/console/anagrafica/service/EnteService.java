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
package it.govpay.web.console.anagrafica.service;

import it.govpay.ejb.controller.AnagraficaEJB;
import it.govpay.ejb.filter.EnteCreditoreFilter;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.TributoModel;
import it.govpay.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ndp.model.DominioEnteModel;
import it.govpay.web.console.anagrafica.bean.EnteBean;
import it.govpay.web.console.anagrafica.bean.TributoBean;
import it.govpay.web.console.anagrafica.form.EnteSearchForm;
import it.govpay.web.console.anagrafica.iservice.IEnteService;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.service.BaseService;


@Singleton @Named("enteService")
public class EnteService extends BaseService<EnteSearchForm> implements IEnteService,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject  
	private transient Logger log;

	@Inject
	AnagraficaEJB anagraficaEjb;

	@Inject
	AnagraficaDominioEJB anagraficaDominioEbj;

	@Override @Inject @Named("enteSearchForm")
	public void setForm(EnteSearchForm form) {
		super.setForm(form);
	}

	@Override
	public List<EnteBean> findAll(int start, int limit) throws ServiceException {
		List<EnteBean> lst = new ArrayList<EnteBean>();

		try{
			log.debug("findAll Enti Offset["+start+"] Limit["+limit+"] in corso...");
			EnteCreditoreFilter filtro = getFiltro(start, limit,this.form);
			lst = _findAll(filtro);
			log.debug("findAll Enti Offset["+start+"] Limit["+limit+"] completata.");

		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAll Enti Offset["+start+"] Limit["+limit+"]: "+ e.getMessage(), e);
		}


		return lst;
	}

	@Override
	public int totalCount() throws ServiceException {
		try{
			log.debug("Count Enti in corso...");
			EnteCreditoreFilter filtro = getFiltro(null, null,this.form);
			int cnt = this.anagraficaEjb.countEntiCreditori(filtro);
			log.debug("Count Enti completata trovati["+cnt+"].");
			return cnt;
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la count degli enti: "+ e.getMessage(), e);
		}

		return 0;
	}

	@Override
	public void store(EnteBean obj) throws ServiceException {
		try{
			log.debug("Salvataggio Ente Creditore ["+obj.getDenominazione().getValue()+"] in corso...");

			String idEnteCreditore = obj.getDTO().getIdEnteCreditore(); 
			this.anagraficaEjb.salvaEnteCreditore(idEnteCreditore , obj.getDTO(), Utils.getLoggedUtente().getIdOperatore());

			log.debug("Salvataggio Ente Creditore ["+obj.getDenominazione().getValue()+"] completato.");
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio dell'ente ["+obj.getDenominazione().getValue()+"]: "+ e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@Override
	public void store(String key, EnteBean obj,DominioEnteModel dominioEnte) throws ServiceException {
		try{
			log.debug("Salvataggio Ente Creditore ["+obj.getDenominazione().getValue()+"] in corso...");
			this.anagraficaEjb.salvaEnteCreditore(key , obj.getDTO(), Utils.getLoggedUtente().getIdOperatore());
			log.debug("Salvataggio Ente Creditore ["+obj.getDenominazione().getValue()+"] completato.");

			if(dominioEnte != null){
				log.debug("Salvataggio Dominio Ente Creditore ["+obj.getDenominazione().getValue()+"] in corso...");
				dominioEnte.setEnteCreditore(obj.getDTO());
				this.anagraficaDominioEbj.salvaDominioEnte(dominioEnte.getIdDominio(), dominioEnte);
				log.debug("Salvataggio Dominio Ente Creditore ["+obj.getDenominazione().getValue()+"] completato.");
			}
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio dell'ente ["+obj.getDenominazione().getValue()+"]: "+ e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteById(Long key) throws ServiceException {
	}

	@Override
	public void delete(EnteBean obj) throws ServiceException {
	}

	@Override
	public EnteBean findById(Long key) throws ServiceException {
		//		try{
		//			EnteCreditoreModel ente = this.anagraficaEjb.getCreditoreByIdLogico(key);
		//
		//			if(ente != null){
		//				EnteBean bean = new EnteBean();
		//				bean.setId(0L);
		//				bean.setDTO(ente);
		//
		//				return bean;
		//			}
		//		}catch(Exception e){
		//			this.log.error("Si e' verificato un errore durante la find dell'ente ["+key+"]: " +e.getMessage(), e);
		//		}

		return null;
	}

	@Override
	public EnteBean findById(String key) throws ServiceException {
		try{
			EnteCreditoreModel ente = this.anagraficaEjb.getCreditoreByIdFisico(key);

			if(ente != null){
				EnteBean bean = new EnteBean();
				bean.setId(0L);
				bean.setDTO(ente);

				return bean;
			}
		}catch(Exception e){
			this.log.error("Si e' verificato un errore durante la find dell'ente ["+key+"]: " +e.getMessage(), e);
		}

		return null;
	}
	
	@Override
	public EnteBean findByIdFiscale(String key) throws ServiceException {
		try{
			EnteCreditoreModel ente = this.anagraficaEjb.getCreditoreByIdLogico(key);

			if(ente != null){
				EnteBean bean = new EnteBean();
				bean.setId(0L);
				bean.setDTO(ente);

				return bean;
			}
		}catch(Exception e){
			this.log.error("Si e' verificato un errore durante la find dell'ente ["+key+"]: " +e.getMessage(), e);
		}

		return null;
	}

	@Override
	public List<EnteBean> findAll() throws ServiceException {
		List<EnteBean> lst = new ArrayList<EnteBean>();

		try{
			log.debug("findAll Enti in corso...");
			EnteCreditoreFilter filtro = getFiltro(null, null,this.form);
			lst = _findAll(filtro);
			log.debug("findAll Enti completata.");

		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAll Enti: "+ e.getMessage(), e);
		}

		return lst;
	}
	
	@Override
	public List<EnteBean> findAll(EnteSearchForm form) throws ServiceException {
		List<EnteBean> lst = new ArrayList<EnteBean>();

		try{
			log.debug("findAll Enti in corso...");
			EnteCreditoreFilter filtro = getFiltro(null, null,form);
			lst = _findAll(filtro);
			log.debug("findAll Enti completata.");

		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAll Enti: "+ e.getMessage(), e);
		}

		return lst;
	}
	
	private List<EnteBean> _findAll(EnteCreditoreFilter filtro) throws Exception {
		List<EnteBean> lst = new ArrayList<EnteBean>();
		
		List<EnteCreditoreModel> creditori = this.anagraficaEjb.findAllEntiCreditori(filtro);

		long id = 0;
		if(creditori != null && creditori.size() > 0 ){
			for (EnteCreditoreModel enteCreditoreModel : creditori) {
				EnteBean bean = new EnteBean();

				bean.setId(id++);
				bean.setDTO(enteCreditoreModel);

				lst.add(bean);
			}
		}
		
		return lst;
	}


	private EnteCreditoreFilter getFiltro(Integer start, Integer limit,EnteSearchForm form){
		EnteCreditoreFilter filtro = new EnteCreditoreFilter();

		filtro.setLimit(limit);
		filtro.setOffset(start);
		filtro.setOperatore(form.getLoggedUtente());

		return filtro;
	}
	
	@Override
	public List<TributoBean> getTributi(String idEnteCreditore, String idSistema) throws ServiceException {
		List<TributoBean> lista = new ArrayList<TributoBean>();
		
		try{
			List<TributoModel> lst = this.anagraficaEjb.getTributi(idEnteCreditore, idSistema);
			
			long id = 0;
			if(lst != null && lst.size() > 0){
				for (TributoModel tributoModel : lst) {
					TributoBean bean = new TributoBean();
					bean.setId(id++);
					bean.setDTO(tributoModel);
					lista.add(bean);
				}
			}
		}catch(Exception e){
			this.log.error("Si e' verificato un errore durante la getTributi IdEnteCreditore ["+idEnteCreditore
					+"] Id Sistema ["+idSistema+"]: " +e.getMessage(), e);
		}
		
		return lista;
	}

	@Override
	public TributoBean findTributoById(String idEnteCreditore, String key)
			throws ServiceException {
		try{
			TributoModel tributo = this.anagraficaEjb.getTributoById(idEnteCreditore, key);

			if(tributo != null){
				TributoBean bean = new TributoBean();
				bean.setId(0L);
				bean.setDTO(tributo);

				return bean;
			}
		}catch(Exception e){
			this.log.error("Si e' verificato un errore durante la find tributo ["+key+"]: " +e.getMessage(), e);
		}

		return null;
	}

	@Override
	public void storeTributo(String idEnteCreditore, String key, TributoBean obj) throws ServiceException {
		try{
			log.debug("Salvataggio Tributo ["+obj.getCodiceTributo().getValue()+"] in corso...");
			this.anagraficaEjb.salvaTributo(idEnteCreditore, key, obj.getDTO());
			log.debug("Salvataggio Tributo ["+obj.getCodiceTributo().getValue()+"] completato.");
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio Tributo ["+obj.getCodiceTributo().getValue()+"]: "+ e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@Override
	public DominioEnteModel getDominioEnte (String idEnteCreditore)throws ServiceException {
		DominioEnteModel dE = null;
		try{
			log.debug("GetDominioEnte ["+idEnteCreditore+"] in corso...");
			dE = anagraficaDominioEbj.getDominioEnte(idEnteCreditore);
			log.debug("GetDominioEnte ["+idEnteCreditore+"] completato.");
		}catch(Exception e){
			log.error("Si e' verificato un errore durante GetDominioEnte ["+idEnteCreditore+"]: "+ e.getMessage(), e);
			throw new ServiceException(e);
		}	

		return dE;
	}

}
