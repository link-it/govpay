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

import it.govpay.ejb.core.ejb.AnagraficaEJB;
import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.ScadenzarioModelId;
import it.govpay.ejb.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ejb.ndp.ejb.filter.ScadenzarioFilter;
import it.govpay.web.console.anagrafica.bean.ScadenzarioBean;
import it.govpay.web.console.anagrafica.form.ScadenzarioSearchForm;
import it.govpay.web.console.anagrafica.iservice.IScadenzarioService;
import it.govpay.web.console.anagrafica.model.ScadenzarioModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.service.BaseService;


@Named("scadenzarioService") @Singleton
public class ScadenzarioService extends BaseService<ScadenzarioSearchForm> implements IScadenzarioService,Serializable{

	private static final long serialVersionUID = 1L; 

	@Inject  
	private transient Logger log;

	@Inject
	AnagraficaDominioEJB anagraficaDominioEJB;

	@Inject
	AnagraficaEJB anagraficaEJB;
	
	@Override  @Inject @Named("scadenzarioSearchForm")
	public void setForm(ScadenzarioSearchForm form) {
		super.setForm(form);
	}

	@Override
	public List<ScadenzarioBean> findAll(int start, int limit)
			throws ServiceException {

		List<ScadenzarioBean> lst = new ArrayList<ScadenzarioBean>();

		try {
			log.debug("findAll Scadenzari Offset["+start+"] Limit["+limit+"] in corso...");

			lst = findAll(start, limit ,form);

			log.debug("findAll Scadenzari Offset["+start+"] Limit["+limit+"] completata.");
		} catch (Exception e) {
			log.error("Si e' verificato un errore durante la findAll Scadenzari Offset["+start+"] Limit["+limit+"]: "+ e.getMessage(), e);
		}


		return lst;
	}

	@Override
	public int totalCount() throws ServiceException {
		try{
			log.debug("Count Scadenzari in corso...");
			int cnt = this.anagraficaDominioEJB.countAllScadenzari(getFiltro(null,null,form));
			log.debug("Count Scadenzari completata trovati["+cnt+"].");
			return cnt;
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la count degli Scadenzari: "+ e.getMessage(), e);
		}

		return 0;
	}

	@Override
	public void store(ScadenzarioBean obj) throws ServiceException {
	}

