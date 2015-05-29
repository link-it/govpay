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

import it.govpay.ejb.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ejb.ndp.ejb.filter.IntermediarioFilter;
import it.govpay.ejb.ndp.model.IntermediarioModel;
import it.govpay.ejb.ndp.model.StazioneModel;
import it.govpay.web.console.anagrafica.bean.IntermediarioNdpBean;
import it.govpay.web.console.anagrafica.bean.StazioneBean;
import it.govpay.web.console.anagrafica.form.IntermediarioNdpSearchForm;
import it.govpay.web.console.anagrafica.iservice.IIntermediarioNdpService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.service.BaseService;


@Named("intNdpService") @Singleton
public class IntermediarioNdpService extends BaseService<IntermediarioNdpSearchForm> implements IIntermediarioNdpService,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	@Inject  
	private transient Logger log;

	@Inject
	AnagraficaDominioEJB anagraficaDominioEJB;


	@Override  @Inject @Named("intNdpSearchForm")
	public void setForm(IntermediarioNdpSearchForm form) {
		super.setForm(form);
	}

	@Override
	public List<IntermediarioNdpBean> findAll(int start, int limit)
			throws ServiceException {
		List<IntermediarioNdpBean> lst = new ArrayList<IntermediarioNdpBean>();

		try{
			log.debug("findAll Intermediari Ndp Offset["+start+"] Limit["+limit+"] in corso...");
			IntermediarioFilter filtro = getFiltro(start,limit, this.form);
			lst = _findAll(filtro);
			log.debug("findAll Intermediari Ndp Offset["+start+"] Limit["+limit+"] completata.");
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAll Intermediari Ndp Offset["+start+"] Limit["+limit+"]: "+ e.getMessage(), e);
		}

		return lst;
	}

	@Override
	public int totalCount() throws ServiceException {
		try{
			log.debug("Count Intermediari Ndp in corso...");
			int cnt = this.anagraficaDominioEJB.countAllIntermediari(getFiltro(null,null, this.form));
			log.debug("Count Intermediari Ndp completata trovati["+cnt+"].");
			return cnt;
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la count degli Intermediari Ndp: "+ e.getMessage(), e);
		}

		return 0;
	}

	@Override
	public void store(IntermediarioNdpBean obj) throws ServiceException {
	}

	@Override
	public void store(String key,IntermediarioNdpBean obj) throws ServiceException {
		try{
			log.debug("Salvataggio Intermediario Ndp in corso...");
			IntermediarioModel  intermediario = obj.getDTO();
			anagraficaDominioEJB.salvaIntermediario(key, intermediario);
			log.debug("Salvataggio Intermediario Ndp completato.");
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio dell'Intermediario Ndp con chiave["+key+"]: "+ e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteById(Long key) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(IntermediarioNdpBean obj) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public IntermediarioNdpBean findById(String key) throws ServiceException {
		try{
			IntermediarioModel  intermediarioModel = anagraficaDominioEJB.getIntermediarioById(key);

			if(intermediarioModel != null){
				IntermediarioNdpBean bean = new IntermediarioNdpBean();
				bean.setDTO(intermediarioModel);
				return bean;
			}
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la find Intermediario Ndp con chiave["+key+"]: "+ e.getMessage(), e);
		}

		return null;
	}

	@Override
	public IntermediarioNdpBean findById(Long key) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IntermediarioNdpBean> findAll() throws ServiceException {
		List<IntermediarioNdpBean> lst = new ArrayList<IntermediarioNdpBean>();

		try{
			log.debug("findAll Intermediari Ndp in corso...");
			IntermediarioFilter filtro = getFiltro(null,null, this.form);
			lst =_findAll(filtro);
			log.debug("findAll Intermediari Ndp completata.");

		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAll Intermediari Ndp: "+ e.getMessage(), e);
		}

		return lst;
	}

	private List<IntermediarioNdpBean> _findAll(IntermediarioFilter filtro) throws Exception{
		List<IntermediarioNdpBean> lst = new ArrayList<IntermediarioNdpBean>();
		List<IntermediarioModel> intermediari = this.anagraficaDominioEJB.findAllIntermediari(filtro);

		if(intermediari != null && intermediari.size() > 0 ){
			int s = 0;
			for (IntermediarioModel interm : intermediari) {
				IntermediarioNdpBean bean = new IntermediarioNdpBean();
				bean.setId((long) s ++);
				bean.setDTO(interm);

				lst.add(bean);
			}
		}
		return lst;
	}

	@Override
	public List<IntermediarioNdpBean> findAll(IntermediarioNdpSearchForm form)
			throws ServiceException {
		List<IntermediarioNdpBean> lst = new ArrayList<IntermediarioNdpBean>();

		try{
			log.debug("findAll Intermediari Ndp in corso...");
			IntermediarioFilter filtro = getFiltro(null,null, form);
			lst =_findAll(filtro);
			log.debug("findAll Intermediari Ndp completata.");

		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAll Intermediari Ndp: "+ e.getMessage(), e);
		}

		return lst;
	}

	private IntermediarioFilter getFiltro(Integer start, Integer limit, IntermediarioNdpSearchForm form){
		IntermediarioFilter filtro = new IntermediarioFilter();

		filtro.setLimit(limit);
		filtro.setOffset(start);

		return filtro;
	}

	@Override
	public List<StazioneBean> findAllStazioni(String idIntermediarioPA) throws ServiceException {
		List<StazioneBean> lst = new ArrayList<StazioneBean>();

		try{
			log.debug("findAllStazioni in corso...");
			List<StazioneModel> stazioniIntermediario = this.anagraficaDominioEJB.getStazioniIntermediario(idIntermediarioPA);

			if(stazioniIntermediario != null && stazioniIntermediario.size() > 0){
				for (StazioneModel stazioneModel : stazioniIntermediario) {
					StazioneBean bean = new StazioneBean();
					bean.setDTO(stazioneModel);

					lst.add(bean);
				}
			}

			log.debug("findAllStazioni completata.");
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAllStazioni: "+ e.getMessage(), e);
		}

		return lst;
	}

	@Override
	public StazioneBean findStazioneById(String idStazione)
			throws ServiceException {
		StazioneBean bean = null;
		try{
			log.debug("findStazioneById ["+idStazione+"] in corso...");
			StazioneModel stazioneModel = this.anagraficaDominioEJB.findStazioneById(idStazione);


			if(stazioneModel != null){
				bean = new StazioneBean();
				bean.setDTO(stazioneModel);

			}

			log.debug("findStazioneById ["+idStazione+"] completata.");
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findStazioneById ["+idStazione+"]: "+ e.getMessage(), e);
		}
		return bean;
	}

	@Override
	public void salvaStazione(StazioneBean stazione, boolean isAdd)
			throws ServiceException {
		try{
			log.debug("Salvataggio Stazione in corso...");
			StazioneModel stazioneModel  = stazione.getDTO();
			anagraficaDominioEJB.salvaStazione(stazioneModel, isAdd); 
			log.debug("Salvataggio Stazione completato.");
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio della stazione: "+ e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

}
