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
package it.govpay.web.console.anagrafica.datamodel;

import it.govpay.ejb.model.ScadenzarioModelId;
import it.govpay.web.console.anagrafica.bean.ScadenzarioBean;
import it.govpay.web.console.anagrafica.iservice.IScadenzarioService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.datamodel.BaseDataModel;

@Named("scadenzarioDM") @RequestScoped 
public class ScadenzarioDM extends BaseDataModel<Long, ScadenzarioBean, IScadenzarioService> { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Long,ScadenzarioModelId> keyconverter = new HashMap<Long,ScadenzarioModelId>();
	
	@Inject  
    private transient Logger log;
	
	
	@Override @Inject @Named("scadenzarioService")
	public void setDataProvider(IScadenzarioService dataProvider) {
			super.setDataProvider(dataProvider);
	}
	
	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {
		try{
			if(detached){
				for (Long  key : this.wrappedKeys) {
					setRowKey(key);
					visitor.process(context, key, argument);
				}
			}else{
				int start = 0; int limit = 0;
				// ripristino la ricerca.
				if(this.getDataProvider().getForm().isRestoreSearch()){
					start = this.getDataProvider().getForm().getStart();
					limit = this.getDataProvider().getForm().getLimit();
					this.getDataProvider().getForm().setRestoreSearch(false);
					
					int pageIndex = (start / limit) + 1;
					this.getDataProvider().getForm().setPageIndex(pageIndex);
					this.getDataProvider().getForm().setCurrentPage(pageIndex);
					// Aggiorno valori paginazione
					range = new SequenceRange(start,limit);
				}
				else{
					  start = ((SequenceRange)range).getFirstRow();
					  limit = ((SequenceRange)range).getRows();
				}
				
				log.debug("Richiesti Record S["+start+"] L["+limit+"], FiltroPagina ["+this.getDataProvider().getForm().getCurrentPage()+"]"); 

				this.getDataProvider().getForm().setStart(start);
				this.getDataProvider().getForm().setLimit(limit); 
				
				this.wrappedKeys = new ArrayList<Long>();
				this.keyconverter = new HashMap<Long, ScadenzarioModelId>();
				List<ScadenzarioBean> list = this.getDataProvider().findAll(start, limit);
				for (ScadenzarioBean intermediario : list) {
					String idSystem = intermediario.getDTO().getIdSystem();
					String idEnte = intermediario.getDTO().getIdEnte();

					ScadenzarioModelId seId = new ScadenzarioModelId(); 
					seId.setIdEnte(idEnte);
					seId.setIdSystem(idSystem);
					
					Long id = intermediario.getId();
					
					this.keyconverter.put(id, seId);
					this.wrappedData.put(id, intermediario);
					this.wrappedKeys.add(id);
					visitor.process(context, id, argument);
				}
			}
		}catch (Exception e) {
			log.error(e,e);
		}
	}

	@Override
	public int getRowCount() {
		if(this.rowCount==null){
			if(this.getDataProvider().getForm().isNewSearch()){
				try {
					this.rowCount = this.getDataProvider().totalCount();
					this.getDataProvider().getForm().setTotalCount(this.rowCount);
				} catch (ServiceException e) {
					this.getDataProvider().getForm().setTotalCount(0);
					return 0;
				} 
				this.getDataProvider().getForm().setNewSearch(false); 
			}
			else {
				this.rowCount = this.getDataProvider().getForm().getTotalCount();
			}
		}
		return this.rowCount;
	}

	@Override
	public Object getRowData() {
		if(this.currentPk==null)
			return null;
		else{
			ScadenzarioBean t = this.wrappedData.get(this.currentPk);
			if(t==null){
				try {
					ScadenzarioModelId key = this.keyconverter.get(this.currentPk);
					t=this.getDataProvider().findById(key);
					this.wrappedData.put(this.currentPk, t);
				} catch (ServiceException e) {}
			}
			return t;
		}
	}
}