	@Override
	public void store(ScadenzarioModelId key, ScadenzarioBean obj) throws ServiceException {
		try{
			log.debug("Salvataggio Scadenzario in corso... TODO!!!!!");
			
			// TODO GIULIANO
			ScadenzarioModel  scadenzario = obj.getDTO();
			String stazione = scadenzario.getIdStazione();
//			StazioneModel stazione = obj.getStazione();
//			if(stazione!= null)
//				stazione.setScadenzario(scadenzario);
			anagraficaDominioEJB.salvaScadenzario(key, scadenzario, stazione);

		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio dell'Scadenzario con chiave["+key+"]: "+ e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteById(Long key) throws ServiceException {
	}

	@Override
	public void delete(ScadenzarioBean obj) throws ServiceException {
	}

	@Override
	public ScadenzarioBean findById(ScadenzarioModelId key) throws ServiceException {
		try{
//			Map<it.govpay.ejb.core.model.ScadenzarioModel, StazioneModel> map = anagraficaDominioEJB.getScadenzarioById(key);
			it.govpay.ejb.core.model.ScadenzarioModel s = anagraficaDominioEJB.getScadenzarioById(key); 
//			StazioneModel st = null;
//			if(map != null){
//				Set<it.govpay.ejb.core.model.ScadenzarioModel> keySet = map.keySet();
//				if(keySet.size() != 1)
//					throw new ServiceException("Il numero di risultati trovati e' diverso da 1");
//
//
//				for (it.govpay.ejb.core.model.ScadenzarioModel scadenzarioModel : keySet) {
//					s = scadenzarioModel;
//					st = map.get(s);
//				}
//			}

			if(s != null){
				ScadenzarioBean bean = new ScadenzarioBean();
				ScadenzarioModel sm = new ScadenzarioModel(s);
				EnteCreditoreModel enteCreditore = anagraficaEJB.getCreditoreByIdFisico(sm.getIdEnte());
				sm.setDenominazioneEnte(enteCreditore.getDenominazione());
				bean.setDTO(sm);
//				bean.setStazione(st); 
				return bean;
			}
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la find Scadenzario con chiave["+key+"]: "+ e.getMessage(), e);
		}

		return null;
	}

	@Override
	public ScadenzarioBean findById(Long key) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScadenzarioBean> findAll() throws ServiceException {

		log.debug("findAll Scadenzari in corso...");
		List<ScadenzarioBean> lst = new ArrayList<ScadenzarioBean>();

		try {
			lst = findAll(null,null,form);
		} catch (Exception e) {
			log.error("Si e' verificato un errore durante la findAll Scadenzari: "+ e.getMessage(), e);
		}
		log.debug("findAll Scadenzari completata.");
		return lst;

	}


	@Override
	public List<ScadenzarioBean> findAll(
			ScadenzarioSearchForm scadForm) throws ServiceException {
		log.debug("findAll Scadenzari in corso...");
		List<ScadenzarioBean> lst = new ArrayList<ScadenzarioBean>();

		try {
			lst = findAll(null,null,scadForm);
		} catch (Exception e) {
			log.error("Si e' verificato un errore durante la findAll Scadenzari: "+ e.getMessage(), e);
		}
		log.debug("findAll Scadenzari completata.");
		return lst;
	}


	private List<ScadenzarioBean> findAll(Integer start, Integer limit, ScadenzarioSearchForm scadForm) throws Exception {
		List<ScadenzarioBean> lst = new ArrayList<ScadenzarioBean>();

//		List<Map<it.govpay.ejb.core.model.ScadenzarioModel, StazioneModel>> scadenzariList = this.anagraficaDominioEJB.findAllScadenzari(getFiltro(start,limit,scadForm));
		
		List<it.govpay.ejb.core.model.ScadenzarioModel> scadenzariList = this.anagraficaDominioEJB.findAllScadenzari(getFiltro(start,limit,scadForm));

		if(scadenzariList != null && scadenzariList.size() > 0 ){
			
			int s = 0;
//			it.govpay.ejb.core.model.ScadenzarioModel sc = null;
//			StazioneModel st = null;
			for( it.govpay.ejb.core.model.ScadenzarioModel sc : scadenzariList) {
//				for (Map<it.govpay.ejb.core.model.ScadenzarioModel, StazioneModel> map : scadenzariList) {
				
				ScadenzarioBean bean = new ScadenzarioBean();
				ScadenzarioModel sm = new ScadenzarioModel(sc);
				EnteCreditoreModel enteCreditore = anagraficaEJB.getCreditoreByIdFisico(sm.getIdEnte());
				sm.setDenominazioneEnte(enteCreditore.getDenominazione());
				bean.setDTO(sm);
				bean.setId((long) s ++);
				lst.add(bean);
				
//				Set<it.govpay.ejb.core.model.ScadenzarioModel> keySet = map.keySet();
//				if(keySet.size() != 1)
//					throw new ServiceException("Il numero di risultati trovati e' diverso da 1");
//
//
//				for (it.govpay.ejb.core.model.ScadenzarioModel scadenzarioModel : keySet) {
//					sc = scadenzarioModel;
//					st = map.get(sc);
//				}
//
//				if(sc != null){
//					ScadenzarioBean bean = new ScadenzarioBean();
//					ScadenzarioModel sm = new ScadenzarioModel(sc);
//					EnteCreditoreModel enteCreditore = anagraficaEJB.getCreditoreByIdFisico(sm.getIdEnte());
//					sm.setDenominazioneEnte(enteCreditore.getDenominazione());
//					bean.setDTO(sm);
//					bean.setStazione(st); 
//					bean.setId((long) s ++);
//					lst.add(bean);
//				}
			}
		}

		return lst;
	}


	private ScadenzarioFilter getFiltro(Integer start, Integer limit, ScadenzarioSearchForm form){
		ScadenzarioFilter filtro = new ScadenzarioFilter();

		filtro.setLimit(limit);
		filtro.setOffset(start);
		
		if(form.getIdEnte().getValue() != null)
			filtro.setIdEnteCreditore( form.getIdEnte().getValue());
		
		if(form.getIdIntermediario().getValue() != null)
			filtro.setIdIntermediarioPA( form.getIdIntermediario().getValue());

		return filtro;
	}

	@Override
	public boolean exists(ScadenzarioBean arg0) throws ServiceException {
		return false;
	}

}
